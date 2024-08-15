package sn.ouznoreyni.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.ouznoreyni.domain.InscriptionAsserts.*;
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
import sn.ouznoreyni.domain.Inscription;
import sn.ouznoreyni.domain.enumeration.StatutInscription;
import sn.ouznoreyni.repository.InscriptionRepository;
import sn.ouznoreyni.service.InscriptionService;
import sn.ouznoreyni.service.dto.InscriptionDTO;
import sn.ouznoreyni.service.mapper.InscriptionMapper;

/**
 * Integration tests for the {@link InscriptionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class InscriptionResourceIT {

    private static final LocalDate DEFAULT_DATE_INSCRIPTION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_INSCRIPTION = LocalDate.now(ZoneId.systemDefault());

    private static final StatutInscription DEFAULT_STATUT = StatutInscription.INSCRIT;
    private static final StatutInscription UPDATED_STATUT = StatutInscription.ABANDONNE;

    private static final String ENTITY_API_URL = "/api/inscriptions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InscriptionRepository inscriptionRepository;

    @Mock
    private InscriptionRepository inscriptionRepositoryMock;

    @Autowired
    private InscriptionMapper inscriptionMapper;

    @Mock
    private InscriptionService inscriptionServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInscriptionMockMvc;

    private Inscription inscription;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Inscription createEntity(EntityManager em) {
        Inscription inscription = new Inscription().dateInscription(DEFAULT_DATE_INSCRIPTION).statut(DEFAULT_STATUT);
        return inscription;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Inscription createUpdatedEntity(EntityManager em) {
        Inscription inscription = new Inscription().dateInscription(UPDATED_DATE_INSCRIPTION).statut(UPDATED_STATUT);
        return inscription;
    }

    @BeforeEach
    public void initTest() {
        inscription = createEntity(em);
    }

    @Test
    @Transactional
    void createInscription() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Inscription
        InscriptionDTO inscriptionDTO = inscriptionMapper.toDto(inscription);
        var returnedInscriptionDTO = om.readValue(
            restInscriptionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inscriptionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            InscriptionDTO.class
        );

        // Validate the Inscription in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedInscription = inscriptionMapper.toEntity(returnedInscriptionDTO);
        assertInscriptionUpdatableFieldsEquals(returnedInscription, getPersistedInscription(returnedInscription));
    }

    @Test
    @Transactional
    void createInscriptionWithExistingId() throws Exception {
        // Create the Inscription with an existing ID
        inscription.setId(1L);
        InscriptionDTO inscriptionDTO = inscriptionMapper.toDto(inscription);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInscriptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inscriptionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Inscription in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateInscriptionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        inscription.setDateInscription(null);

        // Create the Inscription, which fails.
        InscriptionDTO inscriptionDTO = inscriptionMapper.toDto(inscription);

        restInscriptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inscriptionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatutIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        inscription.setStatut(null);

        // Create the Inscription, which fails.
        InscriptionDTO inscriptionDTO = inscriptionMapper.toDto(inscription);

        restInscriptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inscriptionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInscriptions() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        // Get all the inscriptionList
        restInscriptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inscription.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateInscription").value(hasItem(DEFAULT_DATE_INSCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInscriptionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(inscriptionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInscriptionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(inscriptionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInscriptionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(inscriptionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInscriptionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(inscriptionRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getInscription() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        // Get the inscription
        restInscriptionMockMvc
            .perform(get(ENTITY_API_URL_ID, inscription.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(inscription.getId().intValue()))
            .andExpect(jsonPath("$.dateInscription").value(DEFAULT_DATE_INSCRIPTION.toString()))
            .andExpect(jsonPath("$.statut").value(DEFAULT_STATUT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingInscription() throws Exception {
        // Get the inscription
        restInscriptionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInscription() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the inscription
        Inscription updatedInscription = inscriptionRepository.findById(inscription.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedInscription are not directly saved in db
        em.detach(updatedInscription);
        updatedInscription.dateInscription(UPDATED_DATE_INSCRIPTION).statut(UPDATED_STATUT);
        InscriptionDTO inscriptionDTO = inscriptionMapper.toDto(updatedInscription);

        restInscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inscriptionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(inscriptionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Inscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInscriptionToMatchAllProperties(updatedInscription);
    }

    @Test
    @Transactional
    void putNonExistingInscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inscription.setId(longCount.incrementAndGet());

        // Create the Inscription
        InscriptionDTO inscriptionDTO = inscriptionMapper.toDto(inscription);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inscriptionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(inscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inscription.setId(longCount.incrementAndGet());

        // Create the Inscription
        InscriptionDTO inscriptionDTO = inscriptionMapper.toDto(inscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(inscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inscription.setId(longCount.incrementAndGet());

        // Create the Inscription
        InscriptionDTO inscriptionDTO = inscriptionMapper.toDto(inscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInscriptionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inscriptionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Inscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInscriptionWithPatch() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the inscription using partial update
        Inscription partialUpdatedInscription = new Inscription();
        partialUpdatedInscription.setId(inscription.getId());

        partialUpdatedInscription.dateInscription(UPDATED_DATE_INSCRIPTION);

        restInscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInscription.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInscription))
            )
            .andExpect(status().isOk());

        // Validate the Inscription in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInscriptionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedInscription, inscription),
            getPersistedInscription(inscription)
        );
    }

    @Test
    @Transactional
    void fullUpdateInscriptionWithPatch() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the inscription using partial update
        Inscription partialUpdatedInscription = new Inscription();
        partialUpdatedInscription.setId(inscription.getId());

        partialUpdatedInscription.dateInscription(UPDATED_DATE_INSCRIPTION).statut(UPDATED_STATUT);

        restInscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInscription.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInscription))
            )
            .andExpect(status().isOk());

        // Validate the Inscription in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInscriptionUpdatableFieldsEquals(partialUpdatedInscription, getPersistedInscription(partialUpdatedInscription));
    }

    @Test
    @Transactional
    void patchNonExistingInscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inscription.setId(longCount.incrementAndGet());

        // Create the Inscription
        InscriptionDTO inscriptionDTO = inscriptionMapper.toDto(inscription);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, inscriptionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(inscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inscription.setId(longCount.incrementAndGet());

        // Create the Inscription
        InscriptionDTO inscriptionDTO = inscriptionMapper.toDto(inscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(inscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inscription.setId(longCount.incrementAndGet());

        // Create the Inscription
        InscriptionDTO inscriptionDTO = inscriptionMapper.toDto(inscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInscriptionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(inscriptionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Inscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInscription() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the inscription
        restInscriptionMockMvc
            .perform(delete(ENTITY_API_URL_ID, inscription.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return inscriptionRepository.count();
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

    protected Inscription getPersistedInscription(Inscription inscription) {
        return inscriptionRepository.findById(inscription.getId()).orElseThrow();
    }

    protected void assertPersistedInscriptionToMatchAllProperties(Inscription expectedInscription) {
        assertInscriptionAllPropertiesEquals(expectedInscription, getPersistedInscription(expectedInscription));
    }

    protected void assertPersistedInscriptionToMatchUpdatableProperties(Inscription expectedInscription) {
        assertInscriptionAllUpdatablePropertiesEquals(expectedInscription, getPersistedInscription(expectedInscription));
    }
}
