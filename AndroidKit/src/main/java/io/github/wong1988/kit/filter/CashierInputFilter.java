package io.github.wong1988.kit.filter;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;

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
    // Zero
    private static final String ZERO_POINT = "0.00";

    private static final String POINTER = ".";

    private static final String ZERO = "0";

    private final CashierListener mListener;

    private String MAX_VALUE_FILL = "";

    public CashierInputFilter(CashierListener listener) {
        mPattern = Pattern.compile("^[0-9]*\\.?[0-9]*$");
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
     * @param end    新输入的字符串终点下标，一般为start+source长度
     * @param dest   输入之前文本框内容
     * @param dstart 要替换或者添加的起始位置，即光标所在的位置
     * @param dend   原内容终点坐标，若为选择一串字符串进行更改，则为选中字符串 最后一个字符在dest中的位置。
     * @return 替换光标所在位置的CharSequence
     */
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

        Log.e("ddd", "source:" + source + " start:" + start + " end:" + end + " dest:" + dest + " dstart:" + dstart + " dend:" + dend);

        // 新输入的字符串[键盘输入(char)、setText()的内容]
        String sourceText = source.toString();
        // 输入前的字符串
        String destText = dest.toString();

        if (TextUtils.isEmpty(sourceText) && TextUtils.isEmpty(destText)) {
            // 用户调用的setText("")
            if (mListener != null)
                mListener.correct("", ZERO_POINT);
            return "";
        }

        // 删除操作
        if (TextUtils.isEmpty(sourceText) && dend > dstart) {

            String returnText = "";

            if (dend - dstart == destText.length()) {

                // 全部清空
                if (mListener != null)
                    mListener.correct("", ZERO_POINT);

            } else {

                // 需要验证金额
                String sumTextStr;

                if (dend >= destText.length()) {
                    // 从末尾进行删除
                    sumTextStr = destText.substring(0, dstart);
                } else if (dstart == 0) {
                    // 从开始进行删除
                    sumTextStr = destText.substring(dend);
                } else {
                    // 中间删除
                    sumTextStr = destText.substring(0, dstart) + destText.substring(dend);
                }

                //.(需要注意删除只剩下点的情况) ""(此种情况上方if条件会拦截)
                if (sumTextStr.length() == 1 && sumTextStr.contains(POINTER)) {
                    returnText = ZERO;
                    // 防止转换BigDecimal报错
                    sumTextStr = ZERO + sumTextStr;
                }

                // .xx这种情况需要补0
                if (sumTextStr.startsWith(POINTER)) {
                    returnText = ZERO;
                    // 防止转换BigDecimal报错
                    sumTextStr = ZERO + sumTextStr;
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
            }

            return returnText;
        }


        // 拼接
        String sumTextStr;
        // 返回的内容
        String returnText = sourceText;

        // 添加操作或替换操作
        Matcher matcher = mPattern.matcher(source);
        // 不符合正则表达式
        if (!matcher.matches()) {
            // 替换或输入的字符串不合规
            sourceText = "";
            returnText = "";
        }

        // 上方已经拦截了删除的情况，只要不相等就认为是替换了
        if (dstart != dend) {

            // 替换操作

            // 如：原文 123.56 要替换的内容56 替换的内容2.2 不符合规则  上方正则只验证了替换的内容只允许有一个.
            if (destText.contains(POINTER) && sourceText.contains(POINTER) && !destText.substring(dstart, dend).contains(POINTER)) {
                sourceText = "";
                returnText = "";
            }

            if (dstart == 0) {
                // 首位开始替换
                sumTextStr = sourceText + destText.substring(dend);
            } else if (dstart == destText.length()) {
                // 末尾替换
                sumTextStr = destText.substring(0, dend) + sourceText;
            } else {
                // 中间替换
                sumTextStr = (destText.substring(0, dstart) + sourceText + destText.substring(dend));
            }

        } else {

            // 添加操作

            // 如：原文 123.56 添加的内容2.2 不符合规则  上方正则只验证了替换的内容只允许有一个.
            if (destText.contains(POINTER) && sourceText.contains(POINTER)) {
                sourceText = "";
                returnText = "";
            }

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
        }

        if (TextUtils.isEmpty(sumTextStr)) {
            // 如 123 要替换的内容123 替换成abc 组装成的字符串为空
            // 如 空字符串 添加abc 组装成的字符串为空
            if (mListener != null)
                mListener.correct("", ZERO_POINT);
            return "";
        }

        // .xx这种情况需要补0
        if (sumTextStr.startsWith(POINTER)) {
            returnText = ZERO + sourceText;
            // 防止转换BigDecimal报错
            sumTextStr = ZERO + sumTextStr;
        }

        // 如 10.555 只截取正确的，并在监听里进行重新设置setText()
        if (sumTextStr.contains(POINTER) && sumTextStr.indexOf(POINTER) + 3 < sumTextStr.length())
            sumTextStr = sumTextStr.substring(0, sumTextStr.indexOf(POINTER) + 3);

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
