package sn.ouznoreyni.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ouznoreyni.domain.DepartementTestSamples.*;
import static sn.ouznoreyni.domain.ProfesseurTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.ouznoreyni.web.rest.TestUtil;

class ProfesseurTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Professeur.class);
        Professeur professeur1 = getProfesseurSample1();
        Professeur professeur2 = new Professeur();
        assertThat(professeur1).isNotEqualTo(professeur2);

        professeur2.setId(professeur1.getId());
        assertThat(professeur1).isEqualTo(professeur2);

        professeur2 = getProfesseurSample2();
        assertThat(professeur1).isNotEqualTo(professeur2);
    }

    @Test
    void departementTest() throws Exception {
        Professeur professeur = getProfesseurRandomSampleGenerator();
        Departement departementBack = getDepartementRandomSampleGenerator();

        professeur.setDepartement(departementBack);
        assertThat(professeur.getDepartement()).isEqualTo(departementBack);

        professeur.departement(null);
        assertThat(professeur.getDepartement()).isNull();
    }
}
