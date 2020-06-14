package com.aniapps.utils;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.aniapps.siri.SiriApp;

public class Pref {
    private static Pref uniqInstance;
    private static SharedPreferences pref;
    public static SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    public static Pref getIn() {
        if (uniqInstance == null) {
            uniqInstance = new Pref();
            pref = PreferenceManager.getDefaultSharedPreferences(SiriApp.mInstance);
        }
        editor = pref.edit();
        return uniqInstance;
    }

    public String getLanguage() {
        return pref.getString("language", "English");
    }

    public void setLanguage(String language) {
        editor.putString("language", language);
        editor.apply();
    }


    public String getSyncDone() {
        return pref.getString("sync_done", "No");
    }

    public void setSyncDone(String language) {
        editor.putString("sync_done", language);
        editor.apply();
    }

    public void setUniqueId(String id) {
        editor.putString("unique_id", id);
        editor.apply();
    }
    public String getApp_code() {
        return pref.getString("app_code", "");
    }



    public void setApp_code(String app_code) {
        editor.putString("app_code", app_code);
        editor.apply();
    }
    public String getDeviceId() {
        return pref.getString("device_id", "");
    }
    public void saveDeviceId(String id) {
        editor.putString("device_id", id);
        editor.apply();
    }

    public String getUniqueId() {
        return pref.getString("unique_id", "");
    }


    public String getShort_link() {
        return pref.getString("short_link", "");
    }

    public void setShort_link(String short_link) {
        editor.putString("short_link", short_link);
        editor.apply();
    }

    public String getTableName() {
        return pref.getString("tbname", "");
    }

    public void setTableName(String language) {
        editor.putString("tbname", language);
        editor.apply();
    }

    public int getMainCount() {
        return pref.getInt("liveCount", 0);
    }

    public void setMainCount(int mainCount) {
        editor.putInt("liveCount", mainCount);
        editor.apply();
    }



    public boolean isRate_us() {
        return pref.getBoolean("rate_us", false);
    }

    public void setRate_us(boolean rate_us) {
        editor.putBoolean("rate_us", rate_us);
        editor.apply();
    }


    public int getLaunch_count() {
        return pref.getInt("launch_count", 0);
    }

    public void setLaunch_count(int launch_count) {
        editor.putInt("launch_count", launch_count);
        editor.apply();
    }

    long date_count=0;

    public long getDate_count() {
        return pref.getLong("date_count", 0);
    }

    public void setDate_count(long date_count) {
        editor.putLong("date_count", date_count);
        editor.apply();
    }

    boolean nightMode;

    public boolean isNightMode() {
        return pref.getBoolean("nightMode", false);
    }


    public void setNightMode(boolean nightMode) {
        editor.putBoolean("nightMode", nightMode);
        editor.apply();
    }



    public int getSelected_count() {
        return pref.getInt("selected_count",0);
    }

    public void setSelected_count(int selected_count) {
        editor.putInt("selected_count", selected_count);
        editor.apply();
    }
}
