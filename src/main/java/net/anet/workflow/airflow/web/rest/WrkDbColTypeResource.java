package net.anet.workflow.airflow.web.rest;

import net.anet.workflow.airflow.domain.WrkDbColType;
import net.anet.workflow.airflow.repository.WrkDbColTypeRepository;
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
 * REST controller for managing {@link net.anet.workflow.airflow.domain.WrkDbColType}.
 */
@RestController
@RequestMapping("/api")
public class WrkDbColTypeResource {

    private final Logger log = LoggerFactory.getLogger(WrkDbColTypeResource.class);

    private static final String ENTITY_NAME = "wrkairflowWrkDbColType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WrkDbColTypeRepository wrkDbColTypeRepository;

    public WrkDbColTypeResource(WrkDbColTypeRepository wrkDbColTypeRepository) {
        this.wrkDbColTypeRepository = wrkDbColTypeRepository;
    }

    /**
     * {@code POST  /wrk-db-col-types} : Create a new wrkDbColType.
     *
     * @param wrkDbColType the wrkDbColType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new wrkDbColType, or with status {@code 400 (Bad Request)} if the wrkDbColType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/wrk-db-col-types")
    public ResponseEntity<WrkDbColType> createWrkDbColType(@Valid @RequestBody WrkDbColType wrkDbColType) throws URISyntaxException {
        log.debug("REST request to save WrkDbColType : {}", wrkDbColType);
        if (wrkDbColType.getId() != null) {
            throw new BadRequestAlertException("A new wrkDbColType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WrkDbColType result = wrkDbColTypeRepository.save(wrkDbColType);
        return ResponseEntity.created(new URI("/api/wrk-db-col-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /wrk-db-col-types} : Updates an existing wrkDbColType.
     *
     * @param wrkDbColType the wrkDbColType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated wrkDbColType,
     * or with status {@code 400 (Bad Request)} if the wrkDbColType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the wrkDbColType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/wrk-db-col-types")
    public ResponseEntity<WrkDbColType> updateWrkDbColType(@Valid @RequestBody WrkDbColType wrkDbColType) throws URISyntaxException {
        log.debug("REST request to update WrkDbColType : {}", wrkDbColType);
        if (wrkDbColType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        WrkDbColType result = wrkDbColTypeRepository.save(wrkDbColType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, wrkDbColType.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /wrk-db-col-types} : get all the wrkDbColTypes.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of wrkDbColTypes in body.
     */
    @GetMapping("/wrk-db-col-types")
    public List<WrkDbColType> getAllWrkDbColTypes() {
        log.debug("REST request to get all WrkDbColTypes");
        return wrkDbColTypeRepository.findAll();
    }

    /**
     * {@code GET  /wrk-db-col-types/:id} : get the "id" wrkDbColType.
     *
     * @param id the id of the wrkDbColType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the wrkDbColType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/wrk-db-col-types/{id}")
    public ResponseEntity<WrkDbColType> getWrkDbColType(@PathVariable Long id) {
        log.debug("REST request to get WrkDbColType : {}", id);
        Optional<WrkDbColType> wrkDbColType = wrkDbColTypeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(wrkDbColType);
    }

    /**
     * {@code DELETE  /wrk-db-col-types/:id} : delete the "id" wrkDbColType.
     *
     * @param id the id of the wrkDbColType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/wrk-db-col-types/{id}")
    public ResponseEntity<Void> deleteWrkDbColType(@PathVariable Long id) {
        log.debug("REST request to delete WrkDbColType : {}", id);
        wrkDbColTypeRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
