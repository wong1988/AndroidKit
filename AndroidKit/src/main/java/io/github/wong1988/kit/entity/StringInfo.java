package io.github.wong1988.kit.entity;

import java.util.Collections;
import java.util.List;

/**
 * 字符串-字符信息
 */
public class StringInfo {

    // 拆分所有字符
    private List<Character> allChar;
    // 除英文、数字外的字符
    private List<Character> otherChar;
    // 除英文、数字外的字符下标
    private List<Integer> otherCharIndex;
    // 大写字符
    private List<Character> uppercaseChar;
    // 大写字符下标
    private List<Integer> uppercaseCharIndex;
    // 小写字符
    private List<Character> lowercaseChar;
    // 小写字符下标
    private List<Integer> lowercaseCharIndex;
    // 数字字符
    private List<Character> numberChar;
    // 数字字符下标
    private List<Integer> numberCharIndex;

    public StringInfo() {
        this.allChar = Collections.emptyList();
        this.otherChar = Collections.emptyList();
        this.otherCharIndex = Collections.emptyList();
        this.uppercaseChar = Collections.emptyList();
        this.uppercaseCharIndex = Collections.emptyList();
        this.lowercaseChar = Collections.emptyList();
        this.lowercaseCharIndex = Collections.emptyList();
        this.numberChar = Collections.emptyList();
        this.numberCharIndex = Collections.emptyList();
    }

    public List<Character> getAllChar() {
        return allChar;
    }

    public void setAllChar(List<Character> allChar) {
        if (allChar == null)
            this.allChar.clear();
        else
            this.allChar = allChar;
    }

    public List<Character> getOtherChar() {
        return otherChar;
    }

    public List<Integer> getOtherCharIndex() {
        return otherCharIndex;
    }

    public void setOtherChar(List<Character> otherChar, List<Integer> otherCharIndex) {
        if (otherChar == null)
            otherChar = Collections.emptyList();
        if (otherCharIndex == null)
            otherCharIndex = Collections.emptyList();
        if (otherChar.size() != otherCharIndex.size()) {
            otherChar.clear();
            otherCharIndex.clear();
        }
        this.otherChar = otherChar;
        this.otherCharIndex = otherCharIndex;
    }

    public List<Character> getUppercaseChar() {
        return uppercaseChar;
    }

    public List<Integer> getUppercaseCharIndex() {
        return uppercaseCharIndex;
    }

    public void setUppercaseChar(List<Character> uppercaseChar, List<Integer> uppercaseCharIndex) {
        if (uppercaseChar == null)
            uppercaseChar = Collections.emptyList();
        if (uppercaseCharIndex == null)
            uppercaseCharIndex = Collections.emptyList();
        if (uppercaseChar.size() != uppercaseCharIndex.size()) {
            uppercaseChar.clear();
            uppercaseCharIndex.clear();
        }
        this.uppercaseChar = uppercaseChar;
        this.uppercaseCharIndex = uppercaseCharIndex;
    }

    public List<Character> getLowercaseChar() {
        return lowercaseChar;
    }

    public List<Integer> getLowercaseCharIndex() {
        return lowercaseCharIndex;
    }

    public void setLowercaseChar(List<Character> lowercaseChar, List<Integer> lowercaseCharIndex) {
        if (lowercaseChar == null)
            lowercaseChar = Collections.emptyList();
        if (lowercaseCharIndex == null)
            lowercaseCharIndex = Collections.emptyList();
        if (lowercaseChar.size() != lowercaseCharIndex.size()) {
            lowercaseChar.clear();
            lowercaseCharIndex.clear();
        }
        this.lowercaseChar = lowercaseChar;
        this.lowercaseCharIndex = lowercaseCharIndex;
    }

    public List<Character> getNumberChar() {
        return numberChar;
    }

    public List<Integer> getNumberCharIndex() {
        return numberCharIndex;
    }

    public void setNumberChar(List<Character> numberChar, List<Integer> numberCharIndex) {
        if (numberChar == null)
            numberChar = Collections.emptyList();
        if (numberCharIndex == null)
            numberCharIndex = Collections.emptyList();
        if (numberChar.size() != numberCharIndex.size()) {
            numberChar.clear();
            numberCharIndex.clear();
        }
        this.numberChar = numberChar;
        this.numberCharIndex = numberCharIndex;
    }

    public int getUppercase() {
        return uppercaseChar.size();
    }

    public int getLowercase() {
        return lowercaseChar.size();
    }

    public int getNumber() {
        return numberChar.size();
    }

}

