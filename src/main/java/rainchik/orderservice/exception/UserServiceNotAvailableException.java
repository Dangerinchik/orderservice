package rainchik.orderservice.exception;

public class UserServiceNotAvailableException extends Exception{
    public UserServiceNotAvailableException() {
        super("User Service Not Available. Wait some seconds");
    }
}
