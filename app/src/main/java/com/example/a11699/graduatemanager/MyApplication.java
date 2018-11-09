package com.example.a11699.graduatemanager;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class MyApplication extends Application {
    private static String s_id;
    private static String s_name;
    private SharedPreferences pref;//喜好设置
    private SharedPreferences.Editor editor;

    @Override
    public void onCreate() {
        super.onCreate();
        pref= PreferenceManager.getDefaultSharedPreferences(this);//每个工程都有默认的shar的文件，这里是获取到这个文件
        editor=pref.edit();//让share可以进行编辑操作

    }

    public static String getS_id() {
        return s_id;
    }

    public static void setS_id(String s_id) {
        MyApplication.s_id = s_id;
    }

    public static String getS_name() {
        return s_name;
    }

    public static void setS_name(String s_name) {
        MyApplication.s_name = s_name;
    }

    public SharedPreferences getPref() {
        return pref;
    }

    public void setPref(SharedPreferences pref) {
        this.pref = pref;
    }

    public SharedPreferences.Editor getEditor() {
        return editor;
    }

    public void setEditor(SharedPreferences.Editor editor) {
        this.editor = editor;
    }
}
