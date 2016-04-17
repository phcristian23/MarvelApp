package com.phc.api.impl.dropbox;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.ProgressListener;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AppKeyPair;
import com.phc.api.BuildConfig;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Horatiu on 4/17/2016.
 */
public class DropboxManager {
    private DropboxAPI<AndroidAuthSession> dbApi;
    private AndroidAuthSession session;

    public interface FileListener{
        void onImageReceived(File image);
    }

    public interface OnCompletionListener{
        void uploadComplete();
    }

    public static DropboxManager instance() {
        return new DropboxManager();
    }

    private DropboxManager() {
        AppKeyPair appKeys = new AppKeyPair(BuildConfig.DPUBLIC_KEY, BuildConfig.DPRIVATE_KEY);
        session = new AndroidAuthSession(appKeys);
        dbApi = new DropboxAPI<AndroidAuthSession>(session);
    }

    public DropboxManager setToken(String token) {
        if (token == null) return this;
        this.dbApi.getSession().setOAuth2AccessToken(token);
        return this;
    }

    public void startAuthentication(Context context) {
        dbApi.getSession().startOAuth2Authentication(context);
    }

    public boolean isAuthenticated() {
        return dbApi.getSession().authenticationSuccessful();
    }

    public boolean isLinked() {
        return dbApi.getSession().isLinked();
    }

    public String keepUserLoggedIn() {
        if (dbApi.getSession().authenticationSuccessful()) {
            try {
                dbApi.getSession().finishAuthentication();

                return dbApi.getSession().getOAuth2AccessToken();
            } catch (IllegalStateException e) {
                Log.i("DbAuthLog", "Error authenticating", e);
            }
        }

        return null;
    }

    public void uploadImage(final Bitmap image, final long comicID, final OnCompletionListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                InputStream is = new ByteArrayInputStream(byteArray);

                try {
                    dbApi.putFileOverwrite(String.format("/%s.png", comicID), is, byteArray.length, new ProgressListener() {
                                @Override
                                public void onProgress(long l, long l1) {
                                    Log.e("Tag", l + "\n" + l1);
                                   if (listener != null) {
                                       listener.uploadComplete();
                                   }
                                }
                            });
                } catch (DropboxException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void getImage(final long comicID, final FileListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DropboxAPI.DropboxInputStream stream = dbApi.getFileStream(String.format("/%s.png", comicID), null);

                    File file = null;
                    FileOutputStream out = null;
                    try {
                        file = File.createTempFile("temp" + comicID, ".png");
                        file.deleteOnExit();
                        out = new FileOutputStream(file);
                        IOUtils.copy(stream, out);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (stream != null) {
                                stream.close();
                            }

                            if (out != null) {
                                out.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if(listener != null) {
                            listener.onImageReceived(file);
                        }
                    }
                } catch (DropboxException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public boolean checkIfFileExists(long comicID) {
        boolean exists = false;
        try {
            DropboxAPI.Entry entry = dbApi.metadata(String.format("/%s.png", comicID), 1, null, false, null);
            exists = entry != null;
        } catch (DropboxException e) {
            exists = false;
            e.printStackTrace();
        }

        return exists;
    }

    public void getFile(long comicID, Bitmap bitmap, FileListener listener) {
        File file = null;
        FileOutputStream out = null;
        try {
            file = File.createTempFile("temp" + comicID, ".png");
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (listener != null) {
                listener.onImageReceived(file);
            }
        }
    }

}
