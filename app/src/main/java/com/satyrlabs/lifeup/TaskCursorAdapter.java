package com.satyrlabs.lifeup;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import static com.satyrlabs.lifeup.data.TaskContract.TaskEntry.COLUMN_TASK_NAME;

/**
 * Created by mhigh on 6/22/2017.
 */

public class TaskCursorAdapter extends CursorAdapter {

    ImageView iconImage;


    public TaskCursorAdapter(Context context, Cursor c){
        super(context, c, 0);
    }

    @Override
    //Makes a new blank list item view (no data is bound here yet)
    public View newView(Context context, Cursor cursor, ViewGroup parent){
        return LayoutInflater.from(context).inflate(R.layout.task_item, parent, false);
    }

    @Override
    //binds the task data in the current row pointed to by cursor to the given task_item layout
    public void bindView(View view, final Context context, final Cursor cursor){
        //Find the view we modify in task_item xml
        TextView nameTextView = (TextView) view.findViewById(R.id.name_text_view);

        iconImage = (ImageView) view.findViewById(R.id.task_image_icon);

        //Find the column of the task attribute (task) I'm interested in
        int nameColumnIndex = cursor.getColumnIndex(COLUMN_TASK_NAME);

        //Read the task attributes from the Cursor for the current task
        String taskName = cursor.getString(nameColumnIndex);

        checkForHiddenIcons(taskName);

        //Update the textview with the nes attribute
        nameTextView.setText(taskName);


    }

    //Checks a variety of possible text inputs and if it matches the users input then displays an icon in the task.  Because why not have some fun surprises?!
    public void checkForHiddenIcons(String taskName){
        if (taskName.contains("Workout") || taskName.contains("Exercise") || taskName.contains("Lift") ||
                taskName.contains("workout") || taskName.contains("exercise") || taskName.contains("lift")){
            iconImage.setImageResource(R.drawable.workout_icon);
        } else if (taskName.contains("Run") || taskName.contains("Jog") || taskName.contains("Cardio") ||
                taskName.contains("run") || taskName.contains("jog") || taskName.contains("cardio")){
            iconImage.setImageResource(R.drawable.run_icon);
        } else if (taskName.contains("Read") || taskName.contains("Study") || taskName.contains("Write") ||
                taskName.contains("read") || taskName.contains("study") || taskName.contains("write")){
            iconImage.setImageResource(R.drawable.book_icon);
        } else if (taskName.contains("Drink") || taskName.contains("drink") || taskName.contains("Water") || taskName.contains("water") ||
                taskName.contains("Swim") || taskName.contains("swim")){
            iconImage.setImageResource(R.drawable.water_drop);
        } else{
            iconImage.setImageResource(R.drawable.climb_icon);
        }

    }
}