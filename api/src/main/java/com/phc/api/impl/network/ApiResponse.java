package com.phc.api.impl.network;

/**
 * Created by Horatiu Paval on 3/28/2016.
 * horatiu.paval@osf-global.com
 */
public interface ApiResponse<Model> {
    void onSuccess(Model response);
    void onError(int code, String message);
}
