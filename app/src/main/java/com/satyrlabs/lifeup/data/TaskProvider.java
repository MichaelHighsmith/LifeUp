package com.satyrlabs.lifeup.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import static com.satyrlabs.lifeup.data.TaskContract.TaskEntry.COLUMN_TASK_NAME;
import static com.satyrlabs.lifeup.data.TaskContract.TaskEntry.CONTENT_ITEM_TYPE;
import static com.satyrlabs.lifeup.data.TaskContract.TaskEntry.CONTEN_LIST_TYPE;
import static com.satyrlabs.lifeup.data.TaskContract.TaskEntry.TABLE_NAME;
import static com.satyrlabs.lifeup.data.TaskContract.TaskEntry._ID;


public class TaskProvider extends ContentProvider {

    private static final int TASKS = 100;
    private static final int TASK_ID = 101;
    private static final int REWARDS = 200;
    private static final int REWARD_ID = 201;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(TaskContract.CONTENT_AUTHORITY, TaskContract.PATH_TASKS, TASKS);
        sUriMatcher.addURI(TaskContract.CONTENT_AUTHORITY, TaskContract.PATH_TASKS + "/#", TASK_ID);
        sUriMatcher.addURI(RewardsContract.CONTENT_AUTHORITY, RewardsContract.PATH_REWARDS, REWARDS);
        sUriMatcher.addURI(RewardsContract.CONTENT_AUTHORITY, RewardsContract.PATH_REWARDS + "/#", REWARD_ID);
    }


    private TaskDbHelper mDbHelper;
    private RewardDbHelper rewardDbHelper;

    @Override
    public boolean onCreate(){
        mDbHelper = new TaskDbHelper(getContext());
        rewardDbHelper = new RewardDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder){
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        SQLiteDatabase rewardsDatabase = rewardDbHelper.getReadableDatabase();

        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match){
            case TASKS:
                cursor = database.query(TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case TASK_ID:
                selection = _ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri))};  //gives the id of the row being selected

                cursor = database.query(TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case REWARDS:
                cursor = rewardsDatabase.query(RewardsContract.RewardsEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case REWARD_ID:
                selection = RewardsContract.RewardsEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri))};

                cursor = rewardsDatabase.query(RewardsContract.RewardsEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;

    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case TASKS:
                return CONTEN_LIST_TYPE;
            case TASK_ID:
                return CONTENT_ITEM_TYPE;
            case REWARDS:
                return RewardsContract.RewardsEntry.CONTENT_REWARD_LIST_TYPE;
            case REWARD_ID:
                return RewardsContract.RewardsEntry.CONTENT_REWARD_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match" + match);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues){
        final int match = sUriMatcher.match(uri);
        switch (match){
            case TASKS:
                return insertTask(uri, contentValues);
            case REWARDS:
                return insertReward(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertTask(Uri uri, ContentValues values){
        //TODO insert a new task
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        //insert the new task with given values
        long id = database.insert(TABLE_NAME, null, values);

        //test if the insertion failed
        if(id == -1){
            return null;
        }

        //Notify listeners that the data has changed for the task content URI
        getContext().getContentResolver().notifyChange(uri, null);

        //add the row id to the end of the task URI to create a task URI specific for this task
        return ContentUris.withAppendedId(uri, id);
    }

    private Uri insertReward(Uri uri, ContentValues values){
        SQLiteDatabase rewardsDatabase = rewardDbHelper.getWritableDatabase();

        long id = rewardsDatabase.insert(RewardsContract.RewardsEntry.TABLE_NAME, null, values);

        if(id == -1){
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs){
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        SQLiteDatabase rewardsDatabase = rewardDbHelper.getWritableDatabase();

        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match){
            case TASKS:
                rowsDeleted = database.delete(TABLE_NAME, selection, selectionArgs);
                break;
            case TASK_ID:
                selection = _ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(TABLE_NAME, selection, selectionArgs);
                break;
            case REWARDS:
                rowsDeleted = rewardsDatabase.delete(RewardsContract.RewardsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case REWARD_ID:
                selection = RewardsContract.RewardsEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = rewardsDatabase.delete(RewardsContract.RewardsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("You cannot delete " + uri);
        }

        //If any rows were deleted, then notify all listeners that the data at the given URI has changed
        if (rowsDeleted != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs){
        final int match = sUriMatcher.match(uri);
        switch (match){
            case TASKS:
                return updateTask(uri, contentValues, selection, selectionArgs);
            case TASK_ID:
                selection = _ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri))};
                return updateTask(uri, contentValues, selection, selectionArgs);
            case REWARDS:
                return updateReward(uri, contentValues, selection, selectionArgs);
            case REWARD_ID:
                selection = RewardsContract.RewardsEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri))};
                return updateReward(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateTask(Uri uri, ContentValues values, String selection, String[] selectionArgs){

        if (values.containsKey(COLUMN_TASK_NAME)){
            String name = values.getAsString(COLUMN_TASK_NAME);
            if(name == null){
                throw new IllegalArgumentException("Task requires a name");
            }
        }

        //if there are not values to update then dont update the database
        if (values.size() == 0){
            return 0;
        }

        //get writeable database to update the data and then return the number of database rows affected by the update statement
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        //Perform the update and get the number of rows affected
        int rowsUpdated = database.update(TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdated != 0){
            //Notify all listeners that the data has changed
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    private int updateReward(Uri uri, ContentValues values, String selection, String[] selectionArgs){

        if (values.containsKey(RewardsContract.RewardsEntry.COLUMN_REWARD_NAME)){
            String name = values.getAsString(RewardsContract.RewardsEntry.COLUMN_REWARD_NAME);
            if(name == null){
                throw new IllegalArgumentException("Task requires a name");
            }
        }

        if (values.size() == 0){
            return 0;
        }

        SQLiteDatabase rewardsDatabase = rewardDbHelper.getWritableDatabase();

        int rowsUpdated = rewardsDatabase.update(RewardsContract.RewardsEntry.TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdated != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return  rowsUpdated;
    }

}