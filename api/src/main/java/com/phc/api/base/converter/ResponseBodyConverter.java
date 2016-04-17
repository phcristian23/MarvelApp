package com.phc.api.base.converter;

import com.google.gson.Gson;
import com.squareup.okhttp.ResponseBody;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Type;

import okio.BufferedSource;
import retrofit.Converter;

/**
 * Created by Horatiu Paval
 * On 11/19/2015.
 */
class ResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    private final Gson gson;
    private final Type type;

    ResponseBodyConverter(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
    }

    @Override public T convert(ResponseBody value) throws IOException {
        BufferedSource source = value.source();
        try {
            String s = source.readUtf8();
            return gson.fromJson(s, type);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            closeQuietly(source);
        }
    }

    private static void closeQuietly(Closeable closeable) {
        if (closeable == null) return;
        try {
            closeable.close();
        } catch (IOException ignored) {
        }
    }
}
