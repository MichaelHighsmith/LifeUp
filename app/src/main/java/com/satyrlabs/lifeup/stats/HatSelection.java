package com.satyrlabs.lifeup.stats;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.satyrlabs.lifeup.R;
import com.satyrlabs.lifeup.stats.GoldManager;
import com.satyrlabs.lifeup.stats.HealthManager;

public class HatSelection extends AppCompatActivity {

    Context mContext;
    String hatName;
    float currentHatBonus;
    float currentHatBonusUnlocked;
    ImageView hat;

    int goldUnlockRequirement;
    int diamondUnlockRequirement;
    int currentHat;
    int currentGold;
    int currentDiamonds;
    float currentHealth;
    float maxHealth;

    boolean requiresGold;

    int[] hatDrawables = new int[] {
            R.drawable.boy_black_shirt, R.drawable.boy_red_shirt, R.drawable.boy_blue_shirt, R.drawable.boy_green_shirt,
            R.drawable.boy_purple_shirt, R.drawable.boy_orange_shirt, R.drawable.boy_grey_black_striped_shirt, R.drawable.boy_leather_armor,
            R.drawable.boy_green_leather_armor, R.drawable.steel_armor, R.drawable.gold_armor, R.drawable.platinum_armor,
            R.drawable.diamond_armor, R.drawable.samurai_armor, R.drawable.rainbow_armor, R.drawable.blank, R.drawable.hat_wizard_transparent,
            R.drawable.hat_knight_helmet, R.drawable.hat_viking, R.drawable.hat_knight_full, R.drawable.hat_wizard_orange_transparent,
            R.drawable.hat_knight_full_gold, R.drawable.hat_knight_full_platinum, R.drawable.hat_knight_full_diamond, R.drawable.hat_wizard_black_transparent,
            R.drawable.hat_knight_full_samurai, R.drawable.blank
    };

    public HatSelection(Context context, int position, ImageView hat, HealthManager healthManager, GoldManager goldManager){

        this.mContext = context;
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("myPref", 0);
        currentHatBonus = sharedPreferences.getFloat("hatHealth", 0.0f);
        maxHealth = sharedPreferences.getFloat("maxHealth", 50.0f);
        maxHealth = maxHealth - currentHatBonus;

        this.hat = hat;
        this.currentGold = sharedPreferences.getInt("gold", 0);
        this.currentDiamonds = sharedPreferences.getInt("diamonds", 0);
        this.currentHealth = sharedPreferences.getFloat("health", 0.0f);

        changeHat(position, healthManager, goldManager);

    }

    public void changeHat(int position, HealthManager healthManager, GoldManager goldManager){

        SharedPreferences hatLocked = mContext.getSharedPreferences("hatLocked", 0);
        switch (position){
            case(15):
                hatName = "blank";
                goldUnlockRequirement = 0;
                currentHatBonusUnlocked = 0.0f;
                requiresGold = true;
                break;
            case(16):
                hatName = "hat_wizard_transparent";
                goldUnlockRequirement = 100;
                currentHatBonusUnlocked = 8.0f;
                requiresGold = true;
                break;
            case(17):
                hatName = "hat_knight_helmet";
                goldUnlockRequirement = 175;
                currentHatBonusUnlocked = 14.0f;
                requiresGold = true;
                break;
            case(18):
                hatName = "hat_viking";
                goldUnlockRequirement = 250;
                currentHatBonusUnlocked = 22.0f;
                requiresGold = true;
                break;
            case(19):
                hatName = "hat_knight_full";
                goldUnlockRequirement = 400;
                currentHatBonusUnlocked = 30.0f;
                requiresGold = true;
                break;
            case(20):
                hatName = "hat_wizard_orange_transparent";
                goldUnlockRequirement = 500;
                currentHatBonusUnlocked = 38.0f;
                requiresGold = true;
                break;
            case(21):
                hatName = "hat_knight_full_gold";
                goldUnlockRequirement = 750;
                currentHatBonusUnlocked = 52.0f;
                requiresGold = true;
                break;
            case(22):
                hatName = "hat_knight_full_platinum";
                goldUnlockRequirement = 1000;
                currentHatBonusUnlocked = 88.0f;
                requiresGold = true;
                break;
            case(23):
                hatName = "hat_knight_full_diamond";
                diamondUnlockRequirement = 10;
                currentHatBonusUnlocked = 120.0f;
                requiresGold = false;
                break;
            case(24):
                hatName = "hat_wizard_black_transparent";
                diamondUnlockRequirement = 15;
                currentHatBonusUnlocked = 40.0f;
                requiresGold = false;
                break;
            case(25):
                hatName = "hat_knight_full_samurai";
                diamondUnlockRequirement = 25;
                currentHatBonusUnlocked = 144.0f;
                requiresGold = false;
                break;
            case(26):
                hatName = "blank";
                goldUnlockRequirement = 0;
                currentHatBonusUnlocked = 0.0f;
                requiresGold = true;
                break;
            default:
                break;
        }

        if (position > 14 && position <= 26){
            if (requiresGold){
                //Unlock items/tell the user if you cant unlock them yet
                if (hatLocked.getBoolean(hatName, true) && currentGold >= goldUnlockRequirement){
                    hat.setImageResource(hatDrawables[position]);
                    currentHat = position;
                    hatLocked.edit().putBoolean(hatName, false).apply();
                    currentHatBonus = currentHatBonusUnlocked;
                    currentGold = currentGold - goldUnlockRequirement;
                } else if (hatLocked.getBoolean(hatName, true) && currentGold < goldUnlockRequirement){
                    Toast.makeText(mContext, "Sorry, you need " + goldUnlockRequirement  + " gold to purchase this item", Toast.LENGTH_SHORT).show();
                } else {
                    hat.setImageResource(hatDrawables[position]);
                    currentHat = position;
                    currentHatBonus = currentHatBonusUnlocked;
                }
            } else {
                if (hatLocked.getBoolean(hatName, true) && currentDiamonds >= diamondUnlockRequirement){
                    hat.setImageResource(hatDrawables[position]);
                    currentHat = position;
                    hatLocked.edit().putBoolean(hatName, false).apply();
                    currentHatBonus = currentHatBonusUnlocked;
                    currentDiamonds = currentDiamonds - diamondUnlockRequirement;
                } else if (hatLocked.getBoolean(hatName, true) && currentDiamonds < diamondUnlockRequirement){
                    Toast.makeText(mContext, "Sorry, you need " + diamondUnlockRequirement  + " diamonds to purchase this item", Toast.LENGTH_SHORT).show();
                } else {
                    hat.setImageResource(hatDrawables[position]);
                    currentHat = position;
                    currentHatBonus = currentHatBonusUnlocked;
                }
            }
        }

        SharedPreferences hat = mContext.getSharedPreferences("hat", 0);
        SharedPreferences.Editor hat_editor = hat.edit();
        hat_editor.putInt("currentHat", currentHat);
        hat_editor.apply();

        //apply the new item's bonus health
        maxHealth = maxHealth + currentHatBonus;

        SharedPreferences sharedPreferences = mContext.getSharedPreferences("myPref", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("gold", currentGold);
        editor.putInt("diamonds", currentDiamonds);
        editor.putFloat("hatHealth", currentHatBonus);
        editor.putFloat("maxHealth", maxHealth);
        editor.apply();

        healthManager.setViews(currentHealth, maxHealth);
        goldManager.setTextViews(currentGold);

    }
}
