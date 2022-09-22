package com.vaultits.ifad.adapters;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.vaultits.ifad.R;
import com.vaultits.ifad.SyncActivityFilesActivity;
import com.vaultits.ifad.activities.ActivityFileActivity;
import com.vaultits.ifad.activities.AddParticipantActivity;
import com.vaultits.ifad.activities.AddParticipantFromViewingActivity;
import com.vaultits.ifad.activities.EditActivityActivity;
import com.vaultits.ifad.activities.Preuse;
import com.vaultits.ifad.activities.ViewParticipantsActivity;
import com.vaultits.ifad.activities.mytrip;
import com.vaultits.ifad.activities.tripupdate;
import com.vaultits.ifad.database.captured_data.activities_helper;
import com.vaultits.ifad.database.look_up_data.activities_facilitator_helper;
import com.vaultits.ifad.logic.Methods;
import com.vaultits.ifad.models.appointment;
import com.vaultits.ifad.retrofit.ApiInterface;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TripAdpater extends RecyclerView.Adapter<TripAdpater.AppointmentViewholder> {

    private Context mContext;
    private ArrayList<appointment> appointments;
    private Dialog mDialog;
    private activities_helper activities_helper;

    //retrofit
    private ApiInterface apiInterface;
    public TripAdpater(Context context, ArrayList<appointment> apps) {
        this.mContext = context;
        this.appointments = apps;
        this.mDialog = new Dialog(context);
        this.apiInterface = apiInterface;
        activities_helper = new activities_helper(mContext, "", null);
    }
    @NonNull
    @Override
    public TripAdpater.AppointmentViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        try {
            view = LayoutInflater.from(mContext).inflate(R.layout.trip_row_item, parent, false);
        } catch (Exception e) {
            Toast.makeText(mContext, "Error adapter create view holder", Toast.LENGTH_SHORT).show();
        }
        return new TripAdpater.AppointmentViewholder(view);
    }

    public void onBindViewHolder(@NonNull TripAdpater.AppointmentViewholder holder, @SuppressLint("RecyclerView") int position) {
        final appointment appointment = appointments.get(position);
        holder.topic.setText("Destination : " + appointment.getmTopic());
        holder.site.setText("VehicleID: " + appointment.getmSite());
        holder.start.setText("Departure : " + appointment.getmStart());

        holder.pretrip.setText("Pre-use : " +appointment.getmPreuse());
        holder.preuse.setText(" Security: " + appointment.getmSec());
        holder.sec.setText("Pre Trip : " + appointment.getmPretrip());

        Button edit = (Button) holder.mEdit_App;
        Button delete = (Button) holder.mDelete_App;
        Button options = (Button) holder.mOptions;
        //options
        options.setOnClickListener(v -> {
            //Creating the instance of PopupMenu
            final PopupMenu popup = new PopupMenu(mContext, holder.mOptions, Gravity.CENTER);
            //Inflating the Popup using xml file
            popup.getMenuInflater().inflate(R.menu.activity_options, popup.getMenu());
            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.action_add_participants) {
                    //go to add participants activity
                    Intent intent = new Intent(mContext, tripupdate.class);
                    intent.putExtra("id",appointment.getmID());
                    mContext.startActivity(intent);
                } else if (item.getItemId() == R.id.action_view_participants) {
                    //go to view participants activity pass the activity id
                    Intent intent = new Intent(mContext, Preuse.class);
                    intent.putExtra("id", appointment.getmID());
                    intent.putExtra("site", appointment.getmSite());
                    intent.putExtra("topic", appointment.getmTopic());
                    mContext.startActivity(intent);
                } else if(item.getItemId() == R.id.action_add_file){
                    //go to add attach file activity
                    Intent intent = new Intent(mContext, ActivityFileActivity.class);
                    intent.putExtra("id",appointment.getmID());
                    mContext.startActivity(intent);
                } else if(item.getItemId() == R.id.action_view_files){
                    //go to view files activity
                    Intent view_files = new Intent(mContext, SyncActivityFilesActivity.class);
                    view_files.putExtra("id",appointment.getmID());
                    mContext.startActivity(view_files);
                }
                return false;
            });
            popup.show();
        });

        edit.setOnClickListener(v -> {
            checkin( appointment.getmID());
        });

        delete.setOnClickListener(v -> {
            checkout( appointment.getmID());
        });
        //click events for row item
        holder.appointment_card_view.setOnClickListener(v -> {
            try {

            } catch (Exception e) {
                Toast.makeText(mContext, e.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void checkout(String user) {
        try {
            RequestBody username = RequestBody.create(MultipartBody.FORM, user);
            Call<ResponseBody> login = apiInterface.checkout(username);
            Methods.showDialog(mDialog, "Signing in...", true);
            login.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        Methods.showDialog(mDialog, "dismiss", false);
                        String result = response.body().string();

                        String[] tokens = Methods.removeQoutes(result).split(":");
                        String message = tokens[0];

                        if (message.equalsIgnoreCase("Failed")) {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                            builder.setCancelable(true);
                            builder.setTitle("Response");
                            builder.setMessage("Checkout failed");
                        } else if (message.equalsIgnoreCase("Success")) {

                            final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                            builder.setCancelable(true);
                            builder.setTitle("Response");
                            builder.setMessage("Checkout Sucess");
                        }

                    } catch (Exception e) {
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    try {
                        Methods.showDialog(mDialog, "dismiss", false);
                        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setCancelable(true);
                        builder.setTitle("Response");
                        builder.setMessage(t.toString());
                    } catch (Exception e) {
                    }
                }
            });
        } catch (Exception e) {
        }
    }
    private void checkin(String user) {
        try {
            RequestBody username = RequestBody.create(MultipartBody.FORM, user);
            Call<ResponseBody> login = apiInterface.checkin(username);
            Methods.showDialog(mDialog, "Signing in...", true);
            login.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        Methods.showDialog(mDialog, "dismiss", false);
                        String result = response.body().string();

                        String[] tokens = Methods.removeQoutes(result).split(":");
                        String message = tokens[0];

                        if (message.equalsIgnoreCase("Failed")) {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                            builder.setCancelable(true);
                            builder.setTitle("Response");
                            builder.setMessage("Checkout failed");
                        } else if (message.equalsIgnoreCase("Success")) {

                            final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                            builder.setCancelable(true);
                            builder.setTitle("Response");
                            builder.setMessage("Checkout Sucess");
                        }

                    } catch (Exception e) {
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    try {
                        Methods.showDialog(mDialog, "dismiss", false);
                        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setCancelable(true);
                        builder.setTitle("Response");
                        builder.setMessage(t.toString());
                    } catch (Exception e) {
                    }
                }
            });
        } catch (Exception e) {
        }
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    public class AppointmentViewholder extends RecyclerView.ViewHolder {

        public TextView topic, site, start, end ,sec,pretrip,preuse;
        public CardView appointment_card_view;
        public Button mEdit_App;
        public Button mOptions;
        public Button mDelete_App;

        public AppointmentViewholder(@NonNull View itemView) {
            super(itemView);
            topic = itemView.findViewById(R.id.topic);
            site = itemView.findViewById(R.id.site);
            start = itemView.findViewById(R.id.start_date);
            end = itemView.findViewById(R.id.end_date);
            sec = itemView.findViewById(R.id.sec);
            pretrip =itemView.findViewById(R.id.pretrip);
            preuse = itemView.findViewById(R.id.preuse);
            mOptions = itemView.findViewById(R.id.options);
            appointment_card_view = itemView.findViewById(R.id.games_cardview);
            mEdit_App = itemView.findViewById(R.id.edit_appointment);
            mDelete_App = itemView.findViewById(R.id.delete_appointment);
        }
    }}
