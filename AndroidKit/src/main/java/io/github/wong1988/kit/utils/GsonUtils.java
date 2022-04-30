package io.github.wong1988.kit.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.github.wong1988.kit.gson.CollectionTypeAdapterFactory;
import io.github.wong1988.kit.gson.StringTypeAdapterFactory;

public class GsonUtils {

    /**
     * 默认的Gson
     */
    public static Gson gson() {
        return new Gson();
    }

    /**
     * 序列化时不忽略@Null值
     */
    public static Gson gsonSerializeNulls() {
        return new GsonBuilder().serializeNulls().create();
    }

    /**
     * 序列化时不忽略@Null值，
     * 序列化、反序列化时赋予String null为空字符串，集合null为长度为0的空集合
     */
    public static Gson gsonNullsByDefault() {
        return new GsonBuilder()
                .registerTypeAdapterFactory(new StringTypeAdapterFactory())
                .registerTypeAdapterFactory(new CollectionTypeAdapterFactory())
                .serializeNulls()
                .create();
    }

}
