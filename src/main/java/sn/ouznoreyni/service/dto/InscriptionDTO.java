package sn.ouznoreyni.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import sn.ouznoreyni.domain.enumeration.StatutInscription;

/**
 * A DTO for the {@link sn.ouznoreyni.domain.Inscription} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InscriptionDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate dateInscription;

    @NotNull
    private StatutInscription statut;

    private EtudiantDTO etudiant;

    private CoursDTO cours;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(LocalDate dateInscription) {
        this.dateInscription = dateInscription;
    }

    public StatutInscription getStatut() {
        return statut;
    }

    public void setStatut(StatutInscription statut) {
        this.statut = statut;
    }

    public EtudiantDTO getEtudiant() {
        return etudiant;
    }

    public void setEtudiant(EtudiantDTO etudiant) {
        this.etudiant = etudiant;
    }

    public CoursDTO getCours() {
        return cours;
    }

    public void setCours(CoursDTO cours) {
        this.cours = cours;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InscriptionDTO)) {
            return false;
        }

        InscriptionDTO inscriptionDTO = (InscriptionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, inscriptionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InscriptionDTO{" +
            "id=" + getId() +
            ", dateInscription='" + getDateInscription() + "'" +
            ", statut='" + getStatut() + "'" +
            ", etudiant=" + getEtudiant() +
            ", cours=" + getCours() +
            "}";
    }
}
