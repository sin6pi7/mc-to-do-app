package fcul.todoapp.db;

import android.provider.BaseColumns;

public class TodoContract {
    private TodoContract() {}

    public static class TodoEntry implements BaseColumns {
        public static final String TABLE_NAME = "todo";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_CREATED_AT = "created_at";
        public static final String COLUMN_NAME_COMPLETED_AT = "completed_at";
        public static final String COLUMN_NAME_PRIORITY = "priority";
    }
}
