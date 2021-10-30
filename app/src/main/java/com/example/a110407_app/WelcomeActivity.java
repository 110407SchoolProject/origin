package com.example.a110407_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.example.a110407_app.ui.login.LoginActivity;

public class WelcomeActivity extends AppCompatActivity {
    private ImageView image;
    private int nowPicPos = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


        image = (ImageView) findViewById(R.id.welcomeView);
        image.setImageResource(R.drawable.heart_pencil);


        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(2000);
                    startActivity(new Intent().setClass(WelcomeActivity.this,LoginActivity.class));
                    mHandler.sendEmptyMessageAtTime(GOTO_LOGIN_ACTIVITY,1000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void FadeOutAndHideImage(final ImageView image){
        Animation fadeout = new AlphaAnimation(1,0);
        fadeout.setInterpolator(new AccelerateInterpolator());
        fadeout.setDuration(1000);
        fadeout.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                nowPicPos%=2;
                image.setImageResource(R.drawable.pizza);
                nowPicPos++;
                FadeOutAndHideImage(image);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        image.startAnimation(fadeout);
    }

    private void FadeInShowImage(final ImageView image){
        Animation fadein = new AlphaAnimation(0,1);
        fadein.setInterpolator(new AccelerateInterpolator());
        fadein.setDuration(5000);
        fadein.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        image.startAnimation(fadein);
    }

    private static final int GOTO_LOGIN_ACTIVITY = 0;

    private Handler mHandler =  new Handler(){
        public void handleMessage(android.os.Message msg){
            switch (msg.what){
                case GOTO_LOGIN_ACTIVITY:
                    Intent intent = new Intent();
                    intent.setClass(WelcomeActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                default:
                    break;

            }
        }
    };

}