package com.satyrlabs.lifeup;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

/**
 * Created by mhigh on 6/22/2017.
 */

public class ThirdFragment extends Fragment {

    View v;

    OnHeadlinesSelectedListener mCallbacks;

    ImageAdapter boyImageAdapter;
    GirlImageAdapter girlImageAdapter;


    public interface OnHeadlinesSelectedListener{
        public void onPictureSelected(int position);
        public void onHatSelected(int position);
        public void onWeaponSelected(int position);
        public void onShieldSelected(int position);
        public void onLeggingSelected(int position);
    }

    public void onAttach(Context context){
        super.onAttach(context);

        try {
            mCallbacks = (OnHeadlinesSelectedListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement OnHeadlinesSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        v = inflater.inflate(R.layout.pictures_grid, container, false);

        //reference the current gender that was saved the first time the app was launched
        SharedPreferences currentGender = this.getActivity().getSharedPreferences("currentGender", 0);

        final GridView gridview = (GridView) v.findViewById(R.id.gridview);
        //If the initial gender selection was a boy, then display the boy's clothing options in the gridview (reference sharedpreferences to check this data)
        if (currentGender.getString("gender", "").equals("boy")){
            boyImageAdapter = new ImageAdapter(getActivity().getApplicationContext());
            gridview.setAdapter(boyImageAdapter);

            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mCallbacks.onPictureSelected(position);
                    mCallbacks.onHatSelected(position);
                    mCallbacks.onWeaponSelected(position);
                    mCallbacks.onShieldSelected(position);
                    mCallbacks.onLeggingSelected(position);
                }
            });
        } else if (currentGender.getString("gender", "").equals("girl")){
            girlImageAdapter = new GirlImageAdapter(getActivity().getApplicationContext());
            gridview.setAdapter(girlImageAdapter);

            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mCallbacks.onPictureSelected(position);
                    mCallbacks.onHatSelected(position);
                    mCallbacks.onWeaponSelected(position);
                    mCallbacks.onShieldSelected(position);
                    mCallbacks.onLeggingSelected(position);
                }
            });
        }
        //TextView tv = (TextView) v.findViewById(R.id.tvFragThird);
        //tv.setText(getArguments().getString("msg"));

        return v;
    }

    public static ThirdFragment newInstance(String text){

        ThirdFragment f = new ThirdFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }
}