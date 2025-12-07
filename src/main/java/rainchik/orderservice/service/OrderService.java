package rainchik.orderservice.service;

import rainchik.orderservice.dto.OrderDTO;
import rainchik.orderservice.dto.OrderResponseDTO;
import rainchik.orderservice.entity.Status;
import rainchik.orderservice.exception.ItemDoesNotExistException;
import rainchik.orderservice.exception.ListOfOrdersIsEmpty;
import rainchik.orderservice.exception.OrderDoesNotExistException;

import java.util.List;

public interface OrderService {

    public OrderResponseDTO createOrder(OrderDTO order) throws ItemDoesNotExistException;
    public OrderResponseDTO updateOrder(Long orderId, OrderDTO order) throws OrderDoesNotExistException;
    public String deleteOrder(Long orderId) throws OrderDoesNotExistException;
    public OrderResponseDTO getOrder(Long orderId) throws OrderDoesNotExistException;
    public List<OrderResponseDTO> getOrdersInList(List<Long> orderIds) throws ListOfOrdersIsEmpty;
    public List<OrderResponseDTO> getOrdersInStatusList(List<String> statuses) throws ListOfOrdersIsEmpty;

}
