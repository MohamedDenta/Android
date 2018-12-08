package com.denta.etbar3;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
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

public class EditActivity extends AppCompatActivity implements View.OnClickListener {
    EditText etName , etPhone,etBT,etDate;
    ImageView ivEdit , ivCancel;
    Calendar c = Calendar.getInstance();
    User usr ;
    DatabaseReference databaseReference;
    SpotsDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Intent intent = getIntent();
        usr = (User) intent.getSerializableExtra("USER");

        etName = findViewById(R.id.et_edit_name);
        etPhone = findViewById(R.id.et_edit_phone);
        etBT = findViewById(R.id.et_edit_bloodtype);
        etDate = findViewById(R.id.et_edit_lst_donation);

        ivEdit = findViewById(R.id.iv_edit);
        ivCancel = findViewById(R.id.iv_cancel);

        ivEdit.setOnClickListener(this);
        ivCancel.setOnClickListener(this);
        reset();

        dialog = new SpotsDialog(this);
        dialog.setTitle(getText(R.string.Msg_Waiting));
        dialog.setCancelable(false);

        etDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    popUpDate(v);
            }
        });
    }

    private void reset() {
        if (usr!=null)
        {
            etName.setText(usr.getName());
            etPhone.setText(usr.getPhone());
            etBT.setText(usr.getBloodtype());
            etDate.setText(usr.getDntDate());
        }
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
                etDate.setText(str);
            }
        };
        new DatePickerDialog(EditActivity.this,date,c
                .get(Calendar.YEAR), c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.iv_edit:
                edit();
                break;
            case R.id.iv_cancel:
                cancel();
                break;

        }
    }

    private void cancel() {
        Intent i = new Intent(EditActivity.this,List_Activity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
        cancel();
    }

    private void edit() {
        if(TextUtils.isEmpty(etName.getText())||TextUtils.isEmpty(etBT.getText())||
                TextUtils.isEmpty(etDate.getText()))
        {
            Toast.makeText(this,getText(R.string.Msg_missed),Toast.LENGTH_LONG).show();

            return;
        }
        dialog.show();
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(usr.getPhone());
        usr.setName(etName.getText().toString());
        usr.setBloodtype(etBT.getText().toString());
        usr.setDntDate(etDate.getText().toString());
        databaseReference.setValue(usr).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(!task.isSuccessful())
                {
                    Toast.makeText(EditActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(EditActivity.this,getText(R.string.Msg_edit_success),Toast.LENGTH_LONG).show();
                }

                dialog.dismiss();
            }
        });
    }
}
