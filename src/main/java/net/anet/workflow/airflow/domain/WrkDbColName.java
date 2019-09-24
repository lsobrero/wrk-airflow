package net.anet.workflow.airflow.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A WrkDbColName.
 */
@Entity
@Table(name = "wrk_db_col_name")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class WrkDbColName implements Serializable {

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

    @ManyToOne
    @JsonIgnoreProperties("colNames")
    private WrkDbTableName dbName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties("colNames")
    private WrkDbColType colType;

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

    public WrkDbColName name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public WrkDbColName description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

/*
    public WrkDbTableName getDbName() {
        return dbName;
    }
*/

    public WrkDbColName dbName(WrkDbTableName wrkDbTableName) {
        this.dbName = wrkDbTableName;
        return this;
    }

    public void setDbName(WrkDbTableName wrkDbTableName) {
        this.dbName = wrkDbTableName;
    }

    public WrkDbColType getColType() {
        return colType;
    }

    public WrkDbColName colType(WrkDbColType wrkDbColType) {
        this.colType = wrkDbColType;
        return this;
    }

    public void setColType(WrkDbColType wrkDbColType) {
        this.colType = wrkDbColType;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WrkDbColName)) {
            return false;
        }
        return id != null && id.equals(((WrkDbColName) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "WrkDbColName{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
