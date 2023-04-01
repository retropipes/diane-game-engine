/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.diane.random;

/**
 * A source of randomness for the other classes.
 */
public class RandomHex {
    /**
     * Instantiates a new random hexer.
     */
    // Constructor
    private RandomHex() {
        // Do nothing
    }

    /**
     * Next random 2 hex characters.
     *
     * @return the hex string
     */
    public static String next2() {
        return String.format("%02x", RandomnessSource.nextBoundedInt(Byte.MAX_VALUE + 1));
    }

    /**
     * Next random 4 hex characters.
     *
     * @return the hex string
     */
    public static String next4() {
        return String.format("%04x", RandomnessSource.nextBoundedInt(Short.MAX_VALUE + 1));
    }

    /**
     * Next random 8 hex characters.
     *
     * @return the hex string
     */
    public static String next8() {
        return String.format("%08x", RandomnessSource.nextInt());
    }

    /**
     * Next random N hex characters.
     *
     * @param N the number of characters (will be N+1, if N is odd; if N < 2, 2 will
     *          be used instead)
     *
     * @return the hex string
     */
    public static String nextN(final int N) {
        final StringBuilder sb = new StringBuilder();
        int rN = N;
        // Ensure positive number at least 2
        if (rN < 2) {
            rN = 2;
        }
        // Ensure even number
        if (rN % 2 == 1) {
            rN += 1;
        }
        // For efficiency reasons, check 8 first
        int c = rN;
        int r = 0;
        if (rN / 8 > 0) {
            c = rN / 8;
            r = rN % 8;
            for (int i = 0; i < c; i++) {
                sb.append(RandomHex.next8());
            }
            rN = r;
        }
        // ... then check 4
        if (rN / 4 == 1) {
            r = rN % 4;
            sb.append(RandomHex.next4());
            rN = r;
        }
        // ... then finally check 2
        if (rN / 2 == 1) {
            sb.append(RandomHex.next2());
        }
        // All done
        return sb.toString();
    }
}
