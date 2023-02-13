package io.github.wong1988.kit.type;

public enum SortMode {

    ASC("升序"),
    DESC("降序");

    public String describe;

    SortMode(String describe) {
        this.describe = describe;
    }
}
