package net.anet.workflow.airflow.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Anonimation.
 */
@Entity
@Table(name = "anonimation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Anonimation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Size(max = 60)
    @Column(name = "type", length = 60)
    private String type;

    @Size(max = 60)
    @Column(name = "description", length = 60)
    private String description;

    @OneToMany(mappedBy = "anonimation")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<WorkflowTask> workflowTasks = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public Anonimation type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public Anonimation description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<WorkflowTask> getWorkflowTasks() {
        return workflowTasks;
    }

    public Anonimation workflowTasks(Set<WorkflowTask> workflowTasks) {
        this.workflowTasks = workflowTasks;
        return this;
    }

    public Anonimation addWorkflowTask(WorkflowTask workflowTask) {
        this.workflowTasks.add(workflowTask);
        workflowTask.setAnonimation(this);
        return this;
    }

    public Anonimation removeWorkflowTask(WorkflowTask workflowTask) {
        this.workflowTasks.remove(workflowTask);
        workflowTask.setAnonimation(null);
        return this;
    }

    public void setWorkflowTasks(Set<WorkflowTask> workflowTasks) {
        this.workflowTasks = workflowTasks;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Anonimation)) {
            return false;
        }
        return id != null && id.equals(((Anonimation) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Anonimation{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
