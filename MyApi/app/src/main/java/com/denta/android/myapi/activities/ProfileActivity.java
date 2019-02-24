package com.denta.android.myapi.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.denta.android.myapi.R;
import com.denta.android.myapi.fragments.HomeFragment;
import com.denta.android.myapi.fragments.SettingsFragment;
import com.denta.android.myapi.fragments.UsersFragment;

public class ProfileActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        navigationView = findViewById(R.id.bottom_nav);

        navigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        /*if (!SharedPrefManager.getInstance(this).isLoggedIn())
        {
            Intent intent = new Intent(this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }*/
    }

    private void displayFragment(Fragment fragment)
    {
        getSupportFragmentManager()
                .beginTransaction().replace(R.id.mycontainer,fragment).commit();
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId())
        {
            case R.id.menu_home:
                displayFragment(new HomeFragment());
                break;
            case R.id.menu_users:
                displayFragment(new UsersFragment());
                break;
            case R.id.menu_settings:
                displayFragment(new SettingsFragment());
                break;
        }
        return false;
    }
}
