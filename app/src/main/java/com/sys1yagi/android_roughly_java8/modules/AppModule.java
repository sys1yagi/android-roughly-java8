package com.sys1yagi.android_roughly_java8.modules;

import com.sys1yagi.android_roughly_java8.activities.EditActivity;
import com.sys1yagi.android_roughly_java8.activities.MainActivity;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = {
                MainActivity.class,
                EditActivity.class,
        },
        library = true)
public class AppModule {

    Context applicationContext;

    public AppModule(Context context) {
        this.applicationContext = context;
    }

    @Provides
    public Context provideContext() {
        return applicationContext;
    }

}
