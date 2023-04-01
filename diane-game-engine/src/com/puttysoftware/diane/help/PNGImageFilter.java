/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.diane.help;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class PNGImageFilter extends FileFilter {
    private static String getExtension(final File f) {
        String ext = null;
        final var s = f.getName();
        final var i = s.lastIndexOf('.');
        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    @Override
    public boolean accept(final File f) {
        if (f.isDirectory()) {
            return true;
        }
        final var extension = PNGImageFilter.getExtension(f);
        if (extension != null) {
            if (extension.equals("png")) { //$NON-NLS-1$
                return true;
            }
        }
        return false;
    }

    @Override
    public String getDescription() {
        return "PNG Images (.png)"; //$NON-NLS-1$
    }
}
