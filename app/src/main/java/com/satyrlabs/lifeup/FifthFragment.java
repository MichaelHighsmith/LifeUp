package com.satyrlabs.lifeup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by mhigh on 6/22/2017.
 */

public class FifthFragment extends Fragment {

    OnHealthRestoreListener mHealthRestoreListener;

    public interface OnHealthRestoreListener{
        public void onHealthRestoreButtonClicked();
    }

    public void onAttach(Context context){
        super.onAttach(context);
        try{
            mHealthRestoreListener = (OnHealthRestoreListener) context;
        } catch(ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement OnHealthResoreListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.game_frag, container, false);

        ImageView iceDragonButton = (ImageView) v.findViewById(R.id.ice_dragon_button);
        iceDragonButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                //Store the selected enemy in a shared pref and it will be used to load data in the battlefield (must use getActivity() because this is a fragment.
                SharedPreferences enemyPref = getActivity().getSharedPreferences("enemy", 0);
                SharedPreferences.Editor enemy_editor = enemyPref.edit();
                enemy_editor.putString("currentEnemy", "ice_dragon");
                enemy_editor.apply();

                Intent intent = new Intent(getActivity().getApplicationContext(), BattleActivity.class);
                startActivity(intent);
            }
        });

        ImageView batButton = (ImageView) v.findViewById(R.id.bat_button);
        batButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                //Store the selected enemy
                SharedPreferences enemyPref = getActivity().getSharedPreferences("enemy", 0);
                SharedPreferences.Editor enemy_editor = enemyPref.edit();
                enemy_editor.putString("currentEnemy", "bat");
                enemy_editor.apply();

                Intent intent = new Intent(getActivity().getApplicationContext(), BattleActivity.class);
                startActivity(intent);
            }
        });

        ImageView octopusButton = (ImageView) v.findViewById(R.id.octopus_button);
        octopusButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                //Store the selected enemy
                SharedPreferences enemyPref = getActivity().getSharedPreferences("enemy", 0);
                SharedPreferences.Editor enemy_editor = enemyPref.edit();
                enemy_editor.putString("currentEnemy", "octopus");
                enemy_editor.apply();

                Intent intent = new Intent(getActivity().getApplicationContext(), BattleActivity.class);
                startActivity(intent);
            }
        });

        ImageView slimeMonsterButton = (ImageView) v.findViewById(R.id.slime_monster_button);
        slimeMonsterButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                //Store the selected enemy
                SharedPreferences enemyPref = getActivity().getSharedPreferences("enemy", 0);
                SharedPreferences.Editor enemy_editor = enemyPref.edit();
                enemy_editor.putString("currentEnemy", "slime_monster");
                enemy_editor.apply();

                Intent intent = new Intent(getActivity().getApplicationContext(), BattleActivity.class);
                startActivity(intent);
            }
        });

        ImageView spiderButton = (ImageView) v.findViewById(R.id.spider_button);
        spiderButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                //Store the selected enemy
                SharedPreferences enemyPref = getActivity().getSharedPreferences("enemy", 0);
                SharedPreferences.Editor enemy_editor = enemyPref.edit();
                enemy_editor.putString("currentEnemy", "spider");
                enemy_editor.apply();

                Intent intent = new Intent(getActivity().getApplicationContext(), BattleActivity.class);
                startActivity(intent);
            }
        });

        ImageView snakeButton = (ImageView) v.findViewById(R.id.snake_button);
        snakeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                //Store the selected enemy
                SharedPreferences enemyPref = getActivity().getSharedPreferences("enemy", 0);
                SharedPreferences.Editor enemy_editor = enemyPref.edit();
                enemy_editor.putString("currentEnemy", "snake");
                enemy_editor.apply();

                Intent intent = new Intent(getActivity().getApplicationContext(), BattleActivity.class);
                startActivity(intent);
            }
        });

        ImageView wolfButton = (ImageView) v.findViewById(R.id.wolf_button);
        wolfButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                //Store the selected enemy
                SharedPreferences enemyPref = getActivity().getSharedPreferences("enemy", 0);
                SharedPreferences.Editor enemy_editor = enemyPref.edit();
                enemy_editor.putString("currentEnemy", "wolf");
                enemy_editor.apply();

                Intent intent = new Intent(getActivity().getApplicationContext(), BattleActivity.class);
                startActivity(intent);
            }
        });

        ImageView ghostButton = (ImageView) v.findViewById(R.id.ghost_button);
        ghostButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                //Store the selected enemy
                SharedPreferences enemyPref = getActivity().getSharedPreferences("enemy", 0);
                SharedPreferences.Editor enemy_editor = enemyPref.edit();
                enemy_editor.putString("currentEnemy", "ghost");
                enemy_editor.apply();

                Intent intent = new Intent(getActivity().getApplicationContext(), BattleActivity.class);
                startActivity(intent);
            }
        });

        ImageView triclopsButton = (ImageView) v.findViewById(R.id.triclops_button);
        triclopsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                //Store the selected enemy
                SharedPreferences enemyPref = getActivity().getSharedPreferences("enemy", 0);
                SharedPreferences.Editor enemy_editor = enemyPref.edit();
                enemy_editor.putString("currentEnemy", "triclops");
                enemy_editor.apply();

                Intent intent = new Intent(getActivity().getApplicationContext(), BattleActivity.class);
                startActivity(intent);
            }
        });

        ImageView fireDragonButton = (ImageView) v.findViewById(R.id.fire_dragon_button);
        fireDragonButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                //Store the selected enemy
                SharedPreferences enemyPref = getActivity().getSharedPreferences("enemy", 0);
                SharedPreferences.Editor enemy_editor = enemyPref.edit();
                enemy_editor.putString("currentEnemy", "fire_dragon");
                enemy_editor.apply();

                Intent intent = new Intent(getActivity().getApplicationContext(), BattleActivity.class);
                startActivity(intent);
            }
        });

        ImageView crabButton = (ImageView) v.findViewById(R.id.crab_button);
        crabButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                //Store the selected enemy
                SharedPreferences enemyPref = getActivity().getSharedPreferences("enemy", 0);
                SharedPreferences.Editor enemy_editor = enemyPref.edit();
                enemy_editor.putString("currentEnemy", "crab");
                enemy_editor.apply();

                Intent intent = new Intent(getActivity().getApplicationContext(), BattleActivity.class);
                startActivity(intent);
            }
        });

        ImageView grimReaperButton = (ImageView) v.findViewById(R.id.grim_reaper_button);
        grimReaperButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                //Store the selected enemy
                SharedPreferences enemyPref = getActivity().getSharedPreferences("enemy", 0);
                SharedPreferences.Editor enemy_editor = enemyPref.edit();
                enemy_editor.putString("currentEnemy", "grim_reaper");
                enemy_editor.apply();

                Intent intent = new Intent(getActivity().getApplicationContext(), BattleActivity.class);
                startActivity(intent);
            }
        });

        ImageView slimeMonsterMultipleButton = (ImageView) v.findViewById(R.id.slime_monster_multiple_button);
        slimeMonsterMultipleButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                //Store the selected enemy
                SharedPreferences enemyPref = getActivity().getSharedPreferences("enemy", 0);
                SharedPreferences.Editor enemy_editor = enemyPref.edit();
                enemy_editor.putString("currentEnemy", "slime_monster_multiple");
                enemy_editor.apply();

                Intent intent = new Intent(getActivity().getApplicationContext(), BattleActivity.class);
                startActivity(intent);
            }
        });

        ImageView girlEnemyButton = (ImageView) v.findViewById(R.id.girl_enemy_button);
        girlEnemyButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                //Store the selected enemy
                SharedPreferences enemyPref = getActivity().getSharedPreferences("enemy", 0);
                SharedPreferences.Editor enemy_editor = enemyPref.edit();
                enemy_editor.putString("currentEnemy", "girl_enemy");
                enemy_editor.apply();

                Intent intent = new Intent(getActivity().getApplicationContext(), BattleActivity.class);
                startActivity(intent);
            }
        });

        ImageView boyEnemyButton = (ImageView) v.findViewById(R.id.boy_enemy_button);
        boyEnemyButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                //Store the selected enemy
                SharedPreferences enemyPref = getActivity().getSharedPreferences("enemy", 0);
                SharedPreferences.Editor enemy_editor = enemyPref.edit();
                enemy_editor.putString("currentEnemy", "boy_enemy");
                enemy_editor.apply();

                Intent intent = new Intent(getActivity().getApplicationContext(), BattleActivity.class);
                startActivity(intent);
            }
        });

        Button getMoreHealthButton = (Button) v.findViewById(R.id.more_health_button);
        getMoreHealthButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                mHealthRestoreListener.onHealthRestoreButtonClicked();

            }
        });

        return v;
    }

    public static FifthFragment newInstance(String text){
        FifthFragment f = new FifthFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }
}
