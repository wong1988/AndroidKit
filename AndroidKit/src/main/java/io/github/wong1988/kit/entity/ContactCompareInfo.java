package io.github.wong1988.kit.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 联系人拼音比较实体类
 */
public class ContactCompareInfo {

    // 对比取值string【有可能跳字符，推荐使用下方取值index】
    private final String targetValue;

    // 对比取值在原文中的index
    private final List<Integer> targetIndexes;


    public ContactCompareInfo() {
        this.targetValue = "";
        this.targetIndexes = new ArrayList<>();
    }

    public ContactCompareInfo(String targetValue, List<Integer> targetIndexes) {

        if (targetValue == null) {
            targetValue = "";
        }

        if (targetIndexes == null)
            targetIndexes = new ArrayList<>();

        if (targetValue.length() != targetIndexes.size()) {
            targetValue = "";
            targetIndexes.clear();
        }

        this.targetValue = targetValue;
        this.targetIndexes = targetIndexes;
    }

    public String getTargetValue() {
        return targetValue;
    }

    public List<Integer> getTargetIndexes() {
        return targetIndexes;
    }

    @Override
    public String toString() {
        return "PinYinCompare{" +
                "targetValue='" + targetValue + '\'' +
                ", targetIndexes=" + targetIndexes +
                '}';
    }
}
