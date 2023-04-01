/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.diane.polytable;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import com.puttysoftware.diane.fileio.XDataReader;
import com.puttysoftware.diane.fileio.XDataWriter;

public class Polynomial {
	protected static final int DEFAULT_PARAMS = 1;
	protected static final int DEFAULT_PARAM = 0;

	public static Polynomial readPolynomial(final XDataReader reader) throws IOException {
		final var tempMax = reader.readInt();
		final var tempParamCount = reader.readInt();
		final var tempCoefficients = new double[tempMax + 1][tempParamCount];
		int x, y;
		for (x = 0; x < tempMax + 1; x++) {
			for (y = 0; y < tempParamCount; y++) {
				tempCoefficients[x][y] = reader.readDouble();
			}
		}
		final var p = new Polynomial();
		p.max = tempMax;
		p.paramCount = tempParamCount;
		p.coefficients = tempCoefficients;
		return p;
	}

	// Fields
	protected double[][] coefficients;
	protected int max;
	protected int paramCount;

	// Constructors
	private Polynomial() {
		// Do nothing
	}

	public Polynomial(final int maxPower) {
		this.coefficients = new double[maxPower + 1][Polynomial.DEFAULT_PARAMS];
		this.max = maxPower;
		this.paramCount = Polynomial.DEFAULT_PARAMS;
	}

	public Polynomial(final int maxPower, final int params) {
		this.coefficients = new double[maxPower + 1][params];
		this.max = maxPower;
		this.paramCount = params;
	}

	protected Polynomial(final Polynomial p) {
		this.coefficients = p.coefficients;
		this.max = p.max;
		this.paramCount = p.paramCount;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || !(obj instanceof final Polynomial other)
				|| !Arrays.equals(this.coefficients, other.coefficients) || this.max != other.max) {
			return false;
		}
		if (this.paramCount != other.paramCount) {
			return false;
		}
		return true;
	}

	public long evaluate(final int paramValue) {
		int x;
		var result = 0L;
		for (x = 0; x < this.coefficients.length; x++) {
			result += (long) (this.coefficients[x][Polynomial.DEFAULT_PARAM] * Math.pow(paramValue, x));
		}
		return result;
	}

	public long evaluate(final int[] paramValues) {
		int x, y;
		var result = 0L;
		for (x = 0; x < this.coefficients.length; x++) {
			for (y = 0; y < this.coefficients[x].length; y++) {
				result += (long) (this.coefficients[x][y] * Math.pow(paramValues[y], x));
			}
		}
		return result;
	}

	public double getCoefficient(final int power) {
		return this.coefficients[power][Polynomial.DEFAULT_PARAM];
	}

	public double getCoefficient(final int power, final int param) {
		return this.coefficients[power][param - 1];
	}

	public int getMaxPower() {
		return this.max;
	}

	public int getParamCount() {
		return this.paramCount;
	}

	@Override
	public int hashCode() {
		return Objects.hash(Arrays.hashCode(this.coefficients), this.max, this.paramCount);
	}

	public void setCoefficient(final int power, final double value) {
		this.coefficients[power][Polynomial.DEFAULT_PARAM] = value;
	}

	public void setCoefficient(final int power, final int param, final double value) {
		this.coefficients[power][param - 1] = value;
	}

	public void writePolynomial(final XDataWriter writer) throws IOException {
		writer.writeInt(this.max);
		writer.writeInt(this.paramCount);
		int x, y;
		for (x = 0; x < this.coefficients.length; x++) {
			for (y = 0; y < this.coefficients[x].length; y++) {
				writer.writeDouble(this.coefficients[x][y]);
			}
		}
	}
}
