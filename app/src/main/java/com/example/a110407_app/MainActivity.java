package com.example.a110407_app;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a110407_app.Model.ProfileScore;
import com.example.a110407_app.Model.User;
import com.example.a110407_app.RetrofitAPI.APIService;
import com.example.a110407_app.RetrofitAPI.RetrofitManager;
import com.example.a110407_app.ui.SQLiteDBHelper;
import com.example.a110407_app.ui.gallery.GalleryFragment;
import com.example.a110407_app.ui.home.HomeFragment;
import com.example.a110407_app.ui.slideshow.SlideshowFragment;
import com.facebook.stetho.Stetho;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.JsonObject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private APIService ourAPIService;
    private AppBarConfiguration mAppBarConfiguration;
    private FloatingActionButton fab;
    private ImageButton imageButton;
    private String userToken;
    private TextView navbarNickNameTextView;
    private TextView navbarEmailTextView;
    private String userEmail;
    private String userNickName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        userToken = intent.getStringExtra("userToken");
        System.out.println("從Main抓到userToken： "+userToken);

        ourAPIService = RetrofitManager.getInstance().getAPI();
        //setContentView(R.layout.content_main);


        //啟動最上方紫色的Toolbar()
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //新增dairy的按鈕
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "新增日記", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                //新增日記
                openActivityEditDiary(userToken);
            }
        });

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        View header =navigationView.getHeaderView(0);

        navbarEmailTextView =header.findViewById(R.id.navbarEmailTextView);
        navbarNickNameTextView =header.findViewById(R.id.navbarNickNameTextView);
        Call<User> callUserData = ourAPIService.getUserData("bearer "+userToken);
        callUserData.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                String result=response.message();
                System.out.println(result);
                JsonObject userData =response.body().getUserAllDataInJson();
                System.out.println(userData.toString());
                userEmail=userData.get("username").toString();
                userNickName=userData.get("nickname").toString();
                userEmail=userEmail.substring(1,userEmail.length()-1);
                userNickName=userNickName.substring(1,userNickName.length()-1);
                navbarEmailTextView.setText(userEmail);
                navbarNickNameTextView.setText(userNickName);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                System.out.println("伺服器連線失敗");
                Log.d("HKT", "response: " + t.toString());
            }
        });
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_home,R.id.nav_profile,R.id.nav_gallery, R.id.nav_slideshow,R.id.nav_tree).setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        Call<ProfileScore> callProfileScore = ourAPIService.putProfileScore("bearer "+userToken);
        callProfileScore.enqueue(new Callback<ProfileScore>() {
            @Override
            public void onResponse(Call<ProfileScore> call, Response<ProfileScore> response) {
                String result = response.message();
                System.out.println(result);
                float score = response.body().getScore();
                System.out.println("分數為: " + String.valueOf(score));

                Call<User> callUserData = ourAPIService.getUserData("bearer "+userToken);
                callUserData.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        String result=response.message();
                        System.out.println(result);
                        JsonObject userData =response.body().getUserAllDataInJson();
                        System.out.println(userData.toString());
                        userNickName=userData.get("nickname").toString();
                        userNickName=userNickName.substring(1,userNickName.length()-1);

                        if(score <= 1.0) {
                            String welcomeSentence ="歡迎回來 "+userNickName +"\n最近的心情感覺不太好，試著找些事情放鬆一下吧! 祝您的心情可以快點恢復🥺";
                            Toast.makeText(getApplicationContext(), welcomeSentence, Toast.LENGTH_SHORT).show();
                        }else if ( score <=2.0 && score > 1.0){
                            String welcomeSentence ="歡迎回來 "+userNickName +"\n最近發生些什麼了嗎?，好像不太開心，記得別讓自己壓力太大了唷!🥺";
                            Toast.makeText(getApplicationContext(), welcomeSentence, Toast.LENGTH_SHORT).show();

                        }else if (score <=3.0 && score > 2.0){
                            String welcomeSentence ="歡迎回來 "+userNickName +"\n保持快樂是讓身體健康很重要的元素，試著記錄下您的心情來豐富您的日記吧!😁";
                            Toast.makeText(getApplicationContext(), welcomeSentence, Toast.LENGTH_SHORT).show();

                        }else if (score <=4.0 && score > 3.0){

                            String welcomeSentence ="歡迎回來 "+userNickName +"\n最近感覺過的挺好的唷，繼續保持您快樂的心境，寫下些生活來與我分享吧!😝 ";

                            Toast.makeText(getApplicationContext(), welcomeSentence, Toast.LENGTH_SHORT).show();

                        }else if(score >= 5.0 && score > 4.0 ){
                            String welcomeSentence ="歡迎回來 "+userNickName +"\n最近的心情感覺還不賴喔，快來記錄些心情與我分享吧😍";

                            Toast.makeText(getApplicationContext(), welcomeSentence, Toast.LENGTH_SHORT).show();

                        }else {
                            System.out.println("分數計算有誤");
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        System.out.println("伺服器連線失敗");
                        Log.d("HKT", "response: " + t.toString());
                    }
                });



            }

            @Override
            public void onFailure(Call<ProfileScore> call, Throwable t) {
                System.out.println("伺服器連線失敗");
                Log.d("HKT", "response: " + t.toString());
            }
        });
    }
    // 打開右上角的menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //判斷menu點下去，對應的動作
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id == R.id.action_settings){
            Toast.makeText(this, "設定", Toast.LENGTH_SHORT).show();
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    //開啟寫日記
    public void openActivityEditDiary(String userToken){
        Intent intent = new Intent(this, EditDiaryActivity.class);
        intent.putExtra("userToken", userToken);
        startActivity(intent);
    }



}
