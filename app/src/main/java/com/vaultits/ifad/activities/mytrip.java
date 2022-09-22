package com.vaultits.ifad.activities;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonParser;
import com.vaultits.ifad.CapturedDataSyncActivity;
import com.vaultits.ifad.LookUpDataSyncActivity;
import com.vaultits.ifad.MainActivity;
import com.vaultits.ifad.R;
import com.vaultits.ifad.adapters.AppointmentAdapter;
import com.vaultits.ifad.adapters.TripAdpater;
import com.vaultits.ifad.database.look_up_data.activities_facilitator_helper;
import com.vaultits.ifad.logic.Methods;
import com.vaultits.ifad.models.appointment;
import com.vaultits.ifad.navigation.MainNavigationDrawerActivity;
import com.vaultits.ifad.retrofit.APIClient;
import com.vaultits.ifad.retrofit.ApiInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class mytrip extends AppCompatActivity {

    private RecyclerView mActivitiesRecyclerView;
    private ArrayList<appointment> mList;
    private TripAdpater appointmentAdapter;

    private Dialog mDialog;
    //retrofit
    private ApiInterface apiInterface;
    private activities_facilitator_helper activities_helper;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            //set layout
            setContentView(R.layout.activity_mytrip);

            mActivitiesRecyclerView = (RecyclerView) findViewById(R.id.activities_trip_recycler);
            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            mActivitiesRecyclerView.setLayoutManager(linearLayoutManager);
            mActivitiesRecyclerView.setItemAnimator(new DefaultItemAnimator());

            apiInterface = APIClient.getApiClient().create(ApiInterface.class);
            mDialog = new Dialog(this);
            activities_helper = new activities_facilitator_helper(this, "", null);

            handler = new Handler();
            //get activities
            getAppointments();
        }catch (Exception e){
            Toast.makeText(this, "Error loading UI", Toast.LENGTH_SHORT).show();
        }
    }

    private void getAppointmentslocal(){
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

                        app.setmTopic(cursor.getString(cursor.getColumnIndex("DEST")));
                        app.setmSite(cursor.getString(cursor.getColumnIndex("DRIVER")));
                        app.setmStart(cursor.getString(cursor.getColumnIndex("DEPART")));
                        app.setmPreuse(cursor.getString(cursor.getColumnIndex("PREUSE")));
                        app.setmPretrip(cursor.getString(cursor.getColumnIndex("PRETRIP")));
                        app.setmSec(cursor.getString(cursor.getColumnIndex("SECUR")));
                        app.setmID(cursor.getString(cursor.getColumnIndex("TRIP")));

                        mList.add(app);
                    } catch (Exception e) {
                        e.printStackTrace();
                        handler.post(() -> {
                            Methods.Alert("Error", e.toString(), mytrip.this);
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
                    appointmentAdapter = new TripAdpater(mytrip.this,mList);
                    mActivitiesRecyclerView.setAdapter(appointmentAdapter);
                });
            }).start();
        } catch (Exception e) {
            Methods.showAlert("Error", e.toString(), this);
        }
    }
    private void getAppointments(){
        try {

            //get activities
            run();

    }
        catch (Exception e) {
            Toast.makeText(this, "Error loading UI", Toast.LENGTH_SHORT).show();
        }
    }

    private void run() {
        handler.post(() -> {
            Methods.showDialog(mDialog, "Getting data objects...", true);
        });
        String user_id = "";
        if (Methods.getUserId(this) == "0") {
            Methods.showAlert("Error", "Could not get user id.", this);
            return;
        }
        user_id = String.valueOf(Methods.getUserId(this));
        Call<ResponseBody> dis = apiInterface.getrips();
        Methods.showDialog(mDialog, "Loading Trips...", true);
        String finalUser_id = user_id;
        dis.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Methods.showDialog(mDialog, "Dismiss", false);
                    appointment app;
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        JsonParser parser = new JsonParser();
                        String result = parser.parse(responseData).getAsString();
                        JSONArray array = new JSONArray(result);
                        ArrayList<String> a;//ids
                        ArrayList<String> b;//names
                        ArrayList<String> c;//des
                        ArrayList<String> d;//des
                        ArrayList<String> e;//des
                        ArrayList<String> f;//des
                        ArrayList<String> g;//des
                        mList = new ArrayList<>();
                        a = new ArrayList<>();
                        b = new ArrayList<>();
                        c = new ArrayList<>();
                        d = new ArrayList<>();
                        e = new ArrayList<>();
                        f = new ArrayList<>();
                        g = new ArrayList<>();
                 
                        String dest="";
                        String driver="";
                        String depart ="";
                        String sec ="" ;
                        String id ="";
                        String sect = "";
                        String preuse = "";
                        String pretrip ="";
                        for (int i = 0; i < array.length(); i++) {
                            app = new appointment();

                    
                            JSONObject jsonObject = array.getJSONObject(i);

                            dest = jsonObject.getString("dest");
                            driver = jsonObject.getString("veh");
                            depart=jsonObject.getString("datetrip");
                            id=jsonObject.getString("trip");
                            sect=jsonObject.getString("security");
                            pretrip =jsonObject.getString("pretrip");
                            preuse=jsonObject.getString("preuse");
                            sec=jsonObject.getString("driver");

                                app.setmTopic(dest);
                                app.setmSite(driver);
                                app.setmStart(depart);
                                app.setmPreuse(preuse);
                                app.setmPretrip(pretrip);
                                app.setmSec(sect);
                                app.setmID(id);
                                mList.add(app);
                                a.add(jsonObject.getString("dest"));
                                b.add(jsonObject.getString("veh"));
                                c.add(jsonObject.getString("datetrip"));
                                d.add(jsonObject.getString("trip"));
                                e.add(jsonObject.getString("security"));
                                f.add(jsonObject.getString("pretrip"));
                                g.add(jsonObject.getString("preuse"));

                        }
                        activities_helper .insert_trip(a, b,c,d,e,f,g);
                        appointmentAdapter = new TripAdpater(mytrip.this, mList);
                        mActivitiesRecyclerView.setAdapter(appointmentAdapter);
                        Methods.showAlert("Trips Sync Result", "Successfully syncronized trips", mytrip.this);
                    } else {
                        Toast.makeText(mytrip.this, "Request unsuccessful", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    System.out.println(e);
                    Methods.showAlert("Error", e.toString(), mytrip.this);
                }
            }

            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Methods.showDialog(mDialog, "Dismiss", false);
                Methods.showAlert("Failure", "Loading Offline Trips", mytrip.this);
                getAppointmentslocal();
            }


        });
    }

}