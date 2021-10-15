package com.example.a110407_app.Model;

public class UserLogin {
    private String username;
    private String password;

    private String token;
    private String result;
    private String message;

    public String getToken() {
        return token;
    }


    public String getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }

    public UserLogin(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
