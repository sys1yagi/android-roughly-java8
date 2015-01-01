package com.sys1yagi.android_roughly_java8.tools;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DateProvider {

    @Inject
    public DateProvider() {
    }

    public long now() {
        return System.currentTimeMillis();
    }
}
