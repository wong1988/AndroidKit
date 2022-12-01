package io.github.wong1988.kit.utils;

import java.util.Arrays;
import java.util.List;

public class ArrayUtils {

    /**
     * 复制数组
     *
     * @param originalArray  原数组，即待拷贝数组
     * @param newArrayLength 新数组长度，如果小于原数组即复制原数组的前几个，反之后面多余的为空元素
     * @return 新数组
     */
    public static <T> T[] copyArray(T[] originalArray, int newArrayLength) {

        if (newArrayLength < 0)
            newArrayLength = 0;

        return Arrays.copyOf(originalArray, newArrayLength);
    }


    /**
     * @param original    原集合
     * @param targetArray 目标数组，请初始化长度为元集合的长度
     * @return 集合转数组
     */
    public static <T> T[] toArray(List<T> original, T[] targetArray) {
        return original.toArray(targetArray);
    }

}
