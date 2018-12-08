package com.denta.etbar3;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import dmax.dialog.SpotsDialog;

public class DeleteAccount extends AppCompatActivity implements View.OnClickListener {
    ImageView ivRmvDone , ivRmvCncl;
    EditText etRmvEmail,etRmvpwd;
    FirebaseAuth firebaseAuth;
    String email  , pass;
    FirebaseUser user;
    SpotsDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_account);
        firebaseAuth = FirebaseAuth.getInstance();
        etRmvEmail = findViewById(R.id.et_remove_email);
        etRmvpwd = findViewById(R.id.et_remove_pwd);
        ivRmvCncl = findViewById(R.id.iv_remove_cancel);
        ivRmvDone = findViewById(R.id.iv_remove_done);

        progressDialog = new SpotsDialog(DeleteAccount.this);
        progressDialog.setTitle(getText(R.string.Msg_Waiting));
        progressDialog.setCancelable(false);

        ivRmvDone.setOnClickListener(this);
        ivRmvCncl.setOnClickListener(this);
    }
    private boolean check_input() {
        String email = etRmvEmail.getText().toString().trim();
        String pass = etRmvpwd.getText().toString().trim();
        String regex = "^(.+)@(.+)$";
        if(email.length()==0||pass.length()==0)
        {
            Toast.makeText(DeleteAccount.this,getText(R.string.Msg_missed),Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!email.matches(regex))
        {
            Toast.makeText(DeleteAccount.this,getText(R.string.Msg_email_invalid),Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    @Override
    public void onClick(View v) {
        email  = etRmvEmail.getText().toString();
        pass = etRmvpwd.getText().toString();
        switch (v.getId())
        {
            case R.id.iv_remove_cancel:
                cancel();
                break;
            case R.id.iv_remove_done:
                if(check_input())
                    done();
                break;
        }

    }

    private void showErrorMsg() {
        Toast.makeText(this,getText(R.string.Msg_missed),Toast.LENGTH_SHORT).show();
    }

    private void done() {
        progressDialog.show();
        login();

         if(user==null)
         {

             return;
         }
        AuthCredential credential = EmailAuthProvider
                .getCredential(etRmvEmail.getText().toString(), etRmvpwd.getText().toString());

        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        user.delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(DeleteAccount.this,getText(R.string.Msg_account_deleted),Toast.LENGTH_SHORT).show();
                                        }
                                        else
                                        {
                                            Toast.makeText(DeleteAccount.this,getText(R.string.Msg_account_deleted_fail),Toast.LENGTH_SHORT).show();
                                        }
                                        clear();
                                        progressDialog.dismiss();
                                    }

                                });

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showErrorMsg();
                clear();
                progressDialog.dismiss();
            }
        });
    }

    private void login() {

        firebaseAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    user = firebaseAuth.getCurrentUser();
                }
                else
                {
                    user = null;
                    progressDialog.dismiss();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DeleteAccount.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clear() {
        etRmvEmail.setText("");
        etRmvpwd.setText("");
    }

    private void cancel() {
        Intent intent = new Intent(DeleteAccount.this,main.class);
        startActivity(intent);
        finish();
    }


}
