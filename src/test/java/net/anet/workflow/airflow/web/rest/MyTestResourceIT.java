package net.anet.workflow.airflow.web.rest;

import net.anet.workflow.airflow.WrkairflowApp;
import net.anet.workflow.airflow.domain.MyTest;
import net.anet.workflow.airflow.repository.MyTestRepository;
import net.anet.workflow.airflow.service.MyTestService;
import net.anet.workflow.airflow.service.dto.MyTestDTO;
import net.anet.workflow.airflow.service.mapper.MyTestMapper;
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
 * Integration tests for the {@link MyTestResource} REST controller.
 */
@SpringBootTest(classes = WrkairflowApp.class)
public class MyTestResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private MyTestRepository myTestRepository;

    @Autowired
    private MyTestMapper myTestMapper;

    @Autowired
    private MyTestService myTestService;

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

    private MockMvc restMyTestMockMvc;

    private MyTest myTest;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MyTestResource myTestResource = new MyTestResource(myTestService);
        this.restMyTestMockMvc = MockMvcBuilders.standaloneSetup(myTestResource)
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
    public static MyTest createEntity(EntityManager em) {
        MyTest myTest = new MyTest()
            .name(DEFAULT_NAME);
        return myTest;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MyTest createUpdatedEntity(EntityManager em) {
        MyTest myTest = new MyTest()
            .name(UPDATED_NAME);
        return myTest;
    }

    @BeforeEach
    public void initTest() {
        myTest = createEntity(em);
    }

    @Test
    @Transactional
    public void createMyTest() throws Exception {
        int databaseSizeBeforeCreate = myTestRepository.findAll().size();

        // Create the MyTest
        MyTestDTO myTestDTO = myTestMapper.toDto(myTest);
        restMyTestMockMvc.perform(post("/api/my-tests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(myTestDTO)))
            .andExpect(status().isCreated());

        // Validate the MyTest in the database
        List<MyTest> myTestList = myTestRepository.findAll();
        assertThat(myTestList).hasSize(databaseSizeBeforeCreate + 1);
        MyTest testMyTest = myTestList.get(myTestList.size() - 1);
        assertThat(testMyTest.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createMyTestWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = myTestRepository.findAll().size();

        // Create the MyTest with an existing ID
        myTest.setId(1L);
        MyTestDTO myTestDTO = myTestMapper.toDto(myTest);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMyTestMockMvc.perform(post("/api/my-tests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(myTestDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MyTest in the database
        List<MyTest> myTestList = myTestRepository.findAll();
        assertThat(myTestList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = myTestRepository.findAll().size();
        // set the field null
        myTest.setName(null);

        // Create the MyTest, which fails.
        MyTestDTO myTestDTO = myTestMapper.toDto(myTest);

        restMyTestMockMvc.perform(post("/api/my-tests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(myTestDTO)))
            .andExpect(status().isBadRequest());

        List<MyTest> myTestList = myTestRepository.findAll();
        assertThat(myTestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMyTests() throws Exception {
        // Initialize the database
        myTestRepository.saveAndFlush(myTest);

        // Get all the myTestList
        restMyTestMockMvc.perform(get("/api/my-tests?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(myTest.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getMyTest() throws Exception {
        // Initialize the database
        myTestRepository.saveAndFlush(myTest);

        // Get the myTest
        restMyTestMockMvc.perform(get("/api/my-tests/{id}", myTest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(myTest.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMyTest() throws Exception {
        // Get the myTest
        restMyTestMockMvc.perform(get("/api/my-tests/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMyTest() throws Exception {
        // Initialize the database
        myTestRepository.saveAndFlush(myTest);

        int databaseSizeBeforeUpdate = myTestRepository.findAll().size();

        // Update the myTest
        MyTest updatedMyTest = myTestRepository.findById(myTest.getId()).get();
        // Disconnect from session so that the updates on updatedMyTest are not directly saved in db
        em.detach(updatedMyTest);
        updatedMyTest
            .name(UPDATED_NAME);
        MyTestDTO myTestDTO = myTestMapper.toDto(updatedMyTest);

        restMyTestMockMvc.perform(put("/api/my-tests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(myTestDTO)))
            .andExpect(status().isOk());

        // Validate the MyTest in the database
        List<MyTest> myTestList = myTestRepository.findAll();
        assertThat(myTestList).hasSize(databaseSizeBeforeUpdate);
        MyTest testMyTest = myTestList.get(myTestList.size() - 1);
        assertThat(testMyTest.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingMyTest() throws Exception {
        int databaseSizeBeforeUpdate = myTestRepository.findAll().size();

        // Create the MyTest
        MyTestDTO myTestDTO = myTestMapper.toDto(myTest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMyTestMockMvc.perform(put("/api/my-tests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(myTestDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MyTest in the database
        List<MyTest> myTestList = myTestRepository.findAll();
        assertThat(myTestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMyTest() throws Exception {
        // Initialize the database
        myTestRepository.saveAndFlush(myTest);

        int databaseSizeBeforeDelete = myTestRepository.findAll().size();

        // Delete the myTest
        restMyTestMockMvc.perform(delete("/api/my-tests/{id}", myTest.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MyTest> myTestList = myTestRepository.findAll();
        assertThat(myTestList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MyTest.class);
        MyTest myTest1 = new MyTest();
        myTest1.setId(1L);
        MyTest myTest2 = new MyTest();
        myTest2.setId(myTest1.getId());
        assertThat(myTest1).isEqualTo(myTest2);
        myTest2.setId(2L);
        assertThat(myTest1).isNotEqualTo(myTest2);
        myTest1.setId(null);
        assertThat(myTest1).isNotEqualTo(myTest2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MyTestDTO.class);
        MyTestDTO myTestDTO1 = new MyTestDTO();
        myTestDTO1.setId(1L);
        MyTestDTO myTestDTO2 = new MyTestDTO();
        assertThat(myTestDTO1).isNotEqualTo(myTestDTO2);
        myTestDTO2.setId(myTestDTO1.getId());
        assertThat(myTestDTO1).isEqualTo(myTestDTO2);
        myTestDTO2.setId(2L);
        assertThat(myTestDTO1).isNotEqualTo(myTestDTO2);
        myTestDTO1.setId(null);
        assertThat(myTestDTO1).isNotEqualTo(myTestDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(myTestMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(myTestMapper.fromId(null)).isNull();
    }
}
