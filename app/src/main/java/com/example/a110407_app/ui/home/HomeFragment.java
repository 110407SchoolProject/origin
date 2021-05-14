package com.example.a110407_app.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private TextView dateTimeText;

    private ListView diaryListView;
    SQLiteDBHelper mHelper;
    private final String DB_NAME = "MyDairy.db";
    private String TABLE_NAME = "MyDairy";
    private final int DB_VERSION = 13;
    private ArrayList<HashMap<String, String>> diaryTitleList;


    public void openActivityShowDiary(String diaryId){
        Intent intent = new Intent(getActivity(), ShowDiaryActivity.class);

        intent.putExtra("id",diaryId);
        startActivity(intent);
    }

    public void openActivityEditDiary(){
        Intent intent = new Intent(getActivity(), EditDiaryActivity.class);
        startActivity(intent);
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);

        mHelper = new SQLiteDBHelper(getActivity(),DB_NAME,null,DB_VERSION,TABLE_NAME);

        String title="";

        //抓取日記標題
        final ArrayList titleArrayList = new ArrayList();
        final ArrayList idArrayList = new ArrayList();
        final ArrayList newestTitleArrayList = new ArrayList();
        final ArrayList newestIdArrayList = new ArrayList();
        /*
        for(int i = 0;i<=256;i++){
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
                    if(title==null){
                        title="無標題";
                    }
                    titleArrayList.add(title);
                    idArrayList.add(diaryId);
                }
            }
        }

         */


        System.out.println(newestIdArrayList.size());
        if(titleArrayList.size()>=2){
            for(int i = 1 ; i<3;i++){
                newestTitleArrayList.add(titleArrayList.get(titleArrayList.size()-i));
                newestIdArrayList.add(idArrayList.get((titleArrayList.size()-i)));

                diaryListView = (ListView)root.findViewById(R.id.diaryListViewInHome);
                ArrayAdapter adapter = new ArrayAdapter<>(getActivity(),R.layout.list_text_setting,newestTitleArrayList);
                diaryListView.setAdapter(adapter);

                diaryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Toast.makeText(getActivity(),"開啟日記"+(id+1),

                                Toast.LENGTH_LONG).show();
                        //點入看日記的頁面
                        int idByInt =(int)id;

                        String diaryId =(String)newestIdArrayList.get(idByInt);
                        openActivityShowDiary(diaryId);

                    }
                });
            }
        }else if(titleArrayList.size()==1){

            newestTitleArrayList.add(titleArrayList.get(titleArrayList.size()-1));
            newestIdArrayList.add(idArrayList.get((titleArrayList.size()-1)));
            diaryListView = (ListView)root.findViewById(R.id.diaryListViewInHome);
            ArrayAdapter adapter = new ArrayAdapter<>(getActivity(),R.layout.list_text_setting,newestTitleArrayList);
            diaryListView.setAdapter(adapter);

            diaryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(getActivity(),"開啟日記"+(id+1),

                            Toast.LENGTH_LONG).show();
                    //點入看日記的頁面
                    int idByInt =(int)id;

                    String diaryId =(String)newestIdArrayList.get(idByInt);
                    openActivityShowDiary(diaryId);

                }
            });
        }else{
            newestTitleArrayList.add("目前沒有日記喔！點擊我來紀錄新的日記吧！");
            diaryListView = (ListView)root.findViewById(R.id.diaryListViewInHome);
            ArrayAdapter adapter = new ArrayAdapter<>(getActivity(),R.layout.list_text_setting,newestTitleArrayList);
            diaryListView.setAdapter(adapter);

            diaryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    openActivityEditDiary();
                }
            });
        }









        dateTimeText=(TextView)root.findViewById(R.id.DateTextView);


        Integer month = 0;
        Integer date = 0;
        Integer Year =0;
        Integer day=0;
        Date mDate = new Date();
        Year = mDate.getYear()-100+2000;
        month = mDate.getMonth() + 1;
        date = mDate.getDate();
        day= mDate.getDay();
        System.out.println(day);
        String weekday[]={"日","一","二","三","四","五","六",};
        final String stringDate = date.toString();
        final String stringMonth = month.toString();
        final String stringYear = Year.toString();

        dateTimeText.setText(Year+"年 "+month+"月"+date+"日 "+"星期"+weekday[day]);





        return root;
    }
}
