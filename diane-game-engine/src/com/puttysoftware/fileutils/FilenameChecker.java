package com.puttysoftware.fileutils;

public class FilenameChecker {
    // Private constructor
    private FilenameChecker() {
        // Do nothing
    }

    public static boolean isFilenameOK(final String filename) {
        if (filename.contains("/")) { //$NON-NLS-1$
            return false;
        }
        if (filename.contains("?")) { //$NON-NLS-1$
            return false;
        }
        if (filename.contains("<")) { //$NON-NLS-1$
            return false;
        }
        if (filename.contains(">")) { //$NON-NLS-1$
            return false;
        }
        if (filename.contains("\\")) { //$NON-NLS-1$
            return false;
        }
        if (filename.contains(":")) { //$NON-NLS-1$
            return false;
        }
        if (filename.contains("*")) { //$NON-NLS-1$
            return false;
        }
        if (filename.contains("|")) { //$NON-NLS-1$
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
}
