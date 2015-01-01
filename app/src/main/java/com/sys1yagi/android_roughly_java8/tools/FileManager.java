package com.sys1yagi.android_roughly_java8.tools;

import org.apache.commons.io.FileUtils;

import android.content.Context;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

@Singleton
public class FileManager {

    Context context;

    @Inject
    GsonHolder gsonHolder;

    @Inject
    public FileManager(Context context) {
        this.context = context;
    }

    public File getFileDir() {
        return context.getFilesDir();
    }

    public File saveJsonToFileDir(Object object, String name) {
        File dir = getFileDir();
        File file = new File(dir, name);

        try {
            FileUtils.writeStringToFile(file, gsonHolder.getGson().toJson(object));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return file;
    }

    public boolean removeFromFileDir(String name) {
        File dir = getFileDir();
        File file = new File(dir, name);
        return file.delete();
    }

    public <T> T loadJsonFromFileDir(String name, Class<T> clazz) {
        File dir = getFileDir();
        File file = new File(dir, name);
        if (!file.exists()) {
            return null;
        }
        try {
            String string = FileUtils.readFileToString(file);
            return gsonHolder.getGson().fromJson(string, clazz);
        } catch (IOException e) {
            return null;
        }
    }

    public Observable<File> getFileDirFiles() {
        return files(getFileDir());
    }

    public Observable<File> files(File f) {
        if (f == null) {
            return Observable.empty();
        }
        if (f.isDirectory()) {
            return Observable.from(f.listFiles()).flatMap(this::files);
        } else {
            return Observable.just(f);
        }
    }
}
