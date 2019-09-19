package net.anet.workflow.airflow.web.rest;

import net.anet.workflow.airflow.domain.Anonimation;
import net.anet.workflow.airflow.repository.AnonimationRepository;
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
 * REST controller for managing {@link net.anet.workflow.airflow.domain.Anonimation}.
 */
@RestController
@RequestMapping("/api")
public class AnonimationResource {

    private final Logger log = LoggerFactory.getLogger(AnonimationResource.class);

    private static final String ENTITY_NAME = "airflowAnonimation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AnonimationRepository anonimationRepository;

    public AnonimationResource(AnonimationRepository anonimationRepository) {
        this.anonimationRepository = anonimationRepository;
    }

    /**
     * {@code POST  /anonimations} : Create a new anonimation.
     *
     * @param anonimation the anonimation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new anonimation, or with status {@code 400 (Bad Request)} if the anonimation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/anonimations")
    public ResponseEntity<Anonimation> createAnonimation(@Valid @RequestBody Anonimation anonimation) throws URISyntaxException {
        log.debug("REST request to save Anonimation : {}", anonimation);
        if (anonimation.getId() != null) {
            throw new BadRequestAlertException("A new anonimation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Anonimation result = anonimationRepository.save(anonimation);
        return ResponseEntity.created(new URI("/api/anonimations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /anonimations} : Updates an existing anonimation.
     *
     * @param anonimation the anonimation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated anonimation,
     * or with status {@code 400 (Bad Request)} if the anonimation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the anonimation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/anonimations")
    public ResponseEntity<Anonimation> updateAnonimation(@Valid @RequestBody Anonimation anonimation) throws URISyntaxException {
        log.debug("REST request to update Anonimation : {}", anonimation);
        if (anonimation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Anonimation result = anonimationRepository.save(anonimation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, anonimation.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /anonimations} : get all the anonimations.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of anonimations in body.
     */
    @GetMapping("/anonimations")
    public ResponseEntity<List<Anonimation>> getAllAnonimations(Pageable pageable) {
        log.debug("REST request to get a page of Anonimations");
        Page<Anonimation> page = anonimationRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /anonimations/:id} : get the "id" anonimation.
     *
     * @param id the id of the anonimation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the anonimation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/anonimations/{id}")
    public ResponseEntity<Anonimation> getAnonimation(@PathVariable Long id) {
        log.debug("REST request to get Anonimation : {}", id);
        Optional<Anonimation> anonimation = anonimationRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(anonimation);
    }

    /**
     * {@code DELETE  /anonimations/:id} : delete the "id" anonimation.
     *
     * @param id the id of the anonimation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/anonimations/{id}")
    public ResponseEntity<Void> deleteAnonimation(@PathVariable Long id) {
        log.debug("REST request to delete Anonimation : {}", id);
        anonimationRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
