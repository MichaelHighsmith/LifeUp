package com.satyrlabs.lifeup.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by mhigh on 6/22/2017.
 */

public final class TaskContract {

    private TaskContract(){}

    //Sets a string constant for the authority we wrote in the manifest
    public static final String CONTENT_AUTHORITY = "com.satyrlabs.lifeup";

    //takes in a URI string and returns a Uri.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_TASKS = "tasks";



    public static final class TaskEntry implements BaseColumns {

        //MIME type for a list of tasks
        public static final String CONTEN_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TASKS;

        //MIME type for a single task
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TASKS;

        //Create the full URI for the class
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_TASKS);

        public final static String TABLE_NAME = "tasks";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_TASK_NAME = "name";
        public final static String COLUMN_TASK_DIFFICULTY = "difficulty";
        public final static String COLUMN_TASK_FREQUENCY = "frequency";

        public static final int DIFFICULTY_EASY = 0;
        public static final int DIFFICULTY_MEDIUM = 1;
        public static final int DIFFICULTY_HARD = 2;

        public static final int FREQUENCY_PERIODICALLY = 0;
        public static final int FREQUENCY_DAILY = 1;
        public static final int FREQUENCY_WEEKLY = 2;
    }

}