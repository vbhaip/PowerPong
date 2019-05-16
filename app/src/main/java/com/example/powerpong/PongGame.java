package com.example.powerpong;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;

public class PongGame {

    private int mPaddleWidth;
    private int mPaddleHeight;

    private int mUserPaddleX;
    private int mUserPaddleY;

    private int mOppPaddleX;
    private int mOppPaddleY;

    private int mBallX;
    private int mBallY;

    private int mBallVX;
    private int mBallVY;

    private int mBallRadius;

    private int mScreenWidth;
    private int mScreenHeight;

    private int mPaddleVelocity;

    private int mLeftControlX;
    private int mRightControlX;

    private int mControlRadius;

    private boolean mFill;

    private int mUserScore;
    private int mOppScore;

    private Paint paint;


    public PongGame(int height, int width){

        mScreenWidth = width;
        mScreenHeight = height;

        Log.d("height", "" + mScreenHeight);
        Log.d("width","" + mScreenWidth);

        mUserPaddleY = (int)(mScreenHeight *.95);
        mOppPaddleY = (int)(mScreenHeight *.05);

        mUserPaddleX = (int)(mScreenWidth *.5);
        mOppPaddleX = (int)(mScreenWidth *.5);

        mBallX = (int)(mScreenWidth*0.5);
        mBallY = (int)(mScreenHeight*0.5);


        mLeftControlX = (int)(mScreenWidth*0.1);
        mRightControlX = (int)(mScreenWidth*0.9);

        mControlRadius = 75;

        mBallVX = 2;
        mBallVY = 2;

        mBallRadius = 25;

        mPaddleWidth = (int)(mScreenWidth*.2);
        mPaddleHeight = (int)(mScreenHeight*.05);

        mPaddleVelocity = 4;

        mUserScore = 0;
        mOppScore = 0;

        mFill = false;

        paint = new Paint();
    }

    public void drawBall(Canvas canvas){
        paint.setColor(Color.parseColor("#EAEAEA"));
        canvas.drawCircle(mBallX, mBallY, mBallRadius, paint);
    }

    public void drawPaddles(Canvas canvas){

        paint.setColor(Color.parseColor("#9AC4F8"));
        canvas.drawRect(mUserPaddleX - mPaddleWidth/2,mUserPaddleY+mPaddleHeight/2,
                mUserPaddleX + mPaddleWidth/2,mUserPaddleY-mPaddleHeight/2, paint);

        paint.setColor(Color.parseColor("#CC5245"));
        canvas.drawRect(mOppPaddleX - mPaddleWidth/2,mOppPaddleY+mPaddleHeight/2,
                mOppPaddleX + mPaddleWidth/2,mOppPaddleY-mPaddleHeight/2, paint);


    }

    public void drawControls(Canvas canvas){
        paint.setColor(Color.parseColor("#C6D2ED"));
//        if(!mFill) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
//        }
        canvas.drawCircle(mLeftControlX, mScreenHeight/2, mControlRadius, paint);
        canvas.drawCircle(mRightControlX, mScreenHeight/2, mControlRadius, paint);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
    }


    public void onTouch(MotionEvent ev){

            float x = ev.getX();
            float y = ev.getY();
//        float y = ev.getY();
            if (Math.pow(mRightControlX-x,2) + Math.pow(mScreenHeight/2 - y, 2)
                    < Math.pow(mControlRadius + 5, 2)) {
                mUserPaddleX += mPaddleVelocity;
                mFill = true;
            } else if(Math.pow(mLeftControlX-x,2) + Math.pow(mScreenHeight/2 - y, 2)
                    < Math.pow(mControlRadius + 5, 2)){
                mUserPaddleX -= mPaddleVelocity;
                mFill = true;
            }



    }


    public void checkBounce(){

        if(mBallY - mBallRadius == 0){
            score(true);
        }
        if(mBallY + mBallRadius == mScreenHeight){
            score(false);
        }

        if(mBallY + mBallRadius >= mUserPaddleY - mPaddleHeight/2 &&
        mBallX < mUserPaddleX + mPaddleWidth/2 && mBallX > mUserPaddleX - mPaddleWidth/2){
            mBallVY *= -1;
        }
        if(mBallY - mBallRadius <= mOppPaddleY + mPaddleHeight/2 &&
                mBallX < mOppPaddleX + mPaddleWidth/2 && mBallX > mOppPaddleX - mPaddleWidth/2){
            mBallVY *= -1;
        }
        if(mBallX - mBallRadius <= 0){
            mBallVX *= -1;
        }
        if(mBallX + mBallRadius >= mScreenWidth){
            mBallVX *= -1;
        }
    }

    public void runGame(Canvas canvas, MotionEvent event) {
//        if (canvas != null) {
        if(event != null){
            onTouch(event);
        }

        draw(canvas);

        mBallX += mBallVX;
        mBallY += mBallVY;

        checkBounce();
//        }
    }

    public void draw(Canvas canvas){
        canvas.drawColor(Color.parseColor("#031927"));
        drawBall(canvas);
        drawPaddles(canvas);
        drawControls(canvas);
    }


}
