package com.example.a110407_app.RetrofitAPI;

import com.example.a110407_app.Model.MoodAnalysis;
import com.example.a110407_app.Model.MoodAnalysisCountDiarys;
import com.example.a110407_app.Model.MoodAnalysisLinechart;
import com.example.a110407_app.Model.MoodAnalysisPiechart;
import com.example.a110407_app.Model.MoodAnalysisScore;
import com.example.a110407_app.Model.MoodAnalysisTags;
import com.example.a110407_app.Model.MoodPredict;
import com.example.a110407_app.Model.MoodTalk;
import com.example.a110407_app.Model.MoodTree;
import com.example.a110407_app.Model.User;
import com.example.a110407_app.Model.UserDiary;
import com.example.a110407_app.Model.UserLogin;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface APIService {

//    @GET("/api/index/moodtalk")    // 設置一個GET連線，路徑為albums/1
//    Call<Sentence> getmoodtalk();

    @GET("/api/index/moodtalk")    // 設置一個GET連線，路徑為albums/1
    Call<MoodTalk> getMoodTalk();

    @POST("api/commonauth/users") // 用@Body表示要傳送Body資料
    Call<User> postUser(@Body User user);

    @POST("api/commonauth/tokens") // 用@Body表示要傳送Body資料
    Call<UserLogin> postUserAccountAndPassword(@Body UserLogin userLogin);

    @POST("/api/diary/diarys")
    Call<UserDiary>postUserDiary(@Header("Authorization")String authHeader, @Body UserDiary userDiary );

    @POST("/api/bert/content")
    Call<MoodPredict>postMoodPredict(@Header("Authorization")String authHeader, @Body MoodPredict moodPredict);

    @GET("/api/moodtree/days") //取得moodtree圖片
    Call<MoodTree>getMoodTree(@Header("Authorization")String authHeader, @Body MoodTree moodTree);

    @GET("/api/analysis/analysis/days") //取得期間內日記分數平均
    Call<MoodAnalysisScore>getMoodAnalysisScore(@Header("Authorization")String authHeader, @Body MoodAnalysisScore moodAnalysisScore);

    @POST("/api/analysis/analysis/days") //取得期間內日記中最多的兩個正負標籤
    Call<MoodAnalysisTags>postMoodAnalysisTags(@Header("Authorization")String authHeader, @Body MoodAnalysisTags moodAnalysisTags);

    @PUT("/api/analysis/analysis/days") // 取得期間內日記篇數
    Call<MoodAnalysisCountDiarys>putMoodAnalysisCountDiarys(@Header("Authorization")String authHeader, @Body MoodAnalysisCountDiarys moodAnalysisCountDiarys);

    @GET("/api/analysis/piechart/days") // 取得期間內日記圓餅圖
    Call<MoodAnalysisPiechart>getMoodAnalysisPiechart(@Header("Authorization")String authHeader, @Body MoodAnalysisPiechart moodAnalysisPiechart);

    @GET("/api/analysis/linechart/days") // 取得期間內日記折線圖
    Call<MoodAnalysisLinechart>getMoodAnalysisLinechart(@Header("Authorization")String authHeader, @Body MoodAnalysisLinechart moodAnalysisLinechart);




}
