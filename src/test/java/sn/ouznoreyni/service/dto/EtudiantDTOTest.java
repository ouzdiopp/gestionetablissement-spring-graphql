package sn.ouznoreyni.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.ouznoreyni.web.rest.TestUtil;

class EtudiantDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EtudiantDTO.class);
        EtudiantDTO etudiantDTO1 = new EtudiantDTO();
        etudiantDTO1.setId(1L);
        EtudiantDTO etudiantDTO2 = new EtudiantDTO();
        assertThat(etudiantDTO1).isNotEqualTo(etudiantDTO2);
        etudiantDTO2.setId(etudiantDTO1.getId());
        assertThat(etudiantDTO1).isEqualTo(etudiantDTO2);
        etudiantDTO2.setId(2L);
        assertThat(etudiantDTO1).isNotEqualTo(etudiantDTO2);
        etudiantDTO1.setId(null);
        assertThat(etudiantDTO1).isNotEqualTo(etudiantDTO2);
    }
}
