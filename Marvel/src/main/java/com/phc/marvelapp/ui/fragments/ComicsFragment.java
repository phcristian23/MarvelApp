package com.phc.marvelapp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.phc.api.impl.dropbox.DropboxManager;
import com.phc.api.impl.network.managers.ComicsManager;
import com.phc.api.impl.network.models.Comic;
import com.phc.marvelapp.R;
import com.phc.marvelapp.preferences.MarvelPreferences;
import com.phc.marvelapp.ui.adapter.ComicsAdapter;
import com.phc.marvelapp.ui.adapter.listener.LoadScrollListener;
import com.phc.marvelapp.ui.fragments.base.BaseFragment;
import com.phc.marvelapp.ui.fragments.worker.GetFileFragment;

import java.io.File;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;

/**
 * Created by Horatiu on 4/15/2016.
 */
public class ComicsFragment extends BaseFragment implements GetFileFragment.Listener {
    private static final int SPAN_COUNT = 4;

    private ComicsAdapter comicsAdapter;
    private LoadScrollListener scrollListener;

    private ComicsManager comicsManager;
    private DropboxManager dropboxManager;
    private MarvelPreferences preferences;

    private SearchView searchView;

    @Bind(R.id.fc_refresh_layout)
    SwipyRefreshLayout refreshLayout;

    @Bind(R.id.fc_comic_list)
    RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        this.comicsManager = getApplicationComponent().getApiManager().getComicsManager();
        this.dropboxManager = getApplicationComponent().getDropboxManager();
        this.preferences = getApplicationComponent().getPreferences();

//        this.bus.register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.comicsAdapter = new ComicsAdapter(dropboxManager);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_comics, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), SPAN_COUNT);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(comicsAdapter);

        comicsAdapter.setOnComicLongClickListener(onComicLongTappedListener);

        refreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                loadAndShow(comicsAdapter.getUnfilteredItemCount(), 20, new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                    }
                });
            }
        });

        loadAndShow(0, 20, null);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            String last = "";

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!last.equals(newText)) {
                    last = newText;

                    comicsAdapter.filter(newText);
                }
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    private synchronized void loadAndShow(int start, int limit, final Runnable runnable) {
        if (searchView != null) {
            searchView.setQuery("", true);
        }

        comicsManager.getComicList(start, limit,
                new Subscriber<List<Comic>>() {
                    List<Comic> comicList;

                    @Override
                    public void onCompleted() {
                        comicsAdapter.addComics(comicList);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<Comic> comics) {
                        comicList = comics;

                        for (Comic comic: comicList) {
                            if (preferences.comicIDExists(comic.getComicID())) {
                                comic.setUseCustomFile(true);
                            }
                        }

                        if (runnable != null) {
                            runnable.run();
                        }
                    }
                }
        );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private ComicsAdapter.ComicTapListener onComicLongTappedListener = new ComicsAdapter.ComicTapListener() {
        @Override
        public void onComicTapped(Comic comic, int position) {
        if (isDropboxReady()) {
            startImageActions(comic, position);
        }
        }
    };

    @Override
    public void onFileProcessed(File file, int position) {
        preferences.addComicID(comicsAdapter.getComic(position).getComicID());
        comicsAdapter.getComic(position).setImageFile(file);
        comicsAdapter.notifyItemChanged(position);
    }

    private boolean isDropboxReady() {
        if (dropboxManager.isLinked()) {
            return true;
        }

        dropboxManager.startAuthentication(getContext());
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        String token = dropboxManager.keepUserLoggedIn();

        if (token != null) {
            preferences.setDropboxAuthKey(token);
        }
    }

    public void startImageActions(Comic comic, int currentPosition) {
        String fragmentTag = String.format("ComicWork%s", comic.getComicID());

        FragmentManager manager = getChildFragmentManager();
        GetFileFragment fragment = (GetFileFragment) manager.findFragmentByTag(fragmentTag);

        if(fragment == null) {
            GetFileFragment worker = GetFileFragment.newInstance(comic.getComicID(), currentPosition);
            manager.beginTransaction().add(worker, fragmentTag).commit();
        }

    }
}
