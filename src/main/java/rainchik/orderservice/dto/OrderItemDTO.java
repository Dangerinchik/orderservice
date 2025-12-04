package rainchik.orderservice.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class OrderItemDTO {

    @NotNull
    private Long itemId;

    @NotNull
    private Integer quantity;

}
