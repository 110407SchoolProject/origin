package com.example.a110407_app.Model;

public class MoodAnalysisLinechart {
    private String result;
    private String image_url;

    public MoodAnalysisLinechart(String result, String image_url){
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
