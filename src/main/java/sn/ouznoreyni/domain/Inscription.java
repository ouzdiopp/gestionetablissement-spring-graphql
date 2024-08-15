package sn.ouznoreyni.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import sn.ouznoreyni.domain.enumeration.StatutInscription;

/**
 * A Inscription.
 */
@Entity
@Table(name = "inscription")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Inscription implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "date_inscription", nullable = false)
    private LocalDate dateInscription;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false)
    private StatutInscription statut;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "inscription")
    @JsonIgnoreProperties(value = { "inscription" }, allowSetters = true)
    private Set<Note> notes = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user", "inscriptions", "departement", "cours" }, allowSetters = true)
    private Etudiant etudiant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "inscriptions", "departement", "professeur", "etudiants" }, allowSetters = true)
    private Cours cours;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Inscription id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateInscription() {
        return this.dateInscription;
    }

    public Inscription dateInscription(LocalDate dateInscription) {
        this.setDateInscription(dateInscription);
        return this;
    }

    public void setDateInscription(LocalDate dateInscription) {
        this.dateInscription = dateInscription;
    }

    public StatutInscription getStatut() {
        return this.statut;
    }

    public Inscription statut(StatutInscription statut) {
        this.setStatut(statut);
        return this;
    }

    public void setStatut(StatutInscription statut) {
        this.statut = statut;
    }

    public Set<Note> getNotes() {
        return this.notes;
    }

    public void setNotes(Set<Note> notes) {
        if (this.notes != null) {
            this.notes.forEach(i -> i.setInscription(null));
        }
        if (notes != null) {
            notes.forEach(i -> i.setInscription(this));
        }
        this.notes = notes;
    }

    public Inscription notes(Set<Note> notes) {
        this.setNotes(notes);
        return this;
    }

    public Inscription addNote(Note note) {
        this.notes.add(note);
        note.setInscription(this);
        return this;
    }

    public Inscription removeNote(Note note) {
        this.notes.remove(note);
        note.setInscription(null);
        return this;
    }

    public Etudiant getEtudiant() {
        return this.etudiant;
    }

    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }

    public Inscription etudiant(Etudiant etudiant) {
        this.setEtudiant(etudiant);
        return this;
    }

    public Cours getCours() {
        return this.cours;
    }

    public void setCours(Cours cours) {
        this.cours = cours;
    }

    public Inscription cours(Cours cours) {
        this.setCours(cours);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Inscription)) {
            return false;
        }
        return getId() != null && getId().equals(((Inscription) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Inscription{" +
            "id=" + getId() +
            ", dateInscription='" + getDateInscription() + "'" +
            ", statut='" + getStatut() + "'" +
            "}";
    }
}
