package com.vaultits.ifad.adapters;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.vaultits.ifad.R;
import com.vaultits.ifad.SyncActivityFilesActivity;
import com.vaultits.ifad.activities.ActivityFileActivity;
import com.vaultits.ifad.activities.EditActivityActivity;
import com.vaultits.ifad.activities.Preuse;
import com.vaultits.ifad.activities.pretripelect;
import com.vaultits.ifad.activities.pretripmechanical;
import com.vaultits.ifad.activities.pretriptyre;
import com.vaultits.ifad.activities.tripupdate;
import com.vaultits.ifad.database.captured_data.activities_helper;
import com.vaultits.ifad.models.appointment;

import java.util.ArrayList;
public class InpectAdpater extends  RecyclerView.Adapter<InpectAdpater.AppointmentViewholder> {
    private Context mContext;
    private ArrayList<appointment> appointments;
    private Dialog mDialog;
    private activities_helper activities_helper;

    public InpectAdpater(Context context, ArrayList<appointment> apps) {
        this.mContext = context;
        this.appointments = apps;
        this.mDialog = new Dialog(context);
        activities_helper = new activities_helper(mContext, "", null);
    }
    @NonNull
    @Override
    public InpectAdpater.AppointmentViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        try {
            view = LayoutInflater.from(mContext).inflate(R.layout.activity_inpect_adpater, parent, false);
        } catch (Exception e) {
            Toast.makeText(mContext, "Error adapter create view holder", Toast.LENGTH_SHORT).show();
        }
        return new InpectAdpater.AppointmentViewholder(view);
    }

    public void onBindViewHolder(@NonNull InpectAdpater.AppointmentViewholder holder, @SuppressLint("RecyclerView") int position) {
        final appointment appointment = appointments.get(position);
        holder.topic.setText("Destination : " + appointment.getmTopic());
        holder.site.setText("VehicleID: " + appointment.getmSite());
        holder.start.setText("Departure : " + appointment.getmStart());
        holder.end.setText("Pre-use Status: " + "Fit for travel");
        holder.end.setText("Checkpoint Status: " + "Cleared");
        holder.end.setText("Pre Trip Status: " + "N/A");


        Button options = (Button) holder.mOptions;
        //options
        options.setOnClickListener(v -> {
            //Creating the instance of PopupMenu
            final PopupMenu popup = new PopupMenu(mContext, holder.mOptions, Gravity.CENTER);
            //Inflating the Popup using xml file
            popup.getMenuInflater().inflate(R.menu.activity_drawer_pretrip, popup.getMenu());
            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.elee) {
                    //go to add participants activity
                    Intent intent = new Intent(mContext, pretripelect.class);
                    intent.putExtra("id",appointment.getmID());
                    mContext.startActivity(intent);
                } else if (item.getItemId() == R.id.mec) {
                    //go to view participants activity pass the activity id
                    Intent intent = new Intent(mContext, pretripmechanical.class);
                    intent.putExtra("id",appointment.getmID());
                    mContext.startActivity(intent);
                } else if(item.getItemId() == R.id.ty){
                    //go to add attach file activity
                    Intent intent = new Intent(mContext, pretriptyre.class);
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
        //edit

        //click events for row item
        holder.appointment_card_view.setOnClickListener(v -> {
            try {

            } catch (Exception e) {
                Toast.makeText(mContext, e.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    public class AppointmentViewholder extends RecyclerView.ViewHolder {

        public TextView topic, site, start, end;
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
            mOptions = itemView.findViewById(R.id.options);
            appointment_card_view = itemView.findViewById(R.id.games_cardview);
            mEdit_App = itemView.findViewById(R.id.edit_appointment);
            mDelete_App = itemView.findViewById(R.id.delete_appointment);
        }
    }
}