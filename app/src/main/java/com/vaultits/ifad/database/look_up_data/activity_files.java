package com.vaultits.ifad.database.look_up_data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class activity_files extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "activity_files.db";
    public static final String ACTIVITY_TYPES = "Activity_types_table";

    public activity_files(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory) {
        super(context, DB_NAME, factory, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + ACTIVITY_TYPES + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,TYPEID TEXT,DES TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //INSERT USERS
    public void insert_file_types(ArrayList<String> id, ArrayList<String> names) {
        deleteAll(ACTIVITY_TYPES);
        ContentValues cv = new ContentValues();
        for (int i = 0; i < id.size(); i++) {
            cv.put("TYPEID", id.get(i));
            cv.put("DES", names.get(i));
            this.getWritableDatabase().insertOrThrow(ACTIVITY_TYPES, "", cv);
        }
    }

    //GET TYPE ID FROM DES
    public String getTypeId(String des) {
        String dis = "";
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor res = db.rawQuery("select TYPEID from " + ACTIVITY_TYPES + " where DES='" + des + "'", null);
            while (res.moveToNext()) {
                dis = res.getString(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dis;
    }

    //GET ALL TYPE IDs
    public ArrayList<String> getFileTypes() {
        ArrayList<String> dis = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor res = db.rawQuery("select DES from Activity_types_table", null);
            while (res.moveToNext()) {
                dis.add(res.getString(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dis;
    }

    //delete all
    public void deleteAll(String table) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(table, null, null);
    }
}
