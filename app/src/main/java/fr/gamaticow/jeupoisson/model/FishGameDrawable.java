package fr.gamaticow.jeupoisson.model;

import android.app.Activity;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import fr.gamaticow.jeupoisson.R;

/**
 * Created by Corentin COUTEAUX on 30/09/2020 at 16:48.
 */

public enum FishGameDrawable {

    RED     (   R.string.red,       false,  R.drawable.fish_red,    R.drawable.bucket_red, R.drawable.bucket_red1, R.drawable.bucket_red2, R.drawable.bucket_red3, R.drawable.bucket_red4, R.drawable.bucket_red5),
    BLUE    (   R.string.blue,      false,  R.drawable.fish_blue,   R.drawable.bucket_blue, R.drawable.bucket_blue1, R.drawable.bucket_blue2, R.drawable.bucket_blue3, R.drawable.bucket_blue4, R.drawable.bucket_blue5),
    YELLOW  (   R.string.yellow,    false,  R.drawable.fish_yellow, R.drawable.bucket_yellow, R.drawable.bucket_yellow1, R.drawable.bucket_yellow2, R.drawable.bucket_yellow3, R.drawable.bucket_yellow4, R.drawable.bucket_yellow5    ),
    BROWN   (   R.string.brown,     true,   R.drawable.fish_brown,  R.drawable.bucket_brown, R.drawable.bucket_brown1, R.drawable.bucket_brown2, R.drawable.bucket_brown3, R.drawable.bucket_brown4, R.drawable.bucket_brown5     ),
    ORANGE  (   R.string.orange,    false,  R.drawable.fish_orange, R.drawable.bucket_orange, R.drawable.bucket_orange1, R.drawable.bucket_orange2, R.drawable.bucket_orange3, R.drawable.bucket_orange4, R.drawable.bucket_orange5    ),
    GREEN   (   R.string.green,     false,  R.drawable.fish_green,  R.drawable.bucket_green, R.drawable.bucket_green1, R.drawable.bucket_green2, R.drawable.bucket_green3, R.drawable.bucket_green4, R.drawable.bucket_green5     ),
    WHITE   (   R.string.white,     true,   R.drawable.fish_white,  R.drawable.bucket_white     ),
    PINK    (   R.string.pink,      false,  R.drawable.fish_pink,   R.drawable.bucket_pink, R.drawable.bucket_pink1, R.drawable.bucket_pink2, R.drawable.bucket_pink3, R.drawable.bucket_pink4, R.drawable.bucket_pink5      ),
    PURPLE  (   R.string.purple,    false,  R.drawable.fish_purple, R.drawable.bucket_purple, R.drawable.bucket_purple1, R.drawable.bucket_purple2, R.drawable.bucket_purple3, R.drawable.bucket_purple4, R.drawable.bucket_purple5    ),
    GREY    (   R.string.grey,      true,   R.drawable.fish_grey,   R.drawable.bucket_grey, R.drawable.bucket_grey1, R.drawable.bucket_grey2, R.drawable.bucket_grey3, R.drawable.bucket_grey4, R.drawable.bucket_grey5      ),
    BLACK   (   R.string.black,     true,   R.drawable.fish_black,  R.drawable.bucket_black     );

    private static List<FishGameDrawable> easyColors = new ArrayList<>();
    private static List<FishGameDrawable> hardColors = new ArrayList<>();
    static {
        for(FishGameDrawable color : FishGameDrawable.values()){
            if(color.hardColor)
                hardColors.add(color);
            else
                easyColors.add(color);
        }
    }

    private int name;
    private boolean hardColor;
    private int fish;
    private int[] buckets;

    FishGameDrawable(int name, boolean hardColor, int fish, int... buckets) {
        this.name = name;
        this.hardColor = hardColor;
        this.fish = fish;
        this.buckets = buckets;
    }

    public int getFish(){
        return fish;
    }

    public int getBucket(int fillLevel){
        int index = Math.max(0, Math.min(fillLevel, buckets.length - 1));
        return buckets[index];
    }

    public static FishGameDrawable getRandomColor(){
        int random = (int) (Math.random()* FishGameDrawable.values().length);
        return FishGameDrawable.values()[random];
    }

    public static FishGameDrawable getRandomEasyColor(){
        int random = (int) (Math.random()* easyColors.size());
        return easyColors.get(random);
    }

    public static FishGameDrawable getRandomHardColor(){
        int random = (int) (Math.random()* hardColors.size());
        return hardColors.get(random);
    }

    public String getName(Activity context) {
        return context.getResources().getString(name);
    }
}
