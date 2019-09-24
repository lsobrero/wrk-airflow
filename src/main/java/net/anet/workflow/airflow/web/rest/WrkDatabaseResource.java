package net.anet.workflow.airflow.web.rest;

import net.anet.workflow.airflow.domain.WrkDatabase;
import net.anet.workflow.airflow.service.WrkDatabaseService;
import net.anet.workflow.airflow.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link net.anet.workflow.airflow.domain.WrkDatabase}.
 */
@RestController
@RequestMapping("/api")
public class WrkDatabaseResource {

    private final Logger log = LoggerFactory.getLogger(WrkDatabaseResource.class);

    private static final String ENTITY_NAME = "wrkairflowWrkDatabase";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WrkDatabaseService wrkDatabaseService;

    public WrkDatabaseResource(WrkDatabaseService wrkDatabaseService) {
        this.wrkDatabaseService = wrkDatabaseService;
    }

    /**
     * {@code POST  /wrk-databases} : Create a new wrkDatabase.
     *
     * @param wrkDatabase the wrkDatabase to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new wrkDatabase, or with status {@code 400 (Bad Request)} if the wrkDatabase has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/wrk-databases")
    public ResponseEntity<WrkDatabase> createWrkDatabase(@Valid @RequestBody WrkDatabase wrkDatabase) throws URISyntaxException {
        log.debug("REST request to save WrkDatabase : {}", wrkDatabase);
        if (wrkDatabase.getId() != null) {
            throw new BadRequestAlertException("A new wrkDatabase cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WrkDatabase result = wrkDatabaseService.save(wrkDatabase);
        return ResponseEntity.created(new URI("/api/wrk-databases/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /wrk-databases} : Updates an existing wrkDatabase.
     *
     * @param wrkDatabase the wrkDatabase to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated wrkDatabase,
     * or with status {@code 400 (Bad Request)} if the wrkDatabase is not valid,
     * or with status {@code 500 (Internal Server Error)} if the wrkDatabase couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/wrk-databases")
    public ResponseEntity<WrkDatabase> updateWrkDatabase(@Valid @RequestBody WrkDatabase wrkDatabase) throws URISyntaxException {
        log.debug("REST request to update WrkDatabase : {}", wrkDatabase);
        if (wrkDatabase.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        WrkDatabase result = wrkDatabaseService.save(wrkDatabase);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, wrkDatabase.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /wrk-databases} : get all the wrkDatabases.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of wrkDatabases in body.
     */
    @GetMapping("/wrk-databases")
    public ResponseEntity<List<WrkDatabase>> getAllWrkDatabases(Pageable pageable) {
        log.debug("REST request to get a page of WrkDatabases");
        Page<WrkDatabase> page = wrkDatabaseService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /wrk-databases/:id} : get the "id" wrkDatabase.
     *
     * @param id the id of the wrkDatabase to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the wrkDatabase, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/wrk-databases/{id}")
    public ResponseEntity<WrkDatabase> getWrkDatabase(@PathVariable Long id) {
        log.debug("REST request to get WrkDatabase : {}", id);
        Optional<WrkDatabase> wrkDatabase = wrkDatabaseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(wrkDatabase);
    }

    /**
     * {@code DELETE  /wrk-databases/:id} : delete the "id" wrkDatabase.
     *
     * @param id the id of the wrkDatabase to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/wrk-databases/{id}")
    public ResponseEntity<Void> deleteWrkDatabase(@PathVariable Long id) {
        log.debug("REST request to delete WrkDatabase : {}", id);
        wrkDatabaseService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
