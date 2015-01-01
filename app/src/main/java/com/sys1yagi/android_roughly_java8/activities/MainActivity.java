package com.sys1yagi.android_roughly_java8.activities;

import com.sys1yagi.android_roughly_java8.Application;
import com.sys1yagi.android_roughly_java8.R;
import com.sys1yagi.android_roughly_java8.activities.helpers.TodoHelper;
import com.sys1yagi.android_roughly_java8.models.Todo;
import com.sys1yagi.android_roughly_java8.views.TodoListAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.android.observables.AndroidObservable;
import rx.android.observables.ViewObservable;
import rx.schedulers.Schedulers;

public class MainActivity extends ActionBarActivity {

    public static final int REQUEST_CODE_EDIT = 100;

    @InjectView(R.id.list_view)
    ListView listView;

    @InjectView(R.id.add_form)
    EditText editText;

    @InjectView(R.id.add_button)
    Button addButton;

    @InjectView(R.id.clear_button)
    Button clearButton;

    @Inject
    TodoListAdapter adapter;

    @Inject
    TodoHelper todoHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((Application) getApplication()).inject(this);
        ButterKnife.inject(this);

        listView.setAdapter(adapter);

        addButton.setEnabled(false);

        ViewObservable.text(editText)
                .map(event -> {
                    String text = event.text.toString().trim();
                    return !TextUtils.isEmpty(text);
                })
                .subscribe(addButton::setEnabled);

        ViewObservable.clicks(addButton)
                .subscribe(event -> {
                    Todo todo = todoHelper.addTodo(editText.getText().toString());
                    if (todo != null) {
                        adapter.add(todo);
                        adapter.sort(todoHelper::compare);
                        editText.setText("");
                    }
                });

        ViewObservable.clicks(clearButton)
                .subscribe(event -> {
                    adapter.items()
                            .filter(Todo::isChecked)
                            .filter(todoHelper::removeTodo)
                            .subscribe(adapter::remove);
                });
        listView.setOnItemClickListener(
                (parent, view, position, id) -> {
                    Todo todo = adapter.getItem(position);
                    editTodo(todo);
                });

        initData();
    }

    private void initData() {
        adapter.clear();
        AndroidObservable.bindActivity(this,
                todoHelper
                        .list()
                        .subscribeOn(Schedulers.newThread()))
                .doOnCompleted(() -> adapter.sort(todoHelper::compare))
                .subscribe(adapter::add);
    }

    private void editTodo(Todo todo) {
        Intent intent = EditActivity.createIntent(this, todo);
        startActivityForResult(intent, REQUEST_CODE_EDIT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_EDIT) {
            initData();
        }
    }
}
