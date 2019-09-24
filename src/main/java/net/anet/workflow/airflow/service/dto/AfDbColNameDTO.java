package net.anet.workflow.airflow.service.dto;

import java.io.Serializable;
import java.util.List;

public class AfDbColNameDTO implements Serializable {

    private long id;
    private String name;
    private String description;
    private AfDbColTypeDTO columnType;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AfDbColTypeDTO getColumnType() {
        return columnType;
    }

    public void setColumnType(AfDbColTypeDTO columnType) {
        this.columnType = columnType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AfDbColNameDTO that = (AfDbColNameDTO) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "AfDbColNameDTO{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", columnType=" + columnType +
            '}';
    }
}
