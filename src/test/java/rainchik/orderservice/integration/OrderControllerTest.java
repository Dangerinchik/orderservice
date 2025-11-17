package rainchik.orderservice.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import rainchik.orderservice.config.TestcontainersConfiguration;
import rainchik.orderservice.dto.OrderDTO;
import rainchik.orderservice.dto.OrderItemDTO;
import rainchik.orderservice.entity.Item;
import rainchik.orderservice.entity.Order;
import rainchik.orderservice.entity.Status;
import rainchik.orderservice.repository.ItemRepository;
import rainchik.orderservice.repository.OrderRepository;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@TestPropertySource(properties = {
        "user-service.base-url=http://localhost:8081"
})
@Import(TestcontainersConfiguration.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Item testItem;
    private Order testOrder;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
        itemRepository.deleteAll();

        testItem = new Item();
        testItem.setName("Test Item");
        testItem.setPrice("19.99");
        testItem = itemRepository.save(testItem);

        testOrder = new Order();
        testOrder.setUserId(1L);
        testOrder.setStatus(String.valueOf(Status.PENDING));
        testOrder = orderRepository.save(testOrder);
    }

    @Test
    void getOrder_ShouldReturnOrder_WhenOrderExists() throws Exception {
        mockMvc.perform(get("/order/{id}", testOrder.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testOrder.getId()))
                .andExpect(jsonPath("$.userId").value(1L)) // исправлено: userId вместо userEmail
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void getOrder_ShouldReturn404_WhenOrderDoesNotExist() throws Exception {
        mockMvc.perform(get("/order/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists()); // исправлено: объект с полем message
    }

    @Test
    void getOrdersInList_ShouldReturnOrders_WhenOrdersExist() throws Exception {
        Order secondOrder = new Order();
        secondOrder.setUserId(1L);
        secondOrder.setStatus(String.valueOf(Status.COMPLETED));
        secondOrder = orderRepository.save(secondOrder);

        List<Long> orderIds = Arrays.asList(testOrder.getId(), secondOrder.getId());

        mockMvc.perform(get("/order/list")
                        .param("ids", orderIds.get(0).toString(), orderIds.get(1).toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(testOrder.getId()))
                .andExpect(jsonPath("$[1].id").value(secondOrder.getId()));
    }

    @Test
    void getOrdersInList_ShouldReturn404_WhenNoOrdersFound() throws Exception {
        mockMvc.perform(get("/order/list")
                        .param("ids", "999", "1000"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void getOrdersInStatusList_ShouldReturnOrders_WhenOrdersWithStatusExist() throws Exception {
        Order completedOrder = new Order();
        completedOrder.setUserId(1L);
        completedOrder.setStatus(String.valueOf(Status.COMPLETED));
        orderRepository.save(completedOrder);

        mockMvc.perform(get("/order/list/status")
                        .param("statuses", "PENDING", "COMPLETED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[?(@.status == 'PENDING')]").exists())
                .andExpect(jsonPath("$[?(@.status == 'COMPLETED')]").exists());
    }

    @Disabled("Будет учитываться при запуске всех микросервисов")
    @Test
    void createOrder_ShouldCreateOrder_WhenValidRequest() throws Exception {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setUserEmail("newuser@example.com");
        orderDTO.setStatus(String.valueOf(Status.PENDING));

        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setItemId(testItem.getId());
        orderItemDTO.setQuantity(2);
        orderDTO.setOrderItems(Arrays.asList(orderItemDTO));

        mockMvc.perform(post("/order/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").exists()) // исправлено: userId вместо userEmail
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void createOrder_ShouldReturn400_WhenInvalidRequest() throws Exception {
        OrderDTO invalidOrderDTO = new OrderDTO(); // userEmail is null

        mockMvc.perform(post("/order/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidOrderDTO)))
                .andExpect(status().isBadRequest()) // теперь будет 400 вместо 500
                .andExpect(jsonPath("$.message").exists());
    }

    @Disabled("Будет учитываться при запуске всех микросервисов")
    @Test
    void updateOrder_ShouldUpdateOrder_WhenValidRequest() throws Exception {
        OrderDTO updateDTO = new OrderDTO();
        updateDTO.setUserEmail("updated@example.com");
        updateDTO.setStatus(String.valueOf(Status.COMPLETED));

        // Добавляем обязательные orderItems
        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setItemId(testItem.getId());
        orderItemDTO.setQuantity(1);
        updateDTO.setOrderItems(Arrays.asList(orderItemDTO));

        mockMvc.perform(put("/order/update/{id}", testOrder.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testOrder.getId()))
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    @Test
    void updateOrder_ShouldReturn404_WhenOrderDoesNotExist() throws Exception {
        OrderDTO updateDTO = new OrderDTO();
        updateDTO.setUserEmail("updated@example.com");
        updateDTO.setStatus(String.valueOf(Status.COMPLETED));

        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setItemId(testItem.getId());
        orderItemDTO.setQuantity(1);
        updateDTO.setOrderItems(Arrays.asList(orderItemDTO));

        mockMvc.perform(put("/order/update/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void deleteOrder_ShouldDeleteOrder_WhenOrderExists() throws Exception {
        mockMvc.perform(delete("/order/delete/{id}", testOrder.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("Order deleted")); // исправлено: фактическое сообщение

        // Проверяем что заказ действительно удален
        mockMvc.perform(get("/order/{id}", testOrder.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteOrder_ShouldReturn404_WhenOrderDoesNotExist() throws Exception {
        mockMvc.perform(delete("/order/delete/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }
}