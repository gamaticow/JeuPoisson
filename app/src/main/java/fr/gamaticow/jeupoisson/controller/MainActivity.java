package fr.gamaticow.jeupoisson.controller;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import fr.gamaticow.jeupoisson.R;
import fr.gamaticow.jeupoisson.model.FishGameLevel;
import fr.gamaticow.jeupoisson.model.LevelDifficulties;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String EXTRA_DIFFICULTY = "EXTRA_DIFFICULTY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button easy = findViewById(R.id.easy);
        Button normal = findViewById(R.id.norm);
        Button hard = findViewById(R.id.hard);

        easy.setTag(LevelDifficulties.EASY);
        normal.setTag(LevelDifficulties.NORMAL);
        hard.setTag(LevelDifficulties.HARD);

        easy.setOnClickListener(this);
        normal.setOnClickListener(this);
        hard.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getTag() instanceof LevelDifficulties){
            startGame((LevelDifficulties) v.getTag());
        }
    }

    private void startGame(LevelDifficulties difficulties){
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(EXTRA_DIFFICULTY, difficulties);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        View decorView = getWindow().getDecorView();
        if(hasFocus){
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );
        }
    }

}