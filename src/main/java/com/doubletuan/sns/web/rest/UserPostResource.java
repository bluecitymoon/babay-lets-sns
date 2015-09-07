package com.doubletuan.sns.web.rest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;

import javax.imageio.stream.FileImageOutputStream;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
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
import com.doubletuan.sns.web.rest.util.PaginationUtil;
import com.google.common.io.Files;

/**
 * REST controller for managing UserPost.
 */
@RestController
@RequestMapping("/api")
public class UserPostResource {

	private final Logger log = LoggerFactory.getLogger(UserPostResource.class);

	@Inject
	private UserPostRepository userPostRepository;

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
	 * @throws IOException 
	 */
	@RequestMapping(value = "/userPostsWithImages", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes= MediaType.MULTIPART_FORM_DATA_VALUE)
	@Timed
	public ResponseEntity createWithImages( @RequestParam(value = "fileName", required = false) String fileName, @RequestParam(value = "file", required = false) MultipartFile[] files)
			throws URISyntaxException, IOException {

		System.out.println("file name is " + fileName);
		
		if (files != null && files.length > 0) {
			
			for (int i = 0; i < files.length; i++) {
				MultipartFile file = files[i];
				
				FileOutputStream fileOutputStream = new FileOutputStream(new File("/Users/jerry/Desktop/api/04-代码/dt-tarmag/dt-tarmag-api/pictures/" + System.currentTimeMillis() + "_"+ file.getOriginalFilename()));
				
				IOUtils.copy(file.getInputStream(), fileOutputStream);
			}
		}
		
		return ResponseEntity.ok().build();

	}

	protected int size(InputStream stream) {
		int length = 0;
		try {
			byte[] buffer = new byte[2048];
			int size;
			while ((size = stream.read(buffer)) != -1) {
				length += size;
				// for (int i = 0; i < size; i++) {
				// System.out.print((char) buffer[i]);
				// }
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return length;

	}

	protected String read(InputStream stream) {
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		try {
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
			}
		}
		return sb.toString();
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
