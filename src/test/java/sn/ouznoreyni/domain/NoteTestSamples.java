package sn.ouznoreyni.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class NoteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Note getNoteSample1() {
        return new Note().id(1L);
    }

    public static Note getNoteSample2() {
        return new Note().id(2L);
    }

    public static Note getNoteRandomSampleGenerator() {
        return new Note().id(longCount.incrementAndGet());
    }
}
