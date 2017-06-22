package com.satyrlabs.lifeup;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.satyrlabs.lifeup.data.RewardsContract;

/**
 * Created by mhigh on 6/22/2017.
 */

//The UI that comes up when the user wants to add a new reward
public class RewardsEditorAcitivity extends AppCompatActivity {

    private EditText mRewardEditText;
    private EditText mRewardCostEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rewards_editor);

        setTitle(getString(R.string.new_reward_title));

        mRewardEditText = (EditText) findViewById(R.id.edit_reward_name);
        mRewardCostEditText = (EditText) findViewById(R.id.edit_reward_cost);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.task_editor_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.action_save:
                insertReward();
                finish();
                return true;
            case R.id.action_delete:
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void insertReward(){

        String rewardString = mRewardEditText.getText().toString().trim();

        //Automatically set the gold cost to 10 if the edittext is empty, if not, then set it to what is written by the user.
        String costString = mRewardCostEditText.getText().toString().trim();
        int rewardCost = 10;
        if (!TextUtils.isEmpty(costString)){
            rewardCost = Integer.parseInt(mRewardCostEditText.getText().toString());
        }

        if(TextUtils.isEmpty(rewardString)){
            return;
        }

        ContentValues values = new ContentValues();
        values.put(RewardsContract.RewardsEntry.COLUMN_REWARD_NAME, rewardString);  //TODO add the cost
        values.put(RewardsContract.RewardsEntry.COLUMN_REWARD_COST, rewardCost);

        Uri newUri = getContentResolver().insert(RewardsContract.RewardsEntry.REWARDS_CONTENT_URI, values);

        if (newUri == null){
            Toast.makeText(this, "Error inserting the Reward", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Reward successfully added", Toast.LENGTH_SHORT).show();
        }

    }
}