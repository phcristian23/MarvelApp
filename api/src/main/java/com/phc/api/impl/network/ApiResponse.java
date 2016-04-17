package com.phc.api.impl.network;

/**
 * Created by Horatiu Paval on 3/28/2016.
 *
 */
public interface ApiResponse<Model> {
    void onSuccess(Model response);
    void onError(int code, String message);
}
