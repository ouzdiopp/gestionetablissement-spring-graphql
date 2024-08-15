package sn.ouznoreyni.web.graphql;

import java.util.List;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import sn.ouznoreyni.service.InscriptionService;
import sn.ouznoreyni.service.dto.InscriptionDTO;

@Controller
public class InscriptionGraphQLController {

    private final InscriptionService inscriptionService;

    public InscriptionGraphQLController(InscriptionService inscriptionService) {
        this.inscriptionService = inscriptionService;
    }

    @QueryMapping
    public InscriptionDTO inscription(@Argument Long id) {
        return inscriptionService.findOne(id).orElse(null);
    }

    @QueryMapping
    public List<InscriptionDTO> inscriptions() {
        return inscriptionService.findAll();
    }

    @MutationMapping
    public InscriptionDTO createInscription(@Argument InscriptionDTO input) {
        return inscriptionService.save(input);
    }

    @MutationMapping
    public InscriptionDTO updateInscription(@Argument Long id, @Argument InscriptionDTO input) {
        input.setId(id);
        return inscriptionService.update(input);
    }

    @MutationMapping
    public boolean deleteInscription(@Argument Long id) {
        inscriptionService.delete(id);
        return true;
    }
}
