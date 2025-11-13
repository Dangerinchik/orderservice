package rainchik.orderservice.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import rainchik.orderservice.annotation.ValidStatusValue;
import rainchik.orderservice.entity.Status;

import java.util.Arrays;
import java.util.stream.Stream;

public class StatusValidator implements ConstraintValidator<ValidStatusValue, String> {

    public Class<? extends Status> statusClass;

    @Override
    public void initialize(ValidStatusValue constraintAnnotation) {
        this.statusClass = constraintAnnotation.statusEnum();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
     return Arrays.stream(statusClass.getEnumConstants()).anyMatch(e -> e.name().equals(value));

    }
}
