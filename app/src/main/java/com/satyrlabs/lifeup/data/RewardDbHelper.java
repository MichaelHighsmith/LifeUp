package com.satyrlabs.lifeup.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.satyrlabs.lifeup.data.RewardsContract.RewardsEntry.COLUMN_REWARD_COST;
import static com.satyrlabs.lifeup.data.RewardsContract.RewardsEntry.COLUMN_REWARD_NAME;
import static com.satyrlabs.lifeup.data.RewardsContract.RewardsEntry.TABLE_NAME;
import static com.satyrlabs.lifeup.data.RewardsContract.RewardsEntry._ID;


public class RewardDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = RewardDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "lifeupRewards.db";

    private static final int DATABASE_VERSION = 1;

    public RewardDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    //Creates our rewards database (This db and the task db are both accessed in TaskProvider since the app only needs one provider)
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_REWARDS_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_REWARD_NAME + " TEXT NOT NULL, "
                + COLUMN_REWARD_COST + " INTEGER NOT NULL);";

        db.execSQL(SQL_CREATE_REWARDS_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}