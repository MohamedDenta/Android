package com.denta.android.myapi.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.denta.android.myapi.R;
import com.denta.android.myapi.Storage.SharedPrefManager;
import com.denta.android.myapi.api.RetrofitClient;
import com.denta.android.myapi.model.LoginResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText etEmail , etPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.et_email);
        etPwd = findViewById(R.id.et_pwd);

        findViewById(R.id.btn_signin).setOnClickListener(this);
        findViewById(R.id.tv_register).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_signin:
                signIn();
                break;
            case R.id.tv_login:
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                break;
        }
    }

    private void signIn() {
        String email = etEmail.getText().toString();
        String pass = etPwd.getText().toString();
        if(email.isEmpty())
        {
            etEmail.setError("Email is required ");
            etEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            etEmail.setError("Email is not valid ");
            etEmail.requestFocus();
            return;
        }
        if(pass.isEmpty())
        {
            etPwd.setError("Password is required ");
            etPwd.requestFocus();
            return;
        }
        if(pass.length()<6)
        {
            etPwd.setError("Password is too short , must be > 5 letters ");
            etPwd.requestFocus();
            return;
        }

        Call<LoginResponse>call = RetrofitClient.getInstance().getApi().userLogin(email,pass);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse loginResponse = response.body();
                if (!loginResponse.isError())
                {
                    SharedPrefManager.getInstance(LoginActivity.this).saveUser(loginResponse.getUser());
                    Intent intent = new Intent(LoginActivity.this,ProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                else {
                    printMsg("some errors in login process");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                printMsg(t.getMessage());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (SharedPrefManager.getInstance(this).isLoggedIn())
        {
            Intent intent = new Intent(LoginActivity.this,ProfileActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    void printMsg(String s)
    {
        if(s!=null)
        {
            Toast.makeText(LoginActivity.this,s,Toast.LENGTH_LONG).show();
        }
    }
}
