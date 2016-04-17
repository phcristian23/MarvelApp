package com.phc.api.impl.network;

import android.content.Context;

import com.phc.api.BuildConfig;
import com.phc.api.base.BaseNetworkModule;
import com.phc.api.base.utils.HashUtils;
import com.phc.api.impl.network.managers.ComicsManager;
import com.phc.api.impl.network.service.ComicsService;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;
import java.util.Calendar;
import java.util.TimeZone;

import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

public class MarvelApi extends BaseNetworkModule{
    private static final int NETWORK_CACHE_SIZE_MAX = 15 * 1024 * 1024;

    private static final String GLOBAL_KEY_APIKEY = "apikey";
    private static final String GLOBAL_KEY_TIMESTAMP = "ts";
    private static final String GLOBAL_KEY_HASH = "hash";

    private Context context;
    private ComicsManager comicsManager;

    private MarvelApi(Context context) {
        this.context = context;
        buildRetrofit();

        this.comicsManager = new ComicsManager(retrofit.create(ComicsService.class));
    }

    @Override
    protected Retrofit.Builder customizeBuilder(Retrofit.Builder builder) {
        builder.baseUrl(BuildConfig.BASE_URL);
        builder.addCallAdapterFactory(RxJavaCallAdapterFactory.create());
        return builder;
    }

    @Override
    protected OkHttpClient customizeClient(OkHttpClient client) {
        Cache cache = handleNetworkCache();
        if (cache != null) {
            client.setCache(cache);
            return client;
        }

        return super.customizeClient(client);
    }

    @Override
    protected String getParamsInjection(String callMethod, String callURL, String parameters) {
        if (callMethod.equals("GET")) {
            long currentTimestamp = generateTimestamp();

            parameters = parameters.concat(String.format("&%s=%s", GLOBAL_KEY_APIKEY, BuildConfig.MPUBLIC_KEY));
            parameters = parameters.concat(String.format("&%s=%s", GLOBAL_KEY_TIMESTAMP, currentTimestamp));
            parameters = parameters.concat(String.format("&%s=%s", GLOBAL_KEY_HASH, requestHashSignature(currentTimestamp)));

            return parameters;
        }

        return super.getParamsInjection(callMethod, callURL, parameters);
    }

    private Cache handleNetworkCache() {
        File baseCacheDir = context.getCacheDir();
        if (baseCacheDir != null) {
            File cacheDir = new File(baseCacheDir, "MarvelCache");
            return new Cache(cacheDir, NETWORK_CACHE_SIZE_MAX);
        }

        return null;
    }

    public static MarvelApi instance(Context context) {
        return new MarvelApi(context);
    }

    private String requestHashSignature(long timestamp) {
        return HashUtils.md5(String.valueOf(timestamp) + BuildConfig.MPRIVATE_KEY + BuildConfig.MPUBLIC_KEY);
    }

    private long generateTimestamp() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        return calendar.getTimeInMillis();
    }

    public ComicsManager getComicsManager() {
        return comicsManager;
    }
}
