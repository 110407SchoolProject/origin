package com.example.a110407_app.ui.login;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.a110407_app.R;
import com.example.a110407_app.RetrofitAPI.APIService;
import com.example.a110407_app.RetrofitAPI.RetrofitManager;
import com.example.a110407_app.ui.SQLiteDBHelper;
import com.facebook.stetho.Stetho;

import com.example.a110407_app.Model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    public static final String EXTRA_TEXT="com.example.application.example.EXTRA_TEXT";
    public static final String EXTRA_TEXT2="com.example.application.example.EXTRA_TEXT2";

    APIService ourAPIService;

    private Button registerInRegister  ;
    private RadioGroup genderRadioGroup ;
    private RadioButton genderButtonMale;
    private RadioButton genderButtonFemale;

    private EditText userBirthdayEditText;
    private EditText userTrueNameEditText ;
    private EditText userNameEditText;
    private EditText userPasswordEditText;
    private EditText userPasswordConfirmEditText;
    private String userGender="";
    public String userTrueName, userName,userBirthday, userPassword, userPasswordConfirm;

    private final String DB_NAME = "MyDairy.db";
    private final String TABLE_NAME = "Profile";
    private final int DB_VERSION = 6;
    SQLiteDBHelper mHelper;
    //private  String TABLE_NAME_CATEGORY = "Category";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Stetho.initializeWithDefaults(this);
//        mHelper = new SQLiteDBHelper(this,DB_NAME,null,DB_VERSION,TABLE_NAME);
//        mHelper.getWritableDatabase();

        //畫面上的各個動態欄位
        registerInRegister = (Button)findViewById(R.id.btnRegisterInRegister);
        registerInRegister.setOnClickListener(registerInRegisterOnclick);

        //真實姓名
        userTrueNameEditText =(EditText) findViewById(R.id.userTrueName);
        //性別選取欄位
        genderButtonMale=(RadioButton)findViewById(R.id.genderMale);
        genderButtonFemale=(RadioButton)findViewById(R.id.genderFemale);
        genderRadioGroup=(RadioGroup)findViewById(R.id.genderRadioGroup);
        genderRadioGroup.setOnCheckedChangeListener(radioButtonGenderOnCheckedChange);
        //(生日)
        userBirthdayEditText=(EditText) findViewById(R.id.userBirthdayEditText);
        //帳號(使用者名稱 Email)
        userNameEditText =(EditText)findViewById(R.id.userNameEditText);
        //密碼
        userPasswordEditText = (EditText)findViewById(R.id.userPasswordEditText);
        //確認密碼
        userPasswordConfirmEditText =(EditText)findViewById(R.id.passwordConfirmEditText);

    }
    public RadioGroup.OnCheckedChangeListener radioButtonGenderOnCheckedChange= new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            System.out.println(checkedId);
            if (genderButtonFemale.isChecked()){
                System.out.println("女");
                userGender="女";
            }else if(genderButtonMale.isChecked()){
                System.out.println("男");
                userGender="男";
           }

        }
    };


    public View.OnClickListener registerInRegisterOnclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            userTrueName = userTrueNameEditText.getText().toString();
            userName = userNameEditText.getText().toString();
            userBirthday =userBirthdayEditText.getText().toString();
            userPassword = userPasswordEditText.getText().toString();
            userPasswordConfirm =userPasswordConfirmEditText.getText().toString();

            String message="";
            int checkData =6;

            if(userTrueName.length()==0) {
                message="請檢察真實姓名欄位";
                System.out.println("名字太短");
                checkData-=1;
            }
            if(userGender.length()==0){
                message="請檢察性別欄位";
                System.out.println("沒有選取性別");
                checkData-=1;
            }
            if(userName.length()<=5) {
                message="帳號不得少於五個字";
                System.out.println("帳號不得少於五個字");
                checkData-=1;
            }
            if(userPassword.length()<=5) {
                message="密碼不得少於五個字";
                System.out.println("密碼不得少於五個字");
                checkData-=1;
            }
            if(!(userPassword.equals(userPasswordConfirm))) {
                message="密碼與確認密碼不一致";
                System.out.println("密碼不一致");
                checkData-=1;
            }
            if((userBirthday.length()<=10)) {
                message="日期格式錯誤";
                System.out.println("日期格式錯誤");
                checkData-=1;
            }
            System.out.println(checkData);
            if(checkData==6){

                User user = new User(
                        userName,
                        userPassword,
                        userTrueName,
                        userName,
                        userGender,
                        userBirthday);

                ourAPIService = RetrofitManager.getInstance().getAPI();
                Call<User> callRegister = ourAPIService.postUser(user);

                callRegister.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        //result是伺服器的回傳訊息 ok才是註冊成功
                        String result = response.body().getResult();
                        System.out.println(result);
                        System.out.println("伺服器有成功回應");

                        //是否成功，取決於result是不是OK，因為有可能帳號被重復註冊

                        String registerSuccess ="註冊成功";
                        // TODO : initiate successful logged in experience
                        Toast.makeText(getApplicationContext(), registerSuccess, Toast.LENGTH_LONG).show();
                        openLoginActivity();
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        System.out.println("Fail");
                        Log.d("HKT", "response: " + t.toString());

                    }
                });

            }else{

                // TODO : initiate successful logged in experience
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }

        }
    };
    public void openLoginActivity(){
        Intent intent  = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }

}
