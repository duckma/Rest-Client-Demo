package com.duckma.restclientdemo.network;

import com.duckma.restclientdemo.network.models.ContentResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

/**
 * Copyright © 2016 DuckMa S.r.l. - http://duckma.com
 * <p/>
 * Created by Matteo Gazzurelli on 12/01/16.
 */
public interface AppAPI {
    String FORCE_CACHE_HEADERS =
            "Cache-Control: public, only-if-cached, max-stale=" +
                    Integer.MAX_VALUE;

    @GET("/")
    Call<ContentResponse> loadContents();

    @GET("/")
    @Headers({FORCE_CACHE_HEADERS})
    Call<ContentResponse> loadCachedContents();

}
