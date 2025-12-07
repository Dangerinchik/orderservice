package rainchik.orderservice.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderKafkaDTO {

    private Long orderId;
    private Long userId;
    private BigDecimal amount;
}
