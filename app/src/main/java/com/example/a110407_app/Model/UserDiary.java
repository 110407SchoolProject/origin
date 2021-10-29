package com.example.a110407_app.Model;

public class UserDiary {
    private String title;
    private String content;
    private String tag;
    private String tag2;
    private String tag3;
    private int moodscore;
    private String result;
    private String message;





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
