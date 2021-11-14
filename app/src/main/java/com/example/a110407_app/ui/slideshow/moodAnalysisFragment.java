package com.example.a110407_app.ui.slideshow;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a110407_app.Model.MoodAnalysisCountDiarys;
import com.example.a110407_app.Model.MoodAnalysisLinechart;
import com.example.a110407_app.Model.MoodAnalysisPiechart;
import com.example.a110407_app.Model.MoodAnalysisScore;
import com.example.a110407_app.Model.MoodAnalysisTags;
import com.example.a110407_app.R;
import com.example.a110407_app.RetrofitAPI.APIService;
import com.example.a110407_app.RetrofitAPI.RetrofitManager;
import com.google.gson.JsonArray;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link moodAnalysisFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class moodAnalysisFragment extends Fragment {
    String userToken;
    private TextView diary_count;
    private TextView moodScore;
    private ImageView moodScoreAverage;
    private ImageView pieChart;
    private ImageView lineChart;
    String diary_count_result;
    int diary_count_diarys;
    String moodscore_result;
    Float moodscore;
//    String piechart_result;
//    String pie_image_url;
//    String linechart_result;
//    String linechart_image_url;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public moodAnalysisFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment moodAnalysisActivity.
     */
    // TODO: Rename and change types and number of parameters
    public static moodAnalysisFragment newInstance(String param1, String param2) {
        moodAnalysisFragment fragment = new moodAnalysisFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        System.out.println("跳到心情分析畫面了喔");
        String start = getArguments().getString("start");
        String end = getArguments().getString("end");
        APIService ourAPIService;
        ourAPIService = RetrofitManager.getInstance().getAPI();
        Intent intent = getActivity().getIntent();
        userToken = intent.getStringExtra("userToken");
        System.out.println("心情分析的token: " + userToken);
        MoodAnalysisCountDiarys moodAnalysisCountDiarys = new MoodAnalysisCountDiarys(start,end);
        Call<MoodAnalysisCountDiarys> callAnalysisCountDiarys = ourAPIService.putMoodAnalysisCountDiarys("bearer " + userToken, moodAnalysisCountDiarys);
        MoodAnalysisScore moodAnalysisScore = new MoodAnalysisScore(start,end);
        Call<MoodAnalysisScore> callAnalysisScore = ourAPIService.postMoodAnalysisScore("bearer " + userToken, moodAnalysisScore);
        MoodAnalysisTags moodAnalysisTags = new MoodAnalysisTags(start,end);
        Call<MoodAnalysisTags> callMoodAnalysisTags = ourAPIService.postMoodAnalysisTags("bearer " + userToken, moodAnalysisTags);
        MoodAnalysisPiechart moodAnalysisPiechart = new MoodAnalysisPiechart(start,end);
        Call<MoodAnalysisPiechart> callMoodAnalysisPiechart = ourAPIService.postMoodAnalysisPiechart("bearer " + userToken, moodAnalysisPiechart);
        MoodAnalysisLinechart moodAnalysisLinechart = new MoodAnalysisLinechart(start,end);
        Call<MoodAnalysisLinechart> callmoodAnalysisLineChart = ourAPIService.postMoodAnalysisLinechart("bearer " + userToken, moodAnalysisLinechart);
//        diary_count = getActivity().findViewById(R.id.diary_count);
        //取得日記篇數
        callAnalysisCountDiarys.enqueue(new Callback<MoodAnalysisCountDiarys>() {
            @Override
            public void onResponse(Call<MoodAnalysisCountDiarys> call, Response<MoodAnalysisCountDiarys> response) {
                try {
                    diary_count_result = response.message();
                    diary_count_diarys = response.body().getCount_diarys();
                    String string_diary_count = String.valueOf(diary_count_diarys);
                    diary_count = getActivity().findViewById(R.id.diary_count);
                    diary_count.setText(string_diary_count);
                }catch (Exception e){
                    System.out.println(e);
                    System.out.println("回應日記篇數失敗");
                }

            }


            @Override
            public void onFailure(Call<MoodAnalysisCountDiarys> call, Throwable t) {
                Log.d("HKT", "response: " + t.toString());
            }


        });

        //取得心情分數
        callAnalysisScore.enqueue(new Callback<MoodAnalysisScore>() {
            @Override
            public void onResponse(Call<MoodAnalysisScore> call, Response<MoodAnalysisScore> response) {
                try {
                    String result = response.message();
                    moodscore = response.body().getScore();
                    moodScore = getActivity().findViewById(R.id.moodScore);
                    String string_moodscore = String.valueOf(moodscore);
                    moodScore.setText(string_moodscore);
                }catch (Exception e){
                    System.out.println(e);
                    System.out.println("回應日記篇數失敗");
                }

            }
            @Override
            public void onFailure(Call<MoodAnalysisScore> call, Throwable t) {
                Log.d("HKT", "response: " + t.toString());
            }
        });

        //取得標籤
        callMoodAnalysisTags.enqueue(new Callback<MoodAnalysisTags>() {
            @Override
            public void onResponse(Call<MoodAnalysisTags> call, Response<MoodAnalysisTags> response) {
                String result = response.message();
                JsonArray positive_tags = response.body().getPositive_tags();
                JsonArray negative_tags = response.body().getNegative_tags();
                System.out.println(positive_tags.get(0).toString());
                System.out.println(negative_tags.get(1).toString());
            }

            @Override
            public void onFailure(Call<MoodAnalysisTags> call, Throwable t) {
                Log.d("HKT", "response: " + t.toString());
            }
        });

        //取得圓餅圖url
        callMoodAnalysisPiechart.enqueue(new Callback<MoodAnalysisPiechart>() {
            @Override
            public void onResponse(Call<MoodAnalysisPiechart> call, Response<MoodAnalysisPiechart> response) {
                try {
                    String result = response.message();
                    String pie_image_url = response.body().getImage_url();
                    System.out.println(result);
                    System.out.println("piechart: " + pie_image_url);
                    pieChart = getActivity().findViewById(R.id.pieChart);
                    Picasso.get().load("http://server.gywang.io:8084/" + pie_image_url).fit().centerCrop().into(pieChart);
                }catch (Exception e){
                    System.out.println(e);
                    System.out.println("回應圓餅圖失敗");
                }

            }
            @Override
            public void onFailure(Call<MoodAnalysisPiechart> call, Throwable t) {
                Log.d("HKT", "response: " + t.toString());

            }
        });

        //取得折線圖url
        callmoodAnalysisLineChart.enqueue(new Callback<MoodAnalysisLinechart>() {
            @Override
            public void onResponse(Call<MoodAnalysisLinechart> call, Response<MoodAnalysisLinechart> response) {
                try {
                    String result = response.message();
                    String line_image_url = response.body().getImage_url();
                    System.out.println(result);
                    System.out.println(line_image_url);
                    lineChart = getActivity().findViewById(R.id.lineChart);
                    Picasso.get().load("http://server.gywang.io:8084/" + line_image_url).fit().centerCrop().into(lineChart);
                }catch (Exception e){
                    System.out.println(e);
                    System.out.println("回應折線圖失敗");
                }

            }

            @Override
            public void onFailure(Call<MoodAnalysisLinechart> call, Throwable t) {
                Log.d("HKT", "response: " + t.toString());
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mood_analysis_activity, container, false);
    }
}