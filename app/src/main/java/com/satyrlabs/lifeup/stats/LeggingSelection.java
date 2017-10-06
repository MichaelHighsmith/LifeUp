package com.satyrlabs.lifeup.stats;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.ImageView;
import android.widget.Toast;

import com.satyrlabs.lifeup.R;
import com.satyrlabs.lifeup.stats.GoldManager;
import com.satyrlabs.lifeup.stats.HealthManager;


public class LeggingSelection {

    Context mContext;
    String leggingName;
    float currentLeggingBonus;
    float currentLeggingBonusUnlocked;
    ImageView legging;

    int goldUnlockRequirement;
    int diamondUnlockRequirement;
    int currentLegging;
    int currentGold;
    int currentDiamonds;
    float currentHealth;
    float maxHealth;
    boolean requiresGold;

    int[] leggingDrawables = new int[] {
            R.drawable.boy_black_shirt, R.drawable.boy_red_shirt, R.drawable.boy_blue_shirt, R.drawable.boy_green_shirt,
            R.drawable.boy_purple_shirt, R.drawable.boy_orange_shirt, R.drawable.boy_grey_black_striped_shirt, R.drawable.boy_leather_armor,
            R.drawable.boy_green_leather_armor, R.drawable.steel_armor, R.drawable.gold_armor, R.drawable.platinum_armor,
            R.drawable.diamond_armor, R.drawable.samurai_armor, R.drawable.rainbow_armor, R.drawable.blank, R.drawable.hat_wizard_transparent,
            R.drawable.hat_knight_helmet, R.drawable.hat_viking, R.drawable.hat_knight_full, R.drawable.hat_wizard_orange_transparent,
            R.drawable.hat_knight_full_gold, R.drawable.hat_knight_full_platinum, R.drawable.hat_knight_full_diamond, R.drawable.hat_wizard_black_transparent,
            R.drawable.hat_knight_full_samurai, R.drawable.blank, R.drawable.blank, R.drawable.shield_white, R.drawable.shield_black, R.drawable.shield_pink,
            R.drawable.shield_red, R.drawable.shield_green, R.drawable.shield_large_star, R.drawable.shield_large_green_lines, R.drawable.shield_large_blue_lightning,
            R.drawable.shield_large_heart, R.drawable.samurai_shield, R.drawable.shield_rainbow, R.drawable.leggings_steel, R.drawable.leggings_gold, R.drawable.leggings_platinum,
            R.drawable.leggings_diamond, R.drawable.leggings_samurai, R.drawable.leggings_rainbow
    };

    public LeggingSelection(Context context, int position, ImageView legging, HealthManager healthManager, GoldManager goldManager){

        this.mContext = context;
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("myPref", 0);
        currentLeggingBonus = sharedPreferences.getFloat("leggingHealth", 0.0f);
        maxHealth = sharedPreferences.getFloat("maxHealth", 50.0f);
        maxHealth = maxHealth - currentLeggingBonus;

        this.legging = legging;
        this.currentGold = sharedPreferences.getInt("gold", 0);
        this.currentDiamonds = sharedPreferences.getInt("diamonds", 0);
        this.currentHealth = sharedPreferences.getFloat("health", 0.0f);

        changeLegging(position, healthManager, goldManager);
    }

    public void changeLegging(int position, HealthManager healthManager, GoldManager goldManager){
        SharedPreferences leggingLocked = mContext.getSharedPreferences("leggingLocked", 0);

        switch (position){
            case(39):
                leggingName = "leggings_steel";
                goldUnlockRequirement = 50;
                currentLeggingBonusUnlocked = 20.0f;
                requiresGold = true;
                break;
            case(40):
                leggingName = "leggings_gold";
                goldUnlockRequirement = 100;
                currentLeggingBonusUnlocked = 45.0f;
                requiresGold = true;
                break;
            case(41):
                leggingName = "leggings_platinum";
                goldUnlockRequirement = 200;
                currentLeggingBonusUnlocked = 88.0f;
                requiresGold = true;
                break;
            case(42):
                leggingName = "leggings_diamond";
                goldUnlockRequirement = 325;
                currentLeggingBonusUnlocked = 122.0f;
                requiresGold = true;
                break;
            case(43):
                leggingName = "leggings_samurai";
                goldUnlockRequirement = 450;
                currentLeggingBonusUnlocked = 158.0f;
                requiresGold = true;
                break;
            case(44):
                leggingName = "leggings_rainbow";
                goldUnlockRequirement = 600;
                currentLeggingBonusUnlocked = 222.0f;
                requiresGold = true;
                break;
            default:
                break;
        }

        if (position > 38 && position <= 44){
            if (requiresGold){
                //Unlock items/tell the user if you cant unlock them yet
                if (leggingLocked.getBoolean(leggingName, true) && currentGold >= goldUnlockRequirement){
                    legging.setImageResource(leggingDrawables[position]);
                    currentLegging = position;
                    leggingLocked.edit().putBoolean(leggingName, false).apply();
                    currentLeggingBonus = currentLeggingBonusUnlocked;
                    currentGold = currentGold - goldUnlockRequirement;
                } else if (leggingLocked.getBoolean(leggingName, true) && currentGold < goldUnlockRequirement){
                    Toast.makeText(mContext, "Sorry, you need " + goldUnlockRequirement  + " gold to purchase this item", Toast.LENGTH_SHORT).show();
                } else {
                    legging.setImageResource(leggingDrawables[position]);
                    currentLeggingBonus = currentLeggingBonusUnlocked;
                    currentLegging = position;
                }
            } else {
                if (leggingLocked.getBoolean(leggingName, true) && currentDiamonds >= diamondUnlockRequirement){
                    legging.setImageResource(leggingDrawables[position]);
                    currentLegging = position;
                    leggingLocked.edit().putBoolean(leggingName, false).apply();
                    currentLeggingBonus = currentLeggingBonusUnlocked;
                    currentDiamonds = currentDiamonds - diamondUnlockRequirement;
                } else if (leggingLocked.getBoolean(leggingName, true) && currentDiamonds < diamondUnlockRequirement){
                    Toast.makeText(mContext, "Sorry, you need " + diamondUnlockRequirement  + " diamonds to purchase this item", Toast.LENGTH_SHORT).show();
                } else {
                    legging.setImageResource(leggingDrawables[position]);
                    currentLeggingBonus = currentLeggingBonusUnlocked;
                    currentLegging = position;
                }
            }
        }
        SharedPreferences legging = mContext.getSharedPreferences("legging", 0);
        SharedPreferences.Editor legging_editor = legging.edit();
        legging_editor.putInt("currentLegging", currentLegging);
        legging_editor.apply();

        //apply the new item's bonus health
        maxHealth = maxHealth + currentLeggingBonus;

        SharedPreferences sharedPreferences = mContext.getSharedPreferences("myPref", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("gold", currentGold);
        editor.putInt("diamonds", currentDiamonds);
        editor.putFloat("leggingHealth", currentLeggingBonus);
        editor.putFloat("maxHealth", maxHealth);
        editor.apply();

        healthManager.setViews(currentHealth, maxHealth);
        goldManager.setTextViews(currentGold);

    }
}
