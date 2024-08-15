package sn.ouznoreyni.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Cours.
 */
@Entity
@Table(name = "cours")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Cours implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @NotNull
    @Column(name = "intitule", nullable = false)
    private String intitule;

    @Lob
    @Column(name = "description")
    private String description;

    @NotNull
    @Min(value = 1)
    @Max(value = 30)
    @Column(name = "credits", nullable = false)
    private Integer credits;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cours")
    @JsonIgnoreProperties(value = { "notes", "etudiant", "cours" }, allowSetters = true)
    private Set<Inscription> inscriptions = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private Departement departement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user", "departement" }, allowSetters = true)
    private Professeur professeur;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "cours")
    @JsonIgnoreProperties(value = { "user", "inscriptions", "departement", "cours" }, allowSetters = true)
    private Set<Etudiant> etudiants = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Cours id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public Cours code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIntitule() {
        return this.intitule;
    }

    public Cours intitule(String intitule) {
        this.setIntitule(intitule);
        return this;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public String getDescription() {
        return this.description;
    }

    public Cours description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCredits() {
        return this.credits;
    }

    public Cours credits(Integer credits) {
        this.setCredits(credits);
        return this;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    public Set<Inscription> getInscriptions() {
        return this.inscriptions;
    }

    public void setInscriptions(Set<Inscription> inscriptions) {
        if (this.inscriptions != null) {
            this.inscriptions.forEach(i -> i.setCours(null));
        }
        if (inscriptions != null) {
            inscriptions.forEach(i -> i.setCours(this));
        }
        this.inscriptions = inscriptions;
    }

    public Cours inscriptions(Set<Inscription> inscriptions) {
        this.setInscriptions(inscriptions);
        return this;
    }

    public Cours addInscription(Inscription inscription) {
        this.inscriptions.add(inscription);
        inscription.setCours(this);
        return this;
    }

    public Cours removeInscription(Inscription inscription) {
        this.inscriptions.remove(inscription);
        inscription.setCours(null);
        return this;
    }

    public Departement getDepartement() {
        return this.departement;
    }

    public void setDepartement(Departement departement) {
        this.departement = departement;
    }

    public Cours departement(Departement departement) {
        this.setDepartement(departement);
        return this;
    }

    public Professeur getProfesseur() {
        return this.professeur;
    }

    public void setProfesseur(Professeur professeur) {
        this.professeur = professeur;
    }

    public Cours professeur(Professeur professeur) {
        this.setProfesseur(professeur);
        return this;
    }

    public Set<Etudiant> getEtudiants() {
        return this.etudiants;
    }

    public void setEtudiants(Set<Etudiant> etudiants) {
        if (this.etudiants != null) {
            this.etudiants.forEach(i -> i.removeCours(this));
        }
        if (etudiants != null) {
            etudiants.forEach(i -> i.addCours(this));
        }
        this.etudiants = etudiants;
    }

    public Cours etudiants(Set<Etudiant> etudiants) {
        this.setEtudiants(etudiants);
        return this;
    }

    public Cours addEtudiants(Etudiant etudiant) {
        this.etudiants.add(etudiant);
        etudiant.getCours().add(this);
        return this;
    }

    public Cours removeEtudiants(Etudiant etudiant) {
        this.etudiants.remove(etudiant);
        etudiant.getCours().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cours)) {
            return false;
        }
        return getId() != null && getId().equals(((Cours) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cours{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", intitule='" + getIntitule() + "'" +
            ", description='" + getDescription() + "'" +
            ", credits=" + getCredits() +
            "}";
    }
}
