package com.phc.api.impl.network.managers;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Horatiu on 4/15/2016.
 */
public class BaseManager {
    protected static final int HTTP_OK = 200;

    public <T> void execute(Observable<T> observable, Subscriber<T> subscriber) {
        observable.subscribeOn(Schedulers.computation())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(subscriber);
    }
}
