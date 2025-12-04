package rainchik.orderservice.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ItemDTO {

    @NotBlank
    @Size(min = 3, max = 100)
    @Pattern(regexp = "^[a-zA-Zа-яА-Я0-9\\s():]+$")
    private String name;

    @DecimalMin("0")
    @Size(max = 20)
    private String price;

}
