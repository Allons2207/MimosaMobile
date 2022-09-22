package com.vaultits.ifad.database.look_up_data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class prov_dis_helper extends SQLiteOpenHelper {

    public static final String PROVINCES = "Province_table";
    public static final String DISTRICTS = "Districts_table";
    public static final String WARDS = "Wards_table";
    public static final String GENDERS = "Genders_table";

    //initial db version = 1
    public static final int DB_VERSION = 1;

    public prov_dis_helper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory) {
        super(context, "location.db", factory, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL("create table " + PROVINCES + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,PROVINCEID TEXT,NAME TEXT,COUNTRYID TEXT)");
            db.execSQL("create table " + DISTRICTS + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,DISTRICTID TEXT,NAME TEXT,PROVINCEID TEXT)");
            db.execSQL("create table " + WARDS + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,WARDID TEXT,NAME TEXT,DISID TEXT)");
            db.execSQL("create table " + GENDERS + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,GENDERID TEXT,GENDER TEXT)");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    //INSERT PROVINCES
    public void insert_provinces(ArrayList<String> id, ArrayList<String> names, ArrayList<String> c) {
        deleteAll("Province_table");
        ContentValues cv = new ContentValues();
        for (int i = 0; i < id.size(); i++) {
            cv.put("PROVINCEID", id.get(i));
            cv.put("NAME", names.get(i));
            cv.put("COUNTRYID", c.get(i));
            this.getWritableDatabase().insertOrThrow("Province_table", "", cv);
        }
    }

    //INSERT DISTRICTS
    public void insert_districts(ArrayList<String> id, ArrayList<String> names, ArrayList<String> c) {
        deleteAll("Districts_table");
        ContentValues cv = new ContentValues();
        for (int i = 0; i < id.size(); i++) {
            cv.put("DISTRICTID", id.get(i));
            cv.put("NAME", names.get(i));
            cv.put("PROVINCEID", c.get(i));
            this.getWritableDatabase().insertOrThrow("Districts_table", "", cv);
        }
    }

    //INSERT wards
    public void insert_wards(ArrayList<String> id, ArrayList<String> names, ArrayList<String> c) {
        deleteAll("Wards_table");
        ContentValues cv = new ContentValues();
        for (int i = 0; i < id.size(); i++) {
            cv.put("WARDID", id.get(i));
            cv.put("NAME", names.get(i));
            cv.put("DISID", c.get(i));
            this.getWritableDatabase().insertOrThrow("Wards_table", "", cv);
        }
    }


    //INSERT gender
    public void insert_gender(ArrayList<String> id, ArrayList<String> names) {
        deleteAll("Genders_table");
        ContentValues cv = new ContentValues();
        for (int i = 0; i < id.size(); i++) {
            cv.put("GENDERID", id.get(i));
            cv.put("GENDER", names.get(i));
            this.getWritableDatabase().insertOrThrow("Genders_table", "", cv);
        }
    }

    //GET PROVINCES
    public ArrayList<String> getProvinces() {
        ArrayList<String> provs = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor res = db.rawQuery("select Name from Province_table", null);
            while (res.moveToNext()) {
                provs.add(res.getString(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return provs;
    }

    //GET ALL DISTRICTS
    public ArrayList<String> getDistricts() {
        ArrayList<String> dis = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor res = db.rawQuery("select Name from Districts_table", null);
            while (res.moveToNext()) {
                dis.add(res.getString(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dis;
    }

    //GET DISTRICTS IN A PROVINCE
    public ArrayList<String> getDistricts(String prov) {
        ArrayList<String> dis = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor res = db.rawQuery("select NAME from Districts_table where PROVINCEID='" + prov + "'", null);
            while (res.moveToNext()) {
                dis.add(res.getString(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dis;
    }

    //GET wards in a district
    public ArrayList<String> getWardsOfDistrict(String dis_id) {
        ArrayList<String> dis = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor res = db.rawQuery("select NAME from Wards_table where DISID='" + dis_id + "'", null);
            while (res.moveToNext()) {
                dis.add(res.getString(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dis;
    }

    //get districts if project
    public ArrayList<String> getDistrictsOfProject(ArrayList<String> list) {
        ArrayList<String> dis = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            for (String id : list) {
                Cursor res = db.rawQuery("select NAME from Districts_table where DISTRICTID='" + id + "'", null);
                while (res.moveToNext()) {
                    dis.add(res.getString(0));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dis;
    }

    public ArrayList<String> getGenders() {
        ArrayList<String> dis = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor res = db.rawQuery("select GENDER from Genders_table", null);
            while (res.moveToNext()) {
                dis.add(res.getString(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dis;
    }

    //GET PROVINCE ID FROM PROVINCE NAME
    public String provID(String prov) {
        String id = "";
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor res = db.rawQuery("select PROVINCEID from Province_table where NAME='" + prov + "'", null);
            while (res.moveToNext()) {
                id = res.getString(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    //GET DISTRICT ID FROM DISTRICT NAME
    public String disID(String dis) {
        String id = "";
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor res = db.rawQuery("select DISTRICTID from Districts_table where NAME='" + dis + "'", null);
            while (res.moveToNext()) {
                id = res.getString(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    //get ward name from id
    public String getWardName(String id) {
        String name = "";
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor res = db.rawQuery("select NAME from Wards_table where WARDID='" + id + "'", null);
            while (res.moveToNext()) {
                name = res.getString(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }

    //ward id
    public String getWardId(String ward, String dis_id) {
        String id = "";
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor res = db.rawQuery("select WARDID from Wards_table where NAME='" + ward + "' AND DISID='" + dis_id + "'", null);
            while (res.moveToNext()) {
                id = res.getString(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    //GET PROVINCE NAME FROM PROVINCE ID
    public String getProvinceName(String province_id) {
        String province = "";
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor res = db.rawQuery("select NAME from Province_table where PROVINCEID='" + province_id + "'", null);
            while (res.moveToNext()) {
                province = res.getString(res.getColumnIndex("NAME"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return province;
    }

    //GET DISTRICT NAME FROM DISTRICT ID
    public String getDistrictName(String district_id) {
        String province = "";
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor res = db.rawQuery("select NAME from Districts_table where DISTRICTID='" + district_id + "'", null);
            while (res.moveToNext()) {
                province = res.getString(res.getColumnIndex("NAME"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return province;
    }

    public void deleteAll(String table) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(table, null, null);
    }
}
