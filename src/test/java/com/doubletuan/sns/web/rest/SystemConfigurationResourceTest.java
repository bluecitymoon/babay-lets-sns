package com.doubletuan.sns.web.rest;

import com.doubletuan.sns.Application;
import com.doubletuan.sns.domain.SystemConfiguration;
import com.doubletuan.sns.repository.SystemConfigurationRepository;

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
 * Test class for the SystemConfigurationResource REST controller.
 *
 * @see SystemConfigurationResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class SystemConfigurationResourceTest {

    private static final String DEFAULT_IDENTITY = "SAMPLE_TEXT";
    private static final String UPDATED_IDENTITY = "UPDATED_TEXT";
    private static final String DEFAULT_CURRENT_VALUE = "SAMPLE_TEXT";
    private static final String UPDATED_CURRENT_VALUE = "UPDATED_TEXT";
    private static final String DEFAULT_COLUMN1 = "SAMPLE_TEXT";
    private static final String UPDATED_COLUMN1 = "UPDATED_TEXT";
    private static final String DEFAULT_VALUE1 = "SAMPLE_TEXT";
    private static final String UPDATED_VALUE1 = "UPDATED_TEXT";
    private static final String DEFAULT_COLUMN2 = "SAMPLE_TEXT";
    private static final String UPDATED_COLUMN2 = "UPDATED_TEXT";
    private static final String DEFAULT_VALUE2 = "SAMPLE_TEXT";
    private static final String UPDATED_VALUE2 = "UPDATED_TEXT";
    private static final String DEFAULT_COLUMN3 = "SAMPLE_TEXT";
    private static final String UPDATED_COLUMN3 = "UPDATED_TEXT";
    private static final String DEFAULT_VALUE3 = "SAMPLE_TEXT";
    private static final String UPDATED_VALUE3 = "UPDATED_TEXT";

    @Inject
    private SystemConfigurationRepository systemConfigurationRepository;

    private MockMvc restSystemConfigurationMockMvc;

    private SystemConfiguration systemConfiguration;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SystemConfigurationResource systemConfigurationResource = new SystemConfigurationResource();
        ReflectionTestUtils.setField(systemConfigurationResource, "systemConfigurationRepository", systemConfigurationRepository);
        this.restSystemConfigurationMockMvc = MockMvcBuilders.standaloneSetup(systemConfigurationResource).build();
    }

    @Before
    public void initTest() {
        systemConfiguration = new SystemConfiguration();
        systemConfiguration.setIdentity(DEFAULT_IDENTITY);
        systemConfiguration.setCurrentValue(DEFAULT_CURRENT_VALUE);
        systemConfiguration.setColumn1(DEFAULT_COLUMN1);
        systemConfiguration.setValue1(DEFAULT_VALUE1);
        systemConfiguration.setColumn2(DEFAULT_COLUMN2);
        systemConfiguration.setValue2(DEFAULT_VALUE2);
        systemConfiguration.setColumn3(DEFAULT_COLUMN3);
        systemConfiguration.setValue3(DEFAULT_VALUE3);
    }

    @Test
    @Transactional
    public void createSystemConfiguration() throws Exception {
        int databaseSizeBeforeCreate = systemConfigurationRepository.findAll().size();

        // Create the SystemConfiguration
        restSystemConfigurationMockMvc.perform(post("/api/systemConfigurations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(systemConfiguration)))
                .andExpect(status().isCreated());

        // Validate the SystemConfiguration in the database
        List<SystemConfiguration> systemConfigurations = systemConfigurationRepository.findAll();
        assertThat(systemConfigurations).hasSize(databaseSizeBeforeCreate + 1);
        SystemConfiguration testSystemConfiguration = systemConfigurations.get(systemConfigurations.size() - 1);
        assertThat(testSystemConfiguration.getIdentity()).isEqualTo(DEFAULT_IDENTITY);
        assertThat(testSystemConfiguration.getCurrentValue()).isEqualTo(DEFAULT_CURRENT_VALUE);
        assertThat(testSystemConfiguration.getColumn1()).isEqualTo(DEFAULT_COLUMN1);
        assertThat(testSystemConfiguration.getValue1()).isEqualTo(DEFAULT_VALUE1);
        assertThat(testSystemConfiguration.getColumn2()).isEqualTo(DEFAULT_COLUMN2);
        assertThat(testSystemConfiguration.getValue2()).isEqualTo(DEFAULT_VALUE2);
        assertThat(testSystemConfiguration.getColumn3()).isEqualTo(DEFAULT_COLUMN3);
        assertThat(testSystemConfiguration.getValue3()).isEqualTo(DEFAULT_VALUE3);
    }

    @Test
    @Transactional
    public void getAllSystemConfigurations() throws Exception {
        // Initialize the database
        systemConfigurationRepository.saveAndFlush(systemConfiguration);

        // Get all the systemConfigurations
        restSystemConfigurationMockMvc.perform(get("/api/systemConfigurations"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(systemConfiguration.getId().intValue())))
                .andExpect(jsonPath("$.[*].identity").value(hasItem(DEFAULT_IDENTITY.toString())))
                .andExpect(jsonPath("$.[*].currentValue").value(hasItem(DEFAULT_CURRENT_VALUE.toString())))
                .andExpect(jsonPath("$.[*].column1").value(hasItem(DEFAULT_COLUMN1.toString())))
                .andExpect(jsonPath("$.[*].value1").value(hasItem(DEFAULT_VALUE1.toString())))
                .andExpect(jsonPath("$.[*].column2").value(hasItem(DEFAULT_COLUMN2.toString())))
                .andExpect(jsonPath("$.[*].value2").value(hasItem(DEFAULT_VALUE2.toString())))
                .andExpect(jsonPath("$.[*].column3").value(hasItem(DEFAULT_COLUMN3.toString())))
                .andExpect(jsonPath("$.[*].value3").value(hasItem(DEFAULT_VALUE3.toString())));
    }

    @Test
    @Transactional
    public void getSystemConfiguration() throws Exception {
        // Initialize the database
        systemConfigurationRepository.saveAndFlush(systemConfiguration);

        // Get the systemConfiguration
        restSystemConfigurationMockMvc.perform(get("/api/systemConfigurations/{id}", systemConfiguration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(systemConfiguration.getId().intValue()))
            .andExpect(jsonPath("$.identity").value(DEFAULT_IDENTITY.toString()))
            .andExpect(jsonPath("$.currentValue").value(DEFAULT_CURRENT_VALUE.toString()))
            .andExpect(jsonPath("$.column1").value(DEFAULT_COLUMN1.toString()))
            .andExpect(jsonPath("$.value1").value(DEFAULT_VALUE1.toString()))
            .andExpect(jsonPath("$.column2").value(DEFAULT_COLUMN2.toString()))
            .andExpect(jsonPath("$.value2").value(DEFAULT_VALUE2.toString()))
            .andExpect(jsonPath("$.column3").value(DEFAULT_COLUMN3.toString()))
            .andExpect(jsonPath("$.value3").value(DEFAULT_VALUE3.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSystemConfiguration() throws Exception {
        // Get the systemConfiguration
        restSystemConfigurationMockMvc.perform(get("/api/systemConfigurations/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSystemConfiguration() throws Exception {
        // Initialize the database
        systemConfigurationRepository.saveAndFlush(systemConfiguration);

		int databaseSizeBeforeUpdate = systemConfigurationRepository.findAll().size();

        // Update the systemConfiguration
        systemConfiguration.setIdentity(UPDATED_IDENTITY);
        systemConfiguration.setCurrentValue(UPDATED_CURRENT_VALUE);
        systemConfiguration.setColumn1(UPDATED_COLUMN1);
        systemConfiguration.setValue1(UPDATED_VALUE1);
        systemConfiguration.setColumn2(UPDATED_COLUMN2);
        systemConfiguration.setValue2(UPDATED_VALUE2);
        systemConfiguration.setColumn3(UPDATED_COLUMN3);
        systemConfiguration.setValue3(UPDATED_VALUE3);
        restSystemConfigurationMockMvc.perform(put("/api/systemConfigurations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(systemConfiguration)))
                .andExpect(status().isOk());

        // Validate the SystemConfiguration in the database
        List<SystemConfiguration> systemConfigurations = systemConfigurationRepository.findAll();
        assertThat(systemConfigurations).hasSize(databaseSizeBeforeUpdate);
        SystemConfiguration testSystemConfiguration = systemConfigurations.get(systemConfigurations.size() - 1);
        assertThat(testSystemConfiguration.getIdentity()).isEqualTo(UPDATED_IDENTITY);
        assertThat(testSystemConfiguration.getCurrentValue()).isEqualTo(UPDATED_CURRENT_VALUE);
        assertThat(testSystemConfiguration.getColumn1()).isEqualTo(UPDATED_COLUMN1);
        assertThat(testSystemConfiguration.getValue1()).isEqualTo(UPDATED_VALUE1);
        assertThat(testSystemConfiguration.getColumn2()).isEqualTo(UPDATED_COLUMN2);
        assertThat(testSystemConfiguration.getValue2()).isEqualTo(UPDATED_VALUE2);
        assertThat(testSystemConfiguration.getColumn3()).isEqualTo(UPDATED_COLUMN3);
        assertThat(testSystemConfiguration.getValue3()).isEqualTo(UPDATED_VALUE3);
    }

    @Test
    @Transactional
    public void deleteSystemConfiguration() throws Exception {
        // Initialize the database
        systemConfigurationRepository.saveAndFlush(systemConfiguration);

		int databaseSizeBeforeDelete = systemConfigurationRepository.findAll().size();

        // Get the systemConfiguration
        restSystemConfigurationMockMvc.perform(delete("/api/systemConfigurations/{id}", systemConfiguration.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<SystemConfiguration> systemConfigurations = systemConfigurationRepository.findAll();
        assertThat(systemConfigurations).hasSize(databaseSizeBeforeDelete - 1);
    }
}
