package net.anet.workflow.airflow.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A MyDummy.
 */
@Entity
@Table(name = "my_dummy")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MyDummy implements Serializable {

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

    @OneToMany(mappedBy = "colType")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<MyTest> colNames = new HashSet<>();

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

    public MyDummy name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isIsEnabled() {
        return isEnabled;
    }

    public MyDummy isEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
        return this;
    }

    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public Set<MyTest> getColNames() {
        return colNames;
    }

    public MyDummy colNames(Set<MyTest> myTests) {
        this.colNames = myTests;
        return this;
    }

    public MyDummy addColName(MyTest myTest) {
        this.colNames.add(myTest);
        myTest.setColType(this);
        return this;
    }

    public MyDummy removeColName(MyTest myTest) {
        this.colNames.remove(myTest);
        myTest.setColType(null);
        return this;
    }

    public void setColNames(Set<MyTest> myTests) {
        this.colNames = myTests;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MyDummy)) {
            return false;
        }
        return id != null && id.equals(((MyDummy) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "MyDummy{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", isEnabled='" + isIsEnabled() + "'" +
            "}";
    }
}
