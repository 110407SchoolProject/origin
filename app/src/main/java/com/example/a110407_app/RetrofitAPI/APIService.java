package com.example.a110407_app.RetrofitAPI;

import com.example.a110407_app.Model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface APIService {

//    @GET("/api/index/moodtalk")    // 設置一個GET連線，路徑為albums/1
//    Call<Sentence> getmoodtalk();

    @POST("api/commonauth/users") // 用@Body表示要傳送Body資料
    Call<User> postUser(@Body User user);
}
