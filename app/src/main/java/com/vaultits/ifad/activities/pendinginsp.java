
package com.vaultits.ifad.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonParser;
import com.vaultits.ifad.R;
import com.vaultits.ifad.adapters.InpectAdpater;
import com.vaultits.ifad.adapters.TripAdpater;
import com.vaultits.ifad.database.captured_data.activities_helper;
import com.vaultits.ifad.database.look_up_data.activities_facilitator_helper;
import com.vaultits.ifad.database.look_up_data.budget_activity_list_helper;
import com.vaultits.ifad.database.look_up_data.component_sub_component_helper;
import com.vaultits.ifad.database.look_up_data.project_organisation_helper;
import com.vaultits.ifad.database.look_up_data.prov_dis_helper;
import com.vaultits.ifad.logic.Methods;
import com.vaultits.ifad.models.appointment;
import com.vaultits.ifad.retrofit.APIClient;
import com.vaultits.ifad.retrofit.ApiInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class pendinginsp extends AppCompatActivity {

    private RecyclerView mActivitiesRecyclerView;
    private ArrayList<appointment> mList;
    private InpectAdpater appointmentAdapter;

    private Dialog mDialog;
    //retrofit
    private ApiInterface apiInterface;
    private activities_helper activities_helper;
    private Handler handler;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            //set layout
            setContentView(R.layout.activity_pendinginsp);

            mActivitiesRecyclerView = (RecyclerView) findViewById(R.id.activities_trip_recycler);
            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            mActivitiesRecyclerView.setLayoutManager(linearLayoutManager);
            mActivitiesRecyclerView.setItemAnimator(new DefaultItemAnimator());

            apiInterface = APIClient.getApiClient().create(ApiInterface.class);
            mDialog = new Dialog(this);
            this.mContext =pendinginsp.this;

            activities_helper = new activities_helper(this, "", null);

            handler = new Handler();
            //get activities
            run();
        }catch (Exception e){
            Toast.makeText(this, "Error loading UI", Toast.LENGTH_SHORT).show();
        }
    }
    private void getAppointments(){
        try {
            //get activities
            new Thread(() -> {
                handler.post(() -> {
                    Methods.showDialog(mDialog, "Getting data objects...", true);
                });

                JSONObject jsonObject;
                JSONArray jsonArray = new JSONArray();
                int noOfObjects = 0;
                appointment app;
                int a = 0;
                mList = new ArrayList<>();
                while (a<2) {
                    a=a+1;
                    jsonObject = new JSONObject();
                    app = new appointment();
                    try {
                        //APPOINTMENT DETAILS
                        //jsonObject.put("SUBJECT", cursor.getString(cursor.getColumnIndex("TOPIC")));
                        String start_date = "2022-23-06";
                        String end_date = "2022-23-06";
                        String dest= "Harare";
                        String fleet = "AFK300";
                        String id ="4";
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

                        app.setmTopic(dest);
                        app.setmSite(fleet);
                        app.setmStart(start_date);
                        app.setmEnd(end_date);

                        app.setmID(id);


                        mList.add(app);
                    } catch (Exception e) {
                        e.printStackTrace();
                        handler.post(() -> {
                            Methods.Alert("Error", e.toString(), pendinginsp.this);
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
                    appointmentAdapter = new InpectAdpater(pendinginsp.this,mList);
                    mActivitiesRecyclerView.setAdapter(appointmentAdapter);
                });
            }).start();
        } catch (Exception e) {
            Methods.showAlert("Failure", "Loading Offline Trips", pendinginsp.this);

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


                        String start_date = "";
                        String end_date = "";
                        String dest= "";
                        String fleet = "";
                        String id ="";
                        for (int i = 0; i < array.length(); i++) {
                            app = new appointment();


                            JSONObject jsonObject = array.getJSONObject(i);

                            dest = jsonObject.getString("dest");
                            fleet = jsonObject.getString("veh");
                            start_date=jsonObject.getString("datetrip");
                            id=jsonObject.getString("trip");

                            app.setmTopic(dest);
                            app.setmSite(fleet);
                            app.setmStart(start_date);
                            app.setmEnd(end_date);

                            app.setmID(id);
                                mList.add(app);



                        }
                        appointmentAdapter = new InpectAdpater(pendinginsp.this, mList);
                        mActivitiesRecyclerView.setAdapter(appointmentAdapter);
                        Methods.showAlert("Trips Sync Result", "Successfully syncronized trips", pendinginsp.this);
                    } else {
                        Toast.makeText(pendinginsp.this, "Request unsuccessful", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    System.out.println(e);
                    Methods.showAlert("Error", e.toString(), pendinginsp.this);
                }
            }

            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Methods.showDialog(mDialog, "Dismiss", false);
                Methods.showAlert("Failure", "Loading Offline Trips", pendinginsp.this);

            }


        });
    }
}