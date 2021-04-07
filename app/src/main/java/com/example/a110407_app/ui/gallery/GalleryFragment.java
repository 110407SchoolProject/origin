package com.example.a110407_app.ui.gallery;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.a110407_app.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GalleryFragment extends Fragment {

    private ListView diaryListView;
    private GalleryViewModel galleryViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);

        galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        String[] listFromResource = getResources().getStringArray(R.array.diary);
        diaryListView = (ListView)root.findViewById(R.id.diaryListView);
        ArrayAdapter adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1);

        //想法：應該透過資料庫來新增日記list的項目

        adapter.addAll(listFromResource);
        diaryListView.setAdapter(adapter);

        diaryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(),"開啟日記"+(id+1),
                        Toast.LENGTH_LONG).show();
            }
        });






        return root;
    }
}
