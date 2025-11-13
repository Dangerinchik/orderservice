package rainchik.orderservice.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import rainchik.orderservice.entity.Status;
import rainchik.orderservice.validator.StatusValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = StatusValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidStatusValue {
    String message() default "Invalid status value";
    Class<? extends Status> statusEnum() default Status.class;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
