package com.sys1yagi.android_roughly_java8.tools;

import com.google.gson.Gson;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class GsonHolder {

    Gson gson;

    @Inject
    public GsonHolder() {
        gson = new Gson();
    }

    public Gson getGson() {
        return gson;
    }
}
