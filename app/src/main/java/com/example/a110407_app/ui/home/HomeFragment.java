package com.example.a110407_app.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.a110407_app.R;

import java.util.Date;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private TextView dateTimeText;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);


        dateTimeText=(TextView)root.findViewById(R.id.DateTextView);


        Integer month = 0;
        Integer date = 0;
        Integer Year =0;
        Integer day=0;
        Date mDate = new Date();
        Year = mDate.getYear()-100+2000;
        month = mDate.getMonth() + 1;
        date = mDate.getDate()+1;
        day= mDate.getDay()+1;
        String weekday[]={"日","一","二","三","四","五","六"};
        final String stringDate = date.toString();
        final String stringMonth = month.toString();
        final String stringYear = Year.toString();

        dateTimeText.setText(Year+"年 "+month+"月"+date+"日 "+"星期"+weekday[day]);



        return root;
    }
}
