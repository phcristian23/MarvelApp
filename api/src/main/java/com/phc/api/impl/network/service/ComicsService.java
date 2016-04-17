package com.phc.api.impl.network.service;

import com.phc.api.impl.network.models.response.Comics;

import java.util.HashMap;

import retrofit.http.GET;
import retrofit.http.QueryMap;
import rx.Observable;

/**
 * Created by Horatiu on 4/15/2016.
 */
public interface ComicsService {

    @GET("/v1/public/comics")
    Observable<Comics> getComics(@QueryMap HashMap<String, String> params);
}
