package com.example.lab3;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Initially load the StartFragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fl_screen, new Start())
                    .commit();
        }

    }

    public void startGame() {
        Game gameFragment = Game.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_screen, gameFragment)
                .addToBackStack(null)
                .commit();
    }
}