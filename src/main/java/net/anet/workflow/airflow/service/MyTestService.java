package net.anet.workflow.airflow.service;

import net.anet.workflow.airflow.domain.MyTest;
import net.anet.workflow.airflow.repository.MyTestRepository;
import net.anet.workflow.airflow.service.dto.MyTestDTO;
import net.anet.workflow.airflow.service.mapper.MyTestMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link MyTest}.
 */
@Service
@Transactional
public class MyTestService {

    private final Logger log = LoggerFactory.getLogger(MyTestService.class);

    private final MyTestRepository myTestRepository;

    private final MyTestMapper myTestMapper;

    public MyTestService(MyTestRepository myTestRepository, MyTestMapper myTestMapper) {
        this.myTestRepository = myTestRepository;
        this.myTestMapper = myTestMapper;
    }

    /**
     * Save a myTest.
     *
     * @param myTestDTO the entity to save.
     * @return the persisted entity.
     */
    public MyTestDTO save(MyTestDTO myTestDTO) {
        log.debug("Request to save MyTest : {}", myTestDTO);
        MyTest myTest = myTestMapper.toEntity(myTestDTO);
        myTest = myTestRepository.save(myTest);
        return myTestMapper.toDto(myTest);
    }

    /**
     * Get all the myTests.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<MyTestDTO> findAll() {
        log.debug("Request to get all MyTests");
        return myTestRepository.findAll().stream()
            .map(myTestMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one myTest by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MyTestDTO> findOne(Long id) {
        log.debug("Request to get MyTest : {}", id);
        return myTestRepository.findById(id)
            .map(myTestMapper::toDto);
    }

    /**
     * Delete the myTest by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete MyTest : {}", id);
        myTestRepository.deleteById(id);
    }
}
