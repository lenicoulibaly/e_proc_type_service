package lenicorp.typeservices.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "type_param") @Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class TypeParam
{
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TYPE_ID_GEN")
    @SequenceGenerator(name = "TYPE_ID_GEN", sequenceName = "TYPE_ID_GEN", allocationSize = 10)
    private Long typeParamId;
    @ManyToOne @JoinColumn(name = "parent_code")
    private Type parent;
    @ManyToOne @JoinColumn(name = "child_code")
    private Type child;
    private boolean active;
}