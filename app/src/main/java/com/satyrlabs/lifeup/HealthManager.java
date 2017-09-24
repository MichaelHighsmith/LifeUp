package com.satyrlabs.lifeup;


import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

public class HealthManager extends AppCompatActivity {

    TextView current_health;
    TextView max_health;


    float currentHealth;
    float maxHealth;

    ImageView health_bar;

    public HealthManager(ImageView health_bar, TextView current_health, TextView max_health,
                          float currentHealth, float maxHealth){
        this.health_bar = health_bar;
        this.current_health = current_health;
        this.max_health = max_health;
        this.currentHealth = currentHealth;
        this.maxHealth = maxHealth;

        setViews(currentHealth, maxHealth);
    }

    public void setViews(float currentHealth, float maxHealth){
        //Calculate which image to use (getHealthBar)
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

        //Set the TextViews
        int currentHealthInt = (int) currentHealth;
        int currentMaxHealthInt = (int) maxHealth;
        if(currentHealthInt <= 0){
            currentHealthInt = 0;
        }
        current_health.setText(String.valueOf(currentHealthInt));
        max_health.setText(String.valueOf(currentMaxHealthInt));
    }

    public float loseHealth(){
        if(currentHealth >= 5.0f){
            currentHealth = currentHealth - 5.0f;
        }
        setViews(currentHealth, maxHealth);
        return currentHealth;
    }

    public float gainHealth(){
        if(currentHealth + 10.0f < maxHealth){
            currentHealth += 10.0f;
        } else if(currentHealth < maxHealth){
            currentHealth = maxHealth;
        }
        setViews(currentHealth, maxHealth);
        return currentHealth;
    }

    public int healthRestored(int currentGold){
        if(currentHealth < maxHealth && currentGold >= 20){
            currentHealth = maxHealth;
            currentGold = currentGold - 20;
        } else if(currentHealth < maxHealth){
            throw new NullPointerException();
        } else{
            throw new RuntimeException();
        }

        setViews(currentHealth, maxHealth);
        return currentGold;
    }

}
