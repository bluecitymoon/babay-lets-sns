package com.doubletuan.sns.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.doubletuan.sns.domain.CommentReply;
import com.doubletuan.sns.repository.CommentReplyRepository;
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
 * REST controller for managing CommentReply.
 */
@RestController
@RequestMapping("/api")
public class CommentReplyResource {

    private final Logger log = LoggerFactory.getLogger(CommentReplyResource.class);

    @Inject
    private CommentReplyRepository commentReplyRepository;

    /**
     * POST  /commentReplys -> Create a new commentReply.
     */
    @RequestMapping(value = "/commentReplys",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CommentReply> create(@RequestBody CommentReply commentReply) throws URISyntaxException {
        log.debug("REST request to save CommentReply : {}", commentReply);
        if (commentReply.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new commentReply cannot already have an ID").body(null);
        }
        CommentReply result = commentReplyRepository.save(commentReply);
        return ResponseEntity.created(new URI("/api/commentReplys/" + commentReply.getId())).body(result);
    }

    /**
     * PUT  /commentReplys -> Updates an existing commentReply.
     */
    @RequestMapping(value = "/commentReplys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CommentReply> update(@RequestBody CommentReply commentReply) throws URISyntaxException {
        log.debug("REST request to update CommentReply : {}", commentReply);
        if (commentReply.getId() == null) {
            return create(commentReply);
        }
        CommentReply result = commentReplyRepository.save(commentReply);
        return ResponseEntity.ok().body(result);
    }

    /**
     * GET  /commentReplys -> get all the commentReplys.
     */
    @RequestMapping(value = "/commentReplys",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<CommentReply>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<CommentReply> page = commentReplyRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/commentReplys", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /commentReplys/:id -> get the "id" commentReply.
     */
    @RequestMapping(value = "/commentReplys/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CommentReply> get(@PathVariable Long id) {
        log.debug("REST request to get CommentReply : {}", id);
        return Optional.ofNullable(commentReplyRepository.findOne(id))
            .map(commentReply -> new ResponseEntity<>(
                commentReply,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /commentReplys/:id -> delete the "id" commentReply.
     */
    @RequestMapping(value = "/commentReplys/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete CommentReply : {}", id);
        commentReplyRepository.delete(id);
    }
}
