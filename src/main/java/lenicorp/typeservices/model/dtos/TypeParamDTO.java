package lenicorp.typeservices.model.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TypeParamDTO
{
    @NotNull(message = "L'ID du sous type ne peut être null")
    @ExistingTypeCode(message = "Sous type inexistant")
    private String childCode;

    @NotNull(message = "L'ID du type parent ne peut être null")
    @ExistingTypeCode(message = "type parent inexistant")
    private String parentCode;
}
