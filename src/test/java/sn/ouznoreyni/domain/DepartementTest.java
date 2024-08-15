package sn.ouznoreyni.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ouznoreyni.domain.DepartementTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.ouznoreyni.web.rest.TestUtil;

class DepartementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Departement.class);
        Departement departement1 = getDepartementSample1();
        Departement departement2 = new Departement();
        assertThat(departement1).isNotEqualTo(departement2);

        departement2.setId(departement1.getId());
        assertThat(departement1).isEqualTo(departement2);

        departement2 = getDepartementSample2();
        assertThat(departement1).isNotEqualTo(departement2);
    }
}
