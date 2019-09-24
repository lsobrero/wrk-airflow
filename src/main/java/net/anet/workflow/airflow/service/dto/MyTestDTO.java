package net.anet.workflow.airflow.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link net.anet.workflow.airflow.domain.MyTest} entity.
 */
public class MyTestDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 3, max = 30)
    private String name;


    private Long colTypeId;

    private String colTypeName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getColTypeId() {
        return colTypeId;
    }

    public void setColTypeId(Long myDummyId) {
        this.colTypeId = myDummyId;
    }

    public String getColTypeName() {
        return colTypeName;
    }

    public void setColTypeName(String myDummyName) {
        this.colTypeName = myDummyName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MyTestDTO myTestDTO = (MyTestDTO) o;
        if (myTestDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), myTestDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MyTestDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", colType=" + getColTypeId() +
            ", colType='" + getColTypeName() + "'" +
            "}";
    }
}
