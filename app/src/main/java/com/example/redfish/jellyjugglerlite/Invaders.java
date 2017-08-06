package com.example.redfish.jellyjugglerlite;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Random;

/**
 * Created by Redfish on 8/5/2017.
 */

public class Invaders {
    private Bitmap bitmap;
    private int x;
    private int y;
    private int speed=4;
    private Random rng;
    private Rect hitBox;
    public Invaders(Context context, int x){
        rng=new Random();
        bitmap= BitmapFactory.decodeResource(context.getResources(),R.drawable.asteroid);
        this.y=(rng.nextInt(2)+1)*-200;
        this.x=x;
        hitBox=new Rect(x,y,x+bitmap.getWidth(),y+bitmap.getHeight());
    }


    public void move(){
        this.y+=speed;

        hitBox.set(x,y,x+bitmap.getWidth(),y+getBitmap().getHeight());
    }
    public Rect getHitBox(){return hitBox;}
    public void setX(int x){this.x=x;}
    public void setY(int y){this.y = y;}
    public Bitmap getBitmap() {
        return bitmap;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public void setSpeed(int speed){this.speed=speed;}
    public int getSpeed() {
        return speed;
    }
}
