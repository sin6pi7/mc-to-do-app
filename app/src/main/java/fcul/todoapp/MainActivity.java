package fcul.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import fcul.todoapp.db.TodoContract;
import fcul.todoapp.db.TodoCursorAdapter;
import fcul.todoapp.service.TodoService;

public class MainActivity extends AppCompatActivity {

    private Spinner sortTodosSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final TodoService todoService = new TodoService(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), TodoNewActivity.class);
                startActivity(intent);
            }
        });

        final TodoCursorAdapter todoCursorAdapter = new TodoCursorAdapter(this, todoService.fetchAll(null), todoService);
        ListView listView = (ListView) findViewById(R.id.todo_list_view);
        listView.setAdapter(todoCursorAdapter);

        String[] sortOptions = {
                "Name",
                "Date of completion",
                "Date of creation",
                "Priority"
        };
        final String[] sortColumns = {
                TodoContract.TodoEntry.COLUMN_NAME_NAME,
                TodoContract.TodoEntry.COLUMN_NAME_COMPLETED_AT,
                TodoContract.TodoEntry.COLUMN_NAME_CREATED_AT,
                TodoContract.TodoEntry.COLUMN_NAME_PRIORITY
        };
        sortTodosSpinner = (Spinner) findViewById(R.id.sort_todos_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sortOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortTodosSpinner.setAdapter(adapter);
        sortTodosSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                todoCursorAdapter.changeCursor(todoService.fetchAll(sortColumns[(int) id]));
                todoCursorAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
