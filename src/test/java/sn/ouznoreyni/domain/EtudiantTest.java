package sn.ouznoreyni.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ouznoreyni.domain.CoursTestSamples.*;
import static sn.ouznoreyni.domain.DepartementTestSamples.*;
import static sn.ouznoreyni.domain.EtudiantTestSamples.*;
import static sn.ouznoreyni.domain.InscriptionTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import sn.ouznoreyni.web.rest.TestUtil;

class EtudiantTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Etudiant.class);
        Etudiant etudiant1 = getEtudiantSample1();
        Etudiant etudiant2 = new Etudiant();
        assertThat(etudiant1).isNotEqualTo(etudiant2);

        etudiant2.setId(etudiant1.getId());
        assertThat(etudiant1).isEqualTo(etudiant2);

        etudiant2 = getEtudiantSample2();
        assertThat(etudiant1).isNotEqualTo(etudiant2);
    }

    @Test
    void inscriptionTest() throws Exception {
        Etudiant etudiant = getEtudiantRandomSampleGenerator();
        Inscription inscriptionBack = getInscriptionRandomSampleGenerator();

        etudiant.addInscription(inscriptionBack);
        assertThat(etudiant.getInscriptions()).containsOnly(inscriptionBack);
        assertThat(inscriptionBack.getEtudiant()).isEqualTo(etudiant);

        etudiant.removeInscription(inscriptionBack);
        assertThat(etudiant.getInscriptions()).doesNotContain(inscriptionBack);
        assertThat(inscriptionBack.getEtudiant()).isNull();

        etudiant.inscriptions(new HashSet<>(Set.of(inscriptionBack)));
        assertThat(etudiant.getInscriptions()).containsOnly(inscriptionBack);
        assertThat(inscriptionBack.getEtudiant()).isEqualTo(etudiant);

        etudiant.setInscriptions(new HashSet<>());
        assertThat(etudiant.getInscriptions()).doesNotContain(inscriptionBack);
        assertThat(inscriptionBack.getEtudiant()).isNull();
    }

    @Test
    void departementTest() throws Exception {
        Etudiant etudiant = getEtudiantRandomSampleGenerator();
        Departement departementBack = getDepartementRandomSampleGenerator();

        etudiant.setDepartement(departementBack);
        assertThat(etudiant.getDepartement()).isEqualTo(departementBack);

        etudiant.departement(null);
        assertThat(etudiant.getDepartement()).isNull();
    }

    @Test
    void coursTest() throws Exception {
        Etudiant etudiant = getEtudiantRandomSampleGenerator();
        Cours coursBack = getCoursRandomSampleGenerator();

        etudiant.addCours(coursBack);
        assertThat(etudiant.getCours()).containsOnly(coursBack);

        etudiant.removeCours(coursBack);
        assertThat(etudiant.getCours()).doesNotContain(coursBack);

        etudiant.cours(new HashSet<>(Set.of(coursBack)));
        assertThat(etudiant.getCours()).containsOnly(coursBack);

        etudiant.setCours(new HashSet<>());
        assertThat(etudiant.getCours()).doesNotContain(coursBack);
    }
}
