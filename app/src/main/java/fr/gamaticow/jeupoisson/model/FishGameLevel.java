package fr.gamaticow.jeupoisson.model;

import android.content.Intent;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import fr.gamaticow.jeupoisson.R;
import fr.gamaticow.jeupoisson.controller.EndGameActivity;
import fr.gamaticow.jeupoisson.controller.GameActivity;

/**
 * Created by Corentin COUTEAUX on 01/10/2020 at 13:52.
 */

public class FishGameLevel {

    private GameActivity context;
    private LevelDifficulties difficulties;
    private Bucket[] buckets;

    private int lives;
    private ImageView[] hearts;

    private Runnable onEnd;

    public FishGameLevel(GameActivity context, LevelDifficulties difficulties, LinearLayout livesLayout){
        this.context = context;
        this.difficulties = difficulties;
        this.lives = 3;
        this.hearts = new ImageView[lives];
        createHearths(livesLayout);
    }

    public BucketsContainer initialise(LinearLayout bucketsLayout){
        BucketsContainer container = new BucketsContainer(context, this, bucketsLayout);

        FishGameDrawable color1 = FishGameDrawable.getRandomEasyColor();
        FishGameDrawable color2 = FishGameDrawable.getRandomEasyColor();
        while (color2 == color1) {
            color2 = FishGameDrawable.getRandomEasyColor();
        }

        if(difficulties == LevelDifficulties.EASY){
            buckets = new Bucket[]{ new Bucket(container, color1, false), new Bucket(container, color2, false) };
        }else if(difficulties == LevelDifficulties.NORMAL){
            if(Math.random() < 0.5)
                color2 = FishGameDrawable.getRandomHardColor();

            buckets = new Bucket[]{ new Bucket(container, color1, true), new Bucket(container, color2, true) };
        }else if(difficulties == LevelDifficulties.HARD){
            FishGameDrawable color3 = FishGameDrawable.getRandomHardColor();
            buckets = new Bucket[]{ new Bucket(container, color1, true), new Bucket(container, color2, true), new Bucket(container, color3, true) };
        }

        return container;
    }

    public void setOnEnd(Runnable onEnd){
        this.onEnd = onEnd;
    }

    protected void end(){
        onEnd.run();
    }

    private int lastCorresponding = 0;
    private int nextCorresponding = 3;
    public synchronized FishGameDrawable getFishColor(){
        lastCorresponding++;
        if(lastCorresponding >= nextCorresponding) {
            nextCorresponding = new Random().nextInt(4)+2;
            lastCorresponding = 0;
            return getRandomColorFromBuckets();
        }
        return getRandomColorExceptBuckets();
    }

    private FishGameDrawable getRandomColorFromBuckets(){
        List<FishGameDrawable> colors = new ArrayList<>();
        for(Bucket bucket : buckets){
            if(!bucket.isFilled())
                colors.add(bucket.getColor());
        }
        Collections.shuffle(colors);
        return colors.size() > 0 ? colors.get(new Random().nextInt(colors.size())) : FishGameDrawable.getRandomColor();
    }

    private FishGameDrawable getRandomColorExceptBuckets(){
        List<FishGameDrawable> colors = new ArrayList<>(Arrays.asList(FishGameDrawable.values()));
        for(Bucket bucket : buckets)
            colors.remove(bucket.getColor());
        Collections.shuffle(colors);
        return colors.get(new Random().nextInt(colors.size()));
    }

    private void createHearths(LinearLayout layout){
        float dp = context.getResources().getDisplayMetrics().density;
        final int SIZE = 50;

        for(int i = 0; i < hearts.length; i++){
            ImageView heart = new ImageView(context);
            heart.setImageResource(R.drawable.heart);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) (SIZE*dp), (int) (SIZE*dp));
            heart.setLayoutParams(params);
            layout.addView(heart);

            hearts[i] = heart;
        }
    }

    public int getLives() {
        return lives;
    }

    public void removeLife(){
        lives--;
        if(lives <= 0){
            Intent intent = new Intent(context, EndGameActivity.class);
            intent.putExtra(EndGameActivity.INTENT_STAR_NUMBER, 0);
            context.startActivity(intent);
            return;
        }

        hearts[lives].setImageResource(R.drawable.heart_empty);
    }

}
