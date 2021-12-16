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
import androidx.fragment.app.FragmentManager;

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

import com.example.a110407_app.Model.ModelPasswordChange;
import com.example.a110407_app.Model.ProfileDiaryNumber;
import com.example.a110407_app.Model.ProfileScore;
import com.example.a110407_app.Model.Status;
import com.example.a110407_app.Model.User;
import com.example.a110407_app.Model.UserDiary;
import com.example.a110407_app.Model.UserNickName;
import com.example.a110407_app.R;
import com.example.a110407_app.RetrofitAPI.APIService;
import com.example.a110407_app.RetrofitAPI.RetrofitManager;
import com.example.a110407_app.ShowDiaryActivity;
import com.example.a110407_app.activitychooseprofile;
import com.example.a110407_app.ui.PasswordSetting;
import com.example.a110407_app.ui.SQLiteDBHelper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

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
    private ProfileFragment profileFragment;
    private ImageView profileImageView;
    private TextView userTrueNameTextView;
    private TextView userNickNameUnderImageTextView;
    private TextView userNickNameTextView;
    private TextView userEmailTextView;
    private TextView userBirthdayTextView;
    private TextView userGenderTextView;
    private TextView numberOfDiaryTextView;
    private TextView currentDiaryNumber;
    private ImageView userCurrentMood;
    private ImageView btnEditProfileName;
    private ImageView recentMoodImage;
    private Button btnChangePassword;
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
    private String userCharacterNumber;


    public  void  exit(){
        getActivity().onBackPressed();

    }
    public ProfileFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    public void openActivityChooseProfile(String userToken){
        Intent intent = new Intent(getActivity(), activitychooseprofile.class);
        intent.putExtra("userToken",userToken);
        startActivity(intent);
        exit();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Intent intent = getActivity().getIntent();
        userToken = intent.getStringExtra("userToken");
        System.out.println("Token： "+userToken);

        profileImageView = (ImageView) getView().findViewById(R.id.profileImage);
        btnChangePassword=getView().findViewById(R.id.btnPasswordSetting);
//        userTrueNameTextView= getView().findViewById(R.id.profileEmail);
        userEmailTextView=getView().findViewById(R.id.profileEmail);
        userBirthdayTextView =getView().findViewById(R.id.profileBirhtday);
        userNickNameUnderImageTextView = (TextView) getView().findViewById(R.id.profileName);

        btnEditProfileName = getView().findViewById(R.id.editProfileName);
        //NICKNAME
        userNickNameTextView = (TextView) getView().findViewById(R.id.profileName);
        currentDiaryNumber = (TextView) getView().findViewById(R.id.currentDiaryNumbers);
        recentMoodImage = (ImageView) getView().findViewById(R.id.recentMoodImageView);
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityChooseProfile(userToken);
            }
        });
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

                userGender=userGender.substring(1,userGender.length()-1);
                userNickName=userNickName.substring(1,userNickName.length()-1);
                userEmail=userEmail.substring(1,userEmail.length()-1);
                userBirthday=userBirthday.substring(1,userBirthday.length()-1);
                System.out.println(userGender);
                System.out.println(userNickName);
                System.out.println(userEmail);
                System.out.println(userBirthday);

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
        Call<Status> callUserCharacter = ourAPIService.getUserStatus("bearer "+userToken);
        callUserCharacter.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                JsonArray characterNumberJsonArray = response.body().getStatus();
                if(characterNumberJsonArray.size()!=0){
                    String characterNumberJsonString =characterNumberJsonArray.get(0).toString();
                    try {
                        JSONObject characterNumberJsonObject =new JSONObject(characterNumberJsonString);
                        userCharacterNumber=characterNumberJsonObject.get("status").toString();
                        System.out.println("角色編號:"+userCharacterNumber);
                        if(userCharacterNumber.equals("1")){
                            profileImageView.setImageResource(R.drawable.boy1);
                        }else if(userCharacterNumber.equals("2")){
                            profileImageView.setImageResource(R.drawable.boy2);
                        }else if(userCharacterNumber.equals("3")){
                            profileImageView.setImageResource(R.drawable.boy3);
                        }else if(userCharacterNumber.equals("4")){
                            profileImageView.setImageResource(R.drawable.girl1);
                        }else if(userCharacterNumber.equals("5")){
                            profileImageView.setImageResource(R.drawable.girl2);
                        }else if(userCharacterNumber.equals("6")){
                            profileImageView.setImageResource(R.drawable.girl3);
                        }else{
                            profileImageView.setImageResource(R.drawable.addprofilepict);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        System.out.println("換腳色失敗");
                    }
                }else{
                    JsonArray characterNumberJson =new JsonArray();
                    int roleNumber=0;
                    characterNumberJson.add(roleNumber);
                    profileImageView.setImageResource(R.drawable.addprofilepict);
                    System.out.println(characterNumberJson.toString());
                    Status status =new Status(characterNumberJson);
                    Call<Status> callPostUserCharacter = ourAPIService.postUserStatus("bearer "+userToken,status);
                    callPostUserCharacter.enqueue(new Callback<Status>() {
                        @Override
                        public void onResponse(Call<Status> call, Response<Status> response) {
                            String result=response.message();
                            System.out.println("沒選擇角色 "+result);
                        }
                        @Override
                        public void onFailure(Call<Status> call, Throwable t) {
                            System.out.println("伺服器連線失敗");
                            Log.d("HKT", "response: " + t.toString());
                        }
                    });
                }
            }
            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                System.out.println("伺服器連線失敗");
                Log.d("HKT", "response: " + t.toString());
            }
        });

        //取得日記總篇數
        Call<ProfileDiaryNumber> callProfileDiaryNumber = ourAPIService.getProfileDiaryNumber("bearer "+userToken);
        callProfileDiaryNumber.enqueue(new Callback<ProfileDiaryNumber>() {
            @Override
            public void onResponse(Call<ProfileDiaryNumber> call, Response<ProfileDiaryNumber> response) {
                String result = response.message();
                int diary_number = response.body().getDiary_number();
                //System.out.println("印出來總篇數" + String.valueOf(diary_number));
                currentDiaryNumber.setText(String.valueOf(diary_number));
            }

            @Override
            public void onFailure(Call<ProfileDiaryNumber> call, Throwable t) {
                System.out.println("伺服器連線失敗");
                Log.d("HKT", "response: " + t.toString());
            }
        });

        //取得最新兩篇日記加權平均分數
        Call<ProfileScore> callProfileScore = ourAPIService.putProfileScore("bearer "+userToken);
        callProfileScore.enqueue(new Callback<ProfileScore>() {
            @Override
            public void onResponse(Call<ProfileScore> call, Response<ProfileScore> response) {
                String result = response.message();
                if(result=="OK"){
                    float score = response.body().getScore();
                    System.out.println("分數為: " + String.valueOf(score));

                    if(score <= 1.0) {
                        recentMoodImage.setImageResource(R.drawable.crying);
                    }else if ( score <=2.0 && score > 1.0){
                        recentMoodImage.setImageResource(R.drawable.sad);
                    }else if (score <=3.0 && score > 2.0){
                        recentMoodImage.setImageResource(R.drawable.normal);
                    }else if (score <=4.0 && score > 3.0){
                        recentMoodImage.setImageResource(R.drawable.smiling);
                    }else if(score >= 5.0 && score > 4.0 ){
                        recentMoodImage.setImageResource(R.drawable.exciting);
                    }else {
                        System.out.println("分數計算有誤");
                    }
                }

            }

            @Override
            public void onFailure(Call<ProfileScore> call, Throwable t) {
                System.out.println("伺服器連線失敗");
                Log.d("HKT", "response: " + t.toString());
            }
        });


        btnEditProfileName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText editTextNickName =new EditText(getActivity());
                AlertDialog.Builder dialogEditTextName = new AlertDialog.Builder(getActivity());
                dialogEditTextName.setTitle("輸入新暱稱");
                dialogEditTextName.setView(editTextNickName);
                dialogEditTextName.setCancelable(true);
                dialogEditTextName.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(),"Setting",Toast.LENGTH_LONG);
                        String userNewNickName = editTextNickName.getText().toString();
                        System.out.println(userNewNickName);
                        UserNickName userNickName =new UserNickName(userNewNickName);
                        Call<UserNickName> callPutUser =ourAPIService.putUserNickname("bearer "+userToken,userNickName);
                        callPutUser.enqueue(new Callback<UserNickName>() {
                            @Override
                            public void onResponse(Call<UserNickName> call, Response<UserNickName> response) {
                                String result=response.message();
                                System.out.println(result);
                                if(result.equals("OK")){
                                    userNickNameTextView.setText(userNewNickName);
                                }else{
                                    Toast.makeText(getActivity().getApplicationContext(), "更改暱稱失敗", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<UserNickName> call, Throwable t) {
                                System.out.println("伺服器連線失敗");
                                Log.d("HKT", "response: " + t.toString());
                            }
                        });
                    }
                });
                dialogEditTextName.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialogEditTextName.show();
            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogInputOldPassword= new AlertDialog.Builder(getActivity());
                View viewInputOldPassword = getActivity().getLayoutInflater().inflate(R.layout.inputoldpassword, null);
                dialogInputOldPassword.setView(viewInputOldPassword);
                dialogInputOldPassword.setCancelable(true);
                EditText userOldPasswordEditText = viewInputOldPassword.findViewById(R.id.inputOldUserPassword);
                dialogInputOldPassword.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println("妳按下了確定");
                        ModelPasswordChange userOldPassword =new ModelPasswordChange(userOldPasswordEditText.getText().toString());
                        Call<ModelPasswordChange> callInputOldPassword =ourAPIService.postUserOldPassword("bearer "+userToken,userOldPassword);
                        callInputOldPassword.enqueue(new Callback<ModelPasswordChange>() {
                            @Override
                            public void onResponse(Call<ModelPasswordChange> call, Response<ModelPasswordChange> response) {
                                String result=response.message();
                                System.out.println(result+"密碼驗證");
                                if(result.equals("OK")){
                                    Toast.makeText(getActivity(),"密碼正確",Toast.LENGTH_SHORT).show();
                                    //密碼正確 要開啟另一個對話方塊
                                    AlertDialog.Builder dialogInputNewPassword= new AlertDialog.Builder(getActivity());
                                    View viewInputOldPassword = getActivity().getLayoutInflater().inflate(R.layout.setnewpassword, null);
                                    dialogInputNewPassword.setView(viewInputOldPassword);
                                    dialogInputNewPassword.setCancelable(true);
                                    EditText userNewPasswordEditText = viewInputOldPassword.findViewById(R.id.inputNewPassword);
                                    EditText userNewConfirmPasswordEditText = viewInputOldPassword.findViewById(R.id.inputConfirmNewPassword);
                                    dialogInputNewPassword.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            System.out.println("妳按下了確定");
                                            String newPassword =userNewPasswordEditText.getText().toString();
                                            String newPasswordConfirm =userNewConfirmPasswordEditText.getText().toString();

                                            if(newPassword.equals(newPasswordConfirm)){
                                                ModelPasswordChange newPasswordToUpdate =new ModelPasswordChange(newPassword);
                                                Call<ModelPasswordChange> callPasswordChange =ourAPIService.putUserOldPassword("bearer "+userToken,newPasswordToUpdate);
                                                callPasswordChange.enqueue(new Callback<ModelPasswordChange>() {
                                                    @Override
                                                    public void onResponse(Call<ModelPasswordChange> call, Response<ModelPasswordChange> response) {
                                                        String result=response.message();
                                                        System.out.println(result+"密碼修改");
                                                        if(result.equals("OK")){
                                                            Toast.makeText(getActivity(),"密碼修改成功",Toast.LENGTH_SHORT).show();
                                                        }else {
                                                            Toast.makeText(getActivity(),"密碼格式不符",Toast.LENGTH_SHORT).show();
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<ModelPasswordChange> call, Throwable t) {

                                                    }
                                                });
                                            }else{
                                                Toast.makeText(getActivity(),"前後密碼不一致",Toast.LENGTH_SHORT).show();
                                            }


                                        }
                                    });
                                    dialogInputNewPassword.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            System.out.println("妳按下了取消");
                                        }
                                    });

                                    dialogInputNewPassword.show();

                                }else{
                                    Toast.makeText(getActivity(),"密碼錯誤",Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ModelPasswordChange> call, Throwable t) {
                                System.out.println("密碼修改 - 連線失敗");
                                Log.d("HKT", "response: " + t.toString());
                            }
                        });

                    }
                });
                dialogInputOldPassword.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println("妳按下了取消");
                    }
                });
                dialogInputOldPassword.show();
            }
        });
    }
}
