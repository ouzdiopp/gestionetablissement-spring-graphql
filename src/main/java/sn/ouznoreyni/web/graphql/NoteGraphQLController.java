package sn.ouznoreyni.web.graphql;

import java.util.List;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import sn.ouznoreyni.service.NoteService;
import sn.ouznoreyni.service.dto.NoteDTO;

@Controller
public class NoteGraphQLController {

    private final NoteService noteService;

    public NoteGraphQLController(NoteService noteService) {
        this.noteService = noteService;
    }

    @QueryMapping
    public NoteDTO note(@Argument Long id) {
        return noteService.findOne(id).orElse(null);
    }

    @QueryMapping
    public List<NoteDTO> notes() {
        return noteService.findAll();
    }

    @MutationMapping
    public NoteDTO createNote(@Argument NoteDTO input) {
        return noteService.save(input);
    }

    @MutationMapping
    public NoteDTO updateNote(@Argument Long id, @Argument NoteDTO input) {
        input.setId(id);
        return noteService.update(input);
    }

    @MutationMapping
    public boolean deleteNote(@Argument Long id) {
        noteService.delete(id);
        return true;
    }
}
