package com.phc.marvelapp.injection.component;

import android.app.Activity;

import com.phc.marvelapp.injection.module.ActivityModule;
import com.phc.marvelapp.injection.scope.PerActivity;

import dagger.Component;

/**
 * Created by Horatiu
 */
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class) @PerActivity
public interface ActivityComponent {
    Activity activity();
}
