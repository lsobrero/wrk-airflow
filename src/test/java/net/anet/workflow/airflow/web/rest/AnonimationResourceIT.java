package net.anet.workflow.airflow.web.rest;

import net.anet.workflow.airflow.WrkairflowApp;
import net.anet.workflow.airflow.domain.Anonimation;
import net.anet.workflow.airflow.repository.AnonimationRepository;
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
 * Integration tests for the {@link AnonimationResource} REST controller.
 */
@SpringBootTest(classes = WrkairflowApp.class)
public class AnonimationResourceIT {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private AnonimationRepository anonimationRepository;

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

    private MockMvc restAnonimationMockMvc;

    private Anonimation anonimation;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AnonimationResource anonimationResource = new AnonimationResource(anonimationRepository);
        this.restAnonimationMockMvc = MockMvcBuilders.standaloneSetup(anonimationResource)
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
    public static Anonimation createEntity(EntityManager em) {
        Anonimation anonimation = new Anonimation()
            .type(DEFAULT_TYPE)
            .description(DEFAULT_DESCRIPTION);
        return anonimation;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Anonimation createUpdatedEntity(EntityManager em) {
        Anonimation anonimation = new Anonimation()
            .type(UPDATED_TYPE)
            .description(UPDATED_DESCRIPTION);
        return anonimation;
    }

    @BeforeEach
    public void initTest() {
        anonimation = createEntity(em);
    }

    @Test
    @Transactional
    public void createAnonimation() throws Exception {
        int databaseSizeBeforeCreate = anonimationRepository.findAll().size();

        // Create the Anonimation
        restAnonimationMockMvc.perform(post("/api/anonimations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(anonimation)))
            .andExpect(status().isCreated());

        // Validate the Anonimation in the database
        List<Anonimation> anonimationList = anonimationRepository.findAll();
        assertThat(anonimationList).hasSize(databaseSizeBeforeCreate + 1);
        Anonimation testAnonimation = anonimationList.get(anonimationList.size() - 1);
        assertThat(testAnonimation.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testAnonimation.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createAnonimationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = anonimationRepository.findAll().size();

        // Create the Anonimation with an existing ID
        anonimation.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAnonimationMockMvc.perform(post("/api/anonimations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(anonimation)))
            .andExpect(status().isBadRequest());

        // Validate the Anonimation in the database
        List<Anonimation> anonimationList = anonimationRepository.findAll();
        assertThat(anonimationList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllAnonimations() throws Exception {
        // Initialize the database
        anonimationRepository.saveAndFlush(anonimation);

        // Get all the anonimationList
        restAnonimationMockMvc.perform(get("/api/anonimations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(anonimation.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
    
    @Test
    @Transactional
    public void getAnonimation() throws Exception {
        // Initialize the database
        anonimationRepository.saveAndFlush(anonimation);

        // Get the anonimation
        restAnonimationMockMvc.perform(get("/api/anonimations/{id}", anonimation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(anonimation.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAnonimation() throws Exception {
        // Get the anonimation
        restAnonimationMockMvc.perform(get("/api/anonimations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAnonimation() throws Exception {
        // Initialize the database
        anonimationRepository.saveAndFlush(anonimation);

        int databaseSizeBeforeUpdate = anonimationRepository.findAll().size();

        // Update the anonimation
        Anonimation updatedAnonimation = anonimationRepository.findById(anonimation.getId()).get();
        // Disconnect from session so that the updates on updatedAnonimation are not directly saved in db
        em.detach(updatedAnonimation);
        updatedAnonimation
            .type(UPDATED_TYPE)
            .description(UPDATED_DESCRIPTION);

        restAnonimationMockMvc.perform(put("/api/anonimations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAnonimation)))
            .andExpect(status().isOk());

        // Validate the Anonimation in the database
        List<Anonimation> anonimationList = anonimationRepository.findAll();
        assertThat(anonimationList).hasSize(databaseSizeBeforeUpdate);
        Anonimation testAnonimation = anonimationList.get(anonimationList.size() - 1);
        assertThat(testAnonimation.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testAnonimation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingAnonimation() throws Exception {
        int databaseSizeBeforeUpdate = anonimationRepository.findAll().size();

        // Create the Anonimation

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnonimationMockMvc.perform(put("/api/anonimations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(anonimation)))
            .andExpect(status().isBadRequest());

        // Validate the Anonimation in the database
        List<Anonimation> anonimationList = anonimationRepository.findAll();
        assertThat(anonimationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAnonimation() throws Exception {
        // Initialize the database
        anonimationRepository.saveAndFlush(anonimation);

        int databaseSizeBeforeDelete = anonimationRepository.findAll().size();

        // Delete the anonimation
        restAnonimationMockMvc.perform(delete("/api/anonimations/{id}", anonimation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Anonimation> anonimationList = anonimationRepository.findAll();
        assertThat(anonimationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Anonimation.class);
        Anonimation anonimation1 = new Anonimation();
        anonimation1.setId(1L);
        Anonimation anonimation2 = new Anonimation();
        anonimation2.setId(anonimation1.getId());
        assertThat(anonimation1).isEqualTo(anonimation2);
        anonimation2.setId(2L);
        assertThat(anonimation1).isNotEqualTo(anonimation2);
        anonimation1.setId(null);
        assertThat(anonimation1).isNotEqualTo(anonimation2);
    }
}
