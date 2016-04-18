package com.phc.marvelapp.preferences;

import android.app.Application;

import com.phc.marvelapp.preferences.base.PreferenceManager;

import java.util.Set;

/**
 * Created by Horatiu on 4/17/2016.
 */
public class MarvelPreferences extends PreferenceManager {
    private static final String DROPBOX_AUTH_KEY = "DROPBOX_AUTH_KEY";
    private static final String IMAGE_CUSTOM_KEY = "IMAGE_CUSTOM_KEY";

    public MarvelPreferences(Application application) {
        super(application);
    }

    public void setDropboxAuthKey(String token) {
        set(DROPBOX_AUTH_KEY, token);
    }

    public String getDropboxAuthKey() {
        return get(DROPBOX_AUTH_KEY, null);
    }

    public void addComicID(Long id) {
        Set<String> idSet = get(IMAGE_CUSTOM_KEY);
        remove(IMAGE_CUSTOM_KEY);

        idSet.add(id.toString());

        set(IMAGE_CUSTOM_KEY, idSet);
    }

    public boolean comicIDExists(Long id) {
        Set<String> idSet = get(IMAGE_CUSTOM_KEY);

        if (idSet == null) {
            return false;
        } else {
            return idSet.contains(id.toString());
        }
    }
}
