package com.phc.api.impl.network.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;

/**
 * Created by Horatiu
 */
public class Comic implements Parcelable {

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.comicID);
        dest.writeString(this.comicName);
        dest.writeString(this.imageURL);
        dest.writeSerializable(this.imageFile);
        dest.writeByte(useCustomFile ? (byte) 1 : (byte) 0);
    }

    public Comic() {
    }

    protected Comic(Parcel in) {
        this.comicID = in.readLong();
        this.comicName = in.readString();
        this.imageURL = in.readString();
        this.imageFile = (File) in.readSerializable();
        this.useCustomFile = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Comic> CREATOR = new Parcelable.Creator<Comic>() {
        @Override
        public Comic createFromParcel(Parcel source) {
            return new Comic(source);
        }

        @Override
        public Comic[] newArray(int size) {
            return new Comic[size];
        }
    };
}
