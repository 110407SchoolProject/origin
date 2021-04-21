package com.example.a110407_app;

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

public class EditDiaryActivity extends AppCompatActivity {
    public static final String EXTRA_TEXT="com.example.application.example.EXTRA_TEXT";
    public static final String EXTRA_TEXT2="com.example.application.example.EXTRA_TEXT2";

    private  EditText editTextTitle;
    private  EditText editTextContent;
    private Button btnSaveDiary;
    public String getTitle;
    public String getContent;

    //建立SQLite DataBase
    private final String DB_NAME = "MyDairy.db";
    private String TABLE_NAME = "MyDairy";
    private final int DB_VERSION = 1;
    SQLiteDBHelper mHelper;

    FragmentManager fragmentManager;
    GalleryFragment galleryFragment;




    @RequiresApi(api = Build.VERSION_CODES.N)

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_diary);
        Calendar mCalendar = Calendar.getInstance();
        //連結Facebook 開發的stetho資料庫工具
        Stetho.initializeWithDefaults(this);
        //初始化資料庫
        mHelper = new SQLiteDBHelper(this,DB_NAME,null,DB_VERSION,TABLE_NAME);


        //抓取今天的日期設定到標題
        Integer month = 0;
        Integer date= 0;
        Date mDate = new Date();
        month = mDate.getMonth()+1 ;
        date= mDate.getDate() ;
        editTextTitle =(EditText)findViewById(R.id.editTextTitle);
        editTextTitle.setHint(month+"月"+date+"號的日記");
        //抓取輸入的內文，下面要在寫入data base
        editTextContent= findViewById(R.id.editTextContent);
        editTextContent.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        editTextContent.setGravity(Gravity.TOP);
        editTextContent.setSingleLine(false);
        btnSaveDiary = findViewById(R.id.btnSaveDiary);

        //儲存日記
        btnSaveDiary = (Button) findViewById(R.id.btnSaveDiary);



        btnSaveDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTitle = editTextTitle.getText().toString();
                getContent = editTextContent.getText().toString();
                mHelper.addData(getTitle,getContent);


                Toast.makeText(getApplicationContext(), "儲存成功", Toast.LENGTH_SHORT).show();
//                fragmentManager=getSupportFragmentManager();
//                fragmentManager.beginTransaction().
//                        add(R.id.LayoutForFragment,galleryFragment).
//                        show(galleryFragment).
//                        commit();
            }
        });
    }
}
