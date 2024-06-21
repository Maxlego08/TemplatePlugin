package fr.maxlego08.template.zcore.utils;

import java.security.SecureRandom;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

import java.security.SecureRandom;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

/**
 * Utility class for generating random strings.
 * Supports generating alphanumeric strings using different random generators and symbol sets.
 */
public class RandomString {

    /**
     * Generates a random string.
     *
     * @return a randomly generated string.
     */
    public String nextString() {
        for (int idx = 0; idx < buf.length; ++idx) {
            buf[idx] = symbols[random.nextInt(symbols.length)];
        }
        return new String(buf);
    }

    public static final String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String lower = upper.toLowerCase(Locale.ROOT);
    public static final String digits = "0123456789";
    public static final String alphanum = upper + lower + digits;

    private final Random random;
    private final char[] symbols;
    private final char[] buf;

    /**
     * Constructs a new RandomString with the specified length, random generator, and symbol set.
     *
     * @param length  the length of the generated strings.
     * @param random  the random generator to use.
     * @param symbols the set of symbols to use for generating the string.
     * @throws IllegalArgumentException if length is less than 1 or symbols length is less than 2.
     */
    public RandomString(int length, Random random, String symbols) {
        if (length < 1) throw new IllegalArgumentException();
        if (symbols.length() < 2) throw new IllegalArgumentException();
        this.random = Objects.requireNonNull(random);
        this.symbols = symbols.toCharArray();
        this.buf = new char[length];
    }

    /**
     * Constructs a new RandomString with the specified length and random generator.
     * Uses the alphanumeric symbol set.
     *
     * @param length the length of the generated strings.
     * @param random the random generator to use.
     */
    public RandomString(int length, Random random) {
        this(length, random, alphanum);
    }

    /**
     * Constructs a new RandomString with the specified length.
     * Uses a secure random generator and the alphanumeric symbol set.
     *
     * @param length the length of the generated strings.
     */
    public RandomString(int length) {
        this(length, new SecureRandom());
    }

    /**
     * Constructs a new RandomString with a default length of 21.
     * Uses a secure random generator and the alphanumeric symbol set.
     */
    public RandomString() {
        this(21);
    }
}
