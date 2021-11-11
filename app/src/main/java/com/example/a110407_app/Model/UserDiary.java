package com.example.a110407_app.Model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

public class UserDiary {
    private JsonArray diary_list;
    private String title;
    private String content;
    private String tag;
    private String tag2;
    private String tag3;
    private int moodscore;
    private String result;
    private String message;

    public JsonArray getDiaryList() {
        return diary_list;
    }

    public UserDiary(String title, String content, String tag, String tag2, String tag3, int moodscore) {

        this.title = title;
        this.content = content;
        this.tag = tag;
        this.tag2 = tag2;
        this.tag3 = tag3;
        this.moodscore = moodscore;
    }
    public String getMessage() {
        return message;
    }

    public String getResult() {
        return result;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getTag() {
        return tag;
    }

    public String getTag2() {
        return tag2;
    }

    public String getTag3() {
        return tag3;
    }

    public int getMoodscore() {
        return moodscore;
    }
}
