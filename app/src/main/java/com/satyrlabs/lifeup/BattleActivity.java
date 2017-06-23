package com.satyrlabs.lifeup;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

/**
 * Created by mhigh on 6/22/2017.
 */

public class BattleActivity extends AppCompatActivity {

    //gold
    int diamonds, currentDiamonds, diamondsFromEnemy;

    ImageView enemy1, enemy2, enemy3, enemy4, enemy5, enemy6, enemy7, enemy8, enemy9, enemy10, enemy11, enemy12, enemy13;

    ImageView health_bar, enemy_health_bar;

    ImageView enemy_image;

    TextView tv_health, tv_enemy;

    Button button;

    Random r;

    //total clicks to take place during the battle;
    int totalClicks = 0, selectedWeapon = 1, selectedShield = 1;
    float weaponDamage = 1.0f, enemyDamage = 1.0f, defense = 1.0f;

    String sharedPrefEnemy;
    String enemyName;

    float playerHealth, health, maxHealth, enemyHealth, maxEnemyHealth;
    int playerHealthInt, enemyHealthInt;

    int fps = 1000, tempifleft = 0;

    int which = 0;
    int whichsave = 0;

    AnimationDrawable an;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.battle_layout);

        tv_health = (TextView) findViewById(R.id.tv_health);
        tv_enemy = (TextView) findViewById(R.id.tv_enemy);

        enemy_image = (ImageView) findViewById(R.id.enemyImage);
        //Retrieve the info regarding which enemy was selected
        SharedPreferences enemyPref = getSharedPreferences("enemy", 0);
        sharedPrefEnemy = enemyPref.getString("currentEnemy", "");
        Log.v(sharedPrefEnemy, "current enemy is this");

        //Set the images accordingly
        setEnemyDetails();

        SharedPreferences shield = getSharedPreferences("shield", 0);
        selectedShield = shield.getInt("currentShield", 1);
        switch(selectedShield){
            case(27):
                defense = 1.0f;
                break;
            case(28):
                defense = 1.3f;
                break;
            case(29):
                defense = 1.3f;
                break;
            case(30):
                defense = 1.5f;
                break;
            case(31):
                defense = 1.5f;
                break;
            case(32):
                defense = 1.5f;
                break;
            case(33):
                defense = 2.0f;
                break;
            case(34):
                defense = 2.3f;
                break;
            case(35):
                defense = 2.6f;
                break;
            case(36):
                defense = 3.0f;
                break;
            case(37):
                defense = 3.5f;
                break;
            case(38):
                defense = 4.0f;
                break;

        }


        //Retrieve info on what weapon has been selected (for damage purposes)TODO update this according to the Store fragment
        SharedPreferences weapon = getSharedPreferences("weapon", 0);
        selectedWeapon =  weapon.getInt("currentWeapon", 1);
        switch (selectedWeapon){
            case(45):
                weaponDamage = 1.0f;
                break;
            case(46):
                weaponDamage = 2.0f;
                break;
            case(47):
                weaponDamage = 3.0f;
                break;
            case(48):
                weaponDamage = 5.0f;
                break;
            case(49):
                weaponDamage = 7.0f;
                break;
            case(50):
                weaponDamage = 11.0f;
                break;
            case(51):
                weaponDamage = 16.0f;
                break;
            case(52):
                weaponDamage = 18.0f;
                break;
            case(53):
                weaponDamage = 21.0f;
                break;
            case(54):
                weaponDamage = 25.0f;
                break;
            case(55):
                weaponDamage = 40.0f;
                break;
            case(56):
                weaponDamage = 57.0f;
                break;
            case(57):
                weaponDamage = 71.0f;
                break;
            case(58):
                weaponDamage = 133.0f;
                break;
            case(59):
                weaponDamage = 226.0f;
                break;

        }



        SharedPreferences firstBattle = getSharedPreferences("myFirstBattle", 0);
        if (firstBattle.getBoolean("my_first_battle", true)){
            introBattleDialog();
            firstBattle.edit().putBoolean("my_first_battle", false).apply();
        }

        //Get the user's health (from main activity)
        SharedPreferences myHealth = getSharedPreferences("myPref", 0);
        playerHealth = myHealth.getFloat("health", 50.0f);

        health_bar = (ImageView) findViewById(R.id.player_health_bar);
        getPlayerHealthBar();

        health = playerHealth;
        playerHealthInt = Math.round(health);
        tv_health.setText("Health: " + playerHealthInt);

        enemy_health_bar = (ImageView) findViewById(R.id.enemy_health_bar);
        getEnemyHealthBar();

        enemyHealthInt = Math.round(enemyHealth);
        tv_enemy.setText(enemyName + ": " + enemyHealthInt);

        r = new Random();

        button = (Button) findViewById(R.id.start_battle_button);


        enemy1 = (ImageView) findViewById(R.id.enemy1);
        enemy2 = (ImageView) findViewById(R.id.enemy2);
        enemy3 = (ImageView) findViewById(R.id.enemy3);
        enemy4 = (ImageView) findViewById(R.id.enemy4);
        enemy5 = (ImageView) findViewById(R.id.enemy5);
        enemy6 = (ImageView) findViewById(R.id.enemy6);
        enemy7 = (ImageView) findViewById(R.id.enemy7);
        enemy8 = (ImageView) findViewById(R.id.enemy8);
        enemy9 = (ImageView) findViewById(R.id.enemy9);
        enemy10 = (ImageView) findViewById(R.id.enemy10);
        enemy11 = (ImageView) findViewById(R.id.enemy11);
        enemy12 = (ImageView) findViewById(R.id.enemy12);
        enemy13 = (ImageView) findViewById(R.id.enemy13);

        enemy1.setVisibility(View.INVISIBLE);
        enemy2.setVisibility(View.INVISIBLE);
        enemy3.setVisibility(View.INVISIBLE);
        enemy4.setVisibility(View.INVISIBLE);
        enemy5.setVisibility(View.INVISIBLE);
        enemy6.setVisibility(View.INVISIBLE);
        enemy7.setVisibility(View.INVISIBLE);
        enemy8.setVisibility(View.INVISIBLE);
        enemy9.setVisibility(View.INVISIBLE);
        enemy10.setVisibility(View.INVISIBLE);
        enemy11.setVisibility(View.INVISIBLE);
        enemy12.setVisibility(View.INVISIBLE);
        enemy13.setVisibility(View.INVISIBLE);

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if (playerHealth == 0){
                    Toast.makeText(BattleActivity.this, "Sorry you don't have enough health to battle", Toast.LENGTH_SHORT).show();
                } else{
                    //reset the healthbar and enemy health
                    setEnemyDetails();
                    enemyHealthInt = Math.round(enemyHealth);
                    tv_enemy.setText(enemyName + ": " + enemyHealthInt);
                    getEnemyHealthBar();
                    health = playerHealth;
                    playerHealthInt = Math.round(health);
                    tv_health.setText("Health: " + playerHealthInt);
                    //run the handler to start the animation
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run(){
                            theGameActions();
                        }
                    }, 1000);
                    button.setEnabled(false);
                }
            }
        });

        enemy1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                tempifleft = 1;
                enemy1.setImageResource(R.drawable.sword_checkmark);
                enemyHealth = enemyHealth - weaponDamage;
                enemyHealthInt = Math.round(enemyHealth);
                tv_enemy.setText(enemyName + ": " + enemyHealthInt);
                getEnemyHealthBar();
                totalClicks = totalClicks + 1;
                enemy1.setEnabled(false);
            }
        });

        enemy2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                tempifleft = 1;
                enemy2.setImageResource(R.drawable.sword_checkmark);
                enemyHealth = enemyHealth - weaponDamage;
                enemyHealthInt = Math.round(enemyHealth);
                tv_enemy.setText(enemyName + ": " + enemyHealthInt);
                getEnemyHealthBar();
                totalClicks = totalClicks + 1;
                enemy2.setEnabled(false);
            }
        });

        enemy3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                tempifleft = 1;
                enemy3.setImageResource(R.drawable.sword_checkmark);
                enemyHealth = enemyHealth - weaponDamage;
                enemyHealthInt = Math.round(enemyHealth);
                tv_enemy.setText(enemyName + ": " + enemyHealthInt);
                getEnemyHealthBar();
                totalClicks = totalClicks + 1;
                enemy3.setEnabled(false);
            }
        });

        enemy4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                tempifleft = 1;
                enemy4.setImageResource(R.drawable.sword_checkmark);
                enemyHealth = enemyHealth - weaponDamage;
                enemyHealthInt = Math.round(enemyHealth);
                tv_enemy.setText(enemyName + ": " + enemyHealthInt);
                getEnemyHealthBar();
                totalClicks = totalClicks + 1;
                enemy4.setEnabled(false);
            }
        });

        enemy5.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                tempifleft = 1;
                enemy5.setImageResource(R.drawable.sword_checkmark);
                enemyHealth = enemyHealth - weaponDamage;
                enemyHealthInt = Math.round(enemyHealth);
                tv_enemy.setText(enemyName + ": " + enemyHealthInt);
                getEnemyHealthBar();
                totalClicks = totalClicks + 1;
                enemy5.setEnabled(false);
            }
        });

        enemy6.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                tempifleft = 1;
                enemy6.setImageResource(R.drawable.sword_checkmark);
                enemyHealth = enemyHealth - weaponDamage;
                enemyHealthInt = Math.round(enemyHealth);
                tv_enemy.setText(enemyName + ": " + enemyHealthInt);
                getEnemyHealthBar();
                totalClicks = totalClicks + 1;
                enemy6.setEnabled(false);
            }
        });

        enemy7.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                tempifleft = 1;
                enemy7.setImageResource(R.drawable.sword_checkmark);
                enemyHealth = enemyHealth - weaponDamage;
                enemyHealthInt = Math.round(enemyHealth);
                tv_enemy.setText(enemyName + ": " + enemyHealthInt);
                getEnemyHealthBar();
                totalClicks = totalClicks + 1;
                enemy7.setEnabled(false);
            }
        });

        enemy8.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                tempifleft = 1;
                enemy8.setImageResource(R.drawable.sword_checkmark);
                enemyHealth = enemyHealth - weaponDamage;
                enemyHealthInt = Math.round(enemyHealth);
                tv_enemy.setText(enemyName + ": " + enemyHealthInt);
                getEnemyHealthBar();
                totalClicks = totalClicks + 1;
                enemy8.setEnabled(false);
            }
        });

        enemy9.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                tempifleft = 1;
                enemy9.setImageResource(R.drawable.sword_checkmark);
                enemyHealth = enemyHealth - weaponDamage;
                enemyHealthInt = Math.round(enemyHealth);
                tv_enemy.setText(enemyName + ": " + enemyHealthInt);
                getEnemyHealthBar();
                totalClicks = totalClicks + 1;
                enemy9.setEnabled(false);
            }
        });

        enemy10.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                tempifleft = 1;
                enemy10.setImageResource(R.drawable.sword_checkmark);
                enemyHealth = enemyHealth - weaponDamage;
                enemyHealthInt = Math.round(enemyHealth);
                tv_enemy.setText(enemyName + ": " + enemyHealthInt);
                getEnemyHealthBar();
                totalClicks = totalClicks + 1;
                enemy10.setEnabled(false);
            }
        });

        enemy11.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                tempifleft = 1;
                enemy11.setImageResource(R.drawable.sword_checkmark);
                enemyHealth = enemyHealth - weaponDamage;
                enemyHealthInt = Math.round(enemyHealth);
                tv_enemy.setText(enemyName + ": " + enemyHealthInt);
                getEnemyHealthBar();
                totalClicks = totalClicks + 1;
                enemy11.setEnabled(false);
            }
        });

        enemy12.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                tempifleft = 1;
                enemy12.setImageResource(R.drawable.sword_checkmark);
                enemyHealth = enemyHealth - weaponDamage;
                enemyHealthInt = Math.round(enemyHealth);
                tv_enemy.setText(enemyName + ": " + enemyHealthInt);
                getEnemyHealthBar();
                totalClicks = totalClicks + 1;
                enemy12.setEnabled(false);
            }
        });

        enemy13.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                tempifleft = 1;
                enemy13.setImageResource(R.drawable.sword_checkmark);
                enemyHealth = enemyHealth - weaponDamage;
                enemyHealthInt = Math.round(enemyHealth);
                tv_enemy.setText(enemyName + ": " + enemyHealthInt);
                getEnemyHealthBar();
                totalClicks = totalClicks + 1;
                enemy13.setEnabled(false);
            }
        });

    }

    private void theGameActions() {

        //As the total # of clicks goes up, speed up the image rate
        if(totalClicks < 5){
            fps = 1000;
        } else if (totalClicks < 10){
            fps = 900;
        } else if (totalClicks < 15){
            fps = 800;
        } else if (totalClicks < 20){
            fps = 700;
        } else if (totalClicks < 25){
            fps = 650;
        } else if (totalClicks < 30){
            fps = 600;
        } else if (totalClicks < 35){
            fps = 550;
        } else if (totalClicks < 40){
            fps = 525;
        } else if (totalClicks < 45){
            fps = 500;
        } else if (totalClicks < 50){
            fps = 475;
        } else if (totalClicks < 55){
            fps = 450;
        } else if (totalClicks < 60){
            fps = 425;
        } else if (totalClicks < 65){
            fps = 410;
        } else if (totalClicks < 70){
            fps = 400;
        } else if (totalClicks < 75){
            fps = 380;
        } else if (totalClicks < 80){
            fps = 370;
        } else if (totalClicks < 90){
            fps = 360;
        } else if (totalClicks < 100){
            fps = 350;
        } else if (totalClicks < 110){
            fps = 340;
        } else if (totalClicks < 120){
            fps = 330;
        }


        //change the pop-up images (the 13 clickable images) depending on which enemy is battling
        if (sharedPrefEnemy.equals("ice_dragon")){
            an = (AnimationDrawable) ContextCompat.getDrawable(this, R.drawable.battle_anim);
        } else if (sharedPrefEnemy.equals("bat")){
            an = (AnimationDrawable) ContextCompat.getDrawable(this, R.drawable.bat_battle_anim);
        } else if (sharedPrefEnemy.equals("octopus")){
            an = (AnimationDrawable) ContextCompat.getDrawable(this, R.drawable.octopus_battle_anim);
        } else if (sharedPrefEnemy.equals("slime_monster")){
            an = (AnimationDrawable) ContextCompat.getDrawable(this, R.drawable.slime_monster_battle_anim);
        } else if (sharedPrefEnemy.equals("spider")){
            an = (AnimationDrawable) ContextCompat.getDrawable(this, R.drawable.spider_battle_anim);
        } else if (sharedPrefEnemy.equals("snake")){
            an = (AnimationDrawable) ContextCompat.getDrawable(this, R.drawable.snake_battle_anim);
        } else if (sharedPrefEnemy.equals("wolf")){
            an = (AnimationDrawable) ContextCompat.getDrawable(this, R.drawable.wolf_battle_anim);
        } else if (sharedPrefEnemy.equals("ghost")){
            an = (AnimationDrawable) ContextCompat.getDrawable(this, R.drawable.ghost_battle_anim);
        } else if (sharedPrefEnemy.equals("triclops")){
            an = (AnimationDrawable) ContextCompat.getDrawable(this, R.drawable.triclops_battle_anim);
        } else if (sharedPrefEnemy.equals("fire_dragon")){
            an = (AnimationDrawable) ContextCompat.getDrawable(this, R.drawable.fire_dragon_battle_anim);
        } else if (sharedPrefEnemy.equals("crab")){
            an = (AnimationDrawable) ContextCompat.getDrawable(this, R.drawable.crab_battle_anim);
        } else if (sharedPrefEnemy.equals("grim_reaper")){
            an = (AnimationDrawable) ContextCompat.getDrawable(this, R.drawable.grim_reaper_battle_anim);
        } else if (sharedPrefEnemy.equals("slime_monster_multiple")){
            an = (AnimationDrawable) ContextCompat.getDrawable(this, R.drawable.slime_monster_multiple_battle_anim);
        } else if (sharedPrefEnemy.equals("girl_enemy")){
            an = (AnimationDrawable) ContextCompat.getDrawable(this, R.drawable.girl_enemy_battle_anim);
        } else if (sharedPrefEnemy.equals("boy_enemy")){
            an = (AnimationDrawable) ContextCompat.getDrawable(this, R.drawable.boy_enemy_battle_anim);
        }

        //as long as the selected image matches the random one (changes every fps lenght (starts at 1000), that image is enabled for clicking
        do{
            which = r.nextInt(13) + 1;
        } while (whichsave == which);
        whichsave = which;

        if (which == 1){
            enemy1.setImageDrawable(an);
            enemy1.setVisibility(View.VISIBLE);
            enemy1.setEnabled(true);
        } else if(which == 2){
            enemy2.setImageDrawable(an);
            enemy2.setVisibility(View.VISIBLE);
            enemy2.setEnabled(true);
        } else if(which == 3){
            enemy3.setImageDrawable(an);
            enemy3.setVisibility(View.VISIBLE);
            enemy3.setEnabled(true);
        } else if(which == 4){
            enemy4.setImageDrawable(an);
            enemy4.setVisibility(View.VISIBLE);
            enemy4.setEnabled(true);
        } else if(which == 5){
            enemy5.setImageDrawable(an);
            enemy5.setVisibility(View.VISIBLE);
            enemy5.setEnabled(true);
        } else if(which == 6){
            enemy6.setImageDrawable(an);
            enemy6.setVisibility(View.VISIBLE);
            enemy6.setEnabled(true);
        } else if(which == 7){
            enemy7.setImageDrawable(an);
            enemy7.setVisibility(View.VISIBLE);
            enemy7.setEnabled(true);
        } else if(which == 8){
            enemy8.setImageDrawable(an);
            enemy8.setVisibility(View.VISIBLE);
            enemy8.setEnabled(true);
        } else if(which == 9){
            enemy9.setImageDrawable(an);
            enemy9.setVisibility(View.VISIBLE);
            enemy9.setEnabled(true);
        } else if(which == 10){
            enemy10.setImageDrawable(an);
            enemy10.setVisibility(View.VISIBLE);
            enemy10.setEnabled(true);
        } else if(which == 11){
            enemy11.setImageDrawable(an);
            enemy11.setVisibility(View.VISIBLE);
            enemy11.setEnabled(true);
        } else if(which == 12){
            enemy12.setImageDrawable(an);
            enemy12.setVisibility(View.VISIBLE);
            enemy12.setEnabled(true);
        } else if(which == 13){
            enemy13.setImageDrawable(an);
            enemy13.setVisibility(View.VISIBLE);
            enemy13.setEnabled(true);
        }
        an.start();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run(){
                enemy1.setVisibility(View.INVISIBLE);
                enemy2.setVisibility(View.INVISIBLE);
                enemy3.setVisibility(View.INVISIBLE);
                enemy4.setVisibility(View.INVISIBLE);
                enemy5.setVisibility(View.INVISIBLE);
                enemy6.setVisibility(View.INVISIBLE);
                enemy7.setVisibility(View.INVISIBLE);
                enemy8.setVisibility(View.INVISIBLE);
                enemy9.setVisibility(View.INVISIBLE);
                enemy10.setVisibility(View.INVISIBLE);
                enemy11.setVisibility(View.INVISIBLE);
                enemy12.setVisibility(View.INVISIBLE);
                enemy13.setVisibility(View.INVISIBLE);

                enemy1.setEnabled(false);
                enemy2.setEnabled(false);
                enemy3.setEnabled(false);
                enemy4.setEnabled(false);
                enemy5.setEnabled(false);
                enemy6.setEnabled(false);
                enemy7.setEnabled(false);
                enemy8.setEnabled(false);
                enemy9.setEnabled(false);
                enemy10.setEnabled(false);
                enemy11.setEnabled(false);
                enemy12.setEnabled(false);
                enemy13.setEnabled(false);

                //If the chosen box isn't clicked, then decrement health by 1
                if(tempifleft == 0){
                    totalClicks = totalClicks + 1;
                    health = health - (enemyDamage / defense);
                    if (health <=0){
                        health = 0;
                    }
                    playerHealthInt = Math.round(health);
                    tv_health.setText("Health: " + playerHealthInt);
                    SharedPreferences sharedPref = getSharedPreferences("myPref", 0);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putFloat("health", health);
                    editor.apply();
                    getPlayerHealthBar();
                } else if(tempifleft == 1){
                    tempifleft = 0;
                }

                //If enemy health runs out, then end the game and TODO add an add/return jewels
                if(enemyHealth <= 0.0f ){
                    Toast.makeText(BattleActivity.this, "You won " + diamondsFromEnemy + " diamonds!", Toast.LENGTH_SHORT).show();
                    button.setEnabled(true);
                    button.setText("Battle Again");

                    //Load the previous gold amount
                    SharedPreferences sharedPreferences = getSharedPreferences("myPref", 0);
                    currentDiamonds = sharedPreferences.getInt("diamonds", 0);

                    diamonds = currentDiamonds + diamondsFromEnemy;

                    //Give the user gold for winning
                    SharedPreferences sharedPref = getSharedPreferences("myPref", 0);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt("diamonds", diamonds);
                    editor.apply();
                    //store the current health after the battle
                    editor.putFloat("health", health);
                    editor.apply();
                }

                //If health runs out, set the battle button to clickable again and end the game
                if(health <= 0.0f){
                    Toast.makeText(BattleActivity.this, "You were defeated in battle", Toast.LENGTH_SHORT).show();
                    button.setEnabled(true);
                    //return the 0 health for mainactivity
                    SharedPreferences sharedPref = getSharedPreferences("myPref", 0);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putFloat("health", health);
                    editor.apply();
                }
                //if health is not 0, run one more frame of the game.
                else if(health > 0.0f && enemyHealth > 0.0f){
                    theGameActions();
                }
            }
        }, fps);

    }

    //first time dialog explainging the battle
    public void introBattleDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.battle_intro_dialog);
        dialog.setTitle("First Battle!");
        dialog.setCancelable(false);

        final Button button = (Button) dialog.findViewById(R.id.battle_dialog_button);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    public void setEnemyDetails(){
        if (sharedPrefEnemy.equals("ice_dragon")){
            enemy_image.setImageResource(R.drawable.dragon_full);
            enemyDamage = 125.0f;
            enemyHealth = 7500.0f;
            maxEnemyHealth = 7500.0f;
            enemyName = "Ice Dragon";
            tv_enemy.setText(enemyName + ": " + enemyHealth);
            diamondsFromEnemy = 7;
        } else if (sharedPrefEnemy.equals("bat")){
            enemy_image.setImageResource(R.drawable.bat);
            enemyDamage = 5.0f;
            enemyHealth = 20.0f;
            maxEnemyHealth = 20.0f;
            enemyName = "Cave Bat";
            tv_enemy.setText(enemyName + ": " + enemyHealth);
            diamondsFromEnemy = 1;
        } else if (sharedPrefEnemy.equals("octopus")){
            enemy_image.setImageResource(R.drawable.octopus);
            enemyDamage = 20.0f;
            enemyHealth = 150f;
            maxEnemyHealth = 150.0f;
            enemyName = "Octopus";
            tv_enemy.setText(enemyName + ": " + enemyHealth);
            diamondsFromEnemy = 2;
        } else if (sharedPrefEnemy.equals("slime_monster")){
            enemy_image.setImageResource(R.drawable.slime_monster);
            enemyDamage = 10.0f;
            enemyHealth = 50.0f;
            maxEnemyHealth = 50.0f;
            enemyName = "Toxic Waste";
            tv_enemy.setText(enemyName + ": " + enemyHealth);
            diamondsFromEnemy = 1;
        } else if (sharedPrefEnemy.equals("spider")){
            enemy_image.setImageResource(R.drawable.spider);
            enemyDamage = 15.0f;
            enemyHealth = 100.0f;
            maxEnemyHealth = 100.0f;
            enemyName = "Spider";
            tv_enemy.setText(enemyName + ": " + enemyHealth);
            diamondsFromEnemy = 1;
        } else if (sharedPrefEnemy.equals("snake")){
            enemy_image.setImageResource(R.drawable.snake);
            enemyDamage = 45.0f;
            enemyHealth = 350.0f;
            maxEnemyHealth = 350.0f;
            enemyName = "Snake";
            tv_enemy.setText(enemyName + ": " + enemyHealth);
            diamondsFromEnemy = 2;
        } else if (sharedPrefEnemy.equals("wolf")){
            enemy_image.setImageResource(R.drawable.wolf);
            enemyDamage = 25.0f;
            enemyHealth = 200.0f;
            maxEnemyHealth = 200.0f;
            enemyName = "Wolf";
            tv_enemy.setText(enemyName + ": " + enemyHealth);
            diamondsFromEnemy = 2;
        } else if (sharedPrefEnemy.equals("ghost")){
            enemy_image.setImageResource(R.drawable.ghost);
            enemyDamage = 35.0f;
            enemyHealth = 600.0f;
            maxEnemyHealth = 600.0f;
            enemyName = "Ghost";
            tv_enemy.setText(enemyName + ": " + enemyHealth);
            diamondsFromEnemy = 3;
        } else if (sharedPrefEnemy.equals("triclops")){
            enemy_image.setImageResource(R.drawable.triclops);
            enemyDamage = 100.0f;
            enemyHealth = 5000.0f;
            maxEnemyHealth = 5000.0f;
            enemyName = "Triclops";
            tv_enemy.setText(enemyName + ": " + enemyHealth);
            diamondsFromEnemy = 6;
        } else if (sharedPrefEnemy.equals("fire_dragon")){
            enemy_image.setImageResource(R.drawable.fire_dragon);
            enemyDamage = 60.0f;
            enemyHealth = 1000.0f;
            maxEnemyHealth = 1000.0f;
            enemyName = "Fire Dragon";
            tv_enemy.setText(enemyName + ": " + enemyHealth);
            diamondsFromEnemy = 5;
        } else if (sharedPrefEnemy.equals("crab")){
            enemy_image.setImageResource(R.drawable.crab);
            enemyDamage = 35.0f;
            enemyHealth = 450.0f;
            maxEnemyHealth = 450.0f;
            enemyName = "Sea Crab";
            tv_enemy.setText(enemyName + ": " + enemyHealth);
            diamondsFromEnemy = 3;
        } else if (sharedPrefEnemy.equals("grim_reaper")){
            enemy_image.setImageResource(R.drawable.grim_reaper);
            enemyDamage = 75.0f;
            enemyHealth = 2000.0f;
            maxEnemyHealth = 2000.0f;
            enemyName = "Grim Reaper";
            tv_enemy.setText(enemyName + ": " + enemyHealth);
            diamondsFromEnemy = 5;
        } else if (sharedPrefEnemy.equals("slime_monster_multiple")){
            enemy_image.setImageResource(R.drawable.slime_monster_multiple);
            enemyDamage = 30.0f;
            enemyHealth = 3500.0f;
            maxEnemyHealth = 3500.0f;
            enemyName = "Toxic Sludge";
            tv_enemy.setText(enemyName + ": " + enemyHealth);
            diamondsFromEnemy = 6;
        } else if (sharedPrefEnemy.equals("girl_enemy")){
            enemy_image.setImageResource(R.drawable.girl_enemy);
            enemyDamage = 40.0f;
            enemyHealth = 750.0f;
            maxEnemyHealth = 750.0f;
            enemyName = "Evil Clone";
            tv_enemy.setText(enemyName + ": " + enemyHealth);
            diamondsFromEnemy = 5;
        } else if (sharedPrefEnemy.equals("boy_enemy")){
            enemy_image.setImageResource(R.drawable.boy_enemy);
            enemyDamage = 50.0f;
            enemyHealth = 750.0f;
            maxEnemyHealth = 750.0f;
            enemyName = "Evil Clone";
            tv_enemy.setText(enemyName + ": " + enemyHealth);
            diamondsFromEnemy = 3;
        }
    }


    public void getPlayerHealthBar(){
        SharedPreferences sharedPreferences = getSharedPreferences("myPref", 0);
        playerHealth = sharedPreferences.getFloat("health", 50.0f);
        maxHealth = sharedPreferences.getFloat("maxHealth", 50.0f);
        if(playerHealth / maxHealth == 100.0f/100.0f){
            health_bar.setImageResource(R.drawable.health_bar_full);
        } else if (playerHealth / maxHealth > 92.0f/100.0f && playerHealth / maxHealth < 100.0f/100.0f){
            health_bar.setImageResource(R.drawable.health_bar11);
        } else if (playerHealth / maxHealth > 84.0f/100.0f && playerHealth / maxHealth <= 92.0f/100.0f){
            health_bar.setImageResource(R.drawable.health_bar10);
        } else if (playerHealth / maxHealth > 76.0f/100.0f && playerHealth / maxHealth < 84.0f/100.0f){
            health_bar.setImageResource(R.drawable.health_bar9);
        } else if (playerHealth / maxHealth > 68.0f/100.0f && playerHealth / maxHealth <= 76.0f/100.0f){
            health_bar.setImageResource(R.drawable.health_bar8);
        } else if (playerHealth / maxHealth > 60.0f/100.0f && playerHealth / maxHealth < 68.0f/100.0f){
            health_bar.setImageResource(R.drawable.health_bar7);
        } else if (playerHealth / maxHealth > 52.0f/100.0f && playerHealth / maxHealth <= 60.0f/100.0f){
            health_bar.setImageResource(R.drawable.health_bar6);
        } else if (playerHealth / maxHealth > 44.0f/100.0f && playerHealth / maxHealth < 52.0f/100.0f){
            health_bar.setImageResource(R.drawable.health_bar5);
        } else if (playerHealth / maxHealth > 36.0f/100.0f && playerHealth / maxHealth <= 44.0f/100.0f){
            health_bar.setImageResource(R.drawable.health_bar4);
        } else if (playerHealth / maxHealth > 28.0f/100.0f && playerHealth / maxHealth < 36.0f/100.0f){
            health_bar.setImageResource(R.drawable.health_bar3);
        } else if (playerHealth / maxHealth > 20.0f/100.0f && playerHealth / maxHealth <= 28.0f/100.0f){
            health_bar.setImageResource(R.drawable.health_bar2);
        } else if (playerHealth / maxHealth > 12.0f/100.0f && playerHealth / maxHealth <= 20.0f/100.0f){
            health_bar.setImageResource(R.drawable.health_bar1);
        } else if (playerHealth / maxHealth > 0.0f/100.0f && playerHealth / maxHealth <= 12.0f/100.0f){
            health_bar.setImageResource(R.drawable.health_bar0);
        }else if (playerHealth / maxHealth <= 0.0f/100.0f){
            health_bar.setImageResource(R.drawable.health_bar_empty);
        }
    }

    public void getEnemyHealthBar(){
        if(enemyHealth / maxEnemyHealth == 100.0f/100.0f){
            enemy_health_bar.setImageResource(R.drawable.health_bar_full);
        } else if (enemyHealth / maxEnemyHealth > 92.0f/100.0f && enemyHealth / maxEnemyHealth < 100.0f/100.0f){
            enemy_health_bar.setImageResource(R.drawable.health_bar11);
        } else if (enemyHealth / maxEnemyHealth > 84.0f/100.0f && enemyHealth / maxEnemyHealth <= 92.0f/100.0f){
            enemy_health_bar.setImageResource(R.drawable.health_bar10);
        } else if (enemyHealth / maxEnemyHealth > 76.0f/100.0f && enemyHealth / maxEnemyHealth < 84.0f/100.0f){
            enemy_health_bar.setImageResource(R.drawable.health_bar9);
        } else if (enemyHealth / maxEnemyHealth > 68.0f/100.0f && enemyHealth / maxEnemyHealth <= 76.0f/100.0f){
            enemy_health_bar.setImageResource(R.drawable.health_bar8);
        } else if (enemyHealth / maxEnemyHealth > 60.0f/100.0f && enemyHealth / maxEnemyHealth < 68.0f/100.0f){
            enemy_health_bar.setImageResource(R.drawable.health_bar7);
        } else if (enemyHealth / maxEnemyHealth > 52.0f/100.0f && enemyHealth / maxEnemyHealth <= 60.0f/100.0f){
            enemy_health_bar.setImageResource(R.drawable.health_bar6);
        } else if (enemyHealth / maxEnemyHealth > 44.0f/100.0f && enemyHealth / maxEnemyHealth < 52.0f/100.0f){
            enemy_health_bar.setImageResource(R.drawable.health_bar5);
        } else if (enemyHealth / maxEnemyHealth > 36.0f/100.0f && enemyHealth / maxEnemyHealth <= 44.0f/100.0f){
            enemy_health_bar.setImageResource(R.drawable.health_bar4);
        } else if (enemyHealth / maxEnemyHealth > 28.0f/100.0f && enemyHealth / maxEnemyHealth < 36.0f/100.0f){
            enemy_health_bar.setImageResource(R.drawable.health_bar3);
        } else if (enemyHealth / maxEnemyHealth > 20.0f/100.0f && enemyHealth / maxEnemyHealth <= 28.0f/100.0f){
            enemy_health_bar.setImageResource(R.drawable.health_bar2);
        } else if (enemyHealth / maxEnemyHealth > 12.0f/100.0f && enemyHealth / maxEnemyHealth <= 20.0f/100.0f){
            enemy_health_bar.setImageResource(R.drawable.health_bar1);
        } else if (enemyHealth / maxEnemyHealth > 0.0f/100.0f && enemyHealth / maxEnemyHealth <= 12.0f/100.0f){
            enemy_health_bar.setImageResource(R.drawable.health_bar0);
        }else if (enemyHealth / maxEnemyHealth <= 0.0f/100.0f){
            enemy_health_bar.setImageResource(R.drawable.health_bar_empty);
        }
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
