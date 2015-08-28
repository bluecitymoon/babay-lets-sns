package com.doubletuan.sns.web.rest;

import com.doubletuan.sns.Application;
import com.doubletuan.sns.domain.OpenFireConfiguration;
import com.doubletuan.sns.repository.OpenFireConfigurationRepository;

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
 * Test class for the OpenFireConfigurationResource REST controller.
 *
 * @see OpenFireConfigurationResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class OpenFireConfigurationResourceTest {

    private static final String DEFAULT_AUTHENTICATION_TOKEN = "SAMPLE_TEXT";
    private static final String UPDATED_AUTHENTICATION_TOKEN = "UPDATED_TEXT";
    private static final String DEFAULT_SERVER_ADDRESS = "SAMPLE_TEXT";
    private static final String UPDATED_SERVER_ADDRESS = "UPDATED_TEXT";
    private static final String DEFAULT_REST_API_PORT = "SAMPLE_TEXT";
    private static final String UPDATED_REST_API_PORT = "UPDATED_TEXT";
    private static final String DEFAULT_IDENTIFIER = "SAMPLE_TEXT";
    private static final String UPDATED_IDENTIFIER = "UPDATED_TEXT";

    @Inject
    private OpenFireConfigurationRepository openFireConfigurationRepository;

    private MockMvc restOpenFireConfigurationMockMvc;

    private OpenFireConfiguration openFireConfiguration;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OpenFireConfigurationResource openFireConfigurationResource = new OpenFireConfigurationResource();
        ReflectionTestUtils.setField(openFireConfigurationResource, "openFireConfigurationRepository", openFireConfigurationRepository);
        this.restOpenFireConfigurationMockMvc = MockMvcBuilders.standaloneSetup(openFireConfigurationResource).build();
    }

    @Before
    public void initTest() {
        openFireConfiguration = new OpenFireConfiguration();
        openFireConfiguration.setAuthenticationToken(DEFAULT_AUTHENTICATION_TOKEN);
        openFireConfiguration.setServerAddress(DEFAULT_SERVER_ADDRESS);
        openFireConfiguration.setRestApiPort(DEFAULT_REST_API_PORT);
        openFireConfiguration.setIdentifier(DEFAULT_IDENTIFIER);
    }

    @Test
    @Transactional
    public void createOpenFireConfiguration() throws Exception {
        int databaseSizeBeforeCreate = openFireConfigurationRepository.findAll().size();

        // Create the OpenFireConfiguration
        restOpenFireConfigurationMockMvc.perform(post("/api/openFireConfigurations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(openFireConfiguration)))
                .andExpect(status().isCreated());

        // Validate the OpenFireConfiguration in the database
        List<OpenFireConfiguration> openFireConfigurations = openFireConfigurationRepository.findAll();
        assertThat(openFireConfigurations).hasSize(databaseSizeBeforeCreate + 1);
        OpenFireConfiguration testOpenFireConfiguration = openFireConfigurations.get(openFireConfigurations.size() - 1);
        assertThat(testOpenFireConfiguration.getAuthenticationToken()).isEqualTo(DEFAULT_AUTHENTICATION_TOKEN);
        assertThat(testOpenFireConfiguration.getServerAddress()).isEqualTo(DEFAULT_SERVER_ADDRESS);
        assertThat(testOpenFireConfiguration.getRestApiPort()).isEqualTo(DEFAULT_REST_API_PORT);
        assertThat(testOpenFireConfiguration.getIdentifier()).isEqualTo(DEFAULT_IDENTIFIER);
    }

    @Test
    @Transactional
    public void getAllOpenFireConfigurations() throws Exception {
        // Initialize the database
        openFireConfigurationRepository.saveAndFlush(openFireConfiguration);

        // Get all the openFireConfigurations
        restOpenFireConfigurationMockMvc.perform(get("/api/openFireConfigurations"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(openFireConfiguration.getId().intValue())))
                .andExpect(jsonPath("$.[*].authenticationToken").value(hasItem(DEFAULT_AUTHENTICATION_TOKEN.toString())))
                .andExpect(jsonPath("$.[*].serverAddress").value(hasItem(DEFAULT_SERVER_ADDRESS.toString())))
                .andExpect(jsonPath("$.[*].restApiPort").value(hasItem(DEFAULT_REST_API_PORT.toString())))
                .andExpect(jsonPath("$.[*].identifier").value(hasItem(DEFAULT_IDENTIFIER.toString())));
    }

    @Test
    @Transactional
    public void getOpenFireConfiguration() throws Exception {
        // Initialize the database
        openFireConfigurationRepository.saveAndFlush(openFireConfiguration);

        // Get the openFireConfiguration
        restOpenFireConfigurationMockMvc.perform(get("/api/openFireConfigurations/{id}", openFireConfiguration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(openFireConfiguration.getId().intValue()))
            .andExpect(jsonPath("$.authenticationToken").value(DEFAULT_AUTHENTICATION_TOKEN.toString()))
            .andExpect(jsonPath("$.serverAddress").value(DEFAULT_SERVER_ADDRESS.toString()))
            .andExpect(jsonPath("$.restApiPort").value(DEFAULT_REST_API_PORT.toString()))
            .andExpect(jsonPath("$.identifier").value(DEFAULT_IDENTIFIER.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingOpenFireConfiguration() throws Exception {
        // Get the openFireConfiguration
        restOpenFireConfigurationMockMvc.perform(get("/api/openFireConfigurations/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOpenFireConfiguration() throws Exception {
        // Initialize the database
        openFireConfigurationRepository.saveAndFlush(openFireConfiguration);

		int databaseSizeBeforeUpdate = openFireConfigurationRepository.findAll().size();

        // Update the openFireConfiguration
        openFireConfiguration.setAuthenticationToken(UPDATED_AUTHENTICATION_TOKEN);
        openFireConfiguration.setServerAddress(UPDATED_SERVER_ADDRESS);
        openFireConfiguration.setRestApiPort(UPDATED_REST_API_PORT);
        openFireConfiguration.setIdentifier(UPDATED_IDENTIFIER);
        restOpenFireConfigurationMockMvc.perform(put("/api/openFireConfigurations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(openFireConfiguration)))
                .andExpect(status().isOk());

        // Validate the OpenFireConfiguration in the database
        List<OpenFireConfiguration> openFireConfigurations = openFireConfigurationRepository.findAll();
        assertThat(openFireConfigurations).hasSize(databaseSizeBeforeUpdate);
        OpenFireConfiguration testOpenFireConfiguration = openFireConfigurations.get(openFireConfigurations.size() - 1);
        assertThat(testOpenFireConfiguration.getAuthenticationToken()).isEqualTo(UPDATED_AUTHENTICATION_TOKEN);
        assertThat(testOpenFireConfiguration.getServerAddress()).isEqualTo(UPDATED_SERVER_ADDRESS);
        assertThat(testOpenFireConfiguration.getRestApiPort()).isEqualTo(UPDATED_REST_API_PORT);
        assertThat(testOpenFireConfiguration.getIdentifier()).isEqualTo(UPDATED_IDENTIFIER);
    }

    @Test
    @Transactional
    public void deleteOpenFireConfiguration() throws Exception {
        // Initialize the database
        openFireConfigurationRepository.saveAndFlush(openFireConfiguration);

		int databaseSizeBeforeDelete = openFireConfigurationRepository.findAll().size();

        // Get the openFireConfiguration
        restOpenFireConfigurationMockMvc.perform(delete("/api/openFireConfigurations/{id}", openFireConfiguration.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<OpenFireConfiguration> openFireConfigurations = openFireConfigurationRepository.findAll();
        assertThat(openFireConfigurations).hasSize(databaseSizeBeforeDelete - 1);
    }
}
