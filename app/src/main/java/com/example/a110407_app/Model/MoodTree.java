package com.example.a110407_app.Model;

import com.google.gson.JsonObject;

public class MoodTree {
    private String start;
    private String end;
    private String result;
    private String tree_image_url;
    private String words;

    public MoodTree(String start, String end){
        this.start = start;
        this.end = end;
    }

    public String getResult(){
        return result;
    }
    public String getImage_url(){
        return tree_image_url;
    }
    public String getWords(){
        return  words;
    }
}
