package com.satyrlabs.lifeup;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import static com.satyrlabs.lifeup.data.TaskContract.TaskEntry.COLUMN_TASK_DIFFICULTY;
import static com.satyrlabs.lifeup.data.TaskContract.TaskEntry.COLUMN_TASK_FREQUENCY;
import static com.satyrlabs.lifeup.data.TaskContract.TaskEntry.COLUMN_TASK_NAME;
import static com.satyrlabs.lifeup.data.TaskContract.TaskEntry.CONTENT_URI;
import static com.satyrlabs.lifeup.data.TaskContract.TaskEntry.DIFFICULTY_EASY;
import static com.satyrlabs.lifeup.data.TaskContract.TaskEntry.DIFFICULTY_HARD;
import static com.satyrlabs.lifeup.data.TaskContract.TaskEntry.DIFFICULTY_MEDIUM;
import static com.satyrlabs.lifeup.data.TaskContract.TaskEntry.FREQUENCY_DAILY;
import static com.satyrlabs.lifeup.data.TaskContract.TaskEntry.FREQUENCY_PERIODICALLY;
import static com.satyrlabs.lifeup.data.TaskContract.TaskEntry.FREQUENCY_WEEKLY;

/**
 * Created by mhigh on 6/22/2017.
 */

public class TaskEditorActivity extends AppCompatActivity {

    private EditText mTaskEditText;

    private Spinner mDifficultySpinner;
    private Spinner mFrequencySpinner;

    private int mDifficulty = 0;
    private int mFrequency = 0;

    final String FIRST_TASK = "MyFirstTask";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_editor);

        setTitle(getString(R.string.new_task_title));

        mTaskEditText = (EditText) findViewById(R.id.edit_task_name);
        mDifficultySpinner = (Spinner) findViewById(R.id.spinner_difficulty);
        mFrequencySpinner = (Spinner) findViewById(R.id.spinner_frequency);

        Button addTaskButton = (Button) findViewById(R.id.add_task_button);
        addTaskButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                insertTask();
                finish();
            }
        });

        setupDifficultySpinner();
        setupFrequencySpinner();

        //Check to see if it is the user's first time on this page.  If so then display the guidance dialogues
        SharedPreferences settings = getSharedPreferences(FIRST_TASK, 0);
        if (settings.getBoolean("my_first_task", true)){
            firstTaskDialog();
            settings.edit().putBoolean("my_first_task", false).apply();
        }
    }

    private void setupDifficultySpinner(){
        //Create adapter for spinner
        ArrayAdapter difficultySpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.array_difficulty_options, android.R.layout.simple_spinner_item);

        //Specify the dropdown layout style
        difficultySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        //Apply the adapter to the spinner
        mDifficultySpinner.setAdapter(difficultySpinnerAdapter);

        //Set the integer mSelected to the constant values
        mDifficultySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)){
                    if(selection.equals(getString(R.string.difficulty_hard))){
                        mDifficulty = DIFFICULTY_HARD; //hard
                    } else if (selection.equals(getString(R.string.difficulty_medium))){
                        mDifficulty = DIFFICULTY_MEDIUM; //medium
                    } else {
                        mDifficulty = DIFFICULTY_EASY; //easy
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){
                mDifficulty = DIFFICULTY_EASY;
            }
        });
    }

    private void setupFrequencySpinner(){
        //Create adapter for spinner
        ArrayAdapter frequencySpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.array_frequency_options, android.R.layout.simple_spinner_item);

        //Specify the dropdown layout style
        frequencySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        //Apply the adapter to the spinner
        mFrequencySpinner.setAdapter(frequencySpinnerAdapter);

        //Set the integer mSelected to the constant values
        mFrequencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)){
                    if(selection.equals(getString(R.string.frequency_weekly))){
                        mFrequency = FREQUENCY_WEEKLY; //weekly
                    } else if (selection.equals(getString(R.string.frequency_daily))){
                        mFrequency = FREQUENCY_DAILY; //daily
                    } else {
                        mFrequency = FREQUENCY_PERIODICALLY; //periodically
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){
                mFrequency = FREQUENCY_PERIODICALLY;
            }
        });
    }

    private void insertTask(){

        String taskString = mTaskEditText.getText().toString().trim();

        //If the name is empty then return without saving anything
        if (TextUtils.isEmpty(taskString)){
            return;
        }

        //Create an instance of the Helper class
        //TaskDbHelper mDbHelper = new TaskDbHelper(this);

        //SQLiteDatabase db = mDbHelper.getWritableDatabase();

        //Create a ContentValues object where column names are the keys, and task attributes are the values
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK_NAME, taskString);
        values.put(COLUMN_TASK_DIFFICULTY, mDifficulty); //Difficulty and frequency variables are updated automatically via the spinner
        values.put(COLUMN_TASK_FREQUENCY, mFrequency);

        Uri newUri = getContentResolver().insert(CONTENT_URI, values);

        if (newUri == null){
            Toast.makeText(this, "Error inserting the task", Toast.LENGTH_SHORT).show();
        } else{
            Toast.makeText(this, "Task successfully added!", Toast.LENGTH_SHORT).show();
        }

//        long newRowId = db.insert(TABLE_NAME, null, values);
//
//        if(newRowId == -1){
//            Toast.makeText(this, "Error with saving task", Toast.LENGTH_SHORT).show();
//        } else{
//            Toast.makeText(this, "Task saved with row id: " + newRowId, Toast.LENGTH_SHORT).show();
//        }
    }

    //Dialog box explaining the task editor on the first time around
    public void firstTaskDialog(){
        final Dialog dialog = new Dialog(TaskEditorActivity.this);
        dialog.setContentView(R.layout.intro_task_editor_dialog);
        dialog.setTitle("Make Your First Task");
        dialog.setCancelable(false);

        final Button button = (Button) dialog.findViewById(R.id.intro_task_editor_ok_button);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                dialog.dismiss();
            }
        });

        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;

        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            //Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}