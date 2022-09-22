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
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
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

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Preuse extends AppCompatActivity {
    private Spinner oil;//project dropdown
    private Spinner brakflu;//organisations dropdown
    private Spinner flevel;
    private Spinner coolant;
    private Spinner hdlights;
    private Spinner taillight;
    private Spinner rearlight;
    private  Spinner frlight;
    private Spinner pgauge;
    private Spinner rcondition;
    private  Spinner tmgauge;
    private Spinner cplates;
    private Spinner lfmirr;
    private  Spinner rsmirr;
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
    private TextInputEditText starttime;//place/site
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
            setContentView(R.layout.pre_use);
            oil = (Spinner) findViewById(R.id.oil);
            brakflu = (Spinner) findViewById(R.id.brakefluid);
            flevel = (Spinner)  findViewById(R.id.flevel);
            coolant = (Spinner) findViewById(R.id.coolant);
            hdlights = (Spinner)  findViewById(R.id.hdlights);
            taillight = (Spinner) findViewById(R.id.tailight);
            rearlight = (Spinner) findViewById(R.id.readlight);
            frlight = (Spinner) findViewById(R.id.frlight);
            pgauge = (Spinner) findViewById(R.id.pgauge);
            tmgauge = (Spinner) findViewById(R.id.tmgauge);
            cplates = (Spinner) findViewById(R.id.cplates);
            lfmirr = (Spinner) findViewById(R.id.lfm);
            rsmirr = (Spinner) findViewById(R.id.rsmrr
            );
            faid = (Spinner) findViewById(R.id.faid);
            fext = (Spinner) findViewById(R.id.fext);
            fcap = (Spinner)  findViewById(R.id.fcap);
            bfcap = (Spinner)  findViewById(R.id.bfcap);
            brke  = (Spinner)  findViewById(R.id.brk);
            hdbrake = (Spinner) findViewById(R.id.hbrak);
            fwps = (Spinner) findViewById(R.id.fwpr);
            rwps = (Spinner)  findViewById(R.id.rwpr);
            btrs = (Spinner)  findViewById(R.id.btri);
            vlic  = (Spinner)  findViewById(R.id.vlic);
            fdisc = (Spinner) findViewById(R.id.fdisc);
            vpass = (Spinner) findViewById(R.id.vpass);
            htr = (Spinner)  findViewById(R.id.hotr);
            rlight = (Spinner)  findViewById(R.id.rvlight);
            wnut  = (Spinner)  findViewById(R.id.wnut);
            sbt = (Spinner) findViewById(R.id.sbt);
            sntz = (Spinner) findViewById(R.id.sntz);
            mask = (Spinner)  findViewById(R.id.mask);
            wins = (Spinner)  findViewById(R.id.wins);
            wpr  = (Spinner)  findViewById(R.id.wpr);
            dents = (Spinner) findViewById(R.id.dents);
            opr= (Spinner) findViewById(R.id.opr);
            rdtlc = (Spinner)  findViewById(R.id.rdtc);
            tv = (Spinner)  findViewById(R.id.tv);
            bty  = (Spinner)  findViewById(R.id.bty);


            bgauge = (Spinner)  findViewById(R.id.bgauge);
            stopr = (Spinner)  findViewById(R.id.stopr);
            vcln  = (Spinner)  findViewById(R.id.vcln);
            spbell = (Spinner) findViewById(R.id.sbell);
            vtail = (Spinner) findViewById(R.id.vtacc);
            vjtool = (Spinner)  findViewById(R.id.vjtool);
            wspan = (Spinner)  findViewById(R.id.wspan);
            spweel  = (Spinner)  findViewById(R.id.spweel);
            wchok = (Spinner) findViewById(R.id.wchok);
            c5w= (Spinner) findViewById(R.id.c5w);
            suzie = (Spinner)  findViewById(R.id.suzie);
            cab = (Spinner)  findViewById(R.id.cab);
            overall  = (Spinner)  findViewById(R.id.overall);


            calendar = Calendar.getInstance();
            mDialog =new Dialog(this);
            mComment = (TextInputEditText) findViewById(R.id.comment);
            mStart = (Button) findViewById(R.id.start_date);
            mEnd = (Button) findViewById(R.id.end_date);
            mSave = (Button) findViewById(R.id.save_ac);
            starttime =(TextInputEditText) findViewById(R.id.time);
            endtime = (TextInputEditText)  findViewById(R.id.timeend);
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


                            jsonObject.put("a", oil.getSelectedItem().toString());

                            jsonObject.put("b", coolant.getSelectedItem().toString());
                            jsonObject.put("c", brakflu.getSelectedItem().toString());
                            jsonObject.put("d", hdlights.getSelectedItem().toString());
                            jsonObject.put("e", taillight.getSelectedItem().toString());
                            jsonObject.put("f", rearlight.getSelectedItem().toString());
                            jsonObject.put("g", frlight.getSelectedItem().toString());
                            jsonObject.put("h", pgauge.getSelectedItem().toString());
                            jsonObject.put("i", cplates.getSelectedItem().toString());
                            jsonObject.put("j", lfmirr.getSelectedItem().toString());
                            jsonObject.put("k", rsmirr.getSelectedItem().toString());
                            jsonObject.put("l", faid.getSelectedItem().toString());
                            //PARTICIPANT DETAILS
                            jsonObject.put("m", fext.getSelectedItem().toString());
                            jsonObject.put("n", fcap.getSelectedItem().toString());
                            jsonObject.put("o", bfcap.getSelectedItem().toString());
                            jsonObject.put("p", bfcap.getSelectedItem().toString());
                            jsonObject.put("q", brke.getSelectedItem().toString());
                            jsonObject.put("r", hdbrake.getSelectedItem().toString());
                            jsonObject.put("s", fwps.getSelectedItem().toString());
                            jsonObject.put("t", rwps.getSelectedItem().toString());
                            jsonObject.put("u", btrs.getSelectedItem().toString());


                            jsonObject.put("v", vlic.getSelectedItem().toString());

                            jsonObject.put("w", fdisc.getSelectedItem().toString());
                            jsonObject.put("x", htr.getSelectedItem().toString());
                            jsonObject.put("y", rlight.getSelectedItem().toString());
                            jsonObject.put("z", wnut.getSelectedItem().toString());


                            jsonObject.put("aa", sbt.getSelectedItem().toString());
                            jsonObject.put("bb", sntz.getSelectedItem().toString());
                            jsonObject.put("cc", mask.getSelectedItem().toString());
                            jsonObject.put("dd", wins.getSelectedItem().toString());
                            jsonObject.put("ee", wpr.getSelectedItem().toString());
                            jsonObject.put("ff", dents.getSelectedItem().toString());
                            jsonObject.put("gg", opr.getSelectedItem().toString());
                            //PARTICIPANT DETAILS
                            jsonObject.put("hh", oil.getSelectedItem().toString());
                            jsonObject.put("ii", rdtlc.getSelectedItem().toString());
                            jsonObject.put("jj", tv.getSelectedItem().toString());
                            jsonObject.put("kk", bty.getSelectedItem().toString());
                            jsonObject.put("ll", bgauge.getSelectedItem().toString());
                            jsonObject.put("mm", stopr.getSelectedItem().toString());
                            jsonObject.put("nn", vlic.getSelectedItem().toString());
                            jsonObject.put("oo", spbell.getSelectedItem().toString());
                            jsonObject.put("pp", vjtool.getSelectedItem().toString());

                            jsonObject.put("qq", vjtool.getSelectedItem().toString());
                            jsonObject.put("rr", wspan.getSelectedItem().toString());
                            jsonObject.put("ss", spweel.getSelectedItem().toString());
                            jsonObject.put("tt", wchok.getSelectedItem().toString());
                            jsonObject.put("uu", vtail.getSelectedItem().toString());
                            jsonObject.put("vv", suzie.getSelectedItem().toString());
                            jsonObject.put("ww", suzie.getSelectedItem().toString());
                            jsonObject.put("xx", cab.getSelectedItem().toString());
                            jsonObject.put("yy", fleet);
                            jsonObject.put("zz", trip);
                            jsonObject.put("aaa", mEnd.getText().toString().trim());
                            jsonObject.put("bbb", flevel.getSelectedItem().toString());
                            jsonObject.put("ccc", oil.getSelectedItem().toString());
                            jsonObject.put("ddd", overall.getSelectedItem().toString());

                            jsonObject.put("fff", rwps.getSelectedItem().toString());
                            jsonObject.put("ggg", finalUser_id);
                            jsonArray.put(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            handler.post(() -> {
                                Methods.Alert("Error", e.toString(), Preuse.this);
                            });
                        }
                        postActivities(jsonArray.toString());
                    }).start();
                    //jsonObject.put("DATE_REGISTERED", cursor.getString(cursor.getColumnIndex("DATEREGISTERED")));
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.post(() -> {
                        Methods.Alert("Error", e.toString(), Preuse.this);
                    });
                }
            }
            });


        } catch (Exception e) {

        }
    }

    private void postActivities(String jsonArray) {
        try {
            RequestBody appointments = RequestBody.create(MultipartBody.FORM, jsonArray);
            Call<ResponseBody> activities = apiInterface.SyncActivities(appointments);
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
                            Methods.showAlert("Response", "Saved Succesfully", Preuse.this);
                        } else if (message.equalsIgnoreCase("Error")) {
                            Methods.showAlert("Response", "Server error.", Preuse.this);
                        } else {
                            Methods.showAlert("Response", message, Preuse.this);
                        }

                    } catch (Exception e) {
                        Methods.showDialog(mDialog, "dismiss", false);
                        Toast.makeText(Preuse.this, "Error " + e.toString(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    try {
                        Methods.showDialog(mDialog, "dismiss", false);
                        Methods.showAlert("Request failed", "Request failed..Check your network connection.", Preuse.this);
                    } catch (Exception e) {
                        Toast.makeText(Preuse.this, "Error " + e.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        } catch (Exception e) {
            Methods.Alert("Error", e.toString(), Preuse.this);
        }
    }
}