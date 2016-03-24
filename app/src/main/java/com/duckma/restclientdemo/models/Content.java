package com.duckma.restclientdemo.models;

import io.realm.RealmObject;

/**
 * Copyright © 2016 DuckMa S.r.l. - http://duckma.com
 * <p/>
 * Created by Matteo Gazzurelli on 12/01/16.
 */
public class Content extends RealmObject {
    String id;
    String title;
    String image;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
