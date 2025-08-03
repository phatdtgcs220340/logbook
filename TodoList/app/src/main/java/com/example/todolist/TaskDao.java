package com.example.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class TaskDao {

    private TaskDbHelper dbHelper;

    public TaskDao(Context context) {
        dbHelper = new TaskDbHelper(context);
    }

    public void addTask(String name) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TaskDbHelper.COL_NAME, name);
        db.insert(TaskDbHelper.TABLE_NAME, null, values);
        db.close();
    }

    public List<Task> getAllTasks() {
        List<Task> taskList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TaskDbHelper.TABLE_NAME,
                null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setId(cursor.getInt(cursor.getColumnIndexOrThrow(TaskDbHelper.COL_ID)));
                task.setName(cursor.getString(cursor.getColumnIndexOrThrow(TaskDbHelper.COL_NAME)));
                taskList.add(task);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return taskList;
    }

    public void updateTask(Task task) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TaskDbHelper.COL_NAME, task.getName());
        db.update(TaskDbHelper.TABLE_NAME, values, "id=?", new String[]{String.valueOf(task.getId())});
        db.close();
    }

    public void deleteTask(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TaskDbHelper.TABLE_NAME, "id=?", new String[]{String.valueOf(id)});
        db.close();
    }
}
