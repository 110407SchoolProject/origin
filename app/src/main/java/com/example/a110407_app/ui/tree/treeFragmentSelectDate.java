package com.example.a110407_app.ui.tree;

import android.app.DatePickerDialog;
import android.app.Dialog;
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

import com.example.a110407_app.R;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

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
    private ImageView myStartDatePicker;
    private ImageView myEndDatePicker;

    static final int START_DATE_DIALOG_ID = 0;
    static final int END_DATE_DIALOG_ID = 1;

    private int mYear;
    private int mMonth;
    private int mDay;
    private EditText userStartDateEditText;
    private EditText userEndDateEditText;
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

        myStartDatePicker =getActivity().findViewById(R.id.myDatePickerInTree);
        myEndDatePicker =getActivity().findViewById(R.id.myDatePicker2InTree);
        userStartDateEditText=getActivity().findViewById(R.id.userStartDateInTree);
        userEndDateEditText=getActivity().findViewById(R.id.userEndDateInTree);

        myStartDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("You Click the StartDatePicker");

                getActivity().showDialog(START_DATE_DIALOG_ID);
            }
        });

        myEndDatePicker.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                System.out.println("You Click the EndDatePicker");
                getActivity().showDialog(END_DATE_DIALOG_ID);
            }
        });

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        //display the current date
        updateEndDateDisplay();


        //都選好日期送出的部分
        btnTreeGenerate=getActivity().findViewById(R.id.btnTreeGenerateInTree);
        btnTreeGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Hello 你按下了產生心情樹");
                //透過當Bundle當傳遞參數的容器
                Bundle bundle = new Bundle();
                bundle.putString("startDate","1999-12-07");
                bundle.putString("endDate","2021-12-07");
                //傳入
                NavHostFragment.findNavController(treeFragmentSelectDate.this)
                        .navigate(R.id.nav_treeGenerated,bundle);
            }
        });
    }

    private void updateStartDateDisplay() {
        this.userStartDateEditText.setText(
                new StringBuilder()
                        // Month is 0 based so add 1
                        .append(mYear).append("-")
                        .append(mMonth + 1 ).append("-")
                        .append(mDay).append(" "));
    }

    private void updateEndDateDisplay() {
        this.userEndDateEditText.setText(
                new StringBuilder()
                        // Month is 0 based so add 1
                        .append(mYear).append("-")
                        .append(mMonth + 1 ).append("-")
                        .append(mDay).append(" "));
    }

    private DatePickerDialog.OnDateSetListener DatePickerListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    updateStartDateDisplay();
                }
            };

    protected Dialog onCreateStartDateDialog(int id) {
        switch (id) {
            case START_DATE_DIALOG_ID:
                return new DatePickerDialog(getActivity(),
                        DatePickerListener,
                        mYear, mMonth, mDay);
        }
        return null;
    }

    protected Dialog onCreateEndDateDialog(int id) {
        switch (id) {
            case END_DATE_DIALOG_ID:
                return new DatePickerDialog(getActivity(),
                        DatePickerListener,
                        mYear, mMonth, mDay);
        }
        return null;
    }
}