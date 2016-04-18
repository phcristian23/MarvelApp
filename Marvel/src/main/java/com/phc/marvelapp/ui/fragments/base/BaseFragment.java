package com.phc.marvelapp.ui.fragments.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.phc.marvelapp.injection.component.ApplicationComponent;
import com.phc.marvelapp.injection.module.ActivityModule;
import com.phc.marvelapp.ui.activities.base.BaseActivity;
import com.phc.marvelapp.ui.interfaces.ActivityInterface;

/**
 * Created by Horatiu on 4/15/2016.
 */
public abstract class BaseFragment extends Fragment{
    private ActivityInterface activityInterface;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof BaseActivity) {
            this.activityInterface = ((BaseActivity) context);
        } else {
            throw new ClassCastException(String.format("%s must implement Activity Interface", getActivity().getClass().getSimpleName()));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof BaseActivity) {
            this.activityInterface = ((BaseActivity) activity);
        } else {
            throw new ClassCastException(String.format("%s must implement Activity Interface", activity.getClass().getSimpleName()));
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    protected ApplicationComponent getApplicationComponent() {
        return activityInterface.getApplicationComponent();
    }

    protected ActivityModule getActivityModule() {
        return activityInterface.getActivityModule();
    }

    protected void changeFragment(Fragment fragment, boolean addToBackStack) {
        this.activityInterface.changeFragment(fragment, addToBackStack);
    }

    protected void startWorkerFragment(Fragment fragment, String tag) {
        this.activityInterface.startWorkerFragment(fragment, tag);
    }

    protected void removeFragmentByTag(String tag) {
        this.activityInterface.removeFragmentByTag(tag);
    }
}
