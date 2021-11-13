package com.example.a110407_app.Model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class MoodAnalysisScore {
    private String result;
    private String start;
    private String end;
    private float score;
    public MoodAnalysisScore(String start, String end){
        this.start = start;
        this.end = end;
    }

    public String getResult(){
        return result;
    }

    public float getScore(){
        return score;
    }
}
