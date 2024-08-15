package sn.ouznoreyni.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.ouznoreyni.domain.CoursAsserts.*;
import static sn.ouznoreyni.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import sn.ouznoreyni.IntegrationTest;
import sn.ouznoreyni.domain.Cours;
import sn.ouznoreyni.domain.Departement;
import sn.ouznoreyni.domain.Etudiant;
import sn.ouznoreyni.domain.Professeur;
import sn.ouznoreyni.repository.CoursRepository;
import sn.ouznoreyni.service.CoursService;
import sn.ouznoreyni.service.dto.CoursDTO;
import sn.ouznoreyni.service.mapper.CoursMapper;

/**
 * Integration tests for the {@link CoursResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CoursResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_INTITULE = "AAAAAAAAAA";
    private static final String UPDATED_INTITULE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_CREDITS = 1;
    private static final Integer UPDATED_CREDITS = 2;
    private static final Integer SMALLER_CREDITS = 1 - 1;

    private static final String ENTITY_API_URL = "/api/cours";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CoursRepository coursRepository;

    @Mock
    private CoursRepository coursRepositoryMock;

    @Autowired
    private CoursMapper coursMapper;

    @Mock
    private CoursService coursServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCoursMockMvc;

    private Cours cours;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cours createEntity(EntityManager em) {
        Cours cours = new Cours().code(DEFAULT_CODE).intitule(DEFAULT_INTITULE).description(DEFAULT_DESCRIPTION).credits(DEFAULT_CREDITS);
        return cours;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cours createUpdatedEntity(EntityManager em) {
        Cours cours = new Cours().code(UPDATED_CODE).intitule(UPDATED_INTITULE).description(UPDATED_DESCRIPTION).credits(UPDATED_CREDITS);
        return cours;
    }

    @BeforeEach
    public void initTest() {
        cours = createEntity(em);
    }

    @Test
    @Transactional
    void createCours() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Cours
        CoursDTO coursDTO = coursMapper.toDto(cours);
        var returnedCoursDTO = om.readValue(
            restCoursMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coursDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CoursDTO.class
        );

        // Validate the Cours in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCours = coursMapper.toEntity(returnedCoursDTO);
        assertCoursUpdatableFieldsEquals(returnedCours, getPersistedCours(returnedCours));
    }

    @Test
    @Transactional
    void createCoursWithExistingId() throws Exception {
        // Create the Cours with an existing ID
        cours.setId(1L);
        CoursDTO coursDTO = coursMapper.toDto(cours);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCoursMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coursDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Cours in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cours.setCode(null);

        // Create the Cours, which fails.
        CoursDTO coursDTO = coursMapper.toDto(cours);

        restCoursMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coursDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIntituleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cours.setIntitule(null);

        // Create the Cours, which fails.
        CoursDTO coursDTO = coursMapper.toDto(cours);

        restCoursMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coursDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreditsIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cours.setCredits(null);

        // Create the Cours, which fails.
        CoursDTO coursDTO = coursMapper.toDto(cours);

        restCoursMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coursDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCours() throws Exception {
        // Initialize the database
        coursRepository.saveAndFlush(cours);

        // Get all the coursList
        restCoursMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cours.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].intitule").value(hasItem(DEFAULT_INTITULE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].credits").value(hasItem(DEFAULT_CREDITS)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCoursWithEagerRelationshipsIsEnabled() throws Exception {
        when(coursServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCoursMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(coursServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCoursWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(coursServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCoursMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(coursRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCours() throws Exception {
        // Initialize the database
        coursRepository.saveAndFlush(cours);

        // Get the cours
        restCoursMockMvc
            .perform(get(ENTITY_API_URL_ID, cours.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cours.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.intitule").value(DEFAULT_INTITULE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.credits").value(DEFAULT_CREDITS));
    }

    @Test
    @Transactional
    void getCoursByIdFiltering() throws Exception {
        // Initialize the database
        coursRepository.saveAndFlush(cours);

        Long id = cours.getId();

        defaultCoursFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCoursFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCoursFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCoursByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        coursRepository.saveAndFlush(cours);

        // Get all the coursList where code equals to
        defaultCoursFiltering("code.equals=" + DEFAULT_CODE, "code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllCoursByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        coursRepository.saveAndFlush(cours);

        // Get all the coursList where code in
        defaultCoursFiltering("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE, "code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllCoursByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        coursRepository.saveAndFlush(cours);

        // Get all the coursList where code is not null
        defaultCoursFiltering("code.specified=true", "code.specified=false");
    }

    @Test
    @Transactional
    void getAllCoursByCodeContainsSomething() throws Exception {
        // Initialize the database
        coursRepository.saveAndFlush(cours);

        // Get all the coursList where code contains
        defaultCoursFiltering("code.contains=" + DEFAULT_CODE, "code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllCoursByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        coursRepository.saveAndFlush(cours);

        // Get all the coursList where code does not contain
        defaultCoursFiltering("code.doesNotContain=" + UPDATED_CODE, "code.doesNotContain=" + DEFAULT_CODE);
    }

    @Test
    @Transactional
    void getAllCoursByIntituleIsEqualToSomething() throws Exception {
        // Initialize the database
        coursRepository.saveAndFlush(cours);

        // Get all the coursList where intitule equals to
        defaultCoursFiltering("intitule.equals=" + DEFAULT_INTITULE, "intitule.equals=" + UPDATED_INTITULE);
    }

    @Test
    @Transactional
    void getAllCoursByIntituleIsInShouldWork() throws Exception {
        // Initialize the database
        coursRepository.saveAndFlush(cours);

        // Get all the coursList where intitule in
        defaultCoursFiltering("intitule.in=" + DEFAULT_INTITULE + "," + UPDATED_INTITULE, "intitule.in=" + UPDATED_INTITULE);
    }

    @Test
    @Transactional
    void getAllCoursByIntituleIsNullOrNotNull() throws Exception {
        // Initialize the database
        coursRepository.saveAndFlush(cours);

        // Get all the coursList where intitule is not null
        defaultCoursFiltering("intitule.specified=true", "intitule.specified=false");
    }

    @Test
    @Transactional
    void getAllCoursByIntituleContainsSomething() throws Exception {
        // Initialize the database
        coursRepository.saveAndFlush(cours);

        // Get all the coursList where intitule contains
        defaultCoursFiltering("intitule.contains=" + DEFAULT_INTITULE, "intitule.contains=" + UPDATED_INTITULE);
    }

    @Test
    @Transactional
    void getAllCoursByIntituleNotContainsSomething() throws Exception {
        // Initialize the database
        coursRepository.saveAndFlush(cours);

        // Get all the coursList where intitule does not contain
        defaultCoursFiltering("intitule.doesNotContain=" + UPDATED_INTITULE, "intitule.doesNotContain=" + DEFAULT_INTITULE);
    }

    @Test
    @Transactional
    void getAllCoursByCreditsIsEqualToSomething() throws Exception {
        // Initialize the database
        coursRepository.saveAndFlush(cours);

        // Get all the coursList where credits equals to
        defaultCoursFiltering("credits.equals=" + DEFAULT_CREDITS, "credits.equals=" + UPDATED_CREDITS);
    }

    @Test
    @Transactional
    void getAllCoursByCreditsIsInShouldWork() throws Exception {
        // Initialize the database
        coursRepository.saveAndFlush(cours);

        // Get all the coursList where credits in
        defaultCoursFiltering("credits.in=" + DEFAULT_CREDITS + "," + UPDATED_CREDITS, "credits.in=" + UPDATED_CREDITS);
    }

    @Test
    @Transactional
    void getAllCoursByCreditsIsNullOrNotNull() throws Exception {
        // Initialize the database
        coursRepository.saveAndFlush(cours);

        // Get all the coursList where credits is not null
        defaultCoursFiltering("credits.specified=true", "credits.specified=false");
    }

    @Test
    @Transactional
    void getAllCoursByCreditsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        coursRepository.saveAndFlush(cours);

        // Get all the coursList where credits is greater than or equal to
        defaultCoursFiltering("credits.greaterThanOrEqual=" + DEFAULT_CREDITS, "credits.greaterThanOrEqual=" + (DEFAULT_CREDITS + 1));
    }

    @Test
    @Transactional
    void getAllCoursByCreditsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        coursRepository.saveAndFlush(cours);

        // Get all the coursList where credits is less than or equal to
        defaultCoursFiltering("credits.lessThanOrEqual=" + DEFAULT_CREDITS, "credits.lessThanOrEqual=" + SMALLER_CREDITS);
    }

    @Test
    @Transactional
    void getAllCoursByCreditsIsLessThanSomething() throws Exception {
        // Initialize the database
        coursRepository.saveAndFlush(cours);

        // Get all the coursList where credits is less than
        defaultCoursFiltering("credits.lessThan=" + (DEFAULT_CREDITS + 1), "credits.lessThan=" + DEFAULT_CREDITS);
    }

    @Test
    @Transactional
    void getAllCoursByCreditsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        coursRepository.saveAndFlush(cours);

        // Get all the coursList where credits is greater than
        defaultCoursFiltering("credits.greaterThan=" + SMALLER_CREDITS, "credits.greaterThan=" + DEFAULT_CREDITS);
    }

    @Test
    @Transactional
    void getAllCoursByDepartementIsEqualToSomething() throws Exception {
        Departement departement;
        if (TestUtil.findAll(em, Departement.class).isEmpty()) {
            coursRepository.saveAndFlush(cours);
            departement = DepartementResourceIT.createEntity(em);
        } else {
            departement = TestUtil.findAll(em, Departement.class).get(0);
        }
        em.persist(departement);
        em.flush();
        cours.setDepartement(departement);
        coursRepository.saveAndFlush(cours);
        Long departementId = departement.getId();
        // Get all the coursList where departement equals to departementId
        defaultCoursShouldBeFound("departementId.equals=" + departementId);

        // Get all the coursList where departement equals to (departementId + 1)
        defaultCoursShouldNotBeFound("departementId.equals=" + (departementId + 1));
    }

    @Test
    @Transactional
    void getAllCoursByProfesseurIsEqualToSomething() throws Exception {
        Professeur professeur;
        if (TestUtil.findAll(em, Professeur.class).isEmpty()) {
            coursRepository.saveAndFlush(cours);
            professeur = ProfesseurResourceIT.createEntity(em);
        } else {
            professeur = TestUtil.findAll(em, Professeur.class).get(0);
        }
        em.persist(professeur);
        em.flush();
        cours.setProfesseur(professeur);
        coursRepository.saveAndFlush(cours);
        Long professeurId = professeur.getId();
        // Get all the coursList where professeur equals to professeurId
        defaultCoursShouldBeFound("professeurId.equals=" + professeurId);

        // Get all the coursList where professeur equals to (professeurId + 1)
        defaultCoursShouldNotBeFound("professeurId.equals=" + (professeurId + 1));
    }

    @Test
    @Transactional
    void getAllCoursByEtudiantsIsEqualToSomething() throws Exception {
        Etudiant etudiants;
        if (TestUtil.findAll(em, Etudiant.class).isEmpty()) {
            coursRepository.saveAndFlush(cours);
            etudiants = EtudiantResourceIT.createEntity(em);
        } else {
            etudiants = TestUtil.findAll(em, Etudiant.class).get(0);
        }
        em.persist(etudiants);
        em.flush();
        cours.addEtudiants(etudiants);
        coursRepository.saveAndFlush(cours);
        Long etudiantsId = etudiants.getId();
        // Get all the coursList where etudiants equals to etudiantsId
        defaultCoursShouldBeFound("etudiantsId.equals=" + etudiantsId);

        // Get all the coursList where etudiants equals to (etudiantsId + 1)
        defaultCoursShouldNotBeFound("etudiantsId.equals=" + (etudiantsId + 1));
    }

    private void defaultCoursFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCoursShouldBeFound(shouldBeFound);
        defaultCoursShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCoursShouldBeFound(String filter) throws Exception {
        restCoursMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cours.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].intitule").value(hasItem(DEFAULT_INTITULE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].credits").value(hasItem(DEFAULT_CREDITS)));

        // Check, that the count call also returns 1
        restCoursMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCoursShouldNotBeFound(String filter) throws Exception {
        restCoursMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCoursMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCours() throws Exception {
        // Get the cours
        restCoursMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCours() throws Exception {
        // Initialize the database
        coursRepository.saveAndFlush(cours);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cours
        Cours updatedCours = coursRepository.findById(cours.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCours are not directly saved in db
        em.detach(updatedCours);
        updatedCours.code(UPDATED_CODE).intitule(UPDATED_INTITULE).description(UPDATED_DESCRIPTION).credits(UPDATED_CREDITS);
        CoursDTO coursDTO = coursMapper.toDto(updatedCours);

        restCoursMockMvc
            .perform(
                put(ENTITY_API_URL_ID, coursDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coursDTO))
            )
            .andExpect(status().isOk());

        // Validate the Cours in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCoursToMatchAllProperties(updatedCours);
    }

    @Test
    @Transactional
    void putNonExistingCours() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cours.setId(longCount.incrementAndGet());

        // Create the Cours
        CoursDTO coursDTO = coursMapper.toDto(cours);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCoursMockMvc
            .perform(
                put(ENTITY_API_URL_ID, coursDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coursDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cours in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCours() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cours.setId(longCount.incrementAndGet());

        // Create the Cours
        CoursDTO coursDTO = coursMapper.toDto(cours);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoursMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(coursDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cours in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCours() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cours.setId(longCount.incrementAndGet());

        // Create the Cours
        CoursDTO coursDTO = coursMapper.toDto(cours);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoursMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coursDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cours in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCoursWithPatch() throws Exception {
        // Initialize the database
        coursRepository.saveAndFlush(cours);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cours using partial update
        Cours partialUpdatedCours = new Cours();
        partialUpdatedCours.setId(cours.getId());

        partialUpdatedCours.code(UPDATED_CODE).intitule(UPDATED_INTITULE).credits(UPDATED_CREDITS);

        restCoursMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCours.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCours))
            )
            .andExpect(status().isOk());

        // Validate the Cours in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCoursUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCours, cours), getPersistedCours(cours));
    }

    @Test
    @Transactional
    void fullUpdateCoursWithPatch() throws Exception {
        // Initialize the database
        coursRepository.saveAndFlush(cours);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cours using partial update
        Cours partialUpdatedCours = new Cours();
        partialUpdatedCours.setId(cours.getId());

        partialUpdatedCours.code(UPDATED_CODE).intitule(UPDATED_INTITULE).description(UPDATED_DESCRIPTION).credits(UPDATED_CREDITS);

        restCoursMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCours.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCours))
            )
            .andExpect(status().isOk());

        // Validate the Cours in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCoursUpdatableFieldsEquals(partialUpdatedCours, getPersistedCours(partialUpdatedCours));
    }

    @Test
    @Transactional
    void patchNonExistingCours() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cours.setId(longCount.incrementAndGet());

        // Create the Cours
        CoursDTO coursDTO = coursMapper.toDto(cours);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCoursMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, coursDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(coursDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cours in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCours() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cours.setId(longCount.incrementAndGet());

        // Create the Cours
        CoursDTO coursDTO = coursMapper.toDto(cours);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoursMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(coursDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cours in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCours() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cours.setId(longCount.incrementAndGet());

        // Create the Cours
        CoursDTO coursDTO = coursMapper.toDto(cours);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoursMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(coursDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cours in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCours() throws Exception {
        // Initialize the database
        coursRepository.saveAndFlush(cours);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cours
        restCoursMockMvc
            .perform(delete(ENTITY_API_URL_ID, cours.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return coursRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Cours getPersistedCours(Cours cours) {
        return coursRepository.findById(cours.getId()).orElseThrow();
    }

    protected void assertPersistedCoursToMatchAllProperties(Cours expectedCours) {
        assertCoursAllPropertiesEquals(expectedCours, getPersistedCours(expectedCours));
    }

    protected void assertPersistedCoursToMatchUpdatableProperties(Cours expectedCours) {
        assertCoursAllUpdatablePropertiesEquals(expectedCours, getPersistedCours(expectedCours));
    }
}
