package com.phc.api.base.converter;

import com.google.gson.Gson;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.ResponseBody;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import retrofit.Converter;

/**
 * Created by Horatiu Paval
 * On 11/19/2015.
 */
public class JsonConverter extends Converter.Factory{

    public static JsonConverter create() {
        return create(new Gson());
    }

    public static JsonConverter create(Gson gson) {
        return new JsonConverter(gson);
    }

    private final Gson gson;

    private JsonConverter(Gson gson) {
        if (gson == null) throw new NullPointerException("gson == null");
        this.gson = gson;
    }

    @Override
    public Converter<ResponseBody, ?> fromResponseBody(Type type, Annotation[] annotations) {
        return new ResponseBodyConverter<>(gson, type);
    }

    @Override public Converter<?, RequestBody> toRequestBody(Type type, Annotation[] annotations) {
        return new RequestBodyConverter<>(gson, type);
    }
}
