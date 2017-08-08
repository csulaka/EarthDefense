package com.example.redfish.jellyjugglerlite;

import android.content.AsyncTaskLoader;
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
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.net.MulticastSocket;
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
    private Random rng;
    int screenX;
    int screenY;
    private SoundPool soundPool;
    private int explosion;

    private Bitmap health1,health2,health3;
    private Bitmap gameOver;
    public int runOnce;
    boolean playing;

    private HealthPack healthPack;
    private boolean isGameOver;
    int score;
    int lives;
    int highScore[] = new int[4];

    private Bitmap poof0,poof1,poof2,poof3,poof4,poof5;
    //TODO List
    public GameView(Context context, int screenX, int screenY){
        super(context);
        surfaceHolder = getHolder();
        paint = new Paint();
        this.screenX = screenX;
        this.screenY=screenY;
        this.context=context;
        isGameOver = false;
        rng=new Random();
        lives=3;
        score=0;
        runOnce=1;
        sharedPreferences=context.getSharedPreferences(GameView.PREFS_NAME,Context.MODE_PRIVATE);
        highScore[0] = sharedPreferences.getInt("hiscore1",0);
        highScore[1] = sharedPreferences.getInt("hiscore2",0);
        highScore[2] = sharedPreferences.getInt("hiscore3",0);
        highScore[3] = sharedPreferences.getInt("hiscore4",0);

        soundPool=new SoundPool(5,AudioManager.STREAM_MUSIC,0);
        explosion=soundPool.load(context,R.raw.explosion,1);
        healthPack=new HealthPack(context,rng.nextInt(screenX-100),0);


        //WIP Explosion TODO
        gameOver=BitmapFactory.decodeResource(context.getResources(),R.drawable.gameover);
        health1=BitmapFactory.decodeResource(context.getResources(),R.drawable.health1);
        health2=BitmapFactory.decodeResource(context.getResources(),R.drawable.health2);
        health3=BitmapFactory.decodeResource(context.getResources(),R.drawable.health3);
        invadersArrayList=new ArrayList<Invaders>();

        poof0 = BitmapFactory.decodeResource(context.getResources(), R.drawable.poof0);
        poof1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.poof1);
        poof2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.poof2);
        poof3 = BitmapFactory.decodeResource(context.getResources(), R.drawable.poof3);
        poof4 = BitmapFactory.decodeResource(context.getResources(), R.drawable.poof4);
        poof5 = BitmapFactory.decodeResource(context.getResources(), R.drawable.poof5);
        //TODO Enemies
        for(int i=0;i<3;i++) {
            invadersArrayList.add(new Invaders(context, rng.nextInt(screenX-100)));
        }

    }


    @Override
    public void run() {
        while (playing) {
            update();
            draw();
            control();
            Log.i("time",""+SystemClock.currentThreadTimeMillis());
        }

    }

    private void update() {
        healthPack.update();

        for(Invaders invaders:invadersArrayList) {
            invaders.move();
        }
        if(score%10==0&&score>0){
            score++;
            for(Invaders invaders:invadersArrayList)
                invaders.setSpeed(invaders.getSpeed()+1);
        }
        if(score%50==0&&score>0){
            score++;
            invadersArrayList.add(new Invaders(context, rng.nextInt(screenX-100)));
        }

    }


    private void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(0, PorterDuff.Mode.CLEAR);

            paint.setColor(Color.WHITE);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTextSize(screenX*screenY/10000);
            canvas.drawText(""+score,canvas.getWidth()/2,140,paint);
            //TODO Draw Lives Done
            if(lives==3)
                canvas.drawBitmap(health3,canvas.getWidth()-health3.getWidth(),Math.round(0.72*screenY),paint);
            else if(lives==2)
                canvas.drawBitmap(health2,canvas.getWidth()-health2.getWidth(),Math.round(0.72*screenY),paint);
            else if(lives==1)
                canvas.drawBitmap(health2,canvas.getWidth()-health1.getWidth(),Math.round(0.72*screenY),paint);
            else if(lives<1) {
                isGameOver = true;
            }
            if (Math.round(healthPack.getY()) > Math.round(screenY/2)){
                healthPack.setX(rng.nextInt(screenX-100));
                healthPack.setY((rng.nextInt(2)+1)*-200);
                healthPack.getHitbox().set(healthPack.getX(),healthPack.getY(),healthPack.getX()+healthPack.getBitmap().getWidth(),healthPack.getY()+healthPack.getBitmap().getHeight());
            }
            for(Invaders invaders:invadersArrayList) {
                canvas.drawBitmap(invaders.getBitmap(), invaders.getX(), invaders.getY(), paint);
                if (Math.round(invaders.getY()) > Math.round(screenY/2)) {
                    lives--;

                    if (!isGameOver) {
                        if (sharedPreferences.getBoolean("soundEnable", true))
                            soundPool.play(explosion, 1, 1, 0, 0, 1);
                    }
                    invaders.setX(rng.nextInt(screenX-100));
                    invaders.setY((rng.nextInt(2) + 1) * -200);
                    invaders.getHitBox().set(invaders.getX(), invaders.getY(), invaders.getX() + invaders.getBitmap().getWidth(), invaders.getY() + invaders.getBitmap().getHeight());
                }
            }

            canvas.drawBitmap(healthPack.getBitmap(),healthPack.getX(),healthPack.getY(),paint);

