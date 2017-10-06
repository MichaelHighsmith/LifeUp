package com.satyrlabs.lifeup.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.satyrlabs.lifeup.R;


//ImageAdapter for the boy selection
public class ImageAdapter extends BaseAdapter {

    private Context mContext;


    public ImageAdapter(Context c){
        mContext = c;
    }

    public int getCount(){
        return mThumbIds.length;
    }

    public Object getItem(int position){
        return null;
    }

    public long getItemId(int position){
        return 0;
    }

    //create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent){
        ImageView imageView;
        if (convertView == null){
            //if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(350, 350));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(0, 0, 0, 0);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setBackgroundResource(mThumbIds[position]);
        return imageView;
    }



    private Integer[] mThumbIds = {
            R.drawable.boy_black_shirt_prepurchase,
            R.drawable.boy_red_shirt_prepurchase,
            R.drawable.boy_blue_shirt_prepurchase,
            R.drawable.boy_green_shirt_prepurchase,
            R.drawable.boy_purple_shirt_prepurchase,
            R.drawable.boy_orange_shirt_prepurchase,
            R.drawable.boy_grey_black_striped_shirt_prepurchase,
            R.drawable.boy_leather_armor_prepurchase,
            R.drawable.boy_green_leather_armor_prepurchase,
            R.drawable.steel_armor_prepurchase,
            R.drawable.gold_armor_prepurchase,
            R.drawable.platinum_armor_prepurchase,
            R.drawable.diamond_armor_prepurchase,
            R.drawable.samurai_armor_prepurchase,
            R.drawable.rainbow_armor_prepurchase,
            R.drawable.no_hat,
            R.drawable.hat_wizard_prepurchase,
            R.drawable.hat_knight_helmet_prepurchase,
            R.drawable.hat_viking_prepurchase,
            R.drawable.hat_knight_full_prepurchase,
            R.drawable.hat_wizard_orange_prepurchase,
            R.drawable.hat_knight_full_gold_prepurchase,
            R.drawable.hat_knight_full_platinum_prepurchase,
            R.drawable.hat_knight_full_diamond_prepurchase,
            R.drawable.hat_wizard_black_prepurchase,
            R.drawable.hat_knight_full_samurai_prepurchase,
            R.drawable.blank,
            R.drawable.no_shield,
            R.drawable.shield_white_prepurchase,
            R.drawable.shield_black_prepurchase,
            R.drawable.shield_pink_prepurchase,
            R.drawable.shield_red_prepurchase,
            R.drawable.shield_green_prepurchase,
            R.drawable.shield_large_star_prepurchase,
            R.drawable.shield_large_green_lines_prepurchase,
            R.drawable.shield_large_blue_lightning_prepurchase,
            R.drawable.shield_large_heart_prepurchase,
            R.drawable.samurai_shield_prepurchase,
            R.drawable.shield_rainbow_prepurchase,
            R.drawable.leggings_steel_prepurchase,
            R.drawable.leggings_gold_prepurchase,
            R.drawable.leggings_platinum_prepurchase,
            R.drawable.leggings_diamond_prepurchase,
            R.drawable.leggings_samurai_prepurchase,
            R.drawable.leggings_rainbow_prepurchase,
            R.drawable.no_weapon,
            R.drawable.short_sword_prepurchase,
            R.drawable.long_sword_prepurchase,
            R.drawable.short_axe_prepurchase,
            R.drawable.axe_prepurchase,
            R.drawable.baseball_bat_prepurchase,
            R.drawable.shovel_prepurchase,
            R.drawable.fencing_sword_prepurchase,
            R.drawable.spiked_baseball_bat_prepurchase,
            R.drawable.schimitar_prepurchase,
            R.drawable.sword_edged_orange_blue_prepurchase,
            R.drawable.spiked_sword_prepurchase,
            R.drawable.sword_glowing_blue_prepurchase,
            R.drawable.honeycomb_sword_prepurchase,
            R.drawable.sword_edged_orange_blue_double_prepurchase

    };
}
