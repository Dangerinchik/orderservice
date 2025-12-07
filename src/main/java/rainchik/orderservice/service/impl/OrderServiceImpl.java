package rainchik.orderservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rainchik.orderservice.dto.*;
import rainchik.orderservice.entity.Item;
import rainchik.orderservice.entity.Order;
import rainchik.orderservice.entity.OrderItem;
import rainchik.orderservice.entity.Status;
import rainchik.orderservice.exception.ItemDoesNotExistException;
import rainchik.orderservice.exception.ListOfOrdersIsEmpty;
import rainchik.orderservice.exception.OrderDoesNotExistException;
import rainchik.orderservice.mapper.ItemMapper;
import rainchik.orderservice.mapper.OrderMapper;
import rainchik.orderservice.repository.OrderRepository;
import rainchik.orderservice.service.ItemService;
import rainchik.orderservice.service.OrderService;
import rainchik.orderservice.service.UserService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final ItemService itemService;
    private OrderRepository orderRepository;
    private OrderMapper orderMapper;
    private UserService userService;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, OrderMapper orderMapper, UserService userService, ItemService itemService, KafkaTemplate<String, Object> kafkaTemplate) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.userService = userService;
        this.itemService = itemService;
        this.kafkaTemplate = kafkaTemplate;
    }


    @Override
    @Transactional
    public OrderResponseDTO createOrder(OrderDTO order) throws ItemDoesNotExistException {
        UserResponseDTO userResponseDTO = userService.getUserByEmail(order.getUserEmail());
        Order orderToSave = orderMapper.toOrder(order);
        orderToSave.setUserId(userResponseDTO.getId());
        for(OrderItemDTO orderItemDTO : order.getOrderItems()) {
            Item item = itemService.getItemByIdForService(orderItemDTO.getItemId());

            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setOrder(orderToSave);
            orderItem.setQuantity(orderItemDTO.getQuantity());

            orderToSave.getOrderItems().add(orderItem);
        }

        return orderMapper.toOrderResponseDTO(orderRepository.save(orderToSave));
    }

    @Override
    @Transactional
    public OrderResponseDTO updateOrder(Long orderId, OrderDTO order) throws OrderDoesNotExistException {
        if(orderRepository.existsById(orderId)) {
            UserResponseDTO userResponseDTO = userService.getUserByEmail(order.getUserEmail());
            Long userId = userResponseDTO.getId();

            orderRepository.updateOrder(orderId, userId, orderMapper.toOrder(order).getStatus());
            Order updatedOrder = orderRepository.findById(orderId).get();

            if(Objects.equals(order.getStatus(), Status.CONFIRMED.name())){
                updatedOrder = confirmOrder(orderId, updatedOrder, userId);

            }

            return orderMapper.toOrderResponseDTO(updatedOrder);
        }
        else {
            throw new OrderDoesNotExistException(orderId);
        }
    }

    public OrderResponseDTO updateOrderStatus(Long orderId, OrderDTO order) throws OrderDoesNotExistException {
        if(orderRepository.existsById(orderId)) {
            orderRepository.updateOrderStatus(orderId, order.getStatus());

            return orderMapper.toOrderResponseDTO(orderRepository.findById(orderId).get());
        }
        else{
            throw new OrderDoesNotExistException(orderId);
        }
    }

    @Override
    @Transactional
    public String deleteOrder(Long orderId) throws OrderDoesNotExistException {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent()) {
            orderRepository.deleteOrder(orderId);
        }
        else {
            throw new OrderDoesNotExistException(orderId);
        }
        return "Order deleted";
    }

    @Override
    public OrderResponseDTO getOrder(Long orderId) throws OrderDoesNotExistException {
        Optional<Order> order = orderRepository.findByOrderId(orderId);
        if (order.isPresent()) {
            return orderMapper.toOrderResponseDTO(order.get());
        }
        else{
            throw new OrderDoesNotExistException(orderId);
        }
    }

    @Override
    public List<OrderResponseDTO> getOrdersInList(List<Long> orderIds) throws ListOfOrdersIsEmpty {
        List<Order> orders = orderRepository.findByIdIn(orderIds);
        if(orders.isEmpty()){
            throw new ListOfOrdersIsEmpty();
        }
        else{
            List<OrderResponseDTO> orderResponseDTOS = new ArrayList<>();
            for(Order order : orders){
                orderResponseDTOS.add(orderMapper.toOrderResponseDTO(order));
            }
            return orderResponseDTOS;
        }
    }

    @Override
    public List<OrderResponseDTO> getOrdersInStatusList(List<String> statuses) throws ListOfOrdersIsEmpty {
        List<Order> orders = orderRepository.findByStatuses(statuses);
        if(orders.isEmpty()){
            throw new ListOfOrdersIsEmpty(statuses);
        }
        else{
            List<OrderResponseDTO> orderResponseDTOS = new ArrayList<>();
            for(Order order : orders){
                orderResponseDTOS.add(orderMapper.toOrderResponseDTO(order));
            }
            return orderResponseDTOS;
        }
    }

    private Order confirmOrder(Long orderId, Order updatedOrder, Long userId) {
        Message<OrderKafkaDTO> message = MessageBuilder
                .withPayload(prepareOrderKafkaDTO(updatedOrder))
                .build();
        kafkaTemplate.send(message);
        updatedOrder.setStatus(Status.PROCESSING.name());
        orderRepository.updateOrder(orderId, userId, updatedOrder.getStatus());
        updatedOrder = orderRepository.findById(orderId).get();
        return updatedOrder;
    }

    private OrderKafkaDTO prepareOrderKafkaDTO(Order order){
        OrderKafkaDTO orderKafkaDTO = new OrderKafkaDTO();
        orderKafkaDTO.setOrderId(order.getId());
        orderKafkaDTO.setUserId(order.getUserId());
        BigDecimal amount = new BigDecimal(0);
        for(OrderItem item : order.getOrderItems()){
            amount.add(BigDecimal.valueOf(
                    (long) item.getQuantity() * Integer.parseInt(item.getItem().getPrice())
            ));
        }
        orderKafkaDTO.setAmount(amount);
        return orderKafkaDTO;
    }
}
