package sn.ouznoreyni.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.ouznoreyni.domain.EtudiantAsserts.*;
import static sn.ouznoreyni.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
import sn.ouznoreyni.domain.User;
import sn.ouznoreyni.repository.EtudiantRepository;
import sn.ouznoreyni.service.EtudiantService;
import sn.ouznoreyni.service.dto.EtudiantDTO;
import sn.ouznoreyni.service.mapper.EtudiantMapper;

/**
 * Integration tests for the {@link EtudiantResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class EtudiantResourceIT {

    private static final String DEFAULT_NUMERO_ETUDIANT = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_ETUDIANT = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_NAISSANCE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_NAISSANCE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_NAISSANCE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_FILIERE = "AAAAAAAAAA";
    private static final String UPDATED_FILIERE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/etudiants";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Mock
    private EtudiantRepository etudiantRepositoryMock;

    @Autowired
    private EtudiantMapper etudiantMapper;

    @Mock
    private EtudiantService etudiantServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEtudiantMockMvc;

    private Etudiant etudiant;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Etudiant createEntity(EntityManager em) {
        Etudiant etudiant = new Etudiant()
            .numeroEtudiant(DEFAULT_NUMERO_ETUDIANT)
            .dateNaissance(DEFAULT_DATE_NAISSANCE)
            .filiere(DEFAULT_FILIERE);
        return etudiant;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Etudiant createUpdatedEntity(EntityManager em) {
        Etudiant etudiant = new Etudiant()
            .numeroEtudiant(UPDATED_NUMERO_ETUDIANT)
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .filiere(UPDATED_FILIERE);
        return etudiant;
    }

    @BeforeEach
    public void initTest() {
        etudiant = createEntity(em);
    }

    @Test
    @Transactional
    void createEtudiant() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Etudiant
        EtudiantDTO etudiantDTO = etudiantMapper.toDto(etudiant);
        var returnedEtudiantDTO = om.readValue(
            restEtudiantMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(etudiantDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EtudiantDTO.class
        );

        // Validate the Etudiant in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEtudiant = etudiantMapper.toEntity(returnedEtudiantDTO);
        assertEtudiantUpdatableFieldsEquals(returnedEtudiant, getPersistedEtudiant(returnedEtudiant));
    }

    @Test
    @Transactional
    void createEtudiantWithExistingId() throws Exception {
        // Create the Etudiant with an existing ID
        etudiant.setId(1L);
        EtudiantDTO etudiantDTO = etudiantMapper.toDto(etudiant);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEtudiantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(etudiantDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Etudiant in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNumeroEtudiantIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        etudiant.setNumeroEtudiant(null);

        // Create the Etudiant, which fails.
        EtudiantDTO etudiantDTO = etudiantMapper.toDto(etudiant);

        restEtudiantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(etudiantDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateNaissanceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        etudiant.setDateNaissance(null);

        // Create the Etudiant, which fails.
        EtudiantDTO etudiantDTO = etudiantMapper.toDto(etudiant);

        restEtudiantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(etudiantDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFiliereIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        etudiant.setFiliere(null);

        // Create the Etudiant, which fails.
        EtudiantDTO etudiantDTO = etudiantMapper.toDto(etudiant);

        restEtudiantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(etudiantDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEtudiants() throws Exception {
        // Initialize the database
        etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList
        restEtudiantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(etudiant.getId().intValue())))
            .andExpect(jsonPath("$.[*].numeroEtudiant").value(hasItem(DEFAULT_NUMERO_ETUDIANT)))
            .andExpect(jsonPath("$.[*].dateNaissance").value(hasItem(DEFAULT_DATE_NAISSANCE.toString())))
            .andExpect(jsonPath("$.[*].filiere").value(hasItem(DEFAULT_FILIERE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEtudiantsWithEagerRelationshipsIsEnabled() throws Exception {
        when(etudiantServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEtudiantMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(etudiantServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEtudiantsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(etudiantServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEtudiantMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(etudiantRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getEtudiant() throws Exception {
        // Initialize the database
        etudiantRepository.saveAndFlush(etudiant);

        // Get the etudiant
        restEtudiantMockMvc
            .perform(get(ENTITY_API_URL_ID, etudiant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(etudiant.getId().intValue()))
            .andExpect(jsonPath("$.numeroEtudiant").value(DEFAULT_NUMERO_ETUDIANT))
            .andExpect(jsonPath("$.dateNaissance").value(DEFAULT_DATE_NAISSANCE.toString()))
            .andExpect(jsonPath("$.filiere").value(DEFAULT_FILIERE));
    }

    @Test
    @Transactional
    void getEtudiantsByIdFiltering() throws Exception {
        // Initialize the database
        etudiantRepository.saveAndFlush(etudiant);

        Long id = etudiant.getId();

        defaultEtudiantFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultEtudiantFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultEtudiantFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEtudiantsByNumeroEtudiantIsEqualToSomething() throws Exception {
        // Initialize the database
        etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where numeroEtudiant equals to
        defaultEtudiantFiltering("numeroEtudiant.equals=" + DEFAULT_NUMERO_ETUDIANT, "numeroEtudiant.equals=" + UPDATED_NUMERO_ETUDIANT);
    }

    @Test
    @Transactional
    void getAllEtudiantsByNumeroEtudiantIsInShouldWork() throws Exception {
        // Initialize the database
        etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where numeroEtudiant in
        defaultEtudiantFiltering(
            "numeroEtudiant.in=" + DEFAULT_NUMERO_ETUDIANT + "," + UPDATED_NUMERO_ETUDIANT,
            "numeroEtudiant.in=" + UPDATED_NUMERO_ETUDIANT
        );
    }

    @Test
    @Transactional
    void getAllEtudiantsByNumeroEtudiantIsNullOrNotNull() throws Exception {
        // Initialize the database
        etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where numeroEtudiant is not null
        defaultEtudiantFiltering("numeroEtudiant.specified=true", "numeroEtudiant.specified=false");
    }

    @Test
    @Transactional
    void getAllEtudiantsByNumeroEtudiantContainsSomething() throws Exception {
        // Initialize the database
        etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where numeroEtudiant contains
        defaultEtudiantFiltering(
            "numeroEtudiant.contains=" + DEFAULT_NUMERO_ETUDIANT,
            "numeroEtudiant.contains=" + UPDATED_NUMERO_ETUDIANT
        );
    }

    @Test
    @Transactional
    void getAllEtudiantsByNumeroEtudiantNotContainsSomething() throws Exception {
        // Initialize the database
        etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where numeroEtudiant does not contain
        defaultEtudiantFiltering(
            "numeroEtudiant.doesNotContain=" + UPDATED_NUMERO_ETUDIANT,
            "numeroEtudiant.doesNotContain=" + DEFAULT_NUMERO_ETUDIANT
        );
    }

    @Test
    @Transactional
    void getAllEtudiantsByDateNaissanceIsEqualToSomething() throws Exception {
        // Initialize the database
        etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where dateNaissance equals to
        defaultEtudiantFiltering("dateNaissance.equals=" + DEFAULT_DATE_NAISSANCE, "dateNaissance.equals=" + UPDATED_DATE_NAISSANCE);
    }

    @Test
    @Transactional
    void getAllEtudiantsByDateNaissanceIsInShouldWork() throws Exception {
        // Initialize the database
        etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where dateNaissance in
        defaultEtudiantFiltering(
            "dateNaissance.in=" + DEFAULT_DATE_NAISSANCE + "," + UPDATED_DATE_NAISSANCE,
            "dateNaissance.in=" + UPDATED_DATE_NAISSANCE
        );
    }

    @Test
    @Transactional
    void getAllEtudiantsByDateNaissanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where dateNaissance is not null
        defaultEtudiantFiltering("dateNaissance.specified=true", "dateNaissance.specified=false");
    }

    @Test
    @Transactional
    void getAllEtudiantsByDateNaissanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where dateNaissance is greater than or equal to
        defaultEtudiantFiltering(
            "dateNaissance.greaterThanOrEqual=" + DEFAULT_DATE_NAISSANCE,
            "dateNaissance.greaterThanOrEqual=" + UPDATED_DATE_NAISSANCE
        );
    }

    @Test
    @Transactional
    void getAllEtudiantsByDateNaissanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where dateNaissance is less than or equal to
        defaultEtudiantFiltering(
            "dateNaissance.lessThanOrEqual=" + DEFAULT_DATE_NAISSANCE,
            "dateNaissance.lessThanOrEqual=" + SMALLER_DATE_NAISSANCE
        );
    }

    @Test
    @Transactional
    void getAllEtudiantsByDateNaissanceIsLessThanSomething() throws Exception {
        // Initialize the database
        etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where dateNaissance is less than
        defaultEtudiantFiltering("dateNaissance.lessThan=" + UPDATED_DATE_NAISSANCE, "dateNaissance.lessThan=" + DEFAULT_DATE_NAISSANCE);
    }

    @Test
    @Transactional
    void getAllEtudiantsByDateNaissanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where dateNaissance is greater than
        defaultEtudiantFiltering(
            "dateNaissance.greaterThan=" + SMALLER_DATE_NAISSANCE,
            "dateNaissance.greaterThan=" + DEFAULT_DATE_NAISSANCE
        );
    }

    @Test
    @Transactional
    void getAllEtudiantsByFiliereIsEqualToSomething() throws Exception {
        // Initialize the database
        etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where filiere equals to
        defaultEtudiantFiltering("filiere.equals=" + DEFAULT_FILIERE, "filiere.equals=" + UPDATED_FILIERE);
    }

    @Test
    @Transactional
    void getAllEtudiantsByFiliereIsInShouldWork() throws Exception {
        // Initialize the database
        etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where filiere in
        defaultEtudiantFiltering("filiere.in=" + DEFAULT_FILIERE + "," + UPDATED_FILIERE, "filiere.in=" + UPDATED_FILIERE);
    }

    @Test
    @Transactional
    void getAllEtudiantsByFiliereIsNullOrNotNull() throws Exception {
        // Initialize the database
        etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where filiere is not null
        defaultEtudiantFiltering("filiere.specified=true", "filiere.specified=false");
    }

    @Test
    @Transactional
    void getAllEtudiantsByFiliereContainsSomething() throws Exception {
        // Initialize the database
        etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where filiere contains
        defaultEtudiantFiltering("filiere.contains=" + DEFAULT_FILIERE, "filiere.contains=" + UPDATED_FILIERE);
    }

    @Test
    @Transactional
    void getAllEtudiantsByFiliereNotContainsSomething() throws Exception {
        // Initialize the database
        etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where filiere does not contain
        defaultEtudiantFiltering("filiere.doesNotContain=" + UPDATED_FILIERE, "filiere.doesNotContain=" + DEFAULT_FILIERE);
    }

    @Test
    @Transactional
    void getAllEtudiantsByUserIsEqualToSomething() throws Exception {
        User user;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            etudiantRepository.saveAndFlush(etudiant);
            user = UserResourceIT.createEntity(em);
        } else {
            user = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(user);
        em.flush();
        etudiant.setUser(user);
        etudiantRepository.saveAndFlush(etudiant);
        Long userId = user.getId();
        // Get all the etudiantList where user equals to userId
        defaultEtudiantShouldBeFound("userId.equals=" + userId);

        // Get all the etudiantList where user equals to (userId + 1)
        defaultEtudiantShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllEtudiantsByDepartementIsEqualToSomething() throws Exception {
        Departement departement;
        if (TestUtil.findAll(em, Departement.class).isEmpty()) {
            etudiantRepository.saveAndFlush(etudiant);
            departement = DepartementResourceIT.createEntity(em);
        } else {
            departement = TestUtil.findAll(em, Departement.class).get(0);
        }
        em.persist(departement);
        em.flush();
        etudiant.setDepartement(departement);
        etudiantRepository.saveAndFlush(etudiant);
        Long departementId = departement.getId();
        // Get all the etudiantList where departement equals to departementId
        defaultEtudiantShouldBeFound("departementId.equals=" + departementId);

        // Get all the etudiantList where departement equals to (departementId + 1)
        defaultEtudiantShouldNotBeFound("departementId.equals=" + (departementId + 1));
    }

    @Test
    @Transactional
    void getAllEtudiantsByCoursIsEqualToSomething() throws Exception {
        Cours cours;
        if (TestUtil.findAll(em, Cours.class).isEmpty()) {
            etudiantRepository.saveAndFlush(etudiant);
            cours = CoursResourceIT.createEntity(em);
        } else {
            cours = TestUtil.findAll(em, Cours.class).get(0);
        }
        em.persist(cours);
        em.flush();
        etudiant.addCours(cours);
        etudiantRepository.saveAndFlush(etudiant);
        Long coursId = cours.getId();
        // Get all the etudiantList where cours equals to coursId
        defaultEtudiantShouldBeFound("coursId.equals=" + coursId);

        // Get all the etudiantList where cours equals to (coursId + 1)
        defaultEtudiantShouldNotBeFound("coursId.equals=" + (coursId + 1));
    }

    private void defaultEtudiantFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultEtudiantShouldBeFound(shouldBeFound);
        defaultEtudiantShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEtudiantShouldBeFound(String filter) throws Exception {
        restEtudiantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(etudiant.getId().intValue())))
            .andExpect(jsonPath("$.[*].numeroEtudiant").value(hasItem(DEFAULT_NUMERO_ETUDIANT)))
            .andExpect(jsonPath("$.[*].dateNaissance").value(hasItem(DEFAULT_DATE_NAISSANCE.toString())))
            .andExpect(jsonPath("$.[*].filiere").value(hasItem(DEFAULT_FILIERE)));

        // Check, that the count call also returns 1
        restEtudiantMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEtudiantShouldNotBeFound(String filter) throws Exception {
        restEtudiantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEtudiantMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEtudiant() throws Exception {
        // Get the etudiant
        restEtudiantMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEtudiant() throws Exception {
        // Initialize the database
        etudiantRepository.saveAndFlush(etudiant);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the etudiant
        Etudiant updatedEtudiant = etudiantRepository.findById(etudiant.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEtudiant are not directly saved in db
        em.detach(updatedEtudiant);
        updatedEtudiant.numeroEtudiant(UPDATED_NUMERO_ETUDIANT).dateNaissance(UPDATED_DATE_NAISSANCE).filiere(UPDATED_FILIERE);
        EtudiantDTO etudiantDTO = etudiantMapper.toDto(updatedEtudiant);

        restEtudiantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, etudiantDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(etudiantDTO))
            )
            .andExpect(status().isOk());

        // Validate the Etudiant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEtudiantToMatchAllProperties(updatedEtudiant);
    }

    @Test
    @Transactional
    void putNonExistingEtudiant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        etudiant.setId(longCount.incrementAndGet());

        // Create the Etudiant
        EtudiantDTO etudiantDTO = etudiantMapper.toDto(etudiant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEtudiantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, etudiantDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(etudiantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Etudiant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEtudiant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        etudiant.setId(longCount.incrementAndGet());

        // Create the Etudiant
        EtudiantDTO etudiantDTO = etudiantMapper.toDto(etudiant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEtudiantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(etudiantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Etudiant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEtudiant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        etudiant.setId(longCount.incrementAndGet());

        // Create the Etudiant
        EtudiantDTO etudiantDTO = etudiantMapper.toDto(etudiant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEtudiantMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(etudiantDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Etudiant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEtudiantWithPatch() throws Exception {
        // Initialize the database
        etudiantRepository.saveAndFlush(etudiant);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the etudiant using partial update
        Etudiant partialUpdatedEtudiant = new Etudiant();
        partialUpdatedEtudiant.setId(etudiant.getId());

        partialUpdatedEtudiant.filiere(UPDATED_FILIERE);

        restEtudiantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEtudiant.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEtudiant))
            )
            .andExpect(status().isOk());

        // Validate the Etudiant in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEtudiantUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedEtudiant, etudiant), getPersistedEtudiant(etudiant));
    }

    @Test
    @Transactional
    void fullUpdateEtudiantWithPatch() throws Exception {
        // Initialize the database
        etudiantRepository.saveAndFlush(etudiant);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the etudiant using partial update
        Etudiant partialUpdatedEtudiant = new Etudiant();
        partialUpdatedEtudiant.setId(etudiant.getId());

        partialUpdatedEtudiant.numeroEtudiant(UPDATED_NUMERO_ETUDIANT).dateNaissance(UPDATED_DATE_NAISSANCE).filiere(UPDATED_FILIERE);

        restEtudiantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEtudiant.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEtudiant))
            )
            .andExpect(status().isOk());

        // Validate the Etudiant in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEtudiantUpdatableFieldsEquals(partialUpdatedEtudiant, getPersistedEtudiant(partialUpdatedEtudiant));
    }

    @Test
    @Transactional
    void patchNonExistingEtudiant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        etudiant.setId(longCount.incrementAndGet());

        // Create the Etudiant
        EtudiantDTO etudiantDTO = etudiantMapper.toDto(etudiant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEtudiantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, etudiantDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(etudiantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Etudiant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEtudiant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        etudiant.setId(longCount.incrementAndGet());

        // Create the Etudiant
        EtudiantDTO etudiantDTO = etudiantMapper.toDto(etudiant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEtudiantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(etudiantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Etudiant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEtudiant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        etudiant.setId(longCount.incrementAndGet());

        // Create the Etudiant
        EtudiantDTO etudiantDTO = etudiantMapper.toDto(etudiant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEtudiantMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(etudiantDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Etudiant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEtudiant() throws Exception {
        // Initialize the database
        etudiantRepository.saveAndFlush(etudiant);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the etudiant
        restEtudiantMockMvc
            .perform(delete(ENTITY_API_URL_ID, etudiant.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return etudiantRepository.count();
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

    protected Etudiant getPersistedEtudiant(Etudiant etudiant) {
        return etudiantRepository.findById(etudiant.getId()).orElseThrow();
    }

    protected void assertPersistedEtudiantToMatchAllProperties(Etudiant expectedEtudiant) {
        assertEtudiantAllPropertiesEquals(expectedEtudiant, getPersistedEtudiant(expectedEtudiant));
    }

    protected void assertPersistedEtudiantToMatchUpdatableProperties(Etudiant expectedEtudiant) {
        assertEtudiantAllUpdatablePropertiesEquals(expectedEtudiant, getPersistedEtudiant(expectedEtudiant));
    }
}
