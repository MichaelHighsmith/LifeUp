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


    public static FirstFragment newInstance(String text){

        FirstFragment f = new FirstFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }


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
