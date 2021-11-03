package com.example.a110407_app.Model;

public class MoodAnalysisScore {
    private String result;
    private int score;
    public MoodAnalysisScore(String result, int score){
        this.result = result;
        this.score = score;
    }

    public String getResult(){
        return result;
    }

    public int getScore(){
        return score;
    }
}
