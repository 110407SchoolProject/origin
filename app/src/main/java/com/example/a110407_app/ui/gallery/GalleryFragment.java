package com.example.a110407_app.ui.gallery;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.a110407_app.EditDiaryActivity;
import com.example.a110407_app.MainActivity;
import com.example.a110407_app.Model.UserDiary;
import com.example.a110407_app.Model.UserLogin;
import com.example.a110407_app.R;
import com.example.a110407_app.RetrofitAPI.APIService;
import com.example.a110407_app.RetrofitAPI.RetrofitManager;
import com.example.a110407_app.ShowDiaryActivity;
import com.example.a110407_app.ui.PasswordSetting;
import com.example.a110407_app.ui.SQLiteDBHelper;
import com.example.a110407_app.ui.home.HomeFragment;
import com.example.a110407_app.ui.login.RegisterActivity;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GalleryFragment extends Fragment {

    private String userToken;
    private ListView diaryListView;
    private GalleryViewModel galleryViewModel;
    APIService ourAPIService;
    ArrayList titleArrayList = new ArrayList();
    ArrayList diaryIdList = new ArrayList();
    ArrayList diaryDateList = new ArrayList();

    //開啟該篇日記
    public void openActivityShowDiary(String diaryId, String userToken){
        Intent intent = new Intent(getActivity(), ShowDiaryActivity.class);
        intent.putExtra("userToken",userToken);
        intent.putExtra("id",diaryId);
        startActivity(intent);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel = ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);

        galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                    textView.setText("test");
            }
        });

        Intent intent = getActivity().getIntent();
        userToken =intent.getStringExtra("userToken");
        System.out.println(userToken);

        ourAPIService = RetrofitManager.getInstance().getAPI();

        Call<UserDiary> callAllDiaryToList = ourAPIService.postUserAllDiary("bearer "+userToken);
        callAllDiaryToList.enqueue(new Callback<UserDiary>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<UserDiary> call, Response<UserDiary> response) {
                try {
                    JsonArray diaryAllList = response.body().getDiaryList();
                    for(int i=0; i<diaryAllList.size();i++){
                        JsonObject diaryJsonObject = (JsonObject) diaryAllList.get(i);
                        String diaryTitle = diaryJsonObject.get("title").toString();
                        diaryTitle= diaryTitle.substring(1,diaryTitle.length()-1);
                        titleArrayList.add(diaryTitle);
                    }

                    for(int i=0;i<diaryAllList.size();i++){
                        JsonObject diaryJsonObject = (JsonObject) diaryAllList.get(i);
                        String diaryId = diaryJsonObject.get("diaryid").toString();
                        System.out.println("DiaryID:"+diaryId);
                        diaryId=diaryId.substring(1,diaryId.length()-1);
                        diaryIdList.add(diaryId);
                    }

                    List<OffsetDateTime> odts = new ArrayList<>();

                    for(int i=0; i<diaryAllList.size();i++){
                        JsonObject diaryJsonObject = (JsonObject) diaryAllList.get(i);
                        String diaryDate = diaryJsonObject.get("last_modified").toString();
                        diaryDate= diaryDate.substring(1,diaryDate.length()-1);
                        OffsetDateTime odt = OffsetDateTime.parse( diaryDate ) ;
                        odts.add(odt);
                        diaryDateList.add(diaryDate);
                        System.out.println("diaryDate:"+diaryDate);
                    }

                    JSONObject idAndDateJson = new JSONObject();
                    JSONObject idAndTitleJson = new JSONObject();
                    for(int i = 0; i<diaryAllList.size();i++){
                        try {
                            idAndDateJson.put((String) diaryDateList.get(i), diaryIdList.get(i));
                            idAndTitleJson.put((String) diaryIdList.get(i), titleArrayList.get(i));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    titleArrayList.clear();
                    diaryIdList.clear();
                    Collections.sort(odts);

                    for(int i=0;i<diaryAllList.size();i++){
                        try {
                            diaryIdList.add(idAndDateJson.get(String.valueOf(odts.get(diaryAllList.size()-i-1))));
                            titleArrayList.add(idAndTitleJson.get((String) diaryIdList.get(i)));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    //日記列表樣式，屆時只要把上面的titleArrayList放入畫面就可以
                    diaryListView = (ListView)root.findViewById(R.id.diaryListView);
                    ArrayAdapter adapter = new ArrayAdapter<>(getActivity(),R.layout.list_text_setting,titleArrayList);
                    diaryListView.setAdapter(adapter);

                    diaryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            Toast.makeText(getActivity(),"開啟日記"+(id+1), Toast.LENGTH_LONG).show();
                            System.out.println("position"+position);
                            //透過position來抓日記ID，接著將日記ID傳到下個頁面後，於下一頁call API。
                            String diaryIdToCall = diaryIdList.get(position).toString();
                            System.out.println("要傳的ID:"+diaryIdToCall);
                            System.out.println("要傳的Token:"+userToken);
                            openActivityShowDiary(diaryIdToCall,userToken);
                        }
                    });
                }catch (Exception e){
                    System.out.println(e);
                    System.out.println("存取日記列表失敗");
                }
            }
            @Override
            public void onFailure(Call<UserDiary> call, Throwable t) {
                System.out.println("伺服器連線失敗");
                Log.d("HKT", "response: " + t.toString());
            }
        });
        return root;
    }
}
