package net.anet.workflow.airflow.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A WrkDatabase.
 */
@Entity
@Table(name = "wrk_database")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class WrkDatabase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(min = 3, max = 30)
    @Column(name = "name", length = 30, nullable = false, unique = true)
    private String name;

    @NotNull
    @Column(name = "host", nullable = false)
    private String host;

    @Column(name = "port")
    private String port;

    @NotNull
    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "pas_user")
    private String pasUser;

    @OneToMany(mappedBy = "databaseName", fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<WrkDbTableName> tableNames = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("dbNames")
    private WrkDataset wrkDataSet;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public WrkDatabase name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public WrkDatabase host(String host) {
        this.host = host;
        return this;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public WrkDatabase port(String port) {
        this.port = port;
        return this;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public WrkDatabase username(String username) {
        this.username = username;
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasUser() {
        return pasUser;
    }

    public WrkDatabase pasUser(String pasUser) {
        this.pasUser = pasUser;
        return this;
    }

    public void setPasUser(String pasUser) {
        this.pasUser = pasUser;
    }

    public Set<WrkDbTableName> getTableNames() {
        return tableNames;
    }

    public WrkDatabase tableNames(Set<WrkDbTableName> wrkDbTableNames) {
        this.tableNames = wrkDbTableNames;
        return this;
    }

    public WrkDatabase addTableName(WrkDbTableName wrkDbTableName) {
        this.tableNames.add(wrkDbTableName);
        wrkDbTableName.setDatabaseName(this);
        return this;
    }

    public WrkDatabase removeTableName(WrkDbTableName wrkDbTableName) {
        this.tableNames.remove(wrkDbTableName);
        wrkDbTableName.setDatabaseName(null);
        return this;
    }

    public void setTableNames(Set<WrkDbTableName> wrkDbTableNames) {
        this.tableNames = wrkDbTableNames;
    }

/*
    public WrkDataset getWrkDataSet() {
        return wrkDataSet;
    }
*/

    public WrkDatabase wrkDataSet(WrkDataset wrkDataset) {
        this.wrkDataSet = wrkDataset;
        return this;
    }

    public void setWrkDataSet(WrkDataset wrkDataset) {
        this.wrkDataSet = wrkDataset;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WrkDatabase)) {
            return false;
        }
        return id != null && id.equals(((WrkDatabase) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "WrkDatabase{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", host='" + getHost() + "'" +
            ", port='" + getPort() + "'" +
            ", username='" + getUsername() + "'" +
            ", pasUser='" + getPasUser() + "'" +
            "}";
    }
}
