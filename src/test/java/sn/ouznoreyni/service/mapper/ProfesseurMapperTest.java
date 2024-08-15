package sn.ouznoreyni.service.mapper;

import static sn.ouznoreyni.domain.ProfesseurAsserts.*;
import static sn.ouznoreyni.domain.ProfesseurTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProfesseurMapperTest {

    private ProfesseurMapper professeurMapper;

    @BeforeEach
    void setUp() {
        professeurMapper = new ProfesseurMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getProfesseurSample1();
        var actual = professeurMapper.toEntity(professeurMapper.toDto(expected));
        assertProfesseurAllPropertiesEquals(expected, actual);
    }
}
