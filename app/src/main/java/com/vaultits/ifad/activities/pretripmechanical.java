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
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
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
import retrofit2.Response;

public class pretripmechanical extends AppCompatActivity {
    private Spinner oil;//project dropdown
    private Spinner brakflu;//organisations dropdown
    private Spinner brake;
    private Spinner coolant;
    private Spinner pwrstrmec;
    private Spinner gearsmec;
    private Spinner stomec;
    private  Spinner rasmec;
    private Spinner fakmec;
    private Spinner fexmec;
    private  Spinner brtmec;
    private Spinner rvmwmec;
    private Spinner licmec;
    private  Spinner ovemec;
    private  Spinner faid;
    private  Spinner fext;
    private  Spinner fcap;
    private  Spinner bfcap;
    private  Spinner rcap;
    private  Spinner brke;
    private  Spinner hdbrake;
    private  Spinner fwps;
    private  Spinner rwps;
    private  Spinner btrs;
    private  Spinner vlic;
    private Spinner fdisc;
    private  Spinner vpass;
    private Spinner htr;
    private  Spinner rlight;
    private  Spinner wnut;
    private  Spinner sbt;
    private  Spinner sntz;
    private  Spinner mask;
    private  Spinner wins;
    private  Spinner wpr;
    private  Spinner dents;
    private  Spinner opr;
    private  Spinner rdtlc;
    private  Spinner tv;
    private  Spinner bty;
    private  Spinner bgauge;
    private  Spinner stopr;
    private  Spinner vcln;
    private  Spinner spbell;
    private Spinner vtail;//components dropdown
    private Spinner vjtool;//sub-components dropdown
    private Spinner wspan;//activities spinner
    private Spinner spweel;//statuses spinner
    private Spinner wchok;//districts dropdown
    private Spinner c5w;//districts dropdown
    private TextInputEditText placemec;//place/site
    private Spinner suzie;//users spinner
    private Spinner cab;//users spinner
    private Spinner overall;//users spinner
    private Spinner lic;//users spinner

