package net.anet.workflow.airflow.service;


import net.anet.workflow.airflow.domain.WrkDatabase;
import net.anet.workflow.airflow.domain.WrkDataset;
import net.anet.workflow.airflow.repository.WrkDatabaseRepository;
import net.anet.workflow.airflow.repository.WrkDatasetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

/**
 * Service Implementation for managing {@link WrkDataset}.
 */
@Service
@Transactional
public class AirFlowDatasetService {

    private final Logger log = LoggerFactory.getLogger(AirFlowDatasetService.class);
    @Autowired
    private WrkDatasetRepository wrkDatasetRepository;
    @Autowired
    private WrkDatabaseRepository wrkDatabaseRepository;


    /**
     * Get all the wrkDatasets.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<WrkDataset> findAll(Pageable pageable) {
        log.debug("Request to get all WrkDatasets");
        Page<WrkDataset> result = null;
        Page<WrkDataset> ds = wrkDatasetRepository.findAll(pageable);
        Iterator<WrkDataset> iter = ds.iterator();
        while (iter.hasNext()){
            WrkDataset a = iter.next();
            log.debug("Dataset id={}",a.getId());
/*
            Optional<WrkDatabase> opt = wrkDatabaseService.findByWrkDataset(a.getId());
            if(opt.isPresent()){
                log.debug("OPTIONAL is Present");
                a.setDbNames((Set<WrkDatabase>) opt.get());
            }
*/
            HashSet<WrkDatabase> m_set = new HashSet<>();
            m_set.add(wrkDatabaseRepository.findByWrkDataSet(a));
            a.setDbNames(m_set);
        }
        return ds;
    }

}
