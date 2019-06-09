package com.example.powerpong;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PongGame2 {

    private int mPaddleWidth;
    private int mPaddleHeight;

    private int mUserPaddleX;
    private int mUserPaddleY;

//    private int mOppPaddleX;
//    private int mOppPaddleY;

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

    private FirebaseUser mUser;
    private DatabaseReference mRef;

    private boolean reset;

    private Paint paint;

    private String mGameCode;

    private DatabaseReference mGameRef;

    private Boolean isPlayerOne;

    private DatabaseReference bXRef;
    private DatabaseReference bYRef;

    public int vXDir;
    public int vYDir;

    public DatabaseReference vXRef;
    public DatabaseReference vYRef;

    public int VELOCITY;




    public PongGame2(Context context, int height, int width,
                    FirebaseUser user, final FirebaseDatabase database){

        Log.d("tribe", "i think i made it");
        mScreenWidth = width;
        mScreenHeight = height;

        vXDir = 1;
        vYDir = 1;

//        Log.d("height", "" + mScreenHeight);
//        Log.d("width","" + mScreenWidth);

        mUserPaddleY = (int)(mScreenHeight *.95);
//        mOppPaddleY = (int)(mScreenHeight *.05);

        mUserPaddleX = (int)(mScreenWidth *.5);
//        mOppPaddleX = (int)(mScreenWidth *.5);

        mBallX = (int)(mScreenWidth*0.5);
        mBallY = (int)(mScreenHeight*0.5);


        mLeftControlX = (int)(mScreenWidth*0.1);
        mRightControlX = (int)(mScreenWidth*0.9);

        mControlRadius = (int)(mScreenWidth*0.08);

        mBallVX = (int)(mScreenWidth*0.004);
        mBallVY = (int)(mScreenWidth*0.004);

        VELOCITY = (int)(mScreenWidth*0.004);

        mBallRadius = (int)(mScreenWidth*0.02);

        mPaddleWidth = (int)(mScreenWidth*.2);
        mPaddleHeight = (int)(mScreenHeight*.05);

        mPaddleVelocity = mBallVX;

        mUserScore = 0;
        mOppScore = 0;

        reset = true;

        mFill = false;

        mUser = user;
        mRef = database.getReference("users/" + user.getUid() + "/score");

        DatabaseReference temp = database.getReference("users/" + user.getUid() + "/gamecode");

        temp.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mGameCode = (String)(dataSnapshot.getValue());
                Log.d("gamecodklfasld", mGameCode);
                bXRef = database.getReference("games/" + mGameCode + "/bx");
                bYRef = database.getReference("games/" + mGameCode + "/by");
//        }
//
                vXRef = database.getReference("games/" + mGameCode + "/vx");
                vYRef = database.getReference("games/" + mGameCode + "/vy");


                DatabaseReference mGameRef = database.getReference("games/" + mGameCode);

                mGameRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot child : dataSnapshot.getChildren()) {

                            //this finds whether we're player one
                            if(isPlayerOne == null && child.getValue().equals(mUser.getUid())){
                                isPlayerOne = true;
                            }
                            else if(isPlayerOne == null){
                                isPlayerOne = false;
                            }


                            if(child.getKey().equals("bx")) {
                                if(isPlayerOne){
                                    mBallX = mScreenWidth * (int) (Integer.parseInt((String)(child.getValue(String.class)))/ 100.0);
                                }
                                else {
                                    mBallX = mScreenWidth - mScreenWidth * (int) (Integer.parseInt((String)(child.getValue(String.class)))/ 100.0);
                                }
                            }
                            else if(child.getKey().equals("by")) {
                                if(isPlayerOne) {
                                    mBallY = mScreenHeight - 2 * mScreenHeight * (int) (Integer.parseInt((String)(child.getValue(String.class)))/ 100.0);
                                }
                                else{
                                    mBallY = mScreenHeight - (2 * mScreenHeight - 2 * mScreenHeight * (int) (Integer.parseInt((String)(child.getValue(String.class)))/ 100.0));
                                }
                                mBallY = 0;

                            }

                            if(isPlayerOne && child.getKey().equals("vx")){
                                vXDir = (Integer.parseInt((String)(child.getValue(String.class))));
                                mBallVX = VELOCITY * vXDir;
                            }
                            else if(child.getKey().equals("vx")){
                                vXDir = (Integer.parseInt((String)(child.getValue(String.class))));
                                mBallVX = -1 * VELOCITY * vXDir;
                            }

                            if(isPlayerOne && child.getKey().equals("vy")){
                                vYDir = (Integer.parseInt((String)(child.getValue(String.class))));
                                mBallVY = VELOCITY * vYDir;
                            }
                            else if(child.getKey().equals("vy")){
                                vYDir =  (Integer.parseInt((String)(child.getValue(String.class))));
                                mBallVY = -1 * VELOCITY * vYDir;
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




//        while(mGameCode == null){

//
//
        paint = new Paint();
        paint.setTextAlign(Paint.Align.CENTER);
        Typeface ac = Typeface.createFromAsset(context.getAssets(), "fonts/ac.ttf");
        paint.setTypeface(ac);
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
//        canvas.drawRect(mOppPaddleX - mPaddleWidth/2,mOppPaddleY+mPaddleHeight/2,
//                mOppPaddleX + mPaddleWidth/2,mOppPaddleY-mPaddleHeight/2, paint);


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
                < Math.pow(mControlRadius + 5, 2) && mUserPaddleX <= mScreenWidth) {
            mUserPaddleX += mPaddleVelocity;
            mFill = true;
        } else if(Math.pow(mLeftControlX-x,2) + Math.pow(mScreenHeight/2 - y, 2)
                < Math.pow(mControlRadius + 5, 2) && mUserPaddleX >= 0){
            mUserPaddleX -= mPaddleVelocity;
            mFill = true;
        }



    }

    public void setBx(int bx){
        bXRef.setValue(bx + "");
        if(isPlayerOne){
            mBallX = mScreenWidth * ((int) (bx / 100.0));
        }
        else {
            mBallX = mScreenWidth - mScreenWidth * ((int) (bx / 100.0));
        }
    }

    public void setBy(int by){
        bYRef.setValue(by + "");
        if(isPlayerOne){
            mBallY = mScreenHeight - 2 * mScreenHeight * ((int) (by / 100.0));
        }
        else{
            mBallY = mScreenHeight - (2 * mScreenHeight - 2 * mScreenHeight * ((int) (by / 100.0)));
        }
        mBallY = 0;
    }

    public void setVx(int vx){
        if(isPlayerOne) {
            vXRef.setValue(vx + "");
        }
        vXDir = (int)(vx);
        if(isPlayerOne){
            mBallVX = VELOCITY * vx;
        }
        else{
            mBallVX = -1 * VELOCITY * vx;
        }
    }

    public void setVy(int vy){
        vYRef.setValue(vy + "");
        vYDir = (int)(vy);
        if(isPlayerOne){
            mBallVY = VELOCITY * vy;
        }
        else{
            mBallVY = -1 * VELOCITY * vy;
        }
    }




    public boolean ballOnMySide(){
        if(mBallY > 0){
            return true;
        }
        return false;
    }

    public void checkBounce(){

//        if(ballOnMySide() && mBallY - mBallRadius <= 0){
        if(ballOnMySide() && mBallY + mBallRadius >= mScreenHeight){
            mUserScore++;
            reset = true;
            setBx(50);
            setBy(25);


            mRef.addListenerForSingleValueEvent(new ValueEventListener(){
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Get Post object and use the values to update the UI
                    long score = (long)(dataSnapshot.getValue());

                    mRef.setValue(score + 1);
                    // ...
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    // ...
                }
            });
        }
//        else if(mBallY + mBallRadius >= mScreenHeight){
//            mOppScore++;
//            reset = true;
//            mBallX = mScreenWidth / 2;
//            mBallY = mScreenHeight / 2;
//            mRef.addListenerForSingleValueEvent(new ValueEventListener(){
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    // Get Post object and use the values to update the UI
//                    long score = (long)(dataSnapshot.getValue());
//
//                    mRef.setValue(score - 1);
//                    // ...
//                }
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//                    // Getting Post failed, log a message
//                    // ...
//                }
//            });
//        }

        if(mBallY + mBallRadius >= mUserPaddleY - mPaddleHeight/2 &&
                mBallX < mUserPaddleX + mPaddleWidth/2 && mBallX > mUserPaddleX - mPaddleWidth/2){
            setVy(-1*vYDir);
        }
//        if(mBallY - mBallRadius <= mOppPaddleY + mPaddleHeight/2 &&
//                mBallX < mOppPaddleX + mPaddleWidth/2 && mBallX > mOppPaddleX - mPaddleWidth/2){
//            mBallVY *= -1;
//        }
        if(mBallX - mBallRadius <= 0){
            setVx(-1*vXDir);
        }
        if(mBallX + mBallRadius >= mScreenWidth){
            setVx(-1*vXDir);
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

        mBallY = 0;
        Log.d("xloc", mBallX + "");
        Log.d("yloc", mBallY + "");

//        mOppPaddleX += mBallVX*3/4;

        checkBounce();
//        }
    }

    public void displayScore(Canvas canvas){
//        if(reset) {
        Log.d("tribe", "girl i can't explain it");
        paint.setColor(Color.parseColor("#EAEAEA"));
        paint.setTextSize(90);

        canvas.drawText(mUserScore + " - " + mOppScore, mScreenWidth / 2, mScreenHeight / 2, paint);
//            try {
//                Thread.sleep(5000);
//            } catch (Exception e) {
//                Log.d("thread can't sleep", e + "");
//            }
//            reset = false;
//        }
    }

    public void draw(Canvas canvas){
        Log.d("tribe", "I think i made it cuz i'm always smiling");
        canvas.drawColor(Color.parseColor("#031927"));
        displayScore(canvas);
        drawBall(canvas);
        drawPaddles(canvas);
        drawControls(canvas);

        Log.d("imma", isPlayerOne + "");
    }


}
