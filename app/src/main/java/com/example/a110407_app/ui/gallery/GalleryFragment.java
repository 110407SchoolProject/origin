package com.example.a110407_app.ui.gallery;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.a110407_app.EditDiaryActivity;
import com.example.a110407_app.MainActivity;
import com.example.a110407_app.R;
import com.example.a110407_app.ShowDiaryActivity;
import com.example.a110407_app.ui.PasswordSetting;
import com.example.a110407_app.ui.SQLiteDBHelper;
import com.example.a110407_app.ui.home.HomeFragment;
import com.example.a110407_app.ui.login.RegisterActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GalleryFragment extends Fragment {

    private ListView diaryListView;
    private GalleryViewModel galleryViewModel;


    SQLiteDBHelper mHelper;
    private final String DB_NAME = "MyDairy.db";
    private String TABLE_NAME = "MyDairy";
    private final int DB_VERSION = 6;
    private ArrayList<HashMap<String, String>> diaryTitleList;

    private EditText getPassword;
    private String strPassword, p, lock;
    SQLiteDBHelper TableUserPassword;
    private String PASSWORD_TABLE_NAME = "UserPassword";


    //開啟該篇日記
    public void openActivityShowDiary(String diaryId){
        Intent intent = new Intent(getActivity(), ShowDiaryActivity.class);

        intent.putExtra("id",diaryId);
        startActivity(intent);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //資料表初始化
        TableUserPassword = new SQLiteDBHelper(getActivity(),DB_NAME,null,DB_VERSION,PASSWORD_TABLE_NAME);
        TableUserPassword.getWritableDatabase();
        mHelper = new SQLiteDBHelper(getActivity(),DB_NAME,null,DB_VERSION,TABLE_NAME);
        mHelper.getWritableDatabase();
        // 初始化
        galleryViewModel = ViewModelProviders.of(this).get(GalleryViewModel.class);
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
        //抓是否設密碼欄位
        TableUserPassword.showLock();
        for(HashMap<String,String> data:TableUserPassword.showLock()){
            lock = data.get("IfSetLock");
        }
        if(lock.equals("1")){//密碼鎖已開啟
            // 必須先輸入密碼
            AlertDialog.Builder GalleryPasswordDialog = new AlertDialog.Builder(getActivity());
            View view = getLayoutInflater().inflate(R.layout.openclosepassword, null);//共用密碼設置開啟與關閉的Layout
            GalleryPasswordDialog.setView(view);
            GalleryPasswordDialog.setPositiveButton("確認", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getPassword = view.findViewById(R.id.inputOpcnClosePassword);
                    strPassword = getPassword.getText().toString();
                    for(HashMap<String,String> data:TableUserPassword.showAllPassword()){
                        p = data.get("Password");
                    }if(strPassword.equals(p)){

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
                                    if(title==null){
                                        title="無標題";
                                    }
                                    titleArrayList.add(title);
                                    idArrayList.add(diaryId);
                                }
                            }
                        }

                        System.out.println("所有的日記："+titleArrayList);
                        //抓ListView ，並把剛抓到的日記顯示出來
                        diaryListView = (ListView)root.findViewById(R.id.diaryListView);
                        ArrayAdapter adapter = new ArrayAdapter<>(getActivity(),R.layout.list_text_setting,titleArrayList);
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

                    }else{
                        Toast.makeText(getActivity(),"密碼錯誤，將回到主畫面",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                    }

                }
            });
            GalleryPasswordDialog.show();

        }else{//密碼鎖已解開
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
                        if(title==null){
                            title="無標題";
                        }
                        titleArrayList.add(title);
                        idArrayList.add(diaryId);
                    }
                }
            }

            System.out.println("所有的日記："+titleArrayList);
            //抓ListView ，並把剛抓到的日記顯示出來
            diaryListView = (ListView)root.findViewById(R.id.diaryListView);
            ArrayAdapter adapter = new ArrayAdapter<>(getActivity(),R.layout.list_text_setting,titleArrayList);
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
        }
        return root;
    }
}
