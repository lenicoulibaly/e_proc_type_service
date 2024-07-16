package lenicorp.typeservices.model.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.validator.constraints.Length;


@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class CreateTypeDTO
{
    @NotBlank(message = "Le groupCode ne peut être null")
    @NotNull(message = "Le groupCode ne peut être null")
    private String typeGroup;

    @Pattern(message = "Le groupeCode ne doit contenir d'espace, il doit commencer par TYP_ ou STA_ et contenir entre 6 et 8 caractères", regexp = "(^TYP_|^STA_)\\w{2,4}")
    @NotBlank(message = "Le uniqueCode ne peut être null")
    @NotNull(message = "Le uniqueCode ne peut être null")
    @UniqueTypeCode(message="Ce code est déjà utilisé")
    private String uniqueCode;

    @Length(message = "Le nom du type doit contenir au moins deux caratères", min = 2)
    @NotBlank(message = "Le nom du type ne peut être nul")
    @NotNull(message = "Le nom du type ne peut être nul")
    private String name;
}