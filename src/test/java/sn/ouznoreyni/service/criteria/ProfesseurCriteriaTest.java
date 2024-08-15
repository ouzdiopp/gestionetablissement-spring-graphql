package sn.ouznoreyni.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ProfesseurCriteriaTest {

    @Test
    void newProfesseurCriteriaHasAllFiltersNullTest() {
        var professeurCriteria = new ProfesseurCriteria();
        assertThat(professeurCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void professeurCriteriaFluentMethodsCreatesFiltersTest() {
        var professeurCriteria = new ProfesseurCriteria();

        setAllFilters(professeurCriteria);

        assertThat(professeurCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void professeurCriteriaCopyCreatesNullFilterTest() {
        var professeurCriteria = new ProfesseurCriteria();
        var copy = professeurCriteria.copy();

        assertThat(professeurCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(professeurCriteria)
        );
    }

    @Test
    void professeurCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var professeurCriteria = new ProfesseurCriteria();
        setAllFilters(professeurCriteria);

        var copy = professeurCriteria.copy();

        assertThat(professeurCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(professeurCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var professeurCriteria = new ProfesseurCriteria();

        assertThat(professeurCriteria).hasToString("ProfesseurCriteria{}");
    }

    private static void setAllFilters(ProfesseurCriteria professeurCriteria) {
        professeurCriteria.id();
        professeurCriteria.numeroEmploye();
        professeurCriteria.dateEmbauche();
        professeurCriteria.specialite();
        professeurCriteria.bureau();
        professeurCriteria.userId();
        professeurCriteria.departementId();
        professeurCriteria.distinct();
    }

    private static Condition<ProfesseurCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNumeroEmploye()) &&
                condition.apply(criteria.getDateEmbauche()) &&
                condition.apply(criteria.getSpecialite()) &&
                condition.apply(criteria.getBureau()) &&
                condition.apply(criteria.getUserId()) &&
                condition.apply(criteria.getDepartementId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ProfesseurCriteria> copyFiltersAre(ProfesseurCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNumeroEmploye(), copy.getNumeroEmploye()) &&
                condition.apply(criteria.getDateEmbauche(), copy.getDateEmbauche()) &&
                condition.apply(criteria.getSpecialite(), copy.getSpecialite()) &&
                condition.apply(criteria.getBureau(), copy.getBureau()) &&
                condition.apply(criteria.getUserId(), copy.getUserId()) &&
                condition.apply(criteria.getDepartementId(), copy.getDepartementId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
