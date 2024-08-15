package sn.ouznoreyni.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A Professeur.
 */
@Entity
@Table(name = "professeur")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Professeur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "numero_employe", nullable = false, unique = true)
    private String numeroEmploye;

    @NotNull
    @Column(name = "date_embauche", nullable = false)
    private LocalDate dateEmbauche;

    @NotNull
    @Column(name = "specialite", nullable = false)
    private String specialite;

    @Column(name = "bureau")
    private String bureau;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Departement departement;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Professeur id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroEmploye() {
        return this.numeroEmploye;
    }

    public Professeur numeroEmploye(String numeroEmploye) {
        this.setNumeroEmploye(numeroEmploye);
        return this;
    }

    public void setNumeroEmploye(String numeroEmploye) {
        this.numeroEmploye = numeroEmploye;
    }

    public LocalDate getDateEmbauche() {
        return this.dateEmbauche;
    }

    public Professeur dateEmbauche(LocalDate dateEmbauche) {
        this.setDateEmbauche(dateEmbauche);
        return this;
    }

    public void setDateEmbauche(LocalDate dateEmbauche) {
        this.dateEmbauche = dateEmbauche;
    }

    public String getSpecialite() {
        return this.specialite;
    }

    public Professeur specialite(String specialite) {
        this.setSpecialite(specialite);
        return this;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }

    public String getBureau() {
        return this.bureau;
    }

    public Professeur bureau(String bureau) {
        this.setBureau(bureau);
        return this;
    }

    public void setBureau(String bureau) {
        this.bureau = bureau;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Professeur user(User user) {
        this.setUser(user);
        return this;
    }

    public Departement getDepartement() {
        return this.departement;
    }

    public void setDepartement(Departement departement) {
        this.departement = departement;
    }

    public Professeur departement(Departement departement) {
        this.setDepartement(departement);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Professeur)) {
            return false;
        }
        return getId() != null && getId().equals(((Professeur) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Professeur{" +
            "id=" + getId() +
            ", numeroEmploye='" + getNumeroEmploye() + "'" +
            ", dateEmbauche='" + getDateEmbauche() + "'" +
            ", specialite='" + getSpecialite() + "'" +
            ", bureau='" + getBureau() + "'" +
            "}";
    }
}
