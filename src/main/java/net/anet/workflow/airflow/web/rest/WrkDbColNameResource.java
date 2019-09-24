package net.anet.workflow.airflow.web.rest;

import net.anet.workflow.airflow.domain.WrkDbColName;
import net.anet.workflow.airflow.repository.WrkDbColNameRepository;
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
 * REST controller for managing {@link net.anet.workflow.airflow.domain.WrkDbColName}.
 */
@RestController
@RequestMapping("/api")
public class WrkDbColNameResource {

    private final Logger log = LoggerFactory.getLogger(WrkDbColNameResource.class);

    private static final String ENTITY_NAME = "wrkairflowWrkDbColName";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WrkDbColNameRepository wrkDbColNameRepository;

    public WrkDbColNameResource(WrkDbColNameRepository wrkDbColNameRepository) {
        this.wrkDbColNameRepository = wrkDbColNameRepository;
    }

    /**
     * {@code POST  /wrk-db-col-names} : Create a new wrkDbColName.
     *
     * @param wrkDbColName the wrkDbColName to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new wrkDbColName, or with status {@code 400 (Bad Request)} if the wrkDbColName has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/wrk-db-col-names")
    public ResponseEntity<WrkDbColName> createWrkDbColName(@Valid @RequestBody WrkDbColName wrkDbColName) throws URISyntaxException {
        log.debug("REST request to save WrkDbColName : {}", wrkDbColName);
        if (wrkDbColName.getId() != null) {
            throw new BadRequestAlertException("A new wrkDbColName cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WrkDbColName result = wrkDbColNameRepository.save(wrkDbColName);
        return ResponseEntity.created(new URI("/api/wrk-db-col-names/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /wrk-db-col-names} : Updates an existing wrkDbColName.
     *
     * @param wrkDbColName the wrkDbColName to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated wrkDbColName,
     * or with status {@code 400 (Bad Request)} if the wrkDbColName is not valid,
     * or with status {@code 500 (Internal Server Error)} if the wrkDbColName couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/wrk-db-col-names")
    public ResponseEntity<WrkDbColName> updateWrkDbColName(@Valid @RequestBody WrkDbColName wrkDbColName) throws URISyntaxException {
        log.debug("REST request to update WrkDbColName : {}", wrkDbColName);
        if (wrkDbColName.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        WrkDbColName result = wrkDbColNameRepository.save(wrkDbColName);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, wrkDbColName.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /wrk-db-col-names} : get all the wrkDbColNames.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of wrkDbColNames in body.
     */
    @GetMapping("/wrk-db-col-names")
    public List<WrkDbColName> getAllWrkDbColNames() {
        log.debug("REST request to get all WrkDbColNames");
        return wrkDbColNameRepository.findAll();
    }

    /**
     * {@code GET  /wrk-db-col-names/:id} : get the "id" wrkDbColName.
     *
     * @param id the id of the wrkDbColName to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the wrkDbColName, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/wrk-db-col-names/{id}")
    public ResponseEntity<WrkDbColName> getWrkDbColName(@PathVariable Long id) {
        log.debug("REST request to get WrkDbColName : {}", id);
        Optional<WrkDbColName> wrkDbColName = wrkDbColNameRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(wrkDbColName);
    }

    /**
     * {@code DELETE  /wrk-db-col-names/:id} : delete the "id" wrkDbColName.
     *
     * @param id the id of the wrkDbColName to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/wrk-db-col-names/{id}")
    public ResponseEntity<Void> deleteWrkDbColName(@PathVariable Long id) {
        log.debug("REST request to delete WrkDbColName : {}", id);
        wrkDbColNameRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
