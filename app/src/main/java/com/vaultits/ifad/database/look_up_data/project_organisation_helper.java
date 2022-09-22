package com.vaultits.ifad.database.look_up_data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class project_organisation_helper extends SQLiteOpenHelper {

    public static final String PROJECTS = "Projects_table";
    public static final String ORGANISATIONS = "Organisations_table";
    public static final String PROJECT_DISTRICTS = "Project_Districts_table";
    public static final String IRRIGATION_SCHEMES = "Irrigations_table";

    //initial db version = 1
    public static final int DB_VERSION = 1;

    public project_organisation_helper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory) {
        super(context, "ProjectOrganisation.db", factory, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL("create table " + PROJECTS + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,PROJECTID TEXT,NAME TEXT,ACRONYM TEXT)");
            db.execSQL("create table " + ORGANISATIONS + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,ORGID TEXT,NAME TEXT,DES TEXT)");
            db.execSQL("create table " + PROJECT_DISTRICTS + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,PROJECTID TEXT,DISTRICTID TEXT)");
            db.execSQL("create table " + IRRIGATION_SCHEMES + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,SCHEMEID TEXT,NAME TEXT)");
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

    //INSERT projects
    public void insert_projects(ArrayList<String> id, ArrayList<String> names, ArrayList<String> c) {
        deleteAll("Projects_table");
        ContentValues cv = new ContentValues();
        for (int i = 0; i < id.size(); i++) {
            cv.put("PROJECTID", id.get(i));
            cv.put("NAME", names.get(i));
            cv.put("ACRONYM", c.get(i));
            this.getWritableDatabase().insertOrThrow("Projects_table", "", cv);
        }
    }

    //INSERT organisations
    public void insert_organisations(ArrayList<String> id, ArrayList<String> names, ArrayList<String> c) {
        deleteAll("Organisations_table");
        ContentValues cv = new ContentValues();
        for (int i = 0; i < id.size(); i++) {
            cv.put("ORGID", id.get(i));
            cv.put("NAME", names.get(i));
            cv.put("DES", c.get(i));
            this.getWritableDatabase().insertOrThrow("Organisations_table", "", cv);
        }
    }

    //INSERT IRRIGATION SCHEMES
    public void insert_irrigation_schemes(ArrayList<String> id, ArrayList<String> names) {
        deleteAll("Irrigations_table");
        ContentValues cv = new ContentValues();
        for (int i = 0; i < id.size(); i++) {
            cv.put("SCHEMEID", id.get(i));
            cv.put("NAME", names.get(i));
            this.getWritableDatabase().insertOrThrow("Irrigations_table", "", cv);
        }
    }

    //INSERT PROJECT DISTRICTS
    public void insert_project_districts(ArrayList<String> id, ArrayList<String> names) {
        deleteAll("Project_Districts_table");
        ContentValues cv = new ContentValues();
        for (int i = 0; i < id.size(); i++) {
            cv.put("PROJECTID", id.get(i));
            cv.put("DISTRICTID", names.get(i));
            this.getWritableDatabase().insertOrThrow("Project_Districts_table", "", cv);
        }
    }

    //GET SCHEME ID FROM NAME
    public String getSchemeId(String scheme) {
        String id = "";
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor res = db.rawQuery("select SCHEMEID from Irrigations_table where NAME='" + scheme + "'", null);
            while (res.moveToNext()) {
                id = res.getString(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    //GET PROJECTS
    public ArrayList<String> getProjects() {
        ArrayList<String> projs = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor res = db.rawQuery("select Name from Projects_table", null);
            while (res.moveToNext()) {
                projs.add(res.getString(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return projs;
    }

    //GET schemes
    public ArrayList<String> getSchemes() {
        ArrayList<String> projs = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor res = db.rawQuery("select Name from Irrigations_table", null);
            while (res.moveToNext()) {
                projs.add(res.getString(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return projs;
    }

    //GET ALL ORGANISATIONS
    public ArrayList<String> getOrganisations() {
        ArrayList<String> orgs = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor res = db.rawQuery("select Name from Organisations_table", null);
            while (res.moveToNext()) {
                orgs.add(res.getString(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orgs;
    }

    //get district ids of a project
    public ArrayList<String> getDistrictIds(String proj_id) {
        ArrayList<String> list = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor res = db.rawQuery("select DISTRICTID from Project_Districts_table where PROJECTID='" + proj_id + "'", null);
            while (res.moveToNext()) {
                list.add(res.getString(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    //GET project ID FROM project NAME
    public String projectID(String proj) {
        String id = "";
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor res = db.rawQuery("select PROJECTID from Projects_table where NAME='" + proj + "'", null);
            while (res.moveToNext()) {
                id = res.getString(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    //GET org ID FROM org NAME
    public String organisationID(String org) {
        String id = "";
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor res = db.rawQuery("select ORGID from Organisations_table where NAME='" + org + "'", null);
            while (res.moveToNext()) {
                id = res.getString(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    //GET project NAME FROM project ID
    public String getProjectName(String project_id){
        String province = "";
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor res = db.rawQuery("select NAME from Projects_table where PROJECTID='" + project_id + "'", null);
            while (res.moveToNext()) {
                province = res.getString(res.getColumnIndex("NAME"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return province;
    }

    //GET organisation NAME FROM organisation ID
    public String getOrganisationName(String org_id){
        String province = "";
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor res = db.rawQuery("select NAME from Organisations_table where ORGID='" + org_id + "'", null);
            while (res.moveToNext()) {
                province = res.getString(res.getColumnIndex("NAME"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return province;
    }

    //get scheme from id
    public String getSchemeName(String id){
        String scheme = "";
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor res = db.rawQuery("select NAME from Irrigations_table where SCHEMEID='" + id + "'", null);
            while (res.moveToNext()) {
                scheme = res.getString(res.getColumnIndex("NAME"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return scheme;
    }

    public void deleteAll(String table) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(table, null, null);
    }
}
