package com.sys1yagi.android_roughly_java8.views;

import org.junit.Test;

import android.support.test.InstrumentationRegistry;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import rx.Observable;

public class ObservableArrayAdapterTest {

    @Test
    public void testItems() throws Exception {
        ObservableArrayAdapter<String> adapter = new ObservableArrayAdapter<>(
                InstrumentationRegistry.getContext(), -1);

        Observable.range(0, 10)
                .map(i -> "test")
                .subscribe(adapter::add);

        adapter.items().subscribe(adapter::remove);
        assertThat(adapter.getCount(), is(0));
    }
}
