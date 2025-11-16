package rainchik.orderservice.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import rainchik.orderservice.dto.OrderServiceErrorResponse;
import rainchik.orderservice.exception.*;

import java.time.LocalDateTime;

@RestControllerAdvice
public class OrderServiceExceptionHandler {

    @ExceptionHandler({ItemAlreadyExistsException.class})
    public ResponseEntity<OrderServiceErrorResponse> handleItemAlreadyExistsException(ItemAlreadyExistsException e) {
        String message = String.format("Item already exists: %s %s", LocalDateTime.now(), e.getMessage());
        OrderServiceErrorResponse errorResponse = new OrderServiceErrorResponse(message);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler({ItemDoesNotExistException.class})
    public ResponseEntity<OrderServiceErrorResponse> handleItemDoesNotExistException(ItemDoesNotExistException e) {
        String message = String.format("Item does not exist: %s %s", LocalDateTime.now(), e.getMessage());
        OrderServiceErrorResponse errorResponse = new OrderServiceErrorResponse(message);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler({ListOfItemsFoundedByNameIsEmpty.class})
    public ResponseEntity<OrderServiceErrorResponse> handleListOfItemsFoundedByNameIsEmpty(ListOfItemsFoundedByNameIsEmpty e) {
        String message = String.format("List of items founded by name is empty: %s %s", LocalDateTime.now(), e.getMessage());
        OrderServiceErrorResponse errorResponse = new OrderServiceErrorResponse(message);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler({ListOfItemsIsEmpty.class})
    public ResponseEntity<OrderServiceErrorResponse> handleListOfItemsIsEmpty(ListOfItemsIsEmpty e) {
        String message = String.format("List of items is empty: %s %s", LocalDateTime.now(), e.getMessage());
        OrderServiceErrorResponse errorResponse = new OrderServiceErrorResponse(message);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler({ListOfOrdersIsEmpty.class})
    public ResponseEntity<OrderServiceErrorResponse> handleListOfOrdersIsEmpty(ListOfOrdersIsEmpty e) {
        String message = String.format("List of orders is empty: %s %s", LocalDateTime.now(), e.getMessage());
        OrderServiceErrorResponse errorResponse = new OrderServiceErrorResponse(message);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler({OrderAlreadyExistsException.class})
    public ResponseEntity<OrderServiceErrorResponse> handleOrderAlreadyExistsException(OrderAlreadyExistsException e) {
        String message = String.format("Order already exists: %s %s", LocalDateTime.now(), e.getMessage());
        OrderServiceErrorResponse errorResponse = new OrderServiceErrorResponse(message);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler({OrderDoesNotExistException.class})
    public ResponseEntity<OrderServiceErrorResponse> handleOrderDoesNotExistException(OrderDoesNotExistException e) {
        String message = String.format("Order does not exist: %s %s", LocalDateTime.now(), e.getMessage());
        OrderServiceErrorResponse errorResponse = new OrderServiceErrorResponse(message);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler({UserServiceNotAvailableException.class})
    public ResponseEntity<OrderServiceErrorResponse> handleUserServiceNotAvailableException(UserServiceNotAvailableException e) {
        String message = String.format("User service not available: %s %s", LocalDateTime.now(), e.getMessage());
        OrderServiceErrorResponse errorResponse = new OrderServiceErrorResponse(message);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<OrderServiceErrorResponse> handleGenericException(Exception ex) {
        OrderServiceErrorResponse response = new OrderServiceErrorResponse(
                String.format("Internal server error: %s", ex.getMessage())
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

}
