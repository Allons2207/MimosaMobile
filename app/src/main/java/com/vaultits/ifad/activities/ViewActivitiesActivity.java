package com.vaultits.ifad.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.vaultits.ifad.CapturedDataSyncActivity;
import com.vaultits.ifad.R;
import com.vaultits.ifad.adapters.AppointmentAdapter;
import com.vaultits.ifad.database.captured_data.activities_helper;
import com.vaultits.ifad.logic.Methods;
import com.vaultits.ifad.models.appointment;
import com.vaultits.ifad.retrofit.APIClient;
import com.vaultits.ifad.retrofit.ApiInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewActivitiesActivity extends AppCompatActivity {

    private RecyclerView mActivitiesRecyclerView;
    private ArrayList<appointment> mList;
    private AppointmentAdapter appointmentAdapter;

    private Dialog mDialog;
    //retrofit
    private ApiInterface apiInterface;
    private activities_helper activities_helper;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            //set layout
            setContentView(R.layout.activity_view_activities);

            mActivitiesRecyclerView = (RecyclerView) findViewById(R.id.activities_list_recycler);
            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            mActivitiesRecyclerView.setLayoutManager(linearLayoutManager);
            mActivitiesRecyclerView.setItemAnimator(new DefaultItemAnimator());

            apiInterface = APIClient.getApiClient().create(ApiInterface.class);
            mDialog = new Dialog(this);
            activities_helper = new activities_helper(this, "", null);

            handler = new Handler();
            //get activities
            getAppointments();
        }catch (Exception e){
            Toast.makeText(this, "Error loading UI", Toast.LENGTH_SHORT).show();
        }
    }

    //get appointments
    private void getAppointments(){
        try {
            //get activities
            new Thread(() -> {
                handler.post(() -> {
                    Methods.showDialog(mDialog, "Getting data objects...", true);
                });
                Cursor cursor = activities_helper.getSavedActivities();
                if (cursor == null) {
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Database error, contact admin.", Toast.LENGTH_SHORT).show();
                        Methods.showDialog(mDialog, "Getting data objects...", false);
                    });
                    return;
                }
                JSONObject jsonObject;
                JSONArray jsonArray = new JSONArray();
                int noOfObjects = 0;
                appointment app;
                mList = new ArrayList<>();
                while (cursor.moveToNext()) {
                    jsonObject = new JSONObject();
                    app = new appointment();
                    try {
                        //APPOINTMENT DETAILS
                        //jsonObject.put("SUBJECT", cursor.getString(cursor.getColumnIndex("TOPIC")));
                        String start_date = cursor.getString(cursor.getColumnIndex("START"));
                        String end_date = cursor.getString(cursor.getColumnIndex("ENDDATE"));
                        //jsonObject.put("START", Methods.toSqlDate(start_date));
                        //jsonObject.put("END", Methods.toSqlDate(end_date));
                        //jsonObject.put("USER_ID", cursor.getString(cursor.getColumnIndex("FACILITATOR")));
                        //jsonObject.put("DESCRIPTION", cursor.getString(cursor.getColumnIndex("COMMENT")));
                        //jsonObject.put("ACTIVITY_ID", cursor.getString(cursor.getColumnIndex("ACTIVITY")));
                        //jsonObject.put("ACTIVITY_SITE", cursor.getString(cursor.getColumnIndex("SITE")));
                        //jsonObject.put("STATUS_ID", cursor.getString(cursor.getColumnIndex("STATUS")));
                        //jsonObject.put("PROJECT_ID", cursor.getString(cursor.getColumnIndex("PROJECT")));
                        //jsonObject.put("ORG_ID", cursor.getString(cursor.getColumnIndex("ORG")));
                        //jsonObject.put("DISTRICT_ID", cursor.getString(cursor.getColumnIndex("DIS")));
                        //jsonObject.put("SCHEME", cursor.getString(cursor.getColumnIndex("SCHEME")));

                        app.setmTopic(cursor.getString(cursor.getColumnIndex("TOPIC")));
                        app.setmSite(cursor.getString(cursor.getColumnIndex("SITE")));
                        app.setmStart(cursor.getString(cursor.getColumnIndex("START")));
                        app.setmEnd(cursor.getString(cursor.getColumnIndex("ENDDATE")));

                        app.setmID(cursor.getString(cursor.getColumnIndex("ID")));
                        app.setmProject(cursor.getString(cursor.getColumnIndex("PROJECT")));
                        app.setmOrganisation(cursor.getString(cursor.getColumnIndex("ORG")));
                        app.setmComponent(cursor.getString(cursor.getColumnIndex("COMP")));
                        app.setmSubComponent(cursor.getString(cursor.getColumnIndex("SUBCOMP")));
                        app.setmActivity(cursor.getString(cursor.getColumnIndex("ACTIVITY")));
                        app.setmStatus(cursor.getString(cursor.getColumnIndex("STATUS")));
                        app.setmDistrict(cursor.getString(cursor.getColumnIndex("DIS")));
                        app.setmFacilitator(cursor.getString(cursor.getColumnIndex("FACILITATOR")));
                        app.setmComment(cursor.getString(cursor.getColumnIndex("COMMENT")));
                        app.setmDateCaptured(cursor.getString(cursor.getColumnIndex("DATECAPTURED")));
                        app.setmWhoCaptured(cursor.getString(cursor.getColumnIndex("WHOCAPTURED")));
                        app.setmScheme(cursor.getString(cursor.getColumnIndex("SCHEME")));

                        mList.add(app);
                    } catch (Exception e) {
                        e.printStackTrace();
                        handler.post(() -> {
                            Methods.Alert("Error", e.toString(), ViewActivitiesActivity.this);
                        });
                    }
                    try {
                        jsonArray.put(noOfObjects, jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    noOfObjects = +1;
                }
                int finalNoOfObjects = noOfObjects;
                handler.post(() -> {
                    Methods.showDialog(mDialog, "Getting data objects...", false);
                    appointmentAdapter = new AppointmentAdapter(ViewActivitiesActivity.this,mList);
                    mActivitiesRecyclerView.setAdapter(appointmentAdapter);
                });
            }).start();
        } catch (Exception e) {
            Methods.showAlert("Error", e.toString(), this);
        }
    }
}