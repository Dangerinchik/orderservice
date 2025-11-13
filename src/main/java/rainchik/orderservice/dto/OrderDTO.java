package rainchik.orderservice.dto;

import com.fasterxml.jackson.databind.EnumNamingStrategies;
import com.fasterxml.jackson.databind.EnumNamingStrategy;
import com.fasterxml.jackson.databind.annotation.EnumNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import rainchik.orderservice.annotation.ValidStatusValue;
import rainchik.orderservice.entity.Status;

import java.time.LocalDateTime;

import static rainchik.orderservice.entity.Status.PENDING;

@Data
@RequiredArgsConstructor
public class OrderDTO {

    @NotBlank
    private Long userId;

    @NotBlank
    @ValidStatusValue
    private String status;

    @PastOrPresent
    private LocalDateTime time;

}
