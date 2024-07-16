package lenicorp.typeservices.model.dtos;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import lenicorp.typeservices.controller.repositories.TypeRepo;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ExistingTypeCode.ExistingTypeIdValidator.class})
@Documented
public @interface ExistingTypeCode
{
    String message() default "Le code du type est inexistant";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
    @Component
    @RequiredArgsConstructor
    class ExistingTypeIdValidator implements ConstraintValidator<ExistingTypeCode, String>
    {
        private final TypeRepo typeRepo;
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context)
        {
            if(value == null) return true;
            return typeRepo.existsById(value);
        }
    }
}


