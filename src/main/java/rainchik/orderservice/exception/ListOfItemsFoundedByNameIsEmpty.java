package rainchik.orderservice.exception;

public class ListOfItemsFoundedByNameIsEmpty extends Exception {
    public ListOfItemsFoundedByNameIsEmpty(String name) {
        super("List of Items which were found by name: " + name + " is empty");
    }
}
