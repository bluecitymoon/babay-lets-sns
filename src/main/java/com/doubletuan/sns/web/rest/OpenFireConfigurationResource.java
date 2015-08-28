package com.doubletuan.sns.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.doubletuan.sns.domain.OpenFireConfiguration;
import com.doubletuan.sns.repository.OpenFireConfigurationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing OpenFireConfiguration.
 */
@RestController
@RequestMapping("/api")
public class OpenFireConfigurationResource {

    private final Logger log = LoggerFactory.getLogger(OpenFireConfigurationResource.class);

    @Inject
    private OpenFireConfigurationRepository openFireConfigurationRepository;

    /**
     * POST  /openFireConfigurations -> Create a new openFireConfiguration.
     */
    @RequestMapping(value = "/openFireConfigurations",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OpenFireConfiguration> create(@RequestBody OpenFireConfiguration openFireConfiguration) throws URISyntaxException {
        log.debug("REST request to save OpenFireConfiguration : {}", openFireConfiguration);
        if (openFireConfiguration.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new openFireConfiguration cannot already have an ID").body(null);
        }
        OpenFireConfiguration result = openFireConfigurationRepository.save(openFireConfiguration);
        return ResponseEntity.created(new URI("/api/openFireConfigurations/" + openFireConfiguration.getId())).body(result);
    }

    /**
     * PUT  /openFireConfigurations -> Updates an existing openFireConfiguration.
     */
    @RequestMapping(value = "/openFireConfigurations",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OpenFireConfiguration> update(@RequestBody OpenFireConfiguration openFireConfiguration) throws URISyntaxException {
        log.debug("REST request to update OpenFireConfiguration : {}", openFireConfiguration);
        if (openFireConfiguration.getId() == null) {
            return create(openFireConfiguration);
        }
        OpenFireConfiguration result = openFireConfigurationRepository.save(openFireConfiguration);
        return ResponseEntity.ok().body(result);
    }

    /**
     * GET  /openFireConfigurations -> get all the openFireConfigurations.
     */
    @RequestMapping(value = "/openFireConfigurations",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<OpenFireConfiguration> getAll() {
        log.debug("REST request to get all OpenFireConfigurations");
        return openFireConfigurationRepository.findAll();
    }

    /**
     * GET  /openFireConfigurations/:id -> get the "id" openFireConfiguration.
     */
    @RequestMapping(value = "/openFireConfigurations/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OpenFireConfiguration> get(@PathVariable Long id) {
        log.debug("REST request to get OpenFireConfiguration : {}", id);
        return Optional.ofNullable(openFireConfigurationRepository.findOne(id))
            .map(openFireConfiguration -> new ResponseEntity<>(
                openFireConfiguration,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /openFireConfigurations/:id -> delete the "id" openFireConfiguration.
     */
    @RequestMapping(value = "/openFireConfigurations/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete OpenFireConfiguration : {}", id);
        openFireConfigurationRepository.delete(id);
    }
}
