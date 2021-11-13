package com.example.a110407_app.ui.profile;

import android.Manifest;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.a110407_app.Model.User;
import com.example.a110407_app.Model.UserDiary;
import com.example.a110407_app.R;
import com.example.a110407_app.RetrofitAPI.APIService;
import com.example.a110407_app.RetrofitAPI.RetrofitManager;
import com.example.a110407_app.ui.PasswordSetting;
import com.example.a110407_app.ui.SQLiteDBHelper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.lang.Math;
import java.util.jar.Pack200;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    //介面元件
    private ImageView profileImageView;
    private TextView userTrueNameTextView;
    private TextView userNickNameUnderImageTextView;
    private TextView userNickNameTextView;
    private TextView userEmailTextView;
    private TextView userBirthdayTextView;
    private TextView userGenderTextView;
    private TextView numberOfDiaryTextView;
    private ImageView userCurrentMood;
    //Token
    private String userToken;
    //API
    private APIService ourAPIService;
    //User相關的變數
    private String userEmail;
    private String userBirthday;
    private String userNickName;
    private String userTrueName;
    private String userGender;

    public ProfileFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Intent intent = getActivity().getIntent();
        userToken = intent.getStringExtra("userToken");
        System.out.println("Token： "+userToken);

        profileImageView = (ImageView) getView().findViewById(R.id.profileImage);
        profileImageView.setImageResource(R.drawable.ic_menu_camera);
//        userTrueNameTextView= getView().findViewById(R.id.profileEmail);
        userEmailTextView=getView().findViewById(R.id.profileEmail);
        userBirthdayTextView =getView().findViewById(R.id.profileBirhtday);
        userNickNameUnderImageTextView = (TextView) getView().findViewById(R.id.profileName);
        //NICKNAME
       // userNickNameTextView = (TextView) getView().findViewById(R.id.profileNickname);
//        userGenderTextView =getView().findViewById(R.id.pro)
////        numberOfDiaryTextView = getView().findViewById();
////        userCurrentMood;

        ourAPIService = RetrofitManager.getInstance().getAPI();
        Call<User> callUserData = ourAPIService.getUserData("bearer "+userToken);
        callUserData.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                String result=response.message();
                System.out.println(result);
                JsonObject userData =response.body().getUserAllDataInJson();
                System.out.println(userData.toString());
                userEmail=userData.get("username").toString();
                userNickName=userData.get("nickname").toString();
                userGender=userData.get("gender").toString();
                userBirthday=userData.get("birthday").toString();
                userTrueName=userData.get("truename").toString();

                System.out.println(userEmail);
                System.out.println(userNickName);
                System.out.println(userGender);
                System.out.println(userBirthday);
                System.out.println(userTrueName);

                userNickNameTextView.setText(userNickName);
                userNickNameUnderImageTextView.setText(userNickName);
                userEmailTextView.setText(userEmail);
                userBirthdayTextView.setText(userBirthday);
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                System.out.println("伺服器連線失敗");
                Log.d("HKT", "response: " + t.toString());
            }
        });
    }
}
