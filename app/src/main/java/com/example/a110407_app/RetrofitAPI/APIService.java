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
import com.example.a110407_app.Model.Status;
import com.example.a110407_app.Model.User;
import com.example.a110407_app.Model.UserDiary;
import com.example.a110407_app.Model.UserLogin;
import com.example.a110407_app.Model.UserNickName;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface APIService {

//    @GET("/api/index/moodtalk")    // 設置一個GET連線，路徑為albums/1
//    Call<Sentence> getmoodtalk();

    @GET("api/status/status") // 用@Body表示要傳送Body資料
    Call<Status> getUserStatus(@Header("Authorization")String authHeader);

    @POST("api/status/status") // 用@Body表示要傳送Body資料
    Call<Status> postUserStatus(@Header("Authorization")String authHeader,@Body Status status);

    @PUT("api/status/status") // 用@Body表示要傳送Body資料
    Call<Status> putUserStatus(@Header("Authorization")String authHeader,@Body Status status);

    @GET("/api/index/moodtalk")    // 設置一個GET連線，路徑為albums/1
    Call<MoodTalk> getMoodTalk();

    @POST("api/commonauth/users") // 用@Body表示要傳送Body資料
    Call<User> postUser(@Body User user);

    @PUT("api/commonauth/users") // 用@Body表示要傳送Body資料
    Call<UserNickName> putUserNickname(@Header("Authorization")String authHeader,@Body UserNickName nickname);

    @GET("api/commonauth/users") // 用@Body表示要傳送Body資料
    Call<User> getUserData(@Header("Authorization")String authHeader);

    @POST("api/commonauth/tokens") // 用@Body表示要傳送Body資料
    Call<UserLogin> postUserAccountAndPassword(@Body UserLogin userLogin);

    @GET("/api/diary/diarys/{diaryId}")//回傳單篇日記
    Call<UserDiary>getUserSingleDiary(@Header("Authorization")String authHeader, @Path("diaryId") String DiaryId);

    @DELETE("/api/diary/diarys/{diaryId}")//刪除單篇日記
    Call<UserDiary>deleteUserSingleDiary(@Header("Authorization")String authHeader, @Path("diaryId") String DiaryId);

    @GET("/api/diary/diarys")//回傳所有日記
    Call<UserDiary>postUserAllDiary(@Header("Authorization")String authHeader);

    @POST("/api/diary/diarys")//新增日記
    Call<UserDiary>postUserDiary(@Header("Authorization")String authHeader, @Body UserDiary userDiary );

    @PUT("/api/diary/diarys/{diaryId}")//更新日記
    Call<UserDiary>putUserDiary(@Header("Authorization")String authHeader, @Body UserDiary userDiary, @Path("diaryId") String DiaryId );

    @POST("/api/bert/content")
    Call<MoodPredict>postMoodPredict(@Header("Authorization")String authHeader, @Body MoodPredict moodPredict);

    @POST("/api/moodtree/days") //取得moodtree圖片
    Call<MoodTree>postMoodTree(@Header("Authorization")String authHeader, @Body MoodTree moodTree);

    @POST("/api/analysis/analysisscore/days") //取得期間內日記分數平均
    Call<MoodAnalysisScore>postMoodAnalysisScore(@Header("Authorization")String authHeader, @Body MoodAnalysisScore moodAnalysisScore);

    @POST("/api/analysis/analysis/days") //取得期間內日記中最多的兩個正負標籤
    Call<MoodAnalysisTags>postMoodAnalysisTags(@Header("Authorization")String authHeader, @Body MoodAnalysisTags moodAnalysisTags);

    @PUT("/api/analysis/analysis/days") // 取得期間內日記篇數
    Call<MoodAnalysisCountDiarys>putMoodAnalysisCountDiarys(@Header("Authorization")String authHeader, @Body MoodAnalysisCountDiarys moodAnalysisCountDiarys);

    @POST("/api/analysis/piechart/days") // 取得期間內日記圓餅圖
    Call<MoodAnalysisPiechart>postMoodAnalysisPiechart(@Header("Authorization")String authHeader, @Body MoodAnalysisPiechart moodAnalysisPiechart);

    @POST("/api/analysis/linechart/days") // 取得期間內日記折線圖
    Call<MoodAnalysisLinechart>postMoodAnalysisLinechart(@Header("Authorization")String authHeader, @Body MoodAnalysisLinechart moodAnalysisLinechart);




}
