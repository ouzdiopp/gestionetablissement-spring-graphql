package sn.ouznoreyni.web.graphql;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import sn.ouznoreyni.service.CoursService;
import sn.ouznoreyni.service.dto.CoursDTO;

@Controller
public class CoursGraphQLController {

    private final CoursService coursService;

    public CoursGraphQLController(CoursService coursService) {
        this.coursService = coursService;
    }

    @QueryMapping
    public CoursDTO course(@Argument Long id) {
        return coursService.findOne(id).orElse(null);
    }

    @QueryMapping
    public Page<CoursDTO> courses(@Argument Integer page, @Argument Integer size) {
        int pageNumber = (page != null) ? page : 0;
        int pageSize = (size != null) ? size : 20;
        return coursService.findAllWithEagerRelationships(PageRequest.of(pageNumber, pageSize));
    }

    @MutationMapping
    public CoursDTO createCourse(@Argument CoursDTO input) {
        return coursService.save(input);
    }

    @MutationMapping
    public CoursDTO updateCourse(@Argument Long id, @Argument CoursDTO input) {
        input.setId(id);
        return coursService.update(input);
    }

    @MutationMapping
    public boolean deleteCourse(@Argument Long id) {
        coursService.delete(id);
        return true;
    }
}
