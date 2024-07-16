package lenicorp.typeservices.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class SelectOption
{
    String id;
    String label;
    String typeName;

    public SelectOption(String id, String label) {
        this.id = id;
        this.label = label;
    }

    public SelectOption(Long id, String label) {
        this.id = String.valueOf(id);
        this.label = label;
    }

    public SelectOption(Long id, String label, String typeName) {
        this(id, label);
        this.typeName = typeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SelectOption that = (SelectOption) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}