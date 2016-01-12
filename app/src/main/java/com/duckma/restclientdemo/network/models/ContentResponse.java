package com.duckma.restclientdemo.network.models;

import com.duckma.restclientdemo.models.Content;

import java.util.List;

/**
 * Copyright © 2016 DuckMa S.r.l. - http://duckma.com
 * <p/>
 * Created by Matteo Gazzurelli on 12/01/16.
 */
public class ContentResponse {
    String title;
    String type;
    int version;
    List<Content> contents;
}
