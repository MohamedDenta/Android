package com.denta.etbar3;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class DashBoard extends AppCompatActivity implements View.OnClickListener {
    FirebaseUser  user ;
    ImageView []ivArr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        findViewByIds();

    }
    void findViewByIds()
    {
        ivArr = new ImageView[8];
        ivArr[0] = findViewById(R.id.opos);
        ivArr[1] = findViewById(R.id.oneg);
        ivArr[2] = findViewById(R.id.apos);
        ivArr[3] = findViewById(R.id.aneg);
        ivArr[4] = findViewById(R.id.bpos);
        ivArr[5] = findViewById(R.id.bneg);
        ivArr[6] = findViewById(R.id.abpos);
        ivArr[7] = findViewById(R.id.abneg);

        for (int i=0;i<8;i++)
            ivArr[i].setOnClickListener(this);
    }
    @Override
    protected void onStart()
    {
        super.onStart();
        user = FirebaseAuth.getInstance().getCurrentUser();
        String m="";
        if(user!=null)
            if(user.getEmail()==null)
                m = user.getPhoneNumber()+" is login ... ";
            else
                m = user.getEmail()+" is login ... ";

        Toast.makeText(DashBoard.this,m,Toast.LENGTH_LONG).show();
    }
    public void addClick(View view)
    {
        Intent intent = new Intent(DashBoard.this,Add_Activity.class);
        startActivity(intent);
        finish();
    }
    @Override
    public void onBackPressed()
    {
        finish();
    }
    public void logOutClick(View view)
    {
        Toast.makeText(DashBoard.this,"Logout from "+user.getEmail(),Toast.LENGTH_LONG).show();
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(DashBoard.this,main.class));
        finish();
    }
    @Override
    public void onClick(View v)
    {
        int pos =getPosition((ImageView)v);
        if(pos==-1)
        {
            Toast.makeText(DashBoard.this,"pos "+pos,Toast.LENGTH_LONG).show();
            return;
        }

            Intent intent = new Intent(DashBoard.this,List_Activity.class);
            intent.putExtra("BLOOD_TYPE",pos);
            startActivity(intent);
            finish();
    }
    private int getPosition(ImageView v)
    {
        for (int i=0;i<8;i++)
            if(v.getId()==ivArr[i].getId())
                return i;
        return -1;
    }
}
