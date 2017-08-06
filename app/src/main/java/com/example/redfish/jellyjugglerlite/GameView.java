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
        score=0;
        sharedPreferences=context.getSharedPreferences(GameView.PREFS_NAME,Context.MODE_PRIVATE);
        highScore[0] = sharedPreferences.getInt("hiscore1",0);
        highScore[1] = sharedPreferences.getInt("hiscore2",0);
        highScore[2] = sharedPreferences.getInt("hiscore3",0);
        highScore[3] = sharedPreferences.getInt("hiscore4",0);

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
        for(int i=0;i<4;i++){
            if(highScore[i]<score){
                highScore[i] = score;
                break;
            }
        }
        SharedPreferences.Editor e = sharedPreferences.edit();
        for(int i=0;i<4;i++){
            int j = i+1;
            e.putInt("hiscore"+j,highScore[i]);
        }
        e.apply();
    }


    private void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(0, PorterDuff.Mode.CLEAR);
            Bitmap asdf=BitmapFactory.decodeResource(context.getResources(),R.drawable.back);
            canvas.drawBitmap(asdf,100,100,paint);

            paint.setColor(Color.WHITE);
            paint.setTextSize(100);
            canvas.drawText(""+score,canvas.getWidth()/2-20,100,paint);

            if(isGameOver){
                paint.setTextSize(150);
                paint.setTextAlign(Paint.Align.CENTER);
                int yPos=(int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));
                canvas.drawText("Game Over",canvas.getWidth()/2,yPos,paint);
            }
            surfaceHolder.unlockCanvasAndPost(canvas);

        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_DOWN:
                score++;
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
