package com.satyrlabs.lifeup;

import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.support.design.widget.FloatingActionButton;

import static com.satyrlabs.lifeup.data.TaskContract.TaskEntry.COLUMN_TASK_DIFFICULTY;
import static com.satyrlabs.lifeup.data.TaskContract.TaskEntry.COLUMN_TASK_NAME;
import static com.satyrlabs.lifeup.data.TaskContract.TaskEntry.CONTENT_URI;
import static com.satyrlabs.lifeup.data.TaskContract.TaskEntry._ID;

/**
 * Created by mhigh on 6/22/2017.
 */

public class FirstFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int TASK_LOADER = 0;

    //mCallback gives us allows us to attach this fragment to an Activity (MainActivity) since Fragments can't directly interact outside of themselves.
    //this declaration works with the onAttach method and then mCallback is called in the onClickListener thereby updating the experience view in MainActivity.  Radical!!
    OnHeadlineSelectedListener mCallback;

    TaskCursorAdapter mCursorAdapter;

    View v;

    //Container Activity needs to implement this interface
    public interface OnHeadlineSelectedListener{
        public void onTaskSelected(int position, long id);
    }

    //This allows the method to deliver messages to the context by calling onTaskSelected() method using the mCallback instance of OnHEadlineSelectedListener interface
    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        try {
            mCallback = (OnHeadlineSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.first_frag, container, false);

        //TextView tv = (TextView) v.findViewById(R.id.tvFragFirst);
        //tv.setText(getArguments().getString("msg"));

        //ArrayList<Word> words = new ArrayList<Word>();

        //words.add(new Word("8 Hours of Sleep", "+5"));
        //words.add(new Word("Eat Healthy", "+4"));
        //words.add(new Word("Excercise", "+5"));
        //words.add(new Word("Read (30+)", "+4"));
        //words.add(new Word("Shower", "+3"));
        //words.add(new Word("Study", "+4"));
        //words.add(new Word("Swim", "+6"));
        //words.add(new Word("Meditate", "+5"));
        //words.add(new Word("Clean", "+3"));
        //words.add(new Word("Music", "+4"));
        //words.add(new Word("Play Tennis", "+3"));


        //create an arrayadapter/listview
        //WordAdapter adapter = new WordAdapter(getActivity().getApplicationContext(), words);
        //ListView listView = (ListView)v.findViewById(R.id.list);
        //listView.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getActivity().getApplicationContext(), TaskEditorActivity.class);
                startActivity(intent);
            }
        });

        //displayDatabaseInfo();

        final ListView taskListView = (ListView) v.findViewById(R.id.list);

        //Setup an adapter to create a list item for each row of task data in the cursor
        mCursorAdapter = new TaskCursorAdapter(getActivity().getApplicationContext(), null);
        taskListView.setAdapter(mCursorAdapter);

        //Kick off the loader
        getLoaderManager().initLoader(TASK_LOADER, null, this);

        //Setup item click listener
        taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                mCallback.onTaskSelected(position, id);
            }
        });

        taskListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id){
                //deletes the current task that the user click and holds
                final Uri uri = ContentUris.withAppendedId(CONTENT_URI, id);

                //launch dialog asking if they want to delete the task
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setCancelable(false);
                dialog.setTitle("Delete task");
                dialog.setMessage("Are you sure you want to delete this task?");
                dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int rowsDeleted = getContext().getContentResolver().delete(uri, null, null);
                        if (rowsDeleted == 0){
                            Toast.makeText(getActivity(), "Error deleting task", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "Task deleted", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

                return true;
            }
        });

        return v;
    }

    /*@Override
    public void onStart(){
        super.onStart();
        displayDatabaseInfo();
    }*/

    public static FirstFragment newInstance(String text){

        FirstFragment f = new FirstFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }

    /*private void displayDatabaseInfo(){
        //To access the database we instantiate our subclass of the SQLliteOpenhelper and pass the context of this fragment
        //TaskDbHelper mDbHelper = new TaskDbHelper(getContext());
        //Create and/or open a database to read from
        //SQLiteDatabase db = mDbHelper.getReadableDatabase();
        //Perform a raw SQL query to get a cursor that contains all rows from the tasks table
        String[] projection = {
                _ID,
                COLUMN_TASK_NAME,
                COLUMN_TASK_DIFFICULTY,
                COLUMN_TASK_FREQUENCY
        };*/

    /**Cursor cursor = db.query(
     TABLE_NAME,
     projection,  //projection allows us to pick what columns we are interested in
     null,  //selection and selectionArgs help us pick what rows we are interested in
     null,
     null,
     null,
     null);
     */

    //COMMENTED OUT WHEN WE SWITCH FROM THIS ADAPTER TO THE CURSORLOADER
       /* //perform a query on the provider using the ContentResolver
        Cursor cursor = getActivity().getApplicationContext().getContentResolver().query(CONTENT_URI, projection, null, null, null);  //the getActivity.getApplicationContext is required since we are working with fragments that cant extend appcompatactivity

        //Find the ListView that will be populated with tasks
        ListView taskListView = (ListView) v.findViewById(R.id.list);

        //Setup an adapter to create a list
        TaskCursorAdapter adapter = new TaskCursorAdapter(getActivity().getApplicationContext(), cursor);

        //Attach the adapter to the listview
        taskListView.setAdapter(adapter);*/


    //TextView displayView = (TextView) v.findViewById(R.id.text_view_task);  //in first_frag (removed because we are switching to listview format

    //Display the number of rows in the cursor (reflects the nubmer of rows in the tasks table in the database
        /*try {
            displayView.setText("The tasks table contains " + cursor.getCount() + " tasks.\n\n");

            //Gives a header above the loop below for the textview
            displayView.append(_ID + " - " + COLUMN_TASK_NAME + " - " + COLUMN_TASK_DIFFICULTY + " - " + COLUMN_TASK_FREQUENCY + "\n" );

            //Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(_ID);
            int nameColumnIndex = cursor.getColumnIndex(COLUMN_TASK_NAME);
            int difficultyColumnIndex = cursor.getColumnIndex(COLUMN_TASK_DIFFICULTY);
            int frequencyColumnIndex = cursor.getColumnIndex(COLUMN_TASK_FREQUENCY);

            //Iterate through each returned row in the cursor
            while (cursor.moveToNext()){ //returns true each time it successfully moves to a next row
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                String currentDifficulty = cursor.getString(difficultyColumnIndex);
                String currentFrequency = cursor.getString(frequencyColumnIndex);
                displayView.append("\n" + currentID + " - " + currentName + " - " + currentDifficulty + " - " + currentFrequency);
            }

        } finally {
            cursor.close();
        }*/  //THIS OLD TRY CATCH WAS REMOVED BECASUE WE SWITCHED TO THE LISTVIEW FORMAT

    //}

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                _ID,
                COLUMN_TASK_NAME,
                COLUMN_TASK_DIFFICULTY };

        return new CursorLoader(getActivity().getApplicationContext(),  //Activity context
                CONTENT_URI,                                            //Provider content URI to query
                projection,                                             //Columns to include in the resulting cursor
                null,                                                   //No selection clause
                null,                                                   //No selection arguments
                null);                                                  //Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        //Update the adapter with the new cursor containing the new data
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //Called when the data needs to be deleted
        mCursorAdapter.swapCursor(null);
    }



}
