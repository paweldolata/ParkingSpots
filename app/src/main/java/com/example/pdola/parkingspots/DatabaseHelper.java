package com.example.pdola.parkingspots;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by pdola on 22.03.2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Parking.db";
    public static final String TABLE_NAME = "parking_table";
    public static final String COL_ID = "ID";
    public static final String COL_ADDRESS = "ADDRESS";
    public static final String COL_SPACE = "SPACE";
    public static final String COL_FREE_SPACE = "FREE_SPACE";
    public static final String COL_LATITUDE = "LATITUDE";
    public static final String COL_LONGITUDE = "LONGITUDE";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,ADDRESS TEXT,SPACE INTEGER,FREE_SPACE INTEGER,LATITUDE DOUBLE,LONGITUDE DOUBLE) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    public boolean insertData (String address,String space,String freeSpace, String latitude, String longitude){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ADDRESS,address);
        contentValues.put(COL_SPACE,space);
        contentValues.put(COL_FREE_SPACE,freeSpace);
        contentValues.put(COL_LATITUDE,latitude);
        contentValues.put(COL_LONGITUDE,longitude);
        long result = db.insert(TABLE_NAME,null ,contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }

    public boolean updateData(String id, String address, String space, String freeSpace, String latitude, String longitude){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ID,id);
        contentValues.put(COL_ADDRESS,address);
        contentValues.put(COL_SPACE,space);
        contentValues.put(COL_FREE_SPACE,freeSpace);
        contentValues.put(COL_LATITUDE,latitude);
        contentValues.put(COL_LONGITUDE,longitude);
        db.update(TABLE_NAME, contentValues, "ID = ?", new String[] { id });
        return true;
    }

    public Integer deleteData(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?", new String[] {id});
    }


}
