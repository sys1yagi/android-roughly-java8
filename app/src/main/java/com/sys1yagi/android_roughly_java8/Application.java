package com.sys1yagi.android_roughly_java8;

import com.sys1yagi.android_roughly_java8.modules.AppModule;

import java.util.Arrays;
import java.util.List;

import dagger.ObjectGraph;

public class Application extends android.app.Application {

    private ObjectGraph objectGraph;

    @Override
    public void onCreate() {
        super.onCreate();
        objectGraph = ObjectGraph.create(getModules().toArray());
    }

    protected List getModules() {
        return Arrays.asList(
                new AppModule(this)
        );
    }

    public void inject(Object object) {
        objectGraph.inject(object);
    }
}
