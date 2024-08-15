package sn.ouznoreyni.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ouznoreyni.domain.Inscription;
import sn.ouznoreyni.domain.Note;

/**
 * Spring Data JPA repository for the Note entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    Optional<Note> findByInscription(Inscription inscription);
}
