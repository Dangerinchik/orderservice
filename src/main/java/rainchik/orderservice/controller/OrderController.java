package rainchik.orderservice.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rainchik.orderservice.dto.OrderDTO;
import rainchik.orderservice.dto.OrderResponseDTO;
import rainchik.orderservice.exception.ItemDoesNotExistException;
import rainchik.orderservice.exception.ListOfOrdersIsEmpty;
import rainchik.orderservice.exception.OrderDoesNotExistException;
import rainchik.orderservice.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    private OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrder(@PathVariable("id") Long id) throws OrderDoesNotExistException {
        return ResponseEntity.ok(orderService.getOrder(id));
    }

    @GetMapping("/list")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersInList(@RequestParam("ids") List<Long> ids) throws ListOfOrdersIsEmpty {
        return ResponseEntity.ok(orderService.getOrdersInList(ids));
    }

    @GetMapping("/list/status")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersInStatusList(@RequestParam("statuses") List<String> statuses) throws ListOfOrdersIsEmpty {
        return ResponseEntity.ok(orderService.getOrdersInStatusList(statuses));
    }

    @PostMapping("/create")
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody OrderDTO dto) throws ItemDoesNotExistException {
        return ResponseEntity.ok(orderService.createOrder(dto));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<OrderResponseDTO> updateOrder(@PathVariable("id") Long id, @Valid @RequestBody OrderDTO dto) throws OrderDoesNotExistException {
        return ResponseEntity.ok(orderService.updateOrder(id, dto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable("id") Long id) throws OrderDoesNotExistException {
        return ResponseEntity.ok(orderService.deleteOrder(id));
    }

}
