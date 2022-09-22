package com.vaultits.ifad;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.vaultits.ifad.database.captured_data.activities_helper;
import com.vaultits.ifad.database.look_up_data.activities_facilitator_helper;
import com.vaultits.ifad.logic.Methods;
import com.vaultits.ifad.navigation.MainNavigationDrawerActivity;
import com.vaultits.ifad.retrofit.APIClient;
import com.vaultits.ifad.retrofit.ApiInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CapturedDataSyncActivity extends AppCompatActivity {

    private Button mSyncActivities;
    private Handler handler;
    private Dialog mDialog;
    private activities_facilitator_helper activities_facilitator_helper;
    private activities_helper activities_helper;
    //retrofit
    private ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            //set layout
            setContentView(R.layout.activity_captured_data_sync);
            //database instances
            activities_facilitator_helper = new activities_facilitator_helper(this, "", null);
            activities_helper = new activities_helper(this, "", null);

            //init widget
            mSyncActivities = (Button) findViewById(R.id.sync_activities);
            handler = new Handler();
            mDialog = new Dialog(this);
            apiInterface = APIClient.getApiClient().create(ApiInterface.class);
            //events
            mSyncActivities.setOnClickListener(v -> {
                try {
                    //get activities
                    new Thread(() -> {
                        handler.post(() -> {
                            Methods.showDialog(mDialog, "Getting data objects...", true);
                        });
                        Cursor cursor = activities_helper.getActivityParticipation();
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
                        while (cursor.moveToNext()) {
                            jsonObject = new JSONObject();
                            try {
                                //APPOINTMENT DETAILS

                                jsonObject.put("a", cursor.getString(cursor.getColumnIndex("CURRMILE")));
                                jsonObject.put("b", cursor.getString(cursor.getColumnIndex("CURRLOC")));
                                jsonObject.put("h", cursor.getString(cursor.getColumnIndex("ARRTIME")));
                                jsonObject.put("i", cursor.getString(cursor.getColumnIndex("DEPTTIME")));
                                jsonObject.put("g", cursor.getString(cursor.getColumnIndex("TRIP")));
                                jsonObject.put("e", cursor.getString(cursor.getColumnIndex("USERID")));
                                jsonObject.put("f", cursor.getString(cursor.getColumnIndex("TRIP")));
                                jsonObject.put("c", cursor.getString(cursor.getColumnIndex("MILE")));

                                //jsonObject.put("DATE_REGISTERED", cursor.getString(cursor.getColumnIndex("DATEREGISTERED")));
                            } catch (JSONException e) {
                                e.printStackTrace();
                                handler.post(() -> {
                                    Methods.Alert("Error", e.toString(), CapturedDataSyncActivity.this);
                                });
                            }
                            try {
                                jsonArray.put(noOfObjects, jsonObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            noOfObjects++;
                        }
                        int finalNoOfObjects = noOfObjects;
                        handler.post(() -> {
                            Methods.showDialog(mDialog, "Getting data objects...", false);
                            //show message with details and prompt to continue sync
                            final AlertDialog.Builder builder = new AlertDialog.Builder(CapturedDataSyncActivity.this);
                            builder.setCancelable(true);
                            builder.setTitle("Finished Object extract");
                            builder.setMessage(jsonArray.length() + " object(s) found. Continue to sync?");
                            builder.setPositiveButton("Ok",
                                    (dialog, which) -> {
                                        if (jsonArray.length() > 0) {
                                            //post data to server
                                            postActivities(jsonArray.toString());
                                        } else {
                                            //no data
                                            Toast.makeText(this, "There is no data.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            builder.setNegativeButton("CANCEL", (dialog, which) -> dialog.cancel());
                            builder.show();
                        });
                    }).start();
                } catch (Exception e) {
                    Methods.showAlert("Error", e.toString(), this);
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "Error loading UI.", Toast.LENGTH_SHORT).show();
        }
    }

    //post activities
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
                            Methods.showAlert("Response", "Data Sync Finished.You may check on main system.", CapturedDataSyncActivity.this);
                        } else if (message.equalsIgnoreCase("Error")) {
                            Methods.showAlert("Response", "Server error.", CapturedDataSyncActivity.this);
                        } else {
                            Methods.showAlert("Response", message, CapturedDataSyncActivity.this);
                        }

                    } catch (Exception e) {
                        Methods.showDialog(mDialog, "dismiss", false);
                        Toast.makeText(CapturedDataSyncActivity.this, "Error " + e.toString(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    try {
                        Methods.showDialog(mDialog, "dismiss", false);
                        Methods.showAlert("Request failed", "Request failed..Check your network connection.", CapturedDataSyncActivity.this);
                    } catch (Exception e) {
                        Toast.makeText(CapturedDataSyncActivity.this, "Error " + e.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        } catch (Exception e) {
            Methods.Alert("Error", e.toString(), CapturedDataSyncActivity.this);
        }
    }
}