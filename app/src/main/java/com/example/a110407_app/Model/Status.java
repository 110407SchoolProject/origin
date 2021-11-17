package com.example.a110407_app.Model;

import com.google.gson.JsonArray;

import org.json.JSONObject;

import java.util.ArrayList;

public class Status {
    private JsonArray status;

    public Status(JsonArray status) {
        this.status = status;
    }

    public JsonArray getStatus() {
        return status;
    }
}
