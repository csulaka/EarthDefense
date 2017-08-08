package com.example.redfish.jellyjugglerlite;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Redfish on 8/6/2017.
 */

public class Explosion {
    private int x;
    private int y;
    private Bitmap currentframe,frame1,frame2,frame3,frame4,frame5,frame6;


    public Explosion(Context context, int x, int y){
        frame1= BitmapFactory.decodeResource(context.getResources(),R.drawable.poof0);
        frame2= BitmapFactory.decodeResource(context.getResources(),R.drawable.poof1);
        frame3= BitmapFactory.decodeResource(context.getResources(),R.drawable.poof2);
        frame4= BitmapFactory.decodeResource(context.getResources(),R.drawable.poof3);
        frame5= BitmapFactory.decodeResource(context.getResources(),R.drawable.poof4);
        frame6= BitmapFactory.decodeResource(context.getResources(),R.drawable.poof5);
        currentframe=frame1;
        this.x=x;
        this.y=y;
    }

    public void update(int frame){
       switch(frame) {
           case 0:
               currentframe = frame2;
               break;
           case 1:
               currentframe = frame3;
               break;
           case 2:
               currentframe = frame4;
               break;
           case 3:
               currentframe = frame5;
               break;
           case 4:
               currentframe = frame6;
               break;
       }
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

    public Bitmap getCurrentframe() {
        return currentframe;
    }

    public void setCurrentframe(Bitmap currentframe) {
        this.currentframe = currentframe;
    }
}
