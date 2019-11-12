package com.puttysoftware.page;

import java.io.IOException;
import java.util.Arrays;

import com.puttysoftware.xio.XDataReader;
import com.puttysoftware.xio.XDataWriter;

public class Polynomial {
    // Fields
    protected double[][] coefficients;
    protected int max;
    protected int paramCount;
    protected static final int DEFAULT_PARAMS = 1;
    protected static final int DEFAULT_PARAM = 0;

    // Constructors
    private Polynomial() {
        // Do nothing
    }

    protected Polynomial(Polynomial p) {
        this.coefficients = p.coefficients;
        this.max = p.max;
        this.paramCount = p.paramCount;
    }

    public Polynomial(int maxPower) {
        this.coefficients = new double[maxPower + 1][Polynomial.DEFAULT_PARAMS];
        this.max = maxPower;
        this.paramCount = Polynomial.DEFAULT_PARAMS;
    }

    public Polynomial(int maxPower, int params) {
        this.coefficients = new double[maxPower + 1][params];
        this.max = maxPower;
        this.paramCount = params;
    }

    // Methods
    public int getMaxPower() {
        return this.max;
    }

    public int getParamCount() {
        return this.paramCount;
    }

    public double getCoefficient(int power) {
        return this.coefficients[power][Polynomial.DEFAULT_PARAM];
    }

    public double getCoefficient(int power, int param) {
        return this.coefficients[power][param - 1];
    }

    public void setCoefficient(int power, double value) {
        this.coefficients[power][Polynomial.DEFAULT_PARAM] = value;
    }

    public void setCoefficient(int power, int param, double value) {
        this.coefficients[power][param - 1] = value;
    }

    public long evaluate(int paramValue) {
        int x;
        long result = 0;
        for (x = 0; x < this.coefficients.length; x++) {
            result += (long) (this.coefficients[x][Polynomial.DEFAULT_PARAM]
                    * Math.pow(paramValue, x));
        }
        return result;
    }

    public long evaluate(int[] paramValues) {
        int x, y;
        long result = 0;
        for (x = 0; x < this.coefficients.length; x++) {
            for (y = 0; y < this.coefficients[x].length; y++) {
                result += (long) (this.coefficients[x][y]
                        * Math.pow(paramValues[y], x));
            }
        }
        return result;
    }

    public static Polynomial readPolynomial(XDataReader reader)
            throws IOException {
        int tempMax = reader.readInt();
        int tempParamCount = reader.readInt();
        double[][] tempCoefficients = new double[tempMax + 1][tempParamCount];
        int x, y;
        for (x = 0; x < tempMax + 1; x++) {
            for (y = 0; y < tempParamCount; y++) {
                tempCoefficients[x][y] = reader.readDouble();
            }
        }
        Polynomial p = new Polynomial();
        p.max = tempMax;
        p.paramCount = tempParamCount;
        p.coefficients = tempCoefficients;
        return p;
    }

    public void writePolynomial(XDataWriter writer) throws IOException {
        writer.writeInt(this.max);
        writer.writeInt(this.paramCount);
        int x, y;
        for (x = 0; x < this.coefficients.length; x++) {
            for (y = 0; y < this.coefficients[x].length; y++) {
                writer.writeDouble(this.coefficients[x][y]);
            }
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(this.coefficients);
        result = prime * result + this.max;
        return prime * result + this.paramCount;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Polynomial)) {
            return false;
        }
        Polynomial other = (Polynomial) obj;
        if (!Arrays.equals(this.coefficients, other.coefficients)) {
            return false;
        }
        if (this.max != other.max) {
            return false;
        }
        if (this.paramCount != other.paramCount) {
            return false;
        }
        return true;
    }
}
