package com.doclajotwellnesstrackerbackend.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.doclajotwellnesstrackerbackend.app.IntegrationTest;
import com.doclajotwellnesstrackerbackend.app.domain.HealthConcern;
import com.doclajotwellnesstrackerbackend.app.domain.VitalSign;
import com.doclajotwellnesstrackerbackend.app.repository.HealthConcernRepository;
import com.doclajotwellnesstrackerbackend.app.service.criteria.HealthConcernCriteria;
import com.doclajotwellnesstrackerbackend.app.service.dto.HealthConcernDTO;
import com.doclajotwellnesstrackerbackend.app.service.mapper.HealthConcernMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link HealthConcernResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HealthConcernResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/health-concerns";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HealthConcernRepository healthConcernRepository;

    @Autowired
    private HealthConcernMapper healthConcernMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHealthConcernMockMvc;

    private HealthConcern healthConcern;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HealthConcern createEntity(EntityManager em) {
        HealthConcern healthConcern = new HealthConcern().name(DEFAULT_NAME);
        return healthConcern;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HealthConcern createUpdatedEntity(EntityManager em) {
        HealthConcern healthConcern = new HealthConcern().name(UPDATED_NAME);
        return healthConcern;
    }

    @BeforeEach
    public void initTest() {
        healthConcern = createEntity(em);
    }

    @Test
    @Transactional
    void createHealthConcern() throws Exception {
        int databaseSizeBeforeCreate = healthConcernRepository.findAll().size();
        // Create the HealthConcern
        HealthConcernDTO healthConcernDTO = healthConcernMapper.toDto(healthConcern);
        restHealthConcernMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(healthConcernDTO))
            )
            .andExpect(status().isCreated());

        // Validate the HealthConcern in the database
        List<HealthConcern> healthConcernList = healthConcernRepository.findAll();
        assertThat(healthConcernList).hasSize(databaseSizeBeforeCreate + 1);
        HealthConcern testHealthConcern = healthConcernList.get(healthConcernList.size() - 1);
        assertThat(testHealthConcern.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createHealthConcernWithExistingId() throws Exception {
        // Create the HealthConcern with an existing ID
        healthConcern.setId(1L);
        HealthConcernDTO healthConcernDTO = healthConcernMapper.toDto(healthConcern);

        int databaseSizeBeforeCreate = healthConcernRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHealthConcernMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(healthConcernDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HealthConcern in the database
        List<HealthConcern> healthConcernList = healthConcernRepository.findAll();
        assertThat(healthConcernList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = healthConcernRepository.findAll().size();
        // set the field null
        healthConcern.setName(null);

        // Create the HealthConcern, which fails.
        HealthConcernDTO healthConcernDTO = healthConcernMapper.toDto(healthConcern);

        restHealthConcernMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(healthConcernDTO))
            )
            .andExpect(status().isBadRequest());

        List<HealthConcern> healthConcernList = healthConcernRepository.findAll();
        assertThat(healthConcernList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHealthConcerns() throws Exception {
        // Initialize the database
        healthConcernRepository.saveAndFlush(healthConcern);

        // Get all the healthConcernList
        restHealthConcernMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(healthConcern.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getHealthConcern() throws Exception {
        // Initialize the database
        healthConcernRepository.saveAndFlush(healthConcern);

        // Get the healthConcern
        restHealthConcernMockMvc
            .perform(get(ENTITY_API_URL_ID, healthConcern.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(healthConcern.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getHealthConcernsByIdFiltering() throws Exception {
        // Initialize the database
        healthConcernRepository.saveAndFlush(healthConcern);

        Long id = healthConcern.getId();

        defaultHealthConcernShouldBeFound("id.equals=" + id);
        defaultHealthConcernShouldNotBeFound("id.notEquals=" + id);

        defaultHealthConcernShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultHealthConcernShouldNotBeFound("id.greaterThan=" + id);

        defaultHealthConcernShouldBeFound("id.lessThanOrEqual=" + id);
        defaultHealthConcernShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllHealthConcernsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        healthConcernRepository.saveAndFlush(healthConcern);

        // Get all the healthConcernList where name equals to DEFAULT_NAME
        defaultHealthConcernShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the healthConcernList where name equals to UPDATED_NAME
        defaultHealthConcernShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllHealthConcernsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        healthConcernRepository.saveAndFlush(healthConcern);

        // Get all the healthConcernList where name not equals to DEFAULT_NAME
        defaultHealthConcernShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the healthConcernList where name not equals to UPDATED_NAME
        defaultHealthConcernShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllHealthConcernsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        healthConcernRepository.saveAndFlush(healthConcern);

        // Get all the healthConcernList where name in DEFAULT_NAME or UPDATED_NAME
        defaultHealthConcernShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the healthConcernList where name equals to UPDATED_NAME
        defaultHealthConcernShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllHealthConcernsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        healthConcernRepository.saveAndFlush(healthConcern);

        // Get all the healthConcernList where name is not null
        defaultHealthConcernShouldBeFound("name.specified=true");

        // Get all the healthConcernList where name is null
        defaultHealthConcernShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllHealthConcernsByNameContainsSomething() throws Exception {
        // Initialize the database
        healthConcernRepository.saveAndFlush(healthConcern);

        // Get all the healthConcernList where name contains DEFAULT_NAME
        defaultHealthConcernShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the healthConcernList where name contains UPDATED_NAME
        defaultHealthConcernShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllHealthConcernsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        healthConcernRepository.saveAndFlush(healthConcern);

        // Get all the healthConcernList where name does not contain DEFAULT_NAME
        defaultHealthConcernShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the healthConcernList where name does not contain UPDATED_NAME
        defaultHealthConcernShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllHealthConcernsByVitalSignIsEqualToSomething() throws Exception {
        // Initialize the database
        healthConcernRepository.saveAndFlush(healthConcern);
        VitalSign vitalSign;
        if (TestUtil.findAll(em, VitalSign.class).isEmpty()) {
            vitalSign = VitalSignResourceIT.createEntity(em);
            em.persist(vitalSign);
            em.flush();
        } else {
            vitalSign = TestUtil.findAll(em, VitalSign.class).get(0);
        }
        em.persist(vitalSign);
        em.flush();
        healthConcern.addVitalSign(vitalSign);
        healthConcernRepository.saveAndFlush(healthConcern);
        Long vitalSignId = vitalSign.getId();

        // Get all the healthConcernList where vitalSign equals to vitalSignId
        defaultHealthConcernShouldBeFound("vitalSignId.equals=" + vitalSignId);

        // Get all the healthConcernList where vitalSign equals to (vitalSignId + 1)
        defaultHealthConcernShouldNotBeFound("vitalSignId.equals=" + (vitalSignId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultHealthConcernShouldBeFound(String filter) throws Exception {
        restHealthConcernMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(healthConcern.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restHealthConcernMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultHealthConcernShouldNotBeFound(String filter) throws Exception {
        restHealthConcernMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHealthConcernMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingHealthConcern() throws Exception {
        // Get the healthConcern
        restHealthConcernMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewHealthConcern() throws Exception {
        // Initialize the database
        healthConcernRepository.saveAndFlush(healthConcern);

        int databaseSizeBeforeUpdate = healthConcernRepository.findAll().size();

        // Update the healthConcern
        HealthConcern updatedHealthConcern = healthConcernRepository.findById(healthConcern.getId()).get();
        // Disconnect from session so that the updates on updatedHealthConcern are not directly saved in db
        em.detach(updatedHealthConcern);
        updatedHealthConcern.name(UPDATED_NAME);
        HealthConcernDTO healthConcernDTO = healthConcernMapper.toDto(updatedHealthConcern);

        restHealthConcernMockMvc
            .perform(
                put(ENTITY_API_URL_ID, healthConcernDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(healthConcernDTO))
            )
            .andExpect(status().isOk());

        // Validate the HealthConcern in the database
        List<HealthConcern> healthConcernList = healthConcernRepository.findAll();
        assertThat(healthConcernList).hasSize(databaseSizeBeforeUpdate);
        HealthConcern testHealthConcern = healthConcernList.get(healthConcernList.size() - 1);
        assertThat(testHealthConcern.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingHealthConcern() throws Exception {
        int databaseSizeBeforeUpdate = healthConcernRepository.findAll().size();
        healthConcern.setId(count.incrementAndGet());

        // Create the HealthConcern
        HealthConcernDTO healthConcernDTO = healthConcernMapper.toDto(healthConcern);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHealthConcernMockMvc
            .perform(
                put(ENTITY_API_URL_ID, healthConcernDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(healthConcernDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HealthConcern in the database
        List<HealthConcern> healthConcernList = healthConcernRepository.findAll();
        assertThat(healthConcernList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHealthConcern() throws Exception {
        int databaseSizeBeforeUpdate = healthConcernRepository.findAll().size();
        healthConcern.setId(count.incrementAndGet());

        // Create the HealthConcern
        HealthConcernDTO healthConcernDTO = healthConcernMapper.toDto(healthConcern);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHealthConcernMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(healthConcernDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HealthConcern in the database
        List<HealthConcern> healthConcernList = healthConcernRepository.findAll();
        assertThat(healthConcernList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHealthConcern() throws Exception {
        int databaseSizeBeforeUpdate = healthConcernRepository.findAll().size();
        healthConcern.setId(count.incrementAndGet());

        // Create the HealthConcern
        HealthConcernDTO healthConcernDTO = healthConcernMapper.toDto(healthConcern);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHealthConcernMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(healthConcernDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HealthConcern in the database
        List<HealthConcern> healthConcernList = healthConcernRepository.findAll();
        assertThat(healthConcernList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHealthConcernWithPatch() throws Exception {
        // Initialize the database
        healthConcernRepository.saveAndFlush(healthConcern);

        int databaseSizeBeforeUpdate = healthConcernRepository.findAll().size();

        // Update the healthConcern using partial update
        HealthConcern partialUpdatedHealthConcern = new HealthConcern();
        partialUpdatedHealthConcern.setId(healthConcern.getId());

        restHealthConcernMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHealthConcern.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHealthConcern))
            )
            .andExpect(status().isOk());

        // Validate the HealthConcern in the database
        List<HealthConcern> healthConcernList = healthConcernRepository.findAll();
        assertThat(healthConcernList).hasSize(databaseSizeBeforeUpdate);
        HealthConcern testHealthConcern = healthConcernList.get(healthConcernList.size() - 1);
        assertThat(testHealthConcern.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateHealthConcernWithPatch() throws Exception {
        // Initialize the database
        healthConcernRepository.saveAndFlush(healthConcern);

        int databaseSizeBeforeUpdate = healthConcernRepository.findAll().size();

        // Update the healthConcern using partial update
        HealthConcern partialUpdatedHealthConcern = new HealthConcern();
        partialUpdatedHealthConcern.setId(healthConcern.getId());

        partialUpdatedHealthConcern.name(UPDATED_NAME);

        restHealthConcernMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHealthConcern.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHealthConcern))
            )
            .andExpect(status().isOk());

        // Validate the HealthConcern in the database
        List<HealthConcern> healthConcernList = healthConcernRepository.findAll();
        assertThat(healthConcernList).hasSize(databaseSizeBeforeUpdate);
        HealthConcern testHealthConcern = healthConcernList.get(healthConcernList.size() - 1);
        assertThat(testHealthConcern.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingHealthConcern() throws Exception {
        int databaseSizeBeforeUpdate = healthConcernRepository.findAll().size();
        healthConcern.setId(count.incrementAndGet());

        // Create the HealthConcern
        HealthConcernDTO healthConcernDTO = healthConcernMapper.toDto(healthConcern);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHealthConcernMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, healthConcernDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(healthConcernDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HealthConcern in the database
        List<HealthConcern> healthConcernList = healthConcernRepository.findAll();
        assertThat(healthConcernList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHealthConcern() throws Exception {
        int databaseSizeBeforeUpdate = healthConcernRepository.findAll().size();
        healthConcern.setId(count.incrementAndGet());

        // Create the HealthConcern
        HealthConcernDTO healthConcernDTO = healthConcernMapper.toDto(healthConcern);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHealthConcernMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(healthConcernDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HealthConcern in the database
        List<HealthConcern> healthConcernList = healthConcernRepository.findAll();
        assertThat(healthConcernList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHealthConcern() throws Exception {
        int databaseSizeBeforeUpdate = healthConcernRepository.findAll().size();
        healthConcern.setId(count.incrementAndGet());

        // Create the HealthConcern
        HealthConcernDTO healthConcernDTO = healthConcernMapper.toDto(healthConcern);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHealthConcernMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(healthConcernDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HealthConcern in the database
        List<HealthConcern> healthConcernList = healthConcernRepository.findAll();
        assertThat(healthConcernList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHealthConcern() throws Exception {
        // Initialize the database
        healthConcernRepository.saveAndFlush(healthConcern);

        int databaseSizeBeforeDelete = healthConcernRepository.findAll().size();

        // Delete the healthConcern
        restHealthConcernMockMvc
            .perform(delete(ENTITY_API_URL_ID, healthConcern.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<HealthConcern> healthConcernList = healthConcernRepository.findAll();
        assertThat(healthConcernList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
