/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2010 Eric Ahnell

 Any questions should be directed to the author via email at: lasertank@worldwizard.net
 */
package com.puttysoftware.scoremanager;

import com.puttysoftware.commondialogs.CommonDialogs;

public final class ScoreTableViewer {
    // Private constants
    private static final int ENTRIES_PER_PAGE = 10;
    private static final String VIEWER_STRING = "Score Table Viewer"; //$NON-NLS-1$

    // Private constructor
    private ScoreTableViewer() {
        // Do nothing
    }

    // Methods
    public static void view(final ScoreTable table, final String customTitle,
            final String[] unit) {
        final StringBuilder msgBuilder = new StringBuilder();
        String msg = null;
        String title = null;
        if (customTitle == null || customTitle.isEmpty()) {
            title = ScoreTableViewer.VIEWER_STRING;
        } else {
            title = customTitle;
        }
        int x;
        int y;
        int z;
        for (x = 0; x < table
                .getLength(); x += ScoreTableViewer.ENTRIES_PER_PAGE) {
            msg = ""; //$NON-NLS-1$
            for (y = 1; y <= ScoreTableViewer.ENTRIES_PER_PAGE; y++) {
                try {
                    msgBuilder.append(table.getEntryName(x + y - 1));
                    msgBuilder.append(" - "); //$NON-NLS-1$
                    for (z = 0; z < table.getScoreCount(); z++) {
                        msgBuilder.append(table.getEntryScore(z, x + y - 1));
                        msgBuilder.append(unit[z]);
                        if (z < table.getScoreCount() - 1) {
                            msgBuilder.append(", "); //$NON-NLS-1$
                        }
                    }
                    msgBuilder.append("\n"); //$NON-NLS-1$
                } catch (final ArrayIndexOutOfBoundsException ae) {
                    // Do nothing
                }
            }
            msg = msgBuilder.toString();
            // Strip final newline character
            msg = msg.substring(0, msg.length() - 1);
            CommonDialogs.showTitledDialog(msg, title);
        }
    }
}
