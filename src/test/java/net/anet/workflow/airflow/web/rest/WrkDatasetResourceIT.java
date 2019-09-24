package net.anet.workflow.airflow.web.rest;

import net.anet.workflow.airflow.WrkairflowApp;
import net.anet.workflow.airflow.domain.WrkDataset;
import net.anet.workflow.airflow.repository.WrkDatasetRepository;
import net.anet.workflow.airflow.service.WrkDatasetService;
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
 * Integration tests for the {@link WrkDatasetResource} REST controller.
 */
@SpringBootTest(classes = WrkairflowApp.class)
public class WrkDatasetResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ENABLED = false;
    private static final Boolean UPDATED_IS_ENABLED = true;

    @Autowired
    private WrkDatasetRepository wrkDatasetRepository;

    @Autowired
    private WrkDatasetService wrkDatasetService;

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

    private MockMvc restWrkDatasetMockMvc;

    private WrkDataset wrkDataset;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final WrkDatasetResource wrkDatasetResource = new WrkDatasetResource(wrkDatasetService);
        this.restWrkDatasetMockMvc = MockMvcBuilders.standaloneSetup(wrkDatasetResource)
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
    public static WrkDataset createEntity(EntityManager em) {
        WrkDataset wrkDataset = new WrkDataset()
            .name(DEFAULT_NAME)
            .isEnabled(DEFAULT_IS_ENABLED);
        return wrkDataset;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WrkDataset createUpdatedEntity(EntityManager em) {
        WrkDataset wrkDataset = new WrkDataset()
            .name(UPDATED_NAME)
            .isEnabled(UPDATED_IS_ENABLED);
        return wrkDataset;
    }

    @BeforeEach
    public void initTest() {
        wrkDataset = createEntity(em);
    }

    @Test
    @Transactional
    public void createWrkDataset() throws Exception {
        int databaseSizeBeforeCreate = wrkDatasetRepository.findAll().size();

        // Create the WrkDataset
        restWrkDatasetMockMvc.perform(post("/api/wrk-datasets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wrkDataset)))
            .andExpect(status().isCreated());

        // Validate the WrkDataset in the database
        List<WrkDataset> wrkDatasetList = wrkDatasetRepository.findAll();
        assertThat(wrkDatasetList).hasSize(databaseSizeBeforeCreate + 1);
        WrkDataset testWrkDataset = wrkDatasetList.get(wrkDatasetList.size() - 1);
        assertThat(testWrkDataset.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testWrkDataset.isIsEnabled()).isEqualTo(DEFAULT_IS_ENABLED);
    }

    @Test
    @Transactional
    public void createWrkDatasetWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = wrkDatasetRepository.findAll().size();

        // Create the WrkDataset with an existing ID
        wrkDataset.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWrkDatasetMockMvc.perform(post("/api/wrk-datasets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wrkDataset)))
            .andExpect(status().isBadRequest());

        // Validate the WrkDataset in the database
        List<WrkDataset> wrkDatasetList = wrkDatasetRepository.findAll();
        assertThat(wrkDatasetList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = wrkDatasetRepository.findAll().size();
        // set the field null
        wrkDataset.setName(null);

        // Create the WrkDataset, which fails.

        restWrkDatasetMockMvc.perform(post("/api/wrk-datasets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wrkDataset)))
            .andExpect(status().isBadRequest());

        List<WrkDataset> wrkDatasetList = wrkDatasetRepository.findAll();
        assertThat(wrkDatasetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIsEnabledIsRequired() throws Exception {
        int databaseSizeBeforeTest = wrkDatasetRepository.findAll().size();
        // set the field null
        wrkDataset.setIsEnabled(null);

        // Create the WrkDataset, which fails.

        restWrkDatasetMockMvc.perform(post("/api/wrk-datasets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wrkDataset)))
            .andExpect(status().isBadRequest());

        List<WrkDataset> wrkDatasetList = wrkDatasetRepository.findAll();
        assertThat(wrkDatasetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllWrkDatasets() throws Exception {
        // Initialize the database
        wrkDatasetRepository.saveAndFlush(wrkDataset);

        // Get all the wrkDatasetList
        restWrkDatasetMockMvc.perform(get("/api/wrk-datasets?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wrkDataset.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].isEnabled").value(hasItem(DEFAULT_IS_ENABLED.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getWrkDataset() throws Exception {
        // Initialize the database
        wrkDatasetRepository.saveAndFlush(wrkDataset);

        // Get the wrkDataset
        restWrkDatasetMockMvc.perform(get("/api/wrk-datasets/{id}", wrkDataset.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(wrkDataset.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.isEnabled").value(DEFAULT_IS_ENABLED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingWrkDataset() throws Exception {
        // Get the wrkDataset
        restWrkDatasetMockMvc.perform(get("/api/wrk-datasets/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWrkDataset() throws Exception {
        // Initialize the database
        wrkDatasetService.save(wrkDataset);

        int databaseSizeBeforeUpdate = wrkDatasetRepository.findAll().size();

        // Update the wrkDataset
        WrkDataset updatedWrkDataset = wrkDatasetRepository.findById(wrkDataset.getId()).get();
        // Disconnect from session so that the updates on updatedWrkDataset are not directly saved in db
        em.detach(updatedWrkDataset);
        updatedWrkDataset
            .name(UPDATED_NAME)
            .isEnabled(UPDATED_IS_ENABLED);

        restWrkDatasetMockMvc.perform(put("/api/wrk-datasets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedWrkDataset)))
            .andExpect(status().isOk());

        // Validate the WrkDataset in the database
        List<WrkDataset> wrkDatasetList = wrkDatasetRepository.findAll();
        assertThat(wrkDatasetList).hasSize(databaseSizeBeforeUpdate);
        WrkDataset testWrkDataset = wrkDatasetList.get(wrkDatasetList.size() - 1);
        assertThat(testWrkDataset.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWrkDataset.isIsEnabled()).isEqualTo(UPDATED_IS_ENABLED);
    }

    @Test
    @Transactional
    public void updateNonExistingWrkDataset() throws Exception {
        int databaseSizeBeforeUpdate = wrkDatasetRepository.findAll().size();

        // Create the WrkDataset

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWrkDatasetMockMvc.perform(put("/api/wrk-datasets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wrkDataset)))
            .andExpect(status().isBadRequest());

        // Validate the WrkDataset in the database
        List<WrkDataset> wrkDatasetList = wrkDatasetRepository.findAll();
        assertThat(wrkDatasetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteWrkDataset() throws Exception {
        // Initialize the database
        wrkDatasetService.save(wrkDataset);

        int databaseSizeBeforeDelete = wrkDatasetRepository.findAll().size();

        // Delete the wrkDataset
        restWrkDatasetMockMvc.perform(delete("/api/wrk-datasets/{id}", wrkDataset.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WrkDataset> wrkDatasetList = wrkDatasetRepository.findAll();
        assertThat(wrkDatasetList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WrkDataset.class);
        WrkDataset wrkDataset1 = new WrkDataset();
        wrkDataset1.setId(1L);
        WrkDataset wrkDataset2 = new WrkDataset();
        wrkDataset2.setId(wrkDataset1.getId());
        assertThat(wrkDataset1).isEqualTo(wrkDataset2);
        wrkDataset2.setId(2L);
        assertThat(wrkDataset1).isNotEqualTo(wrkDataset2);
        wrkDataset1.setId(null);
        assertThat(wrkDataset1).isNotEqualTo(wrkDataset2);
    }
}
