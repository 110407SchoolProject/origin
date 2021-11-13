package com.example.a110407_app.ui.slideshow;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;

import com.example.a110407_app.Model.MoodAnalysis;
import com.example.a110407_app.Model.MoodAnalysisCountDiarys;
import com.example.a110407_app.Model.MoodAnalysisLinechart;
import com.example.a110407_app.Model.MoodAnalysisPiechart;
import com.example.a110407_app.Model.MoodAnalysisScore;
import com.example.a110407_app.Model.MoodAnalysisTags;
import com.example.a110407_app.R;
import com.example.a110407_app.RetrofitAPI.APIService;
import com.example.a110407_app.RetrofitAPI.RetrofitManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.navigation.fragment.NavHostFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;

//選擇起始日期跟結束日期的畫面  for 心情分析


public class SlideshowFragment extends Fragment {

    private ImageView myDatePicker;
    private ImageView myDatePicker2;
    private int mYear;
    private int mMonth;
    private int mDay;
    static final int DATE_DIALOG_ID = 0;
    private SlideshowViewModel slideshowViewModel;

    private Button btnMoodAnalysis;

    APIService ourAPIService;
    private String userToken;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                ViewModelProviders.of(this).get(SlideshowViewModel.class);
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        final TextView textView = root.findViewById(R.id.text_slideshow);
        slideshowViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        //按鈕 --> 送出日期
//        btnMoodAnalysis = root.findViewById(R.id.btnMoodAnalysis);
//        ourAPIService = RetrofitManager.getInstance().getAPI();
//        Intent intent = getActivity().getIntent();
//        userToken = intent.getStringExtra("userToken");
//        System.out.println("心情分析的token: " + userToken);
//
//        btnMoodAnalysis.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getActivity(), "按鈕有效", Toast.LENGTH_LONG).show();
//                MoodAnalysisCountDiarys moodAnalysisCountDiarys = new MoodAnalysisCountDiarys("2021-10-10","2021-11-12");
//                Call<MoodAnalysisCountDiarys> callAnalysisCountDiarys = ourAPIService.putMoodAnalysisCountDiarys("bearer " + userToken, moodAnalysisCountDiarys);
//                MoodAnalysisScore moodAnalysisScore = new MoodAnalysisScore("2021-10-10","2021-11-12");
//                Call<MoodAnalysisScore> callAnalysisScore = ourAPIService.postMoodAnalysisScore("bearer " + userToken, moodAnalysisScore);
//                MoodAnalysisTags moodAnalysisTags = new MoodAnalysisTags("2021-10-10","2021-11-12");
//                Call<MoodAnalysisTags> callMoodAnalysisTags = ourAPIService.postMoodAnalysisTags("bearer " + userToken, moodAnalysisTags);
//                MoodAnalysisPiechart moodAnalysisPiechart = new MoodAnalysisPiechart("2021-10-10","2021-11-12");
//                Call<MoodAnalysisPiechart> callMoodAnalysisPiechart = ourAPIService.postMoodAnalysisPiechart("bearer " + userToken, moodAnalysisPiechart);
//                MoodAnalysisLinechart moodAnalysisLinechart = new MoodAnalysisLinechart("2021-10-10","2021-11-12");
//                Call<MoodAnalysisLinechart> callmoodAnalysisLineChart = ourAPIService.postMoodAnalysisLinechart("bearer " + userToken, moodAnalysisLinechart);
//
//
//                callAnalysisCountDiarys.enqueue(new Callback<MoodAnalysisCountDiarys>() {
//                    @Override
//                    public void onResponse(Call<MoodAnalysisCountDiarys> call, Response<MoodAnalysisCountDiarys> response) {
//                        String result = response.message();
//                        int count_diarys = response.body().getCount_diarys();
//                    }
//                    @Override
//                    public void onFailure(Call<MoodAnalysisCountDiarys> call, Throwable t) {
//                        Log.d("HKT", "response: " + t.toString());
//                    }
//                });
//
//                callAnalysisScore.enqueue(new Callback<MoodAnalysisScore>() {
//                    @Override
//                    public void onResponse(Call<MoodAnalysisScore> call, Response<MoodAnalysisScore> response) {
//                        String result = response.message();
//                        float score = response.body().getScore();
//                        System.out.println(score);
//                    }
//
//                    @Override
//                    public void onFailure(Call<MoodAnalysisScore> call, Throwable t) {
//                        Log.d("HKT", "response: " + t.toString());
//                    }
//                });
//
//                callMoodAnalysisTags.enqueue(new Callback<MoodAnalysisTags>() {
//                    @Override
//                    public void onResponse(Call<MoodAnalysisTags> call, Response<MoodAnalysisTags> response) {
//                        String result = response.message();
//                        JsonArray postive_tags = response.body().getPositive_tags();
//                        JsonArray negative_tags = response.body().getNegative_tags();
//
//                        System.out.println(postive_tags.get(0).toString());
//                        System.out.println(negative_tags.get(1).toString());
////                        System.out.println(postive_tags.get(0).toString().getClass().getSimpleName());
////                        System.out.println(postive_tags);
////                        System.out.println(negative_tags);
//                    }
//
//                    @Override
//                    public void onFailure(Call<MoodAnalysisTags> call, Throwable t) {
//                        Log.d("HKT", "response: " + t.toString());
//                    }
//                });
//
//                callMoodAnalysisPiechart.enqueue(new Callback<MoodAnalysisPiechart>() {
//                    @Override
//                    public void onResponse(Call<MoodAnalysisPiechart> call, Response<MoodAnalysisPiechart> response) {
//                        String result = response.message();
//                        String pie_image_url = response.body().getImage_url();
//                        System.out.println(pie_image_url);
//                    }
//
//                    @Override
//                    public void onFailure(Call<MoodAnalysisPiechart> call, Throwable t) {
//                        Log.d("HKT", "response: " + t.toString());
//                    }
//                });
//
//                callmoodAnalysisLineChart.enqueue(new Callback<MoodAnalysisLinechart>() {
//                    @Override
//                    public void onResponse(Call<MoodAnalysisLinechart> call, Response<MoodAnalysisLinechart> response) {
//                        String result = response.message();
//                        String line_image_url = response.body().getImage_url();
//                        System.out.println(line_image_url);
//                    }
//
//                    @Override
//                    public void onFailure(Call<MoodAnalysisLinechart> call, Throwable t) {
//                        Log.d("HKT", "response: " + t.toString());
//                    }
//                });
//            }
//
//        });
//        //切換到心情分析結果的畫面
//        Bundle bundle = new Bundle();
//        bundle.putString("start", "2021-10-10");
//        bundle.putString("end", "2021-11-13");
//        NavHostFragment.findNavController(SlideshowFragment.this).navigate((R.id.nav_mood_analysis), bundle);


        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnMoodAnalysis = getActivity().findViewById(R.id.btnMoodAnalysis);
        btnMoodAnalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("成功ㄌ");
                Bundle bundle = new Bundle();
                bundle.putString("start", "2021-10-10");
                bundle.putString("end", "2021-11-14");
                NavHostFragment.findNavController(SlideshowFragment.this).navigate((R.id.nav_mood_analysis), bundle);
            }
        });
    }

}
