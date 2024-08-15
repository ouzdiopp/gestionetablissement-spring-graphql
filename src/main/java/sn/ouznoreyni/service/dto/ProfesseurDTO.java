package sn.ouznoreyni.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link sn.ouznoreyni.domain.Professeur} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProfesseurDTO implements Serializable {

    private Long id;

    @NotNull
    private String numeroEmploye;

    @NotNull
    private LocalDate dateEmbauche;

    @NotNull
    private String specialite;

    private String bureau;

    private UserDTO user;

    private DepartementDTO departement;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroEmploye() {
        return numeroEmploye;
    }

    public void setNumeroEmploye(String numeroEmploye) {
        this.numeroEmploye = numeroEmploye;
    }

    public LocalDate getDateEmbauche() {
        return dateEmbauche;
    }

    public void setDateEmbauche(LocalDate dateEmbauche) {
        this.dateEmbauche = dateEmbauche;
    }

    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }

    public String getBureau() {
        return bureau;
    }

    public void setBureau(String bureau) {
        this.bureau = bureau;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public DepartementDTO getDepartement() {
        return departement;
    }

    public void setDepartement(DepartementDTO departement) {
        this.departement = departement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProfesseurDTO)) {
            return false;
        }

        ProfesseurDTO professeurDTO = (ProfesseurDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, professeurDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProfesseurDTO{" +
            "id=" + getId() +
            ", numeroEmploye='" + getNumeroEmploye() + "'" +
            ", dateEmbauche='" + getDateEmbauche() + "'" +
            ", specialite='" + getSpecialite() + "'" +
            ", bureau='" + getBureau() + "'" +
            ", user=" + getUser() +
            ", departement=" + getDepartement() +
            "}";
    }
}
