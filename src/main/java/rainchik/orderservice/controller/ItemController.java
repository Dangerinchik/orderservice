package rainchik.orderservice.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rainchik.orderservice.dto.ItemDTO;
import rainchik.orderservice.dto.ItemResponseDTO;
import rainchik.orderservice.entity.Item;
import rainchik.orderservice.exception.ItemDoesNotExistException;
import rainchik.orderservice.exception.ListOfItemsFoundedByNameIsEmpty;
import rainchik.orderservice.exception.ListOfItemsIsEmpty;
import rainchik.orderservice.service.ItemService;

import java.util.List;

@RestController
@RequestMapping("/item")
public class ItemController {

    private ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<ItemResponseDTO> getItem(@PathVariable Long id) throws ItemDoesNotExistException {
        return ResponseEntity.ok(itemService.getItemById(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ItemResponseDTO> updateItem(@PathVariable Long id, @Valid @RequestBody ItemDTO dto) throws ItemDoesNotExistException {
        return ResponseEntity.ok(itemService.updateItem(id, dto));
    }

    @PostMapping("/create")
    public ResponseEntity<ItemResponseDTO> createItem(@RequestBody ItemDTO dto) throws ItemDoesNotExistException {
        return ResponseEntity.ok(itemService.createItem(dto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteItem(@PathVariable Long id) throws ItemDoesNotExistException {
        return ResponseEntity.ok(itemService.deleteItem(id));
    }

}
