package com.phc.api.impl.network.models;

import java.io.File;

/**
 * Created by Horatiu
 */
public class Comic {

    private long comicID;
    private String comicName;
    private String imageURL;
    private File imageFile;
    private boolean useCustomFile;


    public long getComicID() {
        return comicID;
    }

    public void setComicID(long comicID) {
        this.comicID = comicID;
    }

    public String getComicName() {
        return comicName;
    }

    public void setComicName(String comicName) {
        this.comicName = comicName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public File getImageFile() {
        return imageFile;
    }

    public void setImageFile(File imageFile) {
        this.imageFile = imageFile;
    }

    public boolean useCustomFile() {
        return useCustomFile;
    }

    public void setUseCustomFile(boolean useCustomFile) {
        this.useCustomFile = useCustomFile;
    }
}
