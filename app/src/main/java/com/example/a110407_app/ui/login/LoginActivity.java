package com.example.a110407_app.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a110407_app.MainActivity;
import com.example.a110407_app.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
    String result;
    TextView text;
    Button getData;

    private String userNameServer="";
    private String userPasswordServer="";
    private String userTrueName="";
    private LoginViewModel loginViewModel;
    private Button registerButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        text = (TextView) findViewById(R.id.Text);
//        getData = (Button) findViewById(R.id.getData);
//        getData.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Thread thread = new Thread(multiThread);
//                thread.start();
//            }
//        });
        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText usernameEditText = findViewById(R.id.userNameEditText);
        final EditText passwordEditText = findViewById(R.id.userPasswordEditText);
        final Button loginButton = findViewById(R.id.btnLogin);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        registerButton = (Button)findViewById(R.id.btnRegister);
        registerButton.setOnClickListener(btnRegisterOnClickListner);


        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
//???????????????
//                if (loginResult.getSuccess() != null) {
////                    updateUiWithUser(loginResult.getSuccess());
//                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
                openActivityHome();

//                Thread thread = new Thread(multiThread);
//                thread.start();
//                try {
//                    thread.sleep(150);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                System.out.println("????????????:"+userNameServer);
//                System.out.println("????????????:"+userPasswordServer);
//                if(userNameServer.equals(usernameEditText.getText().toString())){
//                    if(userPasswordServer.equals(passwordEditText.getText().toString())){
//                        //??????????????????????????????
//                        openActivityHome();
//                        //????????????
//                        String welcome = getString(R.string.welcome) + userTrueName;
//                        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
//                    }else{
//                        String loginFail = "???????????????????????????";
//                        Toast.makeText(getApplicationContext(), loginFail, Toast.LENGTH_LONG).show();
//                        System.out.println("????????????");
//                        openLoginView();
//                    }
//                }else{
//                    String loginFail = "???????????????";
//                    Toast.makeText(getApplicationContext(), loginFail, Toast.LENGTH_LONG).show();
//                    System.out.println("???????????????");
//                    openLoginView();
//                }



            }

        });
    }

    private final Runnable multiThread = new Runnable() {
        @Override
        public void run() {
            try {
                URL url = new URL("http://172.18.100.17/GetUserData.php");
                //???????????? HTTP ???????????????????????????????????????????????????
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                // ?????? Google ???????????? HttpURLConnection ??????
                connection.setRequestMethod("POST");
                // ????????????????????? POST
                connection.setDoOutput(true); // ????????????
                connection.setDoInput(true); // ????????????
                connection.setUseCaches(false); // ???????????????
                connection.connect(); // ????????????
                int responseCode =
                        connection.getResponseCode();
                // ???????????????????????????
                if(responseCode ==
                        HttpURLConnection.HTTP_OK){
                    // ?????? HTTP ??????????????? OK ???????????? Error
                    InputStream inputStream =
                            connection.getInputStream();
                    // ??????????????????
                    BufferedReader bufReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);
                    // ???????????????????????????
                    String line = ""; // ????????????????????????
                    while((line = bufReader.readLine()) != null) {
                        JSONArray datajson = new JSONArray(line);
                        int i = datajson.length()-1;
                        JSONObject info = datajson.getJSONObject(i);
                        String name = info.getString("userName");
                        String password = info.getString("userPassword");
                        String trueName =info.getString("userTrueName");
                        System.out.println(trueName);
                        System.out.println(name);
                        System.out.println(password);

                        userNameServer =name;
                        userPasswordServer =password;
                        userTrueName=trueName;

                    }
                    inputStream.close(); // ??????????????????
                }
                // ??????????????????????????????????????????
                // ????????????????????????????????????
                // ?????? Json ????????????????????????????????????
            }catch(Exception e){
                result = e.toString();
            }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        text.setText(result);

                    }
                });
        }
    };

    public void openActivityHome(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }



    public void openLoginView(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

    }
    public  View.OnClickListener btnRegisterOnClickListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            openActivityRegister();
        }
    };

    public void openActivityRegister(){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);

    }
//    private void updateUiWithUser(LoggedInUserView model) {
//        String welcome = getString(R.string.welcome) + userTrueName;
//        // TODO : initiate successful logged in experience
//        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
//    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}
