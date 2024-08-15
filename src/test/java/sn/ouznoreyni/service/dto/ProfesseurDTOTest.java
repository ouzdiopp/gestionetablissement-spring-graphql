package sn.ouznoreyni.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.ouznoreyni.web.rest.TestUtil;

class ProfesseurDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfesseurDTO.class);
        ProfesseurDTO professeurDTO1 = new ProfesseurDTO();
        professeurDTO1.setId(1L);
        ProfesseurDTO professeurDTO2 = new ProfesseurDTO();
        assertThat(professeurDTO1).isNotEqualTo(professeurDTO2);
        professeurDTO2.setId(professeurDTO1.getId());
        assertThat(professeurDTO1).isEqualTo(professeurDTO2);
        professeurDTO2.setId(2L);
        assertThat(professeurDTO1).isNotEqualTo(professeurDTO2);
        professeurDTO1.setId(null);
        assertThat(professeurDTO1).isNotEqualTo(professeurDTO2);
    }
}
