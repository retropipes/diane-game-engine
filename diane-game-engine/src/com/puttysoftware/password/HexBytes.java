package com.puttysoftware.password;

public class HexBytes {
    // Code obtained from http://www.devx.com/tips/Tip/13540 and modified
    // slightly.
    /**
     * Convert a byte[] array to readable string format. This makes the "hex"
     * readable!
     *
     * @return result String buffer in String format
     * @param in
     *            byte[] buffer to convert to string format
     */
    public static String hexBytes(byte[] in) {
        byte ch = 0x00;
        int i = 0;
        if (in == null || in.length <= 0) {
            return null;
        }
        String pseudo[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$
                "A", "B", "C", "D", "E", "F" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
        StringBuffer out = new StringBuffer(in.length * 2);
        while (i < in.length) {
            ch = (byte) (in[i] & 0xF0); // Strip off high nibble
            ch = (byte) (ch >>> 4); // shift the bits down
            ch = (byte) (ch & 0x0F); // must do this is high order bit is on!
            out.append(pseudo[ch]); // convert the nibble to a String Character
            ch = (byte) (in[i] & 0x0F); // Strip off low nibble
            out.append(pseudo[ch]); // convert the nibble to a String Character
            i++;
        }
        return out.toString();
    }

    public static byte[] unhexBytes(String in) {
        char ch;
        byte low = 0x00;
        byte high = 0x00;
        int i = 0;
        if (in == null || in.length() <= 0) {
            return null;
        }
        String fIn = in.toUpperCase();
        String pseudo[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$
                "A", "B", "C", "D", "E", "F" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
        byte[] out = new byte[fIn.length() / 2];
        while (i < fIn.length()) {
            ch = fIn.charAt(i);
            for (int z = 0; z < pseudo.length; z++) {
                if (ch == pseudo[z].charAt(0)) {
                    low = (byte) z;
                }
            }
            ch = fIn.charAt(i + 1);
            for (int z = 0; z < pseudo.length; z++) {
                if (ch == pseudo[z].charAt(0)) {
                    high = (byte) z;
                }
            }
            out[i / 2] = (byte) (high << 4 | low);
            i += 2;
        }
        return out;
    }
}
