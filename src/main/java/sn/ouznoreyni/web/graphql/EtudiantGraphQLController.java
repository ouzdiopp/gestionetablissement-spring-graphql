package sn.ouznoreyni.web.graphql;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import sn.ouznoreyni.service.EtudiantService;
import sn.ouznoreyni.service.dto.EtudiantDTO;

@Controller
public class EtudiantGraphQLController {

    private final EtudiantService etudiantService;

    public EtudiantGraphQLController(EtudiantService etudiantService) {
        this.etudiantService = etudiantService;
    }

    @QueryMapping
    public EtudiantDTO etudiant(@Argument Long id) {
        return etudiantService.findOne(id).orElse(null);
    }

    @QueryMapping
    public Page<EtudiantDTO> etudiants(@Argument Integer page, @Argument Integer size) {
        int pageNumber = (page != null) ? page : 0;
        int pageSize = (size != null) ? size : 20;
        return etudiantService.findAllWithEagerRelationships(PageRequest.of(pageNumber, pageSize));
    }

    @MutationMapping
    public EtudiantDTO createEtudiant(@Argument EtudiantDTO input) {
        return etudiantService.save(input);
    }

    @MutationMapping
    public EtudiantDTO updateEtudiant(@Argument Long id, @Argument EtudiantDTO input) {
        input.setId(id);
        return etudiantService.update(input);
    }

    @MutationMapping
    public boolean deleteEtudiant(@Argument Long id) {
        etudiantService.delete(id);
        return true;
    }
}
