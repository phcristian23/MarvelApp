package com.phc.api.impl.network.managers;

import com.phc.api.impl.network.models.Comic;
import com.phc.api.impl.network.models.response.Comics;
import com.phc.api.impl.network.models.response.Result;
import com.phc.api.impl.network.service.ComicsService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by Horatiu on 4/15/2016.
 */
public class ComicsManager extends BaseManager{
    private static final String IMAGE_TYPE = "portrait_uncanny";
    public static final int IMAGE_WIDTH = 300;
    public static final int IMAGE_HEIGHT = 450;

    private ComicsService service;

    public ComicsManager(ComicsService service) {
        this.service = service;
    }

    public void getComicList(Integer offset, Integer limit, Subscriber<List<Comic>> listener) {
        HashMap<String, String> params = new HashMap<>();
        params.put("offset", offset.toString());
        params.put("limit", limit.toString());

        execute(service.getComics(params).map(mapComic), listener);
    }

    private Func1<Comics, List<Comic>> mapComic = new Func1<Comics, List<Comic>>() {
        @Override
        public List<Comic> call(Comics comics) {
            List<Result> resultList = comics.getData().getResults();
            List<Comic> comicList = new ArrayList<>(resultList.size());

            if (comics.getCode() == HTTP_OK) {
                for (Result result: resultList) {
                    Comic comic = new Comic();

                    comic.setComicID(result.getId());
                    comic.setComicName(result.getTitle().trim());
                    comic.setImageURL(String.format("%s/%s.%s",
                            result.getThumbnail().getPath(),
                            IMAGE_TYPE,
                            result.getThumbnail().getExtension()
                        )
                    );

                    comicList.add(comic);
                }
            }

            return comicList;
        }
    };


}
