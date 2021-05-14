package com.example.a110407_app.ui.profile;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.Nullable;
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
import com.example.a110407_app.ui.SQLiteDBHelper;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.lang.Math;

import static androidx.core.content.ContextCompat.getSystemService;

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

    SQLiteDBHelper mHelper;
    private final String DB_NAME = "MyDairy.db";
    private String TABLE_NAME = "MyDairy";
    private final int DB_VERSION = 13;
    private ArrayList<HashMap<String, String>> diaryList;

    private Button editprofile;
    private Switch setpassword;
    private Switch remind;
    private TextView setpasswordtext;
    private TextView remindtext;
    private EditText getpassword, getconfirmpassword;
    private String strpassword, strconfirmpassword;
    private  String stringDate;
    private TimePicker setremindtime;

    SQLiteDBHelper TableUserPassword;
    private String PASSWORD_TABLE_NAME = "UserPassword";

    private TimePickerDialog timePickerDialog;


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
        name.setText("Gary Wang");
        username = (TextView) getView().findViewById(R.id.profileUsername);
        username.setText("guanyu0827");
        email = (TextView) getView().findViewById(R.id.profileEmail);
        email.setText("garywang0827@gmail.com");
        birthday = (TextView) getView().findViewById(R.id.profileBirhtday);
        birthday.setText("20000827");

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
        setpassword = getView().findViewById(R.id.setpassword);
        remind = getView().findViewById(R.id.remind);
        setpasswordtext = getView().findViewById(R.id.setpasswordtext);
        remindtext = getView().findViewById(R.id.remindtext);
        setpasswordtext.setText("密碼鎖");
        remindtext.setText("提醒");


        TableUserPassword = new SQLiteDBHelper(getActivity(),DB_NAME,null,DB_VERSION,PASSWORD_TABLE_NAME);
        TableUserPassword.getWritableDatabase();
        setpassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()){
                    AlertDialog.Builder setpassword = new AlertDialog.Builder(getActivity());
                    final View view = getLayoutInflater().inflate(R.layout.setpassword, null);
                    setpassword.setView(view);
                    setpassword.setTitle("設定螢幕密碼");
                    Integer date = 0;
                    Date mDate = new Date();
                    date = mDate.getDate();
                    stringDate = date.toString();


                    setpassword.setPositiveButton("確認", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getpassword = (EditText) view.findViewById(R.id.editsetpassword);
                            getconfirmpassword = (EditText) view.findViewById(R.id.confirmeditsetpassword);
                            strpassword = getpassword.getText().toString();
                            strconfirmpassword = getconfirmpassword.getText().toString();
                            if(strpassword.equals(strconfirmpassword)){
                                if(strpassword.length() > 5){
                                    Toast.makeText(getActivity(),"設置成功",Toast.LENGTH_LONG).show();
                                    buttonView.setChecked(true);
                                    TableUserPassword.addPassword(strpassword,stringDate);
                                }
                            }else{
                                Toast.makeText(getActivity(),"密碼不相符",Toast.LENGTH_LONG).show();
                                buttonView.setChecked(false);
                            }

                        }
                    });
                    setpassword.show();
                }
                else {
                    Toast.makeText(getActivity(),"關閉螢幕鎖",Toast.LENGTH_LONG).show();
                }

            }

        });

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