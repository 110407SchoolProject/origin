package com.example.a110407_app;

import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a110407_app.ui.SQLiteDBHelper;
import com.facebook.stetho.Stetho;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

public class EditDiaryActivity extends AppCompatActivity {
    public static final String EXTRA_TEXT = "com.example.application.example.EXTRA_TEXT";
    public static final String EXTRA_TEXT2 = "com.example.application.example.EXTRA_TEXT2";

    private EditText editTextTitle;
    private EditText editTextContent;
    private Button btnSaveDiary;
    private String getTitle;
    private String getContent;

    //建立SQLite DataBase
    SQLiteDBHelper mHelper;
    private final String DB_NAME = "MyDairy.db";
    private String TABLE_NAME = "Category";
    private final int DB_VERSION = 10;


    private Button chooseCategory;
    private String category = "未分類";
    private String score = "5";


    String[] Category1 = new String[]{"未分類", "生活", "學校", "朋友"};
    //String[] Category2 = new String[Category1.length + 1];

    private TextView showCategory;
    private TextView getSpinnerText;

    //建立分類的資料表
    SQLiteDBHelper CategoryDBHelper;





    @RequiresApi(api = Build.VERSION_CODES.N)

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_diary);
        Calendar mCalendar = Calendar.getInstance();
        //連結Facebook 開發的stetho資料庫工具
        Stetho.initializeWithDefaults(this);
        //初始化資料庫
        mHelper = new SQLiteDBHelper(this, DB_NAME, null, DB_VERSION, TABLE_NAME);
        mHelper.getWritableDatabase();
        //初始化分類表的資料庫
        //CategoryDBHelper = new SQLiteDBHelper(this,DB_NAME,null,DB_VERSION,TABLE_NAME_CATEGORY);
        //CategoryDBHelper.getWritableDatabase();


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
                mHelper.addData(getTitle, getContent, todayDate, category, score);


                Toast.makeText(getApplicationContext(), "儲存成功", Toast.LENGTH_SHORT).show();
//                fragmentManager=getSupportFragmentManager();
//                fragmentManager.beginTransaction().
//                        add(R.id.LayoutForFragment,galleryFragment).
//                        show(galleryFragment).
//                        commit();
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
                buttonCreateCategory.setText("新增分類");
                alertDialog.setTitle("Choose a Category");
                Spinner spinnerCategory = (Spinner) view.findViewById(R.id.spinnerCategory);
                ArrayAdapter<String> adaptertext = new ArrayAdapter<String>(EditDiaryActivity.this, android.R.layout.simple_spinner_item, Category1);
                adaptertext.setDropDownViewResource(android.R.layout.simple_spinner_item);
                spinnerCategory.setAdapter(adaptertext);
                spinnerCategory.setOnItemSelectedListener(spinnerListener);

                showCategory = (TextView) findViewById(R.id.textViewCategory);
                //當點選"確認選擇此分類"，將此分類帶入文章的TextView中
                alertDialog.setPositiveButton("確定選擇此分類", new DialogInterface.OnClickListener() {
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
                        View view1 = getLayoutInflater().inflate(R.layout.newcategory, null);
                        NewCategory.setView(view1);
                        //NewCategory.show();
                        final EditText EditTextNewCategory = (EditText) view1.findViewById(R.id.EditTextNewCategory);

                        //Dialog Button，點下去將EditText新增的分類加進去Category1 String Array
                        NewCategory.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String[] Category2 = new String[Category1.length + 1];//每增加一筆，原始的Category1就增加一個長度
                                final String getNewCategory = EditTextNewCategory.getText().toString();
                                System.arraycopy(Category1, 0, Category2, 0, Category1.length);
                                Category2[Category1.length] = getNewCategory;
                                Category1 = Category2;

                                //************************
                                //finish();
                                //startActivity(getIntent());
                                //*************************
                                //測試EditText是否有成功新增進去Category1 String Array
                                /*
                                for(String i : Category1){
                                    System.out.println(i);
                                }
                                 */
                                //dialog.dismiss();
                            }
                        });
                        NewCategory.show();
                    }
                });
                //FragmentManager fragmentManager = getSupportFragmentManager();
                //EditDairyFragment fragment= new EditDairyFragment();
                //fragmentManager.beginTransaction().replace(R.id.activityEditDairy,fragment).commit();
                //FragmentManager fm = getSupportFragmentManager();
                //EditDairyFragment fragment = new EditDairyFragment();
                //fm.beginTransaction().add(R.id.activityEditDairy,fragment).commit();
            }
        });


    }

    /*
    @Override
    protected void onRestart(){
        this.recreate();
        super.onRestart();
    }
     */

    //判斷Spinner選到哪一個選項
    private Spinner.OnItemSelectedListener spinnerListener = new Spinner.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String sel = parent.getSelectedItem().toString();
            int p = position;
            showCategory.setText(Category1[p]);
        }


        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

}
