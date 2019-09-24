package net.anet.workflow.airflow.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AfDatasetDTO  implements Serializable {

    private long id;
    private String name;
    private List<AfDatabaseDTO> database;


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

    public List<AfDatabaseDTO> getDatabase() {
        return database;
    }

    public void setDatabase(List<AfDatabaseDTO> database) {
        this.database = database;
    }

    public void setDatabase(AfDatabaseDTO database) {
        if(this.database == null){
            this.database = new ArrayList<>();
        }
        this.database.add(database);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AfDatasetDTO that = (AfDatasetDTO) o;

        if (id != that.id) return false;
        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AfDatasetDTO{" +
            "id=" + id +
            ", name='" + name + '\'' +
            '}';
    }
}
