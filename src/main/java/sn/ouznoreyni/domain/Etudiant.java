package sn.ouznoreyni.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Etudiant.
 */
@Entity
@Table(name = "etudiant")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Etudiant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "numero_etudiant", nullable = false, unique = true)
    private String numeroEtudiant;

    @NotNull
    @Column(name = "date_naissance", nullable = false)
    private LocalDate dateNaissance;

    @NotNull
    @Column(name = "filiere", nullable = false)
    private String filiere;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "etudiant")
    @JsonIgnoreProperties(value = { "notes", "etudiant", "cours" }, allowSetters = true)
    private Set<Inscription> inscriptions = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private Departement departement;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_etudiant__cours",
        joinColumns = @JoinColumn(name = "etudiant_id"),
        inverseJoinColumns = @JoinColumn(name = "cours_id")
    )
    @JsonIgnoreProperties(value = { "inscriptions", "departement", "professeur", "etudiants" }, allowSetters = true)
    private Set<Cours> cours = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Etudiant id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroEtudiant() {
        return this.numeroEtudiant;
    }

    public Etudiant numeroEtudiant(String numeroEtudiant) {
        this.setNumeroEtudiant(numeroEtudiant);
        return this;
    }

    public void setNumeroEtudiant(String numeroEtudiant) {
        this.numeroEtudiant = numeroEtudiant;
    }

    public LocalDate getDateNaissance() {
        return this.dateNaissance;
    }

    public Etudiant dateNaissance(LocalDate dateNaissance) {
        this.setDateNaissance(dateNaissance);
        return this;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getFiliere() {
        return this.filiere;
    }

    public Etudiant filiere(String filiere) {
        this.setFiliere(filiere);
        return this;
    }

    public void setFiliere(String filiere) {
        this.filiere = filiere;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Etudiant user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<Inscription> getInscriptions() {
        return this.inscriptions;
    }

    public void setInscriptions(Set<Inscription> inscriptions) {
        if (this.inscriptions != null) {
            this.inscriptions.forEach(i -> i.setEtudiant(null));
        }
        if (inscriptions != null) {
            inscriptions.forEach(i -> i.setEtudiant(this));
        }
        this.inscriptions = inscriptions;
    }

    public Etudiant inscriptions(Set<Inscription> inscriptions) {
        this.setInscriptions(inscriptions);
        return this;
    }

    public Etudiant addInscription(Inscription inscription) {
        this.inscriptions.add(inscription);
        inscription.setEtudiant(this);
        return this;
    }

    public Etudiant removeInscription(Inscription inscription) {
        this.inscriptions.remove(inscription);
        inscription.setEtudiant(null);
        return this;
    }

    public Departement getDepartement() {
        return this.departement;
    }

    public void setDepartement(Departement departement) {
        this.departement = departement;
    }

    public Etudiant departement(Departement departement) {
        this.setDepartement(departement);
        return this;
    }

    public Set<Cours> getCours() {
        return this.cours;
    }

    public void setCours(Set<Cours> cours) {
        this.cours = cours;
    }

    public Etudiant cours(Set<Cours> cours) {
        this.setCours(cours);
        return this;
    }

    public Etudiant addCours(Cours cours) {
        this.cours.add(cours);
        return this;
    }

    public Etudiant removeCours(Cours cours) {
        this.cours.remove(cours);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Etudiant)) {
            return false;
        }
        return getId() != null && getId().equals(((Etudiant) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Etudiant{" +
            "id=" + getId() +
            ", numeroEtudiant='" + getNumeroEtudiant() + "'" +
            ", dateNaissance='" + getDateNaissance() + "'" +
            ", filiere='" + getFiliere() + "'" +
            "}";
    }
}
