package com.satyrlabs.lifeup;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;


public class ExpToLevel extends AppCompatActivity{

    int currentLevel;
    int maxExperience;
    int experience;

    TextView experience_int, experience_max_int, level_int;

    //Called in Main Activity onCreate()
    public ExpToLevel(TextView experience_int, TextView experience_max_int, TextView level_int, int experience){

        this.experience = experience;
        this.experience_int = experience_int;
        this.experience_max_int = experience_max_int;
        this.level_int = level_int;

        setTextViews(experience);

    }

    public void setTextViews(int experience){
        if (0 <= experience && experience <= 10){
            currentLevel = 1;
            level_int.setText(String.valueOf(currentLevel));
            maxExperience = 10;
        } else if (10 < experience && experience <= 100 && currentLevel != 2){
            currentLevel = 2;
            level_int.setText(String.valueOf(currentLevel));
            maxExperience = 100;
        } else if (100 < experience  && experience <= 200 && currentLevel != 3){
            currentLevel = 3;
            level_int.setText(String.valueOf(currentLevel));
            maxExperience = 200;
        } else if (200 < experience && experience <= 350 && currentLevel != 4){
            currentLevel = 4;
            level_int.setText(String.valueOf(currentLevel));
            maxExperience = 350;
        } else if (350 < experience && experience <= 500 && currentLevel != 5){
            currentLevel = 5;
            level_int.setText(String.valueOf(currentLevel));
            maxExperience = 500;
        } else if (500 < experience && experience <= 750 && currentLevel != 6){
            currentLevel = 6;
            level_int.setText(String.valueOf(currentLevel));
            maxExperience = 750;
        } else if (750 < experience && experience <= 950 && currentLevel != 7){
            currentLevel = 7;
            level_int.setText(String.valueOf(currentLevel));
            maxExperience = 950;
        } else if (950 < experience && experience <= 1200 && currentLevel != 8){
            currentLevel = 8;
            level_int.setText(String.valueOf(currentLevel));
            maxExperience = 1200;
        } else if (1200 < experience && experience <= 1500 && currentLevel != 9){
            currentLevel = 9;
            level_int.setText(String.valueOf(currentLevel));
            maxExperience = 1500;
        } else if (1500 < experience){
            //continue indefinite leveling
            int experienceLevelFactor = experience / 250;
            if(currentLevel != experienceLevelFactor + 4){
                currentLevel = experienceLevelFactor + 4;
                maxExperience = (currentLevel * 250) - 750;
                level_int.setText(String.valueOf(currentLevel));
            }
        }
        experience_int.setText(String.valueOf(experience));
        experience_max_int.setText(String.valueOf(maxExperience));
    }

    public void addExperience(int boost){
        experience += boost;
        setTextViews(experience);
    }

    public int[] getExperienceValues(){
        int[] experienceValues = new int[3];
        experienceValues[0] = experience;
        experienceValues[1] = maxExperience;
        experienceValues[2] = currentLevel;

        return experienceValues;
    }

}
