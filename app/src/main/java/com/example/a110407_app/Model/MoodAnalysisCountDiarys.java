package com.example.a110407_app.Model;

import com.google.gson.JsonObject;

public class MoodAnalysisCountDiarys {
    String result;
    private String start;
    private String end;
    private int count_diarys;
    public MoodAnalysisCountDiarys(String start, String end){
        this.start = start;
        this.end = end;
    }

    public String getResult(){
        return result;
    }

    public int getCount_diarys(){
        return count_diarys;
    }
}
