package sn.ouznoreyni.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link sn.ouznoreyni.domain.Etudiant} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EtudiantDTO implements Serializable {

    private Long id;

    @NotNull
    private String numeroEtudiant;

    @NotNull
    private LocalDate dateNaissance;

    @NotNull
    private String filiere;

    private UserDTO user;

    private DepartementDTO departement;

    private Set<CoursDTO> cours = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroEtudiant() {
        return numeroEtudiant;
    }

    public void setNumeroEtudiant(String numeroEtudiant) {
        this.numeroEtudiant = numeroEtudiant;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getFiliere() {
        return filiere;
    }

    public void setFiliere(String filiere) {
        this.filiere = filiere;
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

    public Set<CoursDTO> getCours() {
        return cours;
    }

    public void setCours(Set<CoursDTO> cours) {
        this.cours = cours;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EtudiantDTO)) {
            return false;
        }

        EtudiantDTO etudiantDTO = (EtudiantDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, etudiantDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EtudiantDTO{" +
            "id=" + getId() +
            ", numeroEtudiant='" + getNumeroEtudiant() + "'" +
            ", dateNaissance='" + getDateNaissance() + "'" +
            ", filiere='" + getFiliere() + "'" +
            ", user=" + getUser() +
            ", departement=" + getDepartement() +
            ", cours=" + getCours() +
            "}";
    }
}
