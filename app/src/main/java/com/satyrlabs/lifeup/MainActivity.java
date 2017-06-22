package com.satyrlabs.lifeup;

import android.app.Dialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
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

public class MainActivity extends FragmentActivity implements FirstFragment.OnHeadlineSelectedListener, ThirdFragment.OnHeadlinesSelectedListener, FourthFragment.OnRewardSelectedListener{

    //int experience;
    //MyPageAdapter pageAdapter;

    private AdView mAdView;

    TextView experience_int;
    TextView level_int;
    TextView gold_int;
    TextView player_name;
    TextView diamond_int;
    TextView current_health;
    TextView max_health;

    int currentExperience;
    int experience;
    int currentLevel;
    int gold;
    int currentGold;
    int goldPrice;
    int currentDiamonds;
    int currentHealthInt;
    int currentMaxHealthInt;

    float health;
    float currentHealth;
    float maxHealth;
    float currentShirtBonus;
    float currentHatBonus;
    float currentLeggingBonus;

    float currentShirtBonusUnlocked, currentHatBonusUnlocked, currentLeggingBonusUnlocked;

    int currentHat;
    int currentShirt;
    int currentWeapon;
    int currentShield;
    int currentLegging;

    String characterSelection;
    String playerName;

    String bought_shirt;
    String shirtName;
    String hatName;
    String shieldName;
    String leggingName;
    String weaponName;

    int goldUnlockRequirement, diamondUnlockRequirement;
    boolean requiresGold;

    ImageView character;
    ImageView shirt;
    ImageView hat;
    ImageView weapon;
    ImageView shield;
    ImageView legging;
    ImageView health_bar;

    final String FIRST_TIME =  "MyFirstTime";
    final String FIRST_TASK_ADDED = "FirstTaskAdded";

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
        currentDiamonds = sharedPreferences.getInt("diamonds", 0);
        playerName = sharedPreferences.getString("name","");
        currentHealth = sharedPreferences.getFloat("health", 50.0f);
        maxHealth = sharedPreferences.getFloat("maxHealth", 50.0f);
        gold_int.setText(String.valueOf(currentGold));
        diamond_int.setText(String.valueOf(currentDiamonds));
        experience_int.setText(String.valueOf(currentExperience));
        player_name.setText(playerName);
        currentHealthInt = Math.round(currentHealth);
        currentMaxHealthInt = Math.round(maxHealth);
        if (currentHealthInt <= 0){
            currentHealthInt = 0;
        }
        current_health.setText(String.valueOf(currentHealthInt));
        max_health.setText(String.valueOf(currentMaxHealthInt));
        getHealthBar();

