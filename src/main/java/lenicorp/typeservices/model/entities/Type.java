package lenicorp.typeservices.model.entities;

import jakarta.persistence.*;
import lenicorp.typeservices.model.enums.TypeGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Table(name = "type") @Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Type
{
    @Id @Column(nullable = false, unique = true)
    private String uniqueCode;
    @Enumerated(EnumType.STRING)
    private TypeGroup typeGroup;
    @Column(nullable = false)
    private String name;
    private boolean active;
    @Transient
    private List<Type> children;
    private String objectFolder;

    @Override
    public String toString() {
        return name + " (" +uniqueCode + ")"  ;
    }

    public Type(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }
}