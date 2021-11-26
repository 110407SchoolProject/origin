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
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

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
    private TextView dateTitleTextView;
    private TextView diary_count;
    private TextView moodScore;
    private ImageView moodScoreAverage;
    private ImageView pieChart;
    private ImageView lineChart;
    String diary_count_result;
    int diary_count_diarys;
    String moodscore_result;
    Float moodscore;
    private TextView positive_tag1; //positive_tag1
    private TextView positive_tag2; //positive_tag2
    private TextView positive_tag3; //positive_tag3
    private TextView negative_tag1; //negative_tag1
    private TextView negative_tag2; //negative_tag2
    private TextView negative_tag3; //negative_tag3

    private ImageView positiveTagImage1;
    private ImageView positiveTagImage2;
    private ImageView positiveTagImage3;
    private ImageView negativeTagImage1;
    private ImageView negativeTagImage2;
    private ImageView negativeTagImage3;
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
//        System.out.println("start開始 : " + start);
//        System.out.println("end結束: " + end);
        System.out.println("跳到心情分析畫面了喔");
        String startDate = getArguments().getString("start");
        String endDate = getArguments().getString("end");





        APIService ourAPIService;
        ourAPIService = RetrofitManager.getInstance().getAPI();
        Intent intent = getActivity().getIntent();
        userToken = intent.getStringExtra("userToken");
        System.out.println("心情分析的token: " + userToken);
        MoodAnalysisCountDiarys moodAnalysisCountDiarys = new MoodAnalysisCountDiarys(startDate,endDate);
        Call<MoodAnalysisCountDiarys> callAnalysisCountDiarys = ourAPIService.putMoodAnalysisCountDiarys("bearer " + userToken, moodAnalysisCountDiarys);
        MoodAnalysisScore moodAnalysisScore = new MoodAnalysisScore(startDate,endDate);
        Call<MoodAnalysisScore> callAnalysisScore = ourAPIService.postMoodAnalysisScore("bearer " + userToken, moodAnalysisScore);
        MoodAnalysisTags moodAnalysisTags = new MoodAnalysisTags(startDate,endDate);
        Call<MoodAnalysisTags> callMoodAnalysisTags = ourAPIService.postMoodAnalysisTags("bearer " + userToken, moodAnalysisTags);
//        MoodAnalysisPiechart moodAnalysisPiechart = new MoodAnalysisPiechart(start,end);
//        Call<MoodAnalysisPiechart> callMoodAnalysisPiechart = ourAPIService.postMoodAnalysisPiechart("bearer " + userToken, moodAnalysisPiechart);
//        MoodAnalysisLinechart moodAnalysisLinechart = new MoodAnalysisLinechart(start,end);
//        Call<MoodAnalysisLinechart> callmoodAnalysisLineChart = ourAPIService.postMoodAnalysisLinechart("bearer " + userToken, moodAnalysisLinechart);
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

                    dateTitleTextView = getActivity().findViewById(R.id.text_DateTitle);

                    SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");
                    Date beginDate =sdf.parse(startDate);
                    Date overDate =sdf.parse(endDate);

                    int startYear=(beginDate.getYear()-100+2000);
                    int startMonth=beginDate.getMonth()+1;
                    int startDate = beginDate.getDate();
                    int endYear=(overDate.getYear()-100+2000);
                    int endMonth=overDate.getMonth()+1;
                    int endDay = overDate.getDate();

                    String titleTextDate=startYear+"年"+startMonth+"月"+startDate+"日"+" - "+endYear+"年"+endMonth+"月"+endDay+"日";
                    dateTitleTextView.setText(titleTextDate);
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
                    //moodScore = getActivity().findViewById(R.id.moodScore);
                    String string_moodscore = String.valueOf(moodscore);
                    //moodScore.setText(string_moodscore);
                    System.out.println("心情評價分數："+moodscore);
                    moodScoreAverage = getActivity().findViewById(R.id.moodScoreAverage);
                    if(moodscore <= 1.5) {
                        moodScoreAverage.setImageResource(R.drawable.crying);
                    }else if ( moodscore <=2.5 && moodscore > 1.5){
                        moodScoreAverage.setImageResource(R.drawable.sad);
                    }else if (moodscore <=3.0 && moodscore > 2.5){
                        moodScoreAverage.setImageResource(R.drawable.normal);
                    }else if (moodscore <=3.5 && moodscore > 3.0){
                        moodScoreAverage.setImageResource(R.drawable.smiling);
                    }else if(moodscore <= 5.0 && moodscore > 3.5 ){
                        moodScoreAverage.setImageResource(R.drawable.exciting);
                    }else {
                        System.out.println("分數計算有誤");
                    }
                }catch (Exception e){
                    System.out.println(e);
                    System.out.println("回應平均心情失敗");
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
                try {
                    String result = response.message();
                    JsonArray positive_tags = response.body().getPositive_tags();
                    JsonArray negative_tags = response.body().getNegative_tags();

                    positiveTagImage1=getActivity().findViewById(R.id.positiveTagImage1);
                    positiveTagImage2=getActivity().findViewById(R.id.positiveTagImage2);
                    positiveTagImage3=getActivity().findViewById(R.id.positiveTagImage3);
                    negativeTagImage1=getActivity().findViewById(R.id.negativeTagImage1);
                    negativeTagImage2=getActivity().findViewById(R.id.negativeTagImage2);
                    negativeTagImage3=getActivity().findViewById(R.id.negativeTagImage3);


                    System.out.println(positive_tags.toString());
                    System.out.println(negative_tags.toString());

                    positive_tag1 = getActivity().findViewById(R.id.positive_tag1);
                    positive_tag2 = getActivity().findViewById(R.id.positive_tag2);
                    positive_tag3 = getActivity().findViewById(R.id.positive_tag3);
                    negative_tag1 = getActivity().findViewById(R.id.negative_tag1);
                    negative_tag2 = getActivity().findViewById(R.id.negative_tag2);
                    negative_tag3 = getActivity().findViewById(R.id.negative_tag3);

                    String positiveTag1=positive_tags.get(0).toString().substring(1,positive_tags.get(0).toString().length()-1);
                    String positiveTag2=positive_tags.get(1).toString().substring(1,positive_tags.get(1).toString().length()-1);
                    String positiveTag3=positive_tags.get(2).toString().substring(1,positive_tags.get(2).toString().length()-1);
                    String negativeTag1=negative_tags.get(0).toString().substring(1,negative_tags.get(0).toString().length()-1);
                    String negativeTag2=negative_tags.get(1).toString().substring(1,negative_tags.get(1).toString().length()-1);
                    String negativeTag3=negative_tags.get(2).toString().substring(1,negative_tags.get(2).toString().length()-1);

                    System.out.println("1-"+positiveTag1);
                    System.out.println("2-"+positiveTag2);
                    System.out.println("3-"+positiveTag3);
                    System.out.println("4-"+negativeTag1);
                    System.out.println("5-"+negativeTag2);
                    System.out.println("6-"+negativeTag3);


                    positive_tag1.setText(positiveTag1);
                    positive_tag2.setText(positiveTag2);
                    positive_tag3.setText(positiveTag3);
                    negative_tag1.setText(negativeTag1);
                    negative_tag2.setText(negativeTag2);
                    negative_tag3.setText(negativeTag3);

                    if(positiveTag1.equals("朋友")){
                        positiveTagImage1.setImageResource(R.drawable.friendicon);
                    }else if(positiveTag1.equals("家庭")){
                        positiveTagImage1.setImageResource(R.drawable.familyicon);
                    }else if(positiveTag1.equals("感情")){
                        positiveTagImage1.setImageResource(R.drawable.relationshipicon);
                    }else if(positiveTag1.equals("工作")){
                        positiveTagImage1.setImageResource(R.drawable.workicon);
                    }else if(positiveTag1.equals("上學")){
                        positiveTagImage1.setImageResource(R.drawable.schoolicon);
                    }else if(positiveTag1.equals("旅遊")){
                        positiveTagImage1.setImageResource(R.drawable.travelicon);
                    }else if(positiveTag1.equals("興趣")){
                        positiveTagImage1.setImageResource(R.drawable.hobbyicon);
                    }else{
                        positiveTagImage1.setImageResource(R.drawable.othericon);
                    }

                    if(positiveTag2.equals("朋友")){
                        positiveTagImage2.setImageResource(R.drawable.friendicon);
                    }else if(positiveTag2.equals("家庭")){
                        positiveTagImage2.setImageResource(R.drawable.familyicon);
                    }else if(positiveTag2.equals("感情")){
                        positiveTagImage2.setImageResource(R.drawable.relationshipicon);
                    }else if(positiveTag2.equals("工作")){
                        positiveTagImage2.setImageResource(R.drawable.workicon);
                    }else if(positiveTag2.equals("上學")){
                        positiveTagImage2.setImageResource(R.drawable.schoolicon);
                    }else if(positiveTag2.equals("旅遊")){
                        positiveTagImage2.setImageResource(R.drawable.travelicon);
                    }else if(positiveTag2.equals("興趣")){
                        positiveTagImage2.setImageResource(R.drawable.hobbyicon);
                    }else{
                        positiveTagImage2.setImageResource(R.drawable.othericon);
                    }

                    if(positiveTag3.equals("朋友")){
                        positiveTagImage3.setImageResource(R.drawable.friendicon);
                    }else if(positiveTag3.equals("家庭")){
                        positiveTagImage3.setImageResource(R.drawable.familyicon);
                    }else if(positiveTag3.equals("感情")){
                        positiveTagImage3.setImageResource(R.drawable.relationshipicon);
                    }else if(positiveTag3.equals("工作")){
                        positiveTagImage3.setImageResource(R.drawable.workicon);
                    }else if(positiveTag3.equals("上學")){
                        positiveTagImage3.setImageResource(R.drawable.schoolicon);
                    }else if(positiveTag3.equals("旅遊")){
                        positiveTagImage3.setImageResource(R.drawable.travelicon);
                    }else if(positiveTag3.equals("興趣")){
                        positiveTagImage3.setImageResource(R.drawable.hobbyicon);
                    }else{
                        positiveTagImage3.setImageResource(R.drawable.othericon);
                    }

                    if(negativeTag1.equals("朋友")){
                        negativeTagImage1.setImageResource(R.drawable.friendicon);
                    }else if(negativeTag1.equals("家庭")){
                        negativeTagImage1.setImageResource(R.drawable.familyicon);
                    }else if(negativeTag1.equals("感情")){
                        negativeTagImage1.setImageResource(R.drawable.relationshipicon);
                    }else if(negativeTag1.equals("工作")){
                        negativeTagImage1.setImageResource(R.drawable.workicon);
                    }else if(negativeTag1.equals("上學")){
                        negativeTagImage1.setImageResource(R.drawable.schoolicon);
                    }else if(negativeTag1.equals("旅遊")){
                        negativeTagImage1.setImageResource(R.drawable.travelicon);
                    }else if(negativeTag1.equals("興趣")){
                        negativeTagImage1.setImageResource(R.drawable.hobbyicon);
                    }else{
                        negativeTagImage1.setImageResource(R.drawable.othericon);
                    }

                    if(negativeTag2.equals("朋友")){
                        negativeTagImage2.setImageResource(R.drawable.friendicon);
                    }else if(negativeTag2.equals("家庭")){
                        negativeTagImage2.setImageResource(R.drawable.familyicon);
                    }else if(negativeTag2.equals("感情")){
                        negativeTagImage2.setImageResource(R.drawable.relationshipicon);
                    }else if(negativeTag2.equals("工作")){
                        negativeTagImage2.setImageResource(R.drawable.workicon);
                    }else if(negativeTag2.equals("上學")){
                        negativeTagImage2.setImageResource(R.drawable.schoolicon);
                    }else if(negativeTag2.equals("旅遊")){
                        negativeTagImage2.setImageResource(R.drawable.travelicon);
                    }else if(negativeTag2.equals("興趣")){
                        negativeTagImage2.setImageResource(R.drawable.hobbyicon);
                    }else{
                        negativeTagImage2.setImageResource(R.drawable.othericon);
                    }

                    if(negativeTag3.equals("朋友")){
                        negativeTagImage3.setImageResource(R.drawable.friendicon);
                    }else if(negativeTag3.equals("家庭")){
                        negativeTagImage3.setImageResource(R.drawable.familyicon);
                    }else if(negativeTag3.equals("感情")){
                        negativeTagImage3.setImageResource(R.drawable.relationshipicon);
                    }else if(negativeTag3.equals("工作")){
                        negativeTagImage3.setImageResource(R.drawable.workicon);
                    }else if(negativeTag3.equals("上學")){
                        negativeTagImage3.setImageResource(R.drawable.schoolicon);
                    }else if(negativeTag3.equals("旅遊")){
                        negativeTagImage3.setImageResource(R.drawable.travelicon);
                    }else if(negativeTag3.equals("興趣")){
                        negativeTagImage3.setImageResource(R.drawable.hobbyicon);
                    }else{
                        negativeTagImage3.setImageResource(R.drawable.othericon);
                    }





                }catch (Exception e){
                    System.out.println(e);
                    System.out.println("回應標籤失敗");
                }
            }

            @Override
            public void onFailure(Call<MoodAnalysisTags> call, Throwable t) {
                Log.d("HKT", "response: " + t.toString());
            }
        });
        //取得圓餅圖url
        MoodAnalysisPiechart moodAnalysisPiechart = new MoodAnalysisPiechart(startDate,endDate);
        Call<MoodAnalysisPiechart> callMoodAnalysisPiechart = ourAPIService.postMoodAnalysisPiechart("bearer " + userToken, moodAnalysisPiechart);
        callMoodAnalysisPiechart.enqueue(new Callback<MoodAnalysisPiechart>() {
            @Override
            public void onResponse(Call<MoodAnalysisPiechart> call, Response<MoodAnalysisPiechart> response) {
                try {
                    String result = response.message();
                    String pie_image_url = response.body().getImage_url();
                    System.out.println(result);
                    System.out.println("圓餅圖"+"piechart: " + pie_image_url);
                    pieChart = getActivity().findViewById(R.id.pieChart);
                    Picasso.get().load("http://server.gywang.io:8084/" + pie_image_url).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).fit().centerCrop().into(pieChart);
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

        try {
            Thread.sleep(100);
            System.out.println("Thread 成功了");
        }catch (Exception e){
            System.out.println("Thread失敗了");
        }

        //取得折線圖url
        MoodAnalysisLinechart moodAnalysisLinechart = new MoodAnalysisLinechart(startDate,endDate);
        Call<MoodAnalysisLinechart> callmoodAnalysisLineChart = ourAPIService.postMoodAnalysisLinechart("bearer " + userToken, moodAnalysisLinechart);
        callmoodAnalysisLineChart.enqueue(new Callback<MoodAnalysisLinechart>() {
            @Override
            public void onResponse(Call<MoodAnalysisLinechart> call, Response<MoodAnalysisLinechart> response) {
                try {
                    String result = response.message();
                    String line_image_url = response.body().getImage_url();
                    System.out.println(result);
                    System.out.println("長條圖"+line_image_url);
                    lineChart = getActivity().findViewById(R.id.lineChart);
                    Picasso.get().load("http://server.gywang.io:8084/" + line_image_url).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).fit().centerCrop().into(lineChart);
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