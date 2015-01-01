package com.sys1yagi.android_roughly_java8.views;

import android.content.Context;
import android.widget.ArrayAdapter;

import rx.Observable;

public class ObservableArrayAdapter<T> extends ArrayAdapter<T> {

    public ObservableArrayAdapter(Context context, int resource) {
        super(context, resource);
    }

    public Observable<T> items() {
        return Observable
                .range(0, getCount())
                .map(i -> getItem(i))
                .window(getCount())
                .toBlocking()
                .single();
    }
}
