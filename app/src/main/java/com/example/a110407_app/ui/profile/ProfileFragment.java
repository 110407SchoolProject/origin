package com.example.a110407_app.ui.profile;

import android.media.Image;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a110407_app.R;
import com.example.a110407_app.ui.SQLiteDBHelper;

import org.w3c.dom.Text;

import java.util.ArrayList;
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

    SQLiteDBHelper mHelper;
    private final String DB_NAME = "MyDairy.db";
    private String TABLE_NAME = "MyDairy";
    private final int DB_VERSION = 5;
    private ArrayList<HashMap<String, String>> diaryList;

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




    }


}