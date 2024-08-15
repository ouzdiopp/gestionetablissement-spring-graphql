package sn.ouznoreyni.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ouznoreyni.domain.Departement;

/**
 * Spring Data JPA repository for the Departement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DepartementRepository extends JpaRepository<Departement, Long> {
    Optional<Departement> findByNom(String nom);
}
