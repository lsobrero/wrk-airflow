package net.anet.workflow.airflow.web.rest;

import net.anet.workflow.airflow.service.MyTestService;
import net.anet.workflow.airflow.web.rest.errors.BadRequestAlertException;
import net.anet.workflow.airflow.service.dto.MyTestDTO;

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
 * REST controller for managing {@link net.anet.workflow.airflow.domain.MyTest}.
 */
@RestController
@RequestMapping("/api")
public class MyTestResource {

    private final Logger log = LoggerFactory.getLogger(MyTestResource.class);

    private static final String ENTITY_NAME = "wrkairflowMyTest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MyTestService myTestService;

    public MyTestResource(MyTestService myTestService) {
        this.myTestService = myTestService;
    }

    /**
     * {@code POST  /my-tests} : Create a new myTest.
     *
     * @param myTestDTO the myTestDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new myTestDTO, or with status {@code 400 (Bad Request)} if the myTest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/my-tests")
    public ResponseEntity<MyTestDTO> createMyTest(@Valid @RequestBody MyTestDTO myTestDTO) throws URISyntaxException {
        log.debug("REST request to save MyTest : {}", myTestDTO);
        if (myTestDTO.getId() != null) {
            throw new BadRequestAlertException("A new myTest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MyTestDTO result = myTestService.save(myTestDTO);
        return ResponseEntity.created(new URI("/api/my-tests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /my-tests} : Updates an existing myTest.
     *
     * @param myTestDTO the myTestDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated myTestDTO,
     * or with status {@code 400 (Bad Request)} if the myTestDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the myTestDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/my-tests")
    public ResponseEntity<MyTestDTO> updateMyTest(@Valid @RequestBody MyTestDTO myTestDTO) throws URISyntaxException {
        log.debug("REST request to update MyTest : {}", myTestDTO);
        if (myTestDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MyTestDTO result = myTestService.save(myTestDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, myTestDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /my-tests} : get all the myTests.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of myTests in body.
     */
    @GetMapping("/my-tests")
    public List<MyTestDTO> getAllMyTests() {
        log.debug("REST request to get all MyTests");
        return myTestService.findAll();
    }

    /**
     * {@code GET  /my-tests/:id} : get the "id" myTest.
     *
     * @param id the id of the myTestDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the myTestDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/my-tests/{id}")
    public ResponseEntity<MyTestDTO> getMyTest(@PathVariable Long id) {
        log.debug("REST request to get MyTest : {}", id);
        Optional<MyTestDTO> myTestDTO = myTestService.findOne(id);
        return ResponseUtil.wrapOrNotFound(myTestDTO);
    }

    /**
     * {@code DELETE  /my-tests/:id} : delete the "id" myTest.
     *
     * @param id the id of the myTestDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/my-tests/{id}")
    public ResponseEntity<Void> deleteMyTest(@PathVariable Long id) {
        log.debug("REST request to delete MyTest : {}", id);
        myTestService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
