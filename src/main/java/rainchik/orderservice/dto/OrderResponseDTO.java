package rainchik.orderservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import rainchik.orderservice.annotation.ValidStatusValue;

import java.time.LocalDateTime;

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

}
