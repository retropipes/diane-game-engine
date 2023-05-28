/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.diane.scoring;

import java.io.IOException;
import java.io.Serializable;
import java.util.Comparator;

import com.puttysoftware.diane.fileio.XDataReader;
import com.puttysoftware.diane.fileio.XDataWriter;

public final class Score {
    public static class ScoreComparatorAsc implements Comparator<Score>, Serializable {
	private static final long serialVersionUID = 30523263423564L;

	@Override
	public int compare(final Score o1, final Score o2) {
	    final var lesser = Math.min(o1.scores.length, o2.scores.length);
	    for (var z = 0; z < lesser; z++) {
		if (o1.scores[z] > o2.scores[z]) {
		    // Greater
		    return 1;
		}
		if (o1.scores[z] < o2.scores[z]) {
		    // Lesser
		    return -1;
		}
		// Equal
		if (z == lesser - 1) {
		    // Run out of things to compare
		    return 0;
		}
		// Keep looping
		continue;
	    }
	    // Shouldn't ever get here
	    return 0;
	}
    }

    public static class ScoreComparatorDesc implements Comparator<Score>, Serializable {
	private static final long serialVersionUID = 30523263423565L;

	@Override
	public int compare(final Score o1, final Score o2) {
	    final var lesser = Math.min(o1.scores.length, o2.scores.length);
	    for (var z = 0; z < lesser; z++) {
		if (o1.scores[z] > o2.scores[z]) {
		    // Greater
		    return -1;
		}
		if (o1.scores[z] < o2.scores[z]) {
		    // Lesser
		    return 1;
		}
		// Equal
		if (z == lesser - 1) {
		    // Run out of things to compare
		    return 0;
		}
		// Keep looping
		continue;
	    }
	    // Shouldn't ever get here
	    return 0;
	}
    }

    public static Score readScore(final XDataReader xdr) throws IOException {
	final var sname = xdr.readString();
	final var len = xdr.readInt();
	final var s = new Score(len);
	s.name = sname;
	for (var x = 0; x < len; x++) {
	    s.scores[x] = xdr.readLong();
	}
	return s;
    }

    // Fields
    long[] scores;
    private String name;

    // Constructors
    public Score() {
	this.scores = new long[1];
	this.scores[0] = 0L;
	this.name = "Nobody"; //$NON-NLS-1$
    }

    public Score(final int mv) {
	this.scores = new long[mv];
	for (var x = 0; x < mv; x++) {
	    this.scores[x] = 0L;
	}
	this.name = "Nobody"; //$NON-NLS-1$
    }

    public Score(final int mv, final long[] newScore, final String newName) {
	this.scores = new long[mv];
	for (var x = 0; x < mv; x++) {
	    this.scores[x] = newScore[x];
	}
	this.name = newName;
    }

    public Score(final long newScore, final String newName) {
	this.scores = new long[1];
	this.scores[0] = newScore;
	this.name = newName;
    }

    public String getName() {
	return this.name;
    }

    public long getScore() {
	return this.scores[0];
    }

    public long getScore(final int which) {
	return this.scores[which];
    }

    public void setName(final String newName) {
	this.name = newName;
    }

    public void setScore(final int pos, final long newScore) {
	this.scores[pos] = newScore;
    }

    public void setScore(final long newScore) {
	this.scores[0] = newScore;
    }

    public void writeScore(final XDataWriter xdw) throws IOException {
	xdw.writeString(this.name);
	xdw.writeInt(this.scores.length);
	for (final long score : this.scores) {
	    xdw.writeLong(score);
	}
    }
}
