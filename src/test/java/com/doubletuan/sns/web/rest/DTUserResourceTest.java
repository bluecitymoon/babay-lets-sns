package com.doubletuan.sns.web.rest;

import com.doubletuan.sns.Application;
import com.doubletuan.sns.domain.DTUser;
import com.doubletuan.sns.repository.DTUserRepository;

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
 * Test class for the DTUserResource REST controller.
 *
 * @see DTUserResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class DTUserResourceTest {

    private static final String DEFAULT_USERNAME = "SAMPLE_TEXT";
    private static final String UPDATED_USERNAME = "UPDATED_TEXT";
    private static final String DEFAULT_PASSWORD = "SAMPLE_TEXT";
    private static final String UPDATED_PASSWORD = "UPDATED_TEXT";
    private static final String DEFAULT_AVATAR = "SAMPLE_TEXT";
    private static final String UPDATED_AVATAR = "UPDATED_TEXT";
    private static final String DEFAULT_PHONE = "SAMPLE_TEXT";
    private static final String UPDATED_PHONE = "UPDATED_TEXT";
    private static final String DEFAULT_SIGN = "SAMPLE_TEXT";
    private static final String UPDATED_SIGN = "UPDATED_TEXT";
    private static final String DEFAULT_BIRTHDAY = "SAMPLE_TEXT";
    private static final String UPDATED_BIRTHDAY = "UPDATED_TEXT";

    @Inject
    private DTUserRepository dTUserRepository;

    private MockMvc restDTUserMockMvc;

    private DTUser dTUser;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DTUserResource dTUserResource = new DTUserResource();
        ReflectionTestUtils.setField(dTUserResource, "dTUserRepository", dTUserRepository);
        this.restDTUserMockMvc = MockMvcBuilders.standaloneSetup(dTUserResource).build();
    }

    @Before
    public void initTest() {
        dTUser = new DTUser();
        dTUser.setUsername(DEFAULT_USERNAME);
        dTUser.setPassword(DEFAULT_PASSWORD);
        dTUser.setAvatar(DEFAULT_AVATAR);
        dTUser.setPhone(DEFAULT_PHONE);
        dTUser.setSign(DEFAULT_SIGN);
        dTUser.setBirthday(DEFAULT_BIRTHDAY);
    }

    @Test
    @Transactional
    public void createDTUser() throws Exception {
        int databaseSizeBeforeCreate = dTUserRepository.findAll().size();

        // Create the DTUser
        restDTUserMockMvc.perform(post("/api/dTUsers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dTUser)))
                .andExpect(status().isCreated());

        // Validate the DTUser in the database
        List<DTUser> dTUsers = dTUserRepository.findAll();
        assertThat(dTUsers).hasSize(databaseSizeBeforeCreate + 1);
        DTUser testDTUser = dTUsers.get(dTUsers.size() - 1);
        assertThat(testDTUser.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testDTUser.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testDTUser.getAvatar()).isEqualTo(DEFAULT_AVATAR);
        assertThat(testDTUser.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testDTUser.getSign()).isEqualTo(DEFAULT_SIGN);
        assertThat(testDTUser.getBirthday()).isEqualTo(DEFAULT_BIRTHDAY);
    }

    @Test
    @Transactional
    public void getAllDTUsers() throws Exception {
        // Initialize the database
        dTUserRepository.saveAndFlush(dTUser);

        // Get all the dTUsers
        restDTUserMockMvc.perform(get("/api/dTUsers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(dTUser.getId().intValue())))
                .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME.toString())))
                .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD.toString())))
                .andExpect(jsonPath("$.[*].avatar").value(hasItem(DEFAULT_AVATAR.toString())))
                .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
                .andExpect(jsonPath("$.[*].sign").value(hasItem(DEFAULT_SIGN.toString())))
                .andExpect(jsonPath("$.[*].birthday").value(hasItem(DEFAULT_BIRTHDAY.toString())));
    }

    @Test
    @Transactional
    public void getDTUser() throws Exception {
        // Initialize the database
        dTUserRepository.saveAndFlush(dTUser);

        // Get the dTUser
        restDTUserMockMvc.perform(get("/api/dTUsers/{id}", dTUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(dTUser.getId().intValue()))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME.toString()))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD.toString()))
            .andExpect(jsonPath("$.avatar").value(DEFAULT_AVATAR.toString()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE.toString()))
            .andExpect(jsonPath("$.sign").value(DEFAULT_SIGN.toString()))
            .andExpect(jsonPath("$.birthday").value(DEFAULT_BIRTHDAY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDTUser() throws Exception {
        // Get the dTUser
        restDTUserMockMvc.perform(get("/api/dTUsers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDTUser() throws Exception {
        // Initialize the database
        dTUserRepository.saveAndFlush(dTUser);

		int databaseSizeBeforeUpdate = dTUserRepository.findAll().size();

        // Update the dTUser
        dTUser.setUsername(UPDATED_USERNAME);
        dTUser.setPassword(UPDATED_PASSWORD);
        dTUser.setAvatar(UPDATED_AVATAR);
        dTUser.setPhone(UPDATED_PHONE);
        dTUser.setSign(UPDATED_SIGN);
        dTUser.setBirthday(UPDATED_BIRTHDAY);
        restDTUserMockMvc.perform(put("/api/dTUsers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dTUser)))
                .andExpect(status().isOk());

        // Validate the DTUser in the database
        List<DTUser> dTUsers = dTUserRepository.findAll();
        assertThat(dTUsers).hasSize(databaseSizeBeforeUpdate);
        DTUser testDTUser = dTUsers.get(dTUsers.size() - 1);
        assertThat(testDTUser.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testDTUser.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testDTUser.getAvatar()).isEqualTo(UPDATED_AVATAR);
        assertThat(testDTUser.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testDTUser.getSign()).isEqualTo(UPDATED_SIGN);
        assertThat(testDTUser.getBirthday()).isEqualTo(UPDATED_BIRTHDAY);
    }

    @Test
    @Transactional
    public void deleteDTUser() throws Exception {
        // Initialize the database
        dTUserRepository.saveAndFlush(dTUser);

		int databaseSizeBeforeDelete = dTUserRepository.findAll().size();

        // Get the dTUser
        restDTUserMockMvc.perform(delete("/api/dTUsers/{id}", dTUser.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<DTUser> dTUsers = dTUserRepository.findAll();
        assertThat(dTUsers).hasSize(databaseSizeBeforeDelete - 1);
    }
}
