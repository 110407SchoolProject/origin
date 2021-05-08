package com.example.a110407_app;

import android.content.DialogInterface;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a110407_app.ui.SQLiteDBHelper;
import com.facebook.stetho.Stetho;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class EditDiaryActivity extends AppCompatActivity {
    public static final String EXTRA_TEXT = "com.example.application.example.EXTRA_TEXT";
    public static final String EXTRA_TEXT2 = "com.example.application.example.EXTRA_TEXT2";
    //日記項
    private EditText editTextTitle;
    private EditText editTextContent;
    private Button btnSaveDiary;
    private String getTitle;
    private String getContent;
    //心情按鈕
    private int img_id[]={R.drawable.crying,R.drawable.sad,R.drawable.normal,R.drawable.smiling,R.drawable.exciting};
    private ImageButton btnCryingMood;
    private ImageButton btnSadMood;
    private ImageButton btnNormalMood;
    private ImageButton btnSmilingMood;
    private ImageButton btnExcitingMood;
    private ImageView currentMood;

    //建立日記表的資料庫
    private SQLiteDBHelper mHelper;
    private final String DB_NAME = "MyDairy.db";
    private String TABLE_NAME = "MyDairy";
    private final int DB_VERSION = 10;

    //分類
    private Button chooseCategory;
    private String category = "未分類";
    private String moodScore = "5";

    //pony
    private ArrayList<HashMap<String, String>> categoryList ;
    private ArrayList<HashMap<String, String>> categoryAllList;
    private TextView showCategory;
    private TextView getSpinnerText;

    //建立分類的資料表
    private  SQLiteDBHelper CategoryDBHelper;
    public final String TABLE_CATEGORY = "CategoryTable";

    @RequiresApi(api = Build.VERSION_CODES.N)

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_diary);
        Calendar mCalendar = Calendar.getInstance();
        //連結Facebook 開發的stetho資料庫工具
        Stetho.initializeWithDefaults(this);
        //初始化日記表資料庫
        mHelper = new SQLiteDBHelper(this, DB_NAME, null, DB_VERSION, TABLE_NAME);
        mHelper.getWritableDatabase();
        //初始化分類表的資料庫
        CategoryDBHelper = new SQLiteDBHelper(this,DB_NAME,null,DB_VERSION,TABLE_CATEGORY);
        CategoryDBHelper.getWritableDatabase();

        //抓取今天的日期設定到標題
        Integer month = 0;
        Integer date = 0;
        Date mDate = new Date();
        month = mDate.getMonth() + 1;
        date = mDate.getDate();
        final String stringDate = date.toString();
        final String stringMonth = month.toString();
        final String todayDate = stringMonth + stringDate;
        editTextTitle = (EditText) findViewById(R.id.editTextTitle);
        editTextTitle.setText(month + "月" + date + "號的日記");


        //抓取輸入的內文，下面要在寫入data base
        editTextContent = findViewById(R.id.editTextContent);
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
                mHelper.addData(getTitle, getContent, todayDate, category, moodScore);

                Toast.makeText(getApplicationContext(), "儲存成功", Toast.LENGTH_SHORT).show();
            }
        });

        //心情選取欄位
        currentMood= (ImageView)findViewById(R.id.currentMoodImageView);
        btnCryingMood =(ImageButton)findViewById(R.id.btnCrying);
        btnSadMood =(ImageButton)findViewById(R.id.btnSad);
        btnNormalMood =(ImageButton)findViewById(R.id.bntNormal);
        btnSmilingMood =(ImageButton)findViewById(R.id.btnSmiling);
        btnExcitingMood =(ImageButton)findViewById(R.id.btnExciting);

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




        //選擇日記分類
        chooseCategory = (Button) findViewById(R.id.chooseCategory);
        chooseCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //跳出AlertDialog，用Spinner選擇分類，如果沒有想要的分類，就點選button新增分類
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(EditDiaryActivity.this);
                View view = getLayoutInflater().inflate(R.layout.activity_choose_category, null);
                alertDialog.setView(view);
                Button buttonCreateCategory = (Button) view.findViewById(R.id.buttonCreateCategory);
                buttonCreateCategory.setText("新增一個新的分類");
                alertDialog.setTitle("請選擇一個分類");
                Spinner spinnerCategory = (Spinner) view.findViewById(R.id.spinnerCategory);


                ArrayList categoryAllListToShow =new ArrayList();
                categoryAllListToShow.add("未分類");
                if(categoryAllListToShow.size()==0){

                }

                categoryAllList= CategoryDBHelper.selectAllCategory();
                System.out.println(categoryAllList);
                String categoryName ="";
                for(HashMap<String,String> data:categoryAllList){
                    categoryName=data.get("Category");
                    categoryAllListToShow.add(categoryName);
                }

                ArrayAdapter<String> adaptertext = new ArrayAdapter<String>(EditDiaryActivity.this, android.R.layout.simple_spinner_item, categoryAllListToShow);

                adaptertext.setDropDownViewResource(android.R.layout.simple_spinner_item);
                spinnerCategory.setAdapter(adaptertext);
                spinnerCategory.setOnItemSelectedListener(spinnerListener);



                showCategory = (TextView) findViewById(R.id.CategoryTextView);
                //當點選"確認選擇此分類"，將此分類帶入文章的TextView中
                alertDialog.setPositiveButton("確定分類", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showCategory.getText();
                    }
                });
                alertDialog.show();

                //點選Button新增分類，跳出AlertDialog，用EditText填入分類
                buttonCreateCategory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final AlertDialog.Builder NewCategory = new AlertDialog.Builder(EditDiaryActivity.this);
                        final View view1 = getLayoutInflater().inflate(R.layout.newcategory, null);
                        NewCategory.setView(view1);
                        //NewCategory.show();
                        final EditText EditTextNewCategory = (EditText) view1.findViewById(R.id.EditTextNewCategory);

                        //Dialog Button，點下去將EditText新增的分類加進去Category1 String Array
                        NewCategory.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String categoryText = EditTextNewCategory.getText().toString();
                                System.out.println(categoryText);

                                categoryList = CategoryDBHelper.searchByCategory(categoryText);
                                if(categoryList.size()==0){
                                    System.out.println(categoryList);
                                    CategoryDBHelper.addCategory(categoryText);
                                }else{
                                    final AlertDialog.Builder categoryExist = new AlertDialog.Builder(EditDiaryActivity.this);
                                    final View view2 = getLayoutInflater().inflate(R.layout.categoryexist, null);
                                    categoryExist.setView(view2);
                                    categoryExist.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            categoryExist.setView(view1);
                                        }
                                    });
                                    categoryExist.show();
                                    System.out.println("該目錄存在");
                                }

                                //************************
                                //finish();
                                //startActivity(getIntent());
                                //*************************
                            }

                        });
                        NewCategory.show();

                    }

                });

            }

        });





    }


    //判斷Spinner選到哪一個選項
    private Spinner.OnItemSelectedListener spinnerListener = new Spinner.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String sel = parent.getSelectedItem().toString();

            showCategory.setText(sel);
        }


        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

}
