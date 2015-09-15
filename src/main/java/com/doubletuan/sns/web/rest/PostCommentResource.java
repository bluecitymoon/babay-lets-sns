package com.doubletuan.sns.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.doubletuan.sns.domain.PostComment;
import com.doubletuan.sns.repository.PostCommentRepository;
import com.doubletuan.sns.service.PostService;
import com.doubletuan.sns.web.rest.util.PaginationUtil;

/**
 * REST controller for managing PostComment.
 */
@RestController
@RequestMapping("/api")
public class PostCommentResource {

    private final Logger log = LoggerFactory.getLogger(PostCommentResource.class);

    @Inject
    private PostCommentRepository postCommentRepository;
    
    @Resource(name="PostService")
    private PostService postService;

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
     * POST /postComments > Create a new postComment.
     */
	@RequestMapping(value = "/comment/{post}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Long> comment(@RequestBody PostComment postComment, @PathVariable("post") Long postId)
			throws URISyntaxException {
		log.debug("REST request to save PostComment : {}, {}", postComment, postId);
		if (postComment.getId() != null) {
			return ResponseEntity.badRequest().header("Failure", "A new postComment cannot already have an ID")
					.body(null);
		}

		PostComment savedPostComment = postService.commentPost(postComment, postId);

		return ResponseEntity.ok(savedPostComment.getId());
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
