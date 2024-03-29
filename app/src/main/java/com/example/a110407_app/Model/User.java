package com.example.a110407_app.Model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class User {
    private JsonObject user;
    private String username;
    private String password;



    private String truename;
    private String nickname;
    private String gender;
    private String birthday;

    private String message;
    private String result;

    public User(String username, String password, String truename, String nickname, String gender, String birthday) {
        this.username = username;
        this.password = password;
        this.truename = truename;
        this.nickname = nickname;
        this.gender = gender;
        this.birthday = birthday;
    }
    public JsonObject getUserAllDataInJson() {
        return user;
    }

    public User(String s, String s1, String 翁罐頭, String pony) {
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getTruename() {
        return truename;
    }

    public String getNickname() {
        return nickname;
    }

    public String getGender() {
        return gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getResult(){return result; }

    public String getMessage(){return message;}
}
