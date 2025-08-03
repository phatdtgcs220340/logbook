package com.example.todolist.ui;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.todolist.*;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText editTextTask;
    Button btnAdd;
    ListView listViewTasks;

    TaskDao taskDao;
    ArrayAdapter<String> adapter;
    List<Task> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextTask = findViewById(R.id.editTextTask);
        btnAdd = findViewById(R.id.btnAdd);
        listViewTasks = findViewById(R.id.listViewTasks);

        taskDao = new TaskDao(this);

        loadTasks();

        btnAdd.setOnClickListener(view -> {
            String taskName = editTextTask.getText().toString().trim();
            if (!taskName.isEmpty()) {
                taskDao.addTask(taskName);
                editTextTask.setText("");
                loadTasks();
                Toast.makeText(this, "Task added", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Enter task name", Toast.LENGTH_SHORT).show();
            }
        });

        listViewTasks.setOnItemClickListener((parent, view, position, id) -> {
            Task task = taskList.get(position); // get selected task

            // Create AlertDialog with EditText
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Edit Task Name");

            final EditText input = new EditText(MainActivity.this);
            input.setText(task.getName());
            builder.setView(input);

            // Save button
            builder.setPositiveButton("Save", (dialog, which) -> {
                String newName = input.getText().toString().trim();
                if (!newName.isEmpty()) {
                    task.setName(newName);               // update model
                    taskDao.updateTask(task);            // update DB
                    loadTasks();                         // refresh UI
                    Toast.makeText(this, "Task updated", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Task name cannot be empty", Toast.LENGTH_SHORT).show();
                }
            });

            // Cancel button
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

            builder.show(); // Show dialog
        });

        listViewTasks.setOnItemLongClickListener((parent, view, position, id) -> {
            Task task = taskList.get(position);
            taskDao.deleteTask(task.getId());
            loadTasks();
            Toast.makeText(this, "Task deleted", Toast.LENGTH_SHORT).show();
            return true;
        });
    }

    private void loadTasks() {
        taskList = taskDao.getAllTasks();
        List<String> taskNames = new ArrayList<>();
        for (Task task : taskList) {
            String display = task.getName();
            taskNames.add(display);
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, taskNames);
        listViewTasks.setAdapter(adapter);
    }
}
