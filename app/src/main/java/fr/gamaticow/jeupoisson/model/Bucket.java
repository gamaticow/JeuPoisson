package fr.gamaticow.jeupoisson.model;

import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import fr.gamaticow.jeupoisson.controller.GameActivity;

/**
 * Created by Corentin COUTEAUX on 30/09/2020 at 13:52.
 */

public class Bucket {

    private static final int SIZE = 100;
    private static final FishGameDrawable NO_COLOR_BUCKET = FishGameDrawable.GREY;

    private GameActivity context;
    private BucketsContainer container;
    private FishGameDrawable color;
    private FishGameDrawable bucketDisplayColor;
    private int fillLevel;
    private ImageView imageView;
    private LinearLayout linearLayout;
    public final float dp;

    public Bucket(BucketsContainer container, FishGameDrawable color, boolean noColor){
        this.container = container;
        this.context = container.getContext();
        this.color = color;
        bucketDisplayColor = color;
        fillLevel = 0;
        if(noColor)
            bucketDisplayColor = NO_COLOR_BUCKET;

        dp = context.getResources().getDisplayMetrics().density;

        linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        this.imageView = new ImageView(context);
        imageView.setImageResource(bucketDisplayColor.getBucket(fillLevel));

        int width = (int) (SIZE*dp);
        int height = (int) (SIZE*dp);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
        layoutParams.topMargin = (int) (10*dp);
        layoutParams.leftMargin = (int) (10*dp);
        layoutParams.rightMargin = (int) (10*dp);
        imageView.setLayoutParams(layoutParams);

        TextView textColor = new TextView(context);
        textColor.setText(color.getName(context));
        textColor.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textColor.setTypeface(null, Typeface.BOLD);

        linearLayout.addView(imageView);
        linearLayout.addView(textColor);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = 0;
        params.bottomMargin = (int) (10*dp);

        linearLayout.setLayoutParams(params);


        container.addBucket(this);
    }

    public LinearLayout getLinearLayout() {
        return linearLayout;
    }

    private static final int ANIMATION_SPEED = 3;
    private static final int ANIMATION_END = 2;
    private int animationStep = 0;
    private int direction = 1;
    public void animate(){
        if(animationStep > ANIMATION_END)
            direction = -1;
        else if(animationStep < ANIMATION_END*-1)
            direction = 1;

        animationStep += direction;
        rotation(animationStep*ANIMATION_SPEED);
    }

    public void resetAnimation(){
        animationStep = 0;
        direction = 1;
        rotation(0);
    }

    private void rotation(float value){
        context.runOnUiThread(() -> {
            imageView.setRotation(value);
        });
    }

    public FishGameDrawable getColor(){
        return color;
    }

    public void scorePoint(){
        fillLevel++;
        context.runOnUiThread(() -> {
            imageView.setImageResource(bucketDisplayColor.getBucket(fillLevel));
        });
    }

    public boolean isFilled(){
        return bucketDisplayColor.getBucket(fillLevel) == bucketDisplayColor.getBucket(fillLevel+1);
    }
}
