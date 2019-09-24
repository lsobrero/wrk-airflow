package net.anet.workflow.airflow.web.rest;

import net.anet.workflow.airflow.domain.WrkDataset;
import net.anet.workflow.airflow.service.WrkDatasetService;
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
 * REST controller for managing {@link net.anet.workflow.airflow.domain.WrkDataset}.
 */
@RestController
@RequestMapping("/api")
public class WrkDatasetResource {

    private final Logger log = LoggerFactory.getLogger(WrkDatasetResource.class);

    private static final String ENTITY_NAME = "wrkairflowWrkDataset";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WrkDatasetService wrkDatasetService;

    public WrkDatasetResource(WrkDatasetService wrkDatasetService) {
        this.wrkDatasetService = wrkDatasetService;
    }

    /**
     * {@code POST  /wrk-datasets} : Create a new wrkDataset.
     *
     * @param wrkDataset the wrkDataset to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new wrkDataset, or with status {@code 400 (Bad Request)} if the wrkDataset has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/wrk-datasets")
    public ResponseEntity<WrkDataset> createWrkDataset(@Valid @RequestBody WrkDataset wrkDataset) throws URISyntaxException {
        log.debug("REST request to save WrkDataset : {}", wrkDataset);
        if (wrkDataset.getId() != null) {
            throw new BadRequestAlertException("A new wrkDataset cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WrkDataset result = wrkDatasetService.save(wrkDataset);
        return ResponseEntity.created(new URI("/api/wrk-datasets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /wrk-datasets} : Updates an existing wrkDataset.
     *
     * @param wrkDataset the wrkDataset to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated wrkDataset,
     * or with status {@code 400 (Bad Request)} if the wrkDataset is not valid,
     * or with status {@code 500 (Internal Server Error)} if the wrkDataset couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/wrk-datasets")
    public ResponseEntity<WrkDataset> updateWrkDataset(@Valid @RequestBody WrkDataset wrkDataset) throws URISyntaxException {
        log.debug("REST request to update WrkDataset : {}", wrkDataset);
        if (wrkDataset.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        WrkDataset result = wrkDatasetService.save(wrkDataset);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, wrkDataset.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /wrk-datasets} : get all the wrkDatasets.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of wrkDatasets in body.
     */
    @GetMapping("/wrk-datasets")
    public ResponseEntity<List<WrkDataset>> getAllWrkDatasets(Pageable pageable) {
        log.debug("REST request to get a page of WrkDatasets");
        Page<WrkDataset> page = wrkDatasetService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /wrk-datasets/:id} : get the "id" wrkDataset.
     *
     * @param id the id of the wrkDataset to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the wrkDataset, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/wrk-datasets/{id}")
    public ResponseEntity<WrkDataset> getWrkDataset(@PathVariable Long id) {
        log.debug("REST request to get WrkDataset : {}", id);
        Optional<WrkDataset> wrkDataset = wrkDatasetService.findOne(id);
        return ResponseUtil.wrapOrNotFound(wrkDataset);
    }

    /**
     * {@code DELETE  /wrk-datasets/:id} : delete the "id" wrkDataset.
     *
     * @param id the id of the wrkDataset to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/wrk-datasets/{id}")
    public ResponseEntity<Void> deleteWrkDataset(@PathVariable Long id) {
        log.debug("REST request to delete WrkDataset : {}", id);
        wrkDatasetService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
