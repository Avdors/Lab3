package com.example.lab3;

import androidx.appcompat.app.AppCompatActivity;

import android.media.SoundPool;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    float p=0;
    private SoundPool sounds;
    private int sExplosion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(new Panel(this));

        setContentView(R.layout.activity_main);

        // Initially load the StartFragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fl_screen, new Start())
                    .commit();
        }

    }


    public void startGame(String playerName, int numBeetles) {
        Game gameFragment = Game.newInstance(playerName, numBeetles);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_screen, gameFragment)
                .addToBackStack(null)
                .commit();
    }
}