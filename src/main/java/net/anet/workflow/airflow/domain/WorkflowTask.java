package net.anet.workflow.airflow.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A WorkflowTask.
 */
@Entity
@Table(name = "workflow_task")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class WorkflowTask implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Size(max = 60)
    @Column(name = "name", length = 60)
    private String name;

    @Size(max = 60)
    @Column(name = "description", length = 60)
    private String description;

    @OneToMany(mappedBy = "workflowTask")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Anonimation> anonimations = new HashSet<>();

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

    public WorkflowTask name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public WorkflowTask description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Anonimation> getAnonimations() {
        return anonimations;
    }

    public WorkflowTask anonimations(Set<Anonimation> anonimations) {
        this.anonimations = anonimations;
        return this;
    }

    public WorkflowTask addAnonimation(Anonimation anonimation) {
        this.anonimations.add(anonimation);
        anonimation.setWorkflowTask(this);
        return this;
    }

    public WorkflowTask removeAnonimation(Anonimation anonimation) {
        this.anonimations.remove(anonimation);
        anonimation.setWorkflowTask(null);
        return this;
    }

    public void setAnonimations(Set<Anonimation> anonimations) {
        this.anonimations = anonimations;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkflowTask)) {
            return false;
        }
        return id != null && id.equals(((WorkflowTask) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "WorkflowTask{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
