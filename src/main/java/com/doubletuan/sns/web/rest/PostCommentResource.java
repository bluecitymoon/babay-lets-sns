package com.doubletuan.sns.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.doubletuan.sns.domain.PostComment;
import com.doubletuan.sns.repository.PostCommentRepository;
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
 * REST controller for managing PostComment.
 */
@RestController
@RequestMapping("/api")
public class PostCommentResource {

    private final Logger log = LoggerFactory.getLogger(PostCommentResource.class);

    @Inject
    private PostCommentRepository postCommentRepository;

    /**
     * POST  /postComments -> Create a new postComment.
     */
    @RequestMapping(value = "/postComments",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PostComment> create(@RequestBody PostComment postComment) throws URISyntaxException {
        log.debug("REST request to save PostComment : {}", postComment);
        if (postComment.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new postComment cannot already have an ID").body(null);
        }
        PostComment result = postCommentRepository.save(postComment);
        return ResponseEntity.created(new URI("/api/postComments/" + postComment.getId())).body(result);
    }

    /**
     * PUT  /postComments -> Updates an existing postComment.
     */
    @RequestMapping(value = "/postComments",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PostComment> update(@RequestBody PostComment postComment) throws URISyntaxException {
        log.debug("REST request to update PostComment : {}", postComment);
        if (postComment.getId() == null) {
            return create(postComment);
        }
        PostComment result = postCommentRepository.save(postComment);
        return ResponseEntity.ok().body(result);
    }

    /**
     * GET  /postComments -> get all the postComments.
     */
    @RequestMapping(value = "/postComments",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<PostComment>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<PostComment> page = postCommentRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/postComments", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /postComments/:id -> get the "id" postComment.
     */
    @RequestMapping(value = "/postComments/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PostComment> get(@PathVariable Long id) {
        log.debug("REST request to get PostComment : {}", id);
        return Optional.ofNullable(postCommentRepository.findOne(id))
            .map(postComment -> new ResponseEntity<>(
                postComment,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /postComments/:id -> delete the "id" postComment.
     */
    @RequestMapping(value = "/postComments/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete PostComment : {}", id);
        postCommentRepository.delete(id);
    }
}
