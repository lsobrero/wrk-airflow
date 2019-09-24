package net.anet.workflow.airflow.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A WrkAnonType.
 */
@Entity
@Table(name = "wrk_anon_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class WrkAnonType implements Serializable {

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

    @OneToMany(mappedBy = "anonType")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<WrkDbColType> colTypes = new HashSet<>();

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

    public WrkAnonType name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public WrkAnonType description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<WrkDbColType> getColTypes() {
        return colTypes;
    }

    public WrkAnonType colTypes(Set<WrkDbColType> wrkDbColTypes) {
        this.colTypes = wrkDbColTypes;
        return this;
    }

    public WrkAnonType addColType(WrkDbColType wrkDbColType) {
        this.colTypes.add(wrkDbColType);
        wrkDbColType.setAnonType(this);
        return this;
    }

    public WrkAnonType removeColType(WrkDbColType wrkDbColType) {
        this.colTypes.remove(wrkDbColType);
        wrkDbColType.setAnonType(null);
        return this;
    }

    public void setColTypes(Set<WrkDbColType> wrkDbColTypes) {
        this.colTypes = wrkDbColTypes;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WrkAnonType)) {
            return false;
        }
        return id != null && id.equals(((WrkAnonType) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "WrkAnonType{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
