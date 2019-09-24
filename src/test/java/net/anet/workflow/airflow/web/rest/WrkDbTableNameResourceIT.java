package net.anet.workflow.airflow.web.rest;

import net.anet.workflow.airflow.WrkairflowApp;
import net.anet.workflow.airflow.domain.WrkDbTableName;
import net.anet.workflow.airflow.repository.WrkDbTableNameRepository;
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
 * Integration tests for the {@link WrkDbTableNameResource} REST controller.
 */
@SpringBootTest(classes = WrkairflowApp.class)
public class WrkDbTableNameResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private WrkDbTableNameRepository wrkDbTableNameRepository;

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

    private MockMvc restWrkDbTableNameMockMvc;

    private WrkDbTableName wrkDbTableName;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final WrkDbTableNameResource wrkDbTableNameResource = new WrkDbTableNameResource(wrkDbTableNameRepository);
        this.restWrkDbTableNameMockMvc = MockMvcBuilders.standaloneSetup(wrkDbTableNameResource)
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
    public static WrkDbTableName createEntity(EntityManager em) {
        WrkDbTableName wrkDbTableName = new WrkDbTableName()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION);
        return wrkDbTableName;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WrkDbTableName createUpdatedEntity(EntityManager em) {
        WrkDbTableName wrkDbTableName = new WrkDbTableName()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);
        return wrkDbTableName;
    }

    @BeforeEach
    public void initTest() {
        wrkDbTableName = createEntity(em);
    }

    @Test
    @Transactional
    public void createWrkDbTableName() throws Exception {
        int databaseSizeBeforeCreate = wrkDbTableNameRepository.findAll().size();

        // Create the WrkDbTableName
        restWrkDbTableNameMockMvc.perform(post("/api/wrk-db-table-names")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wrkDbTableName)))
            .andExpect(status().isCreated());

        // Validate the WrkDbTableName in the database
        List<WrkDbTableName> wrkDbTableNameList = wrkDbTableNameRepository.findAll();
        assertThat(wrkDbTableNameList).hasSize(databaseSizeBeforeCreate + 1);
        WrkDbTableName testWrkDbTableName = wrkDbTableNameList.get(wrkDbTableNameList.size() - 1);
        assertThat(testWrkDbTableName.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testWrkDbTableName.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createWrkDbTableNameWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = wrkDbTableNameRepository.findAll().size();

        // Create the WrkDbTableName with an existing ID
        wrkDbTableName.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWrkDbTableNameMockMvc.perform(post("/api/wrk-db-table-names")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wrkDbTableName)))
            .andExpect(status().isBadRequest());

        // Validate the WrkDbTableName in the database
        List<WrkDbTableName> wrkDbTableNameList = wrkDbTableNameRepository.findAll();
        assertThat(wrkDbTableNameList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = wrkDbTableNameRepository.findAll().size();
        // set the field null
        wrkDbTableName.setName(null);

        // Create the WrkDbTableName, which fails.

        restWrkDbTableNameMockMvc.perform(post("/api/wrk-db-table-names")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wrkDbTableName)))
            .andExpect(status().isBadRequest());

        List<WrkDbTableName> wrkDbTableNameList = wrkDbTableNameRepository.findAll();
        assertThat(wrkDbTableNameList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllWrkDbTableNames() throws Exception {
        // Initialize the database
        wrkDbTableNameRepository.saveAndFlush(wrkDbTableName);

        // Get all the wrkDbTableNameList
        restWrkDbTableNameMockMvc.perform(get("/api/wrk-db-table-names?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wrkDbTableName.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
    
    @Test
    @Transactional
    public void getWrkDbTableName() throws Exception {
        // Initialize the database
        wrkDbTableNameRepository.saveAndFlush(wrkDbTableName);

        // Get the wrkDbTableName
        restWrkDbTableNameMockMvc.perform(get("/api/wrk-db-table-names/{id}", wrkDbTableName.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(wrkDbTableName.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingWrkDbTableName() throws Exception {
        // Get the wrkDbTableName
        restWrkDbTableNameMockMvc.perform(get("/api/wrk-db-table-names/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWrkDbTableName() throws Exception {
        // Initialize the database
        wrkDbTableNameRepository.saveAndFlush(wrkDbTableName);

        int databaseSizeBeforeUpdate = wrkDbTableNameRepository.findAll().size();

        // Update the wrkDbTableName
        WrkDbTableName updatedWrkDbTableName = wrkDbTableNameRepository.findById(wrkDbTableName.getId()).get();
        // Disconnect from session so that the updates on updatedWrkDbTableName are not directly saved in db
        em.detach(updatedWrkDbTableName);
        updatedWrkDbTableName
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);

        restWrkDbTableNameMockMvc.perform(put("/api/wrk-db-table-names")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedWrkDbTableName)))
            .andExpect(status().isOk());

        // Validate the WrkDbTableName in the database
        List<WrkDbTableName> wrkDbTableNameList = wrkDbTableNameRepository.findAll();
        assertThat(wrkDbTableNameList).hasSize(databaseSizeBeforeUpdate);
        WrkDbTableName testWrkDbTableName = wrkDbTableNameList.get(wrkDbTableNameList.size() - 1);
        assertThat(testWrkDbTableName.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWrkDbTableName.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingWrkDbTableName() throws Exception {
        int databaseSizeBeforeUpdate = wrkDbTableNameRepository.findAll().size();

        // Create the WrkDbTableName

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWrkDbTableNameMockMvc.perform(put("/api/wrk-db-table-names")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wrkDbTableName)))
            .andExpect(status().isBadRequest());

        // Validate the WrkDbTableName in the database
        List<WrkDbTableName> wrkDbTableNameList = wrkDbTableNameRepository.findAll();
        assertThat(wrkDbTableNameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteWrkDbTableName() throws Exception {
        // Initialize the database
        wrkDbTableNameRepository.saveAndFlush(wrkDbTableName);

        int databaseSizeBeforeDelete = wrkDbTableNameRepository.findAll().size();

        // Delete the wrkDbTableName
        restWrkDbTableNameMockMvc.perform(delete("/api/wrk-db-table-names/{id}", wrkDbTableName.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WrkDbTableName> wrkDbTableNameList = wrkDbTableNameRepository.findAll();
        assertThat(wrkDbTableNameList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WrkDbTableName.class);
        WrkDbTableName wrkDbTableName1 = new WrkDbTableName();
        wrkDbTableName1.setId(1L);
        WrkDbTableName wrkDbTableName2 = new WrkDbTableName();
        wrkDbTableName2.setId(wrkDbTableName1.getId());
        assertThat(wrkDbTableName1).isEqualTo(wrkDbTableName2);
        wrkDbTableName2.setId(2L);
        assertThat(wrkDbTableName1).isNotEqualTo(wrkDbTableName2);
        wrkDbTableName1.setId(null);
        assertThat(wrkDbTableName1).isNotEqualTo(wrkDbTableName2);
    }
}
