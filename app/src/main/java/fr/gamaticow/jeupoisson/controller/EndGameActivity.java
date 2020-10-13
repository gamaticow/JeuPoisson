package fr.gamaticow.jeupoisson.controller;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.view.ViewCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import fr.gamaticow.jeupoisson.R;

public class EndGameActivity extends AppCompatActivity {

    public static final String INTENT_STAR_NUMBER = "STAR_NUMBER";

    private ImageView[] stars = new ImageView[3];
    private Button menu;

    private StarAnimation starAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int nbStar = getIntent().getIntExtra(INTENT_STAR_NUMBER, 0);
        setContentView(generateLayout(nbStar));

        starAnimation = new StarAnimation(new Star(this, stars[0]), new Star(this, stars[1]), new Star(this, stars[2])).start();

        menu.setOnClickListener((event) -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
    }

    private ConstraintLayout generateLayout(int nbStar){
        final int STAR_SIZE = 100;
        float dp = this.getResources().getDisplayMetrics().density;

        ConstraintLayout layout = new ConstraintLayout(this);
        layout.setId(ViewCompat.generateViewId());
        layout.setBackgroundColor(0xFF61A1AB);
        ConstraintLayout.LayoutParams constraintParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layout.setLayoutParams(constraintParams);

        LinearLayout ll = new LinearLayout(this);
        ll.setId(ViewCompat.generateViewId());
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setGravity(1);
        ConstraintLayout.LayoutParams llParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ll.setLayoutParams(llParams);

        layout.addView(ll);

        ConstraintSet set1 = new ConstraintSet();
        set1.clone(layout);
        set1.connect(ll.getId(), ConstraintSet.BOTTOM, layout.getId(), ConstraintSet.BOTTOM);
        set1.connect(ll.getId(), ConstraintSet.TOP, layout.getId(), ConstraintSet.TOP);
        set1.connect(ll.getId(), ConstraintSet.LEFT, layout.getId(), ConstraintSet.LEFT);
        set1.connect(ll.getId(), ConstraintSet.RIGHT, layout.getId(), ConstraintSet.RIGHT);
        set1.applyTo(layout);

        LinearLayout llStar = new LinearLayout(this);
        llStar.setId(ViewCompat.generateViewId());
        llStar.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams llStarParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        llStar.setLayoutParams(llStarParams);

        ll.addView(llStar);

        for(int i = 0; i < stars.length; i++){
            ConstraintLayout starWrapper = new ConstraintLayout(this);
            starWrapper.setId(ViewCompat.generateViewId());
            LinearLayout.LayoutParams starWrapperParams = new LinearLayout.LayoutParams((int) (STAR_SIZE*dp), (int) (STAR_SIZE*dp));
            if(i == 0)
                starWrapperParams.setMarginEnd((int) (-30*dp));
            else if(i == 1)
                starWrapperParams.topMargin = (int) (-15*dp);
            else if(i == 2)
                starWrapperParams.setMarginStart((int) (-30*dp));
            starWrapper.setLayoutParams(starWrapperParams);

            ImageView star = new ImageView(this);
            star.setId(ViewCompat.generateViewId());
            ConstraintLayout.LayoutParams starParams = new ConstraintLayout.LayoutParams((int) (STAR_SIZE*dp), (int) (STAR_SIZE*dp));
            star.setLayoutParams(starParams);
            star.setImageResource(i < nbStar ? R.drawable.star : R.drawable.star_empty);
            if(i == 0){
                star.setRotation(340);
            }else if(i == 2){
                star.setRotation(20);
            }

            stars[i] = star;

            starWrapper.addView(star);

            ConstraintSet set2 = new ConstraintSet();
            set2.clone(starWrapper);
            set2.connect(star.getId(), ConstraintSet.BOTTOM, starWrapper.getId(), ConstraintSet.BOTTOM);
            set2.connect(star.getId(), ConstraintSet.TOP, starWrapper.getId(), ConstraintSet.TOP);
            set2.connect(star.getId(), ConstraintSet.LEFT, starWrapper.getId(), ConstraintSet.LEFT);
            set2.connect(star.getId(), ConstraintSet.RIGHT, starWrapper.getId(), ConstraintSet.RIGHT);
            set2.applyTo(starWrapper);

            llStar.addView(starWrapper);
        }

        menu = new Button(this);
        menu.setText("MENU");
        LinearLayout.LayoutParams menuParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        menu.setLayoutParams(menuParams);

        ll.addView(menu);

        return layout;
    }

    @Override
    protected void onPause() {
        starAnimation.cancel();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
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

    private static class StarAnimation {

        public static final int ANIMATION_SPEED = 25;

        private Star[] stars;
        private Thread thread;
        private boolean end = false;

        public StarAnimation(Star star1, Star star2, Star star3){
            this.stars = new Star[] {star1, star2, star3};
        }

        public StarAnimation start(){
            if(thread != null)
                return this;

            thread = new Thread(() -> {
                while (!end){
                    boolean finish = true;
                    for (Star star : stars){
                        if(!star.animate()) {
                            finish = false;
                            break;
                        }
                    }

                    end = finish;

                    try {
                        Thread.sleep(ANIMATION_SPEED);
                    } catch (InterruptedException ignored) { }
                }
            });

            thread.start();

            return this;
        }

        public void cancel(){
            end = true;

            for(Star star : stars)
                star.finish();
        }

    }

    private static class Star {

        public static final int ANIMATION_DURATION = 750;
        public static final int ANIMATION_STEP = ANIMATION_DURATION/StarAnimation.ANIMATION_SPEED;

        private EndGameActivity context;

        private ImageView imageView;
        private float rotation;
        private int size;
        private boolean animationEnd = false;

        private int step;

        private float sizePerStep;
        private float rotationPerStep;

        private float currentSize;
        private float currentRotation;

        public Star(EndGameActivity context, ImageView imageView){
            this.context = context;

            this.imageView = imageView;
            this.rotation = imageView.getRotation();
            this.size = imageView.getLayoutParams().width;

            this.step = 0;

            currentSize = 0;
            currentRotation = rotation - 360;

            sizePerStep = 1f*size/ANIMATION_STEP;
            rotationPerStep = (rotation-currentRotation)/ANIMATION_STEP;

            imageView.setVisibility(View.INVISIBLE);
        }

        public boolean animate(){
            if(animationEnd){
                finish();
                return true;
            }

            currentSize += sizePerStep;
            currentRotation += rotationPerStep;

            currentPlacement();

            step++;

            if(step >= ANIMATION_STEP)
                animationEnd = true;

            return false;
        }

        private void currentPlacement() {
            context.runOnUiThread(() -> {
                imageView.setVisibility(View.VISIBLE);
                imageView.setRotation(currentRotation);
                ViewGroup.LayoutParams params = imageView.getLayoutParams();
                params.width = (int) currentSize;
                params.height = (int) currentSize;
                imageView.setLayoutParams(params);
            });
        }

        public void finish(){
            context.runOnUiThread(() -> {
                imageView.setVisibility(View.VISIBLE);
                imageView.setRotation(rotation);
                ViewGroup.LayoutParams params = imageView.getLayoutParams();
                params.width = size;
                params.height = size;
                imageView.setLayoutParams(params);
            });
        }

    }

}