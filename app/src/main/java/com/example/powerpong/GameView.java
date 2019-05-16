package com.example.powerpong;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private MainThread thread;

    private PongGame pongGame;

    private MotionEvent event;
    public GameView(Context context) {
        super(context);

        //the callback allows us to catch different events when they are called
        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);
        setFocusable(true);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        pongGame = new PongGame(displayMetrics.heightPixels, displayMetrics.widthPixels);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //put through loop bc it might take multiple times to stop the thread
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    public void update() {

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev){
        if(ev.getAction() == MotionEvent.ACTION_DOWN) {
            event = ev;
        }
        if(ev.getAction() == MotionEvent.ACTION_UP){
            event = null;
        }
//        pongGame.draw(canvas);
        return true;
    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        pongGame.runGame(canvas, event);

    }
}
