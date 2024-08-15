package sn.ouznoreyni.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.ouznoreyni.repository.InscriptionRepository;
import sn.ouznoreyni.service.InscriptionService;
import sn.ouznoreyni.service.dto.InscriptionDTO;
import sn.ouznoreyni.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.ouznoreyni.domain.Inscription}.
 */
@RestController
@RequestMapping("/api/inscriptions")
public class InscriptionResource {

    private final Logger log = LoggerFactory.getLogger(InscriptionResource.class);

    private static final String ENTITY_NAME = "inscription";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InscriptionService inscriptionService;

    private final InscriptionRepository inscriptionRepository;

    public InscriptionResource(InscriptionService inscriptionService, InscriptionRepository inscriptionRepository) {
        this.inscriptionService = inscriptionService;
        this.inscriptionRepository = inscriptionRepository;
    }

    /**
     * {@code POST  /inscriptions} : Create a new inscription.
     *
     * @param inscriptionDTO the inscriptionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new inscriptionDTO, or with status {@code 400 (Bad Request)} if the inscription has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<InscriptionDTO> createInscription(@Valid @RequestBody InscriptionDTO inscriptionDTO) throws URISyntaxException {
        log.debug("REST request to save Inscription : {}", inscriptionDTO);
        if (inscriptionDTO.getId() != null) {
            throw new BadRequestAlertException("A new inscription cannot already have an ID", ENTITY_NAME, "idexists");
        }
        inscriptionDTO = inscriptionService.save(inscriptionDTO);
        return ResponseEntity.created(new URI("/api/inscriptions/" + inscriptionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, inscriptionDTO.getId().toString()))
            .body(inscriptionDTO);
    }

    /**
     * {@code PUT  /inscriptions/:id} : Updates an existing inscription.
     *
     * @param id the id of the inscriptionDTO to save.
     * @param inscriptionDTO the inscriptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inscriptionDTO,
     * or with status {@code 400 (Bad Request)} if the inscriptionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the inscriptionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InscriptionDTO> updateInscription(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InscriptionDTO inscriptionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Inscription : {}, {}", id, inscriptionDTO);
        if (inscriptionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inscriptionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!inscriptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        inscriptionDTO = inscriptionService.update(inscriptionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, inscriptionDTO.getId().toString()))
            .body(inscriptionDTO);
    }

    /**
     * {@code PATCH  /inscriptions/:id} : Partial updates given fields of an existing inscription, field will ignore if it is null
     *
     * @param id the id of the inscriptionDTO to save.
     * @param inscriptionDTO the inscriptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inscriptionDTO,
     * or with status {@code 400 (Bad Request)} if the inscriptionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the inscriptionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the inscriptionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InscriptionDTO> partialUpdateInscription(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InscriptionDTO inscriptionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Inscription partially : {}, {}", id, inscriptionDTO);
        if (inscriptionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inscriptionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!inscriptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InscriptionDTO> result = inscriptionService.partialUpdate(inscriptionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, inscriptionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /inscriptions} : get all the inscriptions.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of inscriptions in body.
     */
    @GetMapping("")
    public List<InscriptionDTO> getAllInscriptions(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get all Inscriptions");
        return inscriptionService.findAll();
    }

    /**
     * {@code GET  /inscriptions/:id} : get the "id" inscription.
     *
     * @param id the id of the inscriptionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the inscriptionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InscriptionDTO> getInscription(@PathVariable("id") Long id) {
        log.debug("REST request to get Inscription : {}", id);
        Optional<InscriptionDTO> inscriptionDTO = inscriptionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(inscriptionDTO);
    }

    /**
     * {@code DELETE  /inscriptions/:id} : delete the "id" inscription.
     *
     * @param id the id of the inscriptionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInscription(@PathVariable("id") Long id) {
        log.debug("REST request to delete Inscription : {}", id);
        inscriptionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
