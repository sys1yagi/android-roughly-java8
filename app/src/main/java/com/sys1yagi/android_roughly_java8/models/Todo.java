package com.sys1yagi.android_roughly_java8.models;

import org.parceler.Parcel;

@Parcel
public class Todo {

    public static final String PREFIX = "todo_";

    String id;

    String title;

    long createdAt;

    long updatedAt;

    boolean checked;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = PREFIX + id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
