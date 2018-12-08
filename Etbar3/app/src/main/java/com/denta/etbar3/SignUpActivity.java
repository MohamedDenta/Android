package com.denta.etbar3;

import android.accounts.NetworkErrorException;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import dmax.dialog.SpotsDialog;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView btnSignUp,btnSignUpCancel;
    EditText etEmail , etPwd ;
    SpotsDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        btnSignUp = findViewById(R.id.btn_signup);
        btnSignUpCancel = findViewById(R.id.btn_signup_cancel);
        etEmail = findViewById(R.id.et_signup_email);
        etPwd = findViewById(R.id.et_signup_pwd);
        /* ------------------------------------------------------------- */
        btnSignUpCancel.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);

        progressDialog = new SpotsDialog(this);
        progressDialog.setTitle(getResources().getString(R.string.Msg_Waiting));
        progressDialog.setCancelable(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_signup_cancel:
                cancel();
                break;
            case R.id.btn_signup:
                try {
                    addUser();
                }
                catch (Exception ex)
                {
                    Toast.makeText(SignUpActivity.this,ex.getMessage(),Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    private boolean check_input() {
        String email = etEmail.getText().toString().trim();
        String pass = etPwd.getText().toString().trim();
        String regex = "^(.+)@(.+)$";
        if(email.length()==0||pass.length()==0)
        {
            Toast.makeText(SignUpActivity.this,getText(R.string.Msg_missed),Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!email.matches(regex))
        {
            Toast.makeText(SignUpActivity.this,getText(R.string.Msg_email_invalid),Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
    private void addUser() {
        progressDialog.show();
        String email , pwd;
        email = etEmail.getText().toString().trim();
        pwd = etPwd.getText().toString().trim();
        if(!check_input())
            return;

        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email,pwd).addOnCompleteListener(SignUpActivity.this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(SignUpActivity.this,getText(R.string.Msg_add_success),Toast.LENGTH_LONG).show();
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    updateUI(user);
                }
                else
                {
                    if(task.getException() instanceof FirebaseNetworkException||task.getException() instanceof NetworkErrorException)
                        Toast.makeText(SignUpActivity.this,getText(R.string.Msg_network_error),Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUpActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }

        });

    }

    private void updateUI(FirebaseUser user) {
        Intent intent = new Intent(SignUpActivity.this,DashBoard.class);
        startActivity(intent);
        finish();
    }

    private void cancel() {
        Intent intent = new Intent(SignUpActivity.this,main.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        cancel();
    }
}
