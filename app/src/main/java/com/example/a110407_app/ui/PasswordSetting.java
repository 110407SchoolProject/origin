package com.example.a110407_app.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.KeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.a110407_app.R;
import com.example.a110407_app.ui.profile.ProfileFragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import android.view.KeyEvent;

public class PasswordSetting extends AppCompatActivity  {
    SQLiteDBHelper TableUserPassword;
    private final String DB_NAME = "MyDairy.db";
    private String PASSWORD_TABLE_NAME = "UserPassword";
    private final int DB_VERSION = 13;

    private Switch openorclosePassword;
    private Button  btnresetPassword, btnnewPassword;
    private String p, lock;//從Arraylist中撈密碼
    private String  strPassword,strConfirmPassword,strDate, strOldPassword, strresetPassword, strresetConfirmPassword;
    private EditText getPassword, getConfirmPassword, getOldPassword, getresetPassword, getresetConfirmPassword;
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

        //以下為 Gary 5/27、28 寫的
        //判斷密碼鎖是否開啟(自動抓取目前狀態)
        for(HashMap<String,String> data:TableUserPassword.showLock()){
            lock = data.get("IfSetLock");
        }
        if(lock.equals("1")){
            openorclosePassword.setChecked(true);
        }else {
            openorclosePassword.setChecked(false);
        }



        // 密碼鎖Switch
        openorclosePassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //抓密碼表的密碼
                for(HashMap<String,String> data:TableUserPassword.showAllPassword()){
                    p = data.get("Password");
                }
                //判斷Switch是否被打開
                if (buttonView.isChecked()){
                    TableUserPassword.updateLock(p, "1");//打開更新為1
                }else{//Switch關閉密碼鎖
                    TableUserPassword.updateLock(p,"0");//關閉更新為0
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
                    Toast.makeText(PasswordSetting.this,"已設定密碼",Toast.LENGTH_LONG).show();
                }else{//arraylist大小等於0，需設定新密碼
                    AlertDialog.Builder newPasswordDialog = new AlertDialog.Builder(PasswordSetting.this,R.style.AlertDialogTheme);
                    View view = getLayoutInflater().inflate(R.layout.setnewpassword, null);
                    newPasswordDialog.setView(view);
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
    public void resetPassword() {
        btnresetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder inputOldPasswordDialog = new AlertDialog.Builder(PasswordSetting.this, R.style.AlertDialogTheme);
                View view = getLayoutInflater().inflate(R.layout.inputoldpassword, null);
                inputOldPasswordDialog.setView(view);
                inputOldPasswordDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //
                    }
                });
                inputOldPasswordDialog.setPositiveButton("確認", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getOldPassword = view.findViewById(R.id.inputOldPassword);
                        strOldPassword = getOldPassword.getText().toString();
                        //取得資料表中的密碼
                        for (HashMap<String, String> data : TableUserPassword.showAllPassword()) {
                            p = data.get("Password");
                        }
                        //如果輸入的舊密碼不等於資料表中的密碼
                        if (strOldPassword.equals(p) == false) {
                            Toast.makeText(PasswordSetting.this, "密碼錯誤", Toast.LENGTH_SHORT).show();
                        } else {//密碼一致
                            AlertDialog.Builder resetPasswordDialog = new AlertDialog.Builder(PasswordSetting.this, R.style.AlertDialogTheme);
                            View view = getLayoutInflater().inflate(R.layout.setnewpassword, null);//與設置新密碼共用Layout
                            resetPasswordDialog.setView(view);
                            resetPasswordDialog.setPositiveButton("確認", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    getresetPassword = view.findViewById(R.id.inputNewPassword);
                                    getresetConfirmPassword = view.findViewById(R.id.inputConfirmNewPassword);
                                    strresetPassword = getresetPassword.getText().toString();
                                    strresetConfirmPassword = getresetConfirmPassword.getText().toString();
                                    //判斷密碼與確認密碼是否一致
                                    if (strresetPassword.equals(strresetConfirmPassword) == false) {
                                        Toast.makeText(PasswordSetting.this, "密碼不一致", Toast.LENGTH_LONG).show();
                                    } else {
                                        if (strresetPassword.equals(strOldPassword)) {//判斷是否和舊密碼相同
                                            Toast.makeText(PasswordSetting.this, "密碼不可和舊密碼相同", Toast.LENGTH_LONG).show();

                                        } else {//更新成功
                                            TableUserPassword.resetPassword(strOldPassword, strresetPassword);
                                            Toast.makeText(PasswordSetting.this, "更新成功", Toast.LENGTH_LONG).show();
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
    //以上為 Gary 5/27、28 寫的
}