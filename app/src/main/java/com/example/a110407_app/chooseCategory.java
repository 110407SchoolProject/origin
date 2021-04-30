package com.example.a110407_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;


public class chooseCategory extends AppCompatActivity {
    private Spinner spinner;
    private ImageView imageView;
    private TextView textView;
    String[] text =new String[]{"蝴蝶","跑車","橘子","西瓜"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_category);
        ArrayAdapter<String> adaptertext=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,text);
        adaptertext.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adaptertext);

        spinner = (Spinner) findViewById(R.id.spinnerCategory);

        imageView.setImageResource(R.drawable.logo);
        textView.setText("ACDDDDD");
    }
}