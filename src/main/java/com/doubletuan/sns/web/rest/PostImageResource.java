package com.doubletuan.sns.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.doubletuan.sns.domain.PostImage;
import com.doubletuan.sns.repository.PostImageRepository;
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
 * REST controller for managing PostImage.
 */
@RestController
@RequestMapping("/api")
public class PostImageResource {

    private final Logger log = LoggerFactory.getLogger(PostImageResource.class);

    @Inject
    private PostImageRepository postImageRepository;

    /**
     * POST  /postImages -> Create a new postImage.
     */
    @RequestMapping(value = "/postImages",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PostImage> create(@RequestBody PostImage postImage) throws URISyntaxException {
        log.debug("REST request to save PostImage : {}", postImage);
        if (postImage.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new postImage cannot already have an ID").body(null);
        }
        PostImage result = postImageRepository.save(postImage);
        return ResponseEntity.created(new URI("/api/postImages/" + postImage.getId())).body(result);
    }

    /**
     * PUT  /postImages -> Updates an existing postImage.
     */
    @RequestMapping(value = "/postImages",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PostImage> update(@RequestBody PostImage postImage) throws URISyntaxException {
        log.debug("REST request to update PostImage : {}", postImage);
        if (postImage.getId() == null) {
            return create(postImage);
        }
        PostImage result = postImageRepository.save(postImage);
        return ResponseEntity.ok().body(result);
    }

    /**
     * GET  /postImages -> get all the postImages.
     */
    @RequestMapping(value = "/postImages",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<PostImage>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<PostImage> page = postImageRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/postImages", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /postImages/:id -> get the "id" postImage.
     */
    @RequestMapping(value = "/postImages/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PostImage> get(@PathVariable Long id) {
        log.debug("REST request to get PostImage : {}", id);
        return Optional.ofNullable(postImageRepository.findOne(id))
            .map(postImage -> new ResponseEntity<>(
                postImage,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /postImages/:id -> delete the "id" postImage.
     */
    @RequestMapping(value = "/postImages/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete PostImage : {}", id);
        postImageRepository.delete(id);
    }
}
