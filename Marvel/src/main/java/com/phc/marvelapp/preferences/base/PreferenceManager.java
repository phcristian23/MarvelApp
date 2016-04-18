package com.phc.marvelapp.preferences.base;

import android.app.Application;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Horatiu
 */
public class PreferenceManager {

    private final SharedPreferences mPreferences;

    public PreferenceManager(Application application) {
        mPreferences = application.getSharedPreferences(application.getPackageName(), 0);
    }

    protected final String get(String key, String defaultValue) {
        return this.mPreferences.getString(key, defaultValue);
    }

    protected final int get(String key, int defaultValue) {
        return this.mPreferences.getInt(key, defaultValue);
    }

    protected final long get(String key, long defaultValue) {
        return this.mPreferences.getLong(key, defaultValue);
    }

    protected final float get(String key, float defaultValue) {
        return this.mPreferences.getFloat(key, defaultValue);
    }

    protected final boolean get(String key, boolean defaultValue) {
        return this.mPreferences.getBoolean(key, defaultValue);
    }

    protected final Set<String> get(String key) {
        return this.mPreferences.getStringSet(key, new HashSet<String>());
    }

    protected final boolean set(String key, String value) {
        return this.mPreferences.edit().putString(key, value).commit();
    }

    protected final boolean set(String key, int value) {
        return this.mPreferences.edit().putInt(key, value).commit();
    }

    protected final boolean set(String key, long value) {
        return this.mPreferences.edit().putLong(key, value).commit();
    }

    protected final boolean set(String key, float value) {
        return this.mPreferences.edit().putFloat(key, value).commit();
    }

    protected final boolean set(String key, boolean value) {
        return this.mPreferences.edit().putBoolean(key, value).commit();
    }

    protected final boolean set(String key, Set<String> values) {
        return this.mPreferences.edit().putStringSet(key, values).commit();
    }

    protected final void remove(String key) {
        this.mPreferences.edit().remove(key).apply();
    }
}
