package com.doubletuan.sns.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.doubletuan.sns.domain.SystemConfiguration;
import com.doubletuan.sns.repository.SystemConfigurationRepository;
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
 * REST controller for managing SystemConfiguration.
 */
@RestController
@RequestMapping("/api")
public class SystemConfigurationResource {

    private final Logger log = LoggerFactory.getLogger(SystemConfigurationResource.class);

    @Inject
    private SystemConfigurationRepository systemConfigurationRepository;

    /**
     * POST  /systemConfigurations -> Create a new systemConfiguration.
     */
    @RequestMapping(value = "/systemConfigurations",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SystemConfiguration> create(@RequestBody SystemConfiguration systemConfiguration) throws URISyntaxException {
        log.debug("REST request to save SystemConfiguration : {}", systemConfiguration);
        if (systemConfiguration.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new systemConfiguration cannot already have an ID").body(null);
        }
        SystemConfiguration result = systemConfigurationRepository.save(systemConfiguration);
        return ResponseEntity.created(new URI("/api/systemConfigurations/" + systemConfiguration.getId())).body(result);
    }

    /**
     * PUT  /systemConfigurations -> Updates an existing systemConfiguration.
     */
    @RequestMapping(value = "/systemConfigurations",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SystemConfiguration> update(@RequestBody SystemConfiguration systemConfiguration) throws URISyntaxException {
        log.debug("REST request to update SystemConfiguration : {}", systemConfiguration);
        if (systemConfiguration.getId() == null) {
            return create(systemConfiguration);
        }
        SystemConfiguration result = systemConfigurationRepository.save(systemConfiguration);
        return ResponseEntity.ok().body(result);
    }

    /**
     * GET  /systemConfigurations -> get all the systemConfigurations.
     */
    @RequestMapping(value = "/systemConfigurations",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<SystemConfiguration>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<SystemConfiguration> page = systemConfigurationRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/systemConfigurations", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /systemConfigurations/:id -> get the "id" systemConfiguration.
     */
    @RequestMapping(value = "/systemConfigurations/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SystemConfiguration> get(@PathVariable Long id) {
        log.debug("REST request to get SystemConfiguration : {}", id);
        return Optional.ofNullable(systemConfigurationRepository.findOne(id))
            .map(systemConfiguration -> new ResponseEntity<>(
                systemConfiguration,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /systemConfigurations/:id -> delete the "id" systemConfiguration.
     */
    @RequestMapping(value = "/systemConfigurations/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete SystemConfiguration : {}", id);
        systemConfigurationRepository.delete(id);
    }
}
