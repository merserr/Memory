package com.example.memory;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class dbHelper extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "memory";
  //  public static final String ARPTABLE = "satz";
    public static final String TABLE_NAME = "satz";

    public static final String KEY_ID = "_id";
    public static final String KEY_LESSON = "lesson";
    public static final String KEY_OURTEXT = "owntext";
    public static final String KEY_DEUTSCHTEXT = "deutschtext";
    public static final String KEY_OURSOUND = "ownsound";
    public static final String KEY_DEUTSCHSOUND = "deutschsound";


   // public static final String KEY_ID = "_id";
  //  public static final String KEY_IP = "ip_address";
  //  public static final String KEY_MAC = "mac_address";
  //  public static final String KEY_FACTORY = "factory";
  //  public static final String KEY_NAME = "name";

    public dbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("===dbHelper===","--create table--");
        db.execSQL("create table " + TABLE_NAME + "(" + KEY_ID
                + " integer primary key," + KEY_LESSON + " text," + KEY_OURTEXT
                + " text," + KEY_DEUTSCHTEXT + " text," + KEY_OURSOUND + " text,"
                + KEY_DEUTSCHSOUND + " text" + ")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_NAME);

        onCreate(db);

    }
}