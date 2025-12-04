package rainchik.orderservice.service;

import rainchik.orderservice.dto.ItemDTO;
import rainchik.orderservice.dto.ItemResponseDTO;
import rainchik.orderservice.entity.Item;
import rainchik.orderservice.exception.ItemDoesNotExistException;
import rainchik.orderservice.exception.ListOfItemsFoundedByNameIsEmpty;
import rainchik.orderservice.exception.ListOfItemsIsEmpty;

import java.util.List;

public interface ItemService {

    public ItemResponseDTO getItemById(Long id) throws ItemDoesNotExistException;
    public Item getItemByIdForService(Long id) throws ItemDoesNotExistException;
    public List<ItemResponseDTO> getItemsByName(String name) throws ListOfItemsFoundedByNameIsEmpty;
    public List<ItemResponseDTO> getItems() throws ListOfItemsIsEmpty;
    public ItemResponseDTO updateItem(Long itemId, ItemDTO itemDTO) throws ItemDoesNotExistException;
    public ItemResponseDTO createItem(ItemDTO itemDTO);
    public String deleteItem(Long itemId) throws ItemDoesNotExistException;
}
