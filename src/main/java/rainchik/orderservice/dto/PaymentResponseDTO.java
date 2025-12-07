package rainchik.orderservice.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentResponseDTO {

    private String paymentId;
    private Long userId;
    private Long orderId;
    private String paymentStatus;
    private LocalDateTime paymentDate;
    private BigDecimal paymentAmount;
}
