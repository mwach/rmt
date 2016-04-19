package com.mobica.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mobica.domain.Msu;
import com.mobica.repository.MsuRepository;
import com.mobica.web.rest.util.HeaderUtil;
import com.mobica.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Msu.
 */
@RestController
@RequestMapping("/api")
public class MsuResource {

    private final Logger log = LoggerFactory.getLogger(MsuResource.class);
        
    @Inject
    private MsuRepository msuRepository;
    
    /**
     * POST  /msus -> Create a new msu.
     */
    @RequestMapping(value = "/msus",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Msu> createMsu(@Valid @RequestBody Msu msu) throws URISyntaxException {
        log.debug("REST request to save Msu : {}", msu);
        if (msu.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("msu", "idexists", "A new msu cannot already have an ID")).body(null);
        }
        Msu result = msuRepository.save(msu);
        return ResponseEntity.created(new URI("/api/msus/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("msu", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /msus -> Updates an existing msu.
     */
    @RequestMapping(value = "/msus",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Msu> updateMsu(@Valid @RequestBody Msu msu) throws URISyntaxException {
        log.debug("REST request to update Msu : {}", msu);
        if (msu.getId() == null) {
            return createMsu(msu);
        }
        Msu result = msuRepository.save(msu);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("msu", msu.getId().toString()))
            .body(result);
    }

    /**
     * GET  /msus -> get all the msus.
     */
    @RequestMapping(value = "/msus",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Msu>> getAllMsus(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Msus");
        Page<Msu> page = msuRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/msus");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /msus/:id -> get the "id" msu.
     */
    @RequestMapping(value = "/msus/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Msu> getMsu(@PathVariable Long id) {
        log.debug("REST request to get Msu : {}", id);
        Msu msu = msuRepository.findOne(id);
        return Optional.ofNullable(msu)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /msus/:id -> delete the "id" msu.
     */
    @RequestMapping(value = "/msus/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMsu(@PathVariable Long id) {
        log.debug("REST request to delete Msu : {}", id);
        msuRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("msu", id.toString())).build();
    }
}
