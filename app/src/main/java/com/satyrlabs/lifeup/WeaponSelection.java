package com.satyrlabs.lifeup;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by mhigh on 9/24/2017.
 */

public class WeaponSelection {

    int[] weaponDrawables = new int[] {
            R.drawable.boy_black_shirt, R.drawable.boy_red_shirt, R.drawable.boy_blue_shirt, R.drawable.boy_green_shirt,
            R.drawable.boy_purple_shirt, R.drawable.boy_orange_shirt, R.drawable.boy_grey_black_striped_shirt, R.drawable.boy_leather_armor,
            R.drawable.boy_green_leather_armor, R.drawable.steel_armor, R.drawable.gold_armor, R.drawable.platinum_armor,
            R.drawable.diamond_armor, R.drawable.samurai_armor, R.drawable.rainbow_armor, R.drawable.blank, R.drawable.hat_wizard_transparent,
            R.drawable.hat_knight_helmet, R.drawable.hat_viking, R.drawable.hat_knight_full, R.drawable.hat_wizard_orange_transparent,
            R.drawable.hat_knight_full_gold, R.drawable.hat_knight_full_platinum, R.drawable.hat_knight_full_diamond, R.drawable.hat_wizard_black_transparent,
            R.drawable.hat_knight_full_samurai, R.drawable.blank, R.drawable.blank, R.drawable.shield_white, R.drawable.shield_black, R.drawable.shield_pink,
            R.drawable.shield_red, R.drawable.shield_green, R.drawable.shield_large_star, R.drawable.shield_large_green_lines, R.drawable.shield_large_blue_lightning,
            R.drawable.shield_large_heart, R.drawable.samurai_shield, R.drawable.shield_rainbow, R.drawable.leggings_steel, R.drawable.leggings_gold, R.drawable.leggings_platinum,
            R.drawable.leggings_diamond, R.drawable.leggings_samurai, R.drawable.leggings_rainbow, R.drawable.blank, R.drawable.short_sword,
            R.drawable.long_sword, R.drawable.short_axe, R.drawable.axe, R.drawable.baseball_bat, R.drawable.shovel, R.drawable.fencing_sword,
            R.drawable.spiked_baseball_bat, R.drawable.schimitar, R.drawable.sword_edged_orange_blue, R.drawable.spiked_sword, R.drawable.sword_glowing_blue,
            R.drawable.honeycomb_sword, R.drawable.sword_edged_orange_blue_double
    };

    Context mContext;
    String weaponName;

    ImageView weapon;

    int goldUnlockRequirement;
    int diamondUnlockRequirement;
    int currentWeapon;
    int currentGold;
    int currentDiamonds;
    boolean requiresGold;

    public WeaponSelection(Context context, int position, ImageView weapon,
                           GoldManager goldManager){
        this.mContext = context;
        this.weapon = weapon;
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("myPref", 0);
        this.currentGold = sharedPreferences.getInt("gold", 0);
        this.currentDiamonds = sharedPreferences.getInt("diamonds", 0);

        changeWeapons(position, goldManager);

    }

    public void changeWeapons(int position, GoldManager goldManager){
        SharedPreferences weaponLocked = mContext.getSharedPreferences("weaponLocked", 0);

        switch (position){
            case(45):
                weaponName = "blank";
                goldUnlockRequirement = 0;
                requiresGold = true;
                break;
            case(46):
                weaponName = "short_sword";
                goldUnlockRequirement = 50;
                requiresGold = true;
                break;
            case(47):
                weaponName = "long_sword";
                goldUnlockRequirement = 100;
                requiresGold = true;
                break;
            case(48):
                weaponName = "short_axe";
                goldUnlockRequirement = 175;
                requiresGold = true;
                break;
            case(49):
                weaponName = "axe";
                goldUnlockRequirement = 250;
                requiresGold = true;
                break;
            case(50):
                weaponName = "baseball_bat";
                goldUnlockRequirement = 300;
                requiresGold = true;
                break;
            case(51):
                weaponName = "shovel";
                goldUnlockRequirement = 430;
                requiresGold = true;
                break;
            case(52):
                weaponName = "fencing_sword";
                goldUnlockRequirement = 500;
                requiresGold = true;
                break;
            case(53):
                weaponName = "spiked_baseball_bat";
                goldUnlockRequirement = 575;
                requiresGold = true;
                break;
            case(54):
                weaponName = "schimitar";
                goldUnlockRequirement = 700;
                requiresGold = true;
                break;
            case(55):
                weaponName = "sword_edged_orange_blue";
                diamondUnlockRequirement = 10;
                requiresGold = false;
                break;
            case(56):
                weaponName = "spiked_sword";
                diamondUnlockRequirement = 15;
                requiresGold = false;
                break;
            case(57):
                weaponName = "sword_glowing_blue";
                diamondUnlockRequirement = 20;
                requiresGold = false;
                break;
            case(58):
                weaponName = "honeycomb_sword";
                goldUnlockRequirement = 2000;
                requiresGold = true;
                break;
            case(59):
                weaponName = "sword_edged_orange_blue_double";
                diamondUnlockRequirement = 25;
                requiresGold = false;
                break;
            default:
                break;
        }

        if (position > 44 && position <= 59){
            if (requiresGold){
                //Unlock items/tell the user if you cant unlock them yet
                if (weaponLocked.getBoolean(weaponName, true) && currentGold >= goldUnlockRequirement){
                    weapon.setImageResource(weaponDrawables[position]);
                    currentWeapon = position;
                    weaponLocked.edit().putBoolean(weaponName, false).apply();
                    currentGold = currentGold - goldUnlockRequirement;
                } else if (weaponLocked.getBoolean(weaponName, true) && currentGold < goldUnlockRequirement){
                    Toast.makeText(mContext, "Sorry, you need " + goldUnlockRequirement  + " gold to purchase this item", Toast.LENGTH_SHORT).show();
                } else {
                    weapon.setImageResource(weaponDrawables[position]);
                    currentWeapon = position;
                }
            } else {
                if (weaponLocked.getBoolean(weaponName, true) && currentDiamonds >= diamondUnlockRequirement){
                    weapon.setImageResource(weaponDrawables[position]);
                    currentWeapon = position;
                    weaponLocked.edit().putBoolean(weaponName, false).apply();
                    currentDiamonds = currentDiamonds - diamondUnlockRequirement;
                } else if (weaponLocked.getBoolean(weaponName, true) && currentDiamonds < diamondUnlockRequirement){
                    Toast.makeText(mContext, "Sorry, you need " + diamondUnlockRequirement  + " diamonds to purchase this item", Toast.LENGTH_SHORT).show();
                } else {
                    weapon.setImageResource(weaponDrawables[position]);
                    currentWeapon = position;
                }
            }
        }
        SharedPreferences weapon = mContext.getSharedPreferences("weapon", 0);
        SharedPreferences.Editor weapon_editor = weapon.edit();
        weapon_editor.putInt("currentWeapon", currentWeapon);
        weapon_editor.apply();

        SharedPreferences sharedPreferences = mContext.getSharedPreferences("myPref", 0);
        SharedPreferences.Editor goldEditor = sharedPreferences.edit();
        goldEditor.putInt("gold", currentGold);
        goldEditor.putInt("diamonds", currentDiamonds);
        goldEditor.apply();

        goldManager.setTextViews(currentGold);

    }
}
