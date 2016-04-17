package com.phc.api.base;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.phc.api.base.converter.JsonConverter;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okio.Buffer;
import retrofit.Retrofit;

/**
 * Created by Horatiu Paval
 * On 3/21/2016.
 */
public abstract class BaseNetworkModule {
    // Init defaults
    private static int CONNECTION_TIMEOUT = 30;
    private static int READ_TIMEOUT = CONNECTION_TIMEOUT;
    private static int WRITE_TIMEOUT = CONNECTION_TIMEOUT;
    private static boolean DEBUG = true;

    protected Retrofit retrofit;
    private Retrofit.Builder builder;

    public BaseNetworkModule() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        builder = new Retrofit.Builder()
                .addConverterFactory(JsonConverter.create(gson));
    }

    protected final Retrofit buildRetrofit() {
        this.builder.client(buildClient());
        this.builder = customizeBuilder(builder);

        if (builder != null) {
            retrofit = builder.build();
        }

        return retrofit;
    }

    protected abstract Retrofit.Builder customizeBuilder(Retrofit.Builder builder);

    private OkHttpClient buildClient() {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS);
        client.setReadTimeout(READ_TIMEOUT, TimeUnit.SECONDS);
        client.setWriteTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);

        // Header Injection
        client.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                com.squareup.okhttp.Request original = chain.request();
                return chain.proceed(addHeaderInjection(original));
            }
        });

        // Request Params Injection
        client.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                com.squareup.okhttp.Request original = chain.request();
                return chain.proceed(addParamsInjection(original));
            }
        });

        if (DEBUG) {
            client.interceptors().add(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    com.squareup.okhttp.Request originalRequest = chain.request();
                    logRequest(originalRequest);

                    Response originalResponse = chain.proceed(originalRequest);
                    ResponseBody responseBody = originalResponse.body();

                    // Log Response Part
                    String responseBodyString = originalResponse.body().string();
                    System.out.println(String.format("Retrofit Response\n\nCode %s, %s\n%s",
                            originalResponse.code(),
                            originalResponse.message(),
                            responseBodyString
                    ));
                    return originalResponse.newBuilder().body(ResponseBody.create(responseBody.contentType(), responseBodyString.getBytes())).build();
                }
            });
        }

        return customizeClient(client);
    }

    protected OkHttpClient customizeClient(OkHttpClient client) {
        return client;
    }

    private com.squareup.okhttp.Request addHeaderInjection(com.squareup.okhttp.Request original) {
        com.squareup.okhttp.Request.Builder builder = original.newBuilder();
        HashMap<String, String> headers = new HashMap<>();
        headers = addHeaders(headers);

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            builder.addHeader(entry.getKey(), entry.getValue());
        }

        builder.method(original.method(), original.body());

        return builder.build();
    }

    protected HashMap<String, String> addHeaders(HashMap<String, String> headers) {
        return new HashMap<>();
    }

    private com.squareup.okhttp.Request addParamsInjection(com.squareup.okhttp.Request original) {
        com.squareup.okhttp.Request.Builder builder = original.newBuilder();


        String method = original.method().trim().toUpperCase();
        String url = original.urlString();
        RequestBody originalBody = original.body();

        if (method.equals("GET")) {
            String urlWithParams = original.urlString();
            String[] splitUrlParams = urlWithParams.split("\\?");
            String finalUrl = splitUrlParams[0];
            String getParams = splitUrlParams[1];

            if (splitUrlParams.length == 2 && splitUrlParams[1].length() > 0) {
                String finalParams = getParamsInjection(method, finalUrl, getParams);
                builder.url(String.format("%s?%s", finalUrl, finalParams));
            }
        } else {
            if (originalBody != null) {
                // MediaType.parse("application/json; charset=utf-8")
                originalBody = RequestBody.create(originalBody.contentType(),
                    getParamsInjection(method, url, originalBody.toString())
                );
            }
        }

        builder.method(original.method(), originalBody);
        return builder.build();
    }

    protected String getParamsInjection(String callMethod, String callURL, String parameters) {
        return parameters;
    }

    private void logRequest(com.squareup.okhttp.Request original) {
        System.out.println(String.format("%s\n\n%s\n%s", "Api request",
                original.urlString(),
                original.headers().toString()
        ));

        Buffer buffer = new Buffer();
        try {
            RequestBody body = original.body();
            if (body != null) {
                body.writeTo(buffer);
            }

            System.out.println(buffer.readUtf8());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getConnectionTimeout() {
        return CONNECTION_TIMEOUT;
    }

    public static void setConnectionTimeout(int connectionTimeout) {
        CONNECTION_TIMEOUT = connectionTimeout;
    }

    public static int getReadTimeout() {
        return READ_TIMEOUT;
    }

    public static void setReadTimeout(int readTimeout) {
        READ_TIMEOUT = readTimeout;
    }

    public static int getWriteTimeout() {
        return WRITE_TIMEOUT;
    }

    public static void setWriteTimeout(int writeTimeout) {
        WRITE_TIMEOUT = writeTimeout;
    }

    public static boolean isDEBUG() {
        return DEBUG;
    }

    public static void setDEBUG(boolean DEBUG) {
        BaseNetworkModule.DEBUG = DEBUG;
    }
}