package rainchik.orderservice.exception;

import java.util.List;

public class ListOfOrdersIsEmpty extends Exception{
    public ListOfOrdersIsEmpty(){
        super("List of orders is empty");
    }

    public ListOfOrdersIsEmpty(List<String> statuses){
        super("List of orders with statuses: " + statuses.toString() + " is empty");
    }
}
