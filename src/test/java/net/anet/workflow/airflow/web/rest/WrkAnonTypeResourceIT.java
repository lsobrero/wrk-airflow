package net.anet.workflow.airflow.web.rest;

import net.anet.workflow.airflow.WrkairflowApp;
import net.anet.workflow.airflow.domain.WrkAnonType;
import net.anet.workflow.airflow.repository.WrkAnonTypeRepository;
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
 * Integration tests for the {@link WrkAnonTypeResource} REST controller.
 */
@SpringBootTest(classes = WrkairflowApp.class)
public class WrkAnonTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private WrkAnonTypeRepository wrkAnonTypeRepository;

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

    private MockMvc restWrkAnonTypeMockMvc;

    private WrkAnonType wrkAnonType;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final WrkAnonTypeResource wrkAnonTypeResource = new WrkAnonTypeResource(wrkAnonTypeRepository);
        this.restWrkAnonTypeMockMvc = MockMvcBuilders.standaloneSetup(wrkAnonTypeResource)
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
    public static WrkAnonType createEntity(EntityManager em) {
        WrkAnonType wrkAnonType = new WrkAnonType()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION);
        return wrkAnonType;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WrkAnonType createUpdatedEntity(EntityManager em) {
        WrkAnonType wrkAnonType = new WrkAnonType()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);
        return wrkAnonType;
    }

    @BeforeEach
    public void initTest() {
        wrkAnonType = createEntity(em);
    }

    @Test
    @Transactional
    public void createWrkAnonType() throws Exception {
        int databaseSizeBeforeCreate = wrkAnonTypeRepository.findAll().size();

        // Create the WrkAnonType
        restWrkAnonTypeMockMvc.perform(post("/api/wrk-anon-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wrkAnonType)))
            .andExpect(status().isCreated());

        // Validate the WrkAnonType in the database
        List<WrkAnonType> wrkAnonTypeList = wrkAnonTypeRepository.findAll();
        assertThat(wrkAnonTypeList).hasSize(databaseSizeBeforeCreate + 1);
        WrkAnonType testWrkAnonType = wrkAnonTypeList.get(wrkAnonTypeList.size() - 1);
        assertThat(testWrkAnonType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testWrkAnonType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createWrkAnonTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = wrkAnonTypeRepository.findAll().size();

        // Create the WrkAnonType with an existing ID
        wrkAnonType.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWrkAnonTypeMockMvc.perform(post("/api/wrk-anon-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wrkAnonType)))
            .andExpect(status().isBadRequest());

        // Validate the WrkAnonType in the database
        List<WrkAnonType> wrkAnonTypeList = wrkAnonTypeRepository.findAll();
        assertThat(wrkAnonTypeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = wrkAnonTypeRepository.findAll().size();
        // set the field null
        wrkAnonType.setName(null);

        // Create the WrkAnonType, which fails.

        restWrkAnonTypeMockMvc.perform(post("/api/wrk-anon-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wrkAnonType)))
            .andExpect(status().isBadRequest());

        List<WrkAnonType> wrkAnonTypeList = wrkAnonTypeRepository.findAll();
        assertThat(wrkAnonTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllWrkAnonTypes() throws Exception {
        // Initialize the database
        wrkAnonTypeRepository.saveAndFlush(wrkAnonType);

        // Get all the wrkAnonTypeList
        restWrkAnonTypeMockMvc.perform(get("/api/wrk-anon-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wrkAnonType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
    
    @Test
    @Transactional
    public void getWrkAnonType() throws Exception {
        // Initialize the database
        wrkAnonTypeRepository.saveAndFlush(wrkAnonType);

        // Get the wrkAnonType
        restWrkAnonTypeMockMvc.perform(get("/api/wrk-anon-types/{id}", wrkAnonType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(wrkAnonType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingWrkAnonType() throws Exception {
        // Get the wrkAnonType
        restWrkAnonTypeMockMvc.perform(get("/api/wrk-anon-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWrkAnonType() throws Exception {
        // Initialize the database
        wrkAnonTypeRepository.saveAndFlush(wrkAnonType);

        int databaseSizeBeforeUpdate = wrkAnonTypeRepository.findAll().size();

        // Update the wrkAnonType
        WrkAnonType updatedWrkAnonType = wrkAnonTypeRepository.findById(wrkAnonType.getId()).get();
        // Disconnect from session so that the updates on updatedWrkAnonType are not directly saved in db
        em.detach(updatedWrkAnonType);
        updatedWrkAnonType
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);

        restWrkAnonTypeMockMvc.perform(put("/api/wrk-anon-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedWrkAnonType)))
            .andExpect(status().isOk());

        // Validate the WrkAnonType in the database
        List<WrkAnonType> wrkAnonTypeList = wrkAnonTypeRepository.findAll();
        assertThat(wrkAnonTypeList).hasSize(databaseSizeBeforeUpdate);
        WrkAnonType testWrkAnonType = wrkAnonTypeList.get(wrkAnonTypeList.size() - 1);
        assertThat(testWrkAnonType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWrkAnonType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingWrkAnonType() throws Exception {
        int databaseSizeBeforeUpdate = wrkAnonTypeRepository.findAll().size();

        // Create the WrkAnonType

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWrkAnonTypeMockMvc.perform(put("/api/wrk-anon-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wrkAnonType)))
            .andExpect(status().isBadRequest());

        // Validate the WrkAnonType in the database
        List<WrkAnonType> wrkAnonTypeList = wrkAnonTypeRepository.findAll();
        assertThat(wrkAnonTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteWrkAnonType() throws Exception {
        // Initialize the database
        wrkAnonTypeRepository.saveAndFlush(wrkAnonType);

        int databaseSizeBeforeDelete = wrkAnonTypeRepository.findAll().size();

        // Delete the wrkAnonType
        restWrkAnonTypeMockMvc.perform(delete("/api/wrk-anon-types/{id}", wrkAnonType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WrkAnonType> wrkAnonTypeList = wrkAnonTypeRepository.findAll();
        assertThat(wrkAnonTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WrkAnonType.class);
        WrkAnonType wrkAnonType1 = new WrkAnonType();
        wrkAnonType1.setId(1L);
        WrkAnonType wrkAnonType2 = new WrkAnonType();
        wrkAnonType2.setId(wrkAnonType1.getId());
        assertThat(wrkAnonType1).isEqualTo(wrkAnonType2);
        wrkAnonType2.setId(2L);
        assertThat(wrkAnonType1).isNotEqualTo(wrkAnonType2);
        wrkAnonType1.setId(null);
        assertThat(wrkAnonType1).isNotEqualTo(wrkAnonType2);
    }
}
