package sn.ouznoreyni.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ouznoreyni.domain.InscriptionTestSamples.*;
import static sn.ouznoreyni.domain.NoteTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.ouznoreyni.web.rest.TestUtil;

class NoteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Note.class);
        Note note1 = getNoteSample1();
        Note note2 = new Note();
        assertThat(note1).isNotEqualTo(note2);

        note2.setId(note1.getId());
        assertThat(note1).isEqualTo(note2);

        note2 = getNoteSample2();
        assertThat(note1).isNotEqualTo(note2);
    }

    @Test
    void inscriptionTest() throws Exception {
        Note note = getNoteRandomSampleGenerator();
        Inscription inscriptionBack = getInscriptionRandomSampleGenerator();

        note.setInscription(inscriptionBack);
        assertThat(note.getInscription()).isEqualTo(inscriptionBack);

        note.inscription(null);
        assertThat(note.getInscription()).isNull();
    }
}
