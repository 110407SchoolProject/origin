package com.example.a110407_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.a110407_app.Model.Status;
import com.example.a110407_app.Model.User;
import com.example.a110407_app.RetrofitAPI.APIService;
import com.example.a110407_app.RetrofitAPI.RetrofitManager;
import com.example.a110407_app.ui.profile.ProfileFragment;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class activitychooseprofile extends AppCompatActivity {
    Button boy1,boy2,boy3,girl1,btnsave,girl2,girl3;
    String SHARED_PREFS = "codeTheme";
    private int characterNumber;
    private APIService ourAPIService;
    private String userToken;
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

        Intent intent = getIntent();
        userToken = intent.getStringExtra("userToken");
        System.out.println("Token： "+userToken);
        ourAPIService = RetrofitManager.getInstance().getAPI();


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
                characterNumber=2;
                System.out.println(characterNumber);
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
                characterNumber=1;
                System.out.println(characterNumber);
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
                characterNumber=3;
                System.out.println(characterNumber);
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
                characterNumber=4;
                System.out.println(characterNumber);
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
                characterNumber=5;
                System.out.println(characterNumber);
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
                characterNumber=6;
                System.out.println(characterNumber);
                btnsave.setTextColor(Color.parseColor("#FE3BEC"));
                // timer

            }
        });



        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonArray characterNumberJsonArray =new JsonArray();
                characterNumberJsonArray.add(characterNumber);

                System.out.println(characterNumberJsonArray.toString());

                Status status =new Status(characterNumberJsonArray);
                Call<Status> callUpdateCharacter = ourAPIService.putUserStatus("bearer "+userToken, status);
                callUpdateCharacter.enqueue(new Callback<Status>() {
                    @Override
                    public void onResponse(Call<Status> call, Response<Status> response) {
                        String result=response.message();
                        System.out.println(result);
                 

//                        ProfileFragment profileFragment =new ProfileFragment() ;
//                        getSupportFragmentManager().beginTransaction()
//                                .show(profileFragment)
//                                .commit();
                        finish();
                    }

                    @Override
                    public void onFailure(Call<Status> call, Throwable t) {
                        System.out.println("伺服器連線失敗");
                        Log.d("HKT", "response: " + t.toString());
                    }
                });
            }
        });
    }
}