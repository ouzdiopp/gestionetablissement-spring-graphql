package sn.ouznoreyni.web.graphql;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import sn.ouznoreyni.service.ProfesseurService;
import sn.ouznoreyni.service.dto.ProfesseurDTO;

@Controller
public class ProfesseurGraphQLController {

    private final ProfesseurService professeurService;

    public ProfesseurGraphQLController(ProfesseurService professeurService) {
        this.professeurService = professeurService;
    }

    @QueryMapping
    public ProfesseurDTO professeur(@Argument Long id) {
        return professeurService.findOne(id).orElse(null);
    }

    @QueryMapping
    public Page<ProfesseurDTO> professeurs(@Argument Integer page, @Argument Integer size) {
        int pageNumber = (page != null) ? page : 0;
        int pageSize = (size != null) ? size : 20;
        return professeurService.findAllWithEagerRelationships(PageRequest.of(pageNumber, pageSize));
    }

    @MutationMapping
    public ProfesseurDTO createProfesseur(@Argument ProfesseurDTO input) {
        return professeurService.save(input);
    }

    @MutationMapping
    public ProfesseurDTO updateProfesseur(@Argument Long id, @Argument ProfesseurDTO input) {
        input.setId(id);
        return professeurService.update(input);
    }

    @MutationMapping
    public boolean deleteProfesseur(@Argument Long id) {
        professeurService.delete(id);
        return true;
    }
}
