package sn.ouznoreyni.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.ouznoreyni.domain.ProfesseurAsserts.*;
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
import sn.ouznoreyni.domain.Departement;
import sn.ouznoreyni.domain.Professeur;
import sn.ouznoreyni.domain.User;
import sn.ouznoreyni.repository.ProfesseurRepository;
import sn.ouznoreyni.service.ProfesseurService;
import sn.ouznoreyni.service.dto.ProfesseurDTO;
import sn.ouznoreyni.service.mapper.ProfesseurMapper;

/**
 * Integration tests for the {@link ProfesseurResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProfesseurResourceIT {

    private static final String DEFAULT_NUMERO_EMPLOYE = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_EMPLOYE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_EMBAUCHE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_EMBAUCHE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_EMBAUCHE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_SPECIALITE = "AAAAAAAAAA";
    private static final String UPDATED_SPECIALITE = "BBBBBBBBBB";

    private static final String DEFAULT_BUREAU = "AAAAAAAAAA";
    private static final String UPDATED_BUREAU = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/professeurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProfesseurRepository professeurRepository;

    @Mock
    private ProfesseurRepository professeurRepositoryMock;

    @Autowired
    private ProfesseurMapper professeurMapper;

    @Mock
    private ProfesseurService professeurServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProfesseurMockMvc;

    private Professeur professeur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Professeur createEntity(EntityManager em) {
        Professeur professeur = new Professeur()
            .numeroEmploye(DEFAULT_NUMERO_EMPLOYE)
            .dateEmbauche(DEFAULT_DATE_EMBAUCHE)
            .specialite(DEFAULT_SPECIALITE)
            .bureau(DEFAULT_BUREAU);
        return professeur;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Professeur createUpdatedEntity(EntityManager em) {
        Professeur professeur = new Professeur()
            .numeroEmploye(UPDATED_NUMERO_EMPLOYE)
            .dateEmbauche(UPDATED_DATE_EMBAUCHE)
            .specialite(UPDATED_SPECIALITE)
            .bureau(UPDATED_BUREAU);
        return professeur;
    }

    @BeforeEach
    public void initTest() {
        professeur = createEntity(em);
    }

    @Test
    @Transactional
    void createProfesseur() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Professeur
        ProfesseurDTO professeurDTO = professeurMapper.toDto(professeur);
        var returnedProfesseurDTO = om.readValue(
            restProfesseurMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professeurDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProfesseurDTO.class
        );

        // Validate the Professeur in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProfesseur = professeurMapper.toEntity(returnedProfesseurDTO);
        assertProfesseurUpdatableFieldsEquals(returnedProfesseur, getPersistedProfesseur(returnedProfesseur));
    }

    @Test
    @Transactional
    void createProfesseurWithExistingId() throws Exception {
        // Create the Professeur with an existing ID
        professeur.setId(1L);
        ProfesseurDTO professeurDTO = professeurMapper.toDto(professeur);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProfesseurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professeurDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Professeur in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNumeroEmployeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        professeur.setNumeroEmploye(null);

        // Create the Professeur, which fails.
        ProfesseurDTO professeurDTO = professeurMapper.toDto(professeur);

        restProfesseurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professeurDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateEmbaucheIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        professeur.setDateEmbauche(null);

        // Create the Professeur, which fails.
        ProfesseurDTO professeurDTO = professeurMapper.toDto(professeur);

        restProfesseurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professeurDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSpecialiteIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        professeur.setSpecialite(null);

        // Create the Professeur, which fails.
        ProfesseurDTO professeurDTO = professeurMapper.toDto(professeur);

        restProfesseurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professeurDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProfesseurs() throws Exception {
        // Initialize the database
        professeurRepository.saveAndFlush(professeur);

        // Get all the professeurList
        restProfesseurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(professeur.getId().intValue())))
            .andExpect(jsonPath("$.[*].numeroEmploye").value(hasItem(DEFAULT_NUMERO_EMPLOYE)))
            .andExpect(jsonPath("$.[*].dateEmbauche").value(hasItem(DEFAULT_DATE_EMBAUCHE.toString())))
            .andExpect(jsonPath("$.[*].specialite").value(hasItem(DEFAULT_SPECIALITE)))
            .andExpect(jsonPath("$.[*].bureau").value(hasItem(DEFAULT_BUREAU)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProfesseursWithEagerRelationshipsIsEnabled() throws Exception {
        when(professeurServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProfesseurMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(professeurServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProfesseursWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(professeurServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProfesseurMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(professeurRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getProfesseur() throws Exception {
        // Initialize the database
        professeurRepository.saveAndFlush(professeur);

        // Get the professeur
        restProfesseurMockMvc
            .perform(get(ENTITY_API_URL_ID, professeur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(professeur.getId().intValue()))
            .andExpect(jsonPath("$.numeroEmploye").value(DEFAULT_NUMERO_EMPLOYE))
            .andExpect(jsonPath("$.dateEmbauche").value(DEFAULT_DATE_EMBAUCHE.toString()))
            .andExpect(jsonPath("$.specialite").value(DEFAULT_SPECIALITE))
            .andExpect(jsonPath("$.bureau").value(DEFAULT_BUREAU));
    }

    @Test
    @Transactional
    void getProfesseursByIdFiltering() throws Exception {
        // Initialize the database
        professeurRepository.saveAndFlush(professeur);

        Long id = professeur.getId();

        defaultProfesseurFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultProfesseurFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultProfesseurFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProfesseursByNumeroEmployeIsEqualToSomething() throws Exception {
        // Initialize the database
        professeurRepository.saveAndFlush(professeur);

        // Get all the professeurList where numeroEmploye equals to
        defaultProfesseurFiltering("numeroEmploye.equals=" + DEFAULT_NUMERO_EMPLOYE, "numeroEmploye.equals=" + UPDATED_NUMERO_EMPLOYE);
    }

    @Test
    @Transactional
    void getAllProfesseursByNumeroEmployeIsInShouldWork() throws Exception {
        // Initialize the database
        professeurRepository.saveAndFlush(professeur);

        // Get all the professeurList where numeroEmploye in
        defaultProfesseurFiltering(
            "numeroEmploye.in=" + DEFAULT_NUMERO_EMPLOYE + "," + UPDATED_NUMERO_EMPLOYE,
            "numeroEmploye.in=" + UPDATED_NUMERO_EMPLOYE
        );
    }

    @Test
    @Transactional
    void getAllProfesseursByNumeroEmployeIsNullOrNotNull() throws Exception {
        // Initialize the database
        professeurRepository.saveAndFlush(professeur);

        // Get all the professeurList where numeroEmploye is not null
        defaultProfesseurFiltering("numeroEmploye.specified=true", "numeroEmploye.specified=false");
    }

    @Test
    @Transactional
    void getAllProfesseursByNumeroEmployeContainsSomething() throws Exception {
        // Initialize the database
        professeurRepository.saveAndFlush(professeur);

        // Get all the professeurList where numeroEmploye contains
        defaultProfesseurFiltering("numeroEmploye.contains=" + DEFAULT_NUMERO_EMPLOYE, "numeroEmploye.contains=" + UPDATED_NUMERO_EMPLOYE);
    }

    @Test
    @Transactional
    void getAllProfesseursByNumeroEmployeNotContainsSomething() throws Exception {
        // Initialize the database
        professeurRepository.saveAndFlush(professeur);

        // Get all the professeurList where numeroEmploye does not contain
        defaultProfesseurFiltering(
            "numeroEmploye.doesNotContain=" + UPDATED_NUMERO_EMPLOYE,
            "numeroEmploye.doesNotContain=" + DEFAULT_NUMERO_EMPLOYE
        );
    }

    @Test
    @Transactional
    void getAllProfesseursByDateEmbaucheIsEqualToSomething() throws Exception {
        // Initialize the database
        professeurRepository.saveAndFlush(professeur);

        // Get all the professeurList where dateEmbauche equals to
        defaultProfesseurFiltering("dateEmbauche.equals=" + DEFAULT_DATE_EMBAUCHE, "dateEmbauche.equals=" + UPDATED_DATE_EMBAUCHE);
    }

    @Test
    @Transactional
    void getAllProfesseursByDateEmbaucheIsInShouldWork() throws Exception {
        // Initialize the database
        professeurRepository.saveAndFlush(professeur);

        // Get all the professeurList where dateEmbauche in
        defaultProfesseurFiltering(
            "dateEmbauche.in=" + DEFAULT_DATE_EMBAUCHE + "," + UPDATED_DATE_EMBAUCHE,
            "dateEmbauche.in=" + UPDATED_DATE_EMBAUCHE
        );
    }

    @Test
    @Transactional
    void getAllProfesseursByDateEmbaucheIsNullOrNotNull() throws Exception {
        // Initialize the database
        professeurRepository.saveAndFlush(professeur);

        // Get all the professeurList where dateEmbauche is not null
        defaultProfesseurFiltering("dateEmbauche.specified=true", "dateEmbauche.specified=false");
    }

    @Test
    @Transactional
    void getAllProfesseursByDateEmbaucheIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        professeurRepository.saveAndFlush(professeur);

        // Get all the professeurList where dateEmbauche is greater than or equal to
        defaultProfesseurFiltering(
            "dateEmbauche.greaterThanOrEqual=" + DEFAULT_DATE_EMBAUCHE,
            "dateEmbauche.greaterThanOrEqual=" + UPDATED_DATE_EMBAUCHE
        );
    }

    @Test
    @Transactional
    void getAllProfesseursByDateEmbaucheIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        professeurRepository.saveAndFlush(professeur);

        // Get all the professeurList where dateEmbauche is less than or equal to
        defaultProfesseurFiltering(
            "dateEmbauche.lessThanOrEqual=" + DEFAULT_DATE_EMBAUCHE,
            "dateEmbauche.lessThanOrEqual=" + SMALLER_DATE_EMBAUCHE
        );
    }

    @Test
    @Transactional
    void getAllProfesseursByDateEmbaucheIsLessThanSomething() throws Exception {
        // Initialize the database
        professeurRepository.saveAndFlush(professeur);

        // Get all the professeurList where dateEmbauche is less than
        defaultProfesseurFiltering("dateEmbauche.lessThan=" + UPDATED_DATE_EMBAUCHE, "dateEmbauche.lessThan=" + DEFAULT_DATE_EMBAUCHE);
    }

    @Test
    @Transactional
    void getAllProfesseursByDateEmbaucheIsGreaterThanSomething() throws Exception {
        // Initialize the database
        professeurRepository.saveAndFlush(professeur);

        // Get all the professeurList where dateEmbauche is greater than
        defaultProfesseurFiltering(
            "dateEmbauche.greaterThan=" + SMALLER_DATE_EMBAUCHE,
            "dateEmbauche.greaterThan=" + DEFAULT_DATE_EMBAUCHE
        );
    }

    @Test
    @Transactional
    void getAllProfesseursBySpecialiteIsEqualToSomething() throws Exception {
        // Initialize the database
        professeurRepository.saveAndFlush(professeur);

        // Get all the professeurList where specialite equals to
        defaultProfesseurFiltering("specialite.equals=" + DEFAULT_SPECIALITE, "specialite.equals=" + UPDATED_SPECIALITE);
    }

    @Test
    @Transactional
    void getAllProfesseursBySpecialiteIsInShouldWork() throws Exception {
        // Initialize the database
        professeurRepository.saveAndFlush(professeur);

        // Get all the professeurList where specialite in
        defaultProfesseurFiltering("specialite.in=" + DEFAULT_SPECIALITE + "," + UPDATED_SPECIALITE, "specialite.in=" + UPDATED_SPECIALITE);
    }

    @Test
    @Transactional
    void getAllProfesseursBySpecialiteIsNullOrNotNull() throws Exception {
        // Initialize the database
        professeurRepository.saveAndFlush(professeur);

        // Get all the professeurList where specialite is not null
        defaultProfesseurFiltering("specialite.specified=true", "specialite.specified=false");
    }

    @Test
    @Transactional
    void getAllProfesseursBySpecialiteContainsSomething() throws Exception {
        // Initialize the database
        professeurRepository.saveAndFlush(professeur);

        // Get all the professeurList where specialite contains
        defaultProfesseurFiltering("specialite.contains=" + DEFAULT_SPECIALITE, "specialite.contains=" + UPDATED_SPECIALITE);
    }

    @Test
    @Transactional
    void getAllProfesseursBySpecialiteNotContainsSomething() throws Exception {
        // Initialize the database
        professeurRepository.saveAndFlush(professeur);

        // Get all the professeurList where specialite does not contain
        defaultProfesseurFiltering("specialite.doesNotContain=" + UPDATED_SPECIALITE, "specialite.doesNotContain=" + DEFAULT_SPECIALITE);
    }

    @Test
    @Transactional
    void getAllProfesseursByBureauIsEqualToSomething() throws Exception {
        // Initialize the database
        professeurRepository.saveAndFlush(professeur);

        // Get all the professeurList where bureau equals to
        defaultProfesseurFiltering("bureau.equals=" + DEFAULT_BUREAU, "bureau.equals=" + UPDATED_BUREAU);
    }

    @Test
    @Transactional
    void getAllProfesseursByBureauIsInShouldWork() throws Exception {
        // Initialize the database
        professeurRepository.saveAndFlush(professeur);

        // Get all the professeurList where bureau in
        defaultProfesseurFiltering("bureau.in=" + DEFAULT_BUREAU + "," + UPDATED_BUREAU, "bureau.in=" + UPDATED_BUREAU);
    }

    @Test
    @Transactional
    void getAllProfesseursByBureauIsNullOrNotNull() throws Exception {
        // Initialize the database
        professeurRepository.saveAndFlush(professeur);

        // Get all the professeurList where bureau is not null
        defaultProfesseurFiltering("bureau.specified=true", "bureau.specified=false");
    }

    @Test
    @Transactional
    void getAllProfesseursByBureauContainsSomething() throws Exception {
        // Initialize the database
        professeurRepository.saveAndFlush(professeur);

        // Get all the professeurList where bureau contains
        defaultProfesseurFiltering("bureau.contains=" + DEFAULT_BUREAU, "bureau.contains=" + UPDATED_BUREAU);
    }

    @Test
    @Transactional
    void getAllProfesseursByBureauNotContainsSomething() throws Exception {
        // Initialize the database
        professeurRepository.saveAndFlush(professeur);

        // Get all the professeurList where bureau does not contain
        defaultProfesseurFiltering("bureau.doesNotContain=" + UPDATED_BUREAU, "bureau.doesNotContain=" + DEFAULT_BUREAU);
    }

    @Test
    @Transactional
    void getAllProfesseursByUserIsEqualToSomething() throws Exception {
        User user;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            professeurRepository.saveAndFlush(professeur);
            user = UserResourceIT.createEntity(em);
        } else {
            user = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(user);
        em.flush();
        professeur.setUser(user);
        professeurRepository.saveAndFlush(professeur);
        Long userId = user.getId();
        // Get all the professeurList where user equals to userId
        defaultProfesseurShouldBeFound("userId.equals=" + userId);

        // Get all the professeurList where user equals to (userId + 1)
        defaultProfesseurShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllProfesseursByDepartementIsEqualToSomething() throws Exception {
        Departement departement;
        if (TestUtil.findAll(em, Departement.class).isEmpty()) {
            professeurRepository.saveAndFlush(professeur);
            departement = DepartementResourceIT.createEntity(em);
        } else {
            departement = TestUtil.findAll(em, Departement.class).get(0);
        }
        em.persist(departement);
        em.flush();
        professeur.setDepartement(departement);
        professeurRepository.saveAndFlush(professeur);
        Long departementId = departement.getId();
        // Get all the professeurList where departement equals to departementId
        defaultProfesseurShouldBeFound("departementId.equals=" + departementId);

        // Get all the professeurList where departement equals to (departementId + 1)
        defaultProfesseurShouldNotBeFound("departementId.equals=" + (departementId + 1));
    }

    private void defaultProfesseurFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultProfesseurShouldBeFound(shouldBeFound);
        defaultProfesseurShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProfesseurShouldBeFound(String filter) throws Exception {
        restProfesseurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(professeur.getId().intValue())))
            .andExpect(jsonPath("$.[*].numeroEmploye").value(hasItem(DEFAULT_NUMERO_EMPLOYE)))
            .andExpect(jsonPath("$.[*].dateEmbauche").value(hasItem(DEFAULT_DATE_EMBAUCHE.toString())))
            .andExpect(jsonPath("$.[*].specialite").value(hasItem(DEFAULT_SPECIALITE)))
            .andExpect(jsonPath("$.[*].bureau").value(hasItem(DEFAULT_BUREAU)));

        // Check, that the count call also returns 1
        restProfesseurMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProfesseurShouldNotBeFound(String filter) throws Exception {
        restProfesseurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProfesseurMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProfesseur() throws Exception {
        // Get the professeur
        restProfesseurMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProfesseur() throws Exception {
        // Initialize the database
        professeurRepository.saveAndFlush(professeur);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the professeur
        Professeur updatedProfesseur = professeurRepository.findById(professeur.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProfesseur are not directly saved in db
        em.detach(updatedProfesseur);
        updatedProfesseur
            .numeroEmploye(UPDATED_NUMERO_EMPLOYE)
            .dateEmbauche(UPDATED_DATE_EMBAUCHE)
            .specialite(UPDATED_SPECIALITE)
            .bureau(UPDATED_BUREAU);
        ProfesseurDTO professeurDTO = professeurMapper.toDto(updatedProfesseur);

        restProfesseurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, professeurDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(professeurDTO))
            )
            .andExpect(status().isOk());

        // Validate the Professeur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProfesseurToMatchAllProperties(updatedProfesseur);
    }

    @Test
    @Transactional
    void putNonExistingProfesseur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professeur.setId(longCount.incrementAndGet());

        // Create the Professeur
        ProfesseurDTO professeurDTO = professeurMapper.toDto(professeur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfesseurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, professeurDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(professeurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Professeur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProfesseur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professeur.setId(longCount.incrementAndGet());

        // Create the Professeur
        ProfesseurDTO professeurDTO = professeurMapper.toDto(professeur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfesseurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(professeurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Professeur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProfesseur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professeur.setId(longCount.incrementAndGet());

        // Create the Professeur
        ProfesseurDTO professeurDTO = professeurMapper.toDto(professeur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfesseurMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professeurDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Professeur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProfesseurWithPatch() throws Exception {
        // Initialize the database
        professeurRepository.saveAndFlush(professeur);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the professeur using partial update
        Professeur partialUpdatedProfesseur = new Professeur();
        partialUpdatedProfesseur.setId(professeur.getId());

        partialUpdatedProfesseur.numeroEmploye(UPDATED_NUMERO_EMPLOYE).dateEmbauche(UPDATED_DATE_EMBAUCHE);

        restProfesseurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfesseur.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProfesseur))
            )
            .andExpect(status().isOk());

        // Validate the Professeur in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProfesseurUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProfesseur, professeur),
            getPersistedProfesseur(professeur)
        );
    }

    @Test
    @Transactional
    void fullUpdateProfesseurWithPatch() throws Exception {
        // Initialize the database
        professeurRepository.saveAndFlush(professeur);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the professeur using partial update
        Professeur partialUpdatedProfesseur = new Professeur();
        partialUpdatedProfesseur.setId(professeur.getId());

        partialUpdatedProfesseur
            .numeroEmploye(UPDATED_NUMERO_EMPLOYE)
            .dateEmbauche(UPDATED_DATE_EMBAUCHE)
            .specialite(UPDATED_SPECIALITE)
            .bureau(UPDATED_BUREAU);

        restProfesseurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfesseur.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProfesseur))
            )
            .andExpect(status().isOk());

        // Validate the Professeur in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProfesseurUpdatableFieldsEquals(partialUpdatedProfesseur, getPersistedProfesseur(partialUpdatedProfesseur));
    }

    @Test
    @Transactional
    void patchNonExistingProfesseur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professeur.setId(longCount.incrementAndGet());

        // Create the Professeur
        ProfesseurDTO professeurDTO = professeurMapper.toDto(professeur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfesseurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, professeurDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(professeurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Professeur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProfesseur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professeur.setId(longCount.incrementAndGet());

        // Create the Professeur
        ProfesseurDTO professeurDTO = professeurMapper.toDto(professeur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfesseurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(professeurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Professeur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProfesseur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professeur.setId(longCount.incrementAndGet());

        // Create the Professeur
        ProfesseurDTO professeurDTO = professeurMapper.toDto(professeur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfesseurMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(professeurDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Professeur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProfesseur() throws Exception {
        // Initialize the database
        professeurRepository.saveAndFlush(professeur);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the professeur
        restProfesseurMockMvc
            .perform(delete(ENTITY_API_URL_ID, professeur.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return professeurRepository.count();
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

    protected Professeur getPersistedProfesseur(Professeur professeur) {
        return professeurRepository.findById(professeur.getId()).orElseThrow();
    }

    protected void assertPersistedProfesseurToMatchAllProperties(Professeur expectedProfesseur) {
        assertProfesseurAllPropertiesEquals(expectedProfesseur, getPersistedProfesseur(expectedProfesseur));
    }

    protected void assertPersistedProfesseurToMatchUpdatableProperties(Professeur expectedProfesseur) {
        assertProfesseurAllUpdatablePropertiesEquals(expectedProfesseur, getPersistedProfesseur(expectedProfesseur));
    }
}
