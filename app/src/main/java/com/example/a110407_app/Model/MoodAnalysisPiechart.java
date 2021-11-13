package com.example.a110407_app.Model;

public class MoodAnalysisPiechart {
    private String result;
    private String start;
    private String end;
    private String pie_image_url;

    public MoodAnalysisPiechart(String start, String end){
        this.start = start;
        this.end = end;
    }

    public String getResult(){
        return result;
    }

    public String getImage_url(){
        return pie_image_url;
    }
}
