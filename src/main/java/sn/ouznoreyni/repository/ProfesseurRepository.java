package sn.ouznoreyni.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.ouznoreyni.domain.Professeur;
import sn.ouznoreyni.domain.User;

/**
 * Spring Data JPA repository for the Professeur entity.
 */
@Repository
public interface ProfesseurRepository extends JpaRepository<Professeur, Long>, JpaSpecificationExecutor<Professeur> {
    default Optional<Professeur> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Professeur> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Professeur> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    Optional<Professeur> findByUser(User user);

    @Query(
        value = "select professeur from Professeur professeur left join fetch professeur.user left join fetch professeur.departement",
        countQuery = "select count(professeur) from Professeur professeur"
    )
    Page<Professeur> findAllWithToOneRelationships(Pageable pageable);

    @Query("select professeur from Professeur professeur left join fetch professeur.user left join fetch professeur.departement")
    List<Professeur> findAllWithToOneRelationships();

    @Query(
        "select professeur from Professeur professeur left join fetch professeur.user left join fetch professeur.departement where professeur.id =:id"
    )
    Optional<Professeur> findOneWithToOneRelationships(@Param("id") Long id);
}
