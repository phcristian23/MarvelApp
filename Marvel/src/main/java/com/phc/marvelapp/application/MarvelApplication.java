package com.phc.marvelapp.application;

import android.app.Application;
import android.support.multidex.MultiDex;

import com.phc.marvelapp.injection.component.ApplicationComponent;
import com.phc.marvelapp.injection.component.DaggerApplicationComponent;
import com.phc.marvelapp.injection.module.ApplicationModule;

/**
 * Created by Horatiu on 4/14/2016.
 */
public class MarvelApplication extends Application {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);

        this.applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();

    }

    public ApplicationComponent getComponent() {
        return applicationComponent;
    }
}
