package com.phc.marvelapp.ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.phc.marvelapp.ui.activities.base.BaseActivity;
import com.phc.marvelapp.ui.fragments.ComicsFragment;
import com.phc.marvelapp.ui.fragments.worker.GetFileFragment;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

/**
 * Created by Horatiu on 4/15/2016.
 */
public class MainActivity extends BaseActivity  implements GetFileFragment.Listener {

    private EventBus bus = EventBus.getDefault();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            changeFragment(new ComicsFragment(), false);
        }


    }

    @Override
    public void onFileProcessed(File file, int position) {
        bus.post(new ComicFileEvent(file, position));
    }


    public class ComicFileEvent {
        private File file;
        private int position;

        public ComicFileEvent(File file, int position) {
            this.file = file;
            this.position = position;
        }

        public File getFile() {
            return file;
        }

        public int getPosition() {
            return position;
        }
    }
}
