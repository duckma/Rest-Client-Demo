package com.duckma.restclientdemo.network;

import com.duckma.restclientdemo.Config;
import com.duckma.restclientdemo.network.models.ContentResponse;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Copyright © 2016 DuckMa S.r.l. - http://duckma.com
 * <p/>
 * Created by Matteo Gazzurelli on 12/01/16.
 */
public interface AppAPI {

    @GET(Config.ENDPOINT)
    Call<ContentResponse> loadContents();

}
