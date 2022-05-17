package io.github.wong1988.kit.utils;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import io.github.wong1988.kit.entity.StringInfo;

public class StringUtils {

    /**
     * 转换int
     */
    public static int parseInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 转换long
     */
    public static long parseLong(String str) {
        try {
            return Long.parseLong(str);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 转换float
     */
    public static float parseFloat(String str) {
        try {
            return Float.parseFloat(str);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 转换double
     */
    public static double parseDouble(String str) {
        try {
            return Double.parseDouble(str);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 获取字符串相关信息
     * 包含大写、数字等相关信息
     */
    public static StringInfo stringInfo(String str) {

        StringInfo stringInfo = new StringInfo();

        if (!TextUtils.isEmpty(str)) {

            List<Character> allChar = new ArrayList<>();
            List<Character> uppercaseChar = new ArrayList<>();
            List<Integer> uppercaseCharIndex = new ArrayList<>();
            List<Character> lowercaseChar = new ArrayList<>();
            List<Integer> lowercaseCharIndex = new ArrayList<>();
            List<Character> numberChar = new ArrayList<>();
            List<Integer> numberCharIndex = new ArrayList<>();
            List<Character> otherChar = new ArrayList<>();
            List<Integer> otherCharIndex = new ArrayList<>();

            for (int i = 0; i < str.length(); i++) {

                char c = str.charAt(i);
                allChar.add(c);

                if (c >= 'a' && c <= 'z') {
                    lowercaseChar.add(c);
                    lowercaseCharIndex.add(i);
                } else if (c >= 'A' && c <= 'Z') {
                    uppercaseChar.add(c);
                    uppercaseCharIndex.add(i);
                } else if (c >= '0' && c <= '9') {
                    numberChar.add(c);
                    numberCharIndex.add(i);
                } else {
                    otherChar.add(c);
                    otherCharIndex.add(i);
                }
            }

            stringInfo.setAllChar(allChar);
            stringInfo.setOtherChar(otherChar, otherCharIndex);
            stringInfo.setUppercaseChar(uppercaseChar, uppercaseCharIndex);
            stringInfo.setLowercaseChar(lowercaseChar, lowercaseCharIndex);
            stringInfo.setNumberChar(numberChar, numberCharIndex);
        }

        return stringInfo;
    }

    /**
     * 去除字符串首尾空格（全角 & 半角）
     */
    public static String trim(String s) {

        if (TextUtils.isEmpty(s))
            return "";

        // 转换成字符
        char[] val = s.toCharArray();

        // 默认截取全部
        int st = 0;
        int len = val.length;

        // 全角空格 Unicode编码 12288
        // 半角空格 Unicode编码 32
        while ((st < len) && (val[st] <= 32) || val[st] == 12288) {
            st++;
        }

        while ((st < len) && (val[len - 1] <= 32 || val[len - 1] == 12288)) {
            len--;
        }

        return ((st > 0) || (len < val.length)) ? s.substring(st, len) : s;
    }

}
