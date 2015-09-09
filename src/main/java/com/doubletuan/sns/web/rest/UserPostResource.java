package com.doubletuan.sns.web.rest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;
import javax.inject.Inject;

import org.igniterealtime.restclient.entity.RosterEntities;
import org.igniterealtime.restclient.entity.RosterItemEntity;
import org.joda.time.DateTime;
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
import org.springframework.web.multipart.MultipartFile;

import com.codahale.metrics.annotation.Timed;
import com.doubletuan.sns.domain.UserPost;
import com.doubletuan.sns.repository.UserPostRepository;
import com.doubletuan.sns.service.PostService;
import com.doubletuan.sns.web.rest.util.PaginationUtil;

/**
 * REST controller for managing UserPost.
 */
@RestController
@RequestMapping("/api")
public class UserPostResource extends BaseResource{

	private final Logger log = LoggerFactory.getLogger(UserPostResource.class);

	@Inject
	private UserPostRepository userPostRepository;
	
	@Resource(name="PostService")
	private PostService postService;

	/**
	 * POST /userPosts -> Create a new userPost.
	 */
	@RequestMapping(value = "/userPosts", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<UserPost> create(@RequestBody UserPost userPost) throws URISyntaxException {
		log.debug("REST request to save UserPost : {}", userPost);
		if (userPost.getId() != null) {
			return ResponseEntity.badRequest().header("Failure", "A new userPost cannot already have an ID").body(null);
		}
		UserPost result = userPostRepository.save(userPost);
		return ResponseEntity.created(new URI("/api/userPosts/" + userPost.getId())).body(result);
	}

	/**
	 * POST /userPosts -> Create a new userPost.
	 */
	@RequestMapping(value = "/singlePost", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<String> createSinglePost(@RequestBody UserPost userPost) throws URISyntaxException {
		
		log.debug("REST request to save single post : {}", userPost);
		if (userPost.getId() != null) {
			return ResponseEntity.badRequest().header("Failure", "A new userPost cannot already have an ID").body(null);
		}
		UserPost result = userPostRepository.save(userPost);
		
		return ResponseEntity.ok().body(result.getId().toString());
	}
	
	/**
	 * POST /userPosts -> Create a new userPost.
	 * 
	 * @throws IOException
	 */
	@RequestMapping(value = "/userPostsWithImages", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity createWithImages(@RequestParam(value = "content", required = false) String content, @RequestParam(value = "jid", required = true) String jid, 
			@RequestParam(value = "file", required = false) MultipartFile[] files)
					throws URISyntaxException, IOException {

		UserPost userPost = new UserPost();
		userPost.setContent(content);
		userPost.setJid(jid);
		userPost.setCreateDate(new DateTime());
		
		log.debug("recieve new post -> " + userPost.toString());
		
		postService.createNewPost(userPost, files);

		return ResponseEntity.ok().build();

	}

	/**
	 * POST /userPosts -> Create a new userPost.
	 * 
	 * @throws IOException
	 */
	@RequestMapping(value = "/userPostsWithSingleImage", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity createWithSingleImage(@RequestParam(value = "postId", required = false) Long postId,
			@RequestParam(value = "file", required = false) MultipartFile file) throws URISyntaxException, IOException {

		log.debug("REST request to save single image for post id = : {}", postId);
		
		if (postId != null) {
			postService.saveSingleImageForPost(postId, file);
			
		}
		return ResponseEntity.ok().build();

	}

	/**
	 * PUT /userPosts -> Updates an existing userPost.
	 */
	@RequestMapping(value = "/userPosts", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<UserPost> update(@RequestBody UserPost userPost) throws URISyntaxException {
		log.debug("REST request to update UserPost : {}", userPost);
		if (userPost.getId() == null) {
			return create(userPost);
		}
		UserPost result = userPostRepository.save(userPost);
		return ResponseEntity.ok().body(result);
	}

	/**
	 * GET /userPosts -> get all the userPosts.
	 */
	@RequestMapping(value = "/userPosts", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<List<UserPost>> getAll(@RequestParam(value = "page", required = false) Integer offset,
			@RequestParam(value = "per_page", required = false) Integer limit) throws URISyntaxException {
		Page<UserPost> page = userPostRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/userPosts", offset, limit);
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /userPosts -> get all the userPosts.
	 */
	@RequestMapping(value = "/avaliablePosts", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<UserPost>> getUserVisiblePosts(@RequestParam(value = "page", required = false) Integer offset,
			@RequestParam(value = "per_page", required = false) Integer limit,
			@RequestParam(value = "userId", required = true) String userId) throws URISyntaxException {
		
		RosterEntities rosterEntities = getRestApiClient().getRoster(userId);
		List<String> rosterJids = getRosterJidsFromEntityList(rosterEntities, userId);
		
		Page<UserPost> page = postService.findUserReadablePosts(rosterJids, offset, limit);
		
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/userPosts", offset, limit);
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}
	
	private List<String> getRosterJidsFromEntityList(RosterEntities rosterEntities, String userId) {
		
		List<String> jids = new ArrayList<>();
		jids.add(userId);
		
		for (RosterItemEntity rosterItem : rosterEntities.getRoster()) {
			String singleJid = rosterItem.getJid();
			if (singleJid.contains("@")) {
				singleJid = singleJid.split("@")[0];
			}
			jids.add(singleJid);
		}
		
		return jids;
	}

	/**
	 * GET /userPosts/:id -> get the "id" userPost.
	 */
	@RequestMapping(value = "/userPosts/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<UserPost> get(@PathVariable Long id) {
		log.debug("REST request to get UserPost : {}", id);
		return Optional.ofNullable(userPostRepository.findOne(id))
				.map(userPost -> new ResponseEntity<>(userPost, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	/**
	 * DELETE /userPosts/:id -> delete the "id" userPost.
	 */
	@RequestMapping(value = "/userPosts/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public void delete(@PathVariable Long id) {
		log.debug("REST request to delete UserPost : {}", id);
		userPostRepository.delete(id);
	}
}
