package io.github.wong1988.kit.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式
 */
public class RegexUtils {

    // 只有英文和数字，正确返回true
    static final String ONLY_ENGLISH_AND_NUMBERS = "^[A-Za-z0-9]+$";
    // 只有大写和数字，正确返回true
    static final String ONLY_UPPERCASE_AND_NUMBERS = "^[A-Z0-9]+$";
    // 只有小写和数字，正确返回true
    public static final String ONLY_LOWERCASE_AND_NUMBERS = "^[a-z0-9]+$";
    // 只有数字，正确返回true
    public static final String ONLY_NUMBERS = "^[0-9]+$";
    // 是否包含数字
    static final String CONTAINS_NUMBERS = ".*\\d+.*";
    // 是否包含特殊字符
    static final String CONTAINS_SPECIAL = "([^\\u4e00-\\u9fa5\\w])+?";


    /**
     * 是否包含数字
     */
    public static boolean containNum(String str) {
        Pattern r = Pattern.compile(CONTAINS_NUMBERS);
        Matcher m = r.matcher(str);
        return m.matches();
    }

    /**
     * 仅有英文和数字
     */
    public static boolean onlyEnglishAndNumbers(String str) {
        Pattern r = Pattern.compile(ONLY_ENGLISH_AND_NUMBERS);
        Matcher m = r.matcher(str);
        return m.matches();
    }

    /**
     * 仅有大写和数字
     */
    public static boolean onlyUppercaseAndNumbers(String str) {
        Pattern r = Pattern.compile(ONLY_UPPERCASE_AND_NUMBERS);
        Matcher m = r.matcher(str);
        return m.matches();
    }

    /**
     * 移除特殊字符
     */
    public static String removeSpecialChars(String str) {
        if (str == null)
            return "";
        Pattern pattern = Pattern.compile(CONTAINS_SPECIAL);
        Matcher matcher = pattern.matcher(str);
        return matcher.replaceAll("");
    }
}
