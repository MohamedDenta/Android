package com.denta.android.myapi.Storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.denta.android.myapi.model.User;

public class SharedPrefManager {
    private static final String SHARED_PREF_NAME = "my_pref";
    private Context context;
    private static SharedPrefManager sharedPrefManager;

    public SharedPrefManager(Context context) {
        this.context = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context)
    {
        if(sharedPrefManager!=null)
        {
            sharedPrefManager = new SharedPrefManager(context);
        }
        return sharedPrefManager;
    }
    public void saveUser(User user)
    {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("id",user.getId());
        editor.putString("email",user.getEmail());
        editor.putString("name",user.getName());
        editor.putString("school",user.getSchool());
        editor.apply();
    }
    public boolean isLoggedIn()
    {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        if(preferences.getInt("id",-1)!=-1)
            return true;
        return false;
    }
    public User getUser()
    {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return new User(
                preferences.getInt("id",-1),
                preferences.getString("email",""),
                preferences.getString("name",""),
                preferences.getString("school","")
        );
    }
    public void clear()
    {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }
}
