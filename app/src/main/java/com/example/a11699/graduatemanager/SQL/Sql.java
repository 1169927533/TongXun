package com.example.a11699.graduatemanager.SQL;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Sql extends SQLiteOpenHelper {
    private static  String name="grat.db";
    private static  Integer version=1;
    public Sql(Context context){
        super(context,name,null,version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建一个records的表
        db.execSQL("create table records(id integer primary key autoincrement,name varchar(200),sid varchar(200))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
