package net.anet.workflow.airflow.web.rest;

import net.anet.workflow.airflow.AirflowApp;
import net.anet.workflow.airflow.domain.WorkflowTask;
import net.anet.workflow.airflow.repository.WorkflowTaskRepository;
import net.anet.workflow.airflow.service.WorkflowTaskService;
import net.anet.workflow.airflow.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static net.anet.workflow.airflow.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link WorkflowTaskResource} REST controller.
 */
@SpringBootTest(classes = AirflowApp.class)
public class WorkflowTaskResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private WorkflowTaskRepository workflowTaskRepository;

    @Autowired
    private WorkflowTaskService workflowTaskService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restWorkflowTaskMockMvc;

    private WorkflowTask workflowTask;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final WorkflowTaskResource workflowTaskResource = new WorkflowTaskResource(workflowTaskService);
        this.restWorkflowTaskMockMvc = MockMvcBuilders.standaloneSetup(workflowTaskResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkflowTask createEntity(EntityManager em) {
        WorkflowTask workflowTask = new WorkflowTask()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION);
        return workflowTask;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkflowTask createUpdatedEntity(EntityManager em) {
        WorkflowTask workflowTask = new WorkflowTask()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);
        return workflowTask;
    }

    @BeforeEach
    public void initTest() {
        workflowTask = createEntity(em);
    }

    @Test
    @Transactional
    public void createWorkflowTask() throws Exception {
        int databaseSizeBeforeCreate = workflowTaskRepository.findAll().size();

        // Create the WorkflowTask
        restWorkflowTaskMockMvc.perform(post("/api/workflow-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workflowTask)))
            .andExpect(status().isCreated());

        // Validate the WorkflowTask in the database
        List<WorkflowTask> workflowTaskList = workflowTaskRepository.findAll();
        assertThat(workflowTaskList).hasSize(databaseSizeBeforeCreate + 1);
        WorkflowTask testWorkflowTask = workflowTaskList.get(workflowTaskList.size() - 1);
        assertThat(testWorkflowTask.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testWorkflowTask.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createWorkflowTaskWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = workflowTaskRepository.findAll().size();

        // Create the WorkflowTask with an existing ID
        workflowTask.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkflowTaskMockMvc.perform(post("/api/workflow-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workflowTask)))
            .andExpect(status().isBadRequest());

        // Validate the WorkflowTask in the database
        List<WorkflowTask> workflowTaskList = workflowTaskRepository.findAll();
        assertThat(workflowTaskList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllWorkflowTasks() throws Exception {
        // Initialize the database
        workflowTaskRepository.saveAndFlush(workflowTask);

        // Get all the workflowTaskList
        restWorkflowTaskMockMvc.perform(get("/api/workflow-tasks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workflowTask.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
    
    @Test
    @Transactional
    public void getWorkflowTask() throws Exception {
        // Initialize the database
        workflowTaskRepository.saveAndFlush(workflowTask);

        // Get the workflowTask
        restWorkflowTaskMockMvc.perform(get("/api/workflow-tasks/{id}", workflowTask.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(workflowTask.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingWorkflowTask() throws Exception {
        // Get the workflowTask
        restWorkflowTaskMockMvc.perform(get("/api/workflow-tasks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWorkflowTask() throws Exception {
        // Initialize the database
        workflowTaskService.save(workflowTask);

        int databaseSizeBeforeUpdate = workflowTaskRepository.findAll().size();

        // Update the workflowTask
        WorkflowTask updatedWorkflowTask = workflowTaskRepository.findById(workflowTask.getId()).get();
        // Disconnect from session so that the updates on updatedWorkflowTask are not directly saved in db
        em.detach(updatedWorkflowTask);
        updatedWorkflowTask
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);

        restWorkflowTaskMockMvc.perform(put("/api/workflow-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedWorkflowTask)))
            .andExpect(status().isOk());

        // Validate the WorkflowTask in the database
        List<WorkflowTask> workflowTaskList = workflowTaskRepository.findAll();
        assertThat(workflowTaskList).hasSize(databaseSizeBeforeUpdate);
        WorkflowTask testWorkflowTask = workflowTaskList.get(workflowTaskList.size() - 1);
        assertThat(testWorkflowTask.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWorkflowTask.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingWorkflowTask() throws Exception {
        int databaseSizeBeforeUpdate = workflowTaskRepository.findAll().size();

        // Create the WorkflowTask

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkflowTaskMockMvc.perform(put("/api/workflow-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workflowTask)))
            .andExpect(status().isBadRequest());

        // Validate the WorkflowTask in the database
        List<WorkflowTask> workflowTaskList = workflowTaskRepository.findAll();
        assertThat(workflowTaskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteWorkflowTask() throws Exception {
        // Initialize the database
        workflowTaskService.save(workflowTask);

        int databaseSizeBeforeDelete = workflowTaskRepository.findAll().size();

        // Delete the workflowTask
        restWorkflowTaskMockMvc.perform(delete("/api/workflow-tasks/{id}", workflowTask.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WorkflowTask> workflowTaskList = workflowTaskRepository.findAll();
        assertThat(workflowTaskList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkflowTask.class);
        WorkflowTask workflowTask1 = new WorkflowTask();
        workflowTask1.setId(1L);
        WorkflowTask workflowTask2 = new WorkflowTask();
        workflowTask2.setId(workflowTask1.getId());
        assertThat(workflowTask1).isEqualTo(workflowTask2);
        workflowTask2.setId(2L);
        assertThat(workflowTask1).isNotEqualTo(workflowTask2);
        workflowTask1.setId(null);
        assertThat(workflowTask1).isNotEqualTo(workflowTask2);
    }
}
