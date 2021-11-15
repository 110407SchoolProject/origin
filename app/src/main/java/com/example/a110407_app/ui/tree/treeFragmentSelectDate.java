package com.example.a110407_app.ui.tree;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.a110407_app.Model.MoodTree;
import com.example.a110407_app.R;
import com.example.a110407_app.RetrofitAPI.APIService;
import com.example.a110407_app.RetrofitAPI.RetrofitManager;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link treeFragmentSelectDate#newInstance} factory method to
 * create an instance of this fragment.
 */
public class treeFragmentSelectDate extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private Button btnTreeGenerate;
    private ImageView myStartDatePickerinTree;
    private ImageView myEndDatePickerinTree;

    static final int START_DATE_DIALOG_ID = 0;
    static final int END_DATE_DIALOG_ID = 1;

    private int mYear;
    private int mMonth;
    private int mDay;
    private EditText startDateTree;
    private EditText endDateTree;
//    private ImageView treeleaf;
    APIService ourAPIService;


    public treeFragmentSelectDate() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment treeFragmentSelectDate.
     */
    // TODO: Rename and change types and number of parameters
    public static treeFragmentSelectDate newInstance(String param1, String param2) {
        treeFragmentSelectDate fragment = new treeFragmentSelectDate();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tree_select_date, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        System.out.println("你正在treeFragment ");
        myStartDatePickerinTree =getActivity().findViewById(R.id.myDatePicker1InTree);
        myEndDatePickerinTree =getActivity().findViewById(R.id.myDatePicker2InTree);
        startDateTree=getActivity().findViewById(R.id.userStartDateInTree);
        endDateTree=getActivity().findViewById(R.id.userEndDateInTree);
        ourAPIService = RetrofitManager.getInstance().getAPI();
        Intent intent = getActivity().getIntent();
        String userToken = intent.getStringExtra("userToken");

        //都選好日期送出的部分
        btnTreeGenerate=getActivity().findViewById(R.id.btnTreeGenerateInTree);
        btnTreeGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Hello 你按下了產生心情樹");
                //透過當Bundle當傳遞參數的容器
                String start = startDateTree.getText().toString();
                String end = endDateTree.getText().toString();
                System.out.println(start);
                System.out.println(end);
                MoodTree moodTree = new MoodTree(start,end);
                Bundle bundletree = new Bundle();
                Call<MoodTree> callMoodTree = ourAPIService.postMoodTree("bearer " + userToken, moodTree);
                callMoodTree.enqueue(new Callback<MoodTree>() {
                    @Override
                    public void onResponse(Call<MoodTree> call, Response<MoodTree> response) {
                        try {
                            String result = response.message();
                            String tree_image_url = response.body().getImage_url();
                            System.out.println(result);
                            System.out.println(tree_image_url);
                            String test = "http://server.gywang.io:8084/" + tree_image_url;
                            System.out.println("TEST:" + test);
                            bundletree.putString("tree" , tree_image_url);
                            NavHostFragment.findNavController(treeFragmentSelectDate.this)
                                    .navigate(R.id.nav_treeGenerated, bundletree);
                        }catch (Exception e){
                            System.out.println(e);
                            System.out.println("回應文字雲失敗");
                        }
                    }

                    @Override
                    public void onFailure(Call<MoodTree> call, Throwable t) {

                    }
                });
            }

        });

        myStartDatePickerinTree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("起始日期");
                datePickerStartDate(getView());
            }
        });

        myEndDatePickerinTree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("結束日期");
                datePickerEndDate(getView());
            }
        });

    }

    //呼叫StartDate Calendar
    public void datePickerStartDate(View v){
        Calendar calendarStart = Calendar.getInstance();
        int year = calendarStart.get(Calendar.YEAR);
        int month = calendarStart.get(Calendar.MONTH);
        int day = calendarStart.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                String datetime = String.valueOf(year) + "-" + String.valueOf(month +1) + "-" + String.valueOf(day);
                startDateTree.setText(datetime);


            }
        },year, month,day).show();
    }

    //呼叫EndDate Calendar
    public void datePickerEndDate(View v){
        Calendar calendarEnd = Calendar.getInstance();
        int year = calendarEnd.get(Calendar.YEAR);
        int month = calendarEnd.get(Calendar.MONTH);
        int day = calendarEnd.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                String datetime = String.valueOf(year) + "-" + String.valueOf(month+1) + "-" + String.valueOf(day);
                endDateTree.setText(datetime);
            }
        },year, month,day).show();
    }
}