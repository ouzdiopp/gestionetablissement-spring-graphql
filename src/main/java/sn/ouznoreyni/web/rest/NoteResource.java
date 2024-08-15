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
import sn.ouznoreyni.repository.NoteRepository;
import sn.ouznoreyni.service.NoteService;
import sn.ouznoreyni.service.dto.NoteDTO;
import sn.ouznoreyni.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.ouznoreyni.domain.Note}.
 */
@RestController
@RequestMapping("/api/notes")
public class NoteResource {

    private final Logger log = LoggerFactory.getLogger(NoteResource.class);

    private static final String ENTITY_NAME = "note";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NoteService noteService;

    private final NoteRepository noteRepository;

    public NoteResource(NoteService noteService, NoteRepository noteRepository) {
        this.noteService = noteService;
        this.noteRepository = noteRepository;
    }

    /**
     * {@code POST  /notes} : Create a new note.
     *
     * @param noteDTO the noteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new noteDTO, or with status {@code 400 (Bad Request)} if the note has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NoteDTO> createNote(@Valid @RequestBody NoteDTO noteDTO) throws URISyntaxException {
        log.debug("REST request to save Note : {}", noteDTO);
        if (noteDTO.getId() != null) {
            throw new BadRequestAlertException("A new note cannot already have an ID", ENTITY_NAME, "idexists");
        }
        noteDTO = noteService.save(noteDTO);
        return ResponseEntity.created(new URI("/api/notes/" + noteDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, noteDTO.getId().toString()))
            .body(noteDTO);
    }

    /**
     * {@code PUT  /notes/:id} : Updates an existing note.
     *
     * @param id the id of the noteDTO to save.
     * @param noteDTO the noteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated noteDTO,
     * or with status {@code 400 (Bad Request)} if the noteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the noteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NoteDTO> updateNote(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NoteDTO noteDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Note : {}, {}", id, noteDTO);
        if (noteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, noteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!noteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        noteDTO = noteService.update(noteDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, noteDTO.getId().toString()))
            .body(noteDTO);
    }

    /**
     * {@code PATCH  /notes/:id} : Partial updates given fields of an existing note, field will ignore if it is null
     *
     * @param id the id of the noteDTO to save.
     * @param noteDTO the noteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated noteDTO,
     * or with status {@code 400 (Bad Request)} if the noteDTO is not valid,
     * or with status {@code 404 (Not Found)} if the noteDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the noteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NoteDTO> partialUpdateNote(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NoteDTO noteDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Note partially : {}, {}", id, noteDTO);
        if (noteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, noteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!noteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NoteDTO> result = noteService.partialUpdate(noteDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, noteDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /notes} : get all the notes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of notes in body.
     */
    @GetMapping("")
    public List<NoteDTO> getAllNotes() {
        log.debug("REST request to get all Notes");
        return noteService.findAll();
    }

    /**
     * {@code GET  /notes/:id} : get the "id" note.
     *
     * @param id the id of the noteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the noteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NoteDTO> getNote(@PathVariable("id") Long id) {
        log.debug("REST request to get Note : {}", id);
        Optional<NoteDTO> noteDTO = noteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(noteDTO);
    }

    /**
     * {@code DELETE  /notes/:id} : delete the "id" note.
     *
     * @param id the id of the noteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable("id") Long id) {
        log.debug("REST request to delete Note : {}", id);
        noteService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
