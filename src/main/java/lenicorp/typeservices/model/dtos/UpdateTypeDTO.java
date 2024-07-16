package lenicorp.typeservices.model.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;


@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString //@Entity
@UniqueTypeCode(message = "uniqueCode::Code de type est déjà utilisé")
public class UpdateTypeDTO
{
    @NotBlank(message = "Le groupCode ne peut être null")
    @NotNull(message = "Le groupCode ne peut être null")
    private String typeGroup;

    @NotBlank(message = "Le uniqueCode ne peut être null")
    @NotNull(message = "Le uniqueCode ne peut être null")
    private String uniqueCode;

    @NotBlank(message = "veuillez selectionner le type à modifier")
    @NotNull(message = "veuillez selectionner le type à modifier")
    @ExistingTypeCode(message = "veuillez selectionner le type à modifier")
    private String oldUniqueCode;

    @Length(message = "Le nom du type doit contenir au moins deux caratères", min = 2)
    @NotBlank(message = "Le nom du type ne peut être nul")
    @NotNull(message = "Le nom du type ne peut être nul")
    private String name;
}