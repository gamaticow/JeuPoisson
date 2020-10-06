package fr.gamaticow.jeupoisson.controller;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fr.gamaticow.jeupoisson.R;
import fr.gamaticow.jeupoisson.model.Bucket;
import fr.gamaticow.jeupoisson.model.BucketsContainer;
import fr.gamaticow.jeupoisson.model.Fish;
import fr.gamaticow.jeupoisson.model.FishGameDrawable;
import fr.gamaticow.jeupoisson.model.FishGameLevel;
import fr.gamaticow.jeupoisson.model.LevelDifficulties;

public class GameActivity extends AppCompatActivity {

    public ConstraintLayout fishLayout;
    public BucketsContainer container;
    public FishGameLevel level;
    private boolean active;

    public static final String TAG = "POISSON";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        this.fishLayout = findViewById(R.id.fish_layout);

        LevelDifficulties difficulties = (LevelDifficulties) getIntent().getSerializableExtra(MainActivity.EXTRA_DIFFICULTY);
        this.level = new FishGameLevel(this, difficulties);
        container = level.initialise(findViewById(R.id.buckets_layout));
        level.setOnEnd(() -> {
            Toast.makeText(this, "Bravo !", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        active = true;
        new Thread(this::fishAI).start();
    }

    @Override
    protected void onPause() {
        active = false;
        super.onPause();
    }

    public static final int MAX_FISH = 10;
    List<Fish> fishs = new ArrayList<>();
    List<Fish> toRemove = new ArrayList<>();
    List<Fish> toAdd = new ArrayList<>();
    int step = 0;
    int nextStep;

    public void fishAI(){
        while (active){

            if(step >= nextStep)
                step = 0;

            if(step == 0)
                generateFish(false);

            step++;

            for(Fish fish : fishs)
                fish.move();

            container.animate();

            for(Fish fish : toRemove)
                fishs.remove(fish);

            fishs.addAll(toAdd);

            toRemove.clear();
            toAdd.clear();

            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void generateFish(boolean fromDestroy){
        if(fromDestroy || fishs.size() < MAX_FISH) {
            Fish fish = new Fish(this, Math.random() > 0.5 ? Fish.RIGHT : Fish.LEFT, level.getFishColor());
            if(fromDestroy)
                toAdd.add(fish);
            else
                fishs.add(fish);
            fish.setOnDestroy(() -> {
                toRemove.add(fish);
                generateFish(true);
            });
        }

        if(!fromDestroy)
            nextStep = (int) (Math.random()*100+100);
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