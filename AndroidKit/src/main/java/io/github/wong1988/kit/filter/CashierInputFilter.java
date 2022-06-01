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

    private final Pattern mPattern;

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

        if (TextUtils.isEmpty(MAX_VALUE)) {
            throw new RuntimeException("最大值不能为空或调用一参的构造方法");
        }

        Matcher matcher = mPattern.matcher(MAX_VALUE);
        // 不符合正则表达式
        if (!matcher.matches()) {
            throw new RuntimeException("最大值仅允许数字且.小于等于1个");
        }

        if (MAX_VALUE.contains(POINTER) && MAX_VALUE.length() - MAX_VALUE.lastIndexOf(POINTER) > 3) {
            throw new RuntimeException("最大值仅支持两位小数");
        }

        try {
            // 如果最大值错误 会自动报异常
            this.MAX_VALUE = new BigDecimal(MAX_VALUE);
        } catch (Exception e) {
            throw new RuntimeException("请检查最大值是否正确");
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

        // 新输入的字符串[键盘输入(char)、setText()的内容、粘贴的内容]
        String sourceText = source.toString();
        // 输入前的字符串
        String destText = dest.toString();

        if (TextUtils.isEmpty(sourceText) && TextUtils.isEmpty(destText)) {
            // 用户调用的setText("")做特殊处理
            if (mListener != null)
                mListener.correct("", ZERO_POINT);
            return "";
        }

        // 删除操作
        // 选中粘贴空的也认为是删除操作
        if (TextUtils.isEmpty(sourceText) && dend > dstart) {

            // 可能有需要补0的情况
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

                // .xx这种情况需要补0
                if (sumTextStr.startsWith(POINTER)) {
                    returnText = ZERO;
                    // 防止转换BigDecimal报错
                    sumTextStr = ZERO + sumTextStr;
                }

                // 需要验证金额，主要是最大值验证，金额格式肯定会正确，进行了补0 或者 if拦截了""
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
        String returnText;


        // 上方已经拦截了删除的情况，只要不相等就认为是替换了
        if (dstart != dend) {

            // 替换操作

            // 如：原文 123.56 要替换的内容56 替换的内容2.2 替换的内容不允许出现.
            if (destText.contains(POINTER) && sourceText.contains(POINTER) && !destText.substring(dstart, dend).contains(POINTER)) {
                sourceText = correctString(sourceText, false);
            } else {
                sourceText = correctString(sourceText, true);
            }

            returnText = sourceText;

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

            // 添加操作，直接粘贴空的也认为是追加操作

            // 如：原文 123.56 添加的内容2.2 添加的那日容不允许出现.
            if (destText.contains(POINTER)) {
                sourceText = correctString(sourceText, false);
            } else {
                sourceText = correctString(sourceText, true);
            }

            returnText = sourceText;

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
        // 如 添加操作 首位直接输入.
        // 如 替换操作 首位开始替换成. 首位开始粘贴空字符到.[响应到删除操作]
        if (sumTextStr.startsWith(POINTER)) {
            returnText = ZERO + sourceText;
            // 防止转换BigDecimal报错
            sumTextStr = ZERO + sumTextStr;
        }

        // 如 10.555 只截取正确的，并在Edittext监听里进行重新设置setText()
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


    /**
     * 返回正确可用内容
     */
    private String correctString(String s, boolean canDot) {

        if (TextUtils.isEmpty(s))
            return "";

        // 如果.有多个的话，只留最后一个
        int saveDotIndex = -1;

        if (canDot) {
            // 首次出现.
            int indexOf = s.indexOf(POINTER);
            // 最后一次出现.
            int lastIndexOf = s.lastIndexOf(POINTER);
            if (s.contains(POINTER) && indexOf != lastIndexOf)
                saveDotIndex = lastIndexOf;
            else
                saveDotIndex = indexOf;
        }

        StringBuilder builder = new StringBuilder();

        char[] chars = s.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            char aChar = chars[i];
            if (Character.isDigit(aChar)) {
                builder.append(aChar);
            } else if (saveDotIndex == i) {
                builder.append(aChar);
            }
        }

        return builder.toString();
    }
}
