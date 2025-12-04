package rainchik.orderservice.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import rainchik.orderservice.config.TestcontainersConfiguration;
import rainchik.orderservice.dto.ItemDTO;
import rainchik.orderservice.entity.Item;
import rainchik.orderservice.repository.ItemRepository;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Import(TestcontainersConfiguration.class)
public class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Item testItem;

    @BeforeEach
    void setUp() {
        itemRepository.deleteAll();

        testItem = new Item();
        testItem.setName("Test Item");
        testItem.setPrice("19.99");
        testItem = itemRepository.save(testItem);
    }

    @Test
    void getItem_ShouldReturnItem_WhenItemExists() throws Exception {
        mockMvc.perform(get("/item/id/{id}", testItem.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testItem.getId()))
                .andExpect(jsonPath("$.name").value("Test Item"))
                .andExpect(jsonPath("$.price").value("19.99"));
    }

    @Test
    void getItem_ShouldReturn404_WhenItemDoesNotExist() throws Exception {
        mockMvc.perform(get("/item/id/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void getItemByName_ShouldReturnItems_WhenItemsWithNameExist() throws Exception {
        // Создаем второй товар с таким же именем
        Item secondItem = new Item();
        secondItem.setName("Test Item");
        secondItem.setPrice("29.99");
        itemRepository.save(secondItem);

        mockMvc.perform(get("/item/name/{name}", "Test Item"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Test Item"))
                .andExpect(jsonPath("$[1].name").value("Test Item"))
                .andExpect(jsonPath("$[?(@.price == '19.99')]").exists())
                .andExpect(jsonPath("$[?(@.price == '29.99')]").exists());
    }

    @Test
    void getItemByName_ShouldReturn404_WhenNoItemsWithNameFound() throws Exception {
        mockMvc.perform(get("/item/name/{name}", "Nonexistent Item"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void getAllItems_ShouldReturnAllItems_WhenItemsExist() throws Exception {
        // Создаем второй товар
        Item secondItem = new Item();
        secondItem.setName("Second Item");
        secondItem.setPrice("29.99");
        itemRepository.save(secondItem);

        mockMvc.perform(get("/item/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[?(@.name == 'Test Item')]").exists())
                .andExpect(jsonPath("$[?(@.name == 'Second Item')]").exists());
    }

    @Test
    void getAllItems_ShouldReturn404_WhenNoItemsExist() throws Exception {
        // Очищаем базу перед тестом
        itemRepository.deleteAll();

        mockMvc.perform(get("/item/all"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void createItem_ShouldCreateItem_WhenValidRequest() throws Exception {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setName("New Item");
        itemDTO.setPrice("39.99");

        mockMvc.perform(post("/item/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Item"))
                .andExpect(jsonPath("$.price").value("39.99"))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void updateItem_ShouldUpdateItem_WhenValidRequest() throws Exception {
        ItemDTO updateDTO = new ItemDTO();
        updateDTO.setName("Updated Item");
        updateDTO.setPrice("49.99");

        mockMvc.perform(put("/item/update/{id}", testItem.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testItem.getId()))
                .andExpect(jsonPath("$.name").value("Updated Item"))
                .andExpect(jsonPath("$.price").value("49.99"));
    }

    @Test
    void updateItem_ShouldReturn404_WhenItemDoesNotExist() throws Exception {
        ItemDTO updateDTO = new ItemDTO();
        updateDTO.setName("Updated Item");
        updateDTO.setPrice("49.99");

        mockMvc.perform(put("/item/update/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().is(404))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void updateItem_ShouldReturn400_WhenInvalidRequest() throws Exception {
        ItemDTO invalidUpdateDTO = new ItemDTO(); // name and price are null

        mockMvc.perform(put("/item/update/{id}", testItem.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUpdateDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void deleteItem_ShouldDeleteItem_WhenItemExists() throws Exception {
        mockMvc.perform(delete("/item/delete/{id}", testItem.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isString());

        // Проверяем что товар действительно удален
        mockMvc.perform(get("/item/id/{id}", testItem.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteItem_ShouldReturn404_WhenItemDoesNotExist() throws Exception {
        mockMvc.perform(delete("/item/delete/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void createItem_ShouldHandleDuplicateNames() throws Exception {
        // Товары с одинаковыми именами должны создаваться нормально
        ItemDTO firstItemDTO = new ItemDTO();
        firstItemDTO.setName("Duplicate Item");
        firstItemDTO.setPrice("19.99");

        ItemDTO secondItemDTO = new ItemDTO();
        secondItemDTO.setName("Duplicate Item");
        secondItemDTO.setPrice("29.99");

        // Создаем первый товар
        mockMvc.perform(post("/item/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firstItemDTO)))
                .andExpect(status().isOk());

        // Создаем второй товар с таким же именем (должно работать)
        mockMvc.perform(post("/item/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(secondItemDTO)))
                .andExpect(status().isOk());

        // Проверяем что оба товара существуют
        mockMvc.perform(get("/item/name/{name}", "Duplicate Item"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[?(@.price == '19.99')]").exists())
                .andExpect(jsonPath("$[?(@.price == '29.99')]").exists());
    }

    @Test
    void getItemByName_ShouldBeCaseSensitive() throws Exception {
        Item lowerCaseItem = new Item();
        lowerCaseItem.setName("test item");
        lowerCaseItem.setPrice("19.99");
        itemRepository.save(lowerCaseItem);

        // Поиск с другим регистром должен вернуть другой результат
        mockMvc.perform(get("/item/name/{name}", "TEST ITEM"))
                .andExpect(status().isNotFound());

        // Поиск с правильным регистром должен работать
        mockMvc.perform(get("/item/name/{name}", "test item"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("test item"));
    }
}