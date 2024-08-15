package sn.ouznoreyni.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EtudiantTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Etudiant getEtudiantSample1() {
        return new Etudiant().id(1L).numeroEtudiant("numeroEtudiant1").filiere("filiere1");
    }

    public static Etudiant getEtudiantSample2() {
        return new Etudiant().id(2L).numeroEtudiant("numeroEtudiant2").filiere("filiere2");
    }

    public static Etudiant getEtudiantRandomSampleGenerator() {
        return new Etudiant()
            .id(longCount.incrementAndGet())
            .numeroEtudiant(UUID.randomUUID().toString())
            .filiere(UUID.randomUUID().toString());
    }
}
