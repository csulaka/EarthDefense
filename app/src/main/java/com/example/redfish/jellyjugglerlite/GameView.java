package com.example.redfish.jellyjugglerlite;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Redfish on 8/5/2017.
 */

public class GameView extends SurfaceView implements Runnable {
    SharedPreferences sharedPreferences;
    public final static String PREFS_NAME = "JellyJuggler";
    private Thread gameThread;
    Context context;
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;
    private ArrayList<Invaders> invadersArrayList;
    private Invaders invaders;
    private Random rng;
    int screenX;

    boolean playing;

    private boolean isGameOver;
    int score;
    int highScore[] = new int[4];

    public GameView(Context context, int screenX, int screenY){
        super(context);
        surfaceHolder = getHolder();
        paint = new Paint();
        this.screenX = screenX;
        this.context=context;
        isGameOver = false;
        rng=new Random();
        score=0;
        sharedPreferences=context.getSharedPreferences(GameView.PREFS_NAME,Context.MODE_PRIVATE);
        highScore[0] = sharedPreferences.getInt("hiscore1",0);
        highScore[1] = sharedPreferences.getInt("hiscore2",0);
        highScore[2] = sharedPreferences.getInt("hiscore3",0);
        highScore[3] = sharedPreferences.getInt("hiscore4",0);

        invadersArrayList=new ArrayList<Invaders>();
        //TODO Enemies
        for(int i=0;i<3;i++) {
            invadersArrayList.add(invaders = new Invaders(context, 100*rng.nextInt(4)));
        }

    }


    @Override
    public void run() {
        while (playing) {
            update();
            draw();
            control();

        }

    }

    private void update() {
//        for(Invaders invaders:invadersArrayList)
//            invaders.move();

        SharedPreferences.Editor e = sharedPreferences.edit();
        for(int i=0;i<4;i++){
            if(score>sharedPreferences.getInt("hiscore"+i,0)) {
                e.putInt("hiscore" + i, highScore[i]);
                break;
            }
        }
        e.apply();
    }


    private void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(0, PorterDuff.Mode.CLEAR);

            paint.setColor(Color.WHITE);
            paint.setTextSize(100);
            canvas.drawText(""+score,canvas.getWidth()/2-35,100,paint);

            for(Invaders invaders:invadersArrayList)
              canvas.drawBitmap(invaders.getBitmap(),invaders.getX(),invaders.getY(),paint);
            if(isGameOver){
               //TODO Game Over Screen
            }
            surfaceHolder.unlockCanvasAndPost(canvas);

        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int touchX=Math.round(motionEvent.getX());
        int touchY=Math.round(motionEvent.getY());

        switch(motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                System.out.println("Hey touch works");
                for(Invaders invaders:invadersArrayList){
                    if(invaders.getHitBox().contains(touchX,touchY)){
                        System.out.print("Yeah here it is");
                    }
                }
                break;

        }

        if(isGameOver){
            if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                context.startActivity(new Intent(context,MainActivity.class));
            }
        }
        return true;
    }


    private void control() {
        try {
            gameThread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
        }
    }
    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

}
