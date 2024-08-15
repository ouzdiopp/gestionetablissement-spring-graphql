package sn.ouznoreyni.service.mapper;

import org.mapstruct.*;
import sn.ouznoreyni.domain.Inscription;
import sn.ouznoreyni.domain.Note;
import sn.ouznoreyni.service.dto.InscriptionDTO;
import sn.ouznoreyni.service.dto.NoteDTO;

/**
 * Mapper for the entity {@link Note} and its DTO {@link NoteDTO}.
 */
@Mapper(componentModel = "spring")
public interface NoteMapper extends EntityMapper<NoteDTO, Note> {
    @Mapping(target = "inscription", source = "inscription", qualifiedByName = "inscriptionId")
    NoteDTO toDto(Note s);

    @Named("inscriptionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    InscriptionDTO toDtoInscriptionId(Inscription inscription);
}
