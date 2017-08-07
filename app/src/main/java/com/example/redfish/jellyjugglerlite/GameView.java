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
import android.provider.MediaStore;
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
    private Invaders invaders;
    private Random rng;
    int screenX;
    private MediaPlayer explosion;
    private MediaPlayer music;

    private Bitmap health1,health2,health3;
    private Bitmap gameOver;
    public int runOnce;
    boolean playing;

    private boolean isGameOver;
    int score;
    int lives;
    int highScore[] = new int[4];

//    private AnimationDrawable explosionAnimation;
    //TODO List
    // 1. Game Over DONE
    //2.  Explosions Done
    //3. sounds DONE
    //3. Admob
    public GameView(Context context, int screenX, int screenY){
        super(context);
        surfaceHolder = getHolder();
        paint = new Paint();
        this.screenX = screenX;
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

        explosion=MediaPlayer.create(context,R.raw.explosion);
        if(sharedPreferences.getBoolean("musicEnable",true))
            BackgroundMusic.musicPlaying(context);
        gameOver=BitmapFactory.decodeResource(context.getResources(),R.drawable.gameover);
        health1=BitmapFactory.decodeResource(context.getResources(),R.drawable.health1);
        health2=BitmapFactory.decodeResource(context.getResources(),R.drawable.health2);
        health3=BitmapFactory.decodeResource(context.getResources(),R.drawable.health3);
        invadersArrayList=new ArrayList<Invaders>();
//        explosionAnimation=(AnimationDrawable)context.getResources().getDrawable(R.drawable.explosion);
        //TODO Enemies
        for(int i=0;i<3;i++) {
            invadersArrayList.add(invaders = new Invaders(context, 100*rng.nextInt(6)));
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
        for(Invaders invaders:invadersArrayList) {
            invaders.move();
            if(Math.round(invaders.getY())>650){
                lives--;
                if(!isGameOver) {
                    if (sharedPreferences.getBoolean("soundEnable", true))
                        explosion.start();
                }
                invaders.setX(rng.nextInt(6)*100);
                invaders.setY((rng.nextInt(2)+1)*-200);
                invaders.getHitBox().set(invaders.getX(),invaders.getY(),invaders.getX()+invaders.getBitmap().getWidth(),invaders.getY()+invaders.getBitmap().getHeight());
            }
        }

        if(score%10==0&&score>0){
            score++;
            for(Invaders invaders:invadersArrayList)
                invaders.setSpeed(invaders.getSpeed()+1);
        }
        if(score%40==0&&score>0){
            score++;
            invadersArrayList.add(invaders = new Invaders(context, 100*rng.nextInt(7)));
        }

    }


    private void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(0, PorterDuff.Mode.CLEAR);

            paint.setColor(Color.WHITE);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTextSize(100);
            canvas.drawText(""+score,canvas.getWidth()/2,140,paint);
            //TODO Draw Lives Done
            if(lives==3)
                canvas.drawBitmap(health3,canvas.getWidth()-health3.getWidth(),920,paint);
            else if(lives==2)
                canvas.drawBitmap(health2,canvas.getWidth()-health2.getWidth(),920,paint);
            else if(lives==1)
                canvas.drawBitmap(health2,canvas.getWidth()-health1.getWidth(),920,paint);
            else if(lives<1) {
                isGameOver = true;
            }
            for(Invaders invaders:invadersArrayList)
              canvas.drawBitmap(invaders.getBitmap(),invaders.getX(),invaders.getY(),paint);

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
                          runOnce--;
                      }
                BackgroundMusic.stopMusic();
                canvas.drawBitmap(gameOver,canvas.getWidth()/2- gameOver.getWidth()/2,200,paint);
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
                        System.out.println("Yeah here it is");
                        score++;
                        invaders.setX(rng.nextInt(7)*100);
                        invaders.setY((rng.nextInt(2)+1)*-200);
                        invaders.getHitBox().set(invaders.getX(),invaders.getY(),invaders.getX()+invaders.getBitmap().getWidth(),invaders.getY()+invaders.getBitmap().getHeight());
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


