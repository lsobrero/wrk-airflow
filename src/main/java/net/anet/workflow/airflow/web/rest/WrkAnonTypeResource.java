package net.anet.workflow.airflow.web.rest;

import net.anet.workflow.airflow.domain.WrkAnonType;
import net.anet.workflow.airflow.repository.WrkAnonTypeRepository;
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
 * REST controller for managing {@link net.anet.workflow.airflow.domain.WrkAnonType}.
 */
@RestController
@RequestMapping("/api")
public class WrkAnonTypeResource {

    private final Logger log = LoggerFactory.getLogger(WrkAnonTypeResource.class);

    private static final String ENTITY_NAME = "wrkairflowWrkAnonType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WrkAnonTypeRepository wrkAnonTypeRepository;

    public WrkAnonTypeResource(WrkAnonTypeRepository wrkAnonTypeRepository) {
        this.wrkAnonTypeRepository = wrkAnonTypeRepository;
    }

    /**
     * {@code POST  /wrk-anon-types} : Create a new wrkAnonType.
     *
     * @param wrkAnonType the wrkAnonType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new wrkAnonType, or with status {@code 400 (Bad Request)} if the wrkAnonType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/wrk-anon-types")
    public ResponseEntity<WrkAnonType> createWrkAnonType(@Valid @RequestBody WrkAnonType wrkAnonType) throws URISyntaxException {
        log.debug("REST request to save WrkAnonType : {}", wrkAnonType);
        if (wrkAnonType.getId() != null) {
            throw new BadRequestAlertException("A new wrkAnonType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WrkAnonType result = wrkAnonTypeRepository.save(wrkAnonType);
        return ResponseEntity.created(new URI("/api/wrk-anon-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /wrk-anon-types} : Updates an existing wrkAnonType.
     *
     * @param wrkAnonType the wrkAnonType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated wrkAnonType,
     * or with status {@code 400 (Bad Request)} if the wrkAnonType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the wrkAnonType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/wrk-anon-types")
    public ResponseEntity<WrkAnonType> updateWrkAnonType(@Valid @RequestBody WrkAnonType wrkAnonType) throws URISyntaxException {
        log.debug("REST request to update WrkAnonType : {}", wrkAnonType);
        if (wrkAnonType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        WrkAnonType result = wrkAnonTypeRepository.save(wrkAnonType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, wrkAnonType.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /wrk-anon-types} : get all the wrkAnonTypes.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of wrkAnonTypes in body.
     */
    @GetMapping("/wrk-anon-types")
    public List<WrkAnonType> getAllWrkAnonTypes() {
        log.debug("REST request to get all WrkAnonTypes");
        return wrkAnonTypeRepository.findAll();
    }

    /**
     * {@code GET  /wrk-anon-types/:id} : get the "id" wrkAnonType.
     *
     * @param id the id of the wrkAnonType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the wrkAnonType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/wrk-anon-types/{id}")
    public ResponseEntity<WrkAnonType> getWrkAnonType(@PathVariable Long id) {
        log.debug("REST request to get WrkAnonType : {}", id);
        Optional<WrkAnonType> wrkAnonType = wrkAnonTypeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(wrkAnonType);
    }

    /**
     * {@code DELETE  /wrk-anon-types/:id} : delete the "id" wrkAnonType.
     *
     * @param id the id of the wrkAnonType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/wrk-anon-types/{id}")
    public ResponseEntity<Void> deleteWrkAnonType(@PathVariable Long id) {
        log.debug("REST request to delete WrkAnonType : {}", id);
        wrkAnonTypeRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
