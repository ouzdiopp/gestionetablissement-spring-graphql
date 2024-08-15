package sn.ouznoreyni.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ouznoreyni.domain.CoursTestSamples.*;
import static sn.ouznoreyni.domain.EtudiantTestSamples.*;
import static sn.ouznoreyni.domain.InscriptionTestSamples.*;
import static sn.ouznoreyni.domain.NoteTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import sn.ouznoreyni.web.rest.TestUtil;

class InscriptionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Inscription.class);
        Inscription inscription1 = getInscriptionSample1();
        Inscription inscription2 = new Inscription();
        assertThat(inscription1).isNotEqualTo(inscription2);

        inscription2.setId(inscription1.getId());
        assertThat(inscription1).isEqualTo(inscription2);

        inscription2 = getInscriptionSample2();
        assertThat(inscription1).isNotEqualTo(inscription2);
    }

    @Test
    void noteTest() throws Exception {
        Inscription inscription = getInscriptionRandomSampleGenerator();
        Note noteBack = getNoteRandomSampleGenerator();

        inscription.addNote(noteBack);
        assertThat(inscription.getNotes()).containsOnly(noteBack);
        assertThat(noteBack.getInscription()).isEqualTo(inscription);

        inscription.removeNote(noteBack);
        assertThat(inscription.getNotes()).doesNotContain(noteBack);
        assertThat(noteBack.getInscription()).isNull();

        inscription.notes(new HashSet<>(Set.of(noteBack)));
        assertThat(inscription.getNotes()).containsOnly(noteBack);
        assertThat(noteBack.getInscription()).isEqualTo(inscription);

        inscription.setNotes(new HashSet<>());
        assertThat(inscription.getNotes()).doesNotContain(noteBack);
        assertThat(noteBack.getInscription()).isNull();
    }

    @Test
    void etudiantTest() throws Exception {
        Inscription inscription = getInscriptionRandomSampleGenerator();
        Etudiant etudiantBack = getEtudiantRandomSampleGenerator();

        inscription.setEtudiant(etudiantBack);
        assertThat(inscription.getEtudiant()).isEqualTo(etudiantBack);

        inscription.etudiant(null);
        assertThat(inscription.getEtudiant()).isNull();
    }

    @Test
    void coursTest() throws Exception {
        Inscription inscription = getInscriptionRandomSampleGenerator();
        Cours coursBack = getCoursRandomSampleGenerator();

        inscription.setCours(coursBack);
        assertThat(inscription.getCours()).isEqualTo(coursBack);

        inscription.cours(null);
        assertThat(inscription.getCours()).isNull();
    }
}
