package sn.ouznoreyni.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link sn.ouznoreyni.domain.Etudiant} entity. This class is used
 * in {@link sn.ouznoreyni.web.rest.EtudiantResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /etudiants?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EtudiantCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter numeroEtudiant;

    private LocalDateFilter dateNaissance;

    private StringFilter filiere;

    private LongFilter userId;

    private LongFilter inscriptionId;

    private LongFilter departementId;

    private LongFilter coursId;

    private Boolean distinct;

    public EtudiantCriteria() {}

    public EtudiantCriteria(EtudiantCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.numeroEtudiant = other.optionalNumeroEtudiant().map(StringFilter::copy).orElse(null);
        this.dateNaissance = other.optionalDateNaissance().map(LocalDateFilter::copy).orElse(null);
        this.filiere = other.optionalFiliere().map(StringFilter::copy).orElse(null);
        this.userId = other.optionalUserId().map(LongFilter::copy).orElse(null);
        this.inscriptionId = other.optionalInscriptionId().map(LongFilter::copy).orElse(null);
        this.departementId = other.optionalDepartementId().map(LongFilter::copy).orElse(null);
        this.coursId = other.optionalCoursId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public EtudiantCriteria copy() {
        return new EtudiantCriteria(this);
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

    public StringFilter getNumeroEtudiant() {
        return numeroEtudiant;
    }

    public Optional<StringFilter> optionalNumeroEtudiant() {
        return Optional.ofNullable(numeroEtudiant);
    }

    public StringFilter numeroEtudiant() {
        if (numeroEtudiant == null) {
            setNumeroEtudiant(new StringFilter());
        }
        return numeroEtudiant;
    }

    public void setNumeroEtudiant(StringFilter numeroEtudiant) {
        this.numeroEtudiant = numeroEtudiant;
    }

    public LocalDateFilter getDateNaissance() {
        return dateNaissance;
    }

    public Optional<LocalDateFilter> optionalDateNaissance() {
        return Optional.ofNullable(dateNaissance);
    }

    public LocalDateFilter dateNaissance() {
        if (dateNaissance == null) {
            setDateNaissance(new LocalDateFilter());
        }
        return dateNaissance;
    }

    public void setDateNaissance(LocalDateFilter dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public StringFilter getFiliere() {
        return filiere;
    }

    public Optional<StringFilter> optionalFiliere() {
        return Optional.ofNullable(filiere);
    }

    public StringFilter filiere() {
        if (filiere == null) {
            setFiliere(new StringFilter());
        }
        return filiere;
    }

    public void setFiliere(StringFilter filiere) {
        this.filiere = filiere;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public Optional<LongFilter> optionalUserId() {
        return Optional.ofNullable(userId);
    }

    public LongFilter userId() {
        if (userId == null) {
            setUserId(new LongFilter());
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
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

    public LongFilter getCoursId() {
        return coursId;
    }

    public Optional<LongFilter> optionalCoursId() {
        return Optional.ofNullable(coursId);
    }

    public LongFilter coursId() {
        if (coursId == null) {
            setCoursId(new LongFilter());
        }
        return coursId;
    }

    public void setCoursId(LongFilter coursId) {
        this.coursId = coursId;
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
        final EtudiantCriteria that = (EtudiantCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(numeroEtudiant, that.numeroEtudiant) &&
            Objects.equals(dateNaissance, that.dateNaissance) &&
            Objects.equals(filiere, that.filiere) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(inscriptionId, that.inscriptionId) &&
            Objects.equals(departementId, that.departementId) &&
            Objects.equals(coursId, that.coursId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, numeroEtudiant, dateNaissance, filiere, userId, inscriptionId, departementId, coursId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EtudiantCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNumeroEtudiant().map(f -> "numeroEtudiant=" + f + ", ").orElse("") +
            optionalDateNaissance().map(f -> "dateNaissance=" + f + ", ").orElse("") +
            optionalFiliere().map(f -> "filiere=" + f + ", ").orElse("") +
            optionalUserId().map(f -> "userId=" + f + ", ").orElse("") +
            optionalInscriptionId().map(f -> "inscriptionId=" + f + ", ").orElse("") +
            optionalDepartementId().map(f -> "departementId=" + f + ", ").orElse("") +
            optionalCoursId().map(f -> "coursId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
