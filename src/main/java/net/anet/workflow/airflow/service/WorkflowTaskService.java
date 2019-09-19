package net.anet.workflow.airflow.service;

import net.anet.workflow.airflow.domain.WorkflowTask;
import net.anet.workflow.airflow.repository.WorkflowTaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link WorkflowTask}.
 */
@Service
@Transactional
public class WorkflowTaskService {

    private final Logger log = LoggerFactory.getLogger(WorkflowTaskService.class);

    private final WorkflowTaskRepository workflowTaskRepository;

    public WorkflowTaskService(WorkflowTaskRepository workflowTaskRepository) {
        this.workflowTaskRepository = workflowTaskRepository;
    }

    /**
     * Save a workflowTask.
     *
     * @param workflowTask the entity to save.
     * @return the persisted entity.
     */
    public WorkflowTask save(WorkflowTask workflowTask) {
        log.debug("Request to save WorkflowTask : {}", workflowTask);
        return workflowTaskRepository.save(workflowTask);
    }

    /**
     * Get all the workflowTasks.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<WorkflowTask> findAll() {
        log.debug("Request to get all WorkflowTasks");
        return workflowTaskRepository.findAll();
    }


    /**
     * Get one workflowTask by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<WorkflowTask> findOne(Long id) {
        log.debug("Request to get WorkflowTask : {}", id);
        return workflowTaskRepository.findById(id);
    }

    /**
     * Delete the workflowTask by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete WorkflowTask : {}", id);
        workflowTaskRepository.deleteById(id);
    }
}
