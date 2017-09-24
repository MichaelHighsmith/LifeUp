package com.satyrlabs.lifeup;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;


public class PictureSelection extends AppCompatActivity {

    String shirtName;
    float currentShirtBonus;
    ImageView shirt;

    int goldUnlockRequirement;
    float currentShirtBonusUnlocked ;
    boolean requiresGold;
    int diamondUnlockRequirement;
    int currentShirt;

    int currentDiamonds;
    int currentGold;
    float currentHealth;
    float maxHealth;

    Context mContext;


    //drawables for boy
    int[] shirtDrawables = new int[] {
            R.drawable.boy_black_shirt, R.drawable.boy_red_shirt, R.drawable.boy_blue_shirt, R.drawable.boy_green_shirt,
            R.drawable.boy_purple_shirt, R.drawable.boy_orange_shirt, R.drawable.boy_grey_black_striped_shirt, R.drawable.boy_leather_armor,
            R.drawable.boy_green_leather_armor, R.drawable.steel_armor, R.drawable.gold_armor, R.drawable.platinum_armor,
            R.drawable.diamond_armor, R.drawable.samurai_armor, R.drawable.rainbow_armor, R.drawable.blank, R.drawable.hat_wizard_transparent,
            R.drawable.hat_knight_helmet, R.drawable.hat_viking, R.drawable.hat_knight_full, R.drawable.hat_wizard_orange_transparent,
            R.drawable.hat_knight_full_gold, R.drawable.hat_knight_full_platinum, R.drawable.hat_knight_full_diamond, R.drawable.hat_wizard_black_transparent,
            R.drawable.hat_knight_full_samurai, R.drawable.blank
    };

    //drawables for girl
    int[] girlShirtDrawables = new int[] {
            R.drawable.black_tank, R.drawable.red_tank, R.drawable.blue_tank, R.drawable.green_tankkkk, R.drawable.purple_tank,
            R.drawable.orange_tank, R.drawable.tank_grey_black_striped, R.drawable.girl_leather_armor, R.drawable.girl_green_leather_armor,
            R.drawable.steel_armor, R.drawable.gold_armor, R.drawable.platinum_armor, R.drawable.diamond_armor, R.drawable.samurai_armor, R.drawable.rainbow_armor
    };

    public PictureSelection(Context context, int position, ImageView shirt, HealthManager healthManager, GoldManager goldManager){

        this.mContext = context;

        SharedPreferences sharedPreferences = mContext.getSharedPreferences("myPref", 0);
        currentShirtBonus = sharedPreferences.getFloat("shirtHealth", 0.0f);
        maxHealth = sharedPreferences.getFloat("maxHealth", 50.0f);
        maxHealth = maxHealth - currentShirtBonus;
        this.currentGold = sharedPreferences.getInt("gold", 0);
        this.currentDiamonds = sharedPreferences.getInt("diamonds", 0);
        this.currentHealth = sharedPreferences.getFloat("health", 0.0f);

        SharedPreferences currentGender = mContext.getSharedPreferences("currentGender", 0);
        String boyOrGirl;
        if (currentGender.getString("gender", "").equals("girl")){
            boyOrGirl = "girl";
        } else{
            boyOrGirl = "boy";
        }

        this.shirt = shirt;

        changeShirt(position, boyOrGirl, healthManager, goldManager);

    }

