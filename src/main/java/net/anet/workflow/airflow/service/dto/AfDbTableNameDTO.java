package net.anet.workflow.airflow.service.dto;

import java.io.Serializable;
import java.util.List;

public class AfDbTableNameDTO implements Serializable {

    private long id;
    private String name;
    private List<AfDbColNameDTO> cols;

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

    public List<AfDbColNameDTO> getCols() {
        return cols;
    }

    public void setCols(List<AfDbColNameDTO> cols) {
        this.cols = cols;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AfDbTableNameDTO that = (AfDbTableNameDTO) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "AfDbTableNameDTO{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", cols=" + cols +
            '}';
    }
}
