package com.vaultits.ifad.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonParser;
import com.vaultits.ifad.R;
import com.vaultits.ifad.database.captured_data.activities_helper;
import com.vaultits.ifad.database.look_up_data.activities_facilitator_helper;
import com.vaultits.ifad.database.look_up_data.activity_files;
import com.vaultits.ifad.logic.Methods;
import com.vaultits.ifad.retrofit.APIClient;
import com.vaultits.ifad.retrofit.ApiInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class tripupdate extends AppCompatActivity {

    private Spinner overall;//users spinner


    private Intent mIntent;
    private TextInputEditText currntmile;//topic
    private TextInputEditText currntloca;//comme
    private TextInputEditText enmile;//topic
    private TextInputEditText entloc;//com
    private TextInputEditText arrtime;//topic
    private TextInputEditText depttime;//com
    private Button mSave;//
    private ApiInterface apiInterface;
    private Handler handler;
    private activities_helper activities_helper;
    Calendar calendar;//calender
    int year, month, day, hour, minutes;
    private Dialog mDialog;//custom dialog
    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {

        setContentView(R.layout.activity_tripupdate);

        currntloca = (TextInputEditText) findViewById(R.id.currloc);
            currntmile = (TextInputEditText) findViewById(R.id.currmile);
            enmile = (TextInputEditText) findViewById(R.id.enimile);
            entloc = (TextInputEditText) findViewById(R.id.enimile);
            arrtime = (TextInputEditText) findViewById(R.id.arrtime);
            depttime = (TextInputEditText) findViewById(R.id.depttimetrip);
            overall = (Spinner) findViewById(R.id.allonsid);
            mSave =(Button) findViewById(R.id.save_trip);
            apiInterface = APIClient.getApiClient().create(ApiInterface.class);
            activities_helper = new activities_helper(this, "", null);


            overall.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //populate districts spinner
                    String proj = overall.getItemAtPosition(position).toString();
                    login(proj);

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }


            });


            String user_id = "";
            if (Methods.getUserId(this) == "0") {
                Methods.showAlert("Error", "Could not get user id.", this);
                return;
            }
            user_id = String.valueOf(Methods.getUserId(this));
            mIntent = getIntent();
            String trip = mIntent.getStringExtra("id");
            String fleet = mIntent.getStringExtra("site");
            JSONObject jsonObject;
            JSONArray jsonArray = new JSONArray();
            jsonObject = new JSONObject();
            String finalUser_id = user_id;
            String finalUser_id1 = user_id;
            mSave.setOnClickListener(v -> {
                if (v == mSave) {
                    try {

                        new Thread(() -> {
                            //APPOINTMENT DETAILS
                            try {



                                jsonObject.put("a", currntmile.getText().toString().trim());
                                jsonObject.put("b", currntloca.getText().toString().trim());
                                //PARTICIPANT DETAILS
                                jsonObject.put("c", enmile.getText().toString().trim());
                                jsonObject.put("d", currntloca.getText().toString());
                                jsonObject.put("e", finalUser_id1);
                                jsonObject.put("f", fleet);
                                jsonObject.put("g", trip);
                                jsonObject.put("h", arrtime.getText().toString());
                                jsonObject.put("i", depttime.getText().toString());



                                jsonArray.put(jsonObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                handler.post(() -> {
                                    Methods.Alert("Error", e.toString(), tripupdate.this);
                                });
                            }
                            postActivities(jsonArray.toString());
                        }).start();
                        //jsonObject.put("DATE_REGISTERED", cursor.getString(cursor.getColumnIndex("DATEREGISTERED")));

                    } catch (Exception e) {
                        e.printStackTrace();
                        handler.post(() -> {
                            Methods.Alert("Error", e.toString(), tripupdate.this);
                        });
                    }
                }
            });
        }
        catch (Exception e){

        }
    }

    private void postActivities(String jsonArray) {
        try {
            RequestBody appointments = RequestBody.create(MultipartBody.FORM, jsonArray);
            Call<ResponseBody> activities = apiInterface.ACtv(appointments);
            Methods.showDialog(mDialog, "Posting data...", true);
            activities.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        Methods.showDialog(mDialog, "dismiss", false);
                        String result = response.body().string();

                        String tokens = Methods.removeQoutes(result);
                        String message = tokens;

                        if (message.equalsIgnoreCase("Ok")) {
                            Methods.showAlert("Response", "Saved Succesfully", tripupdate.this);
                        } else if (message.equalsIgnoreCase("Error")) {
                            Methods.showAlert("Response", "Server error.", tripupdate.this);
                        } else {
                            Methods.showAlert("Response", message, tripupdate.this);
                        }

                    } catch (Exception e) {
                        Methods.showDialog(mDialog, "dismiss", false);
                        Toast.makeText(tripupdate.this, "Error " + e.toString(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    String user_id = "";
                    if (Methods.getUserId(tripupdate.this) == "0") {
                        Methods.showAlert("Error", "Could not get user id.", tripupdate.this);
                        return;
                    }
                    user_id = String.valueOf(Methods.getUserId(tripupdate.this));
                    mIntent = getIntent();
                    String trip = mIntent.getStringExtra("id");
                    String fleet = mIntent.getStringExtra("site");
                    try {
                        Methods.showDialog(mDialog, "dismiss", false);
                        Methods.showAlert("Request failed", "Request failed..Check your network connection.", tripupdate.this);
                        String  insert = activities_helper .inserttrip(
                                currntloca.getText().toString().trim(),
                                currntmile.getText().toString().trim(),
                                arrtime.getText().toString().trim(),
                                depttime.getText().toString().trim(),
                                trip,
                                user_id,
                                enmile.getText().toString().trim());
                        if (insert.equalsIgnoreCase("Success")) {
                            Methods.Alert("Success", "Activity Successfully Saved locally", tripupdate.this);
                        } else if (insert.equalsIgnoreCase("Exist")) {
                            Methods.Alert("Exist", "Activity Already Exist", tripupdate.this);
                        } else {
                            Methods.Alert("Failed", "Failed to Save Activity.", tripupdate.this);
                        }
                    } catch (Exception e) {
                        Toast.makeText(tripupdate.this, "Error " + e.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        } catch (Exception e) {
            Methods.Alert("Error", e.toString(), tripupdate.this);
        }
    }

    private void login(String pass) {
        try {
            RequestBody vehicle = RequestBody.create(MultipartBody.FORM, pass);
            Call<ResponseBody> login = apiInterface.Trackit(vehicle);

            login.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        Methods.showDialog(mDialog, "dismiss", false);
                        String result = response.body().string();
                        JsonParser parser = new JsonParser();
                        String result1 = parser.parse(result).getAsString();
                        JSONArray array = new JSONArray(result1);
                        ArrayList<String> a = new ArrayList<String>() ;
                        ArrayList<String> b = new ArrayList<String>() ;
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject = array.getJSONObject(i);
                            a.add(jsonObject.getString("mile"));


                            currntloca.setText(jsonObject.getString("position"));
                            currntmile.setText(jsonObject.getString("mile"));
                            b.add(jsonObject.getString("position"));
                        }
                        String[] tokens = Methods.removeQoutes(result).split(":");
                        String message = "Loaded Succesfully";

                        if (message.equalsIgnoreCase("Invalid username or password")) {
                            Methods.showAlert("Response", "Invalid username or password.", tripupdate.this);
                        } else if (message.equalsIgnoreCase("Success")) {
                            //methods.showAlert("Response", "Sign in successful.", UserLoginAndSignUpActivity.this);
                            Toast.makeText(tripupdate.this, "Successful Request", Toast.LENGTH_LONG).show();


                        } else if (message.equalsIgnoreCase("Error")) {
                            Methods.showAlert("Response", "Server error.", tripupdate.this);
                        } else {
                            Methods.showAlert("Response", message, tripupdate.this);
                        }

                    } catch (Exception e) {

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    try {
                        Methods.showDialog(mDialog, "dismiss", false);
                        Methods.showAlert("Request failed", "Request failed..Check your network connection.", tripupdate.this);
                    } catch (Exception e) {
                        Toast.makeText(tripupdate.this, "Error " + e.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        } catch (Exception e) {
            Toast.makeText(tripupdate.this, e.toString(), Toast.LENGTH_LONG).show();
        }
    }
}