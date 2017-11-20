package com.builder.devconnect.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.builder.devconnect.global.MyApp;
import com.google.gson.Gson;


public class SharedPreferenceManager {
    private static SharedPreferenceManager sSharedPreferenceManager;
    private SharedPreferences mSharedPreference;

    private SharedPreferenceManager(Context context) {
        mSharedPreference = context.getSharedPreferences("SHARED_PREFERENCE_KEY", Context.MODE_PRIVATE);
    }

    public synchronized static SharedPreferenceManager newInstance() {
        if (sSharedPreferenceManager == null) {
            sSharedPreferenceManager = new SharedPreferenceManager(MyApp.getInstance());
        }

        return sSharedPreferenceManager;
    }

    /**
     * Use to save String value
     *
     * @param key
     * @param value
     */
    public void saveValue(String key, String value) {
        SharedPreferences.Editor editor = mSharedPreference.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getSavedValue(String key) {
        return mSharedPreference.getString(key, "");
    }

    /**
     * Use to save boolean state values
     *
     * @param key
     * @param value
     */
    public void saveStateValues(String key, boolean value) {
        SharedPreferences.Editor editor = mSharedPreference.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getSavedStates(String key) {
        return mSharedPreference.getBoolean(key, false);
    }

    /**
     * Use to save int state values
     *
     * @param key
     * @param value
     */
    public void saveIntValues(String key, int value) {
        SharedPreferences.Editor editor = mSharedPreference.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public void saveStateLongValues(String key, long value) {
        SharedPreferences.Editor editor = mSharedPreference.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public void saveFloatValues(String key, float value) {
        SharedPreferences.Editor editor = mSharedPreference.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    public int getSavedIntStates(String key) {
        return mSharedPreference.getInt(key, 0);
    }

    public float getSavedFloatStates(String key) {
        return mSharedPreference.getFloat(key, 0f);
    }

    public void saveObject(String key, Object value) {
        SharedPreferences.Editor editor = mSharedPreference.edit();
        Gson gson = new Gson();
        String json = gson.toJson(value);
        editor.putString(key, json);
        editor.apply();
    }

    public String getObject(String key) {
        return mSharedPreference.getString(key, "");
//        Gson gson = new Gson();
//        return gson.fromJson(json, value);
    }


}
