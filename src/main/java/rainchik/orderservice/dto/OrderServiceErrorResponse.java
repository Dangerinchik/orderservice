package rainchik.orderservice.dto;

import lombok.Data;

@Data
public class OrderServiceErrorResponse {

    private String message;

    public OrderServiceErrorResponse() {

    }

    public OrderServiceErrorResponse(String message) {
        this.message = message;
    }

}
