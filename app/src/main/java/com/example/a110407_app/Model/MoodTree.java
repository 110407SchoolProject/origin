package com.example.a110407_app.Model;

import com.google.gson.JsonObject;

public class MoodTree {
    private String result;
    private String image_url;
    private String words;

    public MoodTree(String result, String image_url, String words){
        this.result = result;
        this.image_url = image_url;
        this.words = words;
    }

    public String getResult(){
        return result;
    }

    public String getImage_url(){
        return image_url;
    }
    public String getWords(){
        return  words;
    }
}
