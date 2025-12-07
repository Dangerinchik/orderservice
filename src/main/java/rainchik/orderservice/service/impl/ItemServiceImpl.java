package rainchik.orderservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rainchik.orderservice.dto.ItemDTO;
import rainchik.orderservice.dto.ItemResponseDTO;
import rainchik.orderservice.entity.Item;
import rainchik.orderservice.exception.ItemDoesNotExistException;
import rainchik.orderservice.exception.ListOfItemsFoundedByNameIsEmpty;
import rainchik.orderservice.exception.ListOfItemsIsEmpty;
import rainchik.orderservice.mapper.ItemMapper;
import rainchik.orderservice.repository.ItemRepository;
import rainchik.orderservice.service.ItemService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, ItemMapper itemMapper) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
    }

    @Override
    public ItemResponseDTO getItemById(Long id) throws ItemDoesNotExistException {
        Optional<Item> item = itemRepository.findById(id);
        if (item.isPresent()) {
            return itemMapper.toItemResponseDTO(item.get());
        }
        else{
            throw new ItemDoesNotExistException(id);
        }
    }

    @Override
    public Item getItemByIdForService(Long id) throws ItemDoesNotExistException {
        Optional<Item> item = itemRepository.findById(id);
        if (item.isPresent()) {
            return item.get();
        }
        else{
            throw new ItemDoesNotExistException(id);
        }
    }

    @Override
    @Transactional
    public ItemResponseDTO updateItem(Long itemId, ItemDTO itemDTO) throws ItemDoesNotExistException {

        Optional<Item> item = itemRepository.findById(itemId);

        if(item.isPresent()){
            item.get().setName(itemDTO.getName());
            item.get().setPrice(itemDTO.getPrice());
            return itemMapper.toItemResponseDTO(itemRepository.save(item.get()));
        }
        else{
            throw new ItemDoesNotExistException(itemId);
        }
    }

    @Override
    @Transactional
    public ItemResponseDTO createItem(ItemDTO itemDTO) {
        Item item = itemMapper.toItem(itemDTO);
        return itemMapper.toItemResponseDTO(itemRepository.save(item));
    }

    @Override
    @Transactional
    public String deleteItem(Long itemId) throws ItemDoesNotExistException {
        if(itemRepository.existsById(itemId)){
            itemRepository.deleteById(itemId);
            return "Item has been deleted";
        }
        else{
            throw new ItemDoesNotExistException(itemId);
        }
    }
}
