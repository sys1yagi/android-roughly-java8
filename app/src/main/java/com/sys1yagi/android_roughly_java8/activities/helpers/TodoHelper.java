package com.sys1yagi.android_roughly_java8.activities.helpers;

import com.sys1yagi.android_roughly_java8.R;
import com.sys1yagi.android_roughly_java8.models.Todo;
import com.sys1yagi.android_roughly_java8.tools.DateProvider;
import com.sys1yagi.android_roughly_java8.tools.FileManager;
import com.sys1yagi.android_roughly_java8.tools.IdGenerator;

import org.joda.time.DateTime;

import android.content.Context;
import android.support.annotation.StringRes;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

@Singleton
public class TodoHelper {

    @Inject
    FileManager fileManager;

    @Inject
    DateProvider dateProvider;

    @Inject
    IdGenerator idGenerator;

    @Inject
    public TodoHelper() {
    }

    public Todo addTodo(String title) {
        Todo todo = new Todo();

        todo.setId(idGenerator.generate());
        todo.setTitle(title);
        todo.setCreatedAt(dateProvider.now());
        todo.setUpdatedAt(dateProvider.now());
        todo.setChecked(false);

        File file = fileManager.saveJsonToFileDir(todo, todo.getId());
        if (file != null) {
            return todo;
        }
        return null;
    }

    public void updateTodo(Todo todo) {
        fileManager.saveJsonToFileDir(todo, todo.getId());
    }

    public boolean removeTodo(Todo todo) {
        return fileManager.removeFromFileDir(todo.getId());
    }

    public Observable<Todo> list() {
        return fileManager
                .getFileDirFiles()
                .filter(file -> file.getName().startsWith(Todo.PREFIX))
                .map(file -> fileManager.loadJsonFromFileDir(file.getName(), Todo.class));
    }

    public int compare(Todo lhs, Todo rhs) {
        long l = lhs.getUpdatedAt();
        long r = rhs.getUpdatedAt();
        return -(l == r ? 0 : l < r ? -1 : 1);
    }

    public String getReadableDate(Context context, long time, @StringRes int resId) {
        String date = new DateTime(time).toString(context.getString(R.string.date));
        return context.getString(resId, date);
    }
}