//            int i=0;
//            for(i=0;i<6;i++)
            switch(Math.round(SystemClock.currentThreadTimeMillis()/200)) {
                case 1:
                    canvas.drawBitmap(poof0, 0, 0, paint) ;
                    break;
                case 2:
                    canvas.drawBitmap(poof1, 0, 0, paint) ;
                    break;
                case 3:
                    canvas.drawBitmap(poof2, 0, 0, paint) ;
                    break;
                case 4:
                    canvas.drawBitmap(poof3, 0, 0, paint) ;
                    break;
                case 5:
                    canvas.drawBitmap(poof4, 0, 0, paint) ;
                    break;
                case 6:
                    canvas.drawBitmap(poof5, 0, 0, paint) ;
                    break;
            }
            if(isGameOver){
                      if(runOnce==1) {
                          for (int i = 0; i < 4; i++) {
                              if (score > highScore[i]) {
                                  highScore[i] = score;
                                  i = 4;
                              }
                          }
                          SharedPreferences.Editor e = sharedPreferences.edit();
                          for (int i = 1; i < 5; i++) {
                              e.putInt("hiscore" + i, highScore[i - 1]);
                          }
                          e.apply();
                          BackgroundMusic.muteMusic();
                          runOnce--;
                      }
                canvas.drawBitmap(gameOver,canvas.getWidth()/2- gameOver.getWidth()/2,Math.round(0.16*screenY),paint);
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
//                System.out.println("Hey touch works");
                for(Invaders invaders:invadersArrayList){
                    if(invaders.getHitBox().contains(touchX,touchY)){
//                        System.out.println("Yeah here it is");
                        if (sharedPreferences.getBoolean("soundEnable", true))
                            soundPool.play(explosion, 1, 1, 0, 0, 1);

                        score++;

                        invaders.setX(rng.nextInt(screenX-100));
                        invaders.setY((rng.nextInt(2)+1)*-200);
                        invaders.getHitBox().set(invaders.getX(),invaders.getY(),invaders.getX()+invaders.getBitmap().getWidth(),invaders.getY()+invaders.getBitmap().getHeight());
                    }
                }
                if(healthPack.getHitbox().contains(touchX,touchY)){
                    if(lives<3&&lives>0){
                        lives++;
                    }
                    healthPack.setX(rng.nextInt(screenX-100));
                    healthPack.setY((rng.nextInt(2)+1)*-200);
                    healthPack.getHitbox().set(healthPack.getX(),healthPack.getY(),healthPack.getX()+healthPack.getBitmap().getWidth(),healthPack.getY()+healthPack.getBitmap().getHeight());
                }

                break;

        }

        if(isGameOver){
            if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                context.startActivity(new Intent(context,MainActivity.class));
                GameActivity activity =(GameActivity)this.context;
                activity.finish();
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


