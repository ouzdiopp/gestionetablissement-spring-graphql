package sn.ouznoreyni.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import sn.ouznoreyni.domain.Cours;
import sn.ouznoreyni.domain.Departement;
import sn.ouznoreyni.domain.Etudiant;
import sn.ouznoreyni.domain.User;
import sn.ouznoreyni.service.dto.CoursDTO;
import sn.ouznoreyni.service.dto.DepartementDTO;
import sn.ouznoreyni.service.dto.EtudiantDTO;
import sn.ouznoreyni.service.dto.UserDTO;

/**
 * Mapper for the entity {@link Etudiant} and its DTO {@link EtudiantDTO}.
 */
@Mapper(componentModel = "spring")
public interface EtudiantMapper extends EntityMapper<EtudiantDTO, Etudiant> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    @Mapping(target = "departement", source = "departement", qualifiedByName = "departementNom")
    @Mapping(target = "cours", source = "cours", qualifiedByName = "coursCodeSet")
    EtudiantDTO toDto(Etudiant s);

    @Mapping(target = "removeCours", ignore = true)
    Etudiant toEntity(EtudiantDTO etudiantDTO);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("departementNom")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nom", source = "nom")
    DepartementDTO toDtoDepartementNom(Departement departement);

    @Named("coursCode")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "code", source = "code")
    CoursDTO toDtoCoursCode(Cours cours);

    @Named("coursCodeSet")
    default Set<CoursDTO> toDtoCoursCodeSet(Set<Cours> cours) {
        return cours.stream().map(this::toDtoCoursCode).collect(Collectors.toSet());
    }
}
