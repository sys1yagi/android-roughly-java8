package com.sys1yagi.android_roughly_java8.activities.helpers;

import com.sys1yagi.android_roughly_java8.models.Todo;
import com.sys1yagi.android_roughly_java8.modules.AppModule;
import com.sys1yagi.android_roughly_java8.tools.DateProvider;
import com.sys1yagi.android_roughly_java8.tools.FileManager;
import com.sys1yagi.android_roughly_java8.tools.IdGenerator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import android.support.test.InstrumentationRegistry;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.ObjectGraph;
import dagger.Provides;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TodoHelperTest {

    @Module(injects = TodoHelperTest.class,
            overrides = true,
            includes = AppModule.class)
    class TestModule {

        @Singleton
        @Provides
        public DateProvider provideDateProvider() {
            return mock(DateProvider.class);
        }

        @Singleton
        @Provides
        public IdGenerator provideIdGenerator() {
            return mock(IdGenerator.class);
        }
    }

    @Inject
    FileManager fileManager;

    @Inject
    TodoHelper todoHelper;

    @Inject
    DateProvider dateProvider;

    @Inject
    IdGenerator idGenerator;

    @Before
    public void before() {
        ObjectGraph graph = ObjectGraph.create(new TestModule(),
                new AppModule(InstrumentationRegistry.getTargetContext()));
        graph.inject(this);
    }

    @After
    public void after() {
        fileManager.getFileDirFiles().subscribe(File::delete);
    }

    @Test
    public void addTodo() throws Exception {
        when(dateProvider.now()).thenReturn(1L);
        when(idGenerator.generate()).thenReturn("bbb");

        Todo todo = todoHelper.addTodo("aaa");

        assertThat(todo, notNullValue());
        assertThat(todo.getId(), is("todo_bbb"));
        assertThat(todo.getTitle(), is("aaa"));
        assertThat(todo.getCreatedAt(), is(1L));
        assertThat(todo.getUpdatedAt(), is(1L));

        assertThat(fileManager.getFileDirFiles().count().toBlocking().single(), is(1));
    }

    @Test
    public void loadTodo() throws Exception {
        when(idGenerator.generate()).thenCallRealMethod();

        Todo todo = todoHelper.addTodo("aaa");
        assertThat(todo, notNullValue());

        Todo todo2 = fileManager.loadJsonFromFileDir(todo.getId(), Todo.class);
        assertThat(todo2, notNullValue());
        assertThat(todo2.getTitle(), is("aaa"));
    }

    @Test
    public void listTodo() throws Exception {
        when(idGenerator.generate()).thenCallRealMethod();

        todoHelper.addTodo("aaa");
        todoHelper.addTodo("bbb");
        fileManager.saveJsonToFileDir(new Object(), "test");

        assertThat(fileManager.getFileDirFiles().count().toBlocking().single(), is(3));
        assertThat(todoHelper.list().count().toBlocking().single(), is(2));
    }

}
