package sn.ouznoreyni.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class InscriptionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Inscription getInscriptionSample1() {
        return new Inscription().id(1L);
    }

    public static Inscription getInscriptionSample2() {
        return new Inscription().id(2L);
    }

    public static Inscription getInscriptionRandomSampleGenerator() {
        return new Inscription().id(longCount.incrementAndGet());
    }
}
