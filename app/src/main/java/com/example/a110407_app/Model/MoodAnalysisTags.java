package com.example.a110407_app.Model;

import com.google.gson.JsonObject;

public class MoodAnalysisTags {

    private String result;
    private JsonObject positive_tags;
    private JsonObject negative_tags;

    public MoodAnalysisTags(String result, JsonObject positive_tags, JsonObject negative_tags){
        this.result = result;
        this.positive_tags = positive_tags;
        this.negative_tags = negative_tags;
    }

    public String getResult(){
        return result;
    }

    public JsonObject getPositive_tags(){
        return positive_tags;
    }
    public JsonObject getNegative_tags(){
        return negative_tags;
    }
}
