package com.example.a110407_app;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a110407_app.Model.MoodPredict;
import com.example.a110407_app.Model.UserDiary;
import com.example.a110407_app.RetrofitAPI.APIService;
import com.example.a110407_app.RetrofitAPI.RetrofitManager;
import com.example.a110407_app.ui.SQLiteDBHelper;
import com.example.a110407_app.ui.gallery.GalleryFragment;
import com.example.a110407_app.ui.login.LoginActivity;
import com.facebook.stetho.Stetho;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeDiaryActivity extends AppCompatActivity {
    String userToken;
    private APIService ourAPIService;
    //日記項
    private EditText editTextTitle;
    private EditText editTextContent;
    private Button btnSaveDiary;
    private String getTitle;
    private String getContent;

    private String textTitle;
    private String textContent;
    private String tag;
    private String tag2;
    private String tag3;
    private int moodScoreInt;
    private String createDate;
    private String modifiedDate;
    //心情按鈕

    private ImageView btnCryingMood;
    private ImageView btnSadMood;
    private ImageView btnNormalMood;
    private ImageView btnSmilingMood;
    private ImageView btnExcitingMood;
    private ImageView currentMood;


    private Button moodPredictButton;
    private String category = "未分類";
    private String moodScore = "5";

    private TextView showCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_diary);

        Intent intent =getIntent();
        String diaryId =intent.getStringExtra("id");
        userToken = intent.getStringExtra("userToken");
        System.out.println("DiaryID:"+diaryId);
        System.out.println("userToken:"+userToken);

        Button tagCategory = findViewById(R.id.tagCategoryInChange);
        tagCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        ChangeDiaryActivity.this,R.style.BottomSheetTheme
                );
                View bottomSheetView = LayoutInflater.from(getApplicationContext())
                        .inflate(
                                R.layout.activity_bottom_dialog_tag,
                                (LinearLayout)findViewById(R.id.bottom_layout_tag)
                        );
                bottomSheetView.findViewById(R.id.chosen_tag_from_bottomSheet).setOnClickListener(new View.OnClickListener(){
                    public void onClick(View view){
                        Toast.makeText(ChangeDiaryActivity.this,"CHOSEN",Toast.LENGTH_SHORT).show();
                        bottomSheetDialog.dismiss();
                    }
                });
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            }
        });
        //TAG CATEGORY ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
