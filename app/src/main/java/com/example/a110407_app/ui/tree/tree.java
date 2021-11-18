package com.example.a110407_app.ui.tree;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a110407_app.Model.MoodAnalysisLinechart;
import com.example.a110407_app.Model.MoodTree;
import com.example.a110407_app.R;
import com.example.a110407_app.RetrofitAPI.APIService;
import com.example.a110407_app.RetrofitAPI.RetrofitManager;
import com.example.a110407_app.ui.FirstFragment;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class tree extends Fragment {

    private TreeViewModel mViewModel;
    private OnButtonClick onButtonClick;
    private ImageView tree;
    private TextView dateTitleTextView;

    public static tree newInstance() {
        return new tree();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tree_fragment2, container, false);
    }

    public OnButtonClick getOnButtonClick() {
        return onButtonClick;
    }
    public void setOnButtonClick(OnButtonClick onButtonClick) {
        this.onButtonClick = onButtonClick;
    }
    public interface OnButtonClick{
        public void onClick(View view);
    }
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        mViewModel = new ViewModelProvider(this).get(TreeViewModel.class);
//        // TODO: Use the ViewModel
//    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        System.out.println("你成功跳轉了喔!");

        dateTitleTextView = getActivity().findViewById(R.id.text_date);

        String beginDateString = getArguments().getString("start");
        String endDateString = getArguments().getString("end");
        System.out.println(beginDateString);
        System.out.println(endDateString);

        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate = null;
        try {
            beginDate = sdf.parse(beginDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date overDate = null;
        try {
            overDate = sdf.parse(endDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int startYear=(beginDate.getYear()-100+2000);
        int startMonth=beginDate.getMonth()+1;
        int startDate = beginDate.getDate();
        int endYear=(overDate.getYear()-100+2000);
        int endMonth=overDate.getMonth()+1;
        int endDay = overDate.getDate();

        String titleTextDate=startYear+"年"+startMonth+"月"+startDate+"日"+" - "+endYear+"年"+endMonth+"月"+endDay+"日";
        dateTitleTextView.setText(titleTextDate);

        tree = getActivity().findViewById(R.id.tree);
        String tree_image_url = getArguments().getString("tree");
        System.out.println("樹木url: " + tree_image_url);
        Picasso.get().load("http://server.gywang.io:8084/" + tree_image_url).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).fit().centerCrop().into(tree);
    }
}