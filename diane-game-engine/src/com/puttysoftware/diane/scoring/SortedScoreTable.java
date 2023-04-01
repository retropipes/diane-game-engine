/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.diane.scoring;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import com.puttysoftware.diane.fileio.XDataReader;
import com.puttysoftware.diane.fileio.XDataWriter;

public class SortedScoreTable extends ScoreTable {
    public static SortedScoreTable readSortedScoreTable(final XDataReader xdr) throws IOException {
        final var order = xdr.readBoolean();
        final var len = xdr.readInt();
        final var unitLen = xdr.readInt();
        final var unitArr = new String[unitLen];
        for (var z = 0; z < unitLen; z++) {
            unitArr[z] = xdr.readString();
        }
        final var sst = new SortedScoreTable(unitLen, len, order, unitArr);
        for (var x = 0; x < len; x++) {
            sst.table.set(x, Score.readScore(xdr));
        }
        return sst;
    }

    // Fields
    protected boolean sortOrder;

    // Constructors
    public SortedScoreTable() {
        this.sortOrder = true;
    }

    public SortedScoreTable(final int mv, final int length, final boolean ascending, final long startingScore,
            final String[] customUnit) {
        super(mv, length, customUnit);
        this.sortOrder = ascending;
        int x, y;
        for (x = 0; x < this.table.size(); x++) {
            for (y = 0; y < this.scoreCount; y++) {
                this.table.get(x).setScore(y, startingScore);
            }
        }
    }

    private SortedScoreTable(final int mv, final int length, final boolean ascending, final String[] customUnit) {
        super(mv, length, customUnit);
        this.sortOrder = ascending;
    }

    public void addScore(final long newScore, final String newName) {
        final var newEntry = new Score(newScore, newName);
        if (this.sortOrder) {
            // Append the new score to the end
            this.table.add(newEntry);
            // Sort the score table
            Collections.sort(this.table, new Score.ScoreComparatorDesc());
            // Remove the lowest score
            this.table.remove(0);
        } else {
            // Append the new score to the end
            this.table.add(newEntry);
            // Sort the score table
            Collections.sort(this.table, new Score.ScoreComparatorAsc());
            // Remove the highest score
            this.table.remove(this.table.size() - 1);
        }
    }

    public void addScore(final long[] newScore, final String newName) {
        final var newEntry = new Score(this.scoreCount, newScore, newName);
        if (this.sortOrder) {
            // Append the new score to the end
            this.table.add(newEntry);
            // Sort the score table
            Collections.sort(this.table, new Score.ScoreComparatorDesc());
        } else {
            // Append the new score to the end
            this.table.add(newEntry);
            // Sort the score table
            Collections.sort(this.table, new Score.ScoreComparatorAsc());
        }
        // Remove the lowest score
        this.table.remove(this.table.size() - 1);
    }

    public boolean checkScore(final long[] newScore) {
        final var newEntry = new Score(this.scoreCount, newScore, ""); //$NON-NLS-1$
        final var temp = new ArrayList<>(this.table);
        if (this.sortOrder) {
            // Copy the current table to the temporary table
            Collections.copy(temp, this.table);
            // Append the new score to the end
            temp.add(newEntry);
            // Sort the score table
            Collections.sort(temp, new Score.ScoreComparatorDesc());
            // Determine if lowest score would be removed
            return !Collections.min(temp, new Score.ScoreComparatorDesc()).equals(newEntry);
        }
        // Copy the current table to the temporary table
        Collections.copy(temp, this.table);
        // Append the new score to the end
        temp.add(newEntry);
        // Sort the score table
        Collections.sort(temp, new Score.ScoreComparatorAsc());
        // Determine if highest score would be removed
        return !Collections.max(temp, new Score.ScoreComparatorAsc()).equals(newEntry);
    }

    @Override
    public void setEntryName(final int pos, final String newName) {
        // Do nothing
    }

    @Override
    public void setEntryScore(final int pos, final long newScore) {
        // Do nothing
    }

    public void writeSortedScoreTable(final XDataWriter xdw) throws IOException {
        xdw.writeBoolean(this.sortOrder);
        xdw.writeInt(this.table.size());
        xdw.writeInt(this.unit.length);
        for (final String element : this.unit) {
            if (element.length() > 1) {
                xdw.writeString(element.substring(1));
            } else {
                xdw.writeString(element);
            }
        }
        for (final Score element : this.table) {
            element.writeScore(xdw);
        }
    }
}
