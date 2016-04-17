package com.phc.marvelapp.injection.module;

import android.app.Activity;

import com.phc.marvelapp.injection.scope.PerActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Horatiu
 */
@Module
public class ActivityModule {
    private final Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @Provides @PerActivity
    public Activity getActivity() {
        return this.activity;
    }
}
