package sn.ouznoreyni.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CoursCriteriaTest {

    @Test
    void newCoursCriteriaHasAllFiltersNullTest() {
        var coursCriteria = new CoursCriteria();
        assertThat(coursCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void coursCriteriaFluentMethodsCreatesFiltersTest() {
        var coursCriteria = new CoursCriteria();

        setAllFilters(coursCriteria);

        assertThat(coursCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void coursCriteriaCopyCreatesNullFilterTest() {
        var coursCriteria = new CoursCriteria();
        var copy = coursCriteria.copy();

        assertThat(coursCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(coursCriteria)
        );
    }

    @Test
    void coursCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var coursCriteria = new CoursCriteria();
        setAllFilters(coursCriteria);

        var copy = coursCriteria.copy();

        assertThat(coursCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(coursCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var coursCriteria = new CoursCriteria();

        assertThat(coursCriteria).hasToString("CoursCriteria{}");
    }

    private static void setAllFilters(CoursCriteria coursCriteria) {
        coursCriteria.id();
        coursCriteria.code();
        coursCriteria.intitule();
        coursCriteria.credits();
        coursCriteria.inscriptionId();
        coursCriteria.departementId();
        coursCriteria.professeurId();
        coursCriteria.etudiantsId();
        coursCriteria.distinct();
    }

    private static Condition<CoursCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCode()) &&
                condition.apply(criteria.getIntitule()) &&
                condition.apply(criteria.getCredits()) &&
                condition.apply(criteria.getInscriptionId()) &&
                condition.apply(criteria.getDepartementId()) &&
                condition.apply(criteria.getProfesseurId()) &&
                condition.apply(criteria.getEtudiantsId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CoursCriteria> copyFiltersAre(CoursCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCode(), copy.getCode()) &&
                condition.apply(criteria.getIntitule(), copy.getIntitule()) &&
                condition.apply(criteria.getCredits(), copy.getCredits()) &&
                condition.apply(criteria.getInscriptionId(), copy.getInscriptionId()) &&
                condition.apply(criteria.getDepartementId(), copy.getDepartementId()) &&
                condition.apply(criteria.getProfesseurId(), copy.getProfesseurId()) &&
                condition.apply(criteria.getEtudiantsId(), copy.getEtudiantsId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
