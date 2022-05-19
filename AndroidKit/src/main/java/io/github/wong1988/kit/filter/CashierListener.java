package io.github.wong1988.kit.filter;

public interface CashierListener {

    void overMax(String max, String fillZeroMaxValue);

    void correct(String value, String fillZeroValue);
}
