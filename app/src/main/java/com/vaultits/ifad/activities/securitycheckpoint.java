package com.vaultits.ifad.activities;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonParser;
import com.vaultits.ifad.CapturedDataSyncActivity;
import com.vaultits.ifad.R;
import com.vaultits.ifad.adapters.AppointmentAdapter;
import com.vaultits.ifad.adapters.TripAdpater;
import com.vaultits.ifad.database.captured_data.activities_helper;
import com.vaultits.ifad.logic.Methods;
import com.vaultits.ifad.models.appointment;
import com.vaultits.ifad.retrofit.APIClient;
import com.vaultits.ifad.retrofit.ApiInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;public class securitycheckpoint extends AppCompatActivity {
    private Spinner hlielec;
    private Spinner indelec;
    private Spinner plighelec;
    private Spinner slightelec;
    private Spinner hlightele;
    private Spinner rvlighelec;
    private Spinner intlightelec;
    private Spinner sbellelec;
    private Spinner hooterelec;
    private Spinner gaugelec;
    private Spinner batrryelec;
    private Spinner ovelec;

    private TextInputEditText placeelec;//place/site
    private Spinner suzie;//users spinner
    private Spinner cab;//users spinner
    private Spinner overall;//users spinner
    private Spinner lic;//users spinner

    private Intent mIntent;
    private TextInputEditText endtime;//topic
    private TextInputEditText mComment;//comment
    private TextInputEditText driv;//topic
    private TextInputEditText tools;//comment
    private TextInputEditText mile;//topic
    private TextInputEditText gdstate;//comment
    private Spinner mSchemes;//schemes spinner
    private Button mStart;//start date
    private Button mEnd;//end date
    private Button mSave;//
    private Handler handler;
    Calendar calendar;//calender
    int year, month, day, hour, minutes;
    private Dialog mDialog;//custom dialog
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    private ApiInterface apiInterface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {

            setContentView(R.layout.activity_securitycheckpoint);

            hlielec = (Spinner) findViewById(R.id.vhid);
            indelec = (Spinner) findViewById(R.id.stacheck);


            calendar = Calendar.getInstance();
            mDialog = new Dialog(this);
            mComment = (TextInputEditText) findViewById(R.id.cmsec);
            mStart = (Button) findViewById(R.id.start_datety);
            mEnd = (Button) findViewById(R.id.end_datety);
            mSave = (Button) findViewById(R.id.save_participantsec);
            placeelec = (TextInputEditText) findViewById(R.id.first_namvid);
            endtime = (TextInputEditText) findViewById(R.id.surnamedest);
            driv = (TextInputEditText) findViewById(R.id.national_iddriver);
            mile = (TextInputEditText) findViewById(R.id.year_of_birthmile);
            tools = (TextInputEditText) findViewById(R.id.tols);
            gdstate = (TextInputEditText) findViewById(R.id.gdstate);

            apiInterface = APIClient.getApiClient().create(ApiInterface.class);


            hlielec.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //populate districts spinner
                    String proj = hlielec.getItemAtPosition(position).toString();

                    String Allons ="Allons";
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


                                jsonObject.put("a", hlielec.getSelectedItem().toString());

                                jsonObject.put("b", indelec.getSelectedItem().toString());
                                jsonObject.put("c",  indelec.getSelectedItem().toString());
                                jsonObject.put("d", mile.getText().toString().trim());
                                jsonObject.put("e",tools.getText().toString().trim());
                                jsonObject.put("f", gdstate.getText().toString().trim());
                                jsonObject.put("g", finalUser_id1);



                                jsonArray.put(jsonObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                handler.post(() -> {
                                    Methods.Alert("Error", e.toString(), securitycheckpoint.this);
                                });
                            }
                            postActivities(jsonArray.toString());
                        }).start();
                        //jsonObject.put("DATE_REGISTERED", cursor.getString(cursor.getColumnIndex("DATEREGISTERED")));
                    } catch (Exception e) {
                        e.printStackTrace();
                        handler.post(() -> {
                            Methods.Alert("Error", e.toString(), securitycheckpoint.this);
                        });
                    }
                }
            });
        } catch (Exception e) {

            Methods.Alert("Error", e.toString(), securitycheckpoint.this);
        }
    }

    private void postActivities(String jsonArray) {
        try {
            RequestBody appointments = RequestBody.create(MultipartBody.FORM, jsonArray);
            Call<ResponseBody> activities = apiInterface.SyncSec(appointments);
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
                            Methods.showAlert("Response", "Saved Succesfully", securitycheckpoint.this);
                        } else if (message.equalsIgnoreCase("Error")) {
                            Methods.showAlert("Response", "Server error.", securitycheckpoint.this);
                        } else {
                            Methods.showAlert("Response", message, securitycheckpoint.this);
                        }

                    } catch (Exception e) {
                        Methods.showDialog(mDialog, "dismiss", false);
                        Toast.makeText(securitycheckpoint.this, "Error " + e.toString(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    try {
                        Methods.showDialog(mDialog, "dismiss", false);
                        Methods.showAlert("Request failed", "Request failed..Check your network connection.", securitycheckpoint.this);
                    } catch (Exception e) {
                        Toast.makeText(securitycheckpoint.this, "Error " + e.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        } catch (Exception e) {
            Methods.Alert("Error", e.toString(), securitycheckpoint.this);
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
                            ArrayList<String> a = new ArrayList<String>();
                            ArrayList<String> b = new ArrayList<String>();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject = array.getJSONObject(i);
                                a.add(jsonObject.getString("mile"));

                                placeelec.setText(pass);
                                endtime.setText(jsonObject.getString("position"));
                                mile.setText(jsonObject.getString("mile"));
                                b.add(jsonObject.getString("position"));
                            }
                            String[] tokens = Methods.removeQoutes(result).split(":");
                            String message = "Loaded Succesfully";

                            if (message.equalsIgnoreCase("Invalid username or password")) {
                                Methods.showAlert("Response", "Invalid username or password.", securitycheckpoint.this);
                            } else if (message.equalsIgnoreCase("Success")) {
                                //methods.showAlert("Response", "Sign in successful.", UserLoginAndSignUpActivity.this);
                                Toast.makeText(securitycheckpoint.this, "Successful Request", Toast.LENGTH_LONG).show();


                            } else if (message.equalsIgnoreCase("Error")) {
                                Methods.showAlert("Response", "Server error.", securitycheckpoint.this);
                            } else {
                                Methods.showAlert("Response", message, securitycheckpoint.this);
                            }

                        } catch (Exception e) {

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        try {
                            Methods.showDialog(mDialog, "dismiss", false);
                            Methods.showAlert("Request failed", "Request failed..Check your network connection.", securitycheckpoint.this);
                        } catch (Exception e) {
                            Toast.makeText(securitycheckpoint.this, "Error " + t.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

        } catch (Exception e) {
            Toast.makeText(securitycheckpoint.this, e.toString(), Toast.LENGTH_LONG).show();
        }
    }
}