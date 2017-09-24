package com.satyrlabs.lifeup;

import android.app.Dialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.satyrlabs.lifeup.data.RewardDbHelper;
import com.satyrlabs.lifeup.data.RewardsContract;
import com.satyrlabs.lifeup.data.TaskContract;
import com.satyrlabs.lifeup.data.TaskDbHelper;

import java.util.Random;

public class MainActivity extends FragmentActivity implements FirstFragment.OnHeadlineSelectedListener, ThirdFragment.OnHeadlinesSelectedListener, FourthFragment.OnRewardSelectedListener, FifthFragment.OnHealthRestoreListener{

    private AdView mAdView;

    TextView experience_int;
    TextView experience_max_int;
    TextView level_int;
    TextView gold_int;
    TextView player_name;
    TextView diamond_int;
    TextView current_health;
    TextView max_health;

    int currentExperience;
    int maxExperience;
    int currentLevel;
    int currentGold;
    int currentDiamonds;
    int currentHealthInt;
    int currentMaxHealthInt;

    float currentHealth;
    float maxHealth;

    String characterSelection;
    String playerName;

    ImageView character;
    ImageView shirt;
    ImageView hat;
    ImageView weapon;
    ImageView shield;
    ImageView legging;
    ImageView health_bar;

    final String FIRST_TIME =  "MyFirstTime";

    ExpToLevel expToLevel;
    GoldManager goldManager;
    HealthManager healthManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //If it's the user's first time using the app, launch the intro dialog, if not then create the fragments.
        SharedPreferences settings = getSharedPreferences(FIRST_TIME, 0);
        if (settings.getBoolean("my_first_time", true)){
            introDialog();
            settings.edit().putBoolean("my_first_time", false).apply();
        } else{
            ViewPager pager = (ViewPager) findViewById(R.id.pager);
            //Set the adapter
            pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        }
        //instantiate the character image and set it to the correct gender
        character = (ImageView) findViewById(R.id.character);
        SharedPreferences currentGender = getSharedPreferences("currentGender", 0);
        if (currentGender.getString("gender", "").equals("boy")){
            //set the character animation
            character.setBackgroundResource(R.drawable.boy_animation);
            AnimationDrawable frameAnimation = (AnimationDrawable) character.getBackground();
            frameAnimation.start();
        }
        else if (currentGender.getString("gender", "").equals("girl")){
            character.setBackgroundResource(R.drawable.girl_animation);
            AnimationDrawable frameAnimation = (AnimationDrawable) character.getBackground();
            frameAnimation.start();
        }

        experience_int = (TextView) findViewById(R.id.experience_int);
        experience_max_int = (TextView) findViewById(R.id.experience_max_int);
        level_int = (TextView) findViewById(R.id.level_int);
        gold_int = (TextView) findViewById(R.id.gold_int);
        diamond_int = (TextView) findViewById(R.id.diamond_int);
        player_name = (TextView) findViewById(R.id.player_name);
        health_bar = (ImageView) findViewById(R.id.health_bar);
        current_health = (TextView) findViewById(R.id.current_health);
        max_health = (TextView) findViewById(R.id.max_health);

