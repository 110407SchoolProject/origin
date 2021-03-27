package com.example.a110407_app.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.a110407_app.R;

public class RegisterActivity extends AppCompatActivity {
    private Button registerInRegister  ;
    private RadioGroup genderRadioGroup ;
    private RadioButton genderButtonMale;
    private RadioButton genderButtonFemale;
    private EditText userTrueNameEditText ;
    private EditText userNameEditText;
    private EditText userPasswordEditText;
    private EditText userPasswordConfirmEditText;
    private String userGender="";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //畫面上的各個動態欄位
        registerInRegister = (Button)findViewById(R.id.btnRegisterInRegister);
        registerInRegister.setOnClickListener(registerInRegisterOnclick);

        //真實姓名
        userTrueNameEditText =(EditText) findViewById(R.id.userTrueName);
        //性別選取欄位
        genderRadioGroup =(RadioGroup)findViewById(R.id.genderRadioGroup);
        genderButtonMale=(RadioButton)findViewById(R.id.genderMale);
        genderButtonFemale=(RadioButton)findViewById(R.id.genderFemale);
        genderRadioGroup.setOnCheckedChangeListener(radioButtonGenderOnCheckedChange);
        //帳號(使用者名稱)
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

            String userTrueName = userTrueNameEditText.getText().toString();
            String userName = userNameEditText.getText().toString();
            String userPassword = userPasswordEditText.getText().toString();
            String userPasswordConfirm =userPasswordConfirmEditText.getText().toString();
            System.out.println(userTrueName);
            System.out.println(userGender); //在上面定義
            System.out.println(userName);
            System.out.println(userPassword);
            System.out.println(userPasswordConfirm);

            int checkData =5;

            if(userTrueName.length()==0) {
                System.out.println("名字太短");
                checkData-=1;
            }
            if(userGender.length()==0){
                System.out.println("沒有選取性別");
                checkData-=1;
            }
            if(userName.length()<=5) {
                System.out.println("帳號不得少於五個字");
                checkData-=1;
            }
            if(userPassword.length()<=5) {
                System.out.println("密碼不得少於五個字");
                checkData-=1;
            }
            if(!(userPassword.equals(userPasswordConfirm))) {
                System.out.println("密碼不一致");
                checkData-=1;
            }
            System.out.println(checkData);
            if(checkData==5){
                String registerSuccess ="Register your Account Successfully";
                // TODO : initiate successful logged in experience
                Toast.makeText(getApplicationContext(), registerSuccess, Toast.LENGTH_LONG).show();
            }else{
                String registerFail ="Register your Account Fail 請仔細檢查上面是否填寫";
                // TODO : initiate successful logged in experience
                Toast.makeText(getApplicationContext(), registerFail, Toast.LENGTH_LONG).show();
            }







        }
    };

}
