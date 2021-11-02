package com.doclajotwellnesstrackerbackend.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.doclajotwellnesstrackerbackend.app.IntegrationTest;
import com.doclajotwellnesstrackerbackend.app.domain.AppUser;
import com.doclajotwellnesstrackerbackend.app.domain.HealthConcern;
import com.doclajotwellnesstrackerbackend.app.domain.VitalSign;
import com.doclajotwellnesstrackerbackend.app.repository.VitalSignRepository;
import com.doclajotwellnesstrackerbackend.app.service.VitalSignService;
import com.doclajotwellnesstrackerbackend.app.service.criteria.VitalSignCriteria;
import com.doclajotwellnesstrackerbackend.app.service.dto.VitalSignDTO;
import com.doclajotwellnesstrackerbackend.app.service.mapper.VitalSignMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link VitalSignResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class VitalSignResourceIT {

    private static final Integer DEFAULT_WEIGHT_IN_POUNDS = 1;
    private static final Integer UPDATED_WEIGHT_IN_POUNDS = 2;
    private static final Integer SMALLER_WEIGHT_IN_POUNDS = 1 - 1;

    private static final Integer DEFAULT_HEIGHT_IN_INCHES = 1;
    private static final Integer UPDATED_HEIGHT_IN_INCHES = 2;
    private static final Integer SMALLER_HEIGHT_IN_INCHES = 1 - 1;

    private static final Double DEFAULT_BMI = 1D;
    private static final Double UPDATED_BMI = 2D;
    private static final Double SMALLER_BMI = 1D - 1D;

    private static final Integer DEFAULT_GLASS_OF_WATER = 1;
    private static final Integer UPDATED_GLASS_OF_WATER = 2;
    private static final Integer SMALLER_GLASS_OF_WATER = 1 - 1;

    private static final Integer DEFAULT_SYSTOLIC = 1;
    private static final Integer UPDATED_SYSTOLIC = 2;
    private static final Integer SMALLER_SYSTOLIC = 1 - 1;

    private static final Integer DEFAULT_DIASTOLIC = 1;
    private static final Integer UPDATED_DIASTOLIC = 2;
    private static final Integer SMALLER_DIASTOLIC = 1 - 1;

    private static final Double DEFAULT_CURRENT_BLOOD_SUGAR = 1D;
    private static final Double UPDATED_CURRENT_BLOOD_SUGAR = 2D;
    private static final Double SMALLER_CURRENT_BLOOD_SUGAR = 1D - 1D;

    private static final Double DEFAULT_LIPID_PROFILE = 1D;
    private static final Double UPDATED_LIPID_PROFILE = 2D;
    private static final Double SMALLER_LIPID_PROFILE = 1D - 1D;

    private static final String ENTITY_API_URL = "/api/vital-signs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VitalSignRepository vitalSignRepository;

    @Mock
    private VitalSignRepository vitalSignRepositoryMock;

    @Autowired
    private VitalSignMapper vitalSignMapper;

    @Mock
    private VitalSignService vitalSignServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVitalSignMockMvc;

    private VitalSign vitalSign;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VitalSign createEntity(EntityManager em) {
        VitalSign vitalSign = new VitalSign()
            .weightInPounds(DEFAULT_WEIGHT_IN_POUNDS)
            .heightInInches(DEFAULT_HEIGHT_IN_INCHES)
            .bmi(DEFAULT_BMI)
            .glassOfWater(DEFAULT_GLASS_OF_WATER)
            .systolic(DEFAULT_SYSTOLIC)
            .diastolic(DEFAULT_DIASTOLIC)
            .currentBloodSugar(DEFAULT_CURRENT_BLOOD_SUGAR)
            .lipidProfile(DEFAULT_LIPID_PROFILE);
        // Add required entity
        AppUser appUser;
        if (TestUtil.findAll(em, AppUser.class).isEmpty()) {
            appUser = AppUserResourceIT.createEntity(em);
            em.persist(appUser);
            em.flush();
        } else {
            appUser = TestUtil.findAll(em, AppUser.class).get(0);
        }
        vitalSign.setAppUser(appUser);
        // Add required entity
        HealthConcern healthConcern;
        if (TestUtil.findAll(em, HealthConcern.class).isEmpty()) {
            healthConcern = HealthConcernResourceIT.createEntity(em);
            em.persist(healthConcern);
            em.flush();
        } else {
            healthConcern = TestUtil.findAll(em, HealthConcern.class).get(0);
        }
        vitalSign.getHealthConcerns().add(healthConcern);
        return vitalSign;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VitalSign createUpdatedEntity(EntityManager em) {
        VitalSign vitalSign = new VitalSign()
            .weightInPounds(UPDATED_WEIGHT_IN_POUNDS)
            .heightInInches(UPDATED_HEIGHT_IN_INCHES)
            .bmi(UPDATED_BMI)
            .glassOfWater(UPDATED_GLASS_OF_WATER)
            .systolic(UPDATED_SYSTOLIC)
            .diastolic(UPDATED_DIASTOLIC)
            .currentBloodSugar(UPDATED_CURRENT_BLOOD_SUGAR)
            .lipidProfile(UPDATED_LIPID_PROFILE);
        // Add required entity
        AppUser appUser;
        if (TestUtil.findAll(em, AppUser.class).isEmpty()) {
            appUser = AppUserResourceIT.createUpdatedEntity(em);
            em.persist(appUser);
            em.flush();
        } else {
            appUser = TestUtil.findAll(em, AppUser.class).get(0);
        }
        vitalSign.setAppUser(appUser);
        // Add required entity
        HealthConcern healthConcern;
        if (TestUtil.findAll(em, HealthConcern.class).isEmpty()) {
            healthConcern = HealthConcernResourceIT.createUpdatedEntity(em);
            em.persist(healthConcern);
            em.flush();
        } else {
            healthConcern = TestUtil.findAll(em, HealthConcern.class).get(0);
        }
        vitalSign.getHealthConcerns().add(healthConcern);
        return vitalSign;
    }

    @BeforeEach
    public void initTest() {
        vitalSign = createEntity(em);
    }

    @Test
    @Transactional
    void createVitalSign() throws Exception {
        int databaseSizeBeforeCreate = vitalSignRepository.findAll().size();
        // Create the VitalSign
        VitalSignDTO vitalSignDTO = vitalSignMapper.toDto(vitalSign);
        restVitalSignMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vitalSignDTO)))
            .andExpect(status().isCreated());

        // Validate the VitalSign in the database
        List<VitalSign> vitalSignList = vitalSignRepository.findAll();
        assertThat(vitalSignList).hasSize(databaseSizeBeforeCreate + 1);
        VitalSign testVitalSign = vitalSignList.get(vitalSignList.size() - 1);
        assertThat(testVitalSign.getWeightInPounds()).isEqualTo(DEFAULT_WEIGHT_IN_POUNDS);
        assertThat(testVitalSign.getHeightInInches()).isEqualTo(DEFAULT_HEIGHT_IN_INCHES);
        assertThat(testVitalSign.getBmi()).isEqualTo(DEFAULT_BMI);
        assertThat(testVitalSign.getGlassOfWater()).isEqualTo(DEFAULT_GLASS_OF_WATER);
        assertThat(testVitalSign.getSystolic()).isEqualTo(DEFAULT_SYSTOLIC);
        assertThat(testVitalSign.getDiastolic()).isEqualTo(DEFAULT_DIASTOLIC);
        assertThat(testVitalSign.getCurrentBloodSugar()).isEqualTo(DEFAULT_CURRENT_BLOOD_SUGAR);
        assertThat(testVitalSign.getLipidProfile()).isEqualTo(DEFAULT_LIPID_PROFILE);
    }

    @Test
    @Transactional
    void createVitalSignWithExistingId() throws Exception {
        // Create the VitalSign with an existing ID
        vitalSign.setId(1L);
        VitalSignDTO vitalSignDTO = vitalSignMapper.toDto(vitalSign);

        int databaseSizeBeforeCreate = vitalSignRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVitalSignMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vitalSignDTO)))
            .andExpect(status().isBadRequest());

        // Validate the VitalSign in the database
        List<VitalSign> vitalSignList = vitalSignRepository.findAll();
        assertThat(vitalSignList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVitalSigns() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        // Get all the vitalSignList
        restVitalSignMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vitalSign.getId().intValue())))
            .andExpect(jsonPath("$.[*].weightInPounds").value(hasItem(DEFAULT_WEIGHT_IN_POUNDS)))
            .andExpect(jsonPath("$.[*].heightInInches").value(hasItem(DEFAULT_HEIGHT_IN_INCHES)))
            .andExpect(jsonPath("$.[*].bmi").value(hasItem(DEFAULT_BMI.doubleValue())))
            .andExpect(jsonPath("$.[*].glassOfWater").value(hasItem(DEFAULT_GLASS_OF_WATER)))
            .andExpect(jsonPath("$.[*].systolic").value(hasItem(DEFAULT_SYSTOLIC)))
            .andExpect(jsonPath("$.[*].diastolic").value(hasItem(DEFAULT_DIASTOLIC)))
            .andExpect(jsonPath("$.[*].currentBloodSugar").value(hasItem(DEFAULT_CURRENT_BLOOD_SUGAR.doubleValue())))
            .andExpect(jsonPath("$.[*].lipidProfile").value(hasItem(DEFAULT_LIPID_PROFILE.doubleValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllVitalSignsWithEagerRelationshipsIsEnabled() throws Exception {
        when(vitalSignServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restVitalSignMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(vitalSignServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllVitalSignsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(vitalSignServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restVitalSignMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(vitalSignServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getVitalSign() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        // Get the vitalSign
        restVitalSignMockMvc
            .perform(get(ENTITY_API_URL_ID, vitalSign.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vitalSign.getId().intValue()))
            .andExpect(jsonPath("$.weightInPounds").value(DEFAULT_WEIGHT_IN_POUNDS))
            .andExpect(jsonPath("$.heightInInches").value(DEFAULT_HEIGHT_IN_INCHES))
            .andExpect(jsonPath("$.bmi").value(DEFAULT_BMI.doubleValue()))
            .andExpect(jsonPath("$.glassOfWater").value(DEFAULT_GLASS_OF_WATER))
            .andExpect(jsonPath("$.systolic").value(DEFAULT_SYSTOLIC))
            .andExpect(jsonPath("$.diastolic").value(DEFAULT_DIASTOLIC))
            .andExpect(jsonPath("$.currentBloodSugar").value(DEFAULT_CURRENT_BLOOD_SUGAR.doubleValue()))
            .andExpect(jsonPath("$.lipidProfile").value(DEFAULT_LIPID_PROFILE.doubleValue()));
    }

    @Test
    @Transactional
    void getVitalSignsByIdFiltering() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        Long id = vitalSign.getId();

        defaultVitalSignShouldBeFound("id.equals=" + id);
        defaultVitalSignShouldNotBeFound("id.notEquals=" + id);

        defaultVitalSignShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultVitalSignShouldNotBeFound("id.greaterThan=" + id);

        defaultVitalSignShouldBeFound("id.lessThanOrEqual=" + id);
        defaultVitalSignShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllVitalSignsByWeightInPoundsIsEqualToSomething() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        // Get all the vitalSignList where weightInPounds equals to DEFAULT_WEIGHT_IN_POUNDS
        defaultVitalSignShouldBeFound("weightInPounds.equals=" + DEFAULT_WEIGHT_IN_POUNDS);

        // Get all the vitalSignList where weightInPounds equals to UPDATED_WEIGHT_IN_POUNDS
        defaultVitalSignShouldNotBeFound("weightInPounds.equals=" + UPDATED_WEIGHT_IN_POUNDS);
    }

    @Test
    @Transactional
    void getAllVitalSignsByWeightInPoundsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        // Get all the vitalSignList where weightInPounds not equals to DEFAULT_WEIGHT_IN_POUNDS
        defaultVitalSignShouldNotBeFound("weightInPounds.notEquals=" + DEFAULT_WEIGHT_IN_POUNDS);

        // Get all the vitalSignList where weightInPounds not equals to UPDATED_WEIGHT_IN_POUNDS
        defaultVitalSignShouldBeFound("weightInPounds.notEquals=" + UPDATED_WEIGHT_IN_POUNDS);
    }

    @Test
    @Transactional
    void getAllVitalSignsByWeightInPoundsIsInShouldWork() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        // Get all the vitalSignList where weightInPounds in DEFAULT_WEIGHT_IN_POUNDS or UPDATED_WEIGHT_IN_POUNDS
        defaultVitalSignShouldBeFound("weightInPounds.in=" + DEFAULT_WEIGHT_IN_POUNDS + "," + UPDATED_WEIGHT_IN_POUNDS);

        // Get all the vitalSignList where weightInPounds equals to UPDATED_WEIGHT_IN_POUNDS
        defaultVitalSignShouldNotBeFound("weightInPounds.in=" + UPDATED_WEIGHT_IN_POUNDS);
    }

    @Test
    @Transactional
    void getAllVitalSignsByWeightInPoundsIsNullOrNotNull() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        // Get all the vitalSignList where weightInPounds is not null
        defaultVitalSignShouldBeFound("weightInPounds.specified=true");

        // Get all the vitalSignList where weightInPounds is null
        defaultVitalSignShouldNotBeFound("weightInPounds.specified=false");
    }

    @Test
    @Transactional
    void getAllVitalSignsByWeightInPoundsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        // Get all the vitalSignList where weightInPounds is greater than or equal to DEFAULT_WEIGHT_IN_POUNDS
        defaultVitalSignShouldBeFound("weightInPounds.greaterThanOrEqual=" + DEFAULT_WEIGHT_IN_POUNDS);

        // Get all the vitalSignList where weightInPounds is greater than or equal to UPDATED_WEIGHT_IN_POUNDS
        defaultVitalSignShouldNotBeFound("weightInPounds.greaterThanOrEqual=" + UPDATED_WEIGHT_IN_POUNDS);
    }

    @Test
    @Transactional
    void getAllVitalSignsByWeightInPoundsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        // Get all the vitalSignList where weightInPounds is less than or equal to DEFAULT_WEIGHT_IN_POUNDS
        defaultVitalSignShouldBeFound("weightInPounds.lessThanOrEqual=" + DEFAULT_WEIGHT_IN_POUNDS);

        // Get all the vitalSignList where weightInPounds is less than or equal to SMALLER_WEIGHT_IN_POUNDS
        defaultVitalSignShouldNotBeFound("weightInPounds.lessThanOrEqual=" + SMALLER_WEIGHT_IN_POUNDS);
    }

    @Test
    @Transactional
    void getAllVitalSignsByWeightInPoundsIsLessThanSomething() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        // Get all the vitalSignList where weightInPounds is less than DEFAULT_WEIGHT_IN_POUNDS
        defaultVitalSignShouldNotBeFound("weightInPounds.lessThan=" + DEFAULT_WEIGHT_IN_POUNDS);

        // Get all the vitalSignList where weightInPounds is less than UPDATED_WEIGHT_IN_POUNDS
        defaultVitalSignShouldBeFound("weightInPounds.lessThan=" + UPDATED_WEIGHT_IN_POUNDS);
    }

    @Test
    @Transactional
    void getAllVitalSignsByWeightInPoundsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        // Get all the vitalSignList where weightInPounds is greater than DEFAULT_WEIGHT_IN_POUNDS
        defaultVitalSignShouldNotBeFound("weightInPounds.greaterThan=" + DEFAULT_WEIGHT_IN_POUNDS);

        // Get all the vitalSignList where weightInPounds is greater than SMALLER_WEIGHT_IN_POUNDS
        defaultVitalSignShouldBeFound("weightInPounds.greaterThan=" + SMALLER_WEIGHT_IN_POUNDS);
    }

    @Test
    @Transactional
    void getAllVitalSignsByHeightInInchesIsEqualToSomething() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        // Get all the vitalSignList where heightInInches equals to DEFAULT_HEIGHT_IN_INCHES
        defaultVitalSignShouldBeFound("heightInInches.equals=" + DEFAULT_HEIGHT_IN_INCHES);

        // Get all the vitalSignList where heightInInches equals to UPDATED_HEIGHT_IN_INCHES
        defaultVitalSignShouldNotBeFound("heightInInches.equals=" + UPDATED_HEIGHT_IN_INCHES);
    }

    @Test
    @Transactional
    void getAllVitalSignsByHeightInInchesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        // Get all the vitalSignList where heightInInches not equals to DEFAULT_HEIGHT_IN_INCHES
        defaultVitalSignShouldNotBeFound("heightInInches.notEquals=" + DEFAULT_HEIGHT_IN_INCHES);

        // Get all the vitalSignList where heightInInches not equals to UPDATED_HEIGHT_IN_INCHES
        defaultVitalSignShouldBeFound("heightInInches.notEquals=" + UPDATED_HEIGHT_IN_INCHES);
    }

    @Test
    @Transactional
    void getAllVitalSignsByHeightInInchesIsInShouldWork() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        // Get all the vitalSignList where heightInInches in DEFAULT_HEIGHT_IN_INCHES or UPDATED_HEIGHT_IN_INCHES
        defaultVitalSignShouldBeFound("heightInInches.in=" + DEFAULT_HEIGHT_IN_INCHES + "," + UPDATED_HEIGHT_IN_INCHES);

        // Get all the vitalSignList where heightInInches equals to UPDATED_HEIGHT_IN_INCHES
        defaultVitalSignShouldNotBeFound("heightInInches.in=" + UPDATED_HEIGHT_IN_INCHES);
    }

    @Test
    @Transactional
    void getAllVitalSignsByHeightInInchesIsNullOrNotNull() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        // Get all the vitalSignList where heightInInches is not null
        defaultVitalSignShouldBeFound("heightInInches.specified=true");

        // Get all the vitalSignList where heightInInches is null
        defaultVitalSignShouldNotBeFound("heightInInches.specified=false");
    }

    @Test
    @Transactional
    void getAllVitalSignsByHeightInInchesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        // Get all the vitalSignList where heightInInches is greater than or equal to DEFAULT_HEIGHT_IN_INCHES
        defaultVitalSignShouldBeFound("heightInInches.greaterThanOrEqual=" + DEFAULT_HEIGHT_IN_INCHES);

        // Get all the vitalSignList where heightInInches is greater than or equal to UPDATED_HEIGHT_IN_INCHES
        defaultVitalSignShouldNotBeFound("heightInInches.greaterThanOrEqual=" + UPDATED_HEIGHT_IN_INCHES);
    }

    @Test
    @Transactional
    void getAllVitalSignsByHeightInInchesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        // Get all the vitalSignList where heightInInches is less than or equal to DEFAULT_HEIGHT_IN_INCHES
        defaultVitalSignShouldBeFound("heightInInches.lessThanOrEqual=" + DEFAULT_HEIGHT_IN_INCHES);

        // Get all the vitalSignList where heightInInches is less than or equal to SMALLER_HEIGHT_IN_INCHES
        defaultVitalSignShouldNotBeFound("heightInInches.lessThanOrEqual=" + SMALLER_HEIGHT_IN_INCHES);
    }

    @Test
    @Transactional
    void getAllVitalSignsByHeightInInchesIsLessThanSomething() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        // Get all the vitalSignList where heightInInches is less than DEFAULT_HEIGHT_IN_INCHES
        defaultVitalSignShouldNotBeFound("heightInInches.lessThan=" + DEFAULT_HEIGHT_IN_INCHES);

        // Get all the vitalSignList where heightInInches is less than UPDATED_HEIGHT_IN_INCHES
        defaultVitalSignShouldBeFound("heightInInches.lessThan=" + UPDATED_HEIGHT_IN_INCHES);
    }

    @Test
    @Transactional
    void getAllVitalSignsByHeightInInchesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        // Get all the vitalSignList where heightInInches is greater than DEFAULT_HEIGHT_IN_INCHES
        defaultVitalSignShouldNotBeFound("heightInInches.greaterThan=" + DEFAULT_HEIGHT_IN_INCHES);

        // Get all the vitalSignList where heightInInches is greater than SMALLER_HEIGHT_IN_INCHES
        defaultVitalSignShouldBeFound("heightInInches.greaterThan=" + SMALLER_HEIGHT_IN_INCHES);
    }

    @Test
    @Transactional
    void getAllVitalSignsByBmiIsEqualToSomething() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        // Get all the vitalSignList where bmi equals to DEFAULT_BMI
        defaultVitalSignShouldBeFound("bmi.equals=" + DEFAULT_BMI);

        // Get all the vitalSignList where bmi equals to UPDATED_BMI
        defaultVitalSignShouldNotBeFound("bmi.equals=" + UPDATED_BMI);
    }

    @Test
    @Transactional
    void getAllVitalSignsByBmiIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        // Get all the vitalSignList where bmi not equals to DEFAULT_BMI
        defaultVitalSignShouldNotBeFound("bmi.notEquals=" + DEFAULT_BMI);

        // Get all the vitalSignList where bmi not equals to UPDATED_BMI
        defaultVitalSignShouldBeFound("bmi.notEquals=" + UPDATED_BMI);
    }

    @Test
    @Transactional
    void getAllVitalSignsByBmiIsInShouldWork() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        // Get all the vitalSignList where bmi in DEFAULT_BMI or UPDATED_BMI
        defaultVitalSignShouldBeFound("bmi.in=" + DEFAULT_BMI + "," + UPDATED_BMI);

        // Get all the vitalSignList where bmi equals to UPDATED_BMI
        defaultVitalSignShouldNotBeFound("bmi.in=" + UPDATED_BMI);
    }

    @Test
    @Transactional
    void getAllVitalSignsByBmiIsNullOrNotNull() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        // Get all the vitalSignList where bmi is not null
        defaultVitalSignShouldBeFound("bmi.specified=true");

        // Get all the vitalSignList where bmi is null
        defaultVitalSignShouldNotBeFound("bmi.specified=false");
    }

    @Test
    @Transactional
    void getAllVitalSignsByBmiIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        // Get all the vitalSignList where bmi is greater than or equal to DEFAULT_BMI
        defaultVitalSignShouldBeFound("bmi.greaterThanOrEqual=" + DEFAULT_BMI);

        // Get all the vitalSignList where bmi is greater than or equal to UPDATED_BMI
        defaultVitalSignShouldNotBeFound("bmi.greaterThanOrEqual=" + UPDATED_BMI);
    }

    @Test
    @Transactional
    void getAllVitalSignsByBmiIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        // Get all the vitalSignList where bmi is less than or equal to DEFAULT_BMI
        defaultVitalSignShouldBeFound("bmi.lessThanOrEqual=" + DEFAULT_BMI);

        // Get all the vitalSignList where bmi is less than or equal to SMALLER_BMI
        defaultVitalSignShouldNotBeFound("bmi.lessThanOrEqual=" + SMALLER_BMI);
    }

    @Test
    @Transactional
    void getAllVitalSignsByBmiIsLessThanSomething() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        // Get all the vitalSignList where bmi is less than DEFAULT_BMI
        defaultVitalSignShouldNotBeFound("bmi.lessThan=" + DEFAULT_BMI);

        // Get all the vitalSignList where bmi is less than UPDATED_BMI
        defaultVitalSignShouldBeFound("bmi.lessThan=" + UPDATED_BMI);
    }

    @Test
    @Transactional
    void getAllVitalSignsByBmiIsGreaterThanSomething() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        // Get all the vitalSignList where bmi is greater than DEFAULT_BMI
        defaultVitalSignShouldNotBeFound("bmi.greaterThan=" + DEFAULT_BMI);

        // Get all the vitalSignList where bmi is greater than SMALLER_BMI
        defaultVitalSignShouldBeFound("bmi.greaterThan=" + SMALLER_BMI);
    }

    @Test
    @Transactional
    void getAllVitalSignsByGlassOfWaterIsEqualToSomething() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        // Get all the vitalSignList where glassOfWater equals to DEFAULT_GLASS_OF_WATER
        defaultVitalSignShouldBeFound("glassOfWater.equals=" + DEFAULT_GLASS_OF_WATER);

        // Get all the vitalSignList where glassOfWater equals to UPDATED_GLASS_OF_WATER
        defaultVitalSignShouldNotBeFound("glassOfWater.equals=" + UPDATED_GLASS_OF_WATER);
    }

    @Test
    @Transactional
    void getAllVitalSignsByGlassOfWaterIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        // Get all the vitalSignList where glassOfWater not equals to DEFAULT_GLASS_OF_WATER
        defaultVitalSignShouldNotBeFound("glassOfWater.notEquals=" + DEFAULT_GLASS_OF_WATER);

        // Get all the vitalSignList where glassOfWater not equals to UPDATED_GLASS_OF_WATER
        defaultVitalSignShouldBeFound("glassOfWater.notEquals=" + UPDATED_GLASS_OF_WATER);
    }

    @Test
    @Transactional
    void getAllVitalSignsByGlassOfWaterIsInShouldWork() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        // Get all the vitalSignList where glassOfWater in DEFAULT_GLASS_OF_WATER or UPDATED_GLASS_OF_WATER
        defaultVitalSignShouldBeFound("glassOfWater.in=" + DEFAULT_GLASS_OF_WATER + "," + UPDATED_GLASS_OF_WATER);

        // Get all the vitalSignList where glassOfWater equals to UPDATED_GLASS_OF_WATER
        defaultVitalSignShouldNotBeFound("glassOfWater.in=" + UPDATED_GLASS_OF_WATER);
    }

    @Test
    @Transactional
    void getAllVitalSignsByGlassOfWaterIsNullOrNotNull() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        // Get all the vitalSignList where glassOfWater is not null
        defaultVitalSignShouldBeFound("glassOfWater.specified=true");

        // Get all the vitalSignList where glassOfWater is null
        defaultVitalSignShouldNotBeFound("glassOfWater.specified=false");
    }

    @Test
    @Transactional
    void getAllVitalSignsByGlassOfWaterIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        // Get all the vitalSignList where glassOfWater is greater than or equal to DEFAULT_GLASS_OF_WATER
        defaultVitalSignShouldBeFound("glassOfWater.greaterThanOrEqual=" + DEFAULT_GLASS_OF_WATER);

        // Get all the vitalSignList where glassOfWater is greater than or equal to UPDATED_GLASS_OF_WATER
        defaultVitalSignShouldNotBeFound("glassOfWater.greaterThanOrEqual=" + UPDATED_GLASS_OF_WATER);
    }

    @Test
    @Transactional
    void getAllVitalSignsByGlassOfWaterIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        // Get all the vitalSignList where glassOfWater is less than or equal to DEFAULT_GLASS_OF_WATER
        defaultVitalSignShouldBeFound("glassOfWater.lessThanOrEqual=" + DEFAULT_GLASS_OF_WATER);

        // Get all the vitalSignList where glassOfWater is less than or equal to SMALLER_GLASS_OF_WATER
        defaultVitalSignShouldNotBeFound("glassOfWater.lessThanOrEqual=" + SMALLER_GLASS_OF_WATER);
    }

    @Test
    @Transactional
    void getAllVitalSignsByGlassOfWaterIsLessThanSomething() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        // Get all the vitalSignList where glassOfWater is less than DEFAULT_GLASS_OF_WATER
        defaultVitalSignShouldNotBeFound("glassOfWater.lessThan=" + DEFAULT_GLASS_OF_WATER);

        // Get all the vitalSignList where glassOfWater is less than UPDATED_GLASS_OF_WATER
        defaultVitalSignShouldBeFound("glassOfWater.lessThan=" + UPDATED_GLASS_OF_WATER);
    }

    @Test
    @Transactional
    void getAllVitalSignsByGlassOfWaterIsGreaterThanSomething() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        // Get all the vitalSignList where glassOfWater is greater than DEFAULT_GLASS_OF_WATER
        defaultVitalSignShouldNotBeFound("glassOfWater.greaterThan=" + DEFAULT_GLASS_OF_WATER);

        // Get all the vitalSignList where glassOfWater is greater than SMALLER_GLASS_OF_WATER
        defaultVitalSignShouldBeFound("glassOfWater.greaterThan=" + SMALLER_GLASS_OF_WATER);
    }

    @Test
    @Transactional
    void getAllVitalSignsBySystolicIsEqualToSomething() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        // Get all the vitalSignList where systolic equals to DEFAULT_SYSTOLIC
        defaultVitalSignShouldBeFound("systolic.equals=" + DEFAULT_SYSTOLIC);

        // Get all the vitalSignList where systolic equals to UPDATED_SYSTOLIC
        defaultVitalSignShouldNotBeFound("systolic.equals=" + UPDATED_SYSTOLIC);
    }

    @Test
    @Transactional
    void getAllVitalSignsBySystolicIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        // Get all the vitalSignList where systolic not equals to DEFAULT_SYSTOLIC
        defaultVitalSignShouldNotBeFound("systolic.notEquals=" + DEFAULT_SYSTOLIC);

        // Get all the vitalSignList where systolic not equals to UPDATED_SYSTOLIC
        defaultVitalSignShouldBeFound("systolic.notEquals=" + UPDATED_SYSTOLIC);
    }

    @Test
    @Transactional
    void getAllVitalSignsBySystolicIsInShouldWork() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        // Get all the vitalSignList where systolic in DEFAULT_SYSTOLIC or UPDATED_SYSTOLIC
        defaultVitalSignShouldBeFound("systolic.in=" + DEFAULT_SYSTOLIC + "," + UPDATED_SYSTOLIC);

        // Get all the vitalSignList where systolic equals to UPDATED_SYSTOLIC
        defaultVitalSignShouldNotBeFound("systolic.in=" + UPDATED_SYSTOLIC);
    }

    @Test
    @Transactional
    void getAllVitalSignsBySystolicIsNullOrNotNull() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        // Get all the vitalSignList where systolic is not null
        defaultVitalSignShouldBeFound("systolic.specified=true");

        // Get all the vitalSignList where systolic is null
        defaultVitalSignShouldNotBeFound("systolic.specified=false");
    }

    @Test
    @Transactional
    void getAllVitalSignsBySystolicIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        // Get all the vitalSignList where systolic is greater than or equal to DEFAULT_SYSTOLIC
        defaultVitalSignShouldBeFound("systolic.greaterThanOrEqual=" + DEFAULT_SYSTOLIC);

        // Get all the vitalSignList where systolic is greater than or equal to UPDATED_SYSTOLIC
        defaultVitalSignShouldNotBeFound("systolic.greaterThanOrEqual=" + UPDATED_SYSTOLIC);
    }

    @Test
    @Transactional
    void getAllVitalSignsBySystolicIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        // Get all the vitalSignList where systolic is less than or equal to DEFAULT_SYSTOLIC
        defaultVitalSignShouldBeFound("systolic.lessThanOrEqual=" + DEFAULT_SYSTOLIC);

        // Get all the vitalSignList where systolic is less than or equal to SMALLER_SYSTOLIC
        defaultVitalSignShouldNotBeFound("systolic.lessThanOrEqual=" + SMALLER_SYSTOLIC);
    }

    @Test
    @Transactional
    void getAllVitalSignsBySystolicIsLessThanSomething() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        // Get all the vitalSignList where systolic is less than DEFAULT_SYSTOLIC
        defaultVitalSignShouldNotBeFound("systolic.lessThan=" + DEFAULT_SYSTOLIC);

        // Get all the vitalSignList where systolic is less than UPDATED_SYSTOLIC
        defaultVitalSignShouldBeFound("systolic.lessThan=" + UPDATED_SYSTOLIC);
    }

    @Test
    @Transactional
    void getAllVitalSignsBySystolicIsGreaterThanSomething() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        // Get all the vitalSignList where systolic is greater than DEFAULT_SYSTOLIC
        defaultVitalSignShouldNotBeFound("systolic.greaterThan=" + DEFAULT_SYSTOLIC);

        // Get all the vitalSignList where systolic is greater than SMALLER_SYSTOLIC
        defaultVitalSignShouldBeFound("systolic.greaterThan=" + SMALLER_SYSTOLIC);
    }

    @Test
    @Transactional
    void getAllVitalSignsByDiastolicIsEqualToSomething() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        // Get all the vitalSignList where diastolic equals to DEFAULT_DIASTOLIC
        defaultVitalSignShouldBeFound("diastolic.equals=" + DEFAULT_DIASTOLIC);

        // Get all the vitalSignList where diastolic equals to UPDATED_DIASTOLIC
        defaultVitalSignShouldNotBeFound("diastolic.equals=" + UPDATED_DIASTOLIC);
    }

    @Test
    @Transactional
    void getAllVitalSignsByDiastolicIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        // Get all the vitalSignList where diastolic not equals to DEFAULT_DIASTOLIC
        defaultVitalSignShouldNotBeFound("diastolic.notEquals=" + DEFAULT_DIASTOLIC);

        // Get all the vitalSignList where diastolic not equals to UPDATED_DIASTOLIC
        defaultVitalSignShouldBeFound("diastolic.notEquals=" + UPDATED_DIASTOLIC);
    }

    @Test
    @Transactional
    void getAllVitalSignsByDiastolicIsInShouldWork() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        // Get all the vitalSignList where diastolic in DEFAULT_DIASTOLIC or UPDATED_DIASTOLIC
        defaultVitalSignShouldBeFound("diastolic.in=" + DEFAULT_DIASTOLIC + "," + UPDATED_DIASTOLIC);

        // Get all the vitalSignList where diastolic equals to UPDATED_DIASTOLIC
        defaultVitalSignShouldNotBeFound("diastolic.in=" + UPDATED_DIASTOLIC);
    }

    @Test
    @Transactional
    void getAllVitalSignsByDiastolicIsNullOrNotNull() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        // Get all the vitalSignList where diastolic is not null
        defaultVitalSignShouldBeFound("diastolic.specified=true");

        // Get all the vitalSignList where diastolic is null
        defaultVitalSignShouldNotBeFound("diastolic.specified=false");
    }

    @Test
    @Transactional
    void getAllVitalSignsByDiastolicIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        // Get all the vitalSignList where diastolic is greater than or equal to DEFAULT_DIASTOLIC
        defaultVitalSignShouldBeFound("diastolic.greaterThanOrEqual=" + DEFAULT_DIASTOLIC);

        // Get all the vitalSignList where diastolic is greater than or equal to UPDATED_DIASTOLIC
        defaultVitalSignShouldNotBeFound("diastolic.greaterThanOrEqual=" + UPDATED_DIASTOLIC);
    }

    @Test
    @Transactional
    void getAllVitalSignsByDiastolicIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        // Get all the vitalSignList where diastolic is less than or equal to DEFAULT_DIASTOLIC
        defaultVitalSignShouldBeFound("diastolic.lessThanOrEqual=" + DEFAULT_DIASTOLIC);

        // Get all the vitalSignList where diastolic is less than or equal to SMALLER_DIASTOLIC
        defaultVitalSignShouldNotBeFound("diastolic.lessThanOrEqual=" + SMALLER_DIASTOLIC);
    }

    @Test
    @Transactional
    void getAllVitalSignsByDiastolicIsLessThanSomething() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        // Get all the vitalSignList where diastolic is less than DEFAULT_DIASTOLIC
        defaultVitalSignShouldNotBeFound("diastolic.lessThan=" + DEFAULT_DIASTOLIC);

        // Get all the vitalSignList where diastolic is less than UPDATED_DIASTOLIC
        defaultVitalSignShouldBeFound("diastolic.lessThan=" + UPDATED_DIASTOLIC);
    }

    @Test
    @Transactional
    void getAllVitalSignsByDiastolicIsGreaterThanSomething() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        // Get all the vitalSignList where diastolic is greater than DEFAULT_DIASTOLIC
        defaultVitalSignShouldNotBeFound("diastolic.greaterThan=" + DEFAULT_DIASTOLIC);

        // Get all the vitalSignList where diastolic is greater than SMALLER_DIASTOLIC
        defaultVitalSignShouldBeFound("diastolic.greaterThan=" + SMALLER_DIASTOLIC);
    }

    @Test
    @Transactional
    void getAllVitalSignsByCurrentBloodSugarIsEqualToSomething() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        // Get all the vitalSignList where currentBloodSugar equals to DEFAULT_CURRENT_BLOOD_SUGAR
        defaultVitalSignShouldBeFound("currentBloodSugar.equals=" + DEFAULT_CURRENT_BLOOD_SUGAR);

        // Get all the vitalSignList where currentBloodSugar equals to UPDATED_CURRENT_BLOOD_SUGAR
        defaultVitalSignShouldNotBeFound("currentBloodSugar.equals=" + UPDATED_CURRENT_BLOOD_SUGAR);
    }

    @Test
    @Transactional
    void getAllVitalSignsByCurrentBloodSugarIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        // Get all the vitalSignList where currentBloodSugar not equals to DEFAULT_CURRENT_BLOOD_SUGAR
        defaultVitalSignShouldNotBeFound("currentBloodSugar.notEquals=" + DEFAULT_CURRENT_BLOOD_SUGAR);

        // Get all the vitalSignList where currentBloodSugar not equals to UPDATED_CURRENT_BLOOD_SUGAR
        defaultVitalSignShouldBeFound("currentBloodSugar.notEquals=" + UPDATED_CURRENT_BLOOD_SUGAR);
    }

    @Test
    @Transactional
    void getAllVitalSignsByCurrentBloodSugarIsInShouldWork() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        // Get all the vitalSignList where currentBloodSugar in DEFAULT_CURRENT_BLOOD_SUGAR or UPDATED_CURRENT_BLOOD_SUGAR
        defaultVitalSignShouldBeFound("currentBloodSugar.in=" + DEFAULT_CURRENT_BLOOD_SUGAR + "," + UPDATED_CURRENT_BLOOD_SUGAR);

        // Get all the vitalSignList where currentBloodSugar equals to UPDATED_CURRENT_BLOOD_SUGAR
        defaultVitalSignShouldNotBeFound("currentBloodSugar.in=" + UPDATED_CURRENT_BLOOD_SUGAR);
    }

    @Test
    @Transactional
    void getAllVitalSignsByCurrentBloodSugarIsNullOrNotNull() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        // Get all the vitalSignList where currentBloodSugar is not null
        defaultVitalSignShouldBeFound("currentBloodSugar.specified=true");

        // Get all the vitalSignList where currentBloodSugar is null
        defaultVitalSignShouldNotBeFound("currentBloodSugar.specified=false");
    }

    @Test
    @Transactional
    void getAllVitalSignsByCurrentBloodSugarIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        // Get all the vitalSignList where currentBloodSugar is greater than or equal to DEFAULT_CURRENT_BLOOD_SUGAR
        defaultVitalSignShouldBeFound("currentBloodSugar.greaterThanOrEqual=" + DEFAULT_CURRENT_BLOOD_SUGAR);

        // Get all the vitalSignList where currentBloodSugar is greater than or equal to UPDATED_CURRENT_BLOOD_SUGAR
        defaultVitalSignShouldNotBeFound("currentBloodSugar.greaterThanOrEqual=" + UPDATED_CURRENT_BLOOD_SUGAR);
    }

    @Test
    @Transactional
    void getAllVitalSignsByCurrentBloodSugarIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        // Get all the vitalSignList where currentBloodSugar is less than or equal to DEFAULT_CURRENT_BLOOD_SUGAR
        defaultVitalSignShouldBeFound("currentBloodSugar.lessThanOrEqual=" + DEFAULT_CURRENT_BLOOD_SUGAR);

        // Get all the vitalSignList where currentBloodSugar is less than or equal to SMALLER_CURRENT_BLOOD_SUGAR
        defaultVitalSignShouldNotBeFound("currentBloodSugar.lessThanOrEqual=" + SMALLER_CURRENT_BLOOD_SUGAR);
    }

    @Test
    @Transactional
    void getAllVitalSignsByCurrentBloodSugarIsLessThanSomething() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        // Get all the vitalSignList where currentBloodSugar is less than DEFAULT_CURRENT_BLOOD_SUGAR
        defaultVitalSignShouldNotBeFound("currentBloodSugar.lessThan=" + DEFAULT_CURRENT_BLOOD_SUGAR);

        // Get all the vitalSignList where currentBloodSugar is less than UPDATED_CURRENT_BLOOD_SUGAR
        defaultVitalSignShouldBeFound("currentBloodSugar.lessThan=" + UPDATED_CURRENT_BLOOD_SUGAR);
    }

    @Test
    @Transactional
    void getAllVitalSignsByCurrentBloodSugarIsGreaterThanSomething() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        // Get all the vitalSignList where currentBloodSugar is greater than DEFAULT_CURRENT_BLOOD_SUGAR
        defaultVitalSignShouldNotBeFound("currentBloodSugar.greaterThan=" + DEFAULT_CURRENT_BLOOD_SUGAR);

        // Get all the vitalSignList where currentBloodSugar is greater than SMALLER_CURRENT_BLOOD_SUGAR
        defaultVitalSignShouldBeFound("currentBloodSugar.greaterThan=" + SMALLER_CURRENT_BLOOD_SUGAR);
    }

    @Test
    @Transactional
    void getAllVitalSignsByLipidProfileIsEqualToSomething() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        // Get all the vitalSignList where lipidProfile equals to DEFAULT_LIPID_PROFILE
        defaultVitalSignShouldBeFound("lipidProfile.equals=" + DEFAULT_LIPID_PROFILE);

        // Get all the vitalSignList where lipidProfile equals to UPDATED_LIPID_PROFILE
        defaultVitalSignShouldNotBeFound("lipidProfile.equals=" + UPDATED_LIPID_PROFILE);
    }

    @Test
    @Transactional
    void getAllVitalSignsByLipidProfileIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        // Get all the vitalSignList where lipidProfile not equals to DEFAULT_LIPID_PROFILE
        defaultVitalSignShouldNotBeFound("lipidProfile.notEquals=" + DEFAULT_LIPID_PROFILE);

        // Get all the vitalSignList where lipidProfile not equals to UPDATED_LIPID_PROFILE
        defaultVitalSignShouldBeFound("lipidProfile.notEquals=" + UPDATED_LIPID_PROFILE);
    }

    @Test
    @Transactional
    void getAllVitalSignsByLipidProfileIsInShouldWork() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        // Get all the vitalSignList where lipidProfile in DEFAULT_LIPID_PROFILE or UPDATED_LIPID_PROFILE
        defaultVitalSignShouldBeFound("lipidProfile.in=" + DEFAULT_LIPID_PROFILE + "," + UPDATED_LIPID_PROFILE);

        // Get all the vitalSignList where lipidProfile equals to UPDATED_LIPID_PROFILE
        defaultVitalSignShouldNotBeFound("lipidProfile.in=" + UPDATED_LIPID_PROFILE);
    }

    @Test
    @Transactional
    void getAllVitalSignsByLipidProfileIsNullOrNotNull() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        // Get all the vitalSignList where lipidProfile is not null
        defaultVitalSignShouldBeFound("lipidProfile.specified=true");

        // Get all the vitalSignList where lipidProfile is null
        defaultVitalSignShouldNotBeFound("lipidProfile.specified=false");
    }

    @Test
    @Transactional
    void getAllVitalSignsByLipidProfileIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        // Get all the vitalSignList where lipidProfile is greater than or equal to DEFAULT_LIPID_PROFILE
        defaultVitalSignShouldBeFound("lipidProfile.greaterThanOrEqual=" + DEFAULT_LIPID_PROFILE);

        // Get all the vitalSignList where lipidProfile is greater than or equal to UPDATED_LIPID_PROFILE
        defaultVitalSignShouldNotBeFound("lipidProfile.greaterThanOrEqual=" + UPDATED_LIPID_PROFILE);
    }

    @Test
    @Transactional
    void getAllVitalSignsByLipidProfileIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        // Get all the vitalSignList where lipidProfile is less than or equal to DEFAULT_LIPID_PROFILE
        defaultVitalSignShouldBeFound("lipidProfile.lessThanOrEqual=" + DEFAULT_LIPID_PROFILE);

        // Get all the vitalSignList where lipidProfile is less than or equal to SMALLER_LIPID_PROFILE
        defaultVitalSignShouldNotBeFound("lipidProfile.lessThanOrEqual=" + SMALLER_LIPID_PROFILE);
    }

    @Test
    @Transactional
    void getAllVitalSignsByLipidProfileIsLessThanSomething() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        // Get all the vitalSignList where lipidProfile is less than DEFAULT_LIPID_PROFILE
        defaultVitalSignShouldNotBeFound("lipidProfile.lessThan=" + DEFAULT_LIPID_PROFILE);

        // Get all the vitalSignList where lipidProfile is less than UPDATED_LIPID_PROFILE
        defaultVitalSignShouldBeFound("lipidProfile.lessThan=" + UPDATED_LIPID_PROFILE);
    }

    @Test
    @Transactional
    void getAllVitalSignsByLipidProfileIsGreaterThanSomething() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        // Get all the vitalSignList where lipidProfile is greater than DEFAULT_LIPID_PROFILE
        defaultVitalSignShouldNotBeFound("lipidProfile.greaterThan=" + DEFAULT_LIPID_PROFILE);

        // Get all the vitalSignList where lipidProfile is greater than SMALLER_LIPID_PROFILE
        defaultVitalSignShouldBeFound("lipidProfile.greaterThan=" + SMALLER_LIPID_PROFILE);
    }

    @Test
    @Transactional
    void getAllVitalSignsByAppUserIsEqualToSomething() throws Exception {
        // Get already existing entity
        AppUser appUser = vitalSign.getAppUser();
        vitalSignRepository.saveAndFlush(vitalSign);
        Long appUserId = appUser.getId();

        // Get all the vitalSignList where appUser equals to appUserId
        defaultVitalSignShouldBeFound("appUserId.equals=" + appUserId);

        // Get all the vitalSignList where appUser equals to (appUserId + 1)
        defaultVitalSignShouldNotBeFound("appUserId.equals=" + (appUserId + 1));
    }

    @Test
    @Transactional
    void getAllVitalSignsByHealthConcernIsEqualToSomething() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);
        HealthConcern healthConcern;
        if (TestUtil.findAll(em, HealthConcern.class).isEmpty()) {
            healthConcern = HealthConcernResourceIT.createEntity(em);
            em.persist(healthConcern);
            em.flush();
        } else {
            healthConcern = TestUtil.findAll(em, HealthConcern.class).get(0);
        }
        em.persist(healthConcern);
        em.flush();
        vitalSign.addHealthConcern(healthConcern);
        vitalSignRepository.saveAndFlush(vitalSign);
        Long healthConcernId = healthConcern.getId();

        // Get all the vitalSignList where healthConcern equals to healthConcernId
        defaultVitalSignShouldBeFound("healthConcernId.equals=" + healthConcernId);

        // Get all the vitalSignList where healthConcern equals to (healthConcernId + 1)
        defaultVitalSignShouldNotBeFound("healthConcernId.equals=" + (healthConcernId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultVitalSignShouldBeFound(String filter) throws Exception {
        restVitalSignMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vitalSign.getId().intValue())))
            .andExpect(jsonPath("$.[*].weightInPounds").value(hasItem(DEFAULT_WEIGHT_IN_POUNDS)))
            .andExpect(jsonPath("$.[*].heightInInches").value(hasItem(DEFAULT_HEIGHT_IN_INCHES)))
            .andExpect(jsonPath("$.[*].bmi").value(hasItem(DEFAULT_BMI.doubleValue())))
            .andExpect(jsonPath("$.[*].glassOfWater").value(hasItem(DEFAULT_GLASS_OF_WATER)))
            .andExpect(jsonPath("$.[*].systolic").value(hasItem(DEFAULT_SYSTOLIC)))
            .andExpect(jsonPath("$.[*].diastolic").value(hasItem(DEFAULT_DIASTOLIC)))
            .andExpect(jsonPath("$.[*].currentBloodSugar").value(hasItem(DEFAULT_CURRENT_BLOOD_SUGAR.doubleValue())))
            .andExpect(jsonPath("$.[*].lipidProfile").value(hasItem(DEFAULT_LIPID_PROFILE.doubleValue())));

        // Check, that the count call also returns 1
        restVitalSignMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultVitalSignShouldNotBeFound(String filter) throws Exception {
        restVitalSignMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restVitalSignMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingVitalSign() throws Exception {
        // Get the vitalSign
        restVitalSignMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewVitalSign() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        int databaseSizeBeforeUpdate = vitalSignRepository.findAll().size();

        // Update the vitalSign
        VitalSign updatedVitalSign = vitalSignRepository.findById(vitalSign.getId()).get();
        // Disconnect from session so that the updates on updatedVitalSign are not directly saved in db
        em.detach(updatedVitalSign);
        updatedVitalSign
            .weightInPounds(UPDATED_WEIGHT_IN_POUNDS)
            .heightInInches(UPDATED_HEIGHT_IN_INCHES)
            .bmi(UPDATED_BMI)
            .glassOfWater(UPDATED_GLASS_OF_WATER)
            .systolic(UPDATED_SYSTOLIC)
            .diastolic(UPDATED_DIASTOLIC)
            .currentBloodSugar(UPDATED_CURRENT_BLOOD_SUGAR)
            .lipidProfile(UPDATED_LIPID_PROFILE);
        VitalSignDTO vitalSignDTO = vitalSignMapper.toDto(updatedVitalSign);

        restVitalSignMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vitalSignDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vitalSignDTO))
            )
            .andExpect(status().isOk());

        // Validate the VitalSign in the database
        List<VitalSign> vitalSignList = vitalSignRepository.findAll();
        assertThat(vitalSignList).hasSize(databaseSizeBeforeUpdate);
        VitalSign testVitalSign = vitalSignList.get(vitalSignList.size() - 1);
        assertThat(testVitalSign.getWeightInPounds()).isEqualTo(UPDATED_WEIGHT_IN_POUNDS);
        assertThat(testVitalSign.getHeightInInches()).isEqualTo(UPDATED_HEIGHT_IN_INCHES);
        assertThat(testVitalSign.getBmi()).isEqualTo(UPDATED_BMI);
        assertThat(testVitalSign.getGlassOfWater()).isEqualTo(UPDATED_GLASS_OF_WATER);
        assertThat(testVitalSign.getSystolic()).isEqualTo(UPDATED_SYSTOLIC);
        assertThat(testVitalSign.getDiastolic()).isEqualTo(UPDATED_DIASTOLIC);
        assertThat(testVitalSign.getCurrentBloodSugar()).isEqualTo(UPDATED_CURRENT_BLOOD_SUGAR);
        assertThat(testVitalSign.getLipidProfile()).isEqualTo(UPDATED_LIPID_PROFILE);
    }

    @Test
    @Transactional
    void putNonExistingVitalSign() throws Exception {
        int databaseSizeBeforeUpdate = vitalSignRepository.findAll().size();
        vitalSign.setId(count.incrementAndGet());

        // Create the VitalSign
        VitalSignDTO vitalSignDTO = vitalSignMapper.toDto(vitalSign);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVitalSignMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vitalSignDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vitalSignDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VitalSign in the database
        List<VitalSign> vitalSignList = vitalSignRepository.findAll();
        assertThat(vitalSignList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVitalSign() throws Exception {
        int databaseSizeBeforeUpdate = vitalSignRepository.findAll().size();
        vitalSign.setId(count.incrementAndGet());

        // Create the VitalSign
        VitalSignDTO vitalSignDTO = vitalSignMapper.toDto(vitalSign);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVitalSignMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vitalSignDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VitalSign in the database
        List<VitalSign> vitalSignList = vitalSignRepository.findAll();
        assertThat(vitalSignList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVitalSign() throws Exception {
        int databaseSizeBeforeUpdate = vitalSignRepository.findAll().size();
        vitalSign.setId(count.incrementAndGet());

        // Create the VitalSign
        VitalSignDTO vitalSignDTO = vitalSignMapper.toDto(vitalSign);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVitalSignMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vitalSignDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the VitalSign in the database
        List<VitalSign> vitalSignList = vitalSignRepository.findAll();
        assertThat(vitalSignList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVitalSignWithPatch() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        int databaseSizeBeforeUpdate = vitalSignRepository.findAll().size();

        // Update the vitalSign using partial update
        VitalSign partialUpdatedVitalSign = new VitalSign();
        partialUpdatedVitalSign.setId(vitalSign.getId());

        partialUpdatedVitalSign
            .weightInPounds(UPDATED_WEIGHT_IN_POUNDS)
            .diastolic(UPDATED_DIASTOLIC)
            .currentBloodSugar(UPDATED_CURRENT_BLOOD_SUGAR);

        restVitalSignMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVitalSign.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVitalSign))
            )
            .andExpect(status().isOk());

        // Validate the VitalSign in the database
        List<VitalSign> vitalSignList = vitalSignRepository.findAll();
        assertThat(vitalSignList).hasSize(databaseSizeBeforeUpdate);
        VitalSign testVitalSign = vitalSignList.get(vitalSignList.size() - 1);
        assertThat(testVitalSign.getWeightInPounds()).isEqualTo(UPDATED_WEIGHT_IN_POUNDS);
        assertThat(testVitalSign.getHeightInInches()).isEqualTo(DEFAULT_HEIGHT_IN_INCHES);
        assertThat(testVitalSign.getBmi()).isEqualTo(DEFAULT_BMI);
        assertThat(testVitalSign.getGlassOfWater()).isEqualTo(DEFAULT_GLASS_OF_WATER);
        assertThat(testVitalSign.getSystolic()).isEqualTo(DEFAULT_SYSTOLIC);
        assertThat(testVitalSign.getDiastolic()).isEqualTo(UPDATED_DIASTOLIC);
        assertThat(testVitalSign.getCurrentBloodSugar()).isEqualTo(UPDATED_CURRENT_BLOOD_SUGAR);
        assertThat(testVitalSign.getLipidProfile()).isEqualTo(DEFAULT_LIPID_PROFILE);
    }

    @Test
    @Transactional
    void fullUpdateVitalSignWithPatch() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        int databaseSizeBeforeUpdate = vitalSignRepository.findAll().size();

        // Update the vitalSign using partial update
        VitalSign partialUpdatedVitalSign = new VitalSign();
        partialUpdatedVitalSign.setId(vitalSign.getId());

        partialUpdatedVitalSign
            .weightInPounds(UPDATED_WEIGHT_IN_POUNDS)
            .heightInInches(UPDATED_HEIGHT_IN_INCHES)
            .bmi(UPDATED_BMI)
            .glassOfWater(UPDATED_GLASS_OF_WATER)
            .systolic(UPDATED_SYSTOLIC)
            .diastolic(UPDATED_DIASTOLIC)
            .currentBloodSugar(UPDATED_CURRENT_BLOOD_SUGAR)
            .lipidProfile(UPDATED_LIPID_PROFILE);

        restVitalSignMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVitalSign.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVitalSign))
            )
            .andExpect(status().isOk());

        // Validate the VitalSign in the database
        List<VitalSign> vitalSignList = vitalSignRepository.findAll();
        assertThat(vitalSignList).hasSize(databaseSizeBeforeUpdate);
        VitalSign testVitalSign = vitalSignList.get(vitalSignList.size() - 1);
        assertThat(testVitalSign.getWeightInPounds()).isEqualTo(UPDATED_WEIGHT_IN_POUNDS);
        assertThat(testVitalSign.getHeightInInches()).isEqualTo(UPDATED_HEIGHT_IN_INCHES);
        assertThat(testVitalSign.getBmi()).isEqualTo(UPDATED_BMI);
        assertThat(testVitalSign.getGlassOfWater()).isEqualTo(UPDATED_GLASS_OF_WATER);
        assertThat(testVitalSign.getSystolic()).isEqualTo(UPDATED_SYSTOLIC);
        assertThat(testVitalSign.getDiastolic()).isEqualTo(UPDATED_DIASTOLIC);
        assertThat(testVitalSign.getCurrentBloodSugar()).isEqualTo(UPDATED_CURRENT_BLOOD_SUGAR);
        assertThat(testVitalSign.getLipidProfile()).isEqualTo(UPDATED_LIPID_PROFILE);
    }

    @Test
    @Transactional
    void patchNonExistingVitalSign() throws Exception {
        int databaseSizeBeforeUpdate = vitalSignRepository.findAll().size();
        vitalSign.setId(count.incrementAndGet());

        // Create the VitalSign
        VitalSignDTO vitalSignDTO = vitalSignMapper.toDto(vitalSign);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVitalSignMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vitalSignDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vitalSignDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VitalSign in the database
        List<VitalSign> vitalSignList = vitalSignRepository.findAll();
        assertThat(vitalSignList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVitalSign() throws Exception {
        int databaseSizeBeforeUpdate = vitalSignRepository.findAll().size();
        vitalSign.setId(count.incrementAndGet());

        // Create the VitalSign
        VitalSignDTO vitalSignDTO = vitalSignMapper.toDto(vitalSign);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVitalSignMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vitalSignDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VitalSign in the database
        List<VitalSign> vitalSignList = vitalSignRepository.findAll();
        assertThat(vitalSignList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVitalSign() throws Exception {
        int databaseSizeBeforeUpdate = vitalSignRepository.findAll().size();
        vitalSign.setId(count.incrementAndGet());

        // Create the VitalSign
        VitalSignDTO vitalSignDTO = vitalSignMapper.toDto(vitalSign);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVitalSignMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(vitalSignDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VitalSign in the database
        List<VitalSign> vitalSignList = vitalSignRepository.findAll();
        assertThat(vitalSignList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVitalSign() throws Exception {
        // Initialize the database
        vitalSignRepository.saveAndFlush(vitalSign);

        int databaseSizeBeforeDelete = vitalSignRepository.findAll().size();

        // Delete the vitalSign
        restVitalSignMockMvc
            .perform(delete(ENTITY_API_URL_ID, vitalSign.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<VitalSign> vitalSignList = vitalSignRepository.findAll();
        assertThat(vitalSignList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
