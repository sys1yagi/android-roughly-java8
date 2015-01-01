package com.sys1yagi.android_roughly_java8.tools;

import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class IdGenerator {

    @Inject
    public IdGenerator() {
    }

    public String generate() {
        return UUID.randomUUID().toString();
    }
}
