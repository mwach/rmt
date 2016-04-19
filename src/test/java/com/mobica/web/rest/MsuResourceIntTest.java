package com.mobica.web.rest;

import com.mobica.Application;
import com.mobica.domain.Msu;
import com.mobica.repository.MsuRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
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
 * Test class for the MsuResource REST controller.
 *
 * @see MsuResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class MsuResourceIntTest {

    private static final String DEFAULT_CODE = "AAA";
    private static final String UPDATED_CODE = "BBB";
    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private MsuRepository msuRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restMsuMockMvc;

    private Msu msu;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MsuResource msuResource = new MsuResource();
        ReflectionTestUtils.setField(msuResource, "msuRepository", msuRepository);
        this.restMsuMockMvc = MockMvcBuilders.standaloneSetup(msuResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        msu = new Msu();
        msu.setCode(DEFAULT_CODE);
        msu.setName(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createMsu() throws Exception {
        int databaseSizeBeforeCreate = msuRepository.findAll().size();

        // Create the Msu

        restMsuMockMvc.perform(post("/api/msus")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(msu)))
                .andExpect(status().isCreated());

        // Validate the Msu in the database
        List<Msu> msus = msuRepository.findAll();
        assertThat(msus).hasSize(databaseSizeBeforeCreate + 1);
        Msu testMsu = msus.get(msus.size() - 1);
        assertThat(testMsu.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testMsu.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = msuRepository.findAll().size();
        // set the field null
        msu.setCode(null);

        // Create the Msu, which fails.

        restMsuMockMvc.perform(post("/api/msus")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(msu)))
                .andExpect(status().isBadRequest());

        List<Msu> msus = msuRepository.findAll();
        assertThat(msus).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = msuRepository.findAll().size();
        // set the field null
        msu.setName(null);

        // Create the Msu, which fails.

        restMsuMockMvc.perform(post("/api/msus")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(msu)))
                .andExpect(status().isBadRequest());

        List<Msu> msus = msuRepository.findAll();
        assertThat(msus).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMsus() throws Exception {
        // Initialize the database
        msuRepository.saveAndFlush(msu);

        // Get all the msus
        restMsuMockMvc.perform(get("/api/msus?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(msu.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getMsu() throws Exception {
        // Initialize the database
        msuRepository.saveAndFlush(msu);

        // Get the msu
        restMsuMockMvc.perform(get("/api/msus/{id}", msu.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(msu.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMsu() throws Exception {
        // Get the msu
        restMsuMockMvc.perform(get("/api/msus/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMsu() throws Exception {
        // Initialize the database
        msuRepository.saveAndFlush(msu);

		int databaseSizeBeforeUpdate = msuRepository.findAll().size();

        // Update the msu
        msu.setCode(UPDATED_CODE);
        msu.setName(UPDATED_NAME);

        restMsuMockMvc.perform(put("/api/msus")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(msu)))
                .andExpect(status().isOk());

        // Validate the Msu in the database
        List<Msu> msus = msuRepository.findAll();
        assertThat(msus).hasSize(databaseSizeBeforeUpdate);
        Msu testMsu = msus.get(msus.size() - 1);
        assertThat(testMsu.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testMsu.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteMsu() throws Exception {
        // Initialize the database
        msuRepository.saveAndFlush(msu);

		int databaseSizeBeforeDelete = msuRepository.findAll().size();

        // Get the msu
        restMsuMockMvc.perform(delete("/api/msus/{id}", msu.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Msu> msus = msuRepository.findAll();
        assertThat(msus).hasSize(databaseSizeBeforeDelete - 1);
    }
}
