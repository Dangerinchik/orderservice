package rainchik.orderservice.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rainchik.orderservice.dto.ItemDTO;
import rainchik.orderservice.dto.ItemResponseDTO;
import rainchik.orderservice.entity.Item;
import rainchik.orderservice.exception.ItemDoesNotExistException;
import rainchik.orderservice.exception.ListOfItemsFoundedByNameIsEmpty;
import rainchik.orderservice.exception.ListOfItemsIsEmpty;
import rainchik.orderservice.mapper.ItemMapper;
import rainchik.orderservice.repository.ItemRepository;
import rainchik.orderservice.service.impl.ItemServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {
    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ItemMapper itemMapper;

    @InjectMocks
    private ItemServiceImpl itemService;

    private Item item;
    private ItemDTO itemDTO;
    private ItemResponseDTO itemResponseDTO;

    @BeforeEach
    void setUp() {
        item = new Item();
        item.setId(1L);
        item.setName("Test Item");
        item.setPrice("100.0");

        itemDTO = new ItemDTO();
        itemDTO.setName("Test Item");
        itemDTO.setPrice("100.0");

        itemResponseDTO = new ItemResponseDTO();
        itemResponseDTO.setId(1L);
        itemResponseDTO.setName("Test Item");
        itemResponseDTO.setPrice("100.0");
    }

    @Test
    void getItemById_ShouldReturnItem_WhenItemExists() throws ItemDoesNotExistException {
        // Arrange
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemMapper.toItemResponseDTO(item)).thenReturn(itemResponseDTO);

        // Act
        ItemResponseDTO result = itemService.getItemById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Item", result.getName());
        assertEquals("100.0", result.getPrice());
        verify(itemRepository).findById(1L);
    }

    @Test
    void getItemById_ShouldThrowException_WhenItemDoesNotExist() {
        // Arrange
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ItemDoesNotExistException.class, () -> itemService.getItemById(1L));
        verify(itemRepository).findById(1L);
    }

    @Test
    void getItemByIdForService_ShouldReturnItem_WhenItemExists() throws ItemDoesNotExistException {
        // Arrange
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        // Act
        Item result = itemService.getItemByIdForService(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Item", result.getName());
        verify(itemRepository).findById(1L);
    }

    @Test
    void getItemByIdForService_ShouldThrowException_WhenItemDoesNotExist() {
        // Arrange
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ItemDoesNotExistException.class, () -> itemService.getItemByIdForService(1L));
    }

    @Test
    void updateItem_ShouldUpdateAndReturnItem_WhenItemExists() throws ItemDoesNotExistException {
        // Arrange
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemRepository.save(item)).thenReturn(item);
        when(itemMapper.toItemResponseDTO(item)).thenReturn(itemResponseDTO);

        // Act
        ItemResponseDTO result = itemService.updateItem(1L, itemDTO);

        // Assert
        assertNotNull(result);
        verify(itemRepository).save(item);
    }

    @Test
    void updateItem_ShouldThrowException_WhenItemDoesNotExist() {
        // Arrange
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ItemDoesNotExistException.class, () -> itemService.updateItem(1L, itemDTO));
    }

    @Test
    void createItem_ShouldCreateAndReturnItem() {
        // Arrange
        when(itemMapper.toItem(itemDTO)).thenReturn(item);
        when(itemRepository.save(item)).thenReturn(item);
        when(itemMapper.toItemResponseDTO(item)).thenReturn(itemResponseDTO);

        // Act
        ItemResponseDTO result = itemService.createItem(itemDTO);

        // Assert
        assertNotNull(result);
        verify(itemMapper).toItem(itemDTO);
        verify(itemRepository).save(item);
    }

    @Test
    void deleteItem_ShouldDeleteItem_WhenItemExists() throws ItemDoesNotExistException {
        // Arrange
        when(itemRepository.existsById(1L)).thenReturn(true);

        // Act
        String result = itemService.deleteItem(1L);

        // Assert
        assertEquals("Item has been deleted", result);
        verify(itemRepository).deleteById(1L);
    }

    @Test
    void deleteItem_ShouldThrowException_WhenItemDoesNotExist() {
        // Arrange
        when(itemRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        assertThrows(ItemDoesNotExistException.class, () -> itemService.deleteItem(1L));
    }
}
