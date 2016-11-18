package fcul.todoapp.db;

import android.content.Context;
import android.database.Cursor;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import fcul.todoapp.R;
import fcul.todoapp.service.TodoService;

public class TodoCursorAdapter extends CursorAdapter {
    private final TodoService todoService;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'at' hh:mm:ss");


    public TodoCursorAdapter(Context context, Cursor cursor, TodoService service) {
        super(context, cursor, 0);
        todoService = service;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.todo_list_item, parent, false);
    }

    @Override
    public void bindView(final View view, Context context, Cursor cursor) {
        String name = cursor.getString(cursor.getColumnIndexOrThrow(TodoContract.TodoEntry.COLUMN_NAME_NAME));
        long createdAt = cursor.getLong(cursor.getColumnIndexOrThrow(TodoContract.TodoEntry.COLUMN_NAME_CREATED_AT));
        boolean isCompleted = !cursor.isNull(cursor.getColumnIndexOrThrow(TodoContract.TodoEntry.COLUMN_NAME_COMPLETED_AT));
        int priority = cursor.getInt(cursor.getColumnIndexOrThrow(TodoContract.TodoEntry.COLUMN_NAME_PRIORITY));
        final int id = cursor.getInt(cursor.getColumnIndexOrThrow(TodoContract.TodoEntry._ID));

        TextView nameView = (TextView) view.findViewById(R.id.todo_name);
        nameView.setText(name);
        TextView createdAtView = (TextView) view.findViewById(R.id.todo_created_at);
        createdAtView.setText("Created at: " + sdf.format(createdAt));
        TextView priorityView = (TextView) view.findViewById(R.id.todo_priority);
        priorityView.setText("Priority: " + priority);
        CheckBox completeCheckBox = (CheckBox) view.findViewById(R.id.todo_complete);

        view.findViewById(R.id.todo_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todoService.delete(id);
                changeCursor(todoService.fetchAll(null));
                notifyDataSetChanged();
                Snackbar.make(v, "Todo deleted.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (isCompleted) {
            long completedAt = cursor.getLong(cursor.getColumnIndexOrThrow(TodoContract.TodoEntry.COLUMN_NAME_COMPLETED_AT));
            TextView completedAtView = (TextView) view.findViewById(R.id.todo_completed_at);
            completedAtView.setText("Completed at: " + sdf.format(completedAt));
            completedAtView.setVisibility(View.VISIBLE);
            completeCheckBox.setChecked(true);
            completeCheckBox.setEnabled(false);
        } else {
            completeCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox ch = (CheckBox) v;
                    boolean isChecked = ch.isChecked();

                    if (!isChecked) {
                        return;
                    }
                    ch.setEnabled(false);

                    int updated = todoService.update(id, new Date().getTime());
                    if (updated > 0) {
                        changeCursor(todoService.fetchAll(null));
                        notifyDataSetChanged();
                        Snackbar.make(v, "Todo completed.", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }
            });
        }
    }
}
