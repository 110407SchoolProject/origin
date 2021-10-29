package com.example.a110407_app.Model;

import org.json.JSONObject;

import java.util.ArrayList;

public class MoodPredict {

    private String content;

    private ArrayList<String> score;

    public MoodPredict(String content) {
        this.content = content;
    }

    public ArrayList<String> getScore() {
        return score;
    }

    public String getContent() {
        return content;
    }
}
