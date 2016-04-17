package com.phc.marvelapp.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.phc.api.impl.network.models.Comic;
import com.phc.marvelapp.R;
import com.phc.marvelapp.ui.adapter.base.SearchableAdapter;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Horatiu on 4/15/2016.
 */
public class ComicsAdapter extends SearchableAdapter<Comic, ComicsAdapter.ComicView> {
    private Picasso picasso;

    public interface ComicTapListener{
        void onComicTapped(Comic comic, int position);
    }

    private ComicTapListener listener;

    public ComicsAdapter() {}

    public void addComic(Comic comic) {
        addItem(comic, true);
    }

    public void addComics(List<Comic> comics) {
        addItems(comics, true);
    }

    public Comic getComic(int position) {
        return getAbsoluteItem(position);
    }

    public void filter(String text) {
        filter(text, null);
    }

    @Override
    public ComicView onCreateViewHolder(ViewGroup parent, int viewType) {
        picasso = Picasso.with(parent.getContext());
        return new ComicView(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comic_book, null));
    }

    @Override
    public void onBindViewHolder(final ComicView holder, final int position) {
        final Comic comic = getItems().get(position);

        if (picasso != null) {
            RequestCreator creator = null;

            if (comic.getImageFile() != null) {
                creator = picasso.load(comic.getImageFile());
            } else {
                creator = picasso.load(comic.getImageURL());
            }

            creator.placeholder(R.drawable.placeholder)
                    .into(holder.comicCover);
        }

//        holder.comicTitle.setText(comic.getComicName());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (listener != null) {
                    listener.onComicTapped(comic, holder.getAdapterPosition());
                }
                return true;
            }
        });
    }

    @Override
    public ArrayList<Comic> filterLogic(List<Comic> originalList, String query) {
        final ArrayList<Comic> filteredList = new ArrayList<>();
        for (Comic comic : originalList) {
            final String text = comic.getComicName().toLowerCase();

            if (text.contains(query)) {
                filteredList.add(comic);
            }
        }

        return filteredList;
    }

    public void setOnComicLongClickListener(ComicTapListener listener) {
        this.listener = listener;
    }

    class ComicView extends RecyclerView.ViewHolder {
        @Bind(R.id.icb_image)
        ImageView comicCover;

        @Bind(R.id.icb_title)
        TextView comicTitle;

        public ComicView(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
