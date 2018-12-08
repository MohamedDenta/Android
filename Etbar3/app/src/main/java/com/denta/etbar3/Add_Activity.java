package com.denta.etbar3;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import dmax.dialog.SpotsDialog;

public class Add_Activity extends AppCompatActivity implements View.OnClickListener {

    EditText etName , etPhone,etBloodType,etLstDnt;
    ImageView btnAdd , btnCancel;
    User user;
    Calendar c = Calendar.getInstance();
    SpotsDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_);

        etName = findViewById(R.id.et_add_name);
        etPhone = findViewById(R.id.et_add_phone);
        etBloodType = findViewById(R.id.et_add_bloodtype);
        etLstDnt = findViewById(R.id.et_add_lst_donation);

        btnAdd = findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(this);
        btnCancel = findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(this);

        progressDialog = new SpotsDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle(getText(R.string.Msg_Waiting));

        etLstDnt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    popUpDate(v);
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        etName.setText(outState.getCharSequence("NAME",""));
        etName.setText(outState.getCharSequence("PHONE",""));
        etName.setText(outState.getCharSequence("BLOODTYPE",""));
        etName.setText(outState.getCharSequence("LSTDNT",""));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        savedInstanceState.putCharSequence("NAME",etName.getText());
        savedInstanceState.putCharSequence("PHONE",etPhone.getText());
        savedInstanceState.putCharSequence("BLOODTYPE",etBloodType.getText());
        savedInstanceState.putCharSequence("LSTDNT",etLstDnt.getText());

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id)
        {
            case R.id.btn_add:
                try {
                    if(!check_input())
                    {
                        return;
                    }
                    User user = new User(etName.getText().toString(),etPhone.getText().toString()
                                        ,etBloodType.getText().toString(),etLstDnt.getText().toString());
                    insert(user);
                }
                catch (Exception ex)
                {
                    showToast(ex.getMessage());
                }
                break;
            case R.id.btn_cancel:
                startActivity(new Intent(Add_Activity.this,DashBoard.class));
                finish();
                break;
        }

    }

    private void insert(User user) {
        progressDialog.show();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

        reference.child(user.phone).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(Add_Activity.this,getResources().getText(R.string.Msg_add_success),Toast.LENGTH_LONG).show();
                    reset();
                }
                else
                {
                    assert task.getException()!= null;
                    Toast.makeText(Add_Activity.this,""+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
                progressDialog.dismiss();

            }
        });
    }

    private void reset() {
        etName.setText("");
        etBloodType.setText("");
        etPhone.setText("");
        etLstDnt.setText("");
    }

    private void showToast(String s) {
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }
    private boolean check_input() {
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String lstdate = etLstDnt.getText().toString().trim();
        String bt = etBloodType.getText().toString().trim();
        String regex = "^\\(?([0-9]{3})\\)?[-.\\s]?([0-9]{3})[-.\\s]?([0-9]{4})$";
        if(name.length()==0||phone.length()==0||lstdate.length()==0||bt.length()==0)
        {
            Toast.makeText(Add_Activity.this,getText(R.string.Msg_missed),Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!phone.matches(regex))
        {
            Toast.makeText(Add_Activity.this,getText(R.string.Msg_phone_invalid),Toast.LENGTH_SHORT).show();
            return false;
        }
        regex = "^(A|B|AB|O|a|b|ab|o)[+-]$";
        if(!bt.matches(regex))
        {
            Toast.makeText(Add_Activity.this,getText(R.string.Msg_bt_invalid),Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    public void popUpDate(View view) {
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, monthOfYear);
                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String str =c.get(Calendar.DAY_OF_MONTH)+"-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.YEAR);
                etLstDnt.setText(str);
            }
        };
        new DatePickerDialog(Add_Activity.this,date,c
                .get(Calendar.YEAR), c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)).show();
    }
    private void cancel() {
        Intent intent = new Intent(Add_Activity.this,DashBoard.class);
        startActivity(intent);
        finish();
    }
    @Override
    public void onBackPressed() {
        cancel();
    }
}
