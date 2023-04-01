/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.diane.polytable;

import java.io.IOException;

import com.puttysoftware.diane.fileio.XDataReader;
import com.puttysoftware.diane.fileio.XDataWriter;

public final class PolyTable extends Polynomial {
	public static PolyTable readPage(final XDataReader reader) throws IOException {
		final var p = new PolyTable(Polynomial.readPolynomial(reader));
		final var tempMaxRange = reader.readInt();
		final var tempExperience = reader.readBoolean();
		p.maxRange = tempMaxRange;
		p.experience = tempExperience;
		return p;
	}

	// Fields
	private int maxRange;
	private boolean experience;

	public PolyTable(final int maxPower, final int params, final int range, final boolean isExperience) {
		super(maxPower, params);
		this.maxRange = range;
		this.experience = isExperience;
	}

	// Constructors
	private PolyTable(final Polynomial p) {
		super(p);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj) || !(obj instanceof final PolyTable other) || this.experience != other.experience
				|| this.maxRange != other.maxRange) {
			return false;
		}
		return true;
	}

	@Override
	public long evaluate(final int paramValue) {
		int x;
		var result = 0L;
		for (x = 0; x < this.coefficients.length; x++) {
			result += (long) (this.coefficients[x][Polynomial.DEFAULT_PARAM] * Math.pow(paramValue, x));
		}
		if (this.experience) {
			for (x = 0; x < this.coefficients.length; x++) {
				result -= (long) this.coefficients[x][Polynomial.DEFAULT_PARAM];
			}
		}
		return result;
	}

	@Override
	public long evaluate(final int[] paramValues) {
		int x, y;
		var result = 0L;
		for (x = 0; x < this.coefficients.length; x++) {
			for (y = 0; y < this.coefficients[x].length; y++) {
				result += (long) (this.coefficients[x][y] * Math.pow(paramValues[y], x));
			}
		}
		if (this.experience) {
			for (x = 0; x < this.coefficients.length; x++) {
				for (y = 0; y < this.coefficients[x].length; y++) {
					result -= (long) this.coefficients[x][y];
				}
			}
		}
		return result;
	}

	public long[] evaluateToArray() {
		final var result = new long[this.maxRange];
		for (var x = 0; x < result.length; x++) {
			result[x] = this.evaluate(x + 1);
		}
		return result;
	}

	public int getMaxRange() {
		return this.maxRange;
	}

	@Override
	public int hashCode() {
		final var prime = 31;
		var result = super.hashCode();
		result = prime * result + (this.experience ? 1231 : 1237);
		return prime * result + this.maxRange;
	}

	public boolean isExperience() {
		return this.experience;
	}

	public void writePage(final XDataWriter writer) throws IOException {
		this.writePolynomial(writer);
		writer.writeInt(this.maxRange);
		writer.writeBoolean(this.experience);
	}
}
