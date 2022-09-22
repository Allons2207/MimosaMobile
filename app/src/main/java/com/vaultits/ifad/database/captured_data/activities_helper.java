package com.vaultits.ifad.database.captured_data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.vaultits.ifad.database.look_up_data.activities_facilitator_helper;
import com.vaultits.ifad.logic.Methods;

import java.util.ArrayList;

public class activities_helper extends SQLiteOpenHelper {

    public static final String ACTIVITIES = "Activities_table";//appointments table
    public static final String PARTICIPANTS = "Participants_table";//participants table
    public static final String FILES = "Activity_files_table";//files table
    public static final String TRIP = "Trip";

    //initial db version = 1
    //public static final int DB_VERSION = 1;
    //public static final int DB_VERSION = 2;
    public static final int DB_VERSION = 3;

    public activities_helper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory) {
        super(context, "activities_data.db", factory, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL("create table " + ACTIVITIES + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,PROJECT TEXT,ORG TEXT,COMP TEXT,SUBCOMP TEXT,ACTIVITY TEXT,STATUS TEXT,DIS TEXT,SITE TEXT,FACILITATOR TEXT,TOPIC TEXT," +
                    "COMMENT TEXT,START TEXT,ENDDATE TEXT,DATECAPTURED TEXT,WHOCAPTURED TEXT,SCHEME TEXT)");
            db.execSQL("create table " + PARTICIPANTS + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,PROJECT TEXT,COMP TEXT,SUBCOMP TEXT,ACTIVITY TEXT,DISTRICT TEXT,WARD TEXT,FIRSTNAME TEXT,SURNAME TEXT,NATIONALID TEXT," +
                    "GENDER TEXT,YOB TEXT,CONTACTNO TEXT,APPOINTMENTID TEXT,CREATEDBY TEXT)");
            db.execSQL("create table " + FILES + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,FILETYPEID TEXT,APPOINTMENTID TEXT,TITLE TEXT,AUTHOR TEXT,DESCRIPTION TEXT,DATE TEXT,AUTHORORG TEXT,DOCPATH TEXT,DOCURL TEXT,CREATEDBY TEXT,SYNCSTATUS TEXT)");
            db.execSQL("create table " + TRIP + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,CURRMILE TEXT,CURRLOC TEXT,ARRTIME TEXT,DEPTTIME TEXT,TRIP TEXT,USERID TEXT,MILE TEXT)");

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

    //INSERTING ACTIVITY
    public String insertActivity(String proj, String org, String comp, String sub_comp, String activity, String status, String dis, String site, String user, String topic, String comment, String start, String end, String date_captured, String who_captured, String scheme) {
        String result;
        long ins;
        try {
            //first check existence
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor res = db.rawQuery("SELECT * FROM '" + ACTIVITIES + "' WHERE ACTIVITY ='" + activity + "' AND TOPIC = '" + topic + "' AND START = '" + start + "' AND ENDDATE = '" + end + "'", null);
            if (res.moveToNext()) {
                result = "Exist";
            } else {
                ContentValues contentValues = new ContentValues();

                contentValues.put("PROJECT", proj);
                contentValues.put("ORG", org);
                contentValues.put("COMP", comp);
                contentValues.put("SUBCOMP", sub_comp);
                contentValues.put("ACTIVITY", activity);
                contentValues.put("STATUS", status);
                contentValues.put("DIS", dis);
                contentValues.put("SITE", site);
                contentValues.put("FACILITATOR", user);
                contentValues.put("TOPIC", topic);
                contentValues.put("COMMENT", comment);
                contentValues.put("START", start);
                contentValues.put("ENDDATE", end);
                contentValues.put("DATECAPTURED", date_captured);
                contentValues.put("WHOCAPTURED", who_captured);
                contentValues.put("SCHEME", scheme);

                ins = db.insert(ACTIVITIES, null, contentValues);
                db.close();
                if (ins == -1) {
                    result = "Error";
                } else {
                    result = "Success";
                }
            }

        } catch (Exception e) {
            result = "Error";
        }
        return result;
    }





    //INSERTING PARTICIPANT
    public String inserttrip(String currloc, String currmile, String arrtime, String depttime, String trip, String userid, String mile) {
        String result;
        long ins = 0;
        try {
            //first check existence
            SQLiteDatabase db = this.getWritableDatabase();
            try {
                ;
                ContentValues contentValues = new ContentValues();

                contentValues.put("CURRMILE", currloc);
                contentValues.put("CURRLOC", currmile);
                contentValues.put("ARRTIME", arrtime);
                contentValues.put("DEPTTIME", depttime);
                contentValues.put("TRIP", trip);
                contentValues.put("USERID", userid);
                contentValues.put("MILE", mile);
                ins = db.insert(TRIP, null, contentValues);
                db.close();
            }
            catch (Exception k){

            }
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





    //INSERTING PARTICIPANT
    public String insertParticipant(String project, String comp, String sub_comp, String activity, String dis, String ward, String name, String surname, String id, String sex, String yob, String number, String appointment_id, Context ctx) {
        String result;
        long ins;
        try {
            //first check existence
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor res = db.rawQuery("SELECT * FROM '" + PARTICIPANTS + "' WHERE NATIONALID ='" + id + "' AND APPOINTMENTID='" + appointment_id + "'", null);
            if (res.moveToNext()) {
                result = "Exist";
            } else {
                ContentValues contentValues = new ContentValues();

                contentValues.put("PROJECT", project);
                contentValues.put("COMP", comp);
                contentValues.put("SUBCOMP", sub_comp);
                contentValues.put("ACTIVITY", activity);
                contentValues.put("DISTRICT", dis);
                contentValues.put("WARD", ward);
                contentValues.put("FIRSTNAME", name);
                contentValues.put("SURNAME", surname);
                contentValues.put("NATIONALID", id);
                contentValues.put("GENDER", sex);
                contentValues.put("YOB", yob);
                contentValues.put("CONTACTNO", number);
                contentValues.put("APPOINTMENTID", appointment_id);
                contentValues.put("CREATEDBY", Methods.getUserId(ctx));
                //contentValues.put("DATEREGISTERED", Methods.getDateForSqlServer());

                ins = db.insert(PARTICIPANTS, null, contentValues);
                db.close();
                if (ins == -1) {
                    result = "Error";
                } else {
                    result = "Success";
                }
            }

        } catch (Exception e) {
            result = "Error";
        }
        return result;
    }

    //INSERTING FILE
    public String insertFile(String type, String appointment_id, String title, String author, String des, String date, String author_org, String file_path, String file_url, Context ctx) {
        String result;
        long ins;
        try {
            //first check existence
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor res = db.rawQuery("SELECT * FROM '" + FILES + "' WHERE APPOINTMENTID ='" + appointment_id + "' AND DOCPATH = '" + file_path + "'", null);
            if (res.moveToNext()) {
                result = "Exist";
            } else {
                ContentValues contentValues = new ContentValues();

                contentValues.put("FILETYPEID", type);
                contentValues.put("APPOINTMENTID", appointment_id);
                contentValues.put("TITLE", title);
                contentValues.put("AUTHOR", author);
                contentValues.put("DESCRIPTION", des);
                contentValues.put("DATE", date);
                contentValues.put("AUTHORORG", author_org);
                contentValues.put("DOCPATH", file_path);
                contentValues.put("DOCURL", file_url);
                contentValues.put("CREATEDBY", Methods.getUserId(ctx));
                contentValues.put("SYNCSTATUS", "No");

                ins = db.insert(FILES, null, contentValues);
                db.close();
                if (ins == -1) {
                    result = "Error";
                } else {
                    result = "Success";
                }
            }

        } catch (Exception e) {
            System.out.println(e);
            result = "Error";
        }
        return result;
    }

    //DELETE FILE ENTRY
    public boolean deleteFile(String file_id, Context ctx) {
        boolean del = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            int res = db.delete(FILES, "ID = ?", new String[]{file_id});
            if (res > 0) {
                del = true;
            } else {
                del = false;
            }
        } catch (Exception e) {
            Toast.makeText(ctx, "Error could not delete.", Toast.LENGTH_SHORT).show();
        }
        return del;
    }

    //UPDATE FILE SYNC STATUS
    public boolean updateFileSyncStatus(String file_id, Context ctx) {
        boolean result;
        long ins;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("SYNCSTATUS", "Yes");
            ins = db.update(FILES, contentValues, "ID = ?", new String[]{file_id});
            db.close();
            if (ins > 0) {
                result = true;
            } else {
                result = false;
            }

        } catch (Exception e) {
            result = false;
            Toast.makeText(ctx, "Error updating file sync status", Toast.LENGTH_SHORT).show();
        }
        return result;
    }

    //UPDATING PARTICIPANT
    public boolean updateParticipant(String project, String comp, String sub_comp, String activity, String dis, String ward, String name, String surname, String nat_id, String sex, String yob, String number, String appointment_id, Context ctx, String participant_id) {
        boolean result;
        long ins;
        try {

            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("PROJECT", project);
            contentValues.put("COMP", comp);
            contentValues.put("SUBCOMP", sub_comp);
            contentValues.put("ACTIVITY", activity);
            contentValues.put("DISTRICT", dis);
            contentValues.put("WARD", ward);
            contentValues.put("FIRSTNAME", name);
            contentValues.put("SURNAME", surname);
            contentValues.put("NATIONALID", nat_id);
            contentValues.put("GENDER", sex);
            contentValues.put("YOB", yob);
            contentValues.put("CONTACTNO", number);
            contentValues.put("APPOINTMENTID", appointment_id);
            contentValues.put("CREATEDBY", Methods.getUserId(ctx));

            ins = db.update(PARTICIPANTS, contentValues, "ID = ?", new String[]{participant_id});
            db.close();
            if (ins > 0) {
                result = true;
            } else {
                result = false;
            }
        } catch (Exception e) {
            result = false;
        }
        return result;
    }

    //UPDATING ACTIVITY
    public boolean updateActivity(String proj, String org, String comp, String sub_comp, String activity, String status, String dis, String site, String user, String topic, String comment, String start, String end, String date_captured, String who_captured, String scheme, String appointment_id) {
        boolean result;
        long ins;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("PROJECT", proj);
            contentValues.put("ORG", org);
            contentValues.put("COMP", comp);
            contentValues.put("SUBCOMP", sub_comp);
            contentValues.put("ACTIVITY", activity);
            contentValues.put("STATUS", status);
            contentValues.put("DIS", dis);
            contentValues.put("SITE", site);
            contentValues.put("FACILITATOR", user);
            contentValues.put("TOPIC", topic);
            contentValues.put("COMMENT", comment);
            contentValues.put("START", start);
            contentValues.put("ENDDATE", end);
            contentValues.put("DATECAPTURED", date_captured);
            contentValues.put("WHOCAPTURED", who_captured);
            contentValues.put("SCHEME", scheme);

            ins = db.update(ACTIVITIES, contentValues, "ID = ?", new String[]{appointment_id});
            db.close();
            if (ins > 0) {
                result = true;
            } else {
                result = false;
            }

        } catch (Exception e) {
            result = false;
        }
        return result;
    }

    //delete participant
    public boolean deleteParticipant(String id, String nat_id) {
        boolean result = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            int res = db.delete(PARTICIPANTS, "ID = ? AND NATIONALID = ?", new String[]{id, nat_id});
            if (res > 0) {
                result = true;
            } else {
                result = false;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return result;
    }

    //delete participant
    public boolean deleteAppointment(String id) {
        boolean result = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            int res = db.delete(ACTIVITIES, "ID = ?", new String[]{id});
            if (res > 0) {
                result = true;
            } else {
                result = false;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return result;
    }

    //GET ACTIVITIES
    public Cursor getSavedActivities() {
        Cursor cursor = null;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "SELECT * FROM Activities_table";
            cursor = db.rawQuery(query, null);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return cursor;
    }

    //GET ACTIVITIES
    public Cursor getParticipants(String activity_id) {
        Cursor cursor = null;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "SELECT * FROM Participants_table WHERE APPOINTMENTID = '" + activity_id + "'";
            cursor = db.rawQuery(query, null);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return cursor;
    }

    //get appointments
    public ArrayList<String> getAppointments() {
        ArrayList<String> appointments = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor res = db.rawQuery("select TOPIC from Activities_table", null);
            while (res.moveToNext()) {
                appointments.add(res.getString(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appointments;
    }

    //GET APPOINTMENT ID
    public String getAppointmentID(String topic) {
        String id = "";
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor res = db.rawQuery("select ID from Activities_table where TOPIC='" + topic + "'", null);
            while (res.moveToNext()) {
                id = res.getString(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    //get appointment des from id
    public String getAppointment(String topic) {
        String app = "";
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor res = db.rawQuery("select TOPIC from Activities_table where ID='" + topic + "'", null);
            while (res.moveToNext()) {
                app = res.getString(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return app;
    }

    //get activity participation
    public Cursor getActivityParticipation() {
        Cursor cursor = null;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "SELECT * from Trip";
            cursor = db.rawQuery(query, null);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return cursor;
    }

    //GET FILES WITH APPOINTMENT INFO
    public Cursor getFileInfo(String file_id) {
        Cursor cursor = null;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "SELECT Activity_files_table.FILETYPEID,Activity_files_table.TITLE,Activity_files_table.AUTHOR," +
                    "Activity_files_table.DESCRIPTION,Activity_files_table.DATE,Activity_files_table.AUTHORORG," +
                    "Activity_files_table.CREATEDBY,Activity_files_table.DOCPATH,Activity_files_table.DOCURL," +
                    "Activities_table.TOPIC,Activities_table.START,Activities_table.ENDDATE," +
                    "Activities_table.FACILITATOR,Activities_table.COMMENT,Activities_table.ACTIVITY," +
                    "Activities_table.SITE,Activities_table.STATUS,Activities_table.PROJECT," +
                    "Activities_table.ORG,Activities_table.DIS,Activities_table.SCHEME FROM Activity_files_table INNER JOIN Activities_table WHERE Activity_files_table.APPOINTMENTID = Activities_table.ID AND Activity_files_table.ID = '" + file_id + "'";
            cursor = db.rawQuery(query, null);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return cursor;
    }

    //GET FILES
    public Cursor getFiles(String activity_id) {
        Cursor cursor = null;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.rawQuery("SELECT * FROM Activity_files_table WHERE APPOINTMENTID ='" + activity_id + "'", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursor;
    }
}
