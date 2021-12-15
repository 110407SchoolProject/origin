package com.example.a110407_app.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a110407_app.MainActivity;
import com.example.a110407_app.Model.Token;
import com.example.a110407_app.Model.User;
import com.example.a110407_app.Model.UserLogin;
import com.example.a110407_app.R;
import com.example.a110407_app.RetrofitAPI.APIService;
import com.example.a110407_app.RetrofitAPI.RetrofitManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    APIService ourAPIService;



    String userToken;

    private String userNameServer="";
    private String userPasswordServer="";
    private String userTrueName="";


    private LoginViewModel loginViewModel;
    private Button registerButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);


        EditText usernameEditText = findViewById(R.id.userNameEditText);

        EditText passwordEditText = findViewById(R.id.userPasswordEditText);

        final Button loginButton = findViewById(R.id.btnLogin);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        registerButton = (Button) findViewById(R.id.btnRegister);
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
//暫時用不到
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
        passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
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
//                loadingProgressBar.setVisibility(View.VISIBLE);
//                loginViewModel.login(usernameEditText.getText().toString(),
//                        passwordEditText.getText().toString());

                ourAPIService = RetrofitManager.getInstance().getAPI();

                String username= usernameEditText.getText().toString();
                String userPassword= passwordEditText.getText().toString();



                UserLogin userLogin = new UserLogin(username,userPassword);

                Call<UserLogin> callLogin = ourAPIService.postUserAccountAndPassword(userLogin);

                callLogin.enqueue(new Callback<UserLogin>() {
                    @Override
                    public void onResponse(Call<UserLogin> call, Response<UserLogin> response) {
                        System.out.println("伺服器有回應");

                        String token = response.body().getToken();

                        userToken = token;

                        if(token.length()==0){
                            System.out.println("沒接收到Token，可能是帳密錯誤或是不存在");
                            String loginFailMessage ="帳號或密碼可能有誤";
                            Toast.makeText(getApplicationContext(), loginFailMessage, Toast.LENGTH_LONG).show();

                        }else {
                            System.out.println("有接收到Toke，登入成功");
//                            String loginSuccessMessage ="登入成功";
//                            Toast.makeText(getApplicationContext(), loginSuccessMessage, Toast.LENGTH_LONG).show();

                            openActivityHome(userToken);

                            System.out.println("Token:"+token);
                        }
                    }
                    @Override
                    public void onFailure(Call<UserLogin> call, Throwable t) {
                        System.out.println("伺服器連線失敗");
                        Log.d("HKT", "response: " + t.toString());
                    }
                });
            }
        });

    }

    public void openActivityHome(String userToken){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("userToken", userToken);
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
