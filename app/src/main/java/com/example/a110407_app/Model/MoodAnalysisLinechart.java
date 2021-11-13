package com.example.a110407_app.Model;

public class MoodAnalysisLinechart {
    private String result;
    private String start;
    private String end;
    private String line_image_url;

    public MoodAnalysisLinechart(String start, String end){
        this.start = start;
        this.end = end;
    }

    public String getResult(){
        return result;
    }

    public String getImage_url(){
        return line_image_url;
    }
}
