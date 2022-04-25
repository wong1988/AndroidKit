package io.github.wong1988.kit.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class StringAdapterFactory implements TypeAdapterFactory {

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        Class<T> rawType = (Class<T>) type.getRawType();
        if (String.class == rawType) {
            // 如果对象类型为String，返回自己实现的StringAdapter
            return (TypeAdapter<T>) new StringTypeAdapter();
        }
        return null;
    }

    public static class StringTypeAdapter extends TypeAdapter<String> {

        public String read(JsonReader reader) throws IOException {
            // json转实体，如果后台返回字段 name:null会被解析成name:""
            // 但后台不返回name字段则还是解析成null,不会解析成""
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
                return "";
            }
            return reader.nextString();
        }

        public void write(JsonWriter writer, String value) throws IOException {
            // 转json，空String字段默认赋空字符串
            if (value == null) {
                writer.value("");
                return;
            }
            writer.value(value);
        }
    }

}
