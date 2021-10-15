package com.example.a110407_app.RetrofitAPI;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {

    // 以Singleton模式建立
    private static RetrofitManager mInstance = new RetrofitManager();

    private APIService ourAPIService;

    private RetrofitManager() {

        // 設置baseUrl即要連的網站，addConverterFactory用Gson作為資料處理Converter
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://server.gywang.io:8084/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ourAPIService = retrofit.create(APIService.class);
    }

    public static RetrofitManager getInstance() {
        return mInstance;
    }

    public APIService getAPI() {
        return ourAPIService;
    }
}
