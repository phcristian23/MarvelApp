package com.phc.marvelapp.ui.interfaces;

import android.support.v4.app.Fragment;

import com.phc.marvelapp.injection.component.ApplicationComponent;
import com.phc.marvelapp.injection.module.ActivityModule;

/**
 * Created by Horatiu on 4/15/2016.
 */
public interface ActivityInterface {
    ApplicationComponent getApplicationComponent();
    ActivityModule getActivityModule();
    void changeFragment(Fragment fragment, boolean addToBackStack);
    void startWorkerFragment(Fragment fragment, String fragmentTag);
    void removeFragmentByTag(String fragmentTag);
}
