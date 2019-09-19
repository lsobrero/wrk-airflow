package net.anet.workflow.airflow.repository;
import net.anet.workflow.airflow.domain.WorkflowTask;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the WorkflowTask entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WorkflowTaskRepository extends JpaRepository<WorkflowTask, Long> {

}
