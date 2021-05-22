package com.example.a110407_app.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.a110407_app.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class PasswordSetting extends AppCompatActivity {
    SQLiteDBHelper TableUserPassword;
    private final String DB_NAME = "MyDairy.db";
    private String PASSWORD_TABLE_NAME = "UserPassword";
    private final int DB_VERSION = 13;

    private Switch openorclosePassword;
    private Button  btnresetPassword, btnnewPassword;
    private String p;//從Arraylist中撈密碼
    private String strOpenPassword, strPassword,strConfirmPassword,strDate, strOldPassword, strresetPassword, strresetConfirmPassword;
    private EditText getOpenClosePassword,getPassword, getConfirmPassword, getOldPassword, getresetPassword, getresetConfirmPassword;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_setting);
        openorclosePassword = findViewById(R.id.openorclosePassword);
        btnresetPassword = findViewById(R.id.resetPassword);
        btnnewPassword = findViewById(R.id.newPassword);
        TableUserPassword = new SQLiteDBHelper(PasswordSetting.this,DB_NAME,null,DB_VERSION,PASSWORD_TABLE_NAME);
        TableUserPassword.getWritableDatabase();
        Integer month = 0;
        Integer date = 0;
        Integer year =0;
        Date mDate = new Date();
        year = mDate.getYear()-100+2000;
        month = mDate.getMonth() + 1;
        final String strMonth = month.toString();
        final String strYear = year.toString();
        strDate = strYear + "/"+strMonth;


        // 密碼鎖Switch
        openorclosePassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AlertDialog.Builder inputOpenClosePasswordDialog = new AlertDialog.Builder(PasswordSetting.this);
                View view = getLayoutInflater().inflate(R.layout.openclosepassword, null);
                inputOpenClosePasswordDialog.setView(view);
                inputOpenClosePasswordDialog.setTitle("輸入密碼");

                //判斷Switch是否被打開
                if (buttonView.isChecked()==true){
                    inputOpenClosePasswordDialog.setPositiveButton("確認", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getOpenClosePassword = view.findViewById(R.id.inputOpcnClosePassword);
                            strOpenPassword = getOpenClosePassword.getText().toString();
                            //從資料表中抓密碼出來(與下方的可同步)
                            ArrayList passwordArraylist = new ArrayList();
                            passwordArraylist = TableUserPassword.showAllPassword();
                            //如果密碼表為空，設置新密碼
                            if(passwordArraylist.size()==0){
                                setnewPassword();
                            }else{//否則，進入判斷
                                for(HashMap<String,String> data:TableUserPassword.showAllPassword()){
                                    p = data.get("Password");
                                }
                                //判斷開啟密碼鎖的密碼，是否等於資料表中儲存的密碼
                                if(strOpenPassword.equals(p)){
                                    btnnewPassword.setEnabled(true);//開啟新增密碼的button
                                    btnresetPassword.setEnabled(true);//開啟重設密碼的button
                                    Toast.makeText(PasswordSetting.this,"成功開啟",Toast.LENGTH_LONG).show();
                                    TableUserPassword.updateLock(strOpenPassword,"1");////Set是否上鎖欄為1(已上鎖)
                                }else{
                                    btnnewPassword.setEnabled(false);//關閉新增密碼的button
                                    btnresetPassword.setEnabled(false);//關閉重設密碼的button
                                    buttonView.setChecked(false); //關閉Switch
                                    Toast.makeText(PasswordSetting.this,"密碼不符,請重新輸入",Toast.LENGTH_LONG).show();
                                }
                            }

                        }
                    });
                    inputOpenClosePasswordDialog.show();

                }else{//Switch關閉密碼鎖
                    inputOpenClosePasswordDialog.setPositiveButton("確認", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getOpenClosePassword = view.findViewById(R.id.inputOpcnClosePassword);
                            strOpenPassword = getOpenClosePassword.getText().toString();
                            for(HashMap<String,String> data:TableUserPassword.showAllPassword()){
                                p = data.get("Password");
                            }
                            //判斷關閉密碼鎖的密碼，是否等於資料表中儲存的密碼
                            if(strOpenPassword.equals(p)){
                                btnnewPassword.setEnabled(false);//關閉新增密碼的button
                                btnresetPassword.setEnabled(false);//關閉重設密碼的button
                                //buttonView.setChecked(false);
                                TableUserPassword.updateLock(strOpenPassword,"0");//Set是否上鎖欄為0(未上鎖)
                                Toast.makeText(PasswordSetting.this,"成功關閉",Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(PasswordSetting.this,"密碼不符,未能成功關閉",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    inputOpenClosePasswordDialog.show();
                }
            }
        });
        //重設密碼
        resetPassword();
        //設定新密碼
        setnewPassword();
    }


    //新增密碼method
    public void setnewPassword(){
        btnnewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //從資料表取出密碼
                ArrayList ArraylistuserPassword = new ArrayList();
                ArraylistuserPassword = TableUserPassword.showAllPassword();
                //如果arraylist大小大於0(已設定過密碼)
                if(ArraylistuserPassword.size() > 0){
                    System.out.println(ArraylistuserPassword);
                    Toast.makeText(PasswordSetting.this,"已設定密碼",Toast.LENGTH_LONG).show();
                }else{//arraylist大小等於0，需設定新密碼
                    AlertDialog.Builder newPasswordDialog = new AlertDialog.Builder(PasswordSetting.this);
                    View view = getLayoutInflater().inflate(R.layout.setnewpassword, null);
                    newPasswordDialog.setView(view);
                    newPasswordDialog.setTitle("新增密碼");
                    newPasswordDialog.setPositiveButton("確認", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getPassword = view.findViewById(R.id.inputNewPassword);
                            getConfirmPassword =  view.findViewById(R.id.inputConfirmNewPassword);
                            strPassword = getPassword.getText().toString();
                            strConfirmPassword = getConfirmPassword.getText().toString();
                            //判斷輸入密碼不得為空值
                            if(getPassword.getText().toString().matches("") || getConfirmPassword.getText().toString().matches("")){
                                Toast.makeText(PasswordSetting.this,"密碼不得為空值",Toast.LENGTH_LONG).show();
                                //判斷密碼與確認密碼是否一致
                                if(strPassword.equals(strConfirmPassword)==false){
                                    Toast.makeText(PasswordSetting.this,"密碼不一致",Toast.LENGTH_LONG).show();
                                }else{
                                    //全部正確，寫入資料庫
                                    strPassword = getPassword.getText().toString();
                                    TableUserPassword.addPassword(strPassword,strDate);

                                }
                            }
                        }
                    });
                    newPasswordDialog.show();
                }
            }
        });
    }

    //重設密碼method
    public void resetPassword(){
        btnresetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder inputOldPasswordDialog = new AlertDialog.Builder(PasswordSetting.this);
                View view = getLayoutInflater().inflate(R.layout.inputoldpassword, null);
                inputOldPasswordDialog.setView(view);
                inputOldPasswordDialog.setTitle("輸入舊密碼");
                inputOldPasswordDialog.setPositiveButton("確認", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getOldPassword = view.findViewById(R.id.inputOldPassword);
                        strOldPassword = getOldPassword.getText().toString();
                        //取得資料表中的密碼
                        for(HashMap<String,String> data:TableUserPassword.showAllPassword()){
                            p = data.get("Password");
                        }
                        //如果輸入的舊密碼不等於資料表中的密碼
                        if(strOldPassword.equals(p) == false){
                            Toast.makeText(PasswordSetting.this, "密碼錯誤", Toast.LENGTH_SHORT).show();
                        }else{//密碼一致
                            AlertDialog.Builder resetPasswordDialog = new AlertDialog.Builder(PasswordSetting.this);
                            View view = getLayoutInflater().inflate(R.layout.setnewpassword, null);//與設置新密碼共用Layout
                            resetPasswordDialog.setView(view);
                            resetPasswordDialog.setTitle("重設密碼");
                            resetPasswordDialog.setPositiveButton("確認", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    getresetPassword = view.findViewById(R.id.inputNewPassword);
                                    getresetConfirmPassword = view.findViewById(R.id.inputConfirmNewPassword);
                                    strresetPassword = getresetPassword.getText().toString();
                                    strresetConfirmPassword = getresetConfirmPassword.getText().toString();
                                    //判斷密碼與確認密碼是否一致
                                    if(strresetPassword.equals(strresetConfirmPassword)== false){
                                        Toast.makeText(PasswordSetting.this,"密碼不一致",Toast.LENGTH_LONG).show();
                                    }else{
                                        if(strresetPassword.equals(strOldPassword)){//判斷是否和舊密碼相同
                                            Toast.makeText(PasswordSetting.this,"密碼不可和舊密碼相同",Toast.LENGTH_LONG).show();

                                        }else{//更新成功
                                            TableUserPassword.resetPassword(strOldPassword,strresetPassword);
                                            Toast.makeText(PasswordSetting.this,"更新成功",Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                            });
                            resetPasswordDialog.show();
                        }
                    }
                });
                inputOldPasswordDialog.show();
            }
        });
    }

}