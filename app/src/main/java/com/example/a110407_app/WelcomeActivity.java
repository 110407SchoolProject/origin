package com.example.a110407_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.example.a110407_app.ui.login.LoginActivity;

public class WelcomeActivity extends AppCompatActivity {
    private ImageView image;
    private int nowPicPos = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        mHandler.sendEmptyMessageAtTime(GOTO_LOGIN_ACTIVITY,2000);
        image = (ImageView) findViewById(R.id.pizza);
        image.setImageResource(R.drawable.pizza);
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