package com.example.a110407_app.Model;

public class MoodAnalysisCountDiarys {
    private String result;
    private int count_diarys;
    public MoodAnalysisCountDiarys(String result, int count_diarys){
        this.result = result;
        this.count_diarys = count_diarys;
    }

    public String getResult(){
        return result;
    }

    public int getCount_diarys(){
        return count_diarys;
    }
}
