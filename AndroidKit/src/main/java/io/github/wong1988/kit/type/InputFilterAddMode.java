package io.github.wong1988.kit.type;

/**
 * InputFilter添加方式
 */
public enum InputFilterAddMode {

    REPLACE("直接把EditText的filter替换成新的"),
    APPEND("EditText现有的filter不动再追加新的filter，可能会出现重复的filter"),
    APPEND_SUPER("EditText现有的filter(与新filter相同类型会被移除)与新的filter进行合并");

    InputFilterAddMode(String describe) {
    }
}
