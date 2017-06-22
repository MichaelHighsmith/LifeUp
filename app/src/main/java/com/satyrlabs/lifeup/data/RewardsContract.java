package com.satyrlabs.lifeup.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by mhigh on 6/22/2017.
 */

public final class RewardsContract {

    private RewardsContract(){}

    public static final String CONTENT_AUTHORITY = "com.satyrlabs.lifeup";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_REWARDS = "rewards";

    public static final class RewardsEntry implements BaseColumns {

        //MIME type for rewards
        public static final String CONTENT_REWARD_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REWARDS;
        //MIME type for a single reward
        public static final String CONTENT_REWARD_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REWARDS;

        //Create the full Uri for the class
        public static final Uri REWARDS_CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_REWARDS);

        public final static String TABLE_NAME = "rewards";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_REWARD_NAME = "name";
        public final static String COLUMN_REWARD_COST = "cost";
    }
}
