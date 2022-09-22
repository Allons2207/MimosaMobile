package com.vaultits.ifad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.vaultits.ifad.activities.ViewParticipantsActivity;
import com.vaultits.ifad.adapters.FileAdapter;
import com.vaultits.ifad.adapters.ParticipantAdapter;
import com.vaultits.ifad.database.captured_data.activities_helper;
import com.vaultits.ifad.models.file;

import java.util.ArrayList;

public class SyncActivityFilesActivity extends AppCompatActivity {

    private RecyclerView mFilesRecycler;
    private TextView mNoOfObjects;
    private activities_helper activities_helper;
    private Intent mIntent;
    private ArrayList<file> mList;
    private FileAdapter mFileAdapter;
    private ParticipantAdapter participantAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            //inflate UI
            setContentView(R.layout.activity_sync_files);

            //initialise widgets
            mNoOfObjects = (TextView) findViewById(R.id.no_of_objects);
            mFilesRecycler = (RecyclerView) findViewById(R.id.files_list_recycler);
            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            mFilesRecycler.setLayoutManager(linearLayoutManager);
            mFilesRecycler.setItemAnimator(new DefaultItemAnimator());

            mList = new ArrayList<>();
            activities_helper = new activities_helper(this, "", null);
            mIntent = getIntent();
            String activity_id = mIntent.getStringExtra("id");

            file file;
            Cursor cursor = activities_helper.getFiles(activity_id);
            if (cursor != null) {
                while (cursor.moveToNext()){
                    file = new file();
                    String id = cursor.getString(cursor.getColumnIndex("ID"));
                    String title = cursor.getString(cursor.getColumnIndex("TITLE"));
                    String author = cursor.getString(cursor.getColumnIndex("AUTHOR"));
                    String date = cursor.getString(cursor.getColumnIndex("DATE"));
                    String path = cursor.getString(cursor.getColumnIndex("DOCPATH"));
                    String uri = cursor.getString(cursor.getColumnIndex("DOCURL"));
                    String appointment_id = cursor.getString(cursor.getColumnIndex("APPOINTMENTID"));
                    String sync_status = cursor.getString(cursor.getColumnIndex("SYNCSTATUS"));
                    file.setmID(id);
                    file.setmTitle(title);
                    file.setmAuthor(author);
                    file.setDate(date);
                    file.setmPath(path);
                    file.setmAppointmentID(appointment_id);
                    file.setmStatus(sync_status);
                    file.setmUri(uri);
                    mList.add(file);
                }
            } else {
                Toast.makeText(this, "Database error.", Toast.LENGTH_SHORT).show();
            }

            mFileAdapter = new FileAdapter(this,mList);
            mFilesRecycler.setAdapter(mFileAdapter);
        } catch (Exception e) {
            Toast.makeText(this, "Error loading UI.", Toast.LENGTH_SHORT).show();
        }
    }
}