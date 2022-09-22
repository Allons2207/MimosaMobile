package com.vaultits.ifad.adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.vaultits.ifad.R;
import com.vaultits.ifad.activities.EditParticipantActivity;
import com.vaultits.ifad.database.captured_data.activities_helper;
import com.vaultits.ifad.models.appointment;
import com.vaultits.ifad.models.participant;

import java.util.ArrayList;

public class ParticipantAdapter extends RecyclerView.Adapter<ParticipantAdapter.ParticipantViewholder> {

    private Context mContext;
    private ArrayList<participant> participants;
    private Dialog mDialog;
    private activities_helper activities_helper;

    public ParticipantAdapter(Context context, ArrayList<participant> apps) {
        this.mContext = context;
        this.participants = apps;
        this.mDialog = new Dialog(context);
        activities_helper = new activities_helper(mContext, "", null);
    }

    @NonNull
    @Override
    public ParticipantViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        try {
            view = LayoutInflater.from(mContext).inflate(R.layout.participant_row_item, parent, false);
        } catch (Exception e) {
            Toast.makeText(mContext, "Error adapter create view holder", Toast.LENGTH_SHORT).show();
        }
        return new ParticipantAdapter.ParticipantViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParticipantViewholder holder, int position) {
        final participant part = participants.get(position);
        holder.topic.setText(part.getmName() + " " + part.getmSurname());
        holder.site.setText(part.getmGender());
        holder.start.setText(part.getmNationalID());
        holder.end.setText(part.getmContact());

        Button edit = (Button) holder.mEdit_App;
        Button delete = (Button) holder.mDelete_App;
        //edit
        edit.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(mContext, EditParticipantActivity.class);
                intent.putExtra("id",part.getmID());
                intent.putExtra("name",part.getmName());
                intent.putExtra("surname",part.getmSurname());
                intent.putExtra("national_id",part.getmNationalID());
                intent.putExtra("contact",part.getmContact());
                intent.putExtra("gender",part.getmGender());
                intent.putExtra("district",part.getmDistrict());
                intent.putExtra("ward",part.getmWard());
                intent.putExtra("yob",part.getmYob());
                intent.putExtra("project",part.getmProject());
                intent.putExtra("appointment",part.getmAppointmentID());
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
                builder.setMessage("Are you sure you want to delete this participant?");
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //delete
                                if(activities_helper.deleteParticipant(part.getmID(),part.getmNationalID())){
                                    Toast.makeText(mContext, "Participant deleted", Toast.LENGTH_SHORT).show();
                                    participants.remove(position);
                                    notifyItemRemoved(position);
                                }else {
                                    Toast.makeText(mContext, "Error trying to delete.", Toast.LENGTH_SHORT).show();
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
                //Toast.makeText(mContext, "Hesi", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(mContext, e.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return participants.size();
    }

    public class ParticipantViewholder extends RecyclerView.ViewHolder {

        public TextView topic, site, start, end;
        public CardView appointment_card_view;
        public Button mEdit_App;
        public Button mDelete_App;

        public ParticipantViewholder(@NonNull View itemView) {
            super(itemView);
            topic = itemView.findViewById(R.id.topic);
            site = itemView.findViewById(R.id.site);
            start = itemView.findViewById(R.id.start_date);
            end = itemView.findViewById(R.id.end_date);
            appointment_card_view = itemView.findViewById(R.id.games_cardview);
            mEdit_App = itemView.findViewById(R.id.edit_participant);
            mDelete_App = itemView.findViewById(R.id.delete_participant);
        }
    }

}
