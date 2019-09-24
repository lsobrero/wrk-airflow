package net.anet.workflow.airflow.service.dto;

import java.io.Serializable;
import java.util.List;

public class AfTableNameDTO implements Serializable {

    private long id;
    private String name;
    private String description;
    private List<AfDbColNameDTO> columnsName;

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

    public List<AfDbColNameDTO> getColumnsName() {
        return columnsName;
    }

    public void setColumnsName(List<AfDbColNameDTO> columnsName) {
        this.columnsName = columnsName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AfTableNameDTO that = (AfTableNameDTO) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "AfTableNameDTO{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", columnsName=" + columnsName +
            '}';
    }
}
