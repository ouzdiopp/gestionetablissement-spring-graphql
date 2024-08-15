package sn.ouznoreyni.service.mapper;

import org.mapstruct.*;
import sn.ouznoreyni.domain.Departement;
import sn.ouznoreyni.domain.Professeur;
import sn.ouznoreyni.domain.User;
import sn.ouznoreyni.service.dto.DepartementDTO;
import sn.ouznoreyni.service.dto.ProfesseurDTO;
import sn.ouznoreyni.service.dto.UserDTO;

/**
 * Mapper for the entity {@link Professeur} and its DTO {@link ProfesseurDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProfesseurMapper extends EntityMapper<ProfesseurDTO, Professeur> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    @Mapping(target = "departement", source = "departement", qualifiedByName = "departementNom")
    ProfesseurDTO toDto(Professeur s);

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
}
