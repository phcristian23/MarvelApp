package com.phc.marvelapp.ui.fragments.worker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.phc.api.impl.dropbox.DropboxManager;
import com.phc.marvelapp.ui.fragments.base.BaseFragment;

import java.io.File;

/**
 * Created by Horatiu on 4/17/2016.
 */
public class GetFileFragment extends BaseFragment {
    private static final int REQUEST_TAKE_PHOTO = 100;
    private static final String KEY_COMIC_ID = "KEY_COMIC_ID";
    private static final String KEY_COMIC_POSITION = "KEY_COMIC_POSITION";

    public interface Listener {
        void onFileProcessed(File file, int position);
    }

    private Listener listener;

    private DropboxManager dropboxManager;
    private long comicID;
    private int comicPosition;

    public static GetFileFragment newInstance(long comicID, int currentPosition) {
        Bundle bundle = new Bundle();
        bundle.putLong(KEY_COMIC_ID, comicID);
        bundle.putInt(KEY_COMIC_POSITION, currentPosition);

        GetFileFragment fileFragment = new GetFileFragment();
        fileFragment.setArguments(bundle);

        return fileFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Fragment parent = getParentFragment();

        if (activity instanceof Listener) {
            listener = (Listener) activity;
        } else if (parent instanceof Listener) {
            listener = (Listener) parent;
        }

        processArgs(getArguments());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Fragment parent = getParentFragment();

        if (context instanceof Listener) {
            listener = (Listener) context;
        } else if (parent instanceof Listener) {
            listener = (Listener) parent;
        }

        processArgs(getArguments());
    }

    private void processArgs(Bundle arguments) {
        if (arguments == null) {
            throw new NullPointerException("Please use the newInstance method for creating a new worker fragment!");
        }

        this.comicID = arguments.getLong(KEY_COMIC_ID);
        this.comicPosition = arguments.getInt(KEY_COMIC_POSITION);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(false);

        this.dropboxManager = getApplicationComponent().getDropboxManager();

        startTakePhoto();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                Bitmap image = (Bitmap) data.getExtras().get("data");

                dropboxManager.uploadImage(image, comicID, null);

                dropboxManager.getFile(comicID, image, new DropboxManager.FileListener() {
                    @Override
                    public void onImageReceived(File image) {
                        returnImageFile(image);
                    }
                });
            }
        }

    }

    private void startTakePhoto() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, REQUEST_TAKE_PHOTO);
    }

    private void returnImageFile(File file) {
        if (listener != null) {
            listener.onFileProcessed(file, comicPosition);
        }
    }
}
