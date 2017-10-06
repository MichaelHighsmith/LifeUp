package com.satyrlabs.lifeup.Fragments;

import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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

import com.satyrlabs.lifeup.R;
import com.satyrlabs.lifeup.adapters.RewardsCursorAdapter;
import com.satyrlabs.lifeup.ui.RewardsEditorAcitivity;
import com.satyrlabs.lifeup.data.RewardsContract;


public class FourthFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int REWARD_LOADER = 0;

    OnRewardSelectedListener mCallbackReward;

    RewardsCursorAdapter rewardsCursorAdapter;

    //implement this in MainActivity
    public interface OnRewardSelectedListener{
        public void onRewardSelected(int position);
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        try {
            mCallbackReward = (OnRewardSelectedListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement OnRewardSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.rewards_frag, container, false);

        FloatingActionButton rewardsFab = (FloatingActionButton) v.findViewById(R.id.rewards_fab);
        rewardsFab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getActivity().getApplicationContext(), RewardsEditorAcitivity.class);
                startActivity(intent);
            }
        });

        final ListView rewardsListView = (ListView) v.findViewById(R.id.rewards_list);
        rewardsCursorAdapter = new RewardsCursorAdapter(getActivity().getApplicationContext(), null);
        rewardsListView.setAdapter(rewardsCursorAdapter);

        getLoaderManager().initLoader(REWARD_LOADER, null, this);

        rewardsListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id){
                mCallbackReward.onRewardSelected(position);
            }
        });

        //Set up the dialog so that if a user long clicks a reward they can delete it
        rewardsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id){
                //get the uri for the specified reward
                final Uri uri = ContentUris.withAppendedId(RewardsContract.RewardsEntry.REWARDS_CONTENT_URI, id);

                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setCancelable(false);
                dialog.setTitle("Delete Reward");
                dialog.setMessage("Are you sure you want to delete this reward?");
                dialog.setPositiveButton("YES", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        //delete the reward
                        int rowsDeleted = getContext().getContentResolver().delete(uri, null, null);
                        if(rowsDeleted == 0){
                            Toast.makeText(getActivity(), "Error deleting reward", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "Reward deleted", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                //Cancle the dialog
                dialog.setNegativeButton("NO", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        dialog.dismiss();
                    }
                });
                dialog.show();
                return true;
            }
        });

        return v;
    }

    public static FourthFragment newInstance(String text){
        FourthFragment f = new FourthFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                RewardsContract.RewardsEntry._ID,
                RewardsContract.RewardsEntry.COLUMN_REWARD_NAME,
                RewardsContract.RewardsEntry.COLUMN_REWARD_COST};

        return new CursorLoader(getActivity().getApplicationContext(),
                RewardsContract.RewardsEntry.REWARDS_CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        rewardsCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        rewardsCursorAdapter.swapCursor(null);
    }
}