package com.puttysoftware.page;

import java.io.IOException;

import com.puttysoftware.xio.XDataReader;
import com.puttysoftware.xio.XDataWriter;

public final class Page extends Polynomial {
    // Fields
    private int maxRange;
    private boolean experience;

    // Constructors
    private Page(Polynomial p) {
        super(p);
    }

    public Page(int maxPower, int params, int range, boolean isExperience) {
        super(maxPower, params);
        this.maxRange = range;
        this.experience = isExperience;
    }

    // Methods
    public int getMaxRange() {
        return this.maxRange;
    }

    public boolean isExperience() {
        return this.experience;
    }

    @Override
    public long evaluate(int paramValue) {
        int x;
        long result = 0;
        for (x = 0; x < this.coefficients.length; x++) {
            result += (long) (this.coefficients[x][Polynomial.DEFAULT_PARAM]
                    * Math.pow(paramValue, x));
        }
        if (this.experience) {
            for (x = 0; x < this.coefficients.length; x++) {
                result -= (long) (this.coefficients[x][Polynomial.DEFAULT_PARAM]);
            }
        }
        return result;
    }

    @Override
    public long evaluate(int[] paramValues) {
        int x, y;
        long result = 0;
        for (x = 0; x < this.coefficients.length; x++) {
            for (y = 0; y < this.coefficients[x].length; y++) {
                result += (long) (this.coefficients[x][y]
                        * Math.pow(paramValues[y], x));
            }
        }
        if (this.experience) {
            for (x = 0; x < this.coefficients.length; x++) {
                for (y = 0; y < this.coefficients[x].length; y++) {
                    result -= (long) (this.coefficients[x][y]);
                }
            }
        }
        return result;
    }

    public long[] evaluateToArray() {
        long[] result = new long[this.maxRange];
        for (int x = 0; x < result.length; x++) {
            result[x] = this.evaluate(x + 1);
        }
        return result;
    }

    public static Page readPage(XDataReader reader) throws IOException {
        Page p = new Page(Polynomial.readPolynomial(reader));
        int tempMaxRange = reader.readInt();
        boolean tempExperience = reader.readBoolean();
        p.maxRange = tempMaxRange;
        p.experience = tempExperience;
        return p;
    }

    public void writePage(XDataWriter writer) throws IOException {
        this.writePolynomial(writer);
        writer.writeInt(this.maxRange);
        writer.writeBoolean(this.experience);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (this.experience ? 1231 : 1237);
        return prime * result + this.maxRange;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof Page)) {
            return false;
        }
        Page other = (Page) obj;
        if (this.experience != other.experience) {
            return false;
        }
        if (this.maxRange != other.maxRange) {
            return false;
        }
        return true;
    }
}
