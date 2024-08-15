package sn.ouznoreyni.web.graphql;

import java.util.List;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import sn.ouznoreyni.service.DepartementService;
import sn.ouznoreyni.service.dto.DepartementDTO;

@Controller
public class DepartementGraphQLController {

    private final DepartementService departementService;

    public DepartementGraphQLController(DepartementService departementService) {
        this.departementService = departementService;
    }

    @QueryMapping
    public DepartementDTO departement(@Argument Long id) {
        return departementService.findOne(id).orElse(null);
    }

    @QueryMapping
    public List<DepartementDTO> departements() {
        return departementService.findAll();
    }

    @MutationMapping
    public DepartementDTO createDepartement(@Argument DepartementDTO input) {
        return departementService.save(input);
    }

    @MutationMapping
    public DepartementDTO updateDepartement(@Argument Long id, @Argument DepartementDTO input) {
        input.setId(id);
        return departementService.update(input);
    }

    @MutationMapping
    public boolean deleteDepartement(@Argument Long id) {
        departementService.delete(id);
        return true;
    }
}
