package com.example.a110407_app;

import android.app.Fragment;
import android.content.Intent;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a110407_app.ui.SQLiteDBHelper;
import com.example.a110407_app.ui.gallery.GalleryFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentManager;
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

public class ShowDiaryActivity extends AppCompatActivity {
    private TextView showTitleText; //顯示日記標題
    private TextView showContentText; //顯示日記內文
    private String titleText; //日記標題
    private String contentText; //日記內文
    private SQLiteDBHelper mHelper; //內部資料庫元件
    private final String DB_NAME = "MyDairy.db";
    private String TABLE_NAME = "MyDairy";
    private final int DB_VERSION = 3;
    private ArrayList<HashMap<String, String>> diaryTitleAndContent; //標題和內文的ArrayList
    private  Button btnDeleteDiary; //刪除按鈕
    private  Button btnEditDiary; //編輯按鈕

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_diary);

        //介面元件取得
        showContentText=findViewById(R.id.textShowContents);
        showTitleText=findViewById(R.id.textShowTitle);
        btnDeleteDiary =findViewById(R.id.btnDeleteDiary);
        btnEditDiary = findViewById(R.id.btnEditDiary);

        //抓使用者點選的日記id
        mHelper = new SQLiteDBHelper(this,DB_NAME,null,DB_VERSION,TABLE_NAME);
        //這是上個頁面傳過來的id
        Intent intent =getIntent();
        String id =intent.getStringExtra("id");


        //資料庫搜尋內容，用id搜尋
        diaryTitleAndContent=mHelper.searchById(id);
        for(HashMap<String,String> data:diaryTitleAndContent){
            titleText=data.get("Title");
            contentText=data.get("Content");
        }
        //將標題和內容顯示出來
        showTitleText.setText(titleText);
        showContentText.setText(contentText);

        //刪除按鈕
        btnDeleteDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =getIntent();
                String id = intent.getStringExtra("id");
                System.out.println("刪除日記"+id);

                mHelper.deleteByIdEZ(id);
                Toast.makeText(getApplicationContext(), "刪除成功", Toast.LENGTH_SHORT).show();
            }
        });

        btnEditDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取得本文的id
                Intent intent =getIntent();
                String id = intent.getStringExtra("id");

                openActivityChangeDiaryById(id);
                System.out.println(id);
            }
        });


    }

    public void openActivityChangeDiaryById(String Id){
        Intent intent = new Intent(this, ChangeDiaryActivity.class);
        intent.putExtra("id",Id);
        System.out.println("SHOW"+Id);
        startActivity(intent);
    };


}
