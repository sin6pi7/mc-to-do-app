package fcul.todoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import fcul.todoapp.service.TodoService;

public class TodoNewActivity extends AppCompatActivity {
    private TodoService todoService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_new);
        todoService = new TodoService(this);
    }

    public void save(View view) {
        EditText nameEditText = (EditText) findViewById(R.id.todo_name);
        EditText priorityEditText = (EditText) findViewById(R.id.todo_priority);
        todoService.create(nameEditText.getText().toString(), Integer.parseInt(priorityEditText.getText().toString()));
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void cancel(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
