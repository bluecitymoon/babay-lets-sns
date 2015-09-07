package com.doubletuan.sns.web.rest;

import com.doubletuan.sns.Application;
import com.doubletuan.sns.domain.UserPost;
import com.doubletuan.sns.repository.UserPostRepository;

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
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the UserPostResource REST controller.
 *
 * @see UserPostResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class UserPostResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private static final String DEFAULT_CONTENT = "SAMPLE_TEXT";
    private static final String UPDATED_CONTENT = "UPDATED_TEXT";

    private static final DateTime DEFAULT_CREATE_DATE = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_CREATE_DATE = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_CREATE_DATE_STR = dateTimeFormatter.print(DEFAULT_CREATE_DATE);

    private static final Integer DEFAULT_GREET_COUNT = 0;
    private static final Integer UPDATED_GREET_COUNT = 1;

    private static final Integer DEFAULT_COMMENTS_COUNT = 0;
    private static final Integer UPDATED_COMMENTS_COUNT = 1;
    private static final String DEFAULT_JID = "SAMPLE_TEXT";
    private static final String UPDATED_JID = "UPDATED_TEXT";

    @Inject
    private UserPostRepository userPostRepository;

    private MockMvc restUserPostMockMvc;

    private UserPost userPost;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UserPostResource userPostResource = new UserPostResource();
        ReflectionTestUtils.setField(userPostResource, "userPostRepository", userPostRepository);
        this.restUserPostMockMvc = MockMvcBuilders.standaloneSetup(userPostResource).build();
    }

    @Before
    public void initTest() {
        userPost = new UserPost();
        userPost.setContent(DEFAULT_CONTENT);
        userPost.setCreateDate(DEFAULT_CREATE_DATE);
        userPost.setGreetCount(DEFAULT_GREET_COUNT);
        userPost.setCommentsCount(DEFAULT_COMMENTS_COUNT);
        userPost.setJid(DEFAULT_JID);
    }

    @Test
    @Transactional
    public void createUserPost() throws Exception {
        int databaseSizeBeforeCreate = userPostRepository.findAll().size();

        // Create the UserPost
        restUserPostMockMvc.perform(post("/api/userPosts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userPost)))
                .andExpect(status().isCreated());

        // Validate the UserPost in the database
        List<UserPost> userPosts = userPostRepository.findAll();
        assertThat(userPosts).hasSize(databaseSizeBeforeCreate + 1);
        UserPost testUserPost = userPosts.get(userPosts.size() - 1);
        assertThat(testUserPost.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testUserPost.getCreateDate().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testUserPost.getGreetCount()).isEqualTo(DEFAULT_GREET_COUNT);
        assertThat(testUserPost.getCommentsCount()).isEqualTo(DEFAULT_COMMENTS_COUNT);
        assertThat(testUserPost.getJid()).isEqualTo(DEFAULT_JID);
    }

    @Test
    @Transactional
    public void getAllUserPosts() throws Exception {
        // Initialize the database
        userPostRepository.saveAndFlush(userPost);

        // Get all the userPosts
        restUserPostMockMvc.perform(get("/api/userPosts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(userPost.getId().intValue())))
                .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
                .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE_STR)))
                .andExpect(jsonPath("$.[*].greetCount").value(hasItem(DEFAULT_GREET_COUNT)))
                .andExpect(jsonPath("$.[*].commentsCount").value(hasItem(DEFAULT_COMMENTS_COUNT)))
                .andExpect(jsonPath("$.[*].jid").value(hasItem(DEFAULT_JID.toString())));
    }

    @Test
    @Transactional
    public void getUserPost() throws Exception {
        // Initialize the database
        userPostRepository.saveAndFlush(userPost);

        // Get the userPost
        restUserPostMockMvc.perform(get("/api/userPosts/{id}", userPost.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(userPost.getId().intValue()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE_STR))
            .andExpect(jsonPath("$.greetCount").value(DEFAULT_GREET_COUNT))
            .andExpect(jsonPath("$.commentsCount").value(DEFAULT_COMMENTS_COUNT))
            .andExpect(jsonPath("$.jid").value(DEFAULT_JID.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUserPost() throws Exception {
        // Get the userPost
        restUserPostMockMvc.perform(get("/api/userPosts/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserPost() throws Exception {
        // Initialize the database
        userPostRepository.saveAndFlush(userPost);

		int databaseSizeBeforeUpdate = userPostRepository.findAll().size();

        // Update the userPost
        userPost.setContent(UPDATED_CONTENT);
        userPost.setCreateDate(UPDATED_CREATE_DATE);
        userPost.setGreetCount(UPDATED_GREET_COUNT);
        userPost.setCommentsCount(UPDATED_COMMENTS_COUNT);
        userPost.setJid(UPDATED_JID);
        restUserPostMockMvc.perform(put("/api/userPosts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userPost)))
                .andExpect(status().isOk());

        // Validate the UserPost in the database
        List<UserPost> userPosts = userPostRepository.findAll();
        assertThat(userPosts).hasSize(databaseSizeBeforeUpdate);
        UserPost testUserPost = userPosts.get(userPosts.size() - 1);
        assertThat(testUserPost.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testUserPost.getCreateDate().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testUserPost.getGreetCount()).isEqualTo(UPDATED_GREET_COUNT);
        assertThat(testUserPost.getCommentsCount()).isEqualTo(UPDATED_COMMENTS_COUNT);
        assertThat(testUserPost.getJid()).isEqualTo(UPDATED_JID);
    }

    @Test
    @Transactional
    public void deleteUserPost() throws Exception {
        // Initialize the database
        userPostRepository.saveAndFlush(userPost);

		int databaseSizeBeforeDelete = userPostRepository.findAll().size();

        // Get the userPost
        restUserPostMockMvc.perform(delete("/api/userPosts/{id}", userPost.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<UserPost> userPosts = userPostRepository.findAll();
        assertThat(userPosts).hasSize(databaseSizeBeforeDelete - 1);
    }
}
