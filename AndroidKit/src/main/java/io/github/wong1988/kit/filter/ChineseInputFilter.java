package io.github.wong1988.kit.filter;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;

import com.github.promeg.pinyinhelper.Pinyin;

/**
 * 汉字的过滤器
 */
public class ChineseInputFilter implements InputFilter {

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
            return "";
        }

        // 删除操作
        // 选中粘贴空的也认为是删除操作
        if (TextUtils.isEmpty(sourceText) && dend > dstart) {
            return "";
        }


        // 添加 替换 操作
        if (TextUtils.isEmpty(sourceText))
            return "";

        StringBuilder builder = new StringBuilder();

        char[] chars = sourceText.toCharArray();

        for (char aChar : chars) {
            if (Pinyin.isChinese(aChar))
                builder.append(aChar);
        }

        return builder.toString();
    }

}
