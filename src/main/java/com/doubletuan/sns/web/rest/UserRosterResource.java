package com.doubletuan.sns.web.rest;

import org.igniterealtime.restclient.entity.RosterEntities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

/**
 * REST controller for managing UserPost.
 */
@RestController
@RequestMapping("/api")
public class UserRosterResource extends BaseResource{

	private final Logger log = LoggerFactory.getLogger(UserRosterResource.class);

	/**
	 * GET /userPosts/:id -> get the "id" userPost.
	 */
	@RequestMapping(value = "/userRoster/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<RosterEntities> getUserRoster(@PathVariable String id) {
		
		RosterEntities rosterEntities = getRestApiClient().getRoster(id);
		
		return ResponseEntity.ok(rosterEntities);
	}
}
