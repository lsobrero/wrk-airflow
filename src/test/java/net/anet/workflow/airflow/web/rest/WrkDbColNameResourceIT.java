package net.anet.workflow.airflow.web.rest;

import net.anet.workflow.airflow.WrkairflowApp;
import net.anet.workflow.airflow.domain.WrkDbColName;
import net.anet.workflow.airflow.repository.WrkDbColNameRepository;
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
 * Integration tests for the {@link WrkDbColNameResource} REST controller.
 */
@SpringBootTest(classes = WrkairflowApp.class)
public class WrkDbColNameResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private WrkDbColNameRepository wrkDbColNameRepository;

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

    private MockMvc restWrkDbColNameMockMvc;

    private WrkDbColName wrkDbColName;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final WrkDbColNameResource wrkDbColNameResource = new WrkDbColNameResource(wrkDbColNameRepository);
        this.restWrkDbColNameMockMvc = MockMvcBuilders.standaloneSetup(wrkDbColNameResource)
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
    public static WrkDbColName createEntity(EntityManager em) {
        WrkDbColName wrkDbColName = new WrkDbColName()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION);
        return wrkDbColName;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WrkDbColName createUpdatedEntity(EntityManager em) {
        WrkDbColName wrkDbColName = new WrkDbColName()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);
        return wrkDbColName;
    }

    @BeforeEach
    public void initTest() {
        wrkDbColName = createEntity(em);
    }

    @Test
    @Transactional
    public void createWrkDbColName() throws Exception {
        int databaseSizeBeforeCreate = wrkDbColNameRepository.findAll().size();

        // Create the WrkDbColName
        restWrkDbColNameMockMvc.perform(post("/api/wrk-db-col-names")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wrkDbColName)))
            .andExpect(status().isCreated());

        // Validate the WrkDbColName in the database
        List<WrkDbColName> wrkDbColNameList = wrkDbColNameRepository.findAll();
        assertThat(wrkDbColNameList).hasSize(databaseSizeBeforeCreate + 1);
        WrkDbColName testWrkDbColName = wrkDbColNameList.get(wrkDbColNameList.size() - 1);
        assertThat(testWrkDbColName.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testWrkDbColName.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createWrkDbColNameWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = wrkDbColNameRepository.findAll().size();

        // Create the WrkDbColName with an existing ID
        wrkDbColName.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWrkDbColNameMockMvc.perform(post("/api/wrk-db-col-names")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wrkDbColName)))
            .andExpect(status().isBadRequest());

        // Validate the WrkDbColName in the database
        List<WrkDbColName> wrkDbColNameList = wrkDbColNameRepository.findAll();
        assertThat(wrkDbColNameList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = wrkDbColNameRepository.findAll().size();
        // set the field null
        wrkDbColName.setName(null);

        // Create the WrkDbColName, which fails.

        restWrkDbColNameMockMvc.perform(post("/api/wrk-db-col-names")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wrkDbColName)))
            .andExpect(status().isBadRequest());

        List<WrkDbColName> wrkDbColNameList = wrkDbColNameRepository.findAll();
        assertThat(wrkDbColNameList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllWrkDbColNames() throws Exception {
        // Initialize the database
        wrkDbColNameRepository.saveAndFlush(wrkDbColName);

        // Get all the wrkDbColNameList
        restWrkDbColNameMockMvc.perform(get("/api/wrk-db-col-names?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wrkDbColName.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
    
    @Test
    @Transactional
    public void getWrkDbColName() throws Exception {
        // Initialize the database
        wrkDbColNameRepository.saveAndFlush(wrkDbColName);

        // Get the wrkDbColName
        restWrkDbColNameMockMvc.perform(get("/api/wrk-db-col-names/{id}", wrkDbColName.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(wrkDbColName.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingWrkDbColName() throws Exception {
        // Get the wrkDbColName
        restWrkDbColNameMockMvc.perform(get("/api/wrk-db-col-names/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWrkDbColName() throws Exception {
        // Initialize the database
        wrkDbColNameRepository.saveAndFlush(wrkDbColName);

        int databaseSizeBeforeUpdate = wrkDbColNameRepository.findAll().size();

        // Update the wrkDbColName
        WrkDbColName updatedWrkDbColName = wrkDbColNameRepository.findById(wrkDbColName.getId()).get();
        // Disconnect from session so that the updates on updatedWrkDbColName are not directly saved in db
        em.detach(updatedWrkDbColName);
        updatedWrkDbColName
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);

        restWrkDbColNameMockMvc.perform(put("/api/wrk-db-col-names")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedWrkDbColName)))
            .andExpect(status().isOk());

        // Validate the WrkDbColName in the database
        List<WrkDbColName> wrkDbColNameList = wrkDbColNameRepository.findAll();
        assertThat(wrkDbColNameList).hasSize(databaseSizeBeforeUpdate);
        WrkDbColName testWrkDbColName = wrkDbColNameList.get(wrkDbColNameList.size() - 1);
        assertThat(testWrkDbColName.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWrkDbColName.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingWrkDbColName() throws Exception {
        int databaseSizeBeforeUpdate = wrkDbColNameRepository.findAll().size();

        // Create the WrkDbColName

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWrkDbColNameMockMvc.perform(put("/api/wrk-db-col-names")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wrkDbColName)))
            .andExpect(status().isBadRequest());

        // Validate the WrkDbColName in the database
        List<WrkDbColName> wrkDbColNameList = wrkDbColNameRepository.findAll();
        assertThat(wrkDbColNameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteWrkDbColName() throws Exception {
        // Initialize the database
        wrkDbColNameRepository.saveAndFlush(wrkDbColName);

        int databaseSizeBeforeDelete = wrkDbColNameRepository.findAll().size();

        // Delete the wrkDbColName
        restWrkDbColNameMockMvc.perform(delete("/api/wrk-db-col-names/{id}", wrkDbColName.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WrkDbColName> wrkDbColNameList = wrkDbColNameRepository.findAll();
        assertThat(wrkDbColNameList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WrkDbColName.class);
        WrkDbColName wrkDbColName1 = new WrkDbColName();
        wrkDbColName1.setId(1L);
        WrkDbColName wrkDbColName2 = new WrkDbColName();
        wrkDbColName2.setId(wrkDbColName1.getId());
        assertThat(wrkDbColName1).isEqualTo(wrkDbColName2);
        wrkDbColName2.setId(2L);
        assertThat(wrkDbColName1).isNotEqualTo(wrkDbColName2);
        wrkDbColName1.setId(null);
        assertThat(wrkDbColName1).isNotEqualTo(wrkDbColName2);
    }
}
