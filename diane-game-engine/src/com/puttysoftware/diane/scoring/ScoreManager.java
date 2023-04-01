/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.diane.scoring;

import com.puttysoftware.diane.gui.dialog.CommonDialogs;

public class ScoreManager {
    // Fields and Constants
    private static final String NAME_PROMPT = "Enter a name for the score list:"; //$NON-NLS-1$
    private static final String DIALOG_TITLE = "Score Manager"; //$NON-NLS-1$
    public static final boolean SORT_ORDER_ASCENDING = true;
    public static final boolean SORT_ORDER_DESCENDING = false;
    protected SortedScoreTable table;
    private String name;
    private String title;
    private final String viewerTitle;

    // Constructors
    public ScoreManager() {
        this.table = new SortedScoreTable();
        this.name = ""; //$NON-NLS-1$
        this.title = ScoreManager.DIALOG_TITLE;
        this.viewerTitle = ScoreManager.DIALOG_TITLE;
    }

    public ScoreManager(final int mv, final int length, final boolean sortOrder, final long startingScore,
            final String customTitle, final String[] customUnit) {
        this.table = new SortedScoreTable(mv, length, sortOrder, startingScore, customUnit);
        this.name = ""; //$NON-NLS-1$
        if (customTitle == null || customTitle.equals("")) { //$NON-NLS-1$
            this.title = ScoreManager.DIALOG_TITLE;
        } else {
            this.title = customTitle;
        }
        this.viewerTitle = customTitle;
    }

    public boolean addScore(final long newScore) {
        var success = true;
        this.name = null;
        this.name = CommonDialogs.showTextInputDialog(ScoreManager.NAME_PROMPT, this.title);
        if (this.name != null) {
            this.table.addScore(newScore, this.name);
        } else {
            success = false;
        }
        return success;
    }

    public boolean addScore(final long newScore, final String newName) {
        final var success = true;
        this.table.addScore(newScore, newName);
        return success;
    }

    public boolean addScore(final long[] newScore) {
        var success = true;
        this.name = null;
        this.name = CommonDialogs.showTextInputDialog(ScoreManager.NAME_PROMPT, this.title);
        if (this.name != null) {
            this.table.addScore(newScore, this.name);
        } else {
            success = false;
        }
        return success;
    }

    public boolean checkScore(final long newScore) {
        return this.table.checkScore(new long[] { newScore });
    }

    public boolean checkScore(final long[] newScore) {
        return this.table.checkScore(newScore);
    }

    public void viewTable() {
        ScoreTableViewer.view(this.table, this.viewerTitle, this.table.getUnits());
    }
}
