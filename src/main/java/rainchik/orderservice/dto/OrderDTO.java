package rainchik.orderservice.dto;

import com.fasterxml.jackson.databind.EnumNamingStrategies;
import com.fasterxml.jackson.databind.EnumNamingStrategy;
import com.fasterxml.jackson.databind.annotation.EnumNaming;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import rainchik.orderservice.annotation.ValidStatusValue;
import rainchik.orderservice.entity.OrderItem;
import rainchik.orderservice.entity.Status;

import java.time.LocalDateTime;
import java.util.List;

import static rainchik.orderservice.entity.Status.PENDING;

@Data
@RequiredArgsConstructor
public class OrderDTO {

    @NotBlank
    @Email
    private String userEmail;

    @NotBlank
    @ValidStatusValue
    private String status;

    @NotEmpty
    private List<OrderItemDTO> orderItems;

    @PastOrPresent
    private LocalDateTime time;

}
