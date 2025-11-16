package rainchik.orderservice.exception;

public class OrderDoesNotExistException extends Exception {
    public OrderDoesNotExistException(Long id) {
        super("Order does not exist: " + id);
    }
}
