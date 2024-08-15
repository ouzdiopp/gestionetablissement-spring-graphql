package sn.ouznoreyni.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import sn.ouznoreyni.domain.Cours;
import sn.ouznoreyni.domain.Departement;
import sn.ouznoreyni.domain.Etudiant;
import sn.ouznoreyni.domain.Professeur;
import sn.ouznoreyni.service.dto.CoursDTO;
import sn.ouznoreyni.service.dto.DepartementDTO;
import sn.ouznoreyni.service.dto.EtudiantDTO;
import sn.ouznoreyni.service.dto.ProfesseurDTO;

/**
 * Mapper for the entity {@link Cours} and its DTO {@link CoursDTO}.
 */
@Mapper(componentModel = "spring")
public interface CoursMapper extends EntityMapper<CoursDTO, Cours> {
    @Mapping(target = "departement", source = "departement", qualifiedByName = "departementNom")
    @Mapping(target = "professeur", source = "professeur", qualifiedByName = "professeurNumeroEmploye")
    @Mapping(target = "etudiants", source = "etudiants", qualifiedByName = "etudiantNumeroEtudiantSet")
    CoursDTO toDto(Cours s);

    @Mapping(target = "etudiants", ignore = true)
    @Mapping(target = "removeEtudiants", ignore = true)
    Cours toEntity(CoursDTO coursDTO);

    @Named("departementNom")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nom", source = "nom")
    DepartementDTO toDtoDepartementNom(Departement departement);

    @Named("professeurNumeroEmploye")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "numeroEmploye", source = "numeroEmploye")
    ProfesseurDTO toDtoProfesseurNumeroEmploye(Professeur professeur);

    @Named("etudiantNumeroEtudiant")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "numeroEtudiant", source = "numeroEtudiant")
    EtudiantDTO toDtoEtudiantNumeroEtudiant(Etudiant etudiant);

    @Named("etudiantNumeroEtudiantSet")
    default Set<EtudiantDTO> toDtoEtudiantNumeroEtudiantSet(Set<Etudiant> etudiant) {
        return etudiant.stream().map(this::toDtoEtudiantNumeroEtudiant).collect(Collectors.toSet());
    }
}
