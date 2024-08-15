package sn.ouznoreyni.service.mapper;

import org.mapstruct.*;
import sn.ouznoreyni.domain.Departement;
import sn.ouznoreyni.service.dto.DepartementDTO;

/**
 * Mapper for the entity {@link Departement} and its DTO {@link DepartementDTO}.
 */
@Mapper(componentModel = "spring")
public interface DepartementMapper extends EntityMapper<DepartementDTO, Departement> {}
