package io.github.wong1988.kit.filter;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 现金格式的过滤器
 */
public class CashierInputFilter implements InputFilter {

    Pattern mPattern;

    // 输入的最大金额
    private BigDecimal MAX_VALUE;
    // 小数点后的位数
    private static final int POINTER_LENGTH = 2;

    private static final String POINTER = ".";

    private static final String ZERO = "0";

    private final CashierListener mListener;

    private String MAX_VALUE_FILL = "";

    public CashierInputFilter(CashierListener listener) {
        mPattern = Pattern.compile("([0-9]|\\.)*");
        this.mListener = listener;
    }

    /**
     * @param MAX_VALUE 可输入的最大值
     */
    public CashierInputFilter(String MAX_VALUE, CashierListener listener) {
        this(listener);
        // 如果最大值错误 会自动报异常
        this.MAX_VALUE = new BigDecimal(MAX_VALUE);

        if (this.MAX_VALUE.toString().substring(this.MAX_VALUE.toString().indexOf(".")).length() > 3) {
            throw new RuntimeException("最大值仅支持两位小数");
        }

        this.MAX_VALUE_FILL = new DecimalFormat("0.00#").format(this.MAX_VALUE);
    }

    /**
     * @param source 新输入的字符串
     * @param start  新输入的字符串起始下标，一般为0
     * @param end    新输入的字符串终点下标，一般为source长度-1
     * @param dest   输入之前文本框内容
     * @param dstart 要替换或者添加的起始位置，即光标所在的位置
     * @param dend   原内容终点坐标，若为选择一串字符串进行更改，则为选中字符串 最后一个字符在dest中的位置。
     * @return 替换光标所在位置的CharSequence
     */
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

        // 新输入的字符串[键盘输入(char)、setText()的内容]
        String sourceText = source.toString();
        // 输入前的字符串
        String destText = dest.toString();

        if (TextUtils.isEmpty(sourceText) && TextUtils.isEmpty(destText)) {
            // 用户调用的setText() 输入前的字符串会返回""
            if (mListener != null)
                mListener.correct("", "0.00");

            return "";
        }

        // 删除操作
        if (TextUtils.isEmpty(sourceText) && dend > dstart) {

            if (dend - dstart == destText.length()) {

                // 全部清空
                if (mListener != null)
                    mListener.correct("", "0.00");

                return "";

            } else {

                // 需要验证金额
                String sumTextStr;

                if (dend >= destText.length()) {
                    // 从末尾进行删除
                    sumTextStr = destText.substring(0, dstart);
                } else {

                    if (dstart == 0) {
                        // 从开始进行删除
                        sumTextStr = destText.substring(dend);
                    } else {
                        // 中间删除
                        sumTextStr = destText.substring(0, dstart) + destText.substring(dend);
                    }

                }

                // 需要验证金额
                BigDecimal sumText = new BigDecimal(sumTextStr);

                if (MAX_VALUE == null) {
                    if (mListener != null)
                        mListener.correct(sumText.toString(), new DecimalFormat("0.00#").format(sumText));
                } else {
                    // 验证最大值
                    if (sumText.compareTo(MAX_VALUE) > 0) {
                        // 超出最大值
                        if (mListener != null)
                            mListener.overMax(MAX_VALUE.toString(), MAX_VALUE_FILL);
                    } else {
                        if (mListener != null)
                            mListener.correct(sumText.toString(), new DecimalFormat("0.00#").format(sumText));
                    }
                }

                return sumTextStr.startsWith(".") ? "0" : "";
            }
        }


        // 添加操作
        Matcher matcher = mPattern.matcher(source);
        // 不符合正则表达式
        if (!matcher.matches()) {
            return "";
        }


        // 拼接
        String sumTextStr;

        if (dstart == 0) {
            // 首位添加
            sumTextStr = sourceText + destText;
        } else if (dstart == destText.length()) {
            // 末尾添加
            sumTextStr = destText + sourceText;
        } else {
            // 中间添加
            sumTextStr = (destText.substring(0, dstart) + sourceText + destText.substring(dstart));
        }

        String returnText = sourceText;

        if (sumTextStr.contains(".")) {
            // 有小数点
            if (sumTextStr.indexOf(".") == sumTextStr.lastIndexOf(".")) {
                // 仅一个小数点
                if (dstart == 0 && sumTextStr.startsWith("."))
                    returnText = "0" + sourceText;
            } else {
                // 多个小数点
                return "";
            }
        }

        // 验证输入金额的大小
        BigDecimal sumText = new BigDecimal(sumTextStr);

        if (MAX_VALUE == null) {
            if (mListener != null)
                mListener.correct(sumText.toString(), new DecimalFormat("0.00#").format(sumText));
        } else {
            // 验证最大值
            if (sumText.compareTo(MAX_VALUE) > 0) {
                // 超出最大值
                if (mListener != null)
                    mListener.overMax(MAX_VALUE.toString(), MAX_VALUE_FILL);
            } else {
                if (mListener != null)
                    mListener.correct(sumText.toString(), new DecimalFormat("0.00#").format(sumText));
            }
        }

        return returnText;
    }
}