    private Intent mIntent;
    private TextInputEditText endtime;//topic
    private TextInputEditText mComment;//comment
    private Spinner mSchemes;//schemes spinner
    private Button mStart;//start date
    private Button mEnd;//end date
    private Button mSave;//
    private Handler handler;
    Calendar calendar;//calender
    int year, month, day ,hour , minutes;
    private Dialog mDialog;//custom dialog
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    private ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {

        setContentView(R.layout.activity_pretripmechanical);
            oil = (Spinner) findViewById(R.id.engoilmec);
            brakflu = (Spinner) findViewById(R.id.brflumec);
            brake = (Spinner)  findViewById(R.id.brakmec);
            coolant = (Spinner) findViewById(R.id.colmec);
            pwrstrmec = (Spinner)  findViewById(R.id.pwrstrmec);
            gearsmec = (Spinner) findViewById(R.id.gearsmec);
            stomec = (Spinner) findViewById(R.id.stomec);
            rasmec = (Spinner) findViewById(R.id.rasmec);
            fakmec = (Spinner) findViewById(R.id.fakmec);
            fexmec = (Spinner) findViewById(R.id.fexmec);
            brtmec = (Spinner) findViewById(R.id.brtmec);
            rvmwmec = (Spinner) findViewById(R.id.rvmwmec);
            licmec = (Spinner) findViewById(R.id.licmec
            );
            ovemec = (Spinner) findViewById(R.id.ovemec);




            calendar = Calendar.getInstance();
            mDialog =new Dialog(this);
            mComment = (TextInputEditText) findViewById(R.id.commentmec);
            mStart = (Button) findViewById(R.id.start_datememec);
            mEnd = (Button) findViewById(R.id.end_datemec);
            mSave = (Button) findViewById(R.id.save_mec);
            placemec =(TextInputEditText) findViewById(R.id.placemec);
            endtime = (TextInputEditText)  findViewById(R.id.timemec);
            apiInterface = APIClient.getApiClient().create(ApiInterface.class);

            String user_id = "";
            if (Methods.getUserId(this) == "0") {
                Methods.showAlert("Error", "Could not get user id.", this);
                return;
            }
            user_id = String.valueOf(Methods.getUserId(this));

            mStart.setOnClickListener(v -> {
                if (v == mStart) {
                    year = calendar.get(Calendar.YEAR);
                    month = calendar.get(Calendar.MONTH);
                    day = calendar.get(Calendar.DAY_OF_MONTH);
                    hour= calendar.get(Calendar.HOUR_OF_DAY);
                    minutes = calendar.get(Calendar.MINUTE);
                    datePickerDialog = new DatePickerDialog(this, (DatePickerDialog.OnDateSetListener) (view, year, month, dayOfMonth ) -> {
                        String datenow = null;
                        if (dayOfMonth > 9 && (month + 1) < 10) {
                            datenow = dayOfMonth + "/0" + (month + 1) + "/" + year;
                        } else if (dayOfMonth < 10 && (month + 1) > 9) {

                            datenow = "0" + dayOfMonth + "/" + (month + 1) + "/" + year;
                        } else if (dayOfMonth < 10 && (month + 1) < 10) {

                            datenow = "0" + dayOfMonth + "/0" + (month + 1) + "/" + year;
                        } else {

                            datenow = dayOfMonth + "/" + (month + 1) + "/" + year;
                        }
                        mStart.setText(datenow);
                        // now show the time picker
                    }, year, month, day);
                    datePickerDialog.show();
                }
            });
            //set get end date button click event
            mEnd.setOnClickListener(v -> {
                if (v == mEnd) {
                    year = calendar.get(Calendar.YEAR);
                    month = calendar.get(Calendar.MONTH);
                    day = calendar.get(Calendar.DAY_OF_MONTH);

                    datePickerDialog = new DatePickerDialog(this, (DatePickerDialog.OnDateSetListener) (view, year, month, dayOfMonth) -> {
                        String datenow = null;
                        if (dayOfMonth > 9 && (month + 1) < 10) {
                            datenow = dayOfMonth + "-0" + (month + 1) + "-" + year;
                        } else if (dayOfMonth < 10 && (month + 1) > 9) {

                            datenow = "0" + dayOfMonth + "-" + (month + 1) + "-" + year;
                        } else if (dayOfMonth < 10 && (month + 1) < 10) {

                            datenow = "0" + dayOfMonth + "-0" + (month + 1) + "-" + year;
                        } else {

                            datenow = dayOfMonth + "-" + (month + 1) + "-" + year;
                        }
                        mEnd.setText(datenow);
                    }, year, month, day);
                    datePickerDialog.show();
                }
            });
            mIntent = getIntent();
            String  trip = mIntent.getStringExtra("id");
            String fleet = mIntent.getStringExtra("site");
            JSONObject jsonObject;
            JSONArray jsonArray = new JSONArray();
            jsonObject = new JSONObject();
            String finalUser_id = user_id;
            mSave.setOnClickListener(v ->{
                if (v == mSave) {
                    try {

                        new Thread(() -> {
                            //APPOINTMENT DETAILS
                            try {


                                jsonObject.put("a", coolant.getSelectedItem().toString());

                                jsonObject.put("b", oil.getSelectedItem().toString());
                                jsonObject.put("c", brake.getSelectedItem().toString());
                                jsonObject.put("d", pwrstrmec.getSelectedItem().toString());
                                jsonObject.put("e", brakflu.getSelectedItem().toString());
                                jsonObject.put("f", gearsmec.getSelectedItem().toString());
                                jsonObject.put("g", stomec.getSelectedItem().toString());
                                jsonObject.put("h", rasmec.getSelectedItem().toString());
                                jsonObject.put("i", fakmec.getSelectedItem().toString());
                                jsonObject.put("j", fexmec.getSelectedItem().toString());
                                jsonObject.put("k", brtmec.getSelectedItem().toString());
                                jsonObject.put("l", rvmwmec.getSelectedItem().toString());
                                //PARTICIPANT DETAILS
                                jsonObject.put("m", licmec.getSelectedItem().toString());
                                jsonObject.put("n", ovemec.getSelectedItem().toString());
                                jsonObject.put("o", placemec.getText().toString().trim());
                                jsonObject.put("p", mStart.toString().trim());
                                jsonObject.put("q", mEnd.getText().toString().trim());
                                jsonObject.put("r", mComment.getText().toString().trim());
                                jsonObject.put("t",fleet);
                                jsonObject.put("s", finalUser_id);
                                jsonObject.put("u",trip);



                                jsonArray.put(jsonObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                handler.post(() -> {
                                    Methods.Alert("Error", e.toString(), pretripmechanical.this);
                                });
                            }
                            postActivities(jsonArray.toString());
                        }).start();
                        //jsonObject.put("DATE_REGISTERED", cursor.getString(cursor.getColumnIndex("DATEREGISTERED")));
                    } catch (Exception e) {
                        e.printStackTrace();
                        handler.post(() -> {
                            Methods.Alert("Error", e.toString(), pretripmechanical.this);
                        });
                    }
                }
            });


        }
        catch (Exception e) {

        }
    }

    private void postActivities(String jsonArray) {
        try {
            RequestBody appointments = RequestBody.create(MultipartBody.FORM, jsonArray);
            Call<ResponseBody> activities = apiInterface.SyncActivit(appointments);
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
                            Methods.showAlert("Response", "Saved Succesfully", pretripmechanical.this);
                        } else if (message.equalsIgnoreCase("Error")) {
                            Methods.showAlert("Response", "Server error.", pretripmechanical.this);
                        } else {
                            Methods.showAlert("Response", message, pretripmechanical.this);
                        }

                    } catch (Exception e) {
                        Methods.showDialog(mDialog, "dismiss", false);
                        Toast.makeText(pretripmechanical.this, "Error " + e.toString(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    try {
                        Methods.showDialog(mDialog, "dismiss", false);
                        Methods.showAlert("Request failed", "Request failed..Check your network connection.", pretripmechanical.this);
                    } catch (Exception e) {
                        Toast.makeText(pretripmechanical.this, "Error " + e.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        } catch (Exception e) {
            Methods.Alert("Error", e.toString(), pretripmechanical.this);
        }
    }
}
