package sn.ouznoreyni;

import java.time.LocalDate;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sn.ouznoreyni.domain.*;
import sn.ouznoreyni.domain.enumeration.StatutInscription;
import sn.ouznoreyni.repository.*;
import sn.ouznoreyni.service.UserService;
import sn.ouznoreyni.service.dto.AdminUserDTO;

@Configuration
public class DataSeeder {

    private final Logger log = LoggerFactory.getLogger(DataSeeder.class);
    private final String PASSWORD = "password";

    @Bean
    public CommandLineRunner initDatabase(
        DepartementRepository departementRepository,
        ProfesseurRepository professeurRepository,
        EtudiantRepository etudiantRepository,
        CoursRepository coursRepository,
        InscriptionRepository inscriptionRepository,
        NoteRepository noteRepository,
        UserRepository userRepository,
        UserService userService
    ) {
        return args -> {
            // Seed Departements
            Departement informatique = createDepartementIfNotExist(departementRepository, "Informatique", "Département d'informatique");
            Departement maths = createDepartementIfNotExist(departementRepository, "Mathématiques", "Département de mathématiques");

            // Seed Professeurs
            User userProf1 = createUserIfNotExist(userService, "profi1", "profi1@mgail.com", "Omar", "Faye");
            Professeur prof1 = createProfesseurIfNotExist(
                professeurRepository,
                userProf1,
                "P001",
                LocalDate.of(2020, 1, 1),
                "Java",
                "B101",
                informatique
            );

            // Seed Etudiants
            User userEtudiant1 = createUserIfNotExist(userService, "etudiant1", "etudiant1@mgail.com", "Fallou", "DIOP");
            Etudiant etudiant1 = createEtudiantIfNotExist(
                etudiantRepository,
                userEtudiant1,
                "E001",
                LocalDate.of(2000, 1, 1),
                "Informatique",
                informatique
            );

            // Seed Cours
            Cours coursJava = createCoursIfNotExist(
                coursRepository,
                "INFO101",
                "Introduction à Java",
                "Cours d'introduction à la programmation Java",
                5,
                informatique,
                prof1
            );

            // Seed Inscriptions
            Inscription inscription1 = createInscriptionIfNotExist(inscriptionRepository, etudiant1, coursJava);

            // Seed Notes
            createNoteIfNotExist(noteRepository, 15.5f, "Bon travail", inscription1);

            log.info("Database seeded!");
        };
    }

    private Departement createDepartementIfNotExist(DepartementRepository repository, String nom, String description) {
        return repository
            .findByNom(nom)
            .orElseGet(() -> {
                Departement departement = new Departement();
                departement.setNom(nom);
                departement.setDescription(description);
                return repository.save(departement);
            });
    }

    private User createUserIfNotExist(UserService userService, String login, String email, String firstName, String lastName) {
        return userService
            .getUserWithAuthoritiesByLogin(login)
            .orElseGet(() -> {
                AdminUserDTO userDTO = new AdminUserDTO();
                userDTO.setLogin(login);
                userDTO.setEmail(email);
                userDTO.setFirstName(firstName);
                userDTO.setLastName(lastName);
                userDTO.setActivated(true);
                return userService.registerUser(userDTO, PASSWORD);
            });
    }

    private Professeur createProfesseurIfNotExist(
        ProfesseurRepository repository,
        User user,
        String numeroEmploye,
        LocalDate dateEmbauche,
        String specialite,
        String bureau,
        Departement departement
    ) {
        return repository
            .findByUser(user)
            .orElseGet(() -> {
                Professeur professeur = new Professeur();
                professeur.setUser(user);
                professeur.setNumeroEmploye(numeroEmploye);
                professeur.setDateEmbauche(dateEmbauche);
                professeur.setSpecialite(specialite);
                professeur.setBureau(bureau);
                professeur.setDepartement(departement);
                return repository.save(professeur);
            });
    }

    private Etudiant createEtudiantIfNotExist(
        EtudiantRepository repository,
        User user,
        String numeroEtudiant,
        LocalDate dateNaissance,
        String filiere,
        Departement departement
    ) {
        return repository
            .findByUser(user)
            .orElseGet(() -> {
                Etudiant etudiant = new Etudiant();
                etudiant.setUser(user);
                etudiant.setNumeroEtudiant(numeroEtudiant);
                etudiant.setDateNaissance(dateNaissance);
                etudiant.setFiliere(filiere);
                etudiant.setDepartement(departement);
                return repository.save(etudiant);
            });
    }

    private Cours createCoursIfNotExist(
        CoursRepository repository,
        String code,
        String intitule,
        String description,
        Integer credits,
        Departement departement,
        Professeur professeur
    ) {
        return repository
            .findByCode(code)
            .orElseGet(() -> {
                Cours cours = new Cours();
                cours.setCode(code);
                cours.setIntitule(intitule);
                cours.setDescription(description);
                cours.setCredits(credits);
                cours.setDepartement(departement);
                cours.setProfesseur(professeur);
                return repository.save(cours);
            });
    }

    private Inscription createInscriptionIfNotExist(InscriptionRepository repository, Etudiant etudiant, Cours cours) {
        return repository
            .findByEtudiantAndCours(etudiant, cours)
            .orElseGet(() -> {
                Inscription inscription = new Inscription();
                inscription.setDateInscription(LocalDate.now());
                inscription.setStatut(StatutInscription.INSCRIT);
                inscription.setEtudiant(etudiant);
                inscription.setCours(cours);
                return repository.save(inscription);
            });
    }

    private Note createNoteIfNotExist(NoteRepository repository, float valeur, String commentaire, Inscription inscription) {
        return repository
            .findByInscription(inscription)
            .orElseGet(() -> {
                Note note = new Note();
                note.setValeur(valeur);
                note.setCommentaire(commentaire);
                note.setInscription(inscription);
                return repository.save(note);
            });
    }
}
