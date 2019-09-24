package net.anet.workflow.airflow.web.rest;

import net.anet.workflow.airflow.WrkairflowApp;
import net.anet.workflow.airflow.domain.MyDummy;
import net.anet.workflow.airflow.repository.MyDummyRepository;
import net.anet.workflow.airflow.service.MyDummyService;
import net.anet.workflow.airflow.service.dto.MyDummyDTO;
import net.anet.workflow.airflow.service.mapper.MyDummyMapper;
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
 * Integration tests for the {@link MyDummyResource} REST controller.
 */
@SpringBootTest(classes = WrkairflowApp.class)
public class MyDummyResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ENABLED = false;
    private static final Boolean UPDATED_IS_ENABLED = true;

    @Autowired
    private MyDummyRepository myDummyRepository;

    @Autowired
    private MyDummyMapper myDummyMapper;

    @Autowired
    private MyDummyService myDummyService;

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

    private MockMvc restMyDummyMockMvc;

    private MyDummy myDummy;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MyDummyResource myDummyResource = new MyDummyResource(myDummyService);
        this.restMyDummyMockMvc = MockMvcBuilders.standaloneSetup(myDummyResource)
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
    public static MyDummy createEntity(EntityManager em) {
        MyDummy myDummy = new MyDummy()
            .name(DEFAULT_NAME)
            .isEnabled(DEFAULT_IS_ENABLED);
        return myDummy;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MyDummy createUpdatedEntity(EntityManager em) {
        MyDummy myDummy = new MyDummy()
            .name(UPDATED_NAME)
            .isEnabled(UPDATED_IS_ENABLED);
        return myDummy;
    }

    @BeforeEach
    public void initTest() {
        myDummy = createEntity(em);
    }

    @Test
    @Transactional
    public void createMyDummy() throws Exception {
        int databaseSizeBeforeCreate = myDummyRepository.findAll().size();

        // Create the MyDummy
        MyDummyDTO myDummyDTO = myDummyMapper.toDto(myDummy);
        restMyDummyMockMvc.perform(post("/api/my-dummies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(myDummyDTO)))
            .andExpect(status().isCreated());

        // Validate the MyDummy in the database
        List<MyDummy> myDummyList = myDummyRepository.findAll();
        assertThat(myDummyList).hasSize(databaseSizeBeforeCreate + 1);
        MyDummy testMyDummy = myDummyList.get(myDummyList.size() - 1);
        assertThat(testMyDummy.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMyDummy.isIsEnabled()).isEqualTo(DEFAULT_IS_ENABLED);
    }

    @Test
    @Transactional
    public void createMyDummyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = myDummyRepository.findAll().size();

        // Create the MyDummy with an existing ID
        myDummy.setId(1L);
        MyDummyDTO myDummyDTO = myDummyMapper.toDto(myDummy);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMyDummyMockMvc.perform(post("/api/my-dummies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(myDummyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MyDummy in the database
        List<MyDummy> myDummyList = myDummyRepository.findAll();
        assertThat(myDummyList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = myDummyRepository.findAll().size();
        // set the field null
        myDummy.setName(null);

        // Create the MyDummy, which fails.
        MyDummyDTO myDummyDTO = myDummyMapper.toDto(myDummy);

        restMyDummyMockMvc.perform(post("/api/my-dummies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(myDummyDTO)))
            .andExpect(status().isBadRequest());

        List<MyDummy> myDummyList = myDummyRepository.findAll();
        assertThat(myDummyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIsEnabledIsRequired() throws Exception {
        int databaseSizeBeforeTest = myDummyRepository.findAll().size();
        // set the field null
        myDummy.setIsEnabled(null);

        // Create the MyDummy, which fails.
        MyDummyDTO myDummyDTO = myDummyMapper.toDto(myDummy);

        restMyDummyMockMvc.perform(post("/api/my-dummies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(myDummyDTO)))
            .andExpect(status().isBadRequest());

        List<MyDummy> myDummyList = myDummyRepository.findAll();
        assertThat(myDummyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMyDummies() throws Exception {
        // Initialize the database
        myDummyRepository.saveAndFlush(myDummy);

        // Get all the myDummyList
        restMyDummyMockMvc.perform(get("/api/my-dummies?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(myDummy.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].isEnabled").value(hasItem(DEFAULT_IS_ENABLED.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getMyDummy() throws Exception {
        // Initialize the database
        myDummyRepository.saveAndFlush(myDummy);

        // Get the myDummy
        restMyDummyMockMvc.perform(get("/api/my-dummies/{id}", myDummy.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(myDummy.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.isEnabled").value(DEFAULT_IS_ENABLED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingMyDummy() throws Exception {
        // Get the myDummy
        restMyDummyMockMvc.perform(get("/api/my-dummies/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMyDummy() throws Exception {
        // Initialize the database
        myDummyRepository.saveAndFlush(myDummy);

        int databaseSizeBeforeUpdate = myDummyRepository.findAll().size();

        // Update the myDummy
        MyDummy updatedMyDummy = myDummyRepository.findById(myDummy.getId()).get();
        // Disconnect from session so that the updates on updatedMyDummy are not directly saved in db
        em.detach(updatedMyDummy);
        updatedMyDummy
            .name(UPDATED_NAME)
            .isEnabled(UPDATED_IS_ENABLED);
        MyDummyDTO myDummyDTO = myDummyMapper.toDto(updatedMyDummy);

        restMyDummyMockMvc.perform(put("/api/my-dummies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(myDummyDTO)))
            .andExpect(status().isOk());

        // Validate the MyDummy in the database
        List<MyDummy> myDummyList = myDummyRepository.findAll();
        assertThat(myDummyList).hasSize(databaseSizeBeforeUpdate);
        MyDummy testMyDummy = myDummyList.get(myDummyList.size() - 1);
        assertThat(testMyDummy.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMyDummy.isIsEnabled()).isEqualTo(UPDATED_IS_ENABLED);
    }

    @Test
    @Transactional
    public void updateNonExistingMyDummy() throws Exception {
        int databaseSizeBeforeUpdate = myDummyRepository.findAll().size();

        // Create the MyDummy
        MyDummyDTO myDummyDTO = myDummyMapper.toDto(myDummy);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMyDummyMockMvc.perform(put("/api/my-dummies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(myDummyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MyDummy in the database
        List<MyDummy> myDummyList = myDummyRepository.findAll();
        assertThat(myDummyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMyDummy() throws Exception {
        // Initialize the database
        myDummyRepository.saveAndFlush(myDummy);

        int databaseSizeBeforeDelete = myDummyRepository.findAll().size();

        // Delete the myDummy
        restMyDummyMockMvc.perform(delete("/api/my-dummies/{id}", myDummy.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MyDummy> myDummyList = myDummyRepository.findAll();
        assertThat(myDummyList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MyDummy.class);
        MyDummy myDummy1 = new MyDummy();
        myDummy1.setId(1L);
        MyDummy myDummy2 = new MyDummy();
        myDummy2.setId(myDummy1.getId());
        assertThat(myDummy1).isEqualTo(myDummy2);
        myDummy2.setId(2L);
        assertThat(myDummy1).isNotEqualTo(myDummy2);
        myDummy1.setId(null);
        assertThat(myDummy1).isNotEqualTo(myDummy2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MyDummyDTO.class);
        MyDummyDTO myDummyDTO1 = new MyDummyDTO();
        myDummyDTO1.setId(1L);
        MyDummyDTO myDummyDTO2 = new MyDummyDTO();
        assertThat(myDummyDTO1).isNotEqualTo(myDummyDTO2);
        myDummyDTO2.setId(myDummyDTO1.getId());
        assertThat(myDummyDTO1).isEqualTo(myDummyDTO2);
        myDummyDTO2.setId(2L);
        assertThat(myDummyDTO1).isNotEqualTo(myDummyDTO2);
        myDummyDTO1.setId(null);
        assertThat(myDummyDTO1).isNotEqualTo(myDummyDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(myDummyMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(myDummyMapper.fromId(null)).isNull();
    }
}
