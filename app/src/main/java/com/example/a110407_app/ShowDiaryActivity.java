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
    private TextView showTitleText;
    private TextView showContentText;
    private String titleText;
    private String contentText;

    private SQLiteDBHelper mHelper;
    private final String DB_NAME = "MyDairy.db";
    private String TABLE_NAME = "MyDairy";
    private final int DB_VERSION = 1;
    private ArrayList<HashMap<String, String>> diaryTitleAndContent;

    private  Button btnDeleteDiary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_diary);

        showContentText=findViewById(R.id.textShowContents);
        showTitleText=findViewById(R.id.textShowTitle);
        btnDeleteDiary =findViewById(R.id.btnDeleteDiary);
        //抓使用者點選的日記id
        mHelper = new SQLiteDBHelper(this,DB_NAME,null,DB_VERSION,TABLE_NAME);
        //這是上個頁面傳過來的
        Intent intent =getIntent();
        String title =intent.getStringExtra("Title");


        diaryTitleAndContent=mHelper.searchByTitle(title);
        for(HashMap<String,String> data:diaryTitleAndContent){
            titleText=data.get("Title");
            contentText=data.get("Content");

        }

        showTitleText.setText(titleText);
        showContentText.setText(contentText);


        btnDeleteDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =getIntent();
                String title = intent.getStringExtra("Title");
                System.out.println("刪除日記"+title);

                String thisDiaryId="";

                diaryTitleAndContent=mHelper.searchByTitle(title);
                for(HashMap<String,String> data:diaryTitleAndContent){
                    thisDiaryId=data.get("id");
                }

                mHelper.deleteByIdEZ(thisDiaryId);

            }
        });

    }

}
