package com.example.a110407_app.Model;

public class MoodAnalysisPiechart {
    private String result;
    private String image_url;

    public MoodAnalysisPiechart(String result, String image_url){
        this.result = result;
        this.image_url = image_url;
    }

    public String getResult(){
        return result;
    }

    public String getImage_url(){
        return image_url;
    }
}
