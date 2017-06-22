package com.satyrlabs.lifeup.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.satyrlabs.lifeup.data.TaskContract.TaskEntry.COLUMN_TASK_DIFFICULTY;
import static com.satyrlabs.lifeup.data.TaskContract.TaskEntry.COLUMN_TASK_FREQUENCY;
import static com.satyrlabs.lifeup.data.TaskContract.TaskEntry.COLUMN_TASK_NAME;
import static com.satyrlabs.lifeup.data.TaskContract.TaskEntry.TABLE_NAME;
import static com.satyrlabs.lifeup.data.TaskContract.TaskEntry._ID;

/**
 * Created by mhigh on 6/22/2017.
 */

public class TaskDbHelper extends SQLiteOpenHelper {


    public static final String LOG_TAG = TaskDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "lifeup.db";

    private static final int DATABASE_VERSION = 1;

    public TaskDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String SQL_CREATE_TASKS_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_TASK_NAME + " TEXT NOT NULL, "
                + COLUMN_TASK_DIFFICULTY + " INTEGER NOT NULL, "
                + COLUMN_TASK_FREQUENCY + " INTEGER NOT NULL);";

        db.execSQL(SQL_CREATE_TASKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        //The database is still at version 1, so there's nothing to be done here yet
    }

}
