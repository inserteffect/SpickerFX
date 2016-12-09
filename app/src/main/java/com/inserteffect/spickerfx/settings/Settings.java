package com.inserteffect.spickerfx.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Settings {

    private static final String KEY_TEXT = "key_text";

    private final SharedPreferences sharedPreferences;

    public Settings(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void saveText(String text) {
        sharedPreferences.edit()
                         .putString(KEY_TEXT, text)
                         .apply();
    }

    public String loadText() {
        return sharedPreferences.getString(KEY_TEXT, "");
    }
}
