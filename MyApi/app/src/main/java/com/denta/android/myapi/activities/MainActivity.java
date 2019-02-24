package com.denta.android.myapi.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.denta.android.myapi.Storage.SharedPrefManager;
import com.denta.android.myapi.model.DefaultResponse;
import com.denta.android.myapi.R;
import com.denta.android.myapi.api.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etEmail , etPwd,etName,etSchool;
    private TextView tvClickLogin;
    private Button btnSignup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etEmail = findViewById(R.id.et_email);
        etPwd = findViewById(R.id.et_pwd);
        etName = findViewById(R.id.et_name);
        etSchool = findViewById(R.id.et_school);
        tvClickLogin = findViewById(R.id.tv_login);
        btnSignup = findViewById(R.id.btn_signup);

        tvClickLogin.setOnClickListener(this);
        btnSignup.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_signup:
                userSignUp();
                break;
            case R.id.tv_login:
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                break;
        }
    }

    private void userSignUp() {
        String email = etEmail.getText().toString();
        String pass = etPwd.getText().toString();
        String name = etName.getText().toString();
        String school = etSchool.getText().toString();

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
        if(name.isEmpty())
        {
            etName.setError("Name is required ");
            etName.requestFocus();
            return;
        }
        if(school.isEmpty())
        {
            etSchool.setError("School is required ");
            etSchool.requestFocus();
            return;
        }
        Call<DefaultResponse>call = RetrofitClient.getInstance().getApi().createUser(email,pass,name,school);
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                DefaultResponse dr = response.body();
                if (!dr.isErr())
                {
                    printMsg(dr.getMsg());
                    }else {
                    printMsg("Some error occurred ");
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                printMsg(t.getMessage());
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        //if (SharedPrefManager.getInstance(this).isLoggedIn())
        {
            Intent intent = new Intent(MainActivity.this,ProfileActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    void printMsg(String s)
    {
        if(s!=null)
        {
            //JSONObject jsonObject;
            //jsonObject = new JSONObject(s);
            //String str = jsonObject.getString("message");
            Toast.makeText(MainActivity.this,s,Toast.LENGTH_LONG).show();
        }
    }
}
