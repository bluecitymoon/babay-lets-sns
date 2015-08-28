package com.doubletuan.sns.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.doubletuan.sns.domain.ChatRoom;
import com.doubletuan.sns.domain.OpenFireConfiguration;
import com.doubletuan.sns.repository.ChatRoomRepository;
import com.doubletuan.sns.repository.OpenFireConfigurationRepository;

import org.igniterealtime.restclient.RestApiClient;
import org.igniterealtime.restclient.entity.AuthenticationToken;
import org.igniterealtime.restclient.entity.MUCRoomEntity;
import org.igniterealtime.restclient.entity.ParticipantEntities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.ws.rs.Path;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ChatRoom.
 */
@RestController
@RequestMapping("/api")
public class ChatRoomResource extends BaseResource {

    private final Logger log = LoggerFactory.getLogger(ChatRoomResource.class);

    @Inject
    private ChatRoomRepository chatRoomRepository;
    
    @Inject
    private OpenFireConfigurationRepository openFireConfigurationRepository;
    

    /**
     * POST  /chatRooms -> Create a new chatRoom.
     */
    @RequestMapping(value = "/chatRooms",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ChatRoom> create(@RequestBody ChatRoom chatRoom) throws URISyntaxException {
		
    	MUCRoomEntity mucRoomEntity = new MUCRoomEntity();
    	mucRoomEntity.setRoomName(chatRoom.getName());
    	
    	getRestApiClient().createChatRoom(mucRoomEntity);
    	
        log.debug("REST request to save ChatRoom : {}", chatRoom);
        if (chatRoom.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new chatRoom cannot already have an ID").body(null);
        }
        ChatRoom result = chatRoomRepository.save(chatRoom);
        return ResponseEntity.created(new URI("/api/chatRooms/" + chatRoom.getId())).body(result);
    }
    
    /**
     * POST  /chatRooms -> Invite {jid} to the room {room}.
     */
    @RequestMapping(value = "/chatRooms/invite/{jid}/toroom/{room}",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Boolean> invite(@PathVariable("jid") String jid, @PathVariable("room") String room) throws URISyntaxException {
    	
    	boolean result = getRestApiClient().addMember(room, jid);
    	
        return ResponseEntity.ok(result);
    }
    
    /**
     * POST  /chatRooms -> Invite {jid} to the room {room}.
     */
    @RequestMapping(value = "/chatRooms/participants/{room}",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ParticipantEntities> getChatRoomParticipants(@PathVariable("room") String roomName) throws URISyntaxException {
    	
    	ParticipantEntities result = getRestApiClient().getChatRoomParticipants(roomName);
    	
        return ResponseEntity.ok(result);
    }
    

    /**
     * PUT  /chatRooms -> Updates an existing chatRoom.
     */
    @RequestMapping(value = "/chatRooms",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ChatRoom> update(@RequestBody ChatRoom chatRoom) throws URISyntaxException {
        log.debug("REST request to update ChatRoom : {}", chatRoom);
        if (chatRoom.getId() == null) {
            return create(chatRoom);
        }
        ChatRoom result = chatRoomRepository.save(chatRoom);
        return ResponseEntity.ok().body(result);
    }

    /**
     * GET  /chatRooms -> get all the chatRooms.
     */
    @RequestMapping(value = "/chatRooms",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ChatRoom> getAll() {
        log.debug("REST request to get all ChatRooms");
        return chatRoomRepository.findAll();
    }

    /**
     * GET  /chatRooms/:id -> get the "id" chatRoom.
     */
    @RequestMapping(value = "/chatRooms/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ChatRoom> get(@PathVariable Long id) {
        log.debug("REST request to get ChatRoom : {}", id);
        return Optional.ofNullable(chatRoomRepository.findOne(id))
            .map(chatRoom -> new ResponseEntity<>(
                chatRoom,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /chatRooms/:id -> delete the "id" chatRoom.
     */
    @RequestMapping(value = "/chatRooms/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete ChatRoom : {}", id);
        chatRoomRepository.delete(id);
    }
}
