package sn.ouznoreyni.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.ouznoreyni.domain.Cours;

/**
 * Spring Data JPA repository for the Cours entity.
 */
@Repository
public interface CoursRepository extends JpaRepository<Cours, Long>, JpaSpecificationExecutor<Cours> {
    Optional<Cours> findByCode(String code);

    default Optional<Cours> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Cours> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Cours> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select cours from Cours cours left join fetch cours.departement left join fetch cours.professeur",
        countQuery = "select count(cours) from Cours cours"
    )
    Page<Cours> findAllWithToOneRelationships(Pageable pageable);

    @Query("select cours from Cours cours left join fetch cours.departement left join fetch cours.professeur")
    List<Cours> findAllWithToOneRelationships();

    @Query("select cours from Cours cours left join fetch cours.departement left join fetch cours.professeur where cours.id =:id")
    Optional<Cours> findOneWithToOneRelationships(@Param("id") Long id);
}
