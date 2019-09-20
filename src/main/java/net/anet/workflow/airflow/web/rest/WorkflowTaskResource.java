package net.anet.workflow.airflow.web.rest;

import net.anet.workflow.airflow.domain.WorkflowTask;
import net.anet.workflow.airflow.service.WorkflowTaskService;
import net.anet.workflow.airflow.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link net.anet.workflow.airflow.domain.WorkflowTask}.
 */
@RestController
@RequestMapping("/api")
public class WorkflowTaskResource {

    private final Logger log = LoggerFactory.getLogger(WorkflowTaskResource.class);

    private static final String ENTITY_NAME = "wrkairflowWorkflowTask";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorkflowTaskService workflowTaskService;

    public WorkflowTaskResource(WorkflowTaskService workflowTaskService) {
        this.workflowTaskService = workflowTaskService;
    }

    /**
     * {@code POST  /workflow-tasks} : Create a new workflowTask.
     *
     * @param workflowTask the workflowTask to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new workflowTask, or with status {@code 400 (Bad Request)} if the workflowTask has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/workflow-tasks")
    public ResponseEntity<WorkflowTask> createWorkflowTask(@Valid @RequestBody WorkflowTask workflowTask) throws URISyntaxException {
        log.debug("REST request to save WorkflowTask : {}", workflowTask);
        if (workflowTask.getId() != null) {
            throw new BadRequestAlertException("A new workflowTask cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WorkflowTask result = workflowTaskService.save(workflowTask);
        return ResponseEntity.created(new URI("/api/workflow-tasks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /workflow-tasks} : Updates an existing workflowTask.
     *
     * @param workflowTask the workflowTask to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workflowTask,
     * or with status {@code 400 (Bad Request)} if the workflowTask is not valid,
     * or with status {@code 500 (Internal Server Error)} if the workflowTask couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/workflow-tasks")
    public ResponseEntity<WorkflowTask> updateWorkflowTask(@Valid @RequestBody WorkflowTask workflowTask) throws URISyntaxException {
        log.debug("REST request to update WorkflowTask : {}", workflowTask);
        if (workflowTask.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        WorkflowTask result = workflowTaskService.save(workflowTask);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workflowTask.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /workflow-tasks} : get all the workflowTasks.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workflowTasks in body.
     */
    @GetMapping("/workflow-tasks")
    public List<WorkflowTask> getAllWorkflowTasks() {
        log.debug("REST request to get all WorkflowTasks");
        return workflowTaskService.findAll();
    }

    /**
     * {@code GET  /workflow-tasks/:id} : get the "id" workflowTask.
     *
     * @param id the id of the workflowTask to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workflowTask, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/workflow-tasks/{id}")
    public ResponseEntity<WorkflowTask> getWorkflowTask(@PathVariable Long id) {
        log.debug("REST request to get WorkflowTask : {}", id);
        Optional<WorkflowTask> workflowTask = workflowTaskService.findOne(id);
        return ResponseUtil.wrapOrNotFound(workflowTask);
    }

    /**
     * {@code DELETE  /workflow-tasks/:id} : delete the "id" workflowTask.
     *
     * @param id the id of the workflowTask to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/workflow-tasks/{id}")
    public ResponseEntity<Void> deleteWorkflowTask(@PathVariable Long id) {
        log.debug("REST request to delete WorkflowTask : {}", id);
        workflowTaskService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
