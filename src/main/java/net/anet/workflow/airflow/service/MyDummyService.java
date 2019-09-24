package net.anet.workflow.airflow.service;

import net.anet.workflow.airflow.domain.MyDummy;
import net.anet.workflow.airflow.repository.MyDummyRepository;
import net.anet.workflow.airflow.service.dto.MyDummyDTO;
import net.anet.workflow.airflow.service.mapper.MyDummyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link MyDummy}.
 */
@Service
@Transactional
public class MyDummyService {

    private final Logger log = LoggerFactory.getLogger(MyDummyService.class);

    private final MyDummyRepository myDummyRepository;

    private final MyDummyMapper myDummyMapper;

    public MyDummyService(MyDummyRepository myDummyRepository, MyDummyMapper myDummyMapper) {
        this.myDummyRepository = myDummyRepository;
        this.myDummyMapper = myDummyMapper;
    }

    /**
     * Save a myDummy.
     *
     * @param myDummyDTO the entity to save.
     * @return the persisted entity.
     */
    public MyDummyDTO save(MyDummyDTO myDummyDTO) {
        log.debug("Request to save MyDummy : {}", myDummyDTO);
        MyDummy myDummy = myDummyMapper.toEntity(myDummyDTO);
        myDummy = myDummyRepository.save(myDummy);
        return myDummyMapper.toDto(myDummy);
    }

    /**
     * Get all the myDummies.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<MyDummyDTO> findAll() {
        log.debug("Request to get all MyDummies");
        return myDummyRepository.findAll().stream()
            .map(myDummyMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one myDummy by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MyDummyDTO> findOne(Long id) {
        log.debug("Request to get MyDummy : {}", id);
        return myDummyRepository.findById(id)
            .map(myDummyMapper::toDto);
    }

    /**
     * Delete the myDummy by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete MyDummy : {}", id);
        myDummyRepository.deleteById(id);
    }
}
