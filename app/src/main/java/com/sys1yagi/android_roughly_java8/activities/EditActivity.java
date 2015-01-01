package com.sys1yagi.android_roughly_java8.activities;

import com.sys1yagi.android_roughly_java8.Application;
import com.sys1yagi.android_roughly_java8.R;
import com.sys1yagi.android_roughly_java8.activities.helpers.TodoHelper;
import com.sys1yagi.android_roughly_java8.models.Todo;
import com.sys1yagi.android_roughly_java8.tools.DateProvider;

import org.parceler.Parcels;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.android.observables.ViewObservable;

public class EditActivity extends ActionBarActivity {

    public static final String ARGS_TODO = "todo";

    @InjectView(R.id.edit_form)
    EditText editText;

    @InjectView(R.id.updated_at)
    TextView updatedAt;

    @InjectView(R.id.edit_finish)
    Button editFinish;

    @Inject
    TodoHelper todoHelper;

    @Inject
    DateProvider dateProvider;

    Todo todo;

    public static Intent createIntent(Context context, Todo todo) {
        Intent intent = new Intent(context, EditActivity.class);
        intent.putExtra(ARGS_TODO, Parcels.wrap(todo));
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        ((Application) getApplication()).inject(this);
        ButterKnife.inject(this);

        todo = Parcels.unwrap(getIntent().getParcelableExtra(ARGS_TODO));

        editText.setText(todo.getTitle());
        updatedAt.setText(
                todoHelper.getReadableDate(this, todo.getUpdatedAt(), R.string.updated_at));

        ViewObservable.text(editText)
                .map(event -> {
                    String text = event.text.toString().trim();
                    return !TextUtils.isEmpty(text);
                })
                .subscribe(editFinish::setEnabled);

        ViewObservable.clicks(editFinish)
                .subscribe(event -> {

                    todo.setTitle(editText.getText().toString());
                    todo.setUpdatedAt(dateProvider.now());
                    todoHelper.updateTodo(todo);

                    finish();
                });
    }

}
