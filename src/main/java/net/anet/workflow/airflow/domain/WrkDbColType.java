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
 * A WrkDbColType.
 */
@Entity
@Table(name = "wrk_db_col_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class WrkDbColType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(min = 3, max = 30)
    @Column(name = "name", length = 30, nullable = false, unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "colType")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<WrkDbColName> colNames = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("colTypes")
    private WrkAnonType anonType;

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

    public WrkDbColType name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public WrkDbColType description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<WrkDbColName> getColNames() {
        return colNames;
    }

    public WrkDbColType colNames(Set<WrkDbColName> wrkDbColNames) {
        this.colNames = wrkDbColNames;
        return this;
    }

    public WrkDbColType addColName(WrkDbColName wrkDbColName) {
        this.colNames.add(wrkDbColName);
        wrkDbColName.setColType(this);
        return this;
    }

    public WrkDbColType removeColName(WrkDbColName wrkDbColName) {
        this.colNames.remove(wrkDbColName);
        wrkDbColName.setColType(null);
        return this;
    }

    public void setColNames(Set<WrkDbColName> wrkDbColNames) {
        this.colNames = wrkDbColNames;
    }

    public WrkAnonType getAnonType() {
        return anonType;
    }

    public WrkDbColType anonType(WrkAnonType wrkAnonType) {
        this.anonType = wrkAnonType;
        return this;
    }

    public void setAnonType(WrkAnonType wrkAnonType) {
        this.anonType = wrkAnonType;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WrkDbColType)) {
            return false;
        }
        return id != null && id.equals(((WrkDbColType) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "WrkDbColType{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
