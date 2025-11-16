package rainchik.orderservice.exception;

public class OrderAlreadyExistsException extends Exception{
    public OrderAlreadyExistsException(){
        super("Order already exists");
    }
}
