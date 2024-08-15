package sn.ouznoreyni.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ouznoreyni.domain.CoursTestSamples.*;
import static sn.ouznoreyni.domain.DepartementTestSamples.*;
import static sn.ouznoreyni.domain.EtudiantTestSamples.*;
import static sn.ouznoreyni.domain.InscriptionTestSamples.*;
import static sn.ouznoreyni.domain.ProfesseurTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import sn.ouznoreyni.web.rest.TestUtil;

class CoursTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cours.class);
        Cours cours1 = getCoursSample1();
        Cours cours2 = new Cours();
        assertThat(cours1).isNotEqualTo(cours2);

        cours2.setId(cours1.getId());
        assertThat(cours1).isEqualTo(cours2);

        cours2 = getCoursSample2();
        assertThat(cours1).isNotEqualTo(cours2);
    }

    @Test
    void inscriptionTest() throws Exception {
        Cours cours = getCoursRandomSampleGenerator();
        Inscription inscriptionBack = getInscriptionRandomSampleGenerator();

        cours.addInscription(inscriptionBack);
        assertThat(cours.getInscriptions()).containsOnly(inscriptionBack);
        assertThat(inscriptionBack.getCours()).isEqualTo(cours);

        cours.removeInscription(inscriptionBack);
        assertThat(cours.getInscriptions()).doesNotContain(inscriptionBack);
        assertThat(inscriptionBack.getCours()).isNull();

        cours.inscriptions(new HashSet<>(Set.of(inscriptionBack)));
        assertThat(cours.getInscriptions()).containsOnly(inscriptionBack);
        assertThat(inscriptionBack.getCours()).isEqualTo(cours);

        cours.setInscriptions(new HashSet<>());
        assertThat(cours.getInscriptions()).doesNotContain(inscriptionBack);
        assertThat(inscriptionBack.getCours()).isNull();
    }

    @Test
    void departementTest() throws Exception {
        Cours cours = getCoursRandomSampleGenerator();
        Departement departementBack = getDepartementRandomSampleGenerator();

        cours.setDepartement(departementBack);
        assertThat(cours.getDepartement()).isEqualTo(departementBack);

        cours.departement(null);
        assertThat(cours.getDepartement()).isNull();
    }

    @Test
    void professeurTest() throws Exception {
        Cours cours = getCoursRandomSampleGenerator();
        Professeur professeurBack = getProfesseurRandomSampleGenerator();

        cours.setProfesseur(professeurBack);
        assertThat(cours.getProfesseur()).isEqualTo(professeurBack);

        cours.professeur(null);
        assertThat(cours.getProfesseur()).isNull();
    }

    @Test
    void etudiantsTest() throws Exception {
        Cours cours = getCoursRandomSampleGenerator();
        Etudiant etudiantBack = getEtudiantRandomSampleGenerator();

        cours.addEtudiants(etudiantBack);
        assertThat(cours.getEtudiants()).containsOnly(etudiantBack);
        assertThat(etudiantBack.getCours()).containsOnly(cours);

        cours.removeEtudiants(etudiantBack);
        assertThat(cours.getEtudiants()).doesNotContain(etudiantBack);
        assertThat(etudiantBack.getCours()).doesNotContain(cours);

        cours.etudiants(new HashSet<>(Set.of(etudiantBack)));
        assertThat(cours.getEtudiants()).containsOnly(etudiantBack);
        assertThat(etudiantBack.getCours()).containsOnly(cours);

        cours.setEtudiants(new HashSet<>());
        assertThat(cours.getEtudiants()).doesNotContain(etudiantBack);
        assertThat(etudiantBack.getCours()).doesNotContain(cours);
    }
}
