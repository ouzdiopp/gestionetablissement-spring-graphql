package sn.ouznoreyni.service.mapper;

import org.mapstruct.*;
import sn.ouznoreyni.domain.Cours;
import sn.ouznoreyni.domain.Etudiant;
import sn.ouznoreyni.domain.Inscription;
import sn.ouznoreyni.service.dto.CoursDTO;
import sn.ouznoreyni.service.dto.EtudiantDTO;
import sn.ouznoreyni.service.dto.InscriptionDTO;

/**
 * Mapper for the entity {@link Inscription} and its DTO {@link InscriptionDTO}.
 */
@Mapper(componentModel = "spring")
public interface InscriptionMapper extends EntityMapper<InscriptionDTO, Inscription> {
    @Mapping(target = "etudiant", source = "etudiant", qualifiedByName = "etudiantNumeroEtudiant")
    @Mapping(target = "cours", source = "cours", qualifiedByName = "coursCode")
    InscriptionDTO toDto(Inscription s);

    @Named("etudiantNumeroEtudiant")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "numeroEtudiant", source = "numeroEtudiant")
    EtudiantDTO toDtoEtudiantNumeroEtudiant(Etudiant etudiant);

    @Named("coursCode")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "code", source = "code")
    CoursDTO toDtoCoursCode(Cours cours);
}
