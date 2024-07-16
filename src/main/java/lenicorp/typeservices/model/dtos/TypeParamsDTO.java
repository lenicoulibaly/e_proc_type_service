package lenicorp.typeservices.model.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class TypeParamsDTO
{
    private String[] childCodes;

    @NotNull(message = "L'ID du type parent ne peut être null")
    @ExistingTypeCode(message = "type parent inexistant")
    private String parentCode;

    private String status;
}
