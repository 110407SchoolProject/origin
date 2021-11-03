package com.example.a110407_app.Model;

import com.google.gson.JsonObject;

public class MoodAnalysis {
    String result;
    String url;
    int score;
    JsonObject positive_tags;
    JsonObject negative_tags;
    int count_diarys;
    String image_url;

    public MoodAnalysis(String result, String url, int score, JsonObject positive_tags, JsonObject negative_tags, int count_diarys, String image_url){
        this.result = result;
        this.url = url;
        this.score = score;
        this.positive_tags = positive_tags;
        this.negative_tags = negative_tags;
        this.count_diarys = count_diarys;
        this.image_url = image_url;
    }

    public String getResult(){
        return result;
    }

    public String getUrl(){
        return url;
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
    public String getImage_url(){
        return image_url;
    }

}
