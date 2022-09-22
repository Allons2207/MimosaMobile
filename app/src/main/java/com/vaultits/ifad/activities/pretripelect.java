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

public class pretripelect extends AppCompatActivity {
    private Spinner hlielec;
    private  Spinner indelec;
    private  Spinner plighelec;
    private  Spinner slightelec;
    private  Spinner hlightele;
    private  Spinner rvlighelec;
    private  Spinner intlightelec;
    private  Spinner sbellelec;
    private  Spinner hooterelec;
    private  Spinner gaugelec;
    private  Spinner batrryelec;
    private  Spinner ovelec;
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
    private TextInputEditText placeelec;//place/site
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

        setContentView(R.layout.activity_pretripelect);
            hlielec = (Spinner) findViewById(R.id.hlielec);
            indelec = (Spinner) findViewById(R.id.indelec);
            plighelec= (Spinner)  findViewById(R.id.plighelec);
            slightelec = (Spinner) findViewById(R.id.slightelec);
            hlightele = (Spinner)  findViewById(R.id.hlightele);
            rvlighelec = (Spinner) findViewById(R.id.rvlighelec);
            intlightelec = (Spinner) findViewById(R.id.intlightelec);
            sbellelec = (Spinner) findViewById(R.id.sbellelec);
            hooterelec= (Spinner) findViewById(R.id.hooterelec);
            gaugelec = (Spinner) findViewById(R.id.gaugelec);
            batrryelec = (Spinner) findViewById(R.id.batrryelec);
            ovelec = (Spinner) findViewById(R.id.ovelec);


            calendar = Calendar.getInstance();
            mDialog =new Dialog(this);
            mComment = (TextInputEditText) findViewById(R.id.commentelec);
            mStart = (Button) findViewById(R.id.start_dateelec);
            mEnd = (Button) findViewById(R.id.end_dateelec);
            mSave = (Button) findViewById(R.id.save_elec);
            placeelec =(TextInputEditText) findViewById(R.id.placeelec);
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
            String finalUser_id1 = user_id;
            mSave.setOnClickListener(v ->{
                if (v == mSave) {
                    try {

                        new Thread(() -> {
                            //APPOINTMENT DETAILS
                            try {


                                jsonObject.put("a", hlielec.getSelectedItem().toString());

                                jsonObject.put("b",indelec.getSelectedItem().toString());
                                jsonObject.put("c", plighelec.getSelectedItem().toString());
                                jsonObject.put("d", hlightele.getSelectedItem().toString());
                                jsonObject.put("e", rvlighelec.getSelectedItem().toString());
                                jsonObject.put("f", intlightelec.getSelectedItem().toString());
                                jsonObject.put("g", sbellelec.getSelectedItem().toString());
                                jsonObject.put("h", hooterelec.getSelectedItem().toString());
                                jsonObject.put("i", gaugelec.getSelectedItem().toString());
                                jsonObject.put("j", batrryelec.getSelectedItem().toString());
                                jsonObject.put("k", mStart.getText().toString().trim());
                                jsonObject.put("l", mEnd.getText().toString().trim());
                                //PARTICIPANT DETAILS
                                jsonObject.put("m", mComment.getText().toString().trim());
                                jsonObject.put("n", ovelec.getSelectedItem().toString());
                                jsonObject.put("o", finalUser_id1);
                                jsonObject.put("p", fleet);
                                jsonObject.put("q", trip);
                                jsonObject.put("r", placeelec.getText().toString().trim());




                                jsonArray.put(jsonObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                handler.post(() -> {
                                    Methods.Alert("Error", e.toString(), pretripelect.this);
                                });
                            }
                            postActivities(jsonArray.toString());
                        }).start();
                        //jsonObject.put("DATE_REGISTERED", cursor.getString(cursor.getColumnIndex("DATEREGISTERED")));
                    } catch (Exception e) {
                        e.printStackTrace();
                        handler.post(() -> {
                            Methods.Alert("Error", e.toString(), pretripelect.this);
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
            Call<ResponseBody> activities = apiInterface.SyncActivi(appointments);
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
                            Methods.showAlert("Response", "Saved Succesfully", pretripelect.this);
                        } else if (message.equalsIgnoreCase("Error")) {
                            Methods.showAlert("Response", "Server error.", pretripelect.this);
                        } else {
                            Methods.showAlert("Response", message, pretripelect.this);
                        }

                    } catch (Exception e) {
                        Methods.showDialog(mDialog, "dismiss", false);
                        Toast.makeText(pretripelect.this, "Error " + e.toString(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    try {
                        Methods.showDialog(mDialog, "dismiss", false);
                        Methods.showAlert("Request failed", "Request failed..Check your network connection.", pretripelect.this);
                    } catch (Exception e) {
                        Toast.makeText(pretripelect.this, "Error " + e.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        } catch (Exception e) {
            Methods.Alert("Error", e.toString(), pretripelect.this);
        }
    }
}

