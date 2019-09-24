package net.anet.workflow.airflow.service;

import net.anet.workflow.airflow.domain.WrkDatabase;
import net.anet.workflow.airflow.repository.WrkDatabaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link WrkDatabase}.
 */
@Service
@Transactional
public class WrkDatabaseService {

    private final Logger log = LoggerFactory.getLogger(WrkDatabaseService.class);

    private final WrkDatabaseRepository wrkDatabaseRepository;

    public WrkDatabaseService(WrkDatabaseRepository wrkDatabaseRepository) {
        this.wrkDatabaseRepository = wrkDatabaseRepository;
    }

    /**
     * Save a wrkDatabase.
     *
     * @param wrkDatabase the entity to save.
     * @return the persisted entity.
     */
    public WrkDatabase save(WrkDatabase wrkDatabase) {
        log.debug("Request to save WrkDatabase : {}", wrkDatabase);
        return wrkDatabaseRepository.save(wrkDatabase);
    }

    /**
     * Get all the wrkDatabases.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<WrkDatabase> findAll(Pageable pageable) {
        log.debug("Request to get all WrkDatabases");
        return wrkDatabaseRepository.findAll(pageable);
    }


    /**
     * Get one wrkDatabase by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<WrkDatabase> findOne(Long id) {
        log.debug("Request to get WrkDatabase : {}", id);
        return wrkDatabaseRepository.findById(id);
    }

    /**
     * Delete the wrkDatabase by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete WrkDatabase : {}", id);
        wrkDatabaseRepository.deleteById(id);
    }
}
