/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.diane.fileio.utility;

public class FilenameChecker {
    public static boolean isFilenameOK(final String filename) {
        if (filename.contains("/") || filename.contains("?") || filename.contains("<") || filename.contains(">")) { //$NON-NLS-1$
            return false;
        }
        if (filename.contains("\\") || filename.contains(":") || filename.contains("*") || filename.contains("|")) { //$NON-NLS-1$
            return false;
        }
        if (filename.contains("\"")) { //$NON-NLS-1$
            return false;
        }
        if (filename.equals("con")) { //$NON-NLS-1$
            return false;
        }
        if (filename.equals("nul")) { //$NON-NLS-1$
            return false;
        }
        if (filename.equals("prn")) { //$NON-NLS-1$
            return false;
        }
        if (filename.length() == 4 && filename.matches("com[1-9]")) { //$NON-NLS-1$
            return false;
        }
        if (filename.length() == 4 && filename.matches("lpt[1-9]")) { //$NON-NLS-1$
            return false;
        }
        return true;
    }

    // Private constructor
    private FilenameChecker() {
        // Do nothing
    }
}
