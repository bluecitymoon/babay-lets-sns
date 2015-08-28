package com.doubletuan.sns.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.doubletuan.sns.domain.DTUser;
import com.doubletuan.sns.repository.DTUserRepository;
import com.doubletuan.sns.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing DTUser.
 */
@RestController
@RequestMapping("/api")
public class DTUserResource {

    private final Logger log = LoggerFactory.getLogger(DTUserResource.class);

    @Inject
    private DTUserRepository dTUserRepository;

    /**
     * POST  /dTUsers -> Create a new dTUser.
     */
    @RequestMapping(value = "/dTUsers",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DTUser> create(@RequestBody DTUser dTUser) throws URISyntaxException {
        log.debug("REST request to save DTUser : {}", dTUser);
        if (dTUser.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new dTUser cannot already have an ID").body(null);
        }
        DTUser result = dTUserRepository.save(dTUser);
        return ResponseEntity.created(new URI("/api/dTUsers/" + dTUser.getId())).body(result);
    }

    /**
     * PUT  /dTUsers -> Updates an existing dTUser.
     */
    @RequestMapping(value = "/dTUsers",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DTUser> update(@RequestBody DTUser dTUser) throws URISyntaxException {
        log.debug("REST request to update DTUser : {}", dTUser);
        if (dTUser.getId() == null) {
            return create(dTUser);
        }
        DTUser result = dTUserRepository.save(dTUser);
        return ResponseEntity.ok().body(result);
    }

    /**
     * GET  /dTUsers -> get all the dTUsers.
     */
    @RequestMapping(value = "/dTUsers",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<DTUser>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<DTUser> page = dTUserRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/dTUsers", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /dTUsers/:id -> get the "id" dTUser.
     */
    @RequestMapping(value = "/dTUsers/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DTUser> get(@PathVariable Long id) {
        log.debug("REST request to get DTUser : {}", id);
        return Optional.ofNullable(dTUserRepository.findOne(id))
            .map(dTUser -> new ResponseEntity<>(
                dTUser,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /dTUsers/:id -> delete the "id" dTUser.
     */
    @RequestMapping(value = "/dTUsers/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete DTUser : {}", id);
        dTUserRepository.delete(id);
    }
}
