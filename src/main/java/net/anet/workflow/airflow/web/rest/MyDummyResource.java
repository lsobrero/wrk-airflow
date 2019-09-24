package net.anet.workflow.airflow.web.rest;

import net.anet.workflow.airflow.service.MyDummyService;
import net.anet.workflow.airflow.web.rest.errors.BadRequestAlertException;
import net.anet.workflow.airflow.service.dto.MyDummyDTO;

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
 * REST controller for managing {@link net.anet.workflow.airflow.domain.MyDummy}.
 */
@RestController
@RequestMapping("/api")
public class MyDummyResource {

    private final Logger log = LoggerFactory.getLogger(MyDummyResource.class);

    private static final String ENTITY_NAME = "wrkairflowMyDummy";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MyDummyService myDummyService;

    public MyDummyResource(MyDummyService myDummyService) {
        this.myDummyService = myDummyService;
    }

    /**
     * {@code POST  /my-dummies} : Create a new myDummy.
     *
     * @param myDummyDTO the myDummyDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new myDummyDTO, or with status {@code 400 (Bad Request)} if the myDummy has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/my-dummies")
    public ResponseEntity<MyDummyDTO> createMyDummy(@Valid @RequestBody MyDummyDTO myDummyDTO) throws URISyntaxException {
        log.debug("REST request to save MyDummy : {}", myDummyDTO);
        if (myDummyDTO.getId() != null) {
            throw new BadRequestAlertException("A new myDummy cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MyDummyDTO result = myDummyService.save(myDummyDTO);
        return ResponseEntity.created(new URI("/api/my-dummies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /my-dummies} : Updates an existing myDummy.
     *
     * @param myDummyDTO the myDummyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated myDummyDTO,
     * or with status {@code 400 (Bad Request)} if the myDummyDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the myDummyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/my-dummies")
    public ResponseEntity<MyDummyDTO> updateMyDummy(@Valid @RequestBody MyDummyDTO myDummyDTO) throws URISyntaxException {
        log.debug("REST request to update MyDummy : {}", myDummyDTO);
        if (myDummyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MyDummyDTO result = myDummyService.save(myDummyDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, myDummyDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /my-dummies} : get all the myDummies.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of myDummies in body.
     */
    @GetMapping("/my-dummies")
    public List<MyDummyDTO> getAllMyDummies() {
        log.debug("REST request to get all MyDummies");
        return myDummyService.findAll();
    }

    /**
     * {@code GET  /my-dummies/:id} : get the "id" myDummy.
     *
     * @param id the id of the myDummyDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the myDummyDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/my-dummies/{id}")
    public ResponseEntity<MyDummyDTO> getMyDummy(@PathVariable Long id) {
        log.debug("REST request to get MyDummy : {}", id);
        Optional<MyDummyDTO> myDummyDTO = myDummyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(myDummyDTO);
    }

    /**
     * {@code DELETE  /my-dummies/:id} : delete the "id" myDummy.
     *
     * @param id the id of the myDummyDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/my-dummies/{id}")
    public ResponseEntity<Void> deleteMyDummy(@PathVariable Long id) {
        log.debug("REST request to delete MyDummy : {}", id);
        myDummyService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
