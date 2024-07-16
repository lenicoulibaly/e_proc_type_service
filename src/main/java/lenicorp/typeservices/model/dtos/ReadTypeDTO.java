package lenicorp.typeservices.model.dtos;

import lenicorp.typeservices.model.entities.Type;
import lenicorp.typeservices.model.enums.TypeGroup;
import lombok.*;
import org.springframework.beans.BeanUtils;

import java.util.List;


@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class ReadTypeDTO
{
    private String typeGroup;
    private String uniqueCode;
    private String name;
    private boolean active;
    private String objectFolder;
    private List<ReadTypeDTO> children;

    public ReadTypeDTO(String uniqueCode, TypeGroup typeGroup, String name, boolean active) {
        this.uniqueCode = uniqueCode;
        this.typeGroup = typeGroup.name();
        this.name = name;
        this.active = active;
    }

    public ReadTypeDTO(String uniqueCode, TypeGroup typeGroup, String name, boolean active, String objectFolder) {

        this.typeGroup = typeGroup.name();
        this.uniqueCode = uniqueCode;
        this.name = name;
        this.active = active;
        this.objectFolder = objectFolder;
    }

    public ReadTypeDTO(Type type) {
        BeanUtils.copyProperties(type, this);
    }
}