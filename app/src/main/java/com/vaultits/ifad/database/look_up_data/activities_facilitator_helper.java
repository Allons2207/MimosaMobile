package com.vaultits.ifad.database.look_up_data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.vaultits.ifad.logic.Methods;

import java.util.ArrayList;

public class activities_facilitator_helper extends SQLiteOpenHelper {

    public static final String ACTIVITY = "Activities_table";
    public static final String ACTIVITY_STATUS = "Activity_status_table";
    public static final String TRIPS = "Trips_table";
    public static final String TRIP = "Trip";
    public static final String USERS = "Users_table";

    //initial db version = 1
    public static final int DB_VERSION = 1;

    public activities_facilitator_helper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory) {
        super(context, "activities_facilitator.db", factory, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL("create table " + ACTIVITY + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,ACTID TEXT,DES TEXT)");
            db.execSQL("create table " + ACTIVITY_STATUS + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,ACTSTATUSID TEXT,DES TEXT)");
            db.execSQL("create table " + USERS + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,FACID TEXT,NAME TEXT,SURNAME TEXT)");
            db.execSQL("create table " + TRIPS + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,DEST TEXT,DRIVER TEXT,DEPART TEXT,TRIP TEXT,SECUR TEXT,PREUSE TEXT,PRETRIP TEXT)");

            db.execSQL("create table " + TRIP + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,CURRMILE TEXT,CURRLOC TEXT,ARRTIME TEXT,DEPTTIME TEXT,TRIP TEXT,USERID TEXT,MILE TEXT)");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }









    //INSERT USERS
    public void insert_users(ArrayList<String> id, ArrayList<String> names, ArrayList<String> c) {
        deleteAll("Users_table");
        ContentValues cv = new ContentValues();
        for (int i = 0; i < id.size(); i++) {

            this.getWritableDatabase().insertOrThrow("Users_table", "", cv);
        }
    }

    public void insert_trip(ArrayList<String>  a, ArrayList<String> b, ArrayList<String> c ,ArrayList<String> d ,ArrayList<String> e , ArrayList<String> f , ArrayList<String>g) {
        deleteAll("Trips_table");
        ContentValues cv = new ContentValues();
        for (int i = 0; i < d.size(); i++) {
            cv.put("DEST", a.get(i));
            cv.put("DRIVER", b.get(i));
            cv.put("DEPART", c.get(i));
            cv.put("TRIP", d.get(i));
            cv.put("SECUR", e.get(i));
            cv.put("PREUSE", f.get(i));
            cv.put("PRETRIP", c.get(i));
            this.getWritableDatabase().insertOrThrow("Trips_table", "", cv);
        }
    }










    //INSERTING PARTICIPANT
    public  String inserttrip(String currloc, String currmile, String arrtime, String depttime, String trip, String userid, String mile) {
        String result;
        long ins;
        try {
            //first check existence
            SQLiteDatabase db = activities_facilitator_helper.this.getWritableDatabase();
                ContentValues contentValues = new ContentValues();

                contentValues.put("CURRMILE",currloc);
                contentValues.put("CURRLOC",  currmile);
                contentValues.put("ARRTIME", arrtime);
                contentValues.put("DEPTTIME", depttime);
                contentValues.put("TRIP", trip);
                contentValues.put("USERID", userid);
                contentValues.put("MILE", mile);
                ins = db.insert(TRIP, null, contentValues);
                db.close();
                if (ins == -1) {
                    result = "Error";
                } else {
                    result = "Success";
                }

        } catch (Exception e) {
            result = "Error";
        }
        return result;
    }

    public Cursor getSavedActivities() {
        Cursor cursor = null;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "SELECT * FROM Trips_table";
            cursor = db.rawQuery(query, null);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return cursor;
    }


    //INSERT ACTIVITIES
    public void insert_activities(ArrayList<String> id, ArrayList<String> names) {
        deleteAll("Activities_table");
        ContentValues cv = new ContentValues();
        for (int i = 0; i < id.size(); i++) {
            cv.put("ACTID", id.get(i));
            cv.put("DES", names.get(i));
            this.getWritableDatabase().insertOrThrow("Activities_table", "", cv);
        }
    }

    //INSERT ACTIVITY STATUS
    public void insert_activity_status(ArrayList<String> id, ArrayList<String> names) {
        deleteAll("Activity_status_table");
        ContentValues cv = new ContentValues();
        for (int i = 0; i < id.size(); i++) {
            cv.put("ACTSTATUSID", id.get(i));
            cv.put("DES", names.get(i));
            this.getWritableDatabase().insertOrThrow("Activity_status_table", "", cv);
        }
    }

    //GET ACTIVITIES
    public ArrayList<String> getActivities() {
        ArrayList<String> provs = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor res = db.rawQuery("select DES from Activities_table", null);
            while (res.moveToNext()) {
                provs.add(res.getString(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return provs;
    }

    //GET ALL ACTIVITY STATUS
    public ArrayList<String> getActivityStatuses() {
        ArrayList<String> dis = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor res = db.rawQuery("select DES from Activity_status_table", null);
            while (res.moveToNext()) {
                dis.add(res.getString(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dis;
    }

    //GET ALL USERS
    public ArrayList<String> getUsers() {
        ArrayList<String> dis = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor res = db.rawQuery("select NAME,SURNAME from Users_table", null);
            while (res.moveToNext()) {
                dis.add(res.getString(0) + " " + res.getString(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dis;
    }

    //GET ACTIVITY ID FROM DES
    public String getActivityId(String des) {
        String dis = "";
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor res = db.rawQuery("select ACTID from Activities_table where DES='" + des + "'", null);
            while (res.moveToNext()) {
                dis = res.getString(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dis;
    }
    public String getActivityDescription(String id){
        String des = "";
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor res = db.rawQuery("select DES from Activities_table  where ACTID='" + id + "'", null);
            while (res.moveToNext()) {
                des = res.getString(res.getColumnIndex("DES"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return des;
    }

    //GET ACTIVITY status ID FROM DES
    public String getActivityStatusId(String des) {
        String dis = "";
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor res = db.rawQuery("select ACTSTATUSID from Activity_status_table where DES='" + des + "'", null);
            while (res.moveToNext()) {
                dis = res.getString(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dis;
    }

    //GET ACTIVITY status ID FROM DES
    public String getActivityStatus(String id) {
        String dis = "";
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor res = db.rawQuery("select DES from Activity_status_table where ID='" + id + "'", null);
            while (res.moveToNext()) {
                dis = res.getString(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dis;
    }

    //GET USER ID FROM NAME AND SURNAME
    public String getUserId(String des) {
        String dis = "";
        String[] tokens = des.split(" ");
        String name = tokens[0];
        String surname = tokens[1];
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor res = db.rawQuery("select FACID from Users_table where NAME ='" + name + "' AND SURNAME='" + surname + "'", null);
            while (res.moveToNext()) {
                dis = res.getString(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dis;
    }

    //get name and surname from id
    public String getFullName(String id) {
        String full_name = "";
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor res = db.rawQuery("select NAME,SURNAME from Users_table where FACID ='" + id + "'", null);
            while (res.moveToNext()) {
                full_name = res.getString(0) + " " + res.getString(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return full_name;
    }

    public void deleteAll(String table) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(table, null, null);
    }
}
