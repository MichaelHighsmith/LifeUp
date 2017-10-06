package com.satyrlabs.lifeup.stats;

import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;


public class GoldManager extends AppCompatActivity {

    TextView gold_int;
    int currentGold;

    public GoldManager(TextView gold_int, int currentGold){
        this.gold_int = gold_int;
        this.currentGold = currentGold;

        setTextViews(currentGold);
    }

    public void setTextViews(int currentGold){
        gold_int.setText(String.valueOf(currentGold));
    }

    public int getCurrentGold(){
        return currentGold;
    }

    public int addGold(int boost){
        currentGold = currentGold + boost/2 + 25;
        setTextViews(currentGold);
        return currentGold;
    }

    public int spendReward(int currentCost) {
        if(currentGold < currentCost){
            //not enough gold: make toast saying so in MainActivity.onRewardSelected
            throw new RuntimeException();
        } else {
            currentGold = currentGold - currentCost;
        }
        setTextViews(currentGold);
        return currentGold;
    }


}
