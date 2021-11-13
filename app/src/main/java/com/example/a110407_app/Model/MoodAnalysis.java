package com.example.a110407_app.Model;

import com.google.gson.JsonObject;

public class MoodAnalysis {
    String result;
    String start;
    String end;
    int score;
    JsonObject positive_tags;
    JsonObject negative_tags;
    int count_diarys;
    String pie_image_url;
    String line_image_url;

    public MoodAnalysis(String start, String end){
        this.start =start;
        this.end = end;
    }

    public String getResult(){
        return result;
    }
    public int getScore(){
        return score;
    }
    public JsonObject getPositive_tags(){
        return positive_tags;
    }
    public JsonObject getNegative_tags(){
        return negative_tags;
    }
    public int getCount_diarys(){
        return count_diarys;
    }
    public String getPie_image_url(){
        return pie_image_url;
    }
    public String getLine_image_url(){
        return line_image_url;
    }

}
