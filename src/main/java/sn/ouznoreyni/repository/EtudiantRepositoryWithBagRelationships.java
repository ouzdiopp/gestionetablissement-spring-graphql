package sn.ouznoreyni.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import sn.ouznoreyni.domain.Etudiant;

public interface EtudiantRepositoryWithBagRelationships {
    Optional<Etudiant> fetchBagRelationships(Optional<Etudiant> etudiant);

    List<Etudiant> fetchBagRelationships(List<Etudiant> etudiants);

    Page<Etudiant> fetchBagRelationships(Page<Etudiant> etudiants);
}
