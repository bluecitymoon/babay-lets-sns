package com.doubletuan.sns.web.rest;

import com.doubletuan.sns.Application;
import com.doubletuan.sns.domain.PostImage;
import com.doubletuan.sns.repository.PostImageRepository;

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
 * Test class for the PostImageResource REST controller.
 *
 * @see PostImageResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PostImageResourceTest {

    private static final String DEFAULT_SRC = "SAMPLE_TEXT";
    private static final String UPDATED_SRC = "UPDATED_TEXT";
    private static final String DEFAULT_SRC1 = "SAMPLE_TEXT";
    private static final String UPDATED_SRC1 = "UPDATED_TEXT";
    private static final String DEFAULT_SRC2 = "SAMPLE_TEXT";
    private static final String UPDATED_SRC2 = "UPDATED_TEXT";
    private static final String DEFAULT_SRC3 = "SAMPLE_TEXT";
    private static final String UPDATED_SRC3 = "UPDATED_TEXT";

    @Inject
    private PostImageRepository postImageRepository;

    private MockMvc restPostImageMockMvc;

    private PostImage postImage;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PostImageResource postImageResource = new PostImageResource();
        ReflectionTestUtils.setField(postImageResource, "postImageRepository", postImageRepository);
        this.restPostImageMockMvc = MockMvcBuilders.standaloneSetup(postImageResource).build();
    }

    @Before
    public void initTest() {
        postImage = new PostImage();
        postImage.setSrc(DEFAULT_SRC);
        postImage.setSrc1(DEFAULT_SRC1);
        postImage.setSrc2(DEFAULT_SRC2);
        postImage.setSrc3(DEFAULT_SRC3);
    }

    @Test
    @Transactional
    public void createPostImage() throws Exception {
        int databaseSizeBeforeCreate = postImageRepository.findAll().size();

        // Create the PostImage
        restPostImageMockMvc.perform(post("/api/postImages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(postImage)))
                .andExpect(status().isCreated());

        // Validate the PostImage in the database
        List<PostImage> postImages = postImageRepository.findAll();
        assertThat(postImages).hasSize(databaseSizeBeforeCreate + 1);
        PostImage testPostImage = postImages.get(postImages.size() - 1);
        assertThat(testPostImage.getSrc()).isEqualTo(DEFAULT_SRC);
        assertThat(testPostImage.getSrc1()).isEqualTo(DEFAULT_SRC1);
        assertThat(testPostImage.getSrc2()).isEqualTo(DEFAULT_SRC2);
        assertThat(testPostImage.getSrc3()).isEqualTo(DEFAULT_SRC3);
    }

    @Test
    @Transactional
    public void getAllPostImages() throws Exception {
        // Initialize the database
        postImageRepository.saveAndFlush(postImage);

        // Get all the postImages
        restPostImageMockMvc.perform(get("/api/postImages"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(postImage.getId().intValue())))
                .andExpect(jsonPath("$.[*].src").value(hasItem(DEFAULT_SRC.toString())))
                .andExpect(jsonPath("$.[*].src1").value(hasItem(DEFAULT_SRC1.toString())))
                .andExpect(jsonPath("$.[*].src2").value(hasItem(DEFAULT_SRC2.toString())))
                .andExpect(jsonPath("$.[*].src3").value(hasItem(DEFAULT_SRC3.toString())));
    }

    @Test
    @Transactional
    public void getPostImage() throws Exception {
        // Initialize the database
        postImageRepository.saveAndFlush(postImage);

        // Get the postImage
        restPostImageMockMvc.perform(get("/api/postImages/{id}", postImage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(postImage.getId().intValue()))
            .andExpect(jsonPath("$.src").value(DEFAULT_SRC.toString()))
            .andExpect(jsonPath("$.src1").value(DEFAULT_SRC1.toString()))
            .andExpect(jsonPath("$.src2").value(DEFAULT_SRC2.toString()))
            .andExpect(jsonPath("$.src3").value(DEFAULT_SRC3.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPostImage() throws Exception {
        // Get the postImage
        restPostImageMockMvc.perform(get("/api/postImages/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePostImage() throws Exception {
        // Initialize the database
        postImageRepository.saveAndFlush(postImage);

		int databaseSizeBeforeUpdate = postImageRepository.findAll().size();

        // Update the postImage
        postImage.setSrc(UPDATED_SRC);
        postImage.setSrc1(UPDATED_SRC1);
        postImage.setSrc2(UPDATED_SRC2);
        postImage.setSrc3(UPDATED_SRC3);
        restPostImageMockMvc.perform(put("/api/postImages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(postImage)))
                .andExpect(status().isOk());

        // Validate the PostImage in the database
        List<PostImage> postImages = postImageRepository.findAll();
        assertThat(postImages).hasSize(databaseSizeBeforeUpdate);
        PostImage testPostImage = postImages.get(postImages.size() - 1);
        assertThat(testPostImage.getSrc()).isEqualTo(UPDATED_SRC);
        assertThat(testPostImage.getSrc1()).isEqualTo(UPDATED_SRC1);
        assertThat(testPostImage.getSrc2()).isEqualTo(UPDATED_SRC2);
        assertThat(testPostImage.getSrc3()).isEqualTo(UPDATED_SRC3);
    }

    @Test
    @Transactional
    public void deletePostImage() throws Exception {
        // Initialize the database
        postImageRepository.saveAndFlush(postImage);

		int databaseSizeBeforeDelete = postImageRepository.findAll().size();

        // Get the postImage
        restPostImageMockMvc.perform(delete("/api/postImages/{id}", postImage.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<PostImage> postImages = postImageRepository.findAll();
        assertThat(postImages).hasSize(databaseSizeBeforeDelete - 1);
    }
}
