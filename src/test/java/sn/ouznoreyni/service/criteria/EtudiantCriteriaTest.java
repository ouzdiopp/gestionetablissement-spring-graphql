package sn.ouznoreyni.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class EtudiantCriteriaTest {

    @Test
    void newEtudiantCriteriaHasAllFiltersNullTest() {
        var etudiantCriteria = new EtudiantCriteria();
        assertThat(etudiantCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void etudiantCriteriaFluentMethodsCreatesFiltersTest() {
        var etudiantCriteria = new EtudiantCriteria();

        setAllFilters(etudiantCriteria);

        assertThat(etudiantCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void etudiantCriteriaCopyCreatesNullFilterTest() {
        var etudiantCriteria = new EtudiantCriteria();
        var copy = etudiantCriteria.copy();

        assertThat(etudiantCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(etudiantCriteria)
        );
    }

    @Test
    void etudiantCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var etudiantCriteria = new EtudiantCriteria();
        setAllFilters(etudiantCriteria);

        var copy = etudiantCriteria.copy();

        assertThat(etudiantCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(etudiantCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var etudiantCriteria = new EtudiantCriteria();

        assertThat(etudiantCriteria).hasToString("EtudiantCriteria{}");
    }

    private static void setAllFilters(EtudiantCriteria etudiantCriteria) {
        etudiantCriteria.id();
        etudiantCriteria.numeroEtudiant();
        etudiantCriteria.dateNaissance();
        etudiantCriteria.filiere();
        etudiantCriteria.userId();
        etudiantCriteria.inscriptionId();
        etudiantCriteria.departementId();
        etudiantCriteria.coursId();
        etudiantCriteria.distinct();
    }

    private static Condition<EtudiantCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNumeroEtudiant()) &&
                condition.apply(criteria.getDateNaissance()) &&
                condition.apply(criteria.getFiliere()) &&
                condition.apply(criteria.getUserId()) &&
                condition.apply(criteria.getInscriptionId()) &&
                condition.apply(criteria.getDepartementId()) &&
                condition.apply(criteria.getCoursId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<EtudiantCriteria> copyFiltersAre(EtudiantCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNumeroEtudiant(), copy.getNumeroEtudiant()) &&
                condition.apply(criteria.getDateNaissance(), copy.getDateNaissance()) &&
                condition.apply(criteria.getFiliere(), copy.getFiliere()) &&
                condition.apply(criteria.getUserId(), copy.getUserId()) &&
                condition.apply(criteria.getInscriptionId(), copy.getInscriptionId()) &&
                condition.apply(criteria.getDepartementId(), copy.getDepartementId()) &&
                condition.apply(criteria.getCoursId(), copy.getCoursId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
