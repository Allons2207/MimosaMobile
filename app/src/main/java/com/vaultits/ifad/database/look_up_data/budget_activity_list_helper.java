package com.vaultits.ifad.database.look_up_data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class budget_activity_list_helper extends SQLiteOpenHelper {
    public static final String BUDGET_ACT_LIST = "Budget_act_list_table";
    //initial db version = 1
    public static final int DB_VERSION = 1;

    public budget_activity_list_helper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory) {
        super(context, "budget_activity_list.db", factory, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL("create table " + BUDGET_ACT_LIST + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,RECORDID TEXT,ACTIVITY TEXT,COMPID TEXT,SUBCOMPID)");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //INSERT DISTRICTS
    public void insert_budget_list(ArrayList<String> id, ArrayList<String> names, ArrayList<String> c,ArrayList<String> d) {
        deleteAll("Budget_act_list_table");
        ContentValues cv = new ContentValues();
        for (int i = 0; i < id.size(); i++) {
            cv.put("RECORDID", id.get(i));
            cv.put("ACTIVITY", names.get(i));
            cv.put("COMPID", c.get(i));
            cv.put("SUBCOMPID", d.get(i));
            this.getWritableDatabase().insertOrThrow("Budget_act_list_table", "", cv);
        }
    }

    //GET ACTIVITIES OF A SUB COMPONENT
    public ArrayList<String> getActivitiesOfSubComponent(String sub_comp_id){
        ArrayList<String> list = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor res = db.rawQuery("select ACTIVITY from Budget_act_list_table where SUBCOMPID='" + sub_comp_id + "'", null);
            while (res.moveToNext()) {
                list.add(res.getString(res.getColumnIndex("ACTIVITY")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public void deleteAll(String table) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(table, null, null);
    }
}
