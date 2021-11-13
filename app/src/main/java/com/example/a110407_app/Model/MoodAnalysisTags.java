package com.example.a110407_app.Model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class MoodAnalysisTags {

    private String result;
    private String start;
    private String end;
    private JsonArray positive_tags;
    private JsonArray negative_tags;

    public MoodAnalysisTags(String start, String end){
       this.start = start;
       this.end = end;
    }

    public String getResult(){
        return result;
    }
    public JsonArray getPositive_tags(){
        return positive_tags;
    }
    public JsonArray getNegative_tags(){
        return negative_tags;
    }
}
