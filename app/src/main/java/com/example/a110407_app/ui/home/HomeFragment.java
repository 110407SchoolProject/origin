package com.example.a110407_app.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.a110407_app.EditDiaryActivity;
import com.example.a110407_app.Model.MoodTalk;
import com.example.a110407_app.Model.Token;
import com.example.a110407_app.Model.UserDiary;
import com.example.a110407_app.R;
import com.example.a110407_app.RetrofitAPI.APIService;
import com.example.a110407_app.RetrofitAPI.RetrofitManager;
import com.example.a110407_app.ShowDiaryActivity;
import com.example.a110407_app.ui.SQLiteDBHelper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    APIService ourAPIService;

    private String userToken;
    private HomeViewModel homeViewModel;
    private TextView dateTimeText;

    ArrayList titleArrayList = new ArrayList();
    ArrayList diaryIdList = new ArrayList();
    private ListView diaryListView;
//    private ListView diaryListView;
//    SQLiteDBHelper mHelper;
//    private final String DB_NAME = "MyDairy.db";
//    private String TABLE_NAME = "MyDairy";
//    private final int DB_VERSION = 7;
//    private ArrayList<HashMap<String, String>> diaryTitleList;

    private TextView inspiringSentence;


    public void openActivityShowDiary(String diaryId, String userToken){
        Intent intent = new Intent(getActivity(), ShowDiaryActivity.class);
        intent.putExtra("userToken",userToken);
        intent.putExtra("id",diaryId);
        startActivity(intent);
    }



    public void openActivityEditDiary(){
        Intent intent = new Intent(getActivity(), EditDiaryActivity.class);
        startActivity(intent);
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);

        Intent intent = getActivity().getIntent();
        userToken =intent.getStringExtra("userToken");

        System.out.println("從Home接到userToken:    "+userToken);
        inspiringSentence=(TextView)root.findViewById(R.id.inspiringSentence);
        ourAPIService = RetrofitManager.getInstance().getAPI();
        //心情小語
        Call<MoodTalk> callMoodTalk = ourAPIService.getMoodTalk();
        callMoodTalk.enqueue(new Callback<MoodTalk>() {
            @Override
            public void onResponse(Call<MoodTalk> call, Response<MoodTalk> response) {
                System.out.println("伺服器有回應");
//                String result= response.body().getResult();
                JsonObject sentenceJsonObject= response.body().getSentence();
                String sentence = sentenceJsonObject.get("sentence").toString();
                sentence =sentence.substring(1,sentence.length()-1);
                inspiringSentence.setText(sentence);
            }
            @Override
            public void onFailure(Call<MoodTalk> call, Throwable t) {
                System.out.println("伺服器連線失敗");
                Log.d("HKT", "response: " + t.toString());
            }
        });

        dateTimeText=(TextView)root.findViewById(R.id.DateTextView);

        Integer month = 0;
        Integer date = 0;
        Integer Year =0;
        Integer day=0;
        Date mDate = new Date();
        Year = mDate.getYear()-100+2000;
        month = mDate.getMonth() + 1;
        date = mDate.getDate();
        day= mDate.getDay();
        System.out.println(day);
        String weekday[]={"日","一","二","三","四","五","六",};
        final String stringDate = date.toString();
        final String stringMonth = month.toString();
        final String stringYear = Year.toString();

        dateTimeText.setText(Year+"-"+month+"-"+date+"     "+"星期"+weekday[day]);



        Call<UserDiary> callAllDiaryToList = ourAPIService.postUserAllDiary("bearer "+userToken);
        callAllDiaryToList.enqueue(new Callback<UserDiary>() {
            @Override
            public void onResponse(Call<UserDiary> call, Response<UserDiary> response) {
                JsonArray diaryAllList = response.body().getDiaryList();

                for(int i=0; i<diaryAllList.size();i++){
                    JsonObject diaryJsonObject = (JsonObject) diaryAllList.get(i);
                    String diaryTitle = diaryJsonObject.get("title").toString();
                    diaryTitle= diaryTitle.substring(1,diaryTitle.length()-1);
                    titleArrayList.add(diaryTitle);
                }

                for(int i=0;i<diaryAllList.size();i++){
                    JsonObject diaryJsonObject = (JsonObject) diaryAllList.get(i);
//                        System.out.println(diaryJsonObject.toString());
                    String diaryId = diaryJsonObject.get("diaryid").toString();
                    System.out.println("DiaryID:"+diaryId);
                    diaryId=diaryId.substring(1,diaryId.length()-1);
                    diaryIdList.add(diaryId);
                }

                diaryListView = (ListView)root.findViewById(R.id.diaryListViewInHome);
                ArrayAdapter adapter = new ArrayAdapter<>(getActivity(),R.layout.list_text_setting,titleArrayList);
                diaryListView.setAdapter(adapter);

                //開啟日記
                diaryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        Toast.makeText(getActivity(),"開啟日記"+(id+1), Toast.LENGTH_LONG).show();
                        System.out.println("position"+position);
                        //透過position來抓日記ID，接著將日記ID傳到下個頁面後，於下一頁call API。
                        String diaryIdToCall = diaryIdList.get(position).toString();
                        System.out.println("要傳的ID:"+diaryIdToCall);
                        System.out.println("要傳的Token:"+userToken);
                        openActivityShowDiary(diaryIdToCall,userToken);
                    }
                });
            }

            @Override
            public void onFailure(Call<UserDiary> call, Throwable t) {
                System.out.println("伺服器連線失敗");
                Log.d("HKT", "response: " + t.toString());
            }
        });


//        //開啟該篇日記
//        public void openActivityShowDiary(String diaryId, String userToken){
//            Intent intent = new Intent(getActivity(), ShowDiaryActivity.class);
//            intent.putExtra("userToken",userToken);
//            intent.putExtra("id",diaryId);
//            startActivity(intent);
//        }

        return root;


    }
}
