package com.sys1yagi.android_roughly_java8.views;

import com.sys1yagi.android_roughly_java8.R;
import com.sys1yagi.android_roughly_java8.activities.helpers.TodoHelper;
import com.sys1yagi.android_roughly_java8.models.Todo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class TodoListAdapter extends ObservableArrayAdapter<Todo> {

    @Inject
    TodoHelper todoHelper;

    @Inject
    public TodoListAdapter(Context context) {
        super(context, -1);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(getContext(), R.layout.listitem_todo, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        Todo todo = getItem(position);

        holder.todoChecked.setOnCheckedChangeListener((view, isChecked) -> {
            todo.setChecked(isChecked);
        });
        holder.todoChecked.setChecked(todo.isChecked());
        holder.todoTitle.setText(todo.getTitle());

        holder.updatedAt.setText(
                todoHelper.getReadableDate(getContext(), todo.getUpdatedAt(), R.string.updated_at));

        return convertView;
    }

    static class ViewHolder {

        @InjectView(R.id.todo_checked)
        CheckBox todoChecked;

        @InjectView(R.id.todo_title)
        TextView todoTitle;

        @InjectView(R.id.updated_at)
        TextView updatedAt;

        ViewHolder(View root) {
            ButterKnife.inject(this, root);
        }
    }

}
