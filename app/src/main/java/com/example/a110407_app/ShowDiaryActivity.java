package com.example.a110407_app;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a110407_app.Model.UserDiary;
import com.example.a110407_app.RetrofitAPI.APIService;
import com.example.a110407_app.RetrofitAPI.RetrofitManager;
import com.example.a110407_app.ui.SQLiteDBHelper;
import com.example.a110407_app.ui.gallery.GalleryFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.GenericArrayType;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.example.a110407_app.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowDiaryActivity extends AppCompatActivity {

    private String userToken;
    APIService ourAPIService;
    private String diaryId;
    private TextView showTitleText; //顯示日記標題
    private TextView showContentText; //顯示日記內文
    private TextView showCategory;//顯示日記分類
    private ImageView showImageMood;//顯示日記心情
    private String textTitle;
    private String textContent;
    private String tag;
    private String tag2;
    private String tag3;
    private String createDate;
    private String modifiedDate;
    private String moodScore;
    private  Button btnDeleteDiary; //刪除按鈕
    private  Button btnEditDiary; //編輯按鈕


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_diary);

        //介面元件取得
        showContentText=findViewById(R.id.textShowContents);
        showTitleText=findViewById(R.id.textShowTitle);
        showCategory = findViewById(R.id.textShowCategory);
        showImageMood = findViewById(R.id.moodImage);
        btnDeleteDiary =findViewById(R.id.btnDeleteDiary);
        btnEditDiary = findViewById(R.id.btnEditDiary);

        Intent intent =getIntent();
        diaryId =intent.getStringExtra("id");
        userToken = intent.getStringExtra("userToken");
        System.out.println(diaryId);
        System.out.println(userToken);

        ourAPIService = RetrofitManager.getInstance().getAPI();

        Call<UserDiary> callSingleDiary = ourAPIService.getUserSingleDiary("bearer "+userToken,diaryId);
        callSingleDiary.enqueue(new Callback<UserDiary>() {
            @Override
            public void onResponse(Call<UserDiary> call, Response<UserDiary> response) {
                JsonArray diary = response.body().getDiaryList();
                JsonObject singleDiaryJsonObject = (JsonObject) diary.get(0);

                textTitle=singleDiaryJsonObject.get("title").toString();
                textContent=singleDiaryJsonObject.get("content").toString();
                tag=singleDiaryJsonObject.get("tag").toString();
                tag2=singleDiaryJsonObject.get("tag2").toString();
                tag3=singleDiaryJsonObject.get("tag3").toString();
                moodScore=singleDiaryJsonObject.get("moodscore").toString();
                createDate=singleDiaryJsonObject.get("create_date").toString();
                modifiedDate=singleDiaryJsonObject.get("last_modified").toString();

                textTitle=textTitle.substring(1,textTitle.length()-1);
                textContent=textContent.substring(1,textContent.length()-1);

                System.out.println(textTitle);
                System.out.println(textContent);
                System.out.println(tag);
                System.out.println(tag2);
                System.out.println(tag3);
                System.out.println(moodScore);
                System.out.println(createDate);
                System.out.println(modifiedDate);

                showTitleText.setText(textTitle);
                showContentText.setText(textContent);

                int score = (int)Float.parseFloat(moodScore);
                System.out.println(score);
                switch(score) {
                    case 1:
                        System.out.println("Crying");
                        showImageMood.setImageResource(R.drawable.crying);
                        break;
                    case 2:
                        System.out.println("Sad");
                        showImageMood.setImageResource(R.drawable.sad);
                        break;
                    case 3:
                        System.out.println("Normal");
                        showImageMood.setImageResource(R.drawable.normal);
                        break;
                    case 4:
                        System.out.println("Smiling");
                        showImageMood.setImageResource(R.drawable.smiling);
                        break;
                    case 5:
                        System.out.println("Exciting");
                        showImageMood.setImageResource(R.drawable.exciting);
                        break;
                }
            }

            @Override
            public void onFailure(Call<UserDiary> call, Throwable t) {
                System.out.println("顯示日記失敗");
                Log.d("HKT", "response: " + t.toString());
            }
        });
        //刪除按鈕
        btnDeleteDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =getIntent();
                String id = intent.getStringExtra("id");
                System.out.println("刪除日記"+id);

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ShowDiaryActivity.this);
                alertDialog.setTitle("日記刪除");
                alertDialog.setIcon(R.drawable.sad);
                alertDialog.setMessage("確定要將日記刪除嗎？");
                alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Call<UserDiary> callDeleteSingleDiary = ourAPIService.deleteUserSingleDiary("bearer "+userToken,id);
                        callDeleteSingleDiary.enqueue(new Callback<UserDiary>() {
                            @Override
                            public void onResponse(Call<UserDiary> call, Response<UserDiary> response) {
                                try {
                                    String result = response.body().getResult();
                                    System.out.println(result);
                                    if(result.equals("ok")){
                                        Toast.makeText(getApplicationContext(), "刪除成功", Toast.LENGTH_SHORT).show();
                                        finish();
                                        openActivityHome(userToken);
                                    }else{
                                        Toast.makeText(getApplicationContext(), "刪除失敗，系統可能發生問題", Toast.LENGTH_SHORT).show();
                                    }
                                }catch (Exception e){
                                    System.out.println("刪除失敗");
                                    Toast.makeText(getApplicationContext(), "刪除失敗，系統可能發生問題", Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onFailure(Call<UserDiary> call, Throwable t) {
                                System.out.println("日記刪除失敗");
                                Log.d("HKT", "response: " + t.toString());
                            }
                        });
                    }
                });
                alertDialog.setCancelable(true);
                alertDialog.show();
            }
        });

        btnEditDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openActivityChangeDiaryById(diaryId,userToken);
                System.out.println("要傳到編輯日記的日記ID:"+diaryId);
                System.out.println("要傳到編輯日記的日記userToken:"+userToken);
            }
        });
    }

    public void openActivityChangeDiaryById(String Id,String userToken){
        Intent intent = new Intent(this, ChangeDiaryActivity.class);
        intent.putExtra("id",Id);
        intent.putExtra("userToken",userToken);
        System.out.println("SHOW"+Id);
        startActivity(intent);
        finish();
    };

    public void openActivityHome(String userToken){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("userToken",userToken);
        startActivity(intent);

    }

}
