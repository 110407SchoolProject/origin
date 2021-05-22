package com.example.a110407_app;

import android.content.Intent;
import android.icu.text.CaseMap;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a110407_app.ui.SQLiteDBHelper;
import com.example.a110407_app.ui.gallery.GalleryFragment;
import com.example.a110407_app.ui.login.RegisterActivity;
import com.facebook.stetho.Stetho;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ChangeDiaryActivity extends AppCompatActivity {

    private  EditText changeTextTitle;
    private  EditText changeTextContent;
    private Button btnSaveDiary;
    private String titleText;
    private String contentText;

    //建立SQLite DataBase
    private final String DB_NAME = "MyDairy.db";
    private String TABLE_NAME = "MyDairy";
    private final int DB_VERSION = 13;
    SQLiteDBHelper mHelper;
    private String category = "未分類";
    private String score = "5";

    //private  String TABLE_NAME_CATEGORY = "Category";


    private ArrayList<HashMap<String, String>> diaryTitleAndContent = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_diary);

        Stetho.initializeWithDefaults(this);

        mHelper = new SQLiteDBHelper(this,DB_NAME,null,DB_VERSION,TABLE_NAME);
        mHelper.getWritableDatabase();
        changeTextTitle=(EditText)findViewById(R.id.changeTextTitle);
        changeTextContent= (EditText)findViewById(R.id.changeTextContent);
        changeTextContent.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        changeTextContent.setGravity(Gravity.TOP);
        changeTextContent.setSingleLine(false);
        btnSaveDiary = findViewById(R.id.btnSaveDiary);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        System.out.println(id);

        diaryTitleAndContent=mHelper.searchById(id);
        for(HashMap<String,String> data:diaryTitleAndContent){
            titleText=data.get("Title");
            contentText=data.get("Content");
        }
        System.out.println(diaryTitleAndContent);
        System.out.println(titleText);
        System.out.println(contentText);


        changeTextTitle.setText(titleText,TextView.BufferType.EDITABLE);
        changeTextContent.setText(contentText,TextView.BufferType.EDITABLE);

        Integer month = 0;
        Integer date= 0;
        Date mDate = new Date();
        month = mDate.getMonth()+1 ;
        date= mDate.getDate() ;
        final String stringDate = date.toString();
        final String stringMonth = month.toString();
        final String todayDate = stringMonth + stringDate;

        btnSaveDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                String id = intent.getStringExtra("id");
                String newTitle = changeTextTitle.getText().toString();
                String newContent = changeTextContent.getText().toString();


                mHelper.modifyEZ(id,newTitle,newContent,todayDate,category,score);
                Toast.makeText(getApplicationContext(), "更改成功", Toast.LENGTH_SHORT).show();
                openActivityShowDiary(id);
                finish();
            }
        });

    }

    public void openActivityShowDiary(String id){
        Intent intent = new Intent(this, ShowDiaryActivity.class);

        intent.putExtra("id",id);
        startActivity(intent);
    }
}
