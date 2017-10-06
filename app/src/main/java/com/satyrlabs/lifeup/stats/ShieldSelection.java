package com.satyrlabs.lifeup.stats;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.ImageView;
import android.widget.Toast;

import com.satyrlabs.lifeup.R;
import com.satyrlabs.lifeup.stats.GoldManager;


public class ShieldSelection {

    int[] shieldDrawables = new int[] {
            R.drawable.boy_black_shirt, R.drawable.boy_red_shirt, R.drawable.boy_blue_shirt, R.drawable.boy_green_shirt,
            R.drawable.boy_purple_shirt, R.drawable.boy_orange_shirt, R.drawable.boy_grey_black_striped_shirt, R.drawable.boy_leather_armor,
            R.drawable.boy_green_leather_armor, R.drawable.steel_armor, R.drawable.gold_armor, R.drawable.platinum_armor,
            R.drawable.diamond_armor, R.drawable.samurai_armor, R.drawable.rainbow_armor, R.drawable.blank, R.drawable.hat_wizard_transparent,
            R.drawable.hat_knight_helmet, R.drawable.hat_viking, R.drawable.hat_knight_full, R.drawable.hat_wizard_orange_transparent,
            R.drawable.hat_knight_full_gold, R.drawable.hat_knight_full_platinum, R.drawable.hat_knight_full_diamond, R.drawable.hat_wizard_black_transparent,
            R.drawable.hat_knight_full_samurai, R.drawable.blank, R.drawable.blank, R.drawable.shield_white, R.drawable.shield_black, R.drawable.shield_pink,
            R.drawable.shield_red, R.drawable.shield_green, R.drawable.shield_large_star, R.drawable.shield_large_green_lines, R.drawable.shield_large_blue_lightning,
            R.drawable.shield_large_heart, R.drawable.samurai_shield, R.drawable.shield_rainbow
    };

    Context mContext;
    String shieldName;
    ImageView shield;

    int currentDiamonds, currentGold;

    int goldUnlockRequirement, diamondUnlockRequirement;
    int currentShield;
    boolean requiresGold;

    public ShieldSelection(Context context, int position, ImageView shield,
                           GoldManager goldManager){
        this.mContext = context;
        this.shield = shield;
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("myPref", 0);
        this.currentGold = sharedPreferences.getInt("gold", 0);
        this.currentDiamonds = sharedPreferences.getInt("diamonds", 0);

        changeShields(position, goldManager);
    }

    public void changeShields(int position, GoldManager goldManager){
        SharedPreferences shieldLocked = mContext.getSharedPreferences("shieldLocked", 0);
        switch (position){
            case(27):
                shieldName = "blank";
                goldUnlockRequirement = 0;
                requiresGold = true;
                break;
            case(28):
                shieldName = "shield_white";
                goldUnlockRequirement = 150;
                requiresGold = true;
                break;
            case(29):
                shieldName = "shield_black";
                goldUnlockRequirement = 150;
                requiresGold = true;
                break;
            case(30):
                shieldName = "shield_pink";
                goldUnlockRequirement = 150;
                requiresGold = true;
                break;
            case(31):
                shieldName = "shield_red";
                goldUnlockRequirement = 150;
                requiresGold = true;
                break;
            case(32):
                shieldName = "shield_green";
                goldUnlockRequirement = 150;
                requiresGold = true;
                break;
            case(33):
                shieldName = "shield_large_star";
                goldUnlockRequirement = 450;
                requiresGold = true;
                break;
            case(34):
                shieldName = "shield_large_green_lines";
                goldUnlockRequirement = 600;
                requiresGold = true;
                break;
            case(35):
                shieldName = "shield_large_blue_lightning";
                diamondUnlockRequirement = 10;
                requiresGold = false;
                break;
            case(36):
                shieldName = "shield_large_heart";
                goldUnlockRequirement = 1000;
                requiresGold = true;
                break;
            case(37):
                shieldName = "amurai_shield";
                diamondUnlockRequirement = 15;
                requiresGold = false;
                break;
            case(38):
                shieldName = "shield_rainbow";
                diamondUnlockRequirement = 20;
                requiresGold = false;
                break;
            default:
                break;
        }

        if (position > 26 && position <= 38){
            if (requiresGold){
                //Unlock items/tell the user if you cant unlock them yet
                if (shieldLocked.getBoolean(shieldName, true) && currentGold >= goldUnlockRequirement){
                    shield.setImageResource(shieldDrawables[position]);
                    currentShield = position;
                    shieldLocked.edit().putBoolean(shieldName, false).apply();
                    currentGold = currentGold - goldUnlockRequirement;
                } else if (shieldLocked.getBoolean(shieldName, true) && currentGold < goldUnlockRequirement){
                    Toast.makeText(mContext, "Sorry, you need " + goldUnlockRequirement  + " gold to purchase this item", Toast.LENGTH_SHORT).show();
                } else {
                    shield.setImageResource(shieldDrawables[position]);
                    currentShield = position;
                }
            } else {
                if (shieldLocked.getBoolean(shieldName, true) && currentDiamonds >= diamondUnlockRequirement){
                    shield.setImageResource(shieldDrawables[position]);
                    currentShield = position;
                    shieldLocked.edit().putBoolean(shieldName, false).apply();
                    currentDiamonds = currentDiamonds - diamondUnlockRequirement;
                } else if (shieldLocked.getBoolean(shieldName, true) && currentDiamonds < diamondUnlockRequirement){
                    Toast.makeText(mContext, "Sorry, you need " + diamondUnlockRequirement  + " diamonds to purchase this item", Toast.LENGTH_SHORT).show();
                } else {
                    shield.setImageResource(shieldDrawables[position]);
                    currentShield = position;
                }
            }
        }

        SharedPreferences shield = mContext.getSharedPreferences("shield", 0);
        SharedPreferences.Editor shield_editor = shield.edit();
        shield_editor.putInt("currentShield", currentShield);
        shield_editor.apply();

        SharedPreferences sharedPreferences = mContext.getSharedPreferences("myPref", 0);
        SharedPreferences.Editor goldEditor = sharedPreferences.edit();
        goldEditor.putInt("gold", currentGold);
        goldEditor.putInt("diamonds", currentDiamonds);
        goldEditor.apply();

        goldManager.setTextViews(currentGold);

    }

}
