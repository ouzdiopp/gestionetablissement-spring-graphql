package sn.ouznoreyni.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.ouznoreyni.domain.Cours;
import sn.ouznoreyni.domain.Etudiant;
import sn.ouznoreyni.domain.Inscription;

/**
 * Spring Data JPA repository for the Inscription entity.
 */
@Repository
public interface InscriptionRepository extends JpaRepository<Inscription, Long> {
    Optional<Inscription> findByEtudiantAndCours(Etudiant etudiant, Cours cours);

    default Optional<Inscription> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Inscription> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Inscription> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select inscription from Inscription inscription left join fetch inscription.etudiant left join fetch inscription.cours",
        countQuery = "select count(inscription) from Inscription inscription"
    )
    Page<Inscription> findAllWithToOneRelationships(Pageable pageable);

    @Query("select inscription from Inscription inscription left join fetch inscription.etudiant left join fetch inscription.cours")
    List<Inscription> findAllWithToOneRelationships();

    @Query(
        "select inscription from Inscription inscription left join fetch inscription.etudiant left join fetch inscription.cours where inscription.id =:id"
    )
    Optional<Inscription> findOneWithToOneRelationships(@Param("id") Long id);
}
