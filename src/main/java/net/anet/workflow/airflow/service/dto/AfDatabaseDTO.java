package net.anet.workflow.airflow.service.dto;

import java.io.Serializable;
import java.util.List;

public class AfDatabaseDTO implements Serializable {

    private long id;
    private String name;
    private String host;
    private String port;
    private String username;
    private String pasUser;
    private List<AfDbTableNameDTO> tables;

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

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasUser() {
        return pasUser;
    }

    public void setPasUser(String pasUser) {
        this.pasUser = pasUser;
    }

    public void setColumns(List<AfDbColNameDTO> cols) {
    }

    public List<AfDbTableNameDTO> getTables() {
        return tables;
    }

    public void setTables(List<AfDbTableNameDTO> tables) {
        this.tables = tables;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AfDatabaseDTO that = (AfDatabaseDTO) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "AfDatabaseDTO{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", host='" + host + '\'' +
            ", port='" + port + '\'' +
            ", username='" + username + '\'' +
            ", pasUser='" + pasUser + '\'' +
            ", tables=" + tables +
            '}';
    }
}
