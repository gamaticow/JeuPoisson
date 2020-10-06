package fr.gamaticow.jeupoisson.model;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;

import fr.gamaticow.jeupoisson.controller.GameActivity;

//proxy.univ-lemans.fr
//port: 3128

/**
 * Created by Corentin COUTEAUX on 29/09/2020 at 16:27.
 */

public class Fish implements View.OnTouchListener {

    public static final int LEFT = -1;
    public static final int RIGHT = 1;
    private static final int SPEED = 2;
    private static final int SIZE = 75;
    private static final int SELECTED_SIZE = (int) (SIZE*1.5f);
    private static final int SIZE_DIFF = SELECTED_SIZE - SIZE;
    private static final float FISH_X_DRAG_PLACEMENT = 0.5f;
    private static final float FISH_Y_DRAG_PLACEMENT = 0.66f;

    private GameActivity context;
    private float dp;
    private ImageView imageView;
    private int direction;
    private FishGameDrawable color;

    private boolean isDestroyed;
    private Runnable onDestroy;

    public Fish(GameActivity context, int direction, FishGameDrawable color){
        this.context = context;

        this.color = color;

        dp = context.getResources().getDisplayMetrics().density;
        int width = (int) (SIZE*dp);
        int height = (int) (SIZE*dp);

        float x = context.fishLayout.getX();
        if(direction == LEFT)
            x = context.fishLayout.getWidth()-context.fishLayout.getX()-height;

        float y = (float) (Math.random()*(context.fishLayout.getHeight()-context.container.getLayout().getHeight()-height));

        imageView = new ImageView(context);
        imageView.setImageResource(color.getFish());
        imageView.setLayoutParams(new ConstraintLayout.LayoutParams(width, height));
        imageView.setX(x);
        imageView.setY(y);
        if(direction == RIGHT)
            imageView.setRotationY(180);
        context.runOnUiThread(this::render);
        this.direction = direction;
        isDestroyed = false;

        imageView.setOnTouchListener(this);
    }

    private static final int ANIMATION_END = 8;
    private static final float ANIMATION_SPEED = 2;
    private int animationDirection = 1;
    private int animationStep = 0;

    public void move(){
        if(dragged){
            int position = direction == RIGHT ? 180 : 0;
            rotationY(position);
            rotation(270);
            return;
        }else if(isDestroyed)
            return;

        rotation(0);

        float x = imageView.getX();
        x += direction*SPEED*dp;
        move(x, imageView.getY());

        if(context.fishLayout.getWidth() == 0)
            return;
        if(imageView.getX()+SIZE*dp < context.fishLayout.getX() || imageView.getX() > context.fishLayout.getX()+context.fishLayout.getWidth())
            destroy();

        //ANIMATION
        if(animationStep >= ANIMATION_END)
            animationDirection = -1;
        else if(animationStep <= ANIMATION_END*-1)
            animationDirection = 1;

        animationStep += animationDirection;

        int decalage = 0;
        if(direction == RIGHT)
            decalage = 180;

        rotationY(animationStep*ANIMATION_SPEED+decalage);
    }

    public void setOnDestroy(Runnable callback){
        this.onDestroy = callback;
    }

    private void destroy(){
        isDestroyed = true;
        context.runOnUiThread(() -> context.fishLayout.removeView(imageView));
        if(onDestroy != null)
            onDestroy.run();
    }

    //private float dX, dY;
    private boolean dragged = false;

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            dragged = true;
            //dX = event.getX();
            //dY = event.getY();
            imageView.requestLayout();
            imageView.getLayoutParams().width = (int) (SELECTED_SIZE*dp);
            imageView.getLayoutParams().height = (int) (SELECTED_SIZE*dp);
            //move(imageView.getX()-(SIZE_DIFF/2*dp), imageView.getY()-(SIZE_DIFF/2*dp));
            float x = event.getRawX()- FISH_X_DRAG_PLACEMENT*SELECTED_SIZE*dp;
            float y = event.getRawY()- FISH_Y_DRAG_PLACEMENT*SELECTED_SIZE*dp;
            move(x, y);
        }else if(event.getAction() == MotionEvent.ACTION_MOVE){
            //float x = event.getRawX()- dX -(SIZE_DIFF/2*dp);
            //float y = event.getRawY()- dY -(SIZE_DIFF/2*dp);
            float x = event.getRawX()- FISH_X_DRAG_PLACEMENT*SELECTED_SIZE*dp;
            float y = event.getRawY()- FISH_Y_DRAG_PLACEMENT*SELECTED_SIZE*dp;
            move(x, y);

            if(y+SELECTED_SIZE > context.container.getLayout().getY())
                context.container.fishDragged(x, SELECTED_SIZE);
            else
                context.container.fishDraggedOut();
        }else if(event.getAction() == MotionEvent.ACTION_UP){
            dragged = false;
            imageView.requestLayout();
            imageView.getLayoutParams().width = (int) (SIZE*dp);
            imageView.getLayoutParams().height = (int) (SIZE*dp);
            float x = imageView.getX()+(SIZE_DIFF/2*dp);
            float y = imageView.getY()+(SIZE_DIFF/2*dp);
            if(y+SELECTED_SIZE > context.container.getLayout().getY()) {
                if(!context.container.fishDrop(this, x, SIZE*dp))
                    context.container.fishDragged(x, SIZE*dp);
                else destroy();
            }
            move(x, Math.min(y, context.container.getLayout().getY()-(SIZE*dp)));
            context.container.fishDraggedOut();
        }

        return true;
    }

    private void render(){
        context.fishLayout.addView(imageView);
    }

    private void move(float x, float y){
        context.runOnUiThread(() -> {
            imageView.setX(x);
            imageView.setY(y);
        });
    }

    private void rotationY(float y){
        context.runOnUiThread(() -> {
            imageView.setRotationY(y);
        });
    }

    private void rotation(float rotation){
        context.runOnUiThread(() -> {
            imageView.setRotation(rotation);
        });
    }

    public FishGameDrawable getColor(){
        return color;
    }


}
