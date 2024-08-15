package sn.ouznoreyni.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CoursTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Cours getCoursSample1() {
        return new Cours().id(1L).code("code1").intitule("intitule1").credits(1);
    }

    public static Cours getCoursSample2() {
        return new Cours().id(2L).code("code2").intitule("intitule2").credits(2);
    }

    public static Cours getCoursRandomSampleGenerator() {
        return new Cours()
            .id(longCount.incrementAndGet())
            .code(UUID.randomUUID().toString())
            .intitule(UUID.randomUUID().toString())
            .credits(intCount.incrementAndGet());
    }
}
