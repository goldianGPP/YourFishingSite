package com.goldian.yourfishsingsite.Model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesModel {
    SharedPreferences sharedPref;
    public SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    public PreferencesModel(Context context, String name) {
        sharedPref = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
    }

    public void write(String key, String value){
        editor.putString(key,value);
        editor.apply();
    }

    public String read(String key){
        return sharedPref.getString(key, "");
    }
}
