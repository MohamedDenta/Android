package com.denta.etbar3;

import android.accounts.NetworkErrorException;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.lang.reflect.Method;

import dmax.dialog.SpotsDialog;

public class main extends AppCompatActivity implements View.OnClickListener{

    private static final int PERMISSION_REQUEST_CODE_INTERNET = 1;
    ImageView ibLogin;
    ImageView  tvNotReg;
    ImageView tvPhoneLogin;
    EditText etEmail , etPwd;
    //------------------
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    SpotsDialog progressDialog;
    public final static String cur_usr = "CURRENT_USER";
    RelativeLayout mainLayout;
    private boolean doubleBackToExitPressedOnce = false;
    static int timer = 3;
    TextView tvTimer;
    private boolean isConnected;

    @Override
    protected void onStart() {
        super.onStart();
    }
    private void isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        isNetworkConnected(); // check internet

        ibLogin = findViewById(R.id.btn_login);
        tvPhoneLogin = findViewById(R.id.tv_login_phone);
        tvNotReg = findViewById(R.id.tv_not_registered);
        etEmail = findViewById(R.id.et_login_email);
        etPwd = findViewById(R.id.et_login_pwd);
        mainLayout = findViewById(R.id.main_layout);

        progressDialog = new SpotsDialog(main.this);
        progressDialog.setTitle(getText(R.string.Msg_Waiting));
        progressDialog.setCancelable(false);

        if(!isConnected)
        {
            Toast.makeText(main.this,getText(R.string.Msg_notconnect),Toast.LENGTH_SHORT).show();
            connect_to_internet();
        }
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if(mFirebaseUser != null) // logged in
        {
            hideUI();
        }
        ibLogin.setOnClickListener(this);
        tvPhoneLogin.setOnClickListener(this);
        tvNotReg.setOnClickListener(this);
    }

    private void connect_to_internet() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getText(R.string.Msg_notconnect));
        builder.setPositiveButton(getText(R.string.WIFI), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                WifiManager wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                wifiManager.setWifiEnabled(true);
            }
        });
        builder.setNegativeButton(getText(R.string.DATA), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setMobileDataEnabled(main.this,true);
            }
        });
        builder.show();
    }
    private void setMobileDataEnabled(Context context, boolean enabled){
        try
        {
            TelephonyManager telephonyService = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

            Method setMobileDataEnabledMethod = telephonyService.getClass().getDeclaredMethod("setDataEnabled", boolean.class);

            setMobileDataEnabledMethod.invoke(telephonyService, enabled);
        }
        catch (Exception ex)
        {
            Toast.makeText(context,getText(R.string.Msg_network_error),Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        int id = item.getItemId();
        switch (id)
        {
            case R.id.menu_item_close_account:
                Intent intent = new Intent(main.this,DeleteAccount.class);
                startActivity(intent);
                finish();
                break;
            case R.id.menu_item_exit:
                Toast.makeText(main.this,R.string.exit,Toast.LENGTH_LONG).show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        System.exit(0);
                    }
                }, 2000);

                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onStop() {
        super.onStop();

    }

    private void hideUI() {

        tvNotReg.setVisibility(View.GONE);
        tvPhoneLogin.setVisibility(View.GONE);
        etEmail.setVisibility(View.GONE);
        etPwd.setVisibility(View.GONE);
        ibLogin.setVisibility(View.GONE);

        tvTimer = findViewById(R.id.tv_timer);

        tvTimer.setTextSize(26.0f);

        showUpDownTimer((String) getText(R.string.Msg_Waiting));
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent =new Intent(main.this,DashBoard.class);
                intent.putExtra(cur_usr,mFirebaseUser);
                startActivity(intent);
                finish();
            }
        }, 3000);

    }

    public void click(View view) {
        Intent intent;

        switch (view.getId())
        {
            case R.id.tv_not_registered:
                intent =new Intent(main.this,SignUpActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_login:
                String email = etEmail.getText().toString().trim();
                String pass = etPwd.getText().toString().trim();
               boolean b = check_input();
               if(!b) {
                   return;
               }

                if(checkAuthentication(email,pass))
                {
                    intent =new Intent(main.this,DashBoard.class);
                    intent.putExtra(cur_usr,mFirebaseUser);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    return;
                }

                break;
            case R.id.tv_login_phone:
                loginWithPhone(view);
                break;
        }
    }

    private boolean check_input() {
        String email = etEmail.getText().toString().trim();
        String pass = etPwd.getText().toString().trim();
        String regex = "^(.+)@(.+)$";
        if(email.length()==0||pass.length()==0)
        {
            Toast.makeText(main.this,getText(R.string.Msg_missed),Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!email.matches(regex))
        {
            Toast.makeText(main.this,getText(R.string.Msg_email_invalid),Toast.LENGTH_SHORT).show();
            return false;
        }
        
        return true;
    }

    private boolean checkAuthentication(String email, String pass) {

        progressDialog.show();
        mFirebaseAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    mFirebaseUser = mFirebaseAuth.getCurrentUser();
                }
                else
                {
                    if(task.getException() instanceof FirebaseNetworkException||task.getException() instanceof NetworkErrorException)
                        Toast.makeText(main.this,getText(R.string.Msg_network_error),Toast.LENGTH_SHORT).show();

                    mFirebaseUser = null;
                }
                progressDialog.dismiss();
            }
        });
        return mFirebaseUser != null;
    }

    @Override
    public void onClick(View v) {

        click(v);
    }
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            System.exit(0);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getText(R.string.Msg_back_exit), Toast.LENGTH_LONG).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    public void loginWithPhone(View view) {
        Intent intent = new Intent(main.this,PhoneVerify.class);
        startActivity(intent);
        finish();
    }
    void  showUpDownTimer(String msg) {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                //-
                if(timer<1)
                {
                    Intent intent =new Intent(main.this,DashBoard.class);
                    intent.putExtra(cur_usr,mFirebaseUser);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    tvTimer.setText(timer);
                    timer--;
                    handler.post(this);
                }

            }
        };
        handler.post(runnable);

    }
}
