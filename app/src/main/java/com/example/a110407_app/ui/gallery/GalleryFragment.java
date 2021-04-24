package com.example.a110407_app.ui.gallery;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
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

import com.example.a110407_app.EditDiaryActivity;
import com.example.a110407_app.R;
import com.example.a110407_app.ShowDiaryActivity;
import com.example.a110407_app.ui.SQLiteDBHelper;
import com.example.a110407_app.ui.login.RegisterActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GalleryFragment extends Fragment {

    private ListView diaryListView;
    private GalleryViewModel galleryViewModel;

    SQLiteDBHelper          mHelper;
    private final String DB_NAME = "MyDairy.db";
    private String TABLE_NAME = "MyDairy";
    private final int DB_VERSION = 3;
    private ArrayList<HashMap<String, String>> diaryTitleList;

    //開啟該篇日記
    public void openActivityShowDiary(String diaryId){
        Intent intent = new Intent(getActivity(), ShowDiaryActivity.class);

        intent.putExtra("id",diaryId);
        startActivity(intent);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);

        galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                Integer countDiaryNumber=0;
                for(HashMap<String,String> data:mHelper.showAll()){
                    countDiaryNumber+=1;
                }
                //沒有日記的話就顯示，目前空空如也
                if(countDiaryNumber==0){
                    textView.setText(s);
                }

            }
        });
        //資料庫
        mHelper = new SQLiteDBHelper(getActivity(),DB_NAME,null,DB_VERSION,TABLE_NAME);

        //抓取資料庫的日記筆數
        Integer countDiaryNumber=0;
        for(HashMap<String,String> data:mHelper.showAll()){
            countDiaryNumber+=1;
        }
        System.out.println("目前有這個數筆日記："+countDiaryNumber);
        //日記標題清單
        //標題
        String title="";

        //抓取日記標題
        final ArrayList titleArrayList = new ArrayList();
        final ArrayList idArrayList = new ArrayList();
        for(int i = 1;i<=256;i++){
            String id = Integer.toString(i);
            String diaryId = "";
            diaryTitleList= new ArrayList<>();
            diaryTitleList =mHelper.searchById(id);

            if(diaryTitleList.size()==0){
                continue;
            }else{
                for(HashMap<String,String> data:diaryTitleList){
                    title=data.get("Title");
                    diaryId=data.get("id");
                    titleArrayList.add(title);
                    idArrayList.add(diaryId);
                }
            }
        }
        System.out.println(idArrayList);
        //抓ListView ，並把剛抓到的日記顯示出來
        diaryListView = (ListView)root.findViewById(R.id.diaryListView);
        ArrayAdapter adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,titleArrayList);
        diaryListView.setAdapter(adapter);


        //開啟日記
        diaryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(),"開啟日記"+(id+1),

                Toast.LENGTH_LONG).show();
                //點入看日記的頁面
                int idByInt =(int)id;

                String title = (String) titleArrayList.get(idByInt);
                String diaryId =(String)idArrayList.get(idByInt);
                openActivityShowDiary(diaryId);
            }
        });
        return root;
    }
}
