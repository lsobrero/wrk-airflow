package net.anet.workflow.airflow.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link net.anet.workflow.airflow.domain.MyDummy} entity.
 */
public class MyDummyDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 3, max = 30)
    private String name;

    @NotNull
    private Boolean isEnabled;


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

    public Boolean isIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MyDummyDTO myDummyDTO = (MyDummyDTO) o;
        if (myDummyDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), myDummyDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MyDummyDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", isEnabled='" + isIsEnabled() + "'" +
            "}";
    }
}
