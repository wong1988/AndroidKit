package io.github.wong1988.kit.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 联系人拼音的实体类
 */
public class ContactPinYinInfo {

    // 每个字符的拼音
    private final List<List<String>> pinyin;
    // 当前字符串的多种读音组合【由实体类进行计算】
    private final String[] pinYinCombination;

    // 原文
    private final String originalText;

    // 符合的字符在原文中的下标(剔除特殊字符)
    private final List<Integer> fitCharIndexes;
    // 原文所有下标【由实体类进行计算】
    private final List<Integer> originalIndexes;

    public ContactPinYinInfo(String originalText, List<List<String>> pinyin, List<Integer> fitCharIndexes) {

        // 初始化其他内容

        if (originalText == null)
            originalText = "";

        this.originalText = originalText;

        this.originalIndexes = new ArrayList<>();

        for (int i = 0; i < originalText.length(); i++) {
            this.originalIndexes.add(i);
        }

        if (fitCharIndexes == null)
            fitCharIndexes = new ArrayList<>();

        this.fitCharIndexes = fitCharIndexes;

        // 计算组合

        if (pinyin == null) {
            pinyin = new ArrayList<>();
            this.fitCharIndexes.clear();
        }

        this.pinyin = pinyin;

        if (this.pinyin.size() != this.fitCharIndexes.size()) {
            this.pinyin.clear();
            this.fitCharIndexes.clear();
        }

        if (this.pinyin.size() == 0) {
            this.pinYinCombination = new String[0];
            return;
        }

        // 一共有多少种组合
        int totalAssemble = 1;

        for (int i = 0; i < this.pinyin.size(); i++) {
            totalAssemble *= this.pinyin.get(i).size();
        }

        this.pinYinCombination = new String[totalAssemble];

        // 开始递归写入拼音组合
        assemble(1, pinYinCombination.length);
    }

    public String[] getPinYinCombination() {
        return pinYinCombination;
    }

    public String getOriginalText() {
        return originalText;
    }

    public List<Integer> getFitCharIndexes() {
        return fitCharIndexes;
    }

    public List<Integer> getOriginalIndexes() {
        return originalIndexes;
    }

    public int getFitCharCount() {
        return pinyin.size();
    }

    // 写入数组第几项，循环哪个汉字的集合
    private void assemble(int count, int total) {

        if (pinyin.size() < count)
            return;

        List<String> list = pinyin.get(count - 1);

        // 每几个一循环
        int forCount = total / list.size();

        int pos = 0;
        int temp = 0;
        for (int i = 0; i < pinYinCombination.length; i++) {

            if (pinYinCombination[i] == null)
                pinYinCombination[i] = "";
            pinYinCombination[i] += list.get(pos);

            temp++;

            if (temp == forCount) {
                pos++;
                if (pos >= list.size())
                    pos = 0;
                temp = 0;
            }
        }

        // 开启下一个字符循环写入数组
        assemble(count + 1, forCount);
    }

    @Override
    public String toString() {
        return "ContactPinYinInfo{" +
                "pinyin=" + pinyin +
                ", pinYinCombination=" + Arrays.toString(pinYinCombination) +
                ", originalText='" + originalText + '\'' +
                ", fitCharIndexes=" + fitCharIndexes +
                ", originalIndexes=" + originalIndexes +
                '}';
    }
}
