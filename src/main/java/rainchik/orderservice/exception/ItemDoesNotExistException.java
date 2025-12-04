package rainchik.orderservice.exception;

public class ItemDoesNotExistException extends Exception {

    public ItemDoesNotExistException(Long id) {
        super("Item with id: " + id + " does not exists");
    }
}
