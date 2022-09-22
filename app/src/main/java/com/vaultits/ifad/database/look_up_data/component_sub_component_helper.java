package com.vaultits.ifad.database.look_up_data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class component_sub_component_helper extends SQLiteOpenHelper {

    public static final String COMPONENTS = "Components_table";
    public static final String SUB_COMPONENTS = "Sub_components_table";
    public static final String SUB_COMPONENTS_ACTS = "Sub_components_activities_table";

    //initial db version = 1
    public static final int DB_VERSION = 1;

    public component_sub_component_helper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory) {
        super(context, "components.db", factory, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL("create table " + COMPONENTS + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,COMPID TEXT,DES TEXT)");
            db.execSQL("create table " + SUB_COMPONENTS + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,SUBCOMPID TEXT,COMPID TEXT,DES TEXT)");
            db.execSQL("create table " + SUB_COMPONENTS_ACTS + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,SUBCOMPID TEXT,ACTIVITY TEXT)");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try{
        }catch (Exception e){
            System.out.println(e);
        }
    }

    //GET SUB COMPONENTS OF A COMPONENT
    public ArrayList<String> getSubComponents(String comp_id) {
        ArrayList<String> comp = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor res = db.rawQuery("select DES from Sub_components_table where COMPID='" + comp_id + "'", null);
            while (res.moveToNext()) {
                comp.add(res.getString(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return comp;
    }

    //INSERT COMPONENTS
    public void insert_components(ArrayList<String> id, ArrayList<String> names) {
        deleteAll("Components_table");
        ContentValues cv = new ContentValues();
        for (int i = 0; i < id.size(); i++) {
            cv.put("COMPID", id.get(i));
            cv.put("DES", names.get(i));
            this.getWritableDatabase().insertOrThrow("Components_table", "", cv);
        }
    }

    //INSERT
    public void insert_sub_comp_activities(ArrayList<String> id, ArrayList<String> names) {
        deleteAll("Sub_components_activities_table");
        ContentValues cv = new ContentValues();
        for (int i = 0; i < id.size(); i++) {
            cv.put("SUBCOMPID", id.get(i));
            cv.put("ACTIVITY", names.get(i));
            this.getWritableDatabase().insertOrThrow("Sub_components_activities_table", "", cv);
        }
    }

    //GET ALL activities of sub-component
    public ArrayList<String> getActivitiesOfSubComponent(String sub_comp_id) {
        ArrayList<String> orgs = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor res = db.rawQuery("select ACTIVITY from Sub_components_activities_table WHERE SUBCOMPID='" + sub_comp_id + "'",null);
            while (res.moveToNext()) {
                orgs.add(res.getString(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orgs;
    }

    //INSERT sub comps
    public void insert_sub_comps(ArrayList<String> id, ArrayList<String> names, ArrayList<String> c) {
        deleteAll("Sub_components_table");
        ContentValues cv = new ContentValues();
        for (int i = 0; i < id.size(); i++) {
            cv.put("SUBCOMPID", id.get(i));
            cv.put("COMPID", names.get(i));
            cv.put("DES", c.get(i));
            this.getWritableDatabase().insertOrThrow("Sub_components_table", "", cv);
        }
    }

    //GET COMPONENTS
    public ArrayList<String> getComponents() {
        ArrayList<String> projs = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor res = db.rawQuery("select des from Components_table", null);
            while (res.moveToNext()) {
                projs.add(res.getString(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return projs;
    }

    //GET ALL sub components
    public ArrayList<String> getSubComponents() {
        ArrayList<String> orgs = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor res = db.rawQuery("select des from Sub_components_table", null);
            while (res.moveToNext()) {
                orgs.add(res.getString(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orgs;
    }

    //GET component ID FROM component NAME
    public String componentID(String comp) {
        String id = "";
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor res = db.rawQuery("select COMPID from Components_table where DES='" + comp + "'", null);
            while (res.moveToNext()) {
                id = res.getString(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    //GET sub comp ID FROM sub comp NAME
    public String subComponentId(String sub_comp) {
        String id = "";
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor res = db.rawQuery("select SUBCOMPID from Sub_components_table where des='" + sub_comp + "'", null);
            while (res.moveToNext()) {
                id = res.getString(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    //GET component NAME FROM component ID
    public String getComponent(String comp_id){
        String province = "";
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor res = db.rawQuery("select des from Components_table where COMPID='" + comp_id + "'", null);
            while (res.moveToNext()) {
                province = res.getString(res.getColumnIndex("DES"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return province;
    }

    //GET sub comp NAME FROM sub comp ID
    public String getSubComp(String sub_comp_id){
        String province = "";
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor res = db.rawQuery("select des from Sub_components_table where SUBCOMPID='" + sub_comp_id + "'", null);
            while (res.moveToNext()) {
                province = res.getString(res.getColumnIndex("DES"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return province;
    }

    //get activity des from activity id
    public String getActivityDescription(String id){
        String des = "";
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor res = db.rawQuery("select activity from Sub_components_activities_table where ID='" + id + "'", null);
            while (res.moveToNext()) {
                des = res.getString(res.getColumnIndex("ACTIVITY"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return des;
    }

    public void deleteAll(String table) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(table, null, null);
    }
}
