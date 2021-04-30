package com.example.a110407_app.ui.editDairy;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import com.example.a110407_app.R;

public class EditDairyFragment extends Fragment {
    private CalendarView calendarView;

    private EditDairyViewModel mViewModel;

    public static EditDairyFragment newInstance() {
        return new EditDairyFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.edit_dairy_fragment2, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(EditDairyViewModel.class);
        // TODO: Use the ViewModel
    }



}