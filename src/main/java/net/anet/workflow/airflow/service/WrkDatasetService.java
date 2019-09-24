package net.anet.workflow.airflow.service;

import net.anet.workflow.airflow.domain.WrkDataset;
import net.anet.workflow.airflow.repository.WrkDatasetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link WrkDataset}.
 */
@Service
@Transactional
public class WrkDatasetService {

    private final Logger log = LoggerFactory.getLogger(WrkDatasetService.class);

    private final WrkDatasetRepository wrkDatasetRepository;

    public WrkDatasetService(WrkDatasetRepository wrkDatasetRepository) {
        this.wrkDatasetRepository = wrkDatasetRepository;
    }

    /**
     * Save a wrkDataset.
     *
     * @param wrkDataset the entity to save.
     * @return the persisted entity.
     */
    public WrkDataset save(WrkDataset wrkDataset) {
        log.debug("Request to save WrkDataset : {}", wrkDataset);
        return wrkDatasetRepository.save(wrkDataset);
    }

    /**
     * Get all the wrkDatasets.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<WrkDataset> findAll(Pageable pageable) {
        log.debug("Request to get all WrkDatasets");
        return wrkDatasetRepository.findAll(pageable);
    }


    /**
     * Get one wrkDataset by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<WrkDataset> findOne(Long id) {
        log.debug("Request to get WrkDataset : {}", id);
        return wrkDatasetRepository.findById(id);
    }

    /**
     * Delete the wrkDataset by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete WrkDataset : {}", id);
        wrkDatasetRepository.deleteById(id);
    }
}
