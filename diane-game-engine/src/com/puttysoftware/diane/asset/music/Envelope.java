/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.diane.asset.music;

class Envelope {
    public boolean enabled = false, sustain = false, looped = false;
    public int sustainTick = 0, loopStartTick = 0, loopEndTick = 0;
    public int numPoints = 1;
    public int[] pointsTick = new int[1];
    public int[] pointsAmpl = new int[1];

    public int calculateAmpl(final int tick) {
	var ampl = this.pointsAmpl[this.numPoints - 1];
	if (tick < this.pointsTick[this.numPoints - 1]) {
	    var point = 0;
	    for (var idx = 1; idx < this.numPoints; idx++) {
		if (this.pointsTick[idx] <= tick) {
		    point = idx;
		}
	    }
	    final var dt = this.pointsTick[point + 1] - this.pointsTick[point];
	    final var da = this.pointsAmpl[point + 1] - this.pointsAmpl[point];
	    ampl = this.pointsAmpl[point];
	    ampl += (da << 24) / dt * (tick - this.pointsTick[point]) >> 24;
	}
	return ampl;
    }

    public int nextTick(final int tick, final boolean keyOn) {
	var fixedTick = tick;
	fixedTick++;
	if (this.looped && fixedTick >= this.loopEndTick) {
	    fixedTick = this.loopStartTick;
	}
	if (this.sustain && keyOn && fixedTick >= this.sustainTick) {
	    fixedTick = this.sustainTick;
	}
	return fixedTick;
    }

    public void toStringBuffer(final StringBuffer out) {
	out.append("Enabled: " + this.enabled + '\n'); //$NON-NLS-1$
	out.append("Sustain: " + this.sustain + '\n'); //$NON-NLS-1$
	out.append("Looped: " + this.looped + '\n'); //$NON-NLS-1$
	out.append("Sustain Tick: " + this.sustainTick + '\n'); //$NON-NLS-1$
	out.append("Loop Start Tick: " + this.loopStartTick + '\n'); //$NON-NLS-1$
	out.append("Loop End Tick: " + this.loopEndTick + '\n'); //$NON-NLS-1$
	out.append("Num Points: " + this.numPoints + '\n'); //$NON-NLS-1$
	out.append("Points: "); //$NON-NLS-1$
	for (var point = 0; point < this.numPoints; point++) {
	    out.append("(" + this.pointsTick[point] + ", " //$NON-NLS-1$ //$NON-NLS-2$
		    + this.pointsAmpl[point] + "), "); //$NON-NLS-1$
	}
	out.append('\n');
    }
}