        //Use the experience to determine what level we are at
        experienceToLevel(currentExperience);

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
        getHealthBar();
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
            boost = random.nextInt(10);
        } else if (currentDifficulty == TaskContract.TaskEntry.DIFFICULTY_MEDIUM){
            boost = random.nextInt(30);
        } else{
            boost = random.nextInt(50); //DIFFICULTY_HARD
        }

        //Implement sharedPreferences to keep data persistence for level
        SharedPreferences sharedPref = getSharedPreferences("myPref", 0);
        //get the current gold amount
        currentGold = sharedPref.getInt("gold", 0);
        //Get the amount of gold about to be added
        gold = boost/2 + 25;
        //add to the current gold
        currentGold = currentGold + gold;
        //get the current experience
        currentExperience = sharedPref.getInt("experience", 1);
        //get the amound of experience about to be added
        experience = boost + 10;
        //add to the current experience
        currentExperience = currentExperience + experience;

        Toast.makeText(this, "Gained " + gold + " gold and " + experience + " experience!", Toast.LENGTH_SHORT).show();

        //Store the new gold and experience
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("experience", currentExperience);
        editor.apply();
        editor.putInt("gold", currentGold);
        editor.apply();

        //Set the textviews to display udpated experience and gold
        experience_int.setText(String.valueOf(currentExperience));
        gold_int.setText(String.valueOf(currentGold));

        //Determine what level we are at based upon the experience level
        experienceToLevel(currentExperience);

        //If the frequency is set to once, then delete the task after it has been pressed
        if (currentFrequency == 0){
            final Uri uri = ContentUris.withAppendedId(TaskContract.TaskEntry.CONTENT_URI, id);
            getApplicationContext().getContentResolver().delete(uri, null, null);
        }
    }

    //Overriden from FourthFragment when a reward is selected
    @Override
    public void onRewardSelected(int position){

        //TODO implement this with the spinners for notifications onTaskSelected
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

        //Make sure the gold stays positive (i.e. if the cost is greater than the user has, don't change anything);
        if(currentGold < currentCost){
            Toast.makeText(this, "You don't have enough gold to do this!", Toast.LENGTH_SHORT).show();
        } else {
            currentGold = currentGold - currentCost;
            //Get the current health
            currentHealth = sharedPref.getFloat("health", 50.0f);
            maxHealth = sharedPref.getFloat("maxHealth", 50.0f);
            if(currentHealth >= 5.0f){
                currentHealth = currentHealth - 5.0f;
            }
            Log.v(characterSelection, "Current health is " + currentHealth);
            Log.v(characterSelection, "Max health is " + maxHealth);
        }



        SharedPreferences.Editor goldEditor = sharedPref.edit();
        goldEditor.putInt("gold", currentGold);
        goldEditor.apply();
        goldEditor.putFloat("health", currentHealth);
        goldEditor.apply();

        gold_int.setText(String.valueOf(currentGold));

        currentHealthInt = Math.round(currentHealth);
        currentMaxHealthInt = Math.round(maxHealth);
        if (currentHealthInt <= 0){
            currentHealthInt = 0;
        }
        current_health.setText(String.valueOf(currentHealthInt));
        max_health.setText(String.valueOf(currentMaxHealthInt));
        getHealthBar();

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

    //Determines the level # based upon the amount of experience (eventually boil this down to a simple formula)
    public void experienceToLevel(int experience){
        if (10 < experience && experience <= 50 && currentLevel != 2){
            currentLevel = 2;
            level_int.setText(String.valueOf(currentLevel));
        } else if ( 50 < experience  && experience <= 100 && currentLevel != 3){
            currentLevel = 3;
            level_int.setText(String.valueOf(currentLevel));
        } else if (100 < experience && experience <= 150 && currentLevel != 4){
            currentLevel = 4;
            level_int.setText(String.valueOf(currentLevel));
        } else if (150 < experience && experience <= 200 && currentLevel != 5){
            currentLevel = 5;
            level_int.setText(String.valueOf(currentLevel));
        } else if (200 < experience && experience <= 300 && currentLevel != 6){
            currentLevel = 6;
            level_int.setText(String.valueOf(currentLevel));
        } else if (300 < experience && experience <= 450 && currentLevel != 7){
            currentLevel = 7;
            level_int.setText(String.valueOf(currentLevel));
        } else if (450 < experience && experience <= 600 && currentLevel != 8){
            currentLevel = 8;
            level_int.setText(String.valueOf(currentLevel));
        } else if (600 < experience && experience <= 800 && currentLevel != 9){
            currentLevel = 9;
            level_int.setText(String.valueOf(currentLevel));
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
        maxHealth = maxHealth + 5;
        currentHealth = maxHealth;
        SharedPreferences.Editor healthEditor = sharedPreferences.edit();
        healthEditor.putFloat("maxHealth", maxHealth);
        healthEditor.apply();
        healthEditor.putFloat("health", currentHealth);
        healthEditor.apply();
        currentHealthInt = Math.round(currentHealth);
        currentMaxHealthInt = Math.round(maxHealth);
        Log.v(characterSelection, "current Health Int is" + currentHealthInt);
        Log.v(characterSelection, "max Health Int is" + currentMaxHealthInt);
        current_health.setText(String.valueOf(currentHealthInt));
        max_health.setText(String.valueOf(currentMaxHealthInt));
        getHealthBar();

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
                Log.v(characterSelection, "Character set to boy");
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
                Log.v(characterSelection, "Character set to girl");
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

    public void getHealthBar(){
        SharedPreferences sharedPreferences = getSharedPreferences("myPref", 0);
        currentHealth = sharedPreferences.getFloat("health", 50.0f);
        maxHealth = sharedPreferences.getFloat("maxHealth", 50.0f);
        if(currentHealth / maxHealth >= 100.0f/100.0f){
            health_bar.setImageResource(R.drawable.health_bar_full);
        } else if (currentHealth / maxHealth > 92.0f/100.0f && currentHealth / maxHealth < 100.0f/100.0f){
            health_bar.setImageResource(R.drawable.health_bar11);
        } else if (currentHealth / maxHealth > 84.0f/100.0f && currentHealth / maxHealth <= 92.0f/100.0f){
            health_bar.setImageResource(R.drawable.health_bar10);
        } else if (currentHealth / maxHealth > 76.0f/100.0f && currentHealth / maxHealth < 84.0f/100.0f){
            health_bar.setImageResource(R.drawable.health_bar9);
        } else if (currentHealth / maxHealth > 68.0f/100.0f && currentHealth / maxHealth <= 76.0f/100.0f){
            health_bar.setImageResource(R.drawable.health_bar8);
        } else if (currentHealth / maxHealth > 60.0f/100.0f && currentHealth / maxHealth < 68.0f/100.0f){
            health_bar.setImageResource(R.drawable.health_bar7);
        } else if (currentHealth / maxHealth > 52.0f/100.0f && currentHealth / maxHealth <= 60.0f/100.0f){
            health_bar.setImageResource(R.drawable.health_bar6);
        } else if (currentHealth / maxHealth > 44.0f/100.0f && currentHealth / maxHealth < 52.0f/100.0f){
            health_bar.setImageResource(R.drawable.health_bar5);
        } else if (currentHealth / maxHealth > 36.0f/100.0f && currentHealth / maxHealth <= 44.0f/100.0f){
            health_bar.setImageResource(R.drawable.health_bar4);
        } else if (currentHealth / maxHealth > 28.0f/100.0f && currentHealth / maxHealth < 36.0f/100.0f){
            health_bar.setImageResource(R.drawable.health_bar3);
        } else if (currentHealth / maxHealth > 20.0f/100.0f && currentHealth / maxHealth <= 28.0f/100.0f){
            health_bar.setImageResource(R.drawable.health_bar2);
        } else if (currentHealth / maxHealth > 12.0f/100.0f && currentHealth / maxHealth <= 20.0f/100.0f){
            health_bar.setImageResource(R.drawable.health_bar1);
        } else if (currentHealth / maxHealth > 0.0f/100.0f && currentHealth / maxHealth <= 12.0f/100.0f){
            health_bar.setImageResource(R.drawable.health_bar0);
        }else if (currentHealth / maxHealth == 0.0f/100.0f){
            health_bar.setImageResource(R.drawable.health_bar_empty);
        }

        //adjust the health bar text
        currentHealthInt = Math.round(currentHealth);
        currentMaxHealthInt = Math.round(maxHealth);
        if (currentHealthInt <= 0){
            currentHealthInt = 0;
        }
        current_health.setText(String.valueOf(currentHealthInt));
        max_health.setText(String.valueOf(currentMaxHealthInt));
    }

    //Called whenever a picture is clicked in the images fragment
    @Override
    public void onPictureSelected(int position){
        //Get the health and subtract whatever item is currently equipped (will be added back in at the end of
        SharedPreferences sharedPreferences = getSharedPreferences("myPref", 0);
        currentShirtBonus = sharedPreferences.getFloat("shirtHealth", 0.0f);
        maxHealth = sharedPreferences.getFloat("maxHealth", 50.0f);
        maxHealth = maxHealth - currentShirtBonus;

        SharedPreferences shirtLocked = getSharedPreferences("shirtLocked", 0);

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

        //Two large switch statements depending on which gender is selected.  These determine how the character image is altared
        SharedPreferences currentGender = getSharedPreferences("currentGender", 0);
        if (currentGender.getString("gender", "").equals("boy")){
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
                        Log.v(shirtName, "is no longer locked");
                        shirt.setImageResource(shirtDrawables[position]);
                        currentShirt = position;
                        shirtLocked.edit().putBoolean(shirtName, false).apply();
                        currentShirtBonus = currentShirtBonusUnlocked;
                        currentGold = currentGold - goldUnlockRequirement;
                    } else if (shirtLocked.getBoolean(shirtName, true) && currentGold < goldUnlockRequirement){
                        Toast.makeText(this, "Sorry, you need " + goldUnlockRequirement  + "gold to purchase this item", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.v(shirtName, "has been selected");
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
                        Toast.makeText(this, "Sorry, you need " + diamondUnlockRequirement  + " diamonds to purchase this item", Toast.LENGTH_SHORT).show();
                    } else {
                        shirt.setImageResource(shirtDrawables[position]);
                        currentShirt = position;
                        currentShirtBonus = currentShirtBonusUnlocked;
                    }
                }
            }

        }
        else if (currentGender.getString("gender", "").equals("girl")){
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
                        Toast.makeText(this, "Sorry, you need " + goldUnlockRequirement  + " gold to purchase this item", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(this, "Sorry, you need " + diamondUnlockRequirement  + " diamonds to purchase this item", Toast.LENGTH_SHORT).show();
                    } else {
                        shirt.setImageResource(girlShirtDrawables[position]);
                        currentShirt = position;
                        currentShirtBonus = currentShirtBonusUnlocked;
                    }
                }
            }

        }

        //Store the shirt that was selected in the outfit sharedpreference
        SharedPreferences shirt = getSharedPreferences("outfit", 0);
        SharedPreferences.Editor shirt_editor = shirt.edit();
        shirt_editor.putInt("currentShirt", currentShirt);
        shirt_editor.apply();


        //apply the new item's bonus health
        maxHealth = maxHealth + currentShirtBonus;

        Log.v(characterSelection, "Max health is " + maxHealth);

        SharedPreferences shirtHealth = getSharedPreferences("myPref", 0);
        SharedPreferences.Editor bonus_health_editor = shirtHealth.edit();
        bonus_health_editor.putFloat("shirtHealth", currentShirtBonus);
        bonus_health_editor.apply();
        bonus_health_editor.putFloat("maxHealth", maxHealth);
        bonus_health_editor.apply();

        currentMaxHealthInt = Math.round(maxHealth);
        Log.v(characterSelection, "max Health Int is" + currentMaxHealthInt);
        max_health.setText(String.valueOf(currentMaxHealthInt));

        getHealthBar();

        SharedPreferences.Editor goldEditor = sharedPreferences.edit();
        goldEditor.putInt("gold", currentGold);
        goldEditor.apply();
        goldEditor.putInt("diamonds", currentDiamonds);
        goldEditor.apply();

        gold_int.setText(String.valueOf(currentGold));
        diamond_int.setText(String.valueOf(currentDiamonds));


    }

    @Override
    public void onHatSelected(int position){

        SharedPreferences sharedPreferences = getSharedPreferences("myPref", 0);
        currentHatBonus = sharedPreferences.getFloat("hatHealth", 0.0f);
        maxHealth = sharedPreferences.getFloat("maxHealth", 50.0f);
        maxHealth = maxHealth - currentHatBonus;

        SharedPreferences hatLocked = getSharedPreferences("hatLocked", 0);

        int[] hatDrawables = new int[] {
                R.drawable.boy_black_shirt, R.drawable.boy_red_shirt, R.drawable.boy_blue_shirt, R.drawable.boy_green_shirt,
                R.drawable.boy_purple_shirt, R.drawable.boy_orange_shirt, R.drawable.boy_grey_black_striped_shirt, R.drawable.boy_leather_armor,
                R.drawable.boy_green_leather_armor, R.drawable.steel_armor, R.drawable.gold_armor, R.drawable.platinum_armor,
                R.drawable.diamond_armor, R.drawable.samurai_armor, R.drawable.rainbow_armor, R.drawable.blank, R.drawable.hat_wizard_transparent,
                R.drawable.hat_knight_helmet, R.drawable.hat_viking, R.drawable.hat_knight_full, R.drawable.hat_wizard_orange_transparent,
                R.drawable.hat_knight_full_gold, R.drawable.hat_knight_full_platinum, R.drawable.hat_knight_full_diamond, R.drawable.hat_wizard_black_transparent,
                R.drawable.hat_knight_full_samurai, R.drawable.blank
        };

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
                    Log.v(hatName, "is no longer locked");
                    currentHat = position;
                    hatLocked.edit().putBoolean(hatName, false).apply();
                    currentHatBonus = currentHatBonusUnlocked;
                    currentGold = currentGold - goldUnlockRequirement;
                } else if (hatLocked.getBoolean(hatName, true) && currentGold < goldUnlockRequirement){
                    Toast.makeText(this, "Sorry, you need " + goldUnlockRequirement  + " gold to purchase this item", Toast.LENGTH_SHORT).show();
                } else {
                    hat.setImageResource(hatDrawables[position]);
                    Log.v(hatName, "has been selected");
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
                    Toast.makeText(this, "Sorry, you need " + diamondUnlockRequirement  + " diamonds to purchase this item", Toast.LENGTH_SHORT).show();
                } else {
                    hat.setImageResource(hatDrawables[position]);
                    currentHat = position;
                    currentHatBonus = currentHatBonusUnlocked;
                }
            }
        }

        SharedPreferences hat = getSharedPreferences("hat", 0);
        SharedPreferences.Editor hat_editor = hat.edit();
        hat_editor.putInt("currentHat", currentHat);
        hat_editor.apply();

        //apply the new item's bonus health
        maxHealth = maxHealth + currentHatBonus;

        SharedPreferences hatHealth = getSharedPreferences("myPref", 0);
        SharedPreferences.Editor bonus_health_editor = hatHealth.edit();
        bonus_health_editor.putFloat("hatHealth", currentHatBonus);
        bonus_health_editor.apply();
        bonus_health_editor.putFloat("maxHealth", maxHealth);
        bonus_health_editor.apply();

        currentMaxHealthInt = Math.round(maxHealth);
        Log.v(characterSelection, "max Health Int is" + currentMaxHealthInt);
        max_health.setText(String.valueOf(currentMaxHealthInt));

        getHealthBar();

        SharedPreferences.Editor goldEditor = sharedPreferences.edit();
        goldEditor.putInt("gold", currentGold);
        goldEditor.apply();
        goldEditor.putInt("diamonds", currentDiamonds);
        goldEditor.apply();

        gold_int.setText(String.valueOf(currentGold));
        diamond_int.setText(String.valueOf(currentDiamonds));
    }

    @Override
    public void onWeaponSelected(int position){

        SharedPreferences weaponLocked = getSharedPreferences("weaponLocked", 0);

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
                    Log.v(weaponName, "is no longer locked");
                    currentWeapon = position;
                    weaponLocked.edit().putBoolean(weaponName, false).apply();
                    currentGold = currentGold - goldUnlockRequirement;
                } else if (weaponLocked.getBoolean(weaponName, true) && currentGold < goldUnlockRequirement){
                    Toast.makeText(this, "Sorry, you need " + goldUnlockRequirement  + " gold to purchase this item", Toast.LENGTH_SHORT).show();
                } else {
                    weapon.setImageResource(weaponDrawables[position]);
                    Log.v(weaponName, "has been selected");
                    currentWeapon = position;
                }
            } else {
                if (weaponLocked.getBoolean(weaponName, true) && currentDiamonds >= diamondUnlockRequirement){
                    weapon.setImageResource(weaponDrawables[position]);
                    currentWeapon = position;
                    weaponLocked.edit().putBoolean(weaponName, false).apply();
                    currentDiamonds = currentDiamonds - diamondUnlockRequirement;
                } else if (weaponLocked.getBoolean(weaponName, true) && currentDiamonds < diamondUnlockRequirement){
                    Toast.makeText(this, "Sorry, you need " + diamondUnlockRequirement  + " diamonds to purchase this item", Toast.LENGTH_SHORT).show();
                } else {
                    weapon.setImageResource(weaponDrawables[position]);
                    currentWeapon = position;
                }
            }
        }
        SharedPreferences weapon = getSharedPreferences("weapon", 0);
        SharedPreferences.Editor weapon_editor = weapon.edit();
        weapon_editor.putInt("currentWeapon", currentWeapon);
        weapon_editor.apply();

        SharedPreferences sharedPreferences = getSharedPreferences("myPref", 0);
        SharedPreferences.Editor goldEditor = sharedPreferences.edit();
        goldEditor.putInt("gold", currentGold);
        goldEditor.apply();
        goldEditor.putInt("diamonds", currentDiamonds);
        goldEditor.apply();

        gold_int.setText(String.valueOf(currentGold));
        diamond_int.setText(String.valueOf(currentDiamonds));
    }

    @Override
    public void onShieldSelected(int position){

        SharedPreferences shieldLocked = getSharedPreferences("shieldLocked", 0);

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
                    Log.v(shieldName, "is no longer locked");
                    currentShield = position;
                    shieldLocked.edit().putBoolean(shieldName, false).apply();
                    currentGold = currentGold - goldUnlockRequirement;
                } else if (shieldLocked.getBoolean(shieldName, true) && currentGold < goldUnlockRequirement){
                    Toast.makeText(this, "Sorry, you need " + goldUnlockRequirement  + " gold to purchase this item", Toast.LENGTH_SHORT).show();
                } else {
                    shield.setImageResource(shieldDrawables[position]);
                    Log.v(shieldName, "has been selected");
                    currentShield = position;
                }
            } else {
                if (shieldLocked.getBoolean(shieldName, true) && currentDiamonds >= diamondUnlockRequirement){
                    shield.setImageResource(shieldDrawables[position]);
                    currentShield = position;
                    shieldLocked.edit().putBoolean(shieldName, false).apply();
                    currentDiamonds = currentDiamonds - diamondUnlockRequirement;
                } else if (shieldLocked.getBoolean(shieldName, true) && currentDiamonds < diamondUnlockRequirement){
                    Toast.makeText(this, "Sorry, you need " + diamondUnlockRequirement  + " diamonds to purchase this item", Toast.LENGTH_SHORT).show();
                } else {
                    shield.setImageResource(shieldDrawables[position]);
                    currentShield = position;
                }
            }
        }

        SharedPreferences shield = getSharedPreferences("shield", 0);
        SharedPreferences.Editor shield_editor = shield.edit();
        shield_editor.putInt("currentShield", currentShield);
        shield_editor.apply();

        SharedPreferences sharedPreferences = getSharedPreferences("myPref", 0);
        SharedPreferences.Editor goldEditor = sharedPreferences.edit();
        goldEditor.putInt("gold", currentGold);
        goldEditor.apply();
        goldEditor.putInt("diamonds", currentDiamonds);
        goldEditor.apply();

        gold_int.setText(String.valueOf(currentGold));
        diamond_int.setText(String.valueOf(currentDiamonds));
    }

    @Override
    public void onLeggingSelected(int position){

        SharedPreferences sharedPreferences = getSharedPreferences("myPref", 0);
        currentLeggingBonus = sharedPreferences.getFloat("leggingHealth", 0.0f);
        maxHealth = sharedPreferences.getFloat("maxHealth", 50.0f);
        maxHealth = maxHealth - currentLeggingBonus;

        SharedPreferences leggingLocked = getSharedPreferences("leggingLocked", 0);

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
                    Toast.makeText(this, "Sorry, you need " + goldUnlockRequirement  + " gold to purchase this item", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(this, "Sorry, you need " + diamondUnlockRequirement  + " diamonds to purchase this item", Toast.LENGTH_SHORT).show();
                } else {
                    legging.setImageResource(leggingDrawables[position]);
                    currentLeggingBonus = currentLeggingBonusUnlocked;
                    currentLegging = position;
                }
            }
        }
        SharedPreferences legging = getSharedPreferences("legging", 0);
        SharedPreferences.Editor legging_editor = legging.edit();
        legging_editor.putInt("currentLegging", currentLegging);
        legging_editor.apply();

        //apply the new item's bonus health
        maxHealth = maxHealth + currentLeggingBonus;

        SharedPreferences leggingHealth = getSharedPreferences("myPref", 0);
        SharedPreferences.Editor bonus_health_editor = leggingHealth.edit();
        bonus_health_editor.putFloat("leggingHealth", currentLeggingBonus);
        bonus_health_editor.apply();
        bonus_health_editor.putFloat("maxHealth", maxHealth);
        bonus_health_editor.apply();

        currentMaxHealthInt = Math.round(maxHealth);
        max_health.setText(String.valueOf(currentMaxHealthInt));

        getHealthBar();

        SharedPreferences.Editor goldEditor = sharedPreferences.edit();
        goldEditor.putInt("gold", currentGold);
        goldEditor.apply();
        goldEditor.putInt("diamonds", currentDiamonds);
        goldEditor.apply();

        gold_int.setText(String.valueOf(currentGold));
        diamond_int.setText(String.valueOf(currentDiamonds));
    }
}