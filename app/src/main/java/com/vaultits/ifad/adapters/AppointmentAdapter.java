package com.vaultits.ifad.adapters;

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
import com.vaultits.ifad.activities.ViewParticipantsActivity;
import com.vaultits.ifad.database.captured_data.activities_helper;
import com.vaultits.ifad.models.appointment;

import java.util.ArrayList;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewholder> {

    private Context mContext;
    private ArrayList<appointment> appointments;
    private Dialog mDialog;
    private activities_helper activities_helper;

    public AppointmentAdapter(Context context, ArrayList<appointment> apps) {
        this.mContext = context;
        this.appointments = apps;
        this.mDialog = new Dialog(context);
        activities_helper = new activities_helper(mContext, "", null);
    }

    @NonNull
    @Override
    public AppointmentViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        try {
            view = LayoutInflater.from(mContext).inflate(R.layout.appointment_row_item, parent, false);
        } catch (Exception e) {
            Toast.makeText(mContext, "Error adapter create view holder", Toast.LENGTH_SHORT).show();
        }
        return new AppointmentAdapter.AppointmentViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewholder holder, @SuppressLint("RecyclerView") int position) {
        final appointment appointment = appointments.get(position);
        holder.topic.setText("Topic : " + appointment.getmTopic());
        holder.site.setText("Site : " + appointment.getmSite());
        holder.start.setText("Start : " + appointment.getmStart());
        holder.end.setText("End : " + appointment.getmEnd());

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
                    Intent intent = new Intent(mContext, AddParticipantFromViewingActivity.class);
                    intent.putExtra("id",appointment.getmID());
                    mContext.startActivity(intent);
                } else if (item.getItemId() == R.id.action_view_participants) {
                    //go to view participants activity pass the activity id
                    Intent intent = new Intent(mContext, ViewParticipantsActivity.class);
                    intent.putExtra("id",appointment.getmID());
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
        //edit
        edit.setOnClickListener(v -> {
            try {
                //go to edit intent
                Intent intent = new Intent(mContext, EditActivityActivity.class);
                intent.putExtra("id", appointment.getmID());
                intent.putExtra("site", appointment.getmSite());
                intent.putExtra("topic", appointment.getmTopic());
                intent.putExtra("comment", appointment.getmComment());
                intent.putExtra("start", appointment.getmStart());
                intent.putExtra("end", appointment.getmEnd());
                intent.putExtra("date", appointment.getmDateCaptured());

                intent.putExtra("project", appointment.getmProject());
                intent.putExtra("org", appointment.getmOrganisation());
                intent.putExtra("comp", appointment.getmComponent());
                intent.putExtra("sub_comp", appointment.getmSubComponent());
                intent.putExtra("activity", appointment.getmActivity());
                intent.putExtra("status", appointment.getmStatus());
                intent.putExtra("facilitator", appointment.getmFacilitator());
                intent.putExtra("scheme", appointment.getmScheme());
                intent.putExtra("dis", appointment.getmDistrict());
                mContext.startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(mContext, "Error occurred.", Toast.LENGTH_SHORT).show();
            }
        });
        delete.setOnClickListener(v -> {
            try {

                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setCancelable(true);
                builder.setTitle("Delete");
                builder.setMessage("Are you sure you want to delete this appointment?");
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //delete
                                if (activities_helper.deleteAppointment(appointment.getmID())) {
                                    Toast.makeText(mContext, "Appointment deleted.", Toast.LENGTH_SHORT).show();
                                    appointments.remove(position);
                                    notifyItemRemoved(position);
                                } else {
                                    Toast.makeText(mContext, "Error deleting.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            } catch (Exception e) {
                Toast.makeText(mContext, "Error occurred.", Toast.LENGTH_SHORT).show();
            }
        });
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
