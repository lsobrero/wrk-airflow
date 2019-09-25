package net.anet.workflow.airflow.service;


import net.anet.workflow.airflow.domain.WrkDatabase;
import net.anet.workflow.airflow.domain.WrkDataset;
import net.anet.workflow.airflow.domain.WrkDbTableName;
import net.anet.workflow.airflow.repository.*;
import net.anet.workflow.airflow.service.dto.*;
import net.anet.workflow.airflow.service.mapper.*;
import net.bytebuddy.asm.Advice;
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



    /**
     * Get's th AfDataset
     * Given the dataset name return the AfDataset
     * @param name
     * @return AfDataset
     */
    public AfDatasetDTO findAfDatasetByName(String name) {
        log.debug("Request to get Dataset : {}", name);
        AfDatasetDTO afDatasetDTO =wrkDatasetRepository.findByName(name)
            .map(afDatasetMapper::toDto).orElse(null);
        if(afDatasetDTO == null)
            return null;

        log.debug("Search Databases for Dataset with id={}",afDatasetDTO.getId());
        List<AfDatabaseDTO> databases = findDatabaseByDatasetId(afDatasetDTO.getId());
        for(AfDatabaseDTO db : databases){
            log.debug("Found Database with id={} and Name {}",db.getId(), db.getName());
            List<AfDbTableNameDTO> tables = findTablesNamesByDatabaseId(db.getId());
            log.debug("Found {} Tables",tables.size());
            for(AfDbTableNameDTO table:tables){
                log.debug("Using Table name={}", table.getName());
                List<AfDbColNameDTO> cols = findColNamesByTableId(table.getId());
                log.debug("Found {} Columns",cols.size());
                for(AfDbColNameDTO col : cols) {
                    log.debug("Using Column name={}",col.toString());
                    Optional<AfDbColTypeDTO>colType = findDbColType(col.getId());
                    if(colType.isPresent()){
                        AfDbColTypeDTO coltype=colType.orElse(null);
                        log.debug("Found {} DbColType",coltype.toString());
                        Optional<AfAnonTypeDTO>anonType = findAnonType(coltype.getId());
                        if(anonType.isPresent()){
                            log.debug("Found {} AnonType", anonType.toString());
                            coltype.setAnonType(anonType.orElse(null));
                        }
                        col.setColumnType(coltype);
                    }else{
                        log.debug("DbColType is not present");
                    }
                }
                table.setCols(cols);
            }
            db.setTables(tables);
            afDatasetDTO.setDatabase(db);
        }

        return afDatasetDTO;
    }



    /**
     * Get's all  Datasets as a List
     * @param pageable
     * @return  Datasets
     */
    @Transactional(readOnly = true)
    public List<AfDatasetDTO> findAll(Pageable pageable){
        log.debug("Request to get all AfDatasetDTO");
        return wrkDatasetRepository.findAll().stream()
            .map(afDatasetMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get's the Databases List
     * Given the Dataset returns the Databases
     * @param id
     * @return Databases
     */
    @Transactional(readOnly = true)
    public List<AfDatabaseDTO> findDatabaseByDatasetId(Long id){
        log.debug("Request to get all AfDatabaseDTO");
        WrkDataset ds = afDatasetMapper.fromId(id);
        return wrkDatabaseRepository.findByWrkDataSet(ds).stream()
            .map(afDatabaseMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get's the DbTableName List
     * Given the Database returns the DbTableNames
     * @param id
     * @return DbTableNames
     */
    @Transactional(readOnly = true)
    public List<AfDbTableNameDTO> findTablesNamesByDatabaseId(long id) {
        log.debug("Request to get all AfDbTableNames");
        WrkDatabase db = afDatabaseMapper.fromId(id);
        return wrkDbTableNameRepository.findAllByDatabaseName(db).stream()
            .map(afDbTableNameMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get's the DbColNames List
     * Given th DbTable returns the DbColNames
     * @param id
     * @return DbColNames
     */
    @Transactional(readOnly = true)
    public List<AfDbColNameDTO> findColNamesByTableId(Long id){
        log.debug("Request to get all AfDatabaseDTO");
        WrkDbTableName table = afDbTableNameMapper.fromId(id);
        return wrkDbColNameRepository.findByDbName(table).stream()
            .map(afDbColNameMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get's the DvColType
     * Given the DbColName returns the DbColType
     * @param id
     * @return the DbColType
     */
    @Transactional(readOnly = true)
    public Optional<AfDbColTypeDTO> findDbColType(Long id){
        log.debug("Request to get AfDbColTypeDTO by DbColl id={}", id);
        Long colTypeId= wrkDbColNameRepository.getColTypeIdWhereId(id);
        log.debug("Rpository returns {}",wrkDbColTypeRepository.findById(colTypeId).toString());
        return wrkDbColTypeRepository.findById(colTypeId)
            .map(afDbColTypeMapper::toDto);
    }

    /**
     * Get's the AnonType
     * Given the DbColType returns the AnonType
     * @param id
     * @return the AnonType
     */
    @Transactional(readOnly = true)
    public Optional<AfAnonTypeDTO> findAnonType(Long id){
        log.debug("Request to get AfAnonTypeDTO by DbColType id={}", id);
        Long anonTypeId= wrkDbColTypeRepository.getAnonTypeIdWhereId(id);
        log.debug("Rpository returns {}",wrkAnonTypeRepository.findById(anonTypeId).toString());
        return wrkAnonTypeRepository.findById(anonTypeId)
            .map(afAnonTypeMapper::toDto);
    }

}
