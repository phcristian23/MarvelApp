package com.phc.marvelapp.ui.activities.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.phc.marvelapp.R;
import com.phc.marvelapp.application.MarvelApplication;
import com.phc.marvelapp.injection.component.ApplicationComponent;
import com.phc.marvelapp.injection.module.ActivityModule;
import com.phc.marvelapp.ui.interfaces.ActivityInterface;

import butterknife.ButterKnife;

/**
 * Created by Horatiu on 4/14/2016.
 */
public abstract class BaseActivity extends AppCompatActivity implements ActivityInterface{

    private FragmentManager fragmentManager;
    private FrameLayout mainView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.fragmentManager = getSupportFragmentManager();
        setContentView(R.layout.activity_base);
        this.mainView = (FrameLayout) findViewById(R.id.main_view);
        ButterKnife.bind(this);
    }

    private void inject() {
        ((MarvelApplication) getApplication()).getComponent().inject(this);
    }

    @Override
    public ApplicationComponent getApplicationComponent() {
        return ((MarvelApplication) getApplication()).getComponent();
    }

    @Override
    public ActivityModule getActivityModule() {
        return new ActivityModule(this);
    }

    @Override
    public void changeFragment(Fragment fragment, boolean addToBackStack) {
        changeFragmentImplementation(fragment, addToBackStack, true);
    }

    private void changeFragmentImplementation(final Fragment fragment, final boolean addToBackStack, final boolean safe) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        Fragment inflatedFragment = fragmentManager.findFragmentById(mainView.getId());

        if (inflatedFragment != null &&
                inflatedFragment.getClass().getSimpleName().equals(fragment.getClass().getSimpleName()) && safe) {
            return;
        }

        transaction.replace(mainView.getId(), fragment);
        if (addToBackStack) {
            transaction.addToBackStack(fragment.getClass().getSimpleName());
        } else {
            clearFragmentStack();
        }

        transaction.commitAllowingStateLoss();
    }

    private void clearFragmentStack(){
        if(fragmentManager.getBackStackEntryCount() > 0){
            for(int i = 0; i < fragmentManager.getBackStackEntryCount(); i++){
                fragmentManager.popBackStack();
            }
        }
    }

    @Override
    public void startWorkerFragment(Fragment fragment, String fragmentTag) {
        startWorkerFragmentImpl(fragment, fragmentTag);
    }

    @Override
    public void removeFragmentByTag(String fragmentTag) {
        removeFragmentByTagImpl(fragmentTag);
    }

    private void startWorkerFragmentImpl(Fragment fragment, String fragmentTag) {
        Fragment oldInstance = fragmentManager.findFragmentByTag(fragmentTag);

        if(oldInstance == null) {
            fragmentManager.beginTransaction().add(fragment, fragmentTag).commit();
        }
    }

    private void removeFragmentByTagImpl(String fragmentTag) {
        Fragment oldInstance = fragmentManager.findFragmentByTag(fragmentTag);

        if (oldInstance != null) {
            fragmentManager.beginTransaction().remove(oldInstance).commitAllowingStateLoss();
        }
    }
}
