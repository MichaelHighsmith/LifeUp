package com.satyrlabs.lifeup;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.satyrlabs.lifeup.data.RewardsContract;

/**
 * Created by mhigh on 6/22/2017.
 */

public class RewardsCursorAdapter extends CursorAdapter {

    public RewardsCursorAdapter(Context context, Cursor c){
        super(context, c, 0);
    }

    //Makes a new blank list item
    public View newView(Context context, Cursor cursor, ViewGroup parent){
        return LayoutInflater.from(context).inflate(R.layout.rewards_item, parent, false);  //TODO Change the layout here
    }

    @Override
    //binds the reward data to the empty list item depending on where the cursor is pointing
    public void bindView(View view, Context context, Cursor cursor){
        TextView nameTextView = (TextView) view.findViewById(R.id.reward_name_text_view);
        TextView costTextView = (TextView) view.findViewById(R.id.reward_gold_cost);

        int nameColumnIndex = cursor.getColumnIndex(RewardsContract.RewardsEntry.COLUMN_REWARD_NAME);
        int costColumnIndex = cursor.getColumnIndex(RewardsContract.RewardsEntry.COLUMN_REWARD_COST);

        String rewardName = cursor.getString(nameColumnIndex);
        String rewardCost = cursor.getString(costColumnIndex);

        nameTextView.setText(rewardName);
        costTextView.setText(rewardCost);
    }
}