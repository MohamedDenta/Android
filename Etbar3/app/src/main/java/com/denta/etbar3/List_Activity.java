package com.denta.etbar3;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

public class List_Activity extends AppCompatActivity {

    String bloodType = "";
    ListView listView ;
    ArrayAdapter<String>adapter;
    ArrayList<String> itemIds;
    ArrayList<User>users;
    User usr;
    RelativeLayout mylayout;
    SpotsDialog dialog;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_);
        listView = findViewById(R.id.lst_item);
        mylayout = findViewById(R.id.mylayout);
        itemIds = new ArrayList<>();
        users = new ArrayList<>();

        dialog = new SpotsDialog(this);
        dialog.setTitle(getText(R.string.Msg_Waiting));
        dialog.setCancelable(false);

        final Intent intent = getIntent();
        int x = 0;
        if(intent!=null)
        {
            x = intent.getIntExtra("BLOOD_TYPE",0);
        }
        switch (x)
        {
            case 0:
                //  a+
                bloodType = "o+";
                showToast(bloodType);
                break;
            case 1:
                // a-
                bloodType = "o-";
                showToast(bloodType);
                break;
            case 2:
                // b+
                bloodType = "a+";
                showToast(bloodType);
                break;
            case 3:
                // b-
                bloodType = "a-";
                showToast(bloodType);
                break;
            case 4:
                // o+
                bloodType = "b+";
                showToast(bloodType);
                break;
            case 5:
                // o-
                bloodType = "b-";
                showToast(bloodType);
                break;
            case 6:
                // ab+
                bloodType = "ab+";
                showToast(bloodType);
                break;
            case 7:
                // ab-
                bloodType = "ab-";
                showToast(bloodType);
                break;
        }
        adapter = new ArrayAdapter<>(this,R.layout.list_item,R.id.tv_item,itemIds);
        listView.setAdapter(adapter);
        selectProcess();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showPopup(view, position);

            }
        });
    }
    public void showPopup(View v, final int pos) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.user_menu, popup.getMenu());
        popup.show();

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.menu_item_contact:
                        showToast("contact ...");
                        contact(pos);
                        break;
                    case R.id.menu_item_edit:
                        showToast("edit ...");
                        edit(pos);
                        break;
                    case R.id.menu_item_delete:
                        showToast("delete ...");
                        delete(pos);
                        break;

                }
                return true;
            }
        });
    }

    private void delete(final int pos) {

        dialog.show();
        User u  = users.get(pos);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(u.getPhone());
        reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(List_Activity.this,getText(R.string.Msg_delete_success),Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(List_Activity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
                dialog.dismiss();
            }
        });


    }

    private void edit(int pos) {
        usr = users.get(pos);
        Intent intent = new Intent(List_Activity.this,EditActivity.class);
        intent.putExtra("USER",usr);
        startActivity(intent);
        finish();
    }

    private void contact(int pos) {
        usr = users.get(pos);
        String str = usr.phone;
        if (ActivityCompat.checkSelfPermission(List_Activity.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            showToast(getText(R.string.Msg_call_permession).toString());
            return ;
        }
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+str));
        startActivity(callIntent);
    }

    private void selectProcess() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemIds.clear();
                users.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    User usr = snapshot.getValue(User.class);
                    assert usr != null;
                    if(usr.getBloodtype().equals(bloodType))
                    {
                        users.add(usr);
                        itemIds.add(usr.name);
                    }
                }
                adapter.notifyDataSetChanged();
                if(itemIds.isEmpty())
                {
                    showToast(getText(R.string.Msg_empty_list).toString());
                    
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(List_Activity.this,getText(R.string.Msg_error_retriev)+" Cause : "+databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showToast(String i) {
        Toast.makeText(this,"  "+i,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(List_Activity.this,DashBoard.class);
        startActivity(intent);
        finish();
    }
}
