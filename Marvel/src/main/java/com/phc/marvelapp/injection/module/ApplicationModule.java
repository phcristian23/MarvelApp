package com.phc.marvelapp.injection.module;

import android.app.Application;

import com.phc.api.impl.dropbox.DropboxManager;
import com.phc.api.impl.network.MarvelApi;
import com.phc.marvelapp.preferences.MarvelPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Horatiu
 */
@Module
public class ApplicationModule {

    private final Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public MarvelApi providesMarvelApi() {
        return MarvelApi.instance(application.getApplicationContext());
    }

    @Provides
    @Singleton
    public MarvelPreferences providesMarvelPreferences() {
        return new MarvelPreferences(application);
    }

    @Provides
    @Singleton
    public DropboxManager providesDropboxManager() {
        return DropboxManager.instance().setToken(providesMarvelPreferences().getDropboxAuthKey());
    }
}
