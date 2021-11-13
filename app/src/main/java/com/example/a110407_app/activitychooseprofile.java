package com.example.a110407_app;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class activitychooseprofile extends AppCompatActivity {
    Button boy1,boy2,boy3,girl1,btnsave,girl2,girl3;
    String SHARED_PREFS = "codeTheme";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activitychooseprofile);
        //Get ID of all elements
        btnsave = findViewById(R.id.btnsave);
        boy1 = findViewById(R.id.boy1);
        boy2 = findViewById(R.id.boy2);
        boy3 = findViewById(R.id.boy3);
        girl1 = findViewById(R.id.girl1);
        girl2 = findViewById(R.id.girl2);
        girl3 = findViewById(R.id.girl3);

        //      set the function of theme changer
        boy2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //scale animation
                boy2.animate().translationY(20).scaleX(1.5f).scaleY(1.5f).setDuration(800).start();
                // default button scales
                boy1.animate().translationY(0).scaleX(1).scaleY(1).setDuration(350).start();
                boy3.animate().translationY(0).scaleX(1).scaleY(1).setDuration(350).start();
                girl1.animate().translationY(0).scaleX(1).scaleY(1).setDuration(350).start();
                girl2.animate().translationY(0).scaleX(1).scaleY(1).setDuration(350).start();
                girl3.animate().translationY(0).scaleX(1).scaleY(1).setDuration(350).start();
                // change button color
                btnsave.setTextColor(Color.parseColor("#3498db"));
            }
        });

        boy1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //scale animation
                boy1.animate().scaleX(1.5f).scaleY(1.5f).setDuration(800).start();
                //default button scales
                boy2.animate().translationY(0).scaleX(1).scaleY(1).setDuration(350).start();
                boy3.animate().translationY(0).scaleX(1).scaleY(1).setDuration(350).start();
                girl1.animate().translationY(0).scaleX(1).scaleY(1).setDuration(350).start();
                girl2.animate().translationY(0).scaleX(1).scaleY(1).setDuration(350).start();
                girl3.animate().translationY(0).scaleX(1).scaleY(1).setDuration(350).start();
                // change button color
                btnsave.setTextColor(Color.parseColor("#3498db"));
            }
        });

        boy3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //scale animation
                boy3.animate().scaleX(1.5f).scaleY(1.5f).setDuration(800).start();
                //default button scales
                boy2.animate().translationY(0).scaleX(1).scaleY(1).setDuration(350).start();
                boy1.animate().translationY(0).scaleX(1).scaleY(1).setDuration(350).start();
                girl1.animate().translationY(0).scaleX(1).scaleY(1).setDuration(350).start();
                girl2.animate().translationY(0).scaleX(1).scaleY(1).setDuration(350).start();
                girl3.animate().translationY(0).scaleX(1).scaleY(1).setDuration(350).start();
                // change button color
                btnsave.setTextColor(Color.parseColor("#3498db"));
            }
        });

        girl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //scale animation
                girl1.animate().scaleX(1.5f).scaleY(1.5f).setDuration(800).start();
                //default button scales
                boy2.animate().translationY(0).scaleX(1).scaleY(1).setDuration(350).start();
                boy1.animate().translationY(0).scaleX(1).scaleY(1).setDuration(350).start();
                boy3.animate().translationY(0).scaleX(1).scaleY(1).setDuration(350).start();
                girl2.animate().translationY(0).scaleX(1).scaleY(1).setDuration(350).start();
                girl3.animate().translationY(0).scaleX(1).scaleY(1).setDuration(350).start();
                // change button color
                btnsave.setTextColor(Color.parseColor("#FE3BEC"));
            }
        });

        girl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //scale animation
                girl2.animate().scaleX(1.5f).scaleY(1.5f).setDuration(800).start();
                //default button scales
                boy2.animate().translationY(0).scaleX(1).scaleY(1).setDuration(350).start();
                boy1.animate().translationY(0).scaleX(1).scaleY(1).setDuration(350).start();
                boy3.animate().translationY(0).scaleX(1).scaleY(1).setDuration(350).start();
                girl1.animate().translationY(0).scaleX(1).scaleY(1).setDuration(350).start();
                girl3.animate().translationY(0).scaleX(1).scaleY(1).setDuration(350).start();
                // change button color
                btnsave.setTextColor(Color.parseColor("#FE3BEC"));
                // timer

            }
        });

        girl3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //scale animation
                girl3.animate().scaleX(1.5f).scaleY(1.5f).setDuration(800).start();
                //default button scales
                boy2.animate().translationY(0).scaleX(1).scaleY(1).setDuration(350).start();
                boy1.animate().translationY(0).scaleX(1).scaleY(1).setDuration(350).start();
                boy3.animate().translationY(0).scaleX(1).scaleY(1).setDuration(350).start();
                girl1.animate().translationY(0).scaleX(1).scaleY(1).setDuration(350).start();
                girl2.animate().translationY(0).scaleX(1).scaleY(1).setDuration(350).start();
                // change button color
                btnsave.setTextColor(Color.parseColor("#FE3BEC"));
                // timer

            }
        });


    }
}