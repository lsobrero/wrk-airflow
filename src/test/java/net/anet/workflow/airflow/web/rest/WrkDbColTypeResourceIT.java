package net.anet.workflow.airflow.web.rest;

import net.anet.workflow.airflow.WrkairflowApp;
import net.anet.workflow.airflow.domain.WrkDbColType;
import net.anet.workflow.airflow.repository.WrkDbColTypeRepository;
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
 * Integration tests for the {@link WrkDbColTypeResource} REST controller.
 */
@SpringBootTest(classes = WrkairflowApp.class)
public class WrkDbColTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private WrkDbColTypeRepository wrkDbColTypeRepository;

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

    private MockMvc restWrkDbColTypeMockMvc;

    private WrkDbColType wrkDbColType;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final WrkDbColTypeResource wrkDbColTypeResource = new WrkDbColTypeResource(wrkDbColTypeRepository);
        this.restWrkDbColTypeMockMvc = MockMvcBuilders.standaloneSetup(wrkDbColTypeResource)
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
    public static WrkDbColType createEntity(EntityManager em) {
        WrkDbColType wrkDbColType = new WrkDbColType()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION);
        return wrkDbColType;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WrkDbColType createUpdatedEntity(EntityManager em) {
        WrkDbColType wrkDbColType = new WrkDbColType()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);
        return wrkDbColType;
    }

    @BeforeEach
    public void initTest() {
        wrkDbColType = createEntity(em);
    }

    @Test
    @Transactional
    public void createWrkDbColType() throws Exception {
        int databaseSizeBeforeCreate = wrkDbColTypeRepository.findAll().size();

        // Create the WrkDbColType
        restWrkDbColTypeMockMvc.perform(post("/api/wrk-db-col-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wrkDbColType)))
            .andExpect(status().isCreated());

        // Validate the WrkDbColType in the database
        List<WrkDbColType> wrkDbColTypeList = wrkDbColTypeRepository.findAll();
        assertThat(wrkDbColTypeList).hasSize(databaseSizeBeforeCreate + 1);
        WrkDbColType testWrkDbColType = wrkDbColTypeList.get(wrkDbColTypeList.size() - 1);
        assertThat(testWrkDbColType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testWrkDbColType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createWrkDbColTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = wrkDbColTypeRepository.findAll().size();

        // Create the WrkDbColType with an existing ID
        wrkDbColType.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWrkDbColTypeMockMvc.perform(post("/api/wrk-db-col-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wrkDbColType)))
            .andExpect(status().isBadRequest());

        // Validate the WrkDbColType in the database
        List<WrkDbColType> wrkDbColTypeList = wrkDbColTypeRepository.findAll();
        assertThat(wrkDbColTypeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = wrkDbColTypeRepository.findAll().size();
        // set the field null
        wrkDbColType.setName(null);

        // Create the WrkDbColType, which fails.

        restWrkDbColTypeMockMvc.perform(post("/api/wrk-db-col-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wrkDbColType)))
            .andExpect(status().isBadRequest());

        List<WrkDbColType> wrkDbColTypeList = wrkDbColTypeRepository.findAll();
        assertThat(wrkDbColTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllWrkDbColTypes() throws Exception {
        // Initialize the database
        wrkDbColTypeRepository.saveAndFlush(wrkDbColType);

        // Get all the wrkDbColTypeList
        restWrkDbColTypeMockMvc.perform(get("/api/wrk-db-col-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wrkDbColType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
    
    @Test
    @Transactional
    public void getWrkDbColType() throws Exception {
        // Initialize the database
        wrkDbColTypeRepository.saveAndFlush(wrkDbColType);

        // Get the wrkDbColType
        restWrkDbColTypeMockMvc.perform(get("/api/wrk-db-col-types/{id}", wrkDbColType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(wrkDbColType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingWrkDbColType() throws Exception {
        // Get the wrkDbColType
        restWrkDbColTypeMockMvc.perform(get("/api/wrk-db-col-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWrkDbColType() throws Exception {
        // Initialize the database
        wrkDbColTypeRepository.saveAndFlush(wrkDbColType);

        int databaseSizeBeforeUpdate = wrkDbColTypeRepository.findAll().size();

        // Update the wrkDbColType
        WrkDbColType updatedWrkDbColType = wrkDbColTypeRepository.findById(wrkDbColType.getId()).get();
        // Disconnect from session so that the updates on updatedWrkDbColType are not directly saved in db
        em.detach(updatedWrkDbColType);
        updatedWrkDbColType
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);

        restWrkDbColTypeMockMvc.perform(put("/api/wrk-db-col-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedWrkDbColType)))
            .andExpect(status().isOk());

        // Validate the WrkDbColType in the database
        List<WrkDbColType> wrkDbColTypeList = wrkDbColTypeRepository.findAll();
        assertThat(wrkDbColTypeList).hasSize(databaseSizeBeforeUpdate);
        WrkDbColType testWrkDbColType = wrkDbColTypeList.get(wrkDbColTypeList.size() - 1);
        assertThat(testWrkDbColType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWrkDbColType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingWrkDbColType() throws Exception {
        int databaseSizeBeforeUpdate = wrkDbColTypeRepository.findAll().size();

        // Create the WrkDbColType

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWrkDbColTypeMockMvc.perform(put("/api/wrk-db-col-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wrkDbColType)))
            .andExpect(status().isBadRequest());

        // Validate the WrkDbColType in the database
        List<WrkDbColType> wrkDbColTypeList = wrkDbColTypeRepository.findAll();
        assertThat(wrkDbColTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteWrkDbColType() throws Exception {
        // Initialize the database
        wrkDbColTypeRepository.saveAndFlush(wrkDbColType);

        int databaseSizeBeforeDelete = wrkDbColTypeRepository.findAll().size();

        // Delete the wrkDbColType
        restWrkDbColTypeMockMvc.perform(delete("/api/wrk-db-col-types/{id}", wrkDbColType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WrkDbColType> wrkDbColTypeList = wrkDbColTypeRepository.findAll();
        assertThat(wrkDbColTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WrkDbColType.class);
        WrkDbColType wrkDbColType1 = new WrkDbColType();
        wrkDbColType1.setId(1L);
        WrkDbColType wrkDbColType2 = new WrkDbColType();
        wrkDbColType2.setId(wrkDbColType1.getId());
        assertThat(wrkDbColType1).isEqualTo(wrkDbColType2);
        wrkDbColType2.setId(2L);
        assertThat(wrkDbColType1).isNotEqualTo(wrkDbColType2);
        wrkDbColType1.setId(null);
        assertThat(wrkDbColType1).isNotEqualTo(wrkDbColType2);
    }
}
