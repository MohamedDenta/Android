package com.denta.etbar3;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneVerify extends AppCompatActivity implements View.OnClickListener {

    ImageView ivSendMsg , ivLogin;
    EditText etCode,etPhone;
    FirebaseAuth firebaseAuth;
    private String codeSent;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phoneverify);
        firebaseAuth = FirebaseAuth.getInstance();

        ivLogin = findViewById(R.id.iv_phone_login);
        ivSendMsg = findViewById(R.id.iv_send_code);
        etCode = findViewById(R.id.et_verification_code);
        etPhone = findViewById(R.id.et_phone_number);
        ivSendMsg.setOnClickListener(this);
        ivLogin.setOnClickListener(this);

        etCode.setEnabled(false);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.iv_phone_login:
                login();
                break;
            case R.id.iv_send_code:
                sendCode();
                break;
        }
    }

    private void sendCode() {
        String phoneNumber = etPhone.getText().toString().trim();
        if(phoneNumber.length()==0)
        {
            Toast.makeText(PhoneVerify.this,"Enter Phone Number",Toast.LENGTH_LONG).show();
            return;
        }

        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                codeSent = s;
                etCode.setEnabled(true);
            }
        };
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    private void login() {
        String code = etCode.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent,code);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            user = task.getResult().getUser();
                            loginToDashBoard();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(PhoneVerify.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                });

    }

    private void loginToDashBoard() {
        Intent intent = new Intent(PhoneVerify.this,DashBoard.class);
        startActivity(intent);
        finish();
    }
    private void cancel() {
        Intent intent = new Intent(PhoneVerify.this,main.class);
        startActivity(intent);
        finish();
    }
    @Override
    public void onBackPressed() {
        cancel();
    }
}
