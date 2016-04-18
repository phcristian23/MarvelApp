package com.phc.marvelapp.ui.activities;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.phc.marvelapp.R;
import com.phc.marvelapp.ui.activities.base.BaseActivity;
import com.phc.marvelapp.ui.fragments.ComicsFragment;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Horatiu on 4/15/2016.
 */
public class MainActivity extends BaseActivity{

    private EventBus bus = EventBus.getDefault();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeFragment(new ComicsFragment(), false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final MenuItem searchItem = menu.findItem(R.id.search);
        ComponentName cn = new ComponentName(this, MainActivity.class);

        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(cn));
        searchView.setIconifiedByDefault(true);
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean queryTextFocused) {
                if (!queryTextFocused) {
                    MenuItemCompat.collapseActionView(searchItem);
                }
            }
        });
        return true;
    }

    public static class SearchEvent {
        private String searchString;

        public SearchEvent(String text) {
            this.searchString = text;
        }

        public String getSearchString() {
            return searchString;
        }
    }
}
