package com.satyrlabs.lifeup.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.satyrlabs.lifeup.R;
import com.satyrlabs.lifeup.stats.TaskPhotoMatcher;

import static com.satyrlabs.lifeup.data.TaskContract.TaskEntry.COLUMN_TASK_NAME;


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

        TaskPhotoMatcher photoMatcher = new TaskPhotoMatcher();
        photoMatcher.checkForHiddenIcons(taskName, iconImage);

        //Update the textview with the nes attribute
        nameTextView.setText(taskName);

    }
}