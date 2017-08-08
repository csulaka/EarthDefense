package com.example.redfish.jellyjugglerlite;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Random;

/**
 * Created by Redfish on 8/6/2017.
 */
// Not actually implemented
public class HealthPack {
    private Bitmap bitmap;
    private int x;
    private int y;
    private Rect hitbox;
    private int speed;
    private Random rng;

    public HealthPack(Context context, int x, int y){
        bitmap= BitmapFactory.decodeResource(context.getResources(),R.drawable.healthpack);
        this.x=x;
        this.y=y;
        speed=1;
        hitbox=new Rect(x,y,x+bitmap.getWidth(),y+bitmap.getHeight());
    }

    public void update(){
        y+=speed;

        hitbox.set(x,y,x+bitmap.getWidth(),y+bitmap.getHeight());
    }
    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Rect getHitbox() {
        return hitbox;
    }

    public void setHitbox(Rect hitbox) {
        this.hitbox = hitbox;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
