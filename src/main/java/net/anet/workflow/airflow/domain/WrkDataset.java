package net.anet.workflow.airflow.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A WrkDataset.
 */
@Entity
@Table(name = "wrk_dataset")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class WrkDataset implements Serializable {

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
    @Column(name = "is_enabled", nullable = false)
    private Boolean isEnabled;

    @OneToMany(mappedBy = "wrkDataSet", fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<WrkDatabase> dbNames = new HashSet<>();

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

    public WrkDataset name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isIsEnabled() {
        return isEnabled;
    }

    public WrkDataset isEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
        return this;
    }

    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public Set<WrkDatabase> getDbNames() {
        return dbNames;
    }

    public WrkDataset dbNames(Set<WrkDatabase> wrkDatabases) {
        this.dbNames = wrkDatabases;
        return this;
    }

    public WrkDataset addDbName(WrkDatabase wrkDatabase) {
        this.dbNames.add(wrkDatabase);
        wrkDatabase.setWrkDataSet(this);
        return this;
    }

    public WrkDataset removeDbName(WrkDatabase wrkDatabase) {
        this.dbNames.remove(wrkDatabase);
        wrkDatabase.setWrkDataSet(null);
        return this;
    }

    public void setDbNames(Set<WrkDatabase> wrkDatabases) {
        this.dbNames = wrkDatabases;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WrkDataset)) {
            return false;
        }
        return id != null && id.equals(((WrkDataset) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "WrkDataset{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", isEnabled='" + isIsEnabled() + "'" +
            "}";
    }
}
