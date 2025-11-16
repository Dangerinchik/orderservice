package rainchik.orderservice.exception;

public class ItemAlreadyExistsException extends Exception{
    public ItemAlreadyExistsException(Long id){
        super("Item with id: " + id + " already exists");
    }
}
