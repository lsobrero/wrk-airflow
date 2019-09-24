package net.anet.workflow.airflow.web.rest;

import io.github.jhipster.web.util.PaginationUtil;
import net.anet.workflow.airflow.domain.WrkDataset;
import net.anet.workflow.airflow.service.AirFlowDatasetService;
import net.anet.workflow.airflow.service.WrkDatasetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

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
    public ResponseEntity<List<WrkDataset>> getAllWrkDatasets(Pageable pageable) {
        log.debug("REST request to get a page of WrkDatasets");
        Page<WrkDataset> page = afDatasetService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
