package com.doubletuan.sns.web.rest;

import com.doubletuan.sns.Application;
import com.doubletuan.sns.domain.ChatRoom;
import com.doubletuan.sns.repository.ChatRoomRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the ChatRoomResource REST controller.
 *
 * @see ChatRoomResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ChatRoomResourceTest {

    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";
    private static final String DEFAULT_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPTION = "UPDATED_TEXT";

    @Inject
    private ChatRoomRepository chatRoomRepository;

    private MockMvc restChatRoomMockMvc;

    private ChatRoom chatRoom;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ChatRoomResource chatRoomResource = new ChatRoomResource();
        ReflectionTestUtils.setField(chatRoomResource, "chatRoomRepository", chatRoomRepository);
        this.restChatRoomMockMvc = MockMvcBuilders.standaloneSetup(chatRoomResource).build();
    }

    @Before
    public void initTest() {
        chatRoom = new ChatRoom();
        chatRoom.setName(DEFAULT_NAME);
        chatRoom.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createChatRoom() throws Exception {
        int databaseSizeBeforeCreate = chatRoomRepository.findAll().size();

        // Create the ChatRoom
        restChatRoomMockMvc.perform(post("/api/chatRooms")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(chatRoom)))
                .andExpect(status().isCreated());

        // Validate the ChatRoom in the database
        List<ChatRoom> chatRooms = chatRoomRepository.findAll();
        assertThat(chatRooms).hasSize(databaseSizeBeforeCreate + 1);
        ChatRoom testChatRoom = chatRooms.get(chatRooms.size() - 1);
        assertThat(testChatRoom.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testChatRoom.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllChatRooms() throws Exception {
        // Initialize the database
        chatRoomRepository.saveAndFlush(chatRoom);

        // Get all the chatRooms
        restChatRoomMockMvc.perform(get("/api/chatRooms"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(chatRoom.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getChatRoom() throws Exception {
        // Initialize the database
        chatRoomRepository.saveAndFlush(chatRoom);

        // Get the chatRoom
        restChatRoomMockMvc.perform(get("/api/chatRooms/{id}", chatRoom.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(chatRoom.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingChatRoom() throws Exception {
        // Get the chatRoom
        restChatRoomMockMvc.perform(get("/api/chatRooms/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChatRoom() throws Exception {
        // Initialize the database
        chatRoomRepository.saveAndFlush(chatRoom);

		int databaseSizeBeforeUpdate = chatRoomRepository.findAll().size();

        // Update the chatRoom
        chatRoom.setName(UPDATED_NAME);
        chatRoom.setDescription(UPDATED_DESCRIPTION);
        restChatRoomMockMvc.perform(put("/api/chatRooms")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(chatRoom)))
                .andExpect(status().isOk());

        // Validate the ChatRoom in the database
        List<ChatRoom> chatRooms = chatRoomRepository.findAll();
        assertThat(chatRooms).hasSize(databaseSizeBeforeUpdate);
        ChatRoom testChatRoom = chatRooms.get(chatRooms.size() - 1);
        assertThat(testChatRoom.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testChatRoom.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void deleteChatRoom() throws Exception {
        // Initialize the database
        chatRoomRepository.saveAndFlush(chatRoom);

		int databaseSizeBeforeDelete = chatRoomRepository.findAll().size();

        // Get the chatRoom
        restChatRoomMockMvc.perform(delete("/api/chatRooms/{id}", chatRoom.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ChatRoom> chatRooms = chatRoomRepository.findAll();
        assertThat(chatRooms).hasSize(databaseSizeBeforeDelete - 1);
    }
}
