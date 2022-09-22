package com.vaultits.ifad.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
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
import com.vaultits.ifad.R;
import com.vaultits.ifad.database.captured_data.activities_helper;
import com.vaultits.ifad.database.look_up_data.activities_facilitator_helper;
import com.vaultits.ifad.database.look_up_data.budget_activity_list_helper;
import com.vaultits.ifad.database.look_up_data.component_sub_component_helper;
import com.vaultits.ifad.database.look_up_data.project_organisation_helper;
import com.vaultits.ifad.database.look_up_data.prov_dis_helper;
import com.vaultits.ifad.logic.Methods;
import com.vaultits.ifad.retrofit.APIClient;
import com.vaultits.ifad.retrofit.ApiInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class buschedule extends AppCompatActivity {
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
            setContentView(R.layout.activity_buschedule);
            currntloca = (TextInputEditText) findViewById(R.id.frm);
            currntmile = (TextInputEditText) findViewById(R.id.dtyy);
            enmile = (TextInputEditText) findViewById(R.id.cmbus);
            entloc = (TextInputEditText) findViewById(R.id.to);
            arrtime = (TextInputEditText) findViewById(R.id.dtyy);
            overall = (Spinner) findViewById(R.id.stacheckbus);
            mSave =(Button) findViewById(R.id.savbus);
            depttime = (TextInputEditText) findViewById(R.id.busreg);
            apiInterface = APIClient.getApiClient().create(ApiInterface.class);
            activities_helper = new activities_helper(this, "", null);
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
                                jsonObject.put("d", entloc.getText().toString());
                                jsonObject.put("e", finalUser_id1);
                                jsonObject.put("f", depttime.getText().toString());
                                jsonObject.put("g", overall.getSelectedItem().toString());

                                jsonArray.put(jsonObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                handler.post(() -> {
                                    Methods.Alert("Error", e.toString(), buschedule.this);
                                });
                            }
                            postActivities(jsonArray.toString());
                        }).start();
                        //jsonObject.put("DATE_REGISTERED", cursor.getString(cursor.getColumnIndex("DATEREGISTERED")));

                    } catch (Exception e) {
                        e.printStackTrace();
                        handler.post(() -> {
                            Methods.Alert("Error", e.toString(), buschedule.this);
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
            Call<ResponseBody> activities = apiInterface.Bus(appointments);
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
                            Methods.showAlert("Response", "Saved Succesfully", buschedule.this);
                        } else if (message.equalsIgnoreCase("Error")) {
                            Methods.showAlert("Response", "Server error.", buschedule.this);
                        } else {
                            Methods.showAlert("Response", message, buschedule.this);
                        }
                    } catch (Exception e) {
                        Methods.showDialog(mDialog, "dismiss", false);
                        Toast.makeText(buschedule.this, "Error " + e.toString(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    String user_id = "";
                    if (Methods.getUserId(buschedule.this) == "0") {
                        Methods.showAlert("Error", "Could not connect to server.", buschedule.this);
                        return;
                    }

                }
            });
        } catch (Exception e) {
            Methods.Alert("Error", e.toString(), buschedule.this);
        }
    }}


