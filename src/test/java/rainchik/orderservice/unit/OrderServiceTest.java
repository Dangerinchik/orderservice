package rainchik.orderservice.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rainchik.orderservice.dto.*;
import rainchik.orderservice.entity.Item;
import rainchik.orderservice.entity.Order;
import rainchik.orderservice.entity.OrderItem;
import rainchik.orderservice.exception.ItemDoesNotExistException;
import rainchik.orderservice.exception.ListOfOrdersIsEmpty;
import rainchik.orderservice.exception.OrderDoesNotExistException;
import rainchik.orderservice.mapper.OrderMapper;
import rainchik.orderservice.repository.OrderRepository;
import rainchik.orderservice.service.ItemService;
import rainchik.orderservice.service.UserService;
import rainchik.orderservice.service.impl.OrderServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class OrderServiceTest {
    private OrderRepository orderRepository;
    private OrderMapper orderMapper;
    private UserService userService;
    private ItemService itemService;
    private OrderServiceImpl orderService;

    private Order order;
    private OrderDTO orderDTO;
    private OrderResponseDTO orderResponseDTO;
    private UserResponseDTO userResponseDTO;
    private Item item;

    @BeforeEach
    void setUp() {

        orderRepository = mock(OrderRepository.class);
        orderMapper = mock(OrderMapper.class);
        userService = mock(UserService.class);
        itemService = mock(ItemService.class);

        orderService = new OrderServiceImpl(orderRepository, orderMapper, userService, itemService);

        order = new Order();
        order.setId(1L);
        order.setUserId(1L);

        orderDTO = new OrderDTO();
        orderDTO.setUserEmail("test@example.com");

        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setItemId(1L);
        orderItemDTO.setQuantity(2);
        orderDTO.setOrderItems(List.of(orderItemDTO));

        orderResponseDTO = new OrderResponseDTO();
        orderResponseDTO.setId(1L);
        orderResponseDTO.setUserId(1L);

        userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(1L);

        item = new Item();
        item.setId(1L);
        item.setName("Test Item");
        item.setPrice("100.0");
    }

    @Test
    void createOrder_ShouldCreateOrder_WhenValidData() throws ItemDoesNotExistException {
        // Arrange
        when(userService.getUserByEmail("test@example.com")).thenReturn(userResponseDTO);
        when(orderMapper.toOrder(orderDTO)).thenReturn(order);
        when(itemService.getItemByIdForService(1L)).thenReturn(item);
        when(orderRepository.save(order)).thenReturn(order);
        when(orderMapper.toOrderResponseDTO(order)).thenReturn(orderResponseDTO);

        // Act
        OrderResponseDTO result = orderService.createOrder(orderDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(userService).getUserByEmail("test@example.com");
        verify(itemService).getItemByIdForService(1L);
        verify(orderRepository).save(order);
    }

    @Test
    void createOrder_ShouldThrowException_WhenItemDoesNotExist() throws ItemDoesNotExistException {
        // Arrange
        when(userService.getUserByEmail("test@example.com")).thenReturn(userResponseDTO);
        when(orderMapper.toOrder(orderDTO)).thenReturn(order);
        when(itemService.getItemByIdForService(1L)).thenThrow(ItemDoesNotExistException.class);

        // Act & Assert
        assertThrows(ItemDoesNotExistException.class, () -> orderService.createOrder(orderDTO));
    }

    @Test
    void updateOrder_ShouldUpdateOrder_WhenOrderExists() throws OrderDoesNotExistException {
        // Arrange
        when(orderRepository.existsById(1L)).thenReturn(true);
        when(orderMapper.toOrder(orderDTO)).thenReturn(order);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderMapper.toOrderResponseDTO(order)).thenReturn(orderResponseDTO);

        // Act
        OrderResponseDTO result = orderService.updateOrder(1L, orderDTO);

        // Assert
        assertNotNull(result);
        verify(orderRepository).updateOrder(eq(1L), anyLong(), any(String.class));
        verify(orderRepository).findById(1L);
    }

    @Test
    void updateOrder_ShouldThrowException_WhenOrderDoesNotExist() {
        // Arrange
        when(orderRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        assertThrows(OrderDoesNotExistException.class, () -> orderService.updateOrder(1L, orderDTO));
    }

    @Test
    void deleteOrder_ShouldDeleteOrder_WhenOrderExists() throws OrderDoesNotExistException {
        // Arrange
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        // Act
        String result = orderService.deleteOrder(1L);

        // Assert
        assertEquals("Order deleted", result);
        verify(orderRepository).deleteOrder(1L);
    }

    @Test
    void deleteOrder_ShouldThrowException_WhenOrderDoesNotExist() {
        // Arrange
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(OrderDoesNotExistException.class, () -> orderService.deleteOrder(1L));
    }

    @Test
    void getOrder_ShouldReturnOrder_WhenOrderExists() throws OrderDoesNotExistException {
        // Arrange
        when(orderRepository.findByOrderId(1L)).thenReturn(Optional.of(order));
        when(orderMapper.toOrderResponseDTO(order)).thenReturn(orderResponseDTO);

        // Act
        OrderResponseDTO result = orderService.getOrder(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(orderRepository).findByOrderId(1L);
    }

    @Test
    void getOrder_ShouldThrowException_WhenOrderDoesNotExist() {
        // Arrange
        when(orderRepository.findByOrderId(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(OrderDoesNotExistException.class, () -> orderService.getOrder(1L));
    }

    @Test
    void getOrdersInList_ShouldReturnOrders_WhenOrdersExist() throws ListOfOrdersIsEmpty {
        // Arrange
        List<Long> orderIds = Arrays.asList(1L, 2L);
        List<Order> orders = Arrays.asList(order);
        when(orderRepository.findByIdIn(orderIds)).thenReturn(orders);
        when(orderMapper.toOrderResponseDTO(order)).thenReturn(orderResponseDTO);

        // Act
        List<OrderResponseDTO> result = orderService.getOrdersInList(orderIds);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(orderRepository).findByIdIn(orderIds);
    }

    @Test
    void getOrdersInList_ShouldThrowException_WhenNoOrdersFound() {
        // Arrange
        List<Long> orderIds = Arrays.asList(1L, 2L);
        when(orderRepository.findByIdIn(orderIds)).thenReturn(List.of());

        // Act & Assert
        assertThrows(ListOfOrdersIsEmpty.class, () -> orderService.getOrdersInList(orderIds));
    }

    @Test
    void getOrdersInStatusList_ShouldReturnOrders_WhenOrdersExist() throws ListOfOrdersIsEmpty {
        // Arrange
        List<String> statuses = Arrays.asList("PENDING", "COMPLETED");
        List<Order> orders = Arrays.asList(order);
        when(orderRepository.findByStatuses(statuses)).thenReturn(orders);
        when(orderMapper.toOrderResponseDTO(order)).thenReturn(orderResponseDTO);

        // Act
        List<OrderResponseDTO> result = orderService.getOrdersInStatusList(statuses);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(orderRepository).findByStatuses(statuses);
    }

    @Test
    void getOrdersInStatusList_ShouldThrowException_WhenNoOrdersFound() {
        // Arrange
        List<String> statuses = Arrays.asList("PENDING", "COMPLETED");
        when(orderRepository.findByStatuses(statuses)).thenReturn(List.of());

        // Act & Assert
        assertThrows(ListOfOrdersIsEmpty.class, () -> orderService.getOrdersInStatusList(statuses));
    }
}
