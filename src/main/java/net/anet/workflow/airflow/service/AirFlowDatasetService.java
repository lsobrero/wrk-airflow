package net.anet.workflow.airflow.service;


import net.anet.workflow.airflow.domain.WrkDatabase;
import net.anet.workflow.airflow.domain.WrkDataset;
import net.anet.workflow.airflow.domain.WrkDbTableName;
import net.anet.workflow.airflow.repository.*;
import net.anet.workflow.airflow.service.dto.*;
import net.anet.workflow.airflow.service.mapper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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
    @Autowired
    private WrkDbTableNameRepository wrkDbTableNameRepository;
    @Autowired
    private WrkDbColNameRepository wrkDbColNameRepository;
    @Autowired
    private WrkDbColTypeRepository wrkDbColTypeRepository;
    @Autowired
    private WrkAnonTypeRepository wrkAnonTypeRepository;

    @Autowired
    private AfDatasetMapper afDatasetMapper;
    @Autowired
    private AfDatabaseMapper afDatabaseMapper;
    @Autowired
    private AfDbTableNameMapper afDbTableNameMapper;
    @Autowired
    private AfDbColNameMapper afDbColNameMapper;
    @Autowired
    private AfDbColTypeMapper afDbColTypeMapper;
    @Autowired
    private AfAnonTypeMapper afAnonTypeMapper;


    @Transactional(readOnly = true)
    public List<AfDatasetDTO> findAll(Pageable pageable){
        log.debug("Request to get all AfDatasetDTO");
        return wrkDatasetRepository.findAll().stream()
            .map(afDatasetMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Transactional(readOnly = true)
    public List<AfDatabaseDTO> findDatabaseByDatasetId(Long id){
        log.debug("Request to get all AfDatabaseDTO");
        WrkDataset ds = afDatasetMapper.fromId(id);
        return wrkDatabaseRepository.findByWrkDataSet(ds).stream()
            .map(afDatabaseMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Transactional(readOnly = true)
    public List<AfDbTableNameDTO> findTablesNamesByDatabaseId(long id) {
        log.debug("Request to get all AfDbTableNames");
        WrkDatabase db = afDatabaseMapper.fromId(id);
        return wrkDbTableNameRepository.findAllByDatabaseName(db).stream()
            .map(afDbTableNameMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Transactional(readOnly = true)
    public List<AfDbColNameDTO> findColNamesByTableId(Long id){
        log.debug("Request to get all AfDatabaseDTO");
        WrkDbTableName table = afDbTableNameMapper.fromId(id);
        return wrkDbColNameRepository.findByDbName(table).stream()
            .map(afDbColNameMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Transactional(readOnly = true)
    public Optional<AfDbColTypeDTO> findDbColType(Long id){
        log.debug("Request to get AfDbColTypeDTO by DbColl id={}", id);
        Long colTypeId= wrkDbColNameRepository.getColTypeIdWhereId(id);
        log.debug("Rpository returns {}",wrkDbColTypeRepository.findById(colTypeId).toString());
        return wrkDbColTypeRepository.findById(colTypeId)
            .map(afDbColTypeMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<AfAnonTypeDTO> findAnonType(Long id){
        log.debug("Request to get AfAnonTypeDTO by DbColType id={}", id);
        Long anonTypeId= wrkDbColTypeRepository.getAnonTypeIdWhereId(id);
        log.debug("Rpository returns {}",wrkAnonTypeRepository.findById(anonTypeId).toString());
        return wrkAnonTypeRepository.findById(anonTypeId)
            .map(afAnonTypeMapper::toDto);
    }
}
