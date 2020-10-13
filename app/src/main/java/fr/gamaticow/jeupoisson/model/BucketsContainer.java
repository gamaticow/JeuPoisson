package fr.gamaticow.jeupoisson.model;

import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import fr.gamaticow.jeupoisson.controller.GameActivity;

/**
 * Created by Corentin COUTEAUX on 30/09/2020 at 13:54.
 */

public class BucketsContainer {

    private GameActivity context;
    private FishGameLevel level;
    private LinearLayout layout;

    private List<Bucket> buckets;
    private Bucket animated;

    public BucketsContainer(GameActivity context, FishGameLevel level, LinearLayout layout){
        this.context = context;
        this.level = level;
        this.layout = layout;
        this.buckets = new ArrayList<>();
    }

    public GameActivity getContext(){
        return context;
    }

    protected void addBucket(Bucket bucket){
        buckets.add(bucket);
        layout.addView(bucket.getLinearLayout());
    }

    protected LinearLayout getLayout() {
        return layout;
    }

    public void fishDragged(float x, float width){
        Bucket newAnimated = null;
        for(Bucket bucket : buckets){
            if (x+width > getBucketX(bucket) && x < getBucketX(bucket)+bucket.getLinearLayout().getWidth()/bucket.dp)
                newAnimated = bucket;
        }

        if(newAnimated != animated){
            if(animated != null)
                animated.resetAnimation();
            animated = newAnimated;
        }
    }

    public void fishDraggedOut(){
        if(animated != null){
            animated.resetAnimation();
            animated = null;
        }
    }

    public boolean fishDrop(Fish fish, float x, float width){
        for(Bucket bucket : buckets){
            if (x+width > getBucketX(bucket) && x < getBucketX(bucket)+bucket.getLinearLayout().getWidth()/bucket.dp){
                if(bucket.getColor() == fish.getColor()){
                    bucket.scorePoint();
                    checkWin();
                    return true;
                }else{
                    level.removeLife();
                }
            }

        }
        return false;
    }

    private void checkWin(){
        for(Bucket bucket : buckets){
            if(!bucket.isFilled())
                return;
        }

        level.end();
    }

    private float getBucketX(Bucket bucket){
        return layout.getX()+bucket.getLinearLayout().getX();
    }

    public void animate(){
        for(Bucket bucket : buckets){
            if(animated != bucket)
                bucket.resetAnimation();
        }

        if(animated == null)
            return;
        animated.animate();
    }
}
