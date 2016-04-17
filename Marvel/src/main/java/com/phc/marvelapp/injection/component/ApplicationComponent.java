package com.phc.marvelapp.injection.component;

import com.phc.api.impl.dropbox.DropboxManager;
import com.phc.api.impl.network.MarvelApi;
import com.phc.marvelapp.injection.module.ApplicationModule;
import com.phc.marvelapp.preferences.MarvelPreferences;
import com.phc.marvelapp.ui.activities.base.BaseActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Horatiu
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(BaseActivity activity);

    MarvelApi getApiManager();
    DropboxManager getDropboxManager();
    MarvelPreferences getPreferences();
}
