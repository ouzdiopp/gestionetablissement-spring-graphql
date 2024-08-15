package sn.ouznoreyni.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ProfesseurTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Professeur getProfesseurSample1() {
        return new Professeur().id(1L).numeroEmploye("numeroEmploye1").specialite("specialite1").bureau("bureau1");
    }

    public static Professeur getProfesseurSample2() {
        return new Professeur().id(2L).numeroEmploye("numeroEmploye2").specialite("specialite2").bureau("bureau2");
    }

    public static Professeur getProfesseurRandomSampleGenerator() {
        return new Professeur()
            .id(longCount.incrementAndGet())
            .numeroEmploye(UUID.randomUUID().toString())
            .specialite(UUID.randomUUID().toString())
            .bureau(UUID.randomUUID().toString());
    }
}
