package fcul.todoapp.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.Date;

import fcul.todoapp.db.TodoContract;
import fcul.todoapp.db.TodoDbHelper;

public class TodoService {
    private final TodoDbHelper dbHelper;
    private String lastSortColumn = null;

    public TodoService(Context context) {
        dbHelper = new TodoDbHelper(context);
    }

    public long create(String name, int priority) {
        ContentValues values = new ContentValues();
        values.put(TodoContract.TodoEntry.COLUMN_NAME_NAME, name);
        values.put(TodoContract.TodoEntry.COLUMN_NAME_CREATED_AT, new Date().getTime());
        values.putNull(TodoContract.TodoEntry.COLUMN_NAME_COMPLETED_AT);
        values.put(TodoContract.TodoEntry.COLUMN_NAME_PRIORITY, priority);

        return dbHelper.getWritableDatabase().insert(
                TodoContract.TodoEntry.TABLE_NAME,
                null,
                values);
    }

    public Cursor fetchAll(String sortColumn) {
        String[] projection = {
                TodoContract.TodoEntry._ID,
                TodoContract.TodoEntry.COLUMN_NAME_NAME,
                TodoContract.TodoEntry.COLUMN_NAME_CREATED_AT,
                TodoContract.TodoEntry.COLUMN_NAME_COMPLETED_AT,
                TodoContract.TodoEntry.COLUMN_NAME_PRIORITY,
        };

        if (sortColumn != null) {
            lastSortColumn = sortColumn;
        }

        return dbHelper.getReadableDatabase().query(
                TodoContract.TodoEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                lastSortColumn);
    }

    public void clear() {
        dbHelper.getWritableDatabase().delete(TodoContract.TodoEntry.TABLE_NAME, null, null);
    }

    public void delete(int id) {
        String selection = TodoContract.TodoEntry._ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(id) };
        dbHelper.getWritableDatabase().delete(TodoContract.TodoEntry.TABLE_NAME, selection, selectionArgs);
    }

    public int update(int id, long time) {
        ContentValues values = new ContentValues();
        values.put(TodoContract.TodoEntry.COLUMN_NAME_COMPLETED_AT, time);

        String selection = TodoContract.TodoEntry._ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(id) };

        return dbHelper.getWritableDatabase().update(
                TodoContract.TodoEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }
}
