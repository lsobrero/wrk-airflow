package net.anet.workflow.airflow.web.rest;

import net.anet.workflow.airflow.WrkairflowApp;
import net.anet.workflow.airflow.domain.WrkDatabase;
import net.anet.workflow.airflow.repository.WrkDatabaseRepository;
import net.anet.workflow.airflow.service.WrkDatabaseService;
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
 * Integration tests for the {@link WrkDatabaseResource} REST controller.
 */
@SpringBootTest(classes = WrkairflowApp.class)
public class WrkDatabaseResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_HOST = "AAAAAAAAAA";
    private static final String UPDATED_HOST = "BBBBBBBBBB";

    private static final String DEFAULT_PORT = "AAAAAAAAAA";
    private static final String UPDATED_PORT = "BBBBBBBBBB";

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_PAS_USER = "AAAAAAAAAA";
    private static final String UPDATED_PAS_USER = "BBBBBBBBBB";

    @Autowired
    private WrkDatabaseRepository wrkDatabaseRepository;

    @Autowired
    private WrkDatabaseService wrkDatabaseService;

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

    private MockMvc restWrkDatabaseMockMvc;

    private WrkDatabase wrkDatabase;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final WrkDatabaseResource wrkDatabaseResource = new WrkDatabaseResource(wrkDatabaseService);
        this.restWrkDatabaseMockMvc = MockMvcBuilders.standaloneSetup(wrkDatabaseResource)
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
    public static WrkDatabase createEntity(EntityManager em) {
        WrkDatabase wrkDatabase = new WrkDatabase()
            .name(DEFAULT_NAME)
            .host(DEFAULT_HOST)
            .port(DEFAULT_PORT)
            .username(DEFAULT_USERNAME)
            .pasUser(DEFAULT_PAS_USER);
        return wrkDatabase;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WrkDatabase createUpdatedEntity(EntityManager em) {
        WrkDatabase wrkDatabase = new WrkDatabase()
            .name(UPDATED_NAME)
            .host(UPDATED_HOST)
            .port(UPDATED_PORT)
            .username(UPDATED_USERNAME)
            .pasUser(UPDATED_PAS_USER);
        return wrkDatabase;
    }

    @BeforeEach
    public void initTest() {
        wrkDatabase = createEntity(em);
    }

    @Test
    @Transactional
    public void createWrkDatabase() throws Exception {
        int databaseSizeBeforeCreate = wrkDatabaseRepository.findAll().size();

        // Create the WrkDatabase
        restWrkDatabaseMockMvc.perform(post("/api/wrk-databases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wrkDatabase)))
            .andExpect(status().isCreated());

        // Validate the WrkDatabase in the database
        List<WrkDatabase> wrkDatabaseList = wrkDatabaseRepository.findAll();
        assertThat(wrkDatabaseList).hasSize(databaseSizeBeforeCreate + 1);
        WrkDatabase testWrkDatabase = wrkDatabaseList.get(wrkDatabaseList.size() - 1);
        assertThat(testWrkDatabase.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testWrkDatabase.getHost()).isEqualTo(DEFAULT_HOST);
        assertThat(testWrkDatabase.getPort()).isEqualTo(DEFAULT_PORT);
        assertThat(testWrkDatabase.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testWrkDatabase.getPasUser()).isEqualTo(DEFAULT_PAS_USER);
    }

    @Test
    @Transactional
    public void createWrkDatabaseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = wrkDatabaseRepository.findAll().size();

        // Create the WrkDatabase with an existing ID
        wrkDatabase.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWrkDatabaseMockMvc.perform(post("/api/wrk-databases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wrkDatabase)))
            .andExpect(status().isBadRequest());

        // Validate the WrkDatabase in the database
        List<WrkDatabase> wrkDatabaseList = wrkDatabaseRepository.findAll();
        assertThat(wrkDatabaseList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = wrkDatabaseRepository.findAll().size();
        // set the field null
        wrkDatabase.setName(null);

        // Create the WrkDatabase, which fails.

        restWrkDatabaseMockMvc.perform(post("/api/wrk-databases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wrkDatabase)))
            .andExpect(status().isBadRequest());

        List<WrkDatabase> wrkDatabaseList = wrkDatabaseRepository.findAll();
        assertThat(wrkDatabaseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkHostIsRequired() throws Exception {
        int databaseSizeBeforeTest = wrkDatabaseRepository.findAll().size();
        // set the field null
        wrkDatabase.setHost(null);

        // Create the WrkDatabase, which fails.

        restWrkDatabaseMockMvc.perform(post("/api/wrk-databases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wrkDatabase)))
            .andExpect(status().isBadRequest());

        List<WrkDatabase> wrkDatabaseList = wrkDatabaseRepository.findAll();
        assertThat(wrkDatabaseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUsernameIsRequired() throws Exception {
        int databaseSizeBeforeTest = wrkDatabaseRepository.findAll().size();
        // set the field null
        wrkDatabase.setUsername(null);

        // Create the WrkDatabase, which fails.

        restWrkDatabaseMockMvc.perform(post("/api/wrk-databases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wrkDatabase)))
            .andExpect(status().isBadRequest());

        List<WrkDatabase> wrkDatabaseList = wrkDatabaseRepository.findAll();
        assertThat(wrkDatabaseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllWrkDatabases() throws Exception {
        // Initialize the database
        wrkDatabaseRepository.saveAndFlush(wrkDatabase);

        // Get all the wrkDatabaseList
        restWrkDatabaseMockMvc.perform(get("/api/wrk-databases?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wrkDatabase.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].host").value(hasItem(DEFAULT_HOST.toString())))
            .andExpect(jsonPath("$.[*].port").value(hasItem(DEFAULT_PORT.toString())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME.toString())))
            .andExpect(jsonPath("$.[*].pasUser").value(hasItem(DEFAULT_PAS_USER.toString())));
    }
    
    @Test
    @Transactional
    public void getWrkDatabase() throws Exception {
        // Initialize the database
        wrkDatabaseRepository.saveAndFlush(wrkDatabase);

        // Get the wrkDatabase
        restWrkDatabaseMockMvc.perform(get("/api/wrk-databases/{id}", wrkDatabase.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(wrkDatabase.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.host").value(DEFAULT_HOST.toString()))
            .andExpect(jsonPath("$.port").value(DEFAULT_PORT.toString()))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME.toString()))
            .andExpect(jsonPath("$.pasUser").value(DEFAULT_PAS_USER.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingWrkDatabase() throws Exception {
        // Get the wrkDatabase
        restWrkDatabaseMockMvc.perform(get("/api/wrk-databases/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWrkDatabase() throws Exception {
        // Initialize the database
        wrkDatabaseService.save(wrkDatabase);

        int databaseSizeBeforeUpdate = wrkDatabaseRepository.findAll().size();

        // Update the wrkDatabase
        WrkDatabase updatedWrkDatabase = wrkDatabaseRepository.findById(wrkDatabase.getId()).get();
        // Disconnect from session so that the updates on updatedWrkDatabase are not directly saved in db
        em.detach(updatedWrkDatabase);
        updatedWrkDatabase
            .name(UPDATED_NAME)
            .host(UPDATED_HOST)
            .port(UPDATED_PORT)
            .username(UPDATED_USERNAME)
            .pasUser(UPDATED_PAS_USER);

        restWrkDatabaseMockMvc.perform(put("/api/wrk-databases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedWrkDatabase)))
            .andExpect(status().isOk());

        // Validate the WrkDatabase in the database
        List<WrkDatabase> wrkDatabaseList = wrkDatabaseRepository.findAll();
        assertThat(wrkDatabaseList).hasSize(databaseSizeBeforeUpdate);
        WrkDatabase testWrkDatabase = wrkDatabaseList.get(wrkDatabaseList.size() - 1);
        assertThat(testWrkDatabase.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWrkDatabase.getHost()).isEqualTo(UPDATED_HOST);
        assertThat(testWrkDatabase.getPort()).isEqualTo(UPDATED_PORT);
        assertThat(testWrkDatabase.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testWrkDatabase.getPasUser()).isEqualTo(UPDATED_PAS_USER);
    }

    @Test
    @Transactional
    public void updateNonExistingWrkDatabase() throws Exception {
        int databaseSizeBeforeUpdate = wrkDatabaseRepository.findAll().size();

        // Create the WrkDatabase

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWrkDatabaseMockMvc.perform(put("/api/wrk-databases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wrkDatabase)))
            .andExpect(status().isBadRequest());

        // Validate the WrkDatabase in the database
        List<WrkDatabase> wrkDatabaseList = wrkDatabaseRepository.findAll();
        assertThat(wrkDatabaseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteWrkDatabase() throws Exception {
        // Initialize the database
        wrkDatabaseService.save(wrkDatabase);

        int databaseSizeBeforeDelete = wrkDatabaseRepository.findAll().size();

        // Delete the wrkDatabase
        restWrkDatabaseMockMvc.perform(delete("/api/wrk-databases/{id}", wrkDatabase.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WrkDatabase> wrkDatabaseList = wrkDatabaseRepository.findAll();
        assertThat(wrkDatabaseList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WrkDatabase.class);
        WrkDatabase wrkDatabase1 = new WrkDatabase();
        wrkDatabase1.setId(1L);
        WrkDatabase wrkDatabase2 = new WrkDatabase();
        wrkDatabase2.setId(wrkDatabase1.getId());
        assertThat(wrkDatabase1).isEqualTo(wrkDatabase2);
        wrkDatabase2.setId(2L);
        assertThat(wrkDatabase1).isNotEqualTo(wrkDatabase2);
        wrkDatabase1.setId(null);
        assertThat(wrkDatabase1).isNotEqualTo(wrkDatabase2);
    }
}
