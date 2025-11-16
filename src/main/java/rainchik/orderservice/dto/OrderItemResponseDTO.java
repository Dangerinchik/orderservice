package rainchik.orderservice.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class OrderItemResponseDTO {
    private Long orderItemId;
    private Long orderId;
    private Long itemId;
    private Integer quantity;
}
