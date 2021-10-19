package com.example.a110407_app.Model;

import com.google.gson.JsonObject;

public class MoodTalk {
    String result;
    JsonObject sentence;

    public MoodTalk(String result, JsonObject Sentence) {
        this.result = result;
        this.sentence = sentence;
    }

    public JsonObject getSentence() {
        return sentence;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
