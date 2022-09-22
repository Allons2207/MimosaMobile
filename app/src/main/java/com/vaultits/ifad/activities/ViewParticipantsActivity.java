package com.vaultits.ifad.activities;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vaultits.ifad.R;
import com.vaultits.ifad.adapters.AppointmentAdapter;
import com.vaultits.ifad.adapters.ParticipantAdapter;
import com.vaultits.ifad.database.captured_data.activities_helper;
import com.vaultits.ifad.logic.Methods;
import com.vaultits.ifad.models.appointment;
import com.vaultits.ifad.models.participant;
import com.vaultits.ifad.retrofit.APIClient;
import com.vaultits.ifad.retrofit.ApiInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViewParticipantsActivity extends AppCompatActivity {

    private RecyclerView mParticipantsRecyclerView;
    private ArrayList<participant> mList;
    private ParticipantAdapter participantAdapter;

    private Dialog mDialog;
    //retrofit
    private ApiInterface apiInterface;
    private activities_helper activities_helper;
    private Handler handler;
    private String mActivityID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            //set layout
            setContentView(R.layout.activity_view_participants);

            mParticipantsRecyclerView = (RecyclerView) findViewById(R.id.participants_list_recycler);
            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            mParticipantsRecyclerView.setLayoutManager(linearLayoutManager);
            mParticipantsRecyclerView.setItemAnimator(new DefaultItemAnimator());

            apiInterface = APIClient.getApiClient().create(ApiInterface.class);
            mDialog = new Dialog(this);
            activities_helper = new activities_helper(this, "", null);

            Intent intent = getIntent();
            mActivityID = intent.getStringExtra("id");
            handler = new Handler();
            //get activities
            getParticipants();
        }catch (Exception e){
            Toast.makeText(this, "Error loading UI", Toast.LENGTH_SHORT).show();
        }
    }

    //get participants
    private void getParticipants(){
        try {
            //get activities
            new Thread(() -> {
                handler.post(() -> {
                    Methods.showDialog(mDialog, "Getting data objects...", true);
                });
                Cursor cursor = activities_helper.getParticipants(mActivityID);
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
                participant app;
                mList = new ArrayList<>();
                while (cursor.moveToNext()) {
                    jsonObject = new JSONObject();
                    app = new participant();
                    try {
                        app.setmID(cursor.getString(cursor.getColumnIndex("ID")));
                        app.setmName(cursor.getString(cursor.getColumnIndex("FIRSTNAME")));
                        app.setmSurname(cursor.getString(cursor.getColumnIndex("SURNAME")));
                        app.setmNationalID(cursor.getString(cursor.getColumnIndex("NATIONALID")));
                        app.setmContact(cursor.getString(cursor.getColumnIndex("CONTACTNO")));
                        app.setmGender(cursor.getString(cursor.getColumnIndex("GENDER")));
                        app.setmDistrict(cursor.getString(cursor.getColumnIndex("DISTRICT")));
                        app.setmWard(cursor.getString(cursor.getColumnIndex("WARD")));
                        app.setmYob(cursor.getString(cursor.getColumnIndex("YOB")));
                        app.setmAppointmentID(cursor.getString(cursor.getColumnIndex("APPOINTMENTID")));
                        app.setmProject(cursor.getString(cursor.getColumnIndex("PROJECT")));

                        mList.add(app);
                    } catch (Exception e) {
                        e.printStackTrace();
                        handler.post(() -> {
                            Methods.Alert("Error", e.toString(), ViewParticipantsActivity.this);
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
                    participantAdapter = new ParticipantAdapter(ViewParticipantsActivity.this,mList);
                    mParticipantsRecyclerView.setAdapter(participantAdapter);
                });
            }).start();
        } catch (Exception e) {
            Methods.showAlert("Error", e.toString(), this);
        }
    }
}