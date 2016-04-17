package com.phc.marvelapp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.phc.api.impl.dropbox.DropboxManager;
import com.phc.api.impl.network.managers.ComicsManager;
import com.phc.api.impl.network.models.Comic;
import com.phc.marvelapp.R;
import com.phc.marvelapp.preferences.MarvelPreferences;
import com.phc.marvelapp.ui.activities.MainActivity;
import com.phc.marvelapp.ui.adapter.ComicsAdapter;
import com.phc.marvelapp.ui.adapter.listener.LoadScrollListener;
import com.phc.marvelapp.ui.fragments.base.BaseFragment;
import com.phc.marvelapp.ui.fragments.worker.GetFileFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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

    private EventBus bus = EventBus.getDefault();

    private String currentSearchCriteria = "";

    @Bind(R.id.fc_refresh_layout)
    SwipyRefreshLayout refreshLayout;

    @Bind(R.id.fc_comic_list)
    RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.comicsManager = getApplicationComponent().getApiManager().getComicsManager();
        this.dropboxManager = getApplicationComponent().getDropboxManager();
        this.preferences = getApplicationComponent().getPreferences();

        this.bus.register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.comicsAdapter = new ComicsAdapter();
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
                loadAndShow(comicsAdapter.getItemCount(), 20, new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                    }
                });
            }
        });

        loadAndShow(0, 20, null);
    }

    private synchronized void loadAndShow(int start, int limit, final Runnable runnable) {
        comicsManager.getComicList(start, limit,
                new Subscriber<List<Comic>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<Comic> comics) {
//                        if (dropboxManager.isLinked()) {
//                            for (final Comic comic : comics) {
//                                if (preferences.comicIDExists(comic.getComicID())) {
//                                    dropboxManager.getImage(comic.getComicID(), new DropboxManager.FileListener() {
//                                        @Override
//                                        public void onImageReceived(File image) {
//                                            comic.setImageFile(image);
//                                        }
//                                    });
//                                }
//                            }
//                        }

                        comicsAdapter.addComics(comics);

                        if (runnable != null) {
                            runnable.run();
                        }
                    }
                }
        );
    }

    @Override
    public void onDestroy() {
        this.bus.unregister(this);

        super.onDestroy();
    }

    @Subscribe
    public void onEvent(MainActivity.SearchEvent event){
        currentSearchCriteria = event.getSearchString();

        this.comicsAdapter.filter(currentSearchCriteria);
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
