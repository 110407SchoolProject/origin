package com.example.a110407_app;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.JsonObject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditDiaryActivity extends AppCompatActivity {
    public static final String EXTRA_TEXT = "com.example.application.example.EXTRA_TEXT";
    public static final String EXTRA_TEXT2 = "com.example.application.example.EXTRA_TEXT2";
    private GalleryFragment GalleryFragment;

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
    private String tag1;
    private String tag2;
    private int moodScoreInt;
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
    private ArrayList<HashMap<String, String>> categoryList ;
    private ArrayList<HashMap<String, String>> categoryAllList;
    private TextView showCategory;

    //建立分類的資料表
    private  SQLiteDBHelper CategoryDBHelper;
    public final String TABLE_CATEGORY = "CategoryTable";

    private Notification notification;
    private NotificationManager manager;

    @RequiresApi(api = Build.VERSION_CODES.N)

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_diary);

        Intent intent = getIntent();
        userToken = intent.getStringExtra("userToken");
        System.out.println("從EditDiary抓到Token： "+userToken);

        //tag category bottom dialogVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV
        Button tagCategory = findViewById(R.id.tagCategory);
        tagCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        EditDiaryActivity.this,R.style.BottomSheetTheme
                );
                View bottomSheetView = LayoutInflater.from(getApplicationContext())
                        .inflate(
                                R.layout.bottom_dialog_tag,
                                (LinearLayout)findViewById(R.id.bottom_layout_tag)
                        );
                bottomSheetView.findViewById(R.id.chosen_tag_from_bottomSheet).setOnClickListener(new View.OnClickListener(){
                    public void onClick(View view){
                        Toast.makeText(EditDiaryActivity.this,"CHOSEN",Toast.LENGTH_SHORT).show();
                        bottomSheetDialog.dismiss();
                    }
                });
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            }
        });
        //TAG CATEGORY ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

        //抓取今天的日期設定到標題
        Integer year = 0;
        Integer month = 0;
        Integer date = 0;
        Date mDate = new Date();
        year = mDate.getYear() + 2000 - 100;
        month = mDate.getMonth() + 1;
        date = mDate.getDate();
        final String stringDate = date.toString();
        final String stringyear = year.toString();
        final String stringMonth = month.toString();
        final String todayDate = stringyear + "/" + stringMonth + "/" + stringDate;
        editTextTitle = (EditText) findViewById(R.id.editTextTitle);
        editTextTitle.setText(month + "月" + date + "號的日記");

        //抓取輸入的內文，下面要在寫入data base
        editTextContent = findViewById(R.id.editTextContent);
        editTextContent.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        editTextContent.setGravity(Gravity.TOP);
        editTextContent.setSingleLine(false);

        ourAPIService = RetrofitManager.getInstance().getAPI();

        btnSaveDiary = findViewById(R.id.btnSaveDiary);
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

                //發Token 要加上 "bearer "
                Call<UserDiary> callAddNewDiary = ourAPIService.postUserDiary("bearer "+userToken,userDiary);
                callAddNewDiary.enqueue(new Callback<UserDiary>() {
                    @Override
                    public void onResponse(Call<UserDiary> call, Response<UserDiary> response) {
                        System.out.println("伺服器有回應");
                        String result = response.message();
                        System.out.println("Server:"+result);
                        Toast.makeText(getApplicationContext(), "日記新增成功", Toast.LENGTH_LONG).show();
//                        if(result=="OK"){
//                            Toast.makeText(getApplicationContext(), "日記新增成功", Toast.LENGTH_LONG).show();
//                        }else{
//                            Toast.makeText(getApplicationContext(), "伺服器錯誤", Toast.LENGTH_LONG).show();
//                        }
                    }
                    @Override
                    public void onFailure(Call<UserDiary> call, Throwable t) {
                        System.out.println("無法連線到伺服器");
                        Log.d("HKT", "response: " + t.toString());
                    }
                });

            }
        });
        //心情選取欄位
        currentMood= (ImageView)findViewById(R.id.currentMoodImageView);
        btnCryingMood =(ImageView)findViewById(R.id.btnCrying);
        btnSadMood =(ImageView)findViewById(R.id.btnSad);
        btnNormalMood =(ImageView)findViewById(R.id.bntNormal);
        btnSmilingMood =(ImageView)findViewById(R.id.btnSmiling);
        btnExcitingMood =(ImageView)findViewById(R.id.btnExciting);

        showCategory = (TextView) findViewById(R.id.CategoryTextView);
        showCategory.setText("未分類");

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
        moodPredictButton = (Button) findViewById(R.id.moodPredictButton);




        moodPredictButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ourAPIService = RetrofitManager.getInstance().getAPI();
                textContent=editTextContent.getText().toString();
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
                            System.out.println(predictionArrayList);
                            String prdictResult= predictionArrayList.get(0).toString();
                            System.out.println("結果"+prdictResult);
                        }catch (Exception e){
                            System.out.println("預測失敗");
                        }

                    }

                    @Override
                    public void onFailure(Call<MoodPredict> call, Throwable t) {
                        System.out.println("無法連線到伺服器");
                        Log.d("HKT", "response: " + t.toString());
                    }
                });
            }

        });
    }
}