    public void changeShirt(int position, String boyOrGirl, HealthManager healthManager, GoldManager goldManager){


        SharedPreferences shirtLocked = mContext.getSharedPreferences("shirtLocked", 0);


        if(boyOrGirl.equals("boy")){
            switch (position){
                case(0):
                    shirtName = "boy_black_shirt";
                    goldUnlockRequirement = 0;
                    currentShirtBonusUnlocked = 0.0f;
                    requiresGold = true;
                    break;
                case(1):
                    shirtName = "boy_red_shirt";
                    goldUnlockRequirement = 0;
                    currentShirtBonusUnlocked = 0.0f;
                    requiresGold = true;
                    break;
                case(2):
                    shirtName = "boy_blue_shirt";
                    goldUnlockRequirement = 0;
                    currentShirtBonusUnlocked = 0.0f;
                    requiresGold = true;
                    break;
                case(3):
                    shirtName = "boy_green_shirt";
                    goldUnlockRequirement = 0;
                    currentShirtBonusUnlocked = 0.0f;
                    requiresGold = true;
                    break;
                case(4):
                    shirtName = "boy_purple_shirt";
                    goldUnlockRequirement = 0;
                    currentShirtBonusUnlocked = 0.0f;
                    requiresGold = true;
                    break;
                case(5):
                    shirtName = "boy_orange_shirt";
                    goldUnlockRequirement = 0;
                    currentShirtBonusUnlocked = 0.0f;
                    requiresGold = true;
                    break;
                case(6):
                    shirtName = "boy_grey_black_striped_shirt";
                    goldUnlockRequirement = 50;
                    currentShirtBonusUnlocked = 3.0f;
                    requiresGold = true;
                    break;
                case(7):
                    shirtName = "boy_leather_armor";
                    goldUnlockRequirement = 100;
                    currentShirtBonusUnlocked = 5.0f;
                    requiresGold = true;
                    break;
                case(8):
                    shirtName = "boy_green_leather_armor";
                    goldUnlockRequirement = 150;
                    currentShirtBonusUnlocked = 10.0f;
                    requiresGold = true;
                    break;
                case(9):
                    shirtName = "steel_armor";
                    goldUnlockRequirement = 300;
                    currentShirtBonusUnlocked = 30.0f;
                    requiresGold = true;
                    break;
                case(10):
                    shirtName = "gold_armor";
                    goldUnlockRequirement = 500;
                    currentShirtBonusUnlocked = 60.0f;
                    requiresGold = true;
                    break;
                case(11):
                    shirtName = "platinum_armor";
                    diamondUnlockRequirement = 3;
                    currentShirtBonusUnlocked = 100.0f;
                    requiresGold = false;
                    break;
                case(12):
                    shirtName = "diamond_armor";
                    diamondUnlockRequirement = 8;
                    currentShirtBonusUnlocked = 200.0f;
                    requiresGold = false;
                    break;
                case(13):
                    shirtName = "samurai_armor";
                    goldUnlockRequirement = 2000;
                    currentShirtBonusUnlocked = 500.0f;
                    requiresGold = true;
                    break;
                case(14):
                    shirtName = "rainbow_armor";
                    diamondUnlockRequirement = 15;
                    currentShirtBonusUnlocked = 1000.0f;
                    requiresGold = false;
                    break;
                default:
                    break;
            }

            if (position <=14){
                if (requiresGold){
                    //Unlock items/tell the user if you cant unlock them yet
                    if (shirtLocked.getBoolean(shirtName, true) && currentGold >= goldUnlockRequirement){
                        shirt.setImageResource(shirtDrawables[position]);
                        currentShirt = position;
                        shirtLocked.edit().putBoolean(shirtName, false).apply();
                        currentShirtBonus = currentShirtBonusUnlocked;
                        currentGold = currentGold - goldUnlockRequirement;
                    } else if (shirtLocked.getBoolean(shirtName, true) && currentGold < goldUnlockRequirement){
                        Toast.makeText(mContext, "Sorry, you need " + goldUnlockRequirement  + "gold to purchase this item", Toast.LENGTH_SHORT).show();
                    } else {
                        shirt.setImageResource(shirtDrawables[position]);
                        currentShirt = position;
                        currentShirtBonus = currentShirtBonusUnlocked;
                    }
                } else {
                    if (shirtLocked.getBoolean(shirtName, true) && currentDiamonds >= diamondUnlockRequirement){
                        shirt.setImageResource(shirtDrawables[position]);
                        currentShirt = position;
                        shirtLocked.edit().putBoolean(shirtName, false).apply();
                        currentShirtBonus = currentShirtBonusUnlocked;
                        currentDiamonds = currentDiamonds - diamondUnlockRequirement;
                    } else if (shirtLocked.getBoolean(shirtName, true) && currentDiamonds < diamondUnlockRequirement){
                        Toast.makeText(mContext, "Sorry, you need " + diamondUnlockRequirement  + " diamonds to purchase this item", Toast.LENGTH_SHORT).show();
                    } else {
                        shirt.setImageResource(shirtDrawables[position]);
                        currentShirt = position;
                        currentShirtBonus = currentShirtBonusUnlocked;
                    }
                }
            }
        } else if (boyOrGirl.equals("girl")){
            switch (position){
                case(0):
                    shirtName = "black_tank";
                    goldUnlockRequirement = 0;
                    currentShirtBonusUnlocked = 0.0f;
                    requiresGold = true;
                case(1):
                    shirtName = "red_tank";
                    goldUnlockRequirement = 0;
                    currentShirtBonusUnlocked = 0.0f;
                    requiresGold = true;
                    break;
                case(2):
                    shirtName = "blue_tank";
                    goldUnlockRequirement = 0;
                    currentShirtBonusUnlocked = 0.0f;
                    requiresGold = true;
                    break;
                case(3):
                    shirtName = "green_tankkkk";
                    goldUnlockRequirement = 0;
                    currentShirtBonusUnlocked = 0.0f;
                    requiresGold = true;
                    break;
                case(4):
                    shirtName = "purple_tank";
                    goldUnlockRequirement = 0;
                    currentShirtBonusUnlocked = 0.0f;
                    requiresGold = true;
                    break;
                case(5):
                    shirtName = "orange_tank";
                    goldUnlockRequirement = 0;
                    currentShirtBonusUnlocked = 0.0f;
                    requiresGold = true;
                    break;
                case(6):
                    shirtName = "tank_grey_black_striped";
                    goldUnlockRequirement = 50;
                    currentShirtBonusUnlocked = 3.0f;
                    requiresGold = true;
                    break;
                case(7):
                    shirtName = "girl_leather_armor";
                    goldUnlockRequirement = 100;
                    currentShirtBonusUnlocked = 5.0f;
                    requiresGold = true;
                    break;
                case(8):
                    shirtName = "girl_green_leather_armor";
                    goldUnlockRequirement = 150;
                    currentShirtBonusUnlocked = 10.0f;
                    requiresGold = true;
                    break;
                case(9):
                    shirtName = "steel_armor";
                    goldUnlockRequirement = 300;
                    currentShirtBonusUnlocked = 30.0f;
                    requiresGold = true;
                    break;
                case(10):
                    shirtName = "gold_armor";
                    goldUnlockRequirement = 500;
                    currentShirtBonusUnlocked = 60.0f;
                    requiresGold = true;
                    break;
                case(11):
                    shirtName = "platinum_armor";
                    diamondUnlockRequirement = 3;
                    currentShirtBonusUnlocked = 100.0f;
                    requiresGold = false;
                    break;
                case(12):
                    shirtName = "diamond_armor";
                    diamondUnlockRequirement = 8;
                    currentShirtBonusUnlocked = 200.0f;
                    requiresGold = false;
                    break;
                case(13):
                    shirtName = "samurai_armor";
                    goldUnlockRequirement = 2000;
                    currentShirtBonusUnlocked = 500.0f;
                    requiresGold = true;
                    break;
                case(14):
                    shirtName = "rainbow_armor";
                    diamondUnlockRequirement = 15;
                    currentShirtBonusUnlocked = 1000.0f;
                    requiresGold = false;
                    break;
                default:
                    break;
            }

            if (position <= 14){
                if (requiresGold){
                    //Unlock items/tell the user if you cant unlock them yet
                    if (shirtLocked.getBoolean(shirtName, true) && currentGold >= goldUnlockRequirement){
                        shirt.setImageResource(girlShirtDrawables[position]);
                        currentShirt = position;
                        shirtLocked.edit().putBoolean(shirtName, false).apply();
                        currentShirtBonus = currentShirtBonusUnlocked;
                        currentGold = currentGold - goldUnlockRequirement;
                    } else if (shirtLocked.getBoolean(shirtName, true) && currentGold < goldUnlockRequirement){
                        Toast.makeText(mContext, "Sorry, you need " + goldUnlockRequirement  + " gold to purchase this item", Toast.LENGTH_SHORT).show();
                    } else {
                        shirt.setImageResource(girlShirtDrawables[position]);
                        currentShirt = position;
                        currentShirtBonus = currentShirtBonusUnlocked;
                    }
                } else {
                    if (shirtLocked.getBoolean(shirtName, true) && currentDiamonds >= diamondUnlockRequirement){
                        shirt.setImageResource(girlShirtDrawables[position]);
                        currentShirt = position;
                        shirtLocked.edit().putBoolean(shirtName, false).apply();
                        currentShirtBonus = currentShirtBonusUnlocked;
                        currentDiamonds = currentDiamonds - diamondUnlockRequirement;
                    } else if (shirtLocked.getBoolean(shirtName, true) && currentDiamonds < diamondUnlockRequirement){
                        Toast.makeText(mContext, "Sorry, you need " + diamondUnlockRequirement  + " diamonds to purchase this item", Toast.LENGTH_SHORT).show();
                    } else {
                        shirt.setImageResource(girlShirtDrawables[position]);
                        currentShirt = position;
                        currentShirtBonus = currentShirtBonusUnlocked;
                    }
                }
            }
        }

        //Store the shirt that was selected in the outfit sharedpreference
        SharedPreferences shirt = mContext.getSharedPreferences("outfit", 0);
        SharedPreferences.Editor shirt_editor = shirt.edit();
        shirt_editor.putInt("currentShirt", currentShirt);
        shirt_editor.apply();


        //apply the new item's bonus health
        maxHealth = maxHealth + currentShirtBonus;

        SharedPreferences sharedPreferences = mContext.getSharedPreferences("myPref", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("gold", currentGold);
        editor.putInt("diamonds", currentDiamonds);
        editor.putFloat("shirtHealth", currentShirtBonus);
        editor.putFloat("maxHealth", maxHealth);
        editor.apply();

        //getHealthBar();
        healthManager.setViews(currentHealth, maxHealth);
        goldManager.setTextViews(currentGold);


    }



}