//        editTextTitle.setText(month + "月" + date + "號的日記");
        editTextTitle = (EditText) findViewById(R.id.editTextTitleInChange);
        currentMood= (ImageView)findViewById(R.id.currentMoodImageViewInChange);
        btnCryingMood =(ImageView)findViewById(R.id.btnCryingInChange);
        btnSadMood =(ImageView)findViewById(R.id.btnSadInChange);
        btnNormalMood =(ImageView)findViewById(R.id.bntNormalInChange);
        btnSmilingMood =(ImageView)findViewById(R.id.btnSmilingInChange);
        btnExcitingMood =(ImageView)findViewById(R.id.btnExcitingInChange);
        editTextContent = findViewById(R.id.editTextContentInChange);
        editTextContent.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        editTextContent.setGravity(Gravity.TOP);
        editTextContent.setSingleLine(false);

        btnSaveDiary=findViewById(R.id.btnSaveDiaryInChange);

        btnSaveDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textTitle =editTextTitle.getText().toString();
                textContent=editTextContent.getText().toString();
                moodScoreInt= Integer.parseInt(moodScore);
                UserDiary userDiary =new UserDiary(
                        textTitle,
                        textContent,
                        "標籤1",
                        "標籤2",
                        "標籤3",
                        moodScoreInt
                );
                Call<UserDiary> callUpdateDiary = ourAPIService.putUserDiary("bearer "+userToken,userDiary,diaryId);
                callUpdateDiary.enqueue(new Callback<UserDiary>() {
                    @Override
                    public void onResponse(Call<UserDiary> call, Response<UserDiary> response) {
                        System.out.println("伺服器有回應");

                        try {
                            String result = response.message();
                            System.out.println("Server:"+result);
                            if(result.equals("OK")){
                                Toast.makeText(getApplicationContext(), "日記更新成功", Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(getApplicationContext(), "日記更新失敗", Toast.LENGTH_LONG).show();
                            }
                        }catch (Exception e){
                            Toast.makeText(getApplicationContext(), "系通發生錯誤，日記更新失敗", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserDiary> call, Throwable t) {
                        System.out.println("無法連線到伺服器");
                        Log.d("HKT", "response: " + t.toString());
                    }
                });
            }
        });


        //先把所有日記內容抓下來
        ourAPIService = RetrofitManager.getInstance().getAPI();

        Call<UserDiary> callSingleDiary = ourAPIService.getUserSingleDiary("bearer "+userToken,diaryId);

        callSingleDiary.enqueue(new Callback<UserDiary>() {
            @Override
            public void onResponse(Call<UserDiary> call, Response<UserDiary> response) {
                JsonArray diary = response.body().getDiaryList();
                System.out.println(diary);

                JsonObject singleDiaryJsonObject = (JsonObject) diary.get(0);

                System.out.println(singleDiaryJsonObject.toString());

                textTitle=singleDiaryJsonObject.get("title").toString();
                textContent=singleDiaryJsonObject.get("content").toString();
                tag=singleDiaryJsonObject.get("tag").toString();
                tag2=singleDiaryJsonObject.get("tag2").toString();
                tag3=singleDiaryJsonObject.get("tag3").toString();
                moodScore=singleDiaryJsonObject.get("moodscore").toString();
                createDate=singleDiaryJsonObject.get("create_date").toString();
                modifiedDate=singleDiaryJsonObject.get("last_modified").toString();

                textContent= textContent.substring(1,textContent.length()-1);
                textTitle = textTitle.substring(1,textTitle.length()-1);
                editTextTitle.setText(textTitle);
                editTextContent.setText(textContent);

                if(moodScore.equals("1")){
                    currentMood.setImageResource(R.drawable.crying);
                }else if(moodScore.equals("2")){
                    currentMood.setImageResource(R.drawable.sad);
                }else if(moodScore.equals("3")){
                    currentMood.setImageResource(R.drawable.normal);
                }else if(moodScore.equals("4")){
                    currentMood.setImageResource(R.drawable.smiling);
                }else if(moodScore.equals("5")){
                    currentMood.setImageResource(R.drawable.exciting);
                }

            }
            @Override
            public void onFailure(Call<UserDiary> call, Throwable t) {
                System.out.println("讀取日記失敗");
                Log.d("HKT", "response: " + t.toString());
            }
        });

        btnCryingMood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moodScore="1";

                System.out.println(moodScore);
                currentMood.setImageResource(R.drawable.crying);
            }
        });
        btnSadMood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moodScore="2";
                System.out.println(moodScore);
                currentMood.setImageResource(R.drawable.sad);
            }
        });
        btnNormalMood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moodScore="3";
                System.out.println(moodScore);
                currentMood.setImageResource(R.drawable.normal);
            }
        });
        btnSmilingMood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moodScore="4";
                System.out.println(moodScore);
                currentMood.setImageResource(R.drawable.smiling);
            }
        });
        btnExcitingMood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moodScore="5";
                System.out.println(moodScore);
                currentMood.setImageResource(R.drawable.exciting);
            }
        });

        //用BERT預測心情
        moodPredictButton = (Button) findViewById(R.id.moodPredictButtonInChange);
        moodPredictButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog progressDialog=new ProgressDialog(ChangeDiaryActivity.this);
                progressDialog.setMessage("預測中！可能會花上比較久的時間");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setIndeterminate(true);
                progressDialog.show();
                final int totalProgressTime = 100;
                final Thread t =new Thread(){
                    int jumpTime=0;
                    @Override
                    public void run() {
                        while(jumpTime< totalProgressTime){
                            try {
                                sleep(200);
                                jumpTime += 20;
                                progressDialog.setProgress(jumpTime);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                };
                t.start();

                textContent=editTextContent.getText().toString();
                //去除空白、英文、數字字元，過濾掉一些不重要的東西避免干擾預測
                textContent=textContent.replace(" ","");
                textContent=textContent.replaceAll("(?i)[a-zA-Z]", "");
                textContent=textContent.replaceAll("\n", "");
                MoodPredict moodPredict = new MoodPredict(textContent);
                System.out.println("抓日記內文："+moodPredict.getContent());

                Call<MoodPredict> callMoodPredict = ourAPIService.postMoodPredict("bearer "+userToken, moodPredict);
                callMoodPredict.enqueue(new Callback<MoodPredict>() {
                    @Override
                    public void onResponse(Call<MoodPredict> call, Response<MoodPredict> response) {
                        String result = response.message();
                        System.out.println("Server:"+result);
                        try {
                            ArrayList predictionArrayList= response.body().getScore();
                            String predictResult= predictionArrayList.get(0).toString();
                            System.out.println("結果"+predictResult);
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(ChangeDiaryActivity.this);
                            //dialog box edit here
                            if(predictResult.equals("0")){
                                moodScore="2";
                                alertDialog.setTitle("心情預測結果");
                                alertDialog.setMessage("你的心情看起來不太好呢，是不是有些事情困擾著你，試著休息一會吧？");
                                alertDialog.setIcon(R.drawable.sad);
                                alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                                alertDialog.setCancelable(true);
                                alertDialog.show();
                                System.out.println(moodScore);
                                currentMood.setImageResource(R.drawable.sad);
                            }else{
                                moodScore="4";
                                alertDialog.setTitle("心情預測結果");
                                alertDialog.setIcon(R.drawable.smiling);
                                alertDialog.setMessage("您的心情感覺還不賴，每天保持好心情的話對健康也是很有助益的喔！");
                                alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                                alertDialog.setCancelable(true);
                                alertDialog.show();
                                System.out.println(moodScore);
                                currentMood.setImageResource(R.drawable.smiling);
                            }
                            progressDialog.cancel();
                        }catch (Exception e){
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(ChangeDiaryActivity.this);
                            alertDialog.setTitle("預測失敗");
                            alertDialog.setMessage("可能有些錯誤發生，\n所以導致預測失敗😭😭，\n請稍後再試");
                            alertDialog.setIcon(R.drawable.crying);
                            alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    progressDialog.cancel();
                                }
                            });
                            alertDialog.setCancelable(true);
                            alertDialog.show();
                            System.out.println("預測失敗");
                        }
                    }

                    @Override
                    public void onFailure(Call<MoodPredict> call, Throwable t) {
                        System.out.println("無法連線到伺服器");
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ChangeDiaryActivity.this);
                        alertDialog.setTitle("連線逾時");
                        alertDialog.setMessage("可能有些錯誤發生，\n所以導致預測失敗😭😭，\n請稍後再試");
                        alertDialog.setIcon(R.drawable.crying);
                        alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                progressDialog.cancel();
                            }
                        });
                        alertDialog.setCancelable(true);
                        alertDialog.show();
                        System.out.println("預測失敗");
                        Log.d("HKT", "response: " + t.toString());
                    }
                });


            }
        });
    }






    //當使用者儲存完畢，可以馬上顯示出這筆日記
    public void openActivityShowDiary(String id ){
        Intent intent = new Intent(this, ShowDiaryActivity.class);
        String diaryId = id ;
        intent.putExtra("id",diaryId);
        startActivity(intent);
    }
}
