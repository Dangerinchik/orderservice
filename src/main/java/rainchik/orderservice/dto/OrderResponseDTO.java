package rainchik.orderservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import rainchik.orderservice.annotation.ValidStatusValue;

import java.time.LocalDateTime;
import java.util.List;

@Data
@RequiredArgsConstructor
public class OrderResponseDTO {

    private Long id;


    private Long userId;

    @NotBlank
    @ValidStatusValue
    private String status;

    @PastOrPresent
    private LocalDateTime time;

    private List<OrderItemResponseDTO> orderItems;

}
