package net.anet.workflow.airflow.web.rest;

import io.github.jhipster.web.util.ResponseUtil;
import net.anet.workflow.airflow.service.AirFlowDatasetService;
import net.anet.workflow.airflow.service.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link net.anet.workflow.airflow.domain.WrkDataset}.
 */
@RestController
@RequestMapping("/api")
public class AirflowResource {

    private final Logger log = LoggerFactory.getLogger(AirflowResource.class);
    private static final String ENTITY_NAME = "wrkairflowWrkDataset";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AirFlowDatasetService afDatasetService;


    public AirflowResource(AirFlowDatasetService afDatasetService) {
        this.afDatasetService = afDatasetService;
    }

    /**
     * {@code GET  /wrk-datasets} : get all the wrkDatasets.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of wrkDatasets in body.
     */
    @GetMapping("/af-datasets")
    public List<AfDatasetDTO> getAllWrkDatasets(Pageable pageable) {
        log.debug("REST request to get a page of WrkDatasets");
        List<AfDatasetDTO> result = afDatasetService.findAll(pageable);
        log.debug("Found {} datasets", result.size());
        for(AfDatasetDTO ds : result){
            log.debug("Search Databases for Dataset with id={}",ds.getId());
            List<AfDatabaseDTO> databases = afDatasetService.findDatabaseByDatasetId(ds.getId());
            for(AfDatabaseDTO db : databases){
                log.debug("Found Database with id={} and Name {}",db.getId(), db.getName());
                List<AfDbTableNameDTO> tables = afDatasetService.findTablesNamesByDatabaseId(db.getId());
                log.debug("Found {} Tables",tables.size());
                for(AfDbTableNameDTO table:tables){
                    log.debug("Using Table name={}", table.getName());
                    List<AfDbColNameDTO> cols = afDatasetService.findColNamesByTableId(table.getId());
                    log.debug("Found {} Columns",cols.size());
                    for(AfDbColNameDTO col : cols) {
                        log.debug("Using Column name={}",col.toString());
                        Optional<AfDbColTypeDTO>colType = afDatasetService.findDbColType(col.getId());
                        if(colType.isPresent()){
                            AfDbColTypeDTO coltype=colType.orElse(null);
                            log.debug("Found {} DbColType",coltype.toString());
                            Optional<AfAnonTypeDTO>anonType = afDatasetService.findAnonType(coltype.getId());
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
                ds.setDatabase(db);
            }
        }
        return result;
    }

    /**
     * Get's the AfDataset
     * Given the AfDataset name returns the AfDataset
     * @param name
     * @return AfDataset
     */
    @GetMapping("/af-dataset/{name}")
    public ResponseEntity getAfDatasetByName(@PathVariable String name) {
        log.debug("REST request to get AfDataset: {}", name);
        AfDatasetDTO afDatasetDTO = afDatasetService.findAfDatasetByName(name);
        return ResponseEntity.ok(afDatasetDTO);
    }

}
