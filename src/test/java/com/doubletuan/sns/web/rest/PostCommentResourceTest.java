package com.doubletuan.sns.web.rest;

import com.doubletuan.sns.Application;
import com.doubletuan.sns.domain.PostComment;
import com.doubletuan.sns.repository.PostCommentRepository;

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
 * Test class for the PostCommentResource REST controller.
 *
 * @see PostCommentResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PostCommentResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private static final String DEFAULT_CONTENT = "SAMPLE_TEXT";
    private static final String UPDATED_CONTENT = "UPDATED_TEXT";

    private static final DateTime DEFAULT_CREATE_DATE = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_CREATE_DATE = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_CREATE_DATE_STR = dateTimeFormatter.print(DEFAULT_CREATE_DATE);
    private static final String DEFAULT_TYPE = "SAMPLE_TEXT";
    private static final String UPDATED_TYPE = "UPDATED_TEXT";
    private static final String DEFAULT_JID = "SAMPLE_TEXT";
    private static final String UPDATED_JID = "UPDATED_TEXT";

    @Inject
    private PostCommentRepository postCommentRepository;

    private MockMvc restPostCommentMockMvc;

    private PostComment postComment;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PostCommentResource postCommentResource = new PostCommentResource();
        ReflectionTestUtils.setField(postCommentResource, "postCommentRepository", postCommentRepository);
        this.restPostCommentMockMvc = MockMvcBuilders.standaloneSetup(postCommentResource).build();
    }

    @Before
    public void initTest() {
        postComment = new PostComment();
        postComment.setContent(DEFAULT_CONTENT);
        postComment.setCreateDate(DEFAULT_CREATE_DATE);
        postComment.setType(DEFAULT_TYPE);
        postComment.setJid(DEFAULT_JID);
    }

    @Test
    @Transactional
    public void createPostComment() throws Exception {
        int databaseSizeBeforeCreate = postCommentRepository.findAll().size();

        // Create the PostComment
        restPostCommentMockMvc.perform(post("/api/postComments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(postComment)))
                .andExpect(status().isCreated());

        // Validate the PostComment in the database
        List<PostComment> postComments = postCommentRepository.findAll();
        assertThat(postComments).hasSize(databaseSizeBeforeCreate + 1);
        PostComment testPostComment = postComments.get(postComments.size() - 1);
        assertThat(testPostComment.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testPostComment.getCreateDate().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testPostComment.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testPostComment.getJid()).isEqualTo(DEFAULT_JID);
    }

    @Test
    @Transactional
    public void getAllPostComments() throws Exception {
        // Initialize the database
        postCommentRepository.saveAndFlush(postComment);

        // Get all the postComments
        restPostCommentMockMvc.perform(get("/api/postComments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(postComment.getId().intValue())))
                .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
                .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE_STR)))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
                .andExpect(jsonPath("$.[*].jid").value(hasItem(DEFAULT_JID.toString())));
    }

    @Test
    @Transactional
    public void getPostComment() throws Exception {
        // Initialize the database
        postCommentRepository.saveAndFlush(postComment);

        // Get the postComment
        restPostCommentMockMvc.perform(get("/api/postComments/{id}", postComment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(postComment.getId().intValue()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE_STR))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.jid").value(DEFAULT_JID.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPostComment() throws Exception {
        // Get the postComment
        restPostCommentMockMvc.perform(get("/api/postComments/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePostComment() throws Exception {
        // Initialize the database
        postCommentRepository.saveAndFlush(postComment);

		int databaseSizeBeforeUpdate = postCommentRepository.findAll().size();

        // Update the postComment
        postComment.setContent(UPDATED_CONTENT);
        postComment.setCreateDate(UPDATED_CREATE_DATE);
        postComment.setType(UPDATED_TYPE);
        postComment.setJid(UPDATED_JID);
        restPostCommentMockMvc.perform(put("/api/postComments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(postComment)))
                .andExpect(status().isOk());

        // Validate the PostComment in the database
        List<PostComment> postComments = postCommentRepository.findAll();
        assertThat(postComments).hasSize(databaseSizeBeforeUpdate);
        PostComment testPostComment = postComments.get(postComments.size() - 1);
        assertThat(testPostComment.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testPostComment.getCreateDate().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testPostComment.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testPostComment.getJid()).isEqualTo(UPDATED_JID);
    }

    @Test
    @Transactional
    public void deletePostComment() throws Exception {
        // Initialize the database
        postCommentRepository.saveAndFlush(postComment);

		int databaseSizeBeforeDelete = postCommentRepository.findAll().size();

        // Get the postComment
        restPostCommentMockMvc.perform(delete("/api/postComments/{id}", postComment.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<PostComment> postComments = postCommentRepository.findAll();
        assertThat(postComments).hasSize(databaseSizeBeforeDelete - 1);
    }
}
