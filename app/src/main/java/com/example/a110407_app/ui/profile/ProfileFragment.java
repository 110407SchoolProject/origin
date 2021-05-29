package com.example.a110407_app.ui.profile;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.a110407_app.R;
import com.example.a110407_app.ui.PasswordSetting;
import com.example.a110407_app.ui.SQLiteDBHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.lang.Math;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    private ImageView image;
    private TextView name, username, email,birthday;

    private TextView diaryNumbers;
    private TextView diaryPoints;
    private ImageView recentMood;

    //建立日記資料表
    SQLiteDBHelper mHelper;
    private final String DB_NAME = "MyDairy.db";
    private String TABLE_NAME = "MyDairy";
    private final int DB_VERSION = 13;
    private ArrayList<HashMap<String, String>> diaryList;

    private Button editprofile;
    //private Switch setpassword;
    private Switch remind;
    private Button btnPasswordsetting;

    // 原密碼比對建立的變數
    private TextView remindtext;
    private EditText getpassword, getconfirmpassword;
    private String strpassword, strconfirmpassword;
    private  String stringDate;
    private TimePicker setremindtime;
    private String s;
    // 原密碼比對建立的變數


    //建立存放密碼資料表
    SQLiteDBHelper TableUserPassword;
    private String PASSWORD_TABLE_NAME = "UserPassword";
    private String strlock;//抓是否上鎖
    private EditText getOpenClosePassword; //抓輸入密碼的EditText上的文字
    private String strOpenPassword; //接EditText的文字轉成string的String
    private String p;// 抓密碼表密碼

    public ProfileFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);




        // 必須先呼叫getView()取得程式畫面物件，然後才能呼叫它的
        // findViewById()取得介面物件
        image = (ImageView) getView().findViewById(R.id.profileImage);
        image.setImageResource(R.drawable.ic_menu_camera);
        name = (TextView) getView().findViewById(R.id.profileName);
        name.setText("Pony Weng");
        username = (TextView) getView().findViewById(R.id.profileUsername);
        username.setText("pony1306");
        email = (TextView) getView().findViewById(R.id.profileEmail);
        email.setText("10746026@ntub.edu.tw");
        birthday = (TextView) getView().findViewById(R.id.profileBirhtday);
        birthday.setText("1999/12/07");

        diaryNumbers=getView().findViewById(R.id.currentDiaryNumbers);
        diaryPoints=getView().findViewById(R.id.currentDiaryPoints);
        recentMood=getView().findViewById(R.id.recentMoodImageView);

        mHelper = new SQLiteDBHelper(getActivity(),DB_NAME,null,DB_VERSION,TABLE_NAME);




        final ArrayList allMoodScoreList = new ArrayList();

        Integer countDiaryNumber=0;
        for(HashMap<String,String> data:mHelper.showAll()){
            countDiaryNumber+=1;
            String score =data.get("Score");
            allMoodScoreList.add(score);
        }

        diaryNumbers.setText(Integer.toString(countDiaryNumber));
        float totalMoodScore=0;
        float averageMoodScore=0;

        for(int i = 0 ; i<= allMoodScoreList.size()-1;i++){
            float MoodScore =Float.parseFloat((String) allMoodScoreList.get(i));
            totalMoodScore+=MoodScore;
        }

        averageMoodScore=totalMoodScore/allMoodScoreList.size();
        int roundAverageMoodScore = (int)Math.round(averageMoodScore);

        System.out.println(averageMoodScore);

        switch(roundAverageMoodScore) {
            case 1:
                System.out.println("Crying");
                recentMood.setImageResource(R.drawable.crying);
                break;
            case 2:
                System.out.println("Sad");
                recentMood.setImageResource(R.drawable.sad);
                break;
            case 3:
                System.out.println("Normal");
                recentMood.setImageResource(R.drawable.normal);
                break;
            case 4:
                System.out.println("Smiling");
                recentMood.setImageResource(R.drawable.smiling);
                break;
            case 5:
                System.out.println("Exciting");
                recentMood.setImageResource(R.drawable.exciting);
                break;
        }

        int diaryPoint = countDiaryNumber*5;
        diaryPoints.setText(Integer.toString(diaryPoint));


        editprofile = getView().findViewById(R.id.editprofile);
        editprofile.setText("編輯個人設置");
        btnPasswordsetting = getView().findViewById(R.id.btnPasswordsetting);
        remind = getView().findViewById(R.id.remind);
        remindtext = getView().findViewById(R.id.remindtext);
        remindtext.setText("提醒");

        //以下為 Gary 5/28 寫的
        //初始化 TableUser資料庫
        TableUserPassword = new SQLiteDBHelper(getActivity(),DB_NAME,null,DB_VERSION,PASSWORD_TABLE_NAME);
        TableUserPassword.getWritableDatabase();

        //導向密碼設置Activity
        btnPasswordsetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //初始化AlertDialog.Builder(記得要初始化在Button內，避免第二次點選時跳錯誤)
                AlertDialog.Builder PasswordDialog = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
                View view = getLayoutInflater().inflate(R.layout.openclosepassword, null);
                PasswordDialog.setView(view);
                PasswordDialog.setCancelable(false);//禁止按返回鍵(禁止取消AlertDialog)
                //撈出目前是否上鎖
                for(HashMap<String,String> data:TableUserPassword.showLock()){
                    strlock = data.get("IfSetLock");
                }
                if(strlock.equals("1")){//目前為上鎖狀態
                    PasswordDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //返回密碼設置Actiivty
                        }
                    });

                    PasswordDialog.setPositiveButton("確認", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getOpenClosePassword = view.findViewById(R.id.inputOpcnClosePassword);
                            strOpenPassword = getOpenClosePassword.getText().toString();
                            for(HashMap<String,String> data:TableUserPassword.showAllPassword()){
                                p = data.get("Password");
                            }
                            if(strOpenPassword.equals(p)){//密碼吻合，開啟密碼設置Activity
                                Intent intent = new Intent(getActivity(), PasswordSetting.class);
                                startActivity(intent);

                            }else{//錯誤跳重新輸入Toast
                                Toast.makeText(getActivity(),"密碼錯誤，請重新輸入!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    PasswordDialog.show();

                }else{//目前為無上鎖狀態
                    Intent intent = new Intent(getActivity(), PasswordSetting.class);
                    startActivity(intent);
                }
            }

        });
        //以上為 Gary 5/28 寫的

        remind.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()){
                    /*
                    final AlertDialog.Builder setremind = new AlertDialog.Builder(getActivity());
                    final View view = getLayoutInflater().inflate(R.layout.setremind, null);
                    setremind.setView(view);
                    setremind.setTitle("設定提醒時間");
                     */
                    final Calendar c = Calendar.getInstance();
                    int hour = c.get(Calendar.HOUR_OF_DAY);
                    int min = c.get(Calendar.MINUTE);
                    new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            System.out.println(hourOfDay+"點" + minute+"分");
                            Toast.makeText(getActivity(), "您設定的是"+hourOfDay + "點" + minute +"分" , Toast.LENGTH_LONG).show();
                        }
                    }, hour, min, true).show();

                    //setremind.show();
                }else {
                    Toast.makeText(getActivity(),"關閉提醒",Toast.LENGTH_LONG).show();
                    buttonView.setChecked(false);
                }
            }
        });

    }




}
