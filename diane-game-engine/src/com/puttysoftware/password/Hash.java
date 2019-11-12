package com.puttysoftware.password;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash {
    private Hash() {
        // Do nothing
    }

    public static byte[] hash(byte[] input) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-512"); //$NON-NLS-1$
        } catch (NoSuchAlgorithmException nsae) {
            return null;
        }
        return md.digest(input);
    }

    public static byte[] hash(byte[] input, String algorithm) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException nsae) {
            return null;
        }
        return md.digest(input);
    }
}
