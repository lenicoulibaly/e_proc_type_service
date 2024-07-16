package lenicorp.typeservices.model.dtos;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lenicorp.typeservices.controller.repositories.TypeRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {UniqueTypeCode.UniqueTypeCodeValidatorOnCreate.class, UniqueTypeCode.UniqueTypeCodeValidatorOnUpdate.class})
@Documented
public @interface UniqueTypeCode
{
    String message() default "Code de type déjà utilisé";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    @Component
    @RequiredArgsConstructor
    class UniqueTypeCodeValidatorOnCreate implements ConstraintValidator<UniqueTypeCode, String>
    {
        private final TypeRepo typeRepo;
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context)
        {
            return !typeRepo.existsByUniqueCode(value);
        }
    }

    @Component
    @RequiredArgsConstructor
    class UniqueTypeCodeValidatorOnUpdate implements ConstraintValidator<UniqueTypeCode, UpdateTypeDTO>
    {
        private final TypeRepo typeRepo;
        @Override
        public boolean isValid(UpdateTypeDTO dto, ConstraintValidatorContext context)
        {
            return !typeRepo.existsByUniqueCode(dto.getUniqueCode(), dto.getOldUniqueCode());
        }
    }
}