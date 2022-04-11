package io.github.wong1988.kit.system;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

/**
 * 继承系统的计算类，减少操作失误
 */
public class BigDecimal2 extends BigDecimal {

    public BigDecimal2(char[] in, int offset, int len) {
        super(in, offset, len);
    }

    public BigDecimal2(char[] in, int offset, int len, MathContext mc) {
        super(in, offset, len, mc);
    }

    public BigDecimal2(char[] in) {
        super(in);
    }

    public BigDecimal2(char[] in, MathContext mc) {
        super(in, mc);
    }

    public BigDecimal2(String val) {
        super(val);
    }

    public BigDecimal2(String val, MathContext mc) {
        super(val, mc);
    }

    public BigDecimal2(double val) {
        super(Double.toString(val));
    }

    public BigDecimal2(double val, MathContext mc) {
        super(Double.toString(val), mc);
    }

    public BigDecimal2(BigInteger val) {
        super(val);
    }

    public BigDecimal2(BigInteger val, MathContext mc) {
        super(val, mc);
    }

    public BigDecimal2(BigInteger unscaledVal, int scale) {
        super(unscaledVal, scale);
    }

    public BigDecimal2(BigInteger unscaledVal, int scale, MathContext mc) {
        super(unscaledVal, scale, mc);
    }

    public BigDecimal2(int val) {
        super(val);
    }

    public BigDecimal2(int val, MathContext mc) {
        super(val, mc);
    }

    public BigDecimal2(long val) {
        super(val);
    }

    public BigDecimal2(long val, MathContext mc) {
        super(val, mc);
    }
}
