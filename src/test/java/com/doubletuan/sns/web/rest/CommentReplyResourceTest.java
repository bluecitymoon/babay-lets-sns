package com.doubletuan.sns.web.rest;

import com.doubletuan.sns.Application;
import com.doubletuan.sns.domain.CommentReply;
import com.doubletuan.sns.repository.CommentReplyRepository;

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
 * Test class for the CommentReplyResource REST controller.
 *
 * @see CommentReplyResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class CommentReplyResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private static final String DEFAULT_CONTENT = "SAMPLE_TEXT";
    private static final String UPDATED_CONTENT = "UPDATED_TEXT";
    private static final String DEFAULT_JID = "SAMPLE_TEXT";
    private static final String UPDATED_JID = "UPDATED_TEXT";

    private static final DateTime DEFAULT_CREATE_DATE = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_CREATE_DATE = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_CREATE_DATE_STR = dateTimeFormatter.print(DEFAULT_CREATE_DATE);

    @Inject
    private CommentReplyRepository commentReplyRepository;

    private MockMvc restCommentReplyMockMvc;

    private CommentReply commentReply;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CommentReplyResource commentReplyResource = new CommentReplyResource();
        ReflectionTestUtils.setField(commentReplyResource, "commentReplyRepository", commentReplyRepository);
        this.restCommentReplyMockMvc = MockMvcBuilders.standaloneSetup(commentReplyResource).build();
    }

    @Before
    public void initTest() {
        commentReply = new CommentReply();
        commentReply.setContent(DEFAULT_CONTENT);
        commentReply.setJid(DEFAULT_JID);
        commentReply.setCreateDate(DEFAULT_CREATE_DATE);
    }

    @Test
    @Transactional
    public void createCommentReply() throws Exception {
        int databaseSizeBeforeCreate = commentReplyRepository.findAll().size();

        // Create the CommentReply
        restCommentReplyMockMvc.perform(post("/api/commentReplys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(commentReply)))
                .andExpect(status().isCreated());

        // Validate the CommentReply in the database
        List<CommentReply> commentReplys = commentReplyRepository.findAll();
        assertThat(commentReplys).hasSize(databaseSizeBeforeCreate + 1);
        CommentReply testCommentReply = commentReplys.get(commentReplys.size() - 1);
        assertThat(testCommentReply.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testCommentReply.getJid()).isEqualTo(DEFAULT_JID);
        assertThat(testCommentReply.getCreateDate().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_CREATE_DATE);
    }

    @Test
    @Transactional
    public void getAllCommentReplys() throws Exception {
        // Initialize the database
        commentReplyRepository.saveAndFlush(commentReply);

        // Get all the commentReplys
        restCommentReplyMockMvc.perform(get("/api/commentReplys"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(commentReply.getId().intValue())))
                .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
                .andExpect(jsonPath("$.[*].jid").value(hasItem(DEFAULT_JID.toString())))
                .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE_STR)));
    }

    @Test
    @Transactional
    public void getCommentReply() throws Exception {
        // Initialize the database
        commentReplyRepository.saveAndFlush(commentReply);

        // Get the commentReply
        restCommentReplyMockMvc.perform(get("/api/commentReplys/{id}", commentReply.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(commentReply.getId().intValue()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.jid").value(DEFAULT_JID.toString()))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingCommentReply() throws Exception {
        // Get the commentReply
        restCommentReplyMockMvc.perform(get("/api/commentReplys/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCommentReply() throws Exception {
        // Initialize the database
        commentReplyRepository.saveAndFlush(commentReply);

		int databaseSizeBeforeUpdate = commentReplyRepository.findAll().size();

        // Update the commentReply
        commentReply.setContent(UPDATED_CONTENT);
        commentReply.setJid(UPDATED_JID);
        commentReply.setCreateDate(UPDATED_CREATE_DATE);
        restCommentReplyMockMvc.perform(put("/api/commentReplys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(commentReply)))
                .andExpect(status().isOk());

        // Validate the CommentReply in the database
        List<CommentReply> commentReplys = commentReplyRepository.findAll();
        assertThat(commentReplys).hasSize(databaseSizeBeforeUpdate);
        CommentReply testCommentReply = commentReplys.get(commentReplys.size() - 1);
        assertThat(testCommentReply.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testCommentReply.getJid()).isEqualTo(UPDATED_JID);
        assertThat(testCommentReply.getCreateDate().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    public void deleteCommentReply() throws Exception {
        // Initialize the database
        commentReplyRepository.saveAndFlush(commentReply);

		int databaseSizeBeforeDelete = commentReplyRepository.findAll().size();

        // Get the commentReply
        restCommentReplyMockMvc.perform(delete("/api/commentReplys/{id}", commentReply.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<CommentReply> commentReplys = commentReplyRepository.findAll();
        assertThat(commentReplys).hasSize(databaseSizeBeforeDelete - 1);
    }
}
