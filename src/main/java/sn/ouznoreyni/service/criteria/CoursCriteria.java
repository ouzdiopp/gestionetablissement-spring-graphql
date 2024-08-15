package sn.ouznoreyni.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link sn.ouznoreyni.domain.Cours} entity. This class is used
 * in {@link sn.ouznoreyni.web.rest.CoursResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cours?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CoursCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter code;

    private StringFilter intitule;

    private IntegerFilter credits;

    private LongFilter inscriptionId;

    private LongFilter departementId;

    private LongFilter professeurId;

    private LongFilter etudiantsId;

    private Boolean distinct;

    public CoursCriteria() {}

    public CoursCriteria(CoursCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.code = other.optionalCode().map(StringFilter::copy).orElse(null);
        this.intitule = other.optionalIntitule().map(StringFilter::copy).orElse(null);
        this.credits = other.optionalCredits().map(IntegerFilter::copy).orElse(null);
        this.inscriptionId = other.optionalInscriptionId().map(LongFilter::copy).orElse(null);
        this.departementId = other.optionalDepartementId().map(LongFilter::copy).orElse(null);
        this.professeurId = other.optionalProfesseurId().map(LongFilter::copy).orElse(null);
        this.etudiantsId = other.optionalEtudiantsId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CoursCriteria copy() {
        return new CoursCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getCode() {
        return code;
    }

    public Optional<StringFilter> optionalCode() {
        return Optional.ofNullable(code);
    }

    public StringFilter code() {
        if (code == null) {
            setCode(new StringFilter());
        }
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
    }

    public StringFilter getIntitule() {
        return intitule;
    }

    public Optional<StringFilter> optionalIntitule() {
        return Optional.ofNullable(intitule);
    }

    public StringFilter intitule() {
        if (intitule == null) {
            setIntitule(new StringFilter());
        }
        return intitule;
    }

    public void setIntitule(StringFilter intitule) {
        this.intitule = intitule;
    }

    public IntegerFilter getCredits() {
        return credits;
    }

    public Optional<IntegerFilter> optionalCredits() {
        return Optional.ofNullable(credits);
    }

    public IntegerFilter credits() {
        if (credits == null) {
            setCredits(new IntegerFilter());
        }
        return credits;
    }

    public void setCredits(IntegerFilter credits) {
        this.credits = credits;
    }

    public LongFilter getInscriptionId() {
        return inscriptionId;
    }

    public Optional<LongFilter> optionalInscriptionId() {
        return Optional.ofNullable(inscriptionId);
    }

    public LongFilter inscriptionId() {
        if (inscriptionId == null) {
            setInscriptionId(new LongFilter());
        }
        return inscriptionId;
    }

    public void setInscriptionId(LongFilter inscriptionId) {
        this.inscriptionId = inscriptionId;
    }

    public LongFilter getDepartementId() {
        return departementId;
    }

    public Optional<LongFilter> optionalDepartementId() {
        return Optional.ofNullable(departementId);
    }

    public LongFilter departementId() {
        if (departementId == null) {
            setDepartementId(new LongFilter());
        }
        return departementId;
    }

    public void setDepartementId(LongFilter departementId) {
        this.departementId = departementId;
    }

    public LongFilter getProfesseurId() {
        return professeurId;
    }

    public Optional<LongFilter> optionalProfesseurId() {
        return Optional.ofNullable(professeurId);
    }

    public LongFilter professeurId() {
        if (professeurId == null) {
            setProfesseurId(new LongFilter());
        }
        return professeurId;
    }

    public void setProfesseurId(LongFilter professeurId) {
        this.professeurId = professeurId;
    }

    public LongFilter getEtudiantsId() {
        return etudiantsId;
    }

    public Optional<LongFilter> optionalEtudiantsId() {
        return Optional.ofNullable(etudiantsId);
    }

    public LongFilter etudiantsId() {
        if (etudiantsId == null) {
            setEtudiantsId(new LongFilter());
        }
        return etudiantsId;
    }

    public void setEtudiantsId(LongFilter etudiantsId) {
        this.etudiantsId = etudiantsId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CoursCriteria that = (CoursCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(intitule, that.intitule) &&
            Objects.equals(credits, that.credits) &&
            Objects.equals(inscriptionId, that.inscriptionId) &&
            Objects.equals(departementId, that.departementId) &&
            Objects.equals(professeurId, that.professeurId) &&
            Objects.equals(etudiantsId, that.etudiantsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, intitule, credits, inscriptionId, departementId, professeurId, etudiantsId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CoursCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCode().map(f -> "code=" + f + ", ").orElse("") +
            optionalIntitule().map(f -> "intitule=" + f + ", ").orElse("") +
            optionalCredits().map(f -> "credits=" + f + ", ").orElse("") +
            optionalInscriptionId().map(f -> "inscriptionId=" + f + ", ").orElse("") +
            optionalDepartementId().map(f -> "departementId=" + f + ", ").orElse("") +
            optionalProfesseurId().map(f -> "professeurId=" + f + ", ").orElse("") +
            optionalEtudiantsId().map(f -> "etudiantsId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
