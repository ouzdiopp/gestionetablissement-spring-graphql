package sn.ouznoreyni.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link sn.ouznoreyni.domain.Professeur} entity. This class is used
 * in {@link sn.ouznoreyni.web.rest.ProfesseurResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /professeurs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProfesseurCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter numeroEmploye;

    private LocalDateFilter dateEmbauche;

    private StringFilter specialite;

    private StringFilter bureau;

    private LongFilter userId;

    private LongFilter departementId;

    private Boolean distinct;

    public ProfesseurCriteria() {}

    public ProfesseurCriteria(ProfesseurCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.numeroEmploye = other.optionalNumeroEmploye().map(StringFilter::copy).orElse(null);
        this.dateEmbauche = other.optionalDateEmbauche().map(LocalDateFilter::copy).orElse(null);
        this.specialite = other.optionalSpecialite().map(StringFilter::copy).orElse(null);
        this.bureau = other.optionalBureau().map(StringFilter::copy).orElse(null);
        this.userId = other.optionalUserId().map(LongFilter::copy).orElse(null);
        this.departementId = other.optionalDepartementId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ProfesseurCriteria copy() {
        return new ProfesseurCriteria(this);
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

    public StringFilter getNumeroEmploye() {
        return numeroEmploye;
    }

    public Optional<StringFilter> optionalNumeroEmploye() {
        return Optional.ofNullable(numeroEmploye);
    }

    public StringFilter numeroEmploye() {
        if (numeroEmploye == null) {
            setNumeroEmploye(new StringFilter());
        }
        return numeroEmploye;
    }

    public void setNumeroEmploye(StringFilter numeroEmploye) {
        this.numeroEmploye = numeroEmploye;
    }

    public LocalDateFilter getDateEmbauche() {
        return dateEmbauche;
    }

    public Optional<LocalDateFilter> optionalDateEmbauche() {
        return Optional.ofNullable(dateEmbauche);
    }

    public LocalDateFilter dateEmbauche() {
        if (dateEmbauche == null) {
            setDateEmbauche(new LocalDateFilter());
        }
        return dateEmbauche;
    }

    public void setDateEmbauche(LocalDateFilter dateEmbauche) {
        this.dateEmbauche = dateEmbauche;
    }

    public StringFilter getSpecialite() {
        return specialite;
    }

    public Optional<StringFilter> optionalSpecialite() {
        return Optional.ofNullable(specialite);
    }

    public StringFilter specialite() {
        if (specialite == null) {
            setSpecialite(new StringFilter());
        }
        return specialite;
    }

    public void setSpecialite(StringFilter specialite) {
        this.specialite = specialite;
    }

    public StringFilter getBureau() {
        return bureau;
    }

    public Optional<StringFilter> optionalBureau() {
        return Optional.ofNullable(bureau);
    }

    public StringFilter bureau() {
        if (bureau == null) {
            setBureau(new StringFilter());
        }
        return bureau;
    }

    public void setBureau(StringFilter bureau) {
        this.bureau = bureau;
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
        final ProfesseurCriteria that = (ProfesseurCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(numeroEmploye, that.numeroEmploye) &&
            Objects.equals(dateEmbauche, that.dateEmbauche) &&
            Objects.equals(specialite, that.specialite) &&
            Objects.equals(bureau, that.bureau) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(departementId, that.departementId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, numeroEmploye, dateEmbauche, specialite, bureau, userId, departementId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProfesseurCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNumeroEmploye().map(f -> "numeroEmploye=" + f + ", ").orElse("") +
            optionalDateEmbauche().map(f -> "dateEmbauche=" + f + ", ").orElse("") +
            optionalSpecialite().map(f -> "specialite=" + f + ", ").orElse("") +
            optionalBureau().map(f -> "bureau=" + f + ", ").orElse("") +
            optionalUserId().map(f -> "userId=" + f + ", ").orElse("") +
            optionalDepartementId().map(f -> "departementId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
