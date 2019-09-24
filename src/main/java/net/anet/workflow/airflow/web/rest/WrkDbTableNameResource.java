package net.anet.workflow.airflow.web.rest;

import net.anet.workflow.airflow.domain.WrkDbTableName;
import net.anet.workflow.airflow.repository.WrkDbTableNameRepository;
import net.anet.workflow.airflow.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link net.anet.workflow.airflow.domain.WrkDbTableName}.
 */
@RestController
@RequestMapping("/api")
public class WrkDbTableNameResource {

    private final Logger log = LoggerFactory.getLogger(WrkDbTableNameResource.class);

    private static final String ENTITY_NAME = "wrkairflowWrkDbTableName";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WrkDbTableNameRepository wrkDbTableNameRepository;

    public WrkDbTableNameResource(WrkDbTableNameRepository wrkDbTableNameRepository) {
        this.wrkDbTableNameRepository = wrkDbTableNameRepository;
    }

    /**
     * {@code POST  /wrk-db-table-names} : Create a new wrkDbTableName.
     *
     * @param wrkDbTableName the wrkDbTableName to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new wrkDbTableName, or with status {@code 400 (Bad Request)} if the wrkDbTableName has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/wrk-db-table-names")
    public ResponseEntity<WrkDbTableName> createWrkDbTableName(@Valid @RequestBody WrkDbTableName wrkDbTableName) throws URISyntaxException {
        log.debug("REST request to save WrkDbTableName : {}", wrkDbTableName);
        if (wrkDbTableName.getId() != null) {
            throw new BadRequestAlertException("A new wrkDbTableName cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WrkDbTableName result = wrkDbTableNameRepository.save(wrkDbTableName);
        return ResponseEntity.created(new URI("/api/wrk-db-table-names/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /wrk-db-table-names} : Updates an existing wrkDbTableName.
     *
     * @param wrkDbTableName the wrkDbTableName to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated wrkDbTableName,
     * or with status {@code 400 (Bad Request)} if the wrkDbTableName is not valid,
     * or with status {@code 500 (Internal Server Error)} if the wrkDbTableName couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/wrk-db-table-names")
    public ResponseEntity<WrkDbTableName> updateWrkDbTableName(@Valid @RequestBody WrkDbTableName wrkDbTableName) throws URISyntaxException {
        log.debug("REST request to update WrkDbTableName : {}", wrkDbTableName);
        if (wrkDbTableName.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        WrkDbTableName result = wrkDbTableNameRepository.save(wrkDbTableName);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, wrkDbTableName.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /wrk-db-table-names} : get all the wrkDbTableNames.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of wrkDbTableNames in body.
     */
    @GetMapping("/wrk-db-table-names")
    public List<WrkDbTableName> getAllWrkDbTableNames() {
        log.debug("REST request to get all WrkDbTableNames");
        return wrkDbTableNameRepository.findAll();
    }

    /**
     * {@code GET  /wrk-db-table-names/:id} : get the "id" wrkDbTableName.
     *
     * @param id the id of the wrkDbTableName to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the wrkDbTableName, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/wrk-db-table-names/{id}")
    public ResponseEntity<WrkDbTableName> getWrkDbTableName(@PathVariable Long id) {
        log.debug("REST request to get WrkDbTableName : {}", id);
        Optional<WrkDbTableName> wrkDbTableName = wrkDbTableNameRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(wrkDbTableName);
    }

    /**
     * {@code DELETE  /wrk-db-table-names/:id} : delete the "id" wrkDbTableName.
     *
     * @param id the id of the wrkDbTableName to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/wrk-db-table-names/{id}")
    public ResponseEntity<Void> deleteWrkDbTableName(@PathVariable Long id) {
        log.debug("REST request to delete WrkDbTableName : {}", id);
        wrkDbTableNameRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
