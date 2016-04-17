package com.phc.api.base.callback;


import com.phc.api.impl.network.ApiResponse;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Horatiu Paval on 3/28/2016.
 *
 */
public abstract class BaseWorkerResponse<Model> implements Callback<Model>, ApiResponse<Model> {
    @Override
    public void onResponse(Response<Model> response, Retrofit retrofit) {
        Integer responseCode = response.code();

        if (responseCode >= 200 && responseCode < 300) {
            parseSuccess(response.body());
        } else {
            parseError(response, retrofit);
        }
    }

    @Override
    public void onFailure(Throwable t) {

    }

    private void parseSuccess(Model response) {
        if (response != null) {
            onSuccess(response);
        } else {
            onError(-1, "Response Model is empty!");
        }
    }

    private void parseError(Response<Model> response, Retrofit retrofit) {
        onError(response.code(), response.message());
    }
}
