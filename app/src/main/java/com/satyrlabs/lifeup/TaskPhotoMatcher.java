package com.satyrlabs.lifeup;

import android.widget.ImageView;


public class TaskPhotoMatcher {

    //When a task is created, check for key words in it to match an image
    public void checkForHiddenIcons(String taskName, ImageView iconImage){

        taskName = taskName.toUpperCase();

        if(taskName.contains("WORKOUT") || taskName.contains("EXERCISE") || taskName.contains("LIFT"))
            iconImage.setImageResource(R.drawable.workout_icon);
        else if(taskName.contains("RUN") || taskName.contains("JOG") || taskName.contains("CARDIO"))
            iconImage.setImageResource(R.drawable.run_icon);
        else if(taskName.contains("READ") || taskName.contains("STUDY") || taskName.contains("WRITE"))
            iconImage.setImageResource(R.drawable.book_icon);
        else if(taskName.contains("DRINK") || taskName.contains("WATER") || taskName.contains("SWIM"))
            iconImage.setImageResource(R.drawable.water_drop);
        else if(taskName.contains("PACK") || taskName.contains("TRAVEL") || taskName.contains("FLY"))
            iconImage.setImageResource(R.drawable.suitcase);
        else if(taskName.contains("GROCERIES") || taskName.contains("FOOD") || taskName.contains("EAT"))
            iconImage.setImageResource(R.drawable.apple);
        else if(taskName.contains("DRIVE") || taskName.contains("CAR") || taskName.contains("MOVE"))
            iconImage.setImageResource(R.drawable.car);
        else if(taskName.contains("SHOP") || taskName.contains("BANK") || taskName.contains("CASH") || taskName.contains("SAVE") || taskName.contains("MONEY") || taskName.contains("BUY"))
            iconImage.setImageResource(R.drawable.money);
        else if(taskName.contains("SLEEP") || taskName.contains("BED") || taskName.contains("WAKE") || taskName.contains("REST") || taskName.contains("MEDITATE"))
            iconImage.setImageResource(R.drawable.zs);
        else
            iconImage.setImageResource(R.drawable.climb_icon);
    }

}