        //Allow the player to change their name whenever they want
        player_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameDialog();
            }
        });

        //Load the stored current experience, gold, diamonds, name, and health as permanent data
        SharedPreferences sharedPreferences = getSharedPreferences("myPref", 0);
        currentGold = sharedPreferences.getInt("gold", 0);
        currentExperience = sharedPreferences.getInt("experience", 1);
        maxExperience = sharedPreferences.getInt("maxExperience", 10);
        currentDiamonds = sharedPreferences.getInt("diamonds", 0);
        playerName = sharedPreferences.getString("name","");
        currentHealth = sharedPreferences.getFloat("health", 50.0f);
        maxHealth = sharedPreferences.getFloat("maxHealth", 50.0f);
        //gold_int.setText(String.valueOf(currentGold));
        diamond_int.setText(String.valueOf(currentDiamonds));

        player_name.setText(playerName);
        currentHealthInt = Math.round(currentHealth);
        currentMaxHealthInt = Math.round(maxHealth);
        if (currentHealthInt <= 0){
            currentHealthInt = 0;
        }

        //Initialize a new experience object (When this object is created it updates the textviews)
        expToLevel = new ExpToLevel(experience_int, experience_max_int, level_int, currentExperience);

        //Initialize a new gold manager object
        goldManager = new GoldManager(gold_int, currentGold);

        healthManager = new HealthManager(health_bar, current_health, max_health, currentHealth, maxHealth);

        //Add a textChangedListener to tell when the level changes.  If it changes, launch level up dialog
        level_int.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                //Show the level up dialog
                levelUpDialog();
            }
        });

        //display the correct outfit that was selected in past uses (first time displays black at index 0)
        shirt = (ImageView) findViewById(R.id.start_shirt);
        SharedPreferences outfit = getSharedPreferences("outfit", 0);
        int selectedShirt = outfit.getInt("currentShirt",0);
        onPictureSelected(selectedShirt);

        //display the correct hat via sharedpreferences (onHatSelected in the 3rd fragment correlates)
        hat = (ImageView) findViewById(R.id.start_hat);
        SharedPreferences hat = getSharedPreferences("hat", 0);
        int selectedHat = hat.getInt("currentHat",0);
        onHatSelected(selectedHat);

        weapon = (ImageView) findViewById(R.id.start_weapon);
        SharedPreferences weapon = getSharedPreferences("weapon", 0);
        int selectedWeapon = weapon.getInt("currentWeapon", 0);
        onWeaponSelected(selectedWeapon);

        shield = (ImageView) findViewById(R.id.start_shield);
        SharedPreferences shield = getSharedPreferences("shield", 0);
        int selectedShield = shield.getInt("currentShield", 0);
        onShieldSelected(selectedShield);

        legging = (ImageView) findViewById(R.id.start_legging);
        SharedPreferences legging = getSharedPreferences("legging", 0);
        int selectedLegging = legging.getInt("currentLegging", 0);
        onLeggingSelected(selectedLegging);
    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    protected void onResume(){
        super.onResume();
        SharedPreferences sharedPref = getSharedPreferences("myPref", 0);
        currentGold = sharedPref.getInt("gold", 0);
        gold_int.setText(String.valueOf(currentGold));
        currentDiamonds = sharedPref.getInt("diamonds", 0);
        diamond_int.setText(String.valueOf(currentDiamonds));
        //getHealthBar();
        healthManager.setViews(currentHealth, maxHealth);
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    //This is called from FirstFragment whenever a list item is clicked.
    @Override
    public void onTaskSelected(int position, long id) {

        TaskDbHelper mDbHelper = new TaskDbHelper(getApplicationContext());
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                TaskContract.TaskEntry._ID,
                TaskContract.TaskEntry.COLUMN_TASK_NAME,
                TaskContract.TaskEntry.COLUMN_TASK_DIFFICULTY,
                TaskContract.TaskEntry.COLUMN_TASK_FREQUENCY
        };

        Cursor cursor = db.query(TaskContract.TaskEntry.TABLE_NAME, projection, null, null, null, null, null);
        cursor.moveToPosition(position);

        int difficultyColumnIndex = cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_TASK_DIFFICULTY);
        int frequencyColumnIndex = cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_TASK_FREQUENCY);

        String currentDifficultyString = cursor.getString(difficultyColumnIndex);
        int currentDifficulty = Integer.parseInt(currentDifficultyString);
        String currentFrequencyString = cursor.getString(frequencyColumnIndex);
        int currentFrequency = Integer.parseInt(currentFrequencyString);

        //Declare boost
        int boost;
        //Give a random amount of experience
        Random random = new Random();

        //depending on the current difficulty, change the boost given
        if (currentDifficulty == TaskContract.TaskEntry.DIFFICULTY_EASY){
            boost = random.nextInt(20);
        } else if (currentDifficulty == TaskContract.TaskEntry.DIFFICULTY_MEDIUM){
            boost = random.nextInt(35);
        } else{
            boost = random.nextInt(50); //DIFFICULTY_HARD
        }

        //Implement sharedPreferences to keep data persistence for level
        SharedPreferences sharedPref = getSharedPreferences("myPref", 0);

        //add Gold
        currentGold = goldManager.addGold(boost);

        expToLevel.addExperience(boost);
        int[] xpValues = expToLevel.getExperienceValues();
        currentExperience = xpValues[0];
        maxExperience = xpValues[1];

        currentHealth = healthManager.gainHealth();
        healthManager.setViews(currentHealth, maxHealth);

        //Store the new gold and experience
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("experience", currentExperience);
        editor.putInt("gold", currentGold);
        editor.putFloat("health", currentHealth);
        editor.putInt("maxExperience", maxExperience);
        editor.apply();

        //If the frequency is set to once, then delete the task after it has been pressed
        if (currentFrequency == 0){
            final Uri uri = ContentUris.withAppendedId(TaskContract.TaskEntry.CONTENT_URI, id);
            getApplicationContext().getContentResolver().delete(uri, null, null);
        }
    }

    //Overriden from FourthFragment when a reward is selected
    @Override
    public void onRewardSelected(int position){

        //Instantiate a RewardDbHelper and database
        RewardDbHelper mDbHelper = new RewardDbHelper(getApplicationContext());
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        //make the projection all columns (will be called by cursor below)
        String[] projection = {
                RewardsContract.RewardsEntry._ID,
                RewardsContract.RewardsEntry.COLUMN_REWARD_NAME,
                RewardsContract.RewardsEntry.COLUMN_REWARD_COST
        };

        //use the helper/database to query the table and * columns.
        Cursor cursor = db.query(RewardsContract.RewardsEntry.TABLE_NAME, projection, null, null, null, null, null);
        //Move the cursor to the position the user clicks
        cursor.moveToPosition(position);
        //Get the column index for cost
        int costColumnIndex = cursor.getColumnIndex(RewardsContract.RewardsEntry.COLUMN_REWARD_COST);
        //use that column index along with the cursor position to get the String value of this rewards cost
        String currentCostString = cursor.getString(costColumnIndex);
        //Convert that string into an int
        int currentCost = Integer.parseInt(currentCostString);

        //Access current Gold
        SharedPreferences sharedPref = getSharedPreferences("myPref", 0);
        currentGold = sharedPref.getInt("gold", 0);

        try{
            currentGold = goldManager.spendReward(currentCost);
            currentHealth = healthManager.loseHealth();

        } catch (RuntimeException e){
            Toast.makeText(this, "You don't have enough gold to do this!", Toast.LENGTH_SHORT).show();
        }


        SharedPreferences.Editor goldEditor = sharedPref.edit();
        goldEditor.putInt("gold", currentGold);
        goldEditor.putFloat("health", currentHealth);
        goldEditor.apply();
    }

    public void onHealthRestoreButtonClicked(){
        SharedPreferences sharedPref = getSharedPreferences("myPref", 0);

        try{
            currentGold = healthManager.healthRestored(currentGold);
        }catch(NullPointerException e){
            Toast.makeText(this, "Sorry, you need more gold.  Complete some tasks!", Toast.LENGTH_SHORT).show();
        }catch(RuntimeException e){
            Toast.makeText(this, "You're already on full health!", Toast.LENGTH_SHORT).show();
        }

        SharedPreferences.Editor goldEditor = sharedPref.edit();
        goldEditor.putInt("gold", currentGold);
        goldEditor.putFloat("health", currentHealth);
        goldEditor.apply();
        goldManager.setTextViews(currentGold);

    }

    //Set up the fragments
    public class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public int getCount(){
            return 4;
        }
        @Override
        public Fragment getItem(int pos){
            switch(pos){
                case 0: return FirstFragment.newInstance("FirstFragment, Instance 1");
                case 1: return ThirdFragment.newInstance("ThirdFragment, Instance 1");  //TODO implement a quest page and put it in the 2nd fragment
                case 2: return FourthFragment.newInstance("FourthFragment, Instance 1");
                //case 3: return FourthFragment.newInstance("FourthFragment, Instance 1");
                case 3: return FifthFragment.newInstance("FifthFragment, Instance 1");
                default: return ThirdFragment.newInstance("ThirdFragment, Default");
            }
        }
    }

    public void levelUpDialog(){

        final Dialog dialog = new Dialog(this);
        //set up the dialog window so that it can display animations
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        RelativeLayout levelView = (RelativeLayout) this.getLayoutInflater().inflate(R.layout.level_up_dialog, null);
        dialog.setContentView(levelView);
        dialog.getWindow().setLayout(800, 1000);
        dialog.setCancelable(false);

        Button button = (Button) dialog.findViewById(R.id.level_up_ok_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentLevel == 2)
                    levelExplanationDialog();
                dialog.dismiss();
            }
        });
        //display the stars animation
        ImageView stars = (ImageView) levelView.findViewById(R.id.stars);
        stars.setBackgroundResource(R.drawable.level_up_animation);
        final AnimationDrawable levelAnimation = (AnimationDrawable) stars.getBackground();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                levelAnimation.start();
            }
        });
        dialog.show();

        SharedPreferences sharedPreferences = getSharedPreferences("myPref", 0);
        maxHealth = healthManager.upMaxHealth(maxHealth);
        currentHealth = maxHealth;
        SharedPreferences.Editor healthEditor = sharedPreferences.edit();
        healthEditor.putFloat("maxHealth", maxHealth);
        healthEditor.apply();
        healthEditor.putFloat("health", currentHealth);
        healthEditor.apply();
        currentHealthInt = Math.round(currentHealth);
        currentMaxHealthInt = Math.round(maxHealth);
        current_health.setText(String.valueOf(currentHealthInt));
        max_health.setText(String.valueOf(currentMaxHealthInt));
        //getHealthBar();
        healthManager.setViews(currentHealth, maxHealth);

    }

    public void levelExplanationDialog(){
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.levelup_explanation_dialog);
        dialog.setTitle("Leveling Up");
        dialog.setCancelable(false);

        Button button= (Button) dialog.findViewById(R.id.level_up_explanation_button);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                swipeRightForOutfitsDialog();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void swipeRightForOutfitsDialog(){
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.swipe_right_dialog);
        dialog.setTitle("Swipe Right");
        dialog.setCancelable(false);

        Button button= (Button) dialog.findViewById(R.id.right_arrow_ok_button);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                dialog.dismiss();
            }
        });
        final ImageView arrow = (ImageView) dialog.findViewById(R.id.right_arrow);
        arrow.setBackgroundResource(R.drawable.arrow_right_anim);
        AnimationDrawable arrowAnimation = (AnimationDrawable) arrow.getBackground();
        arrowAnimation.start();

        dialog.show();
    }

    public void introDialog(){
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.intro_dialog);
        dialog.setTitle("Welcome to LifeUp");
        dialog.setCancelable(false);

        //Set the no_gender text
        final TextView no_gender = (TextView) dialog.findViewById(R.id.no_gender_text);
        //ends the intro dialog
        final Button button = (Button) dialog.findViewById(R.id.intro_ok_button);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                dialog.dismiss();
                nameDialog();
                //Stores the gender that was selected
                SharedPreferences currentGender = getSharedPreferences("currentGender", 0);
                SharedPreferences.Editor editor = currentGender.edit();
                editor.putString("gender", characterSelection);
                editor.apply();

                //put in the first health bar
                SharedPreferences currentHealth = getSharedPreferences("myPref", 0);
                SharedPreferences.Editor healthEditor = currentHealth.edit();
                healthEditor.putFloat("health", 50.0f);
                healthEditor.apply();
                //On the first time using the app we make the fragments here.  Every other time it's done in onCreate.  This is so that the 2nd fragment shows up immediately.
                ViewPager pager = (ViewPager) findViewById(R.id.pager);
                //Set the adapter
                pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
            }
        });

        //sets the initial characteristics to boy
        ImageView boy_image = (ImageView) dialog.findViewById(R.id.intro_boy_selection);
        boy_image.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                character.setBackgroundResource(R.drawable.boy_animation);
                AnimationDrawable frameAnimation = (AnimationDrawable) character.getBackground();
                frameAnimation.start();
                no_gender.setVisibility(View.INVISIBLE);
                button.setVisibility(View.VISIBLE);
                //set the initial character selection variable that is used to create the outfit gridview
                characterSelection = "boy";
            }
        });
        //sets the initial characteristics to girl
        ImageView girl_image = (ImageView) dialog.findViewById(R.id.intro_girl_selection);
        girl_image.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                character.setBackgroundResource(R.drawable.girl_animation);
                AnimationDrawable frameAnimation = (AnimationDrawable) character.getBackground();
                frameAnimation.start();
                no_gender.setVisibility(View.INVISIBLE);
                button.setVisibility(View.VISIBLE);
                //set the initial character selection variable that is used to create the outfit gridview
                characterSelection = "girl";
            }
        });
        dialog.show();
    }

    public void nameDialog(){
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.name_dialog);
        dialog.setTitle("Name Selection");
        dialog.setCancelable(false);

        final EditText nameText = (EditText) dialog.findViewById(R.id.name_selection_edit_text);

        final Button button = (Button) dialog.findViewById(R.id.name_selection_button);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                dialog.dismiss();
                SharedPreferences nameSettings = getSharedPreferences("MyFirstName", 0);
                if(nameSettings.getBoolean("my_first_name", true)){
                    taskDialog();
                    nameSettings.edit().putBoolean("my_first_name", false).apply();
                }
                final String currentName = nameText.getText().toString();
                SharedPreferences namePref = getSharedPreferences("myPref", 0);
                SharedPreferences.Editor nameEditor = namePref.edit();
                nameEditor.putString("name", currentName);
                nameEditor.apply();
                player_name.setText(currentName);

                if(namePref.getString("name", "").isEmpty()){
                    nameDialog();
                }
            }
        });
        dialog.show();
    }

    public void taskDialog(){
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.intro_task_dialog);
        dialog.setTitle("Task Selection");
        dialog.setCancelable(false);

        final Button button = (Button) dialog.findViewById(R.id.intro_task_ok_button);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                dialog.dismiss();
            }
        });
        //Set the dialog box to the bottom of the screen
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM | Gravity.LEFT;

        dialog.show();
    }

    //Called whenever a picture is clicked in the images fragment
    @Override
    public void onPictureSelected(int position){
        new PictureSelection(this, position,  shirt, healthManager, goldManager);
    }

    @Override
    public void onHatSelected(int position){
        new HatSelection(this, position, hat, healthManager, goldManager);
    }

    @Override
    public void onWeaponSelected(int position){
        new WeaponSelection(this, position, weapon, goldManager);
    }

    @Override
    public void onShieldSelected(int position){
        new ShieldSelection(this, position, shield, goldManager);
    }

    @Override
    public void onLeggingSelected(int position){
        new LeggingSelection(this, position, legging, healthManager, goldManager);

    }
}