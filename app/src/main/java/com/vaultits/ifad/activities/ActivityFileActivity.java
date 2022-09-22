package com.vaultits.ifad.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.vaultits.ifad.R;
import com.vaultits.ifad.database.captured_data.activities_helper;
import com.vaultits.ifad.database.look_up_data.activity_files;
import com.vaultits.ifad.logic.Methods;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

public class ActivityFileActivity extends AppCompatActivity {

    private Spinner mActivities;
    private Spinner mFileTypes;
    private Spinner mMimeTypes;
    private activities_helper activities_helper;
    private activity_files activity_files;
    private Calendar calendar;//calender
    private int year, month, day;
    private Dialog mDialog;//custom dialog
    private DatePickerDialog datePickerDialog;
    private TextInputEditText mTitle;
    private TextInputEditText mAuthor;
    private TextInputEditText mDescription;
    private TextInputEditText mAuthorOrganisation;
    private String mFilePath;
    private Uri mFileUri;
    private Button mDate;
    private ImageButton mAttach;
    private Button mSave;
    private static final int FILE_SYSTEM = 101;
    private String mActivityID;
    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            //set interface
            setContentView(R.layout.activity_file);
            mActivities = (Spinner) findViewById(R.id.activities);
            mFileTypes = (Spinner) findViewById(R.id.file_types);
            mTitle = (TextInputEditText) findViewById(R.id.file_title);
            mAuthor = (TextInputEditText) findViewById(R.id.file_author);
            mDescription = (TextInputEditText) findViewById(R.id.file_description);
            mAuthorOrganisation = (TextInputEditText) findViewById(R.id.file_author_organisation);
            calendar = Calendar.getInstance();
            mDate = (Button) findViewById(R.id.date);
            mAttach = (ImageButton) findViewById(R.id.attach_file);
            mSave = (Button) findViewById(R.id.save_file);
            mMimeTypes = (Spinner)findViewById(R.id.mimeSpinner);

            mIntent = getIntent();
            mActivityID = mIntent.getStringExtra("id");

            //db
            activities_helper = new activities_helper(this, "", null);
            activity_files = new activity_files(this, "", null);

            //populate file types
            ArrayList<String> file_types = activity_files.getFileTypes();
            file_types.add(0, "--Select Option--");
            ArrayAdapter<String> types = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, file_types);
            types.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mFileTypes.setAdapter(types);

            //populate activities
            ArrayList<String> apps = activities_helper.getAppointments();
            apps.add(0, "--Select Option--");
            ArrayAdapter<String> appoints = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, apps);
            appoints.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mActivities.setAdapter(appoints);

            //date
            mDate.setOnClickListener(v -> {
                if (v == mDate) {
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
                        mDate.setText(datenow);
                    }, year, month, day);
                    datePickerDialog.show();
                }
            });
            mAttach.setOnClickListener(v -> {
                try {
                    //check for file system permissions
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                            PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                        File root1 = Environment.getExternalStorageDirectory();
                        File base_dir = new File(root1.getAbsolutePath() + getString(R.string.base_dir));
                        if (!base_dir.exists()) {
                            base_dir.mkdirs();
                        }

                        String doc_type = mMimeTypes.getSelectedItem().toString();
                        long type = mMimeTypes.getSelectedItemId();
                        if (type == 0) {
                            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                            intent.addCategory(Intent.CATEGORY_OPENABLE);
                            intent.setType("application/pdf");
                            startActivityForResult(intent, FILE_SYSTEM);
                        } else if (type == 1) {
                            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                            intent.addCategory(Intent.CATEGORY_OPENABLE);
                            intent.setType("*/*");
                            String[] mime_types = {"application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/msword"};
                            intent.putExtra(Intent.EXTRA_MIME_TYPES, mime_types);
                            startActivityForResult(intent, FILE_SYSTEM);
                        } else if (type == 2) {
                            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                            intent.addCategory(Intent.CATEGORY_OPENABLE);
                            intent.setType("*/*");
                            String[] mime_types = {"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "application/vnd.ms-excel"};
                            intent.putExtra(Intent.EXTRA_MIME_TYPES, mime_types);
                            startActivityForResult(intent, FILE_SYSTEM);
                        } else if (type == 3) {
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.setType("audio/*");
                            startActivityForResult(intent, FILE_SYSTEM);
                        } else if (type == 4) {
                            Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setType("image/*");
                            startActivityForResult(intent, FILE_SYSTEM);
                        }
                    } else {
                        //ask for it
                        askForPermission();
                    }
                } catch (Exception e) {
                    Toast.makeText(this, "Error raising event.", Toast.LENGTH_SHORT).show();
                }
            });
            //remove focus on edit texts when the activity loads
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            //save
            mSave.setOnClickListener(v -> {
                if (mFilePath == null || mFileUri == null) {
                    Toast.makeText(this, "Please select a file.", Toast.LENGTH_SHORT).show();
                    return;
                }
                String file_type = activity_files.getTypeId(mFileTypes.getSelectedItem().toString());
                String title = mTitle.getText().toString().trim();
                String author = mDescription.getText().toString().trim();
                String des = mDescription.getText().toString().trim();
                String date = mDate.getText().toString().trim();
                String sql_date = null;
                if (date.equalsIgnoreCase("Date")) {
                    Toast.makeText(this, "Enter date.", Toast.LENGTH_SHORT).show();
                } else {
                    sql_date = Methods.toSqlDate(date);
                }
                String author_org = mAuthor.getText().toString().trim();
                if (title.equalsIgnoreCase("") || author.equalsIgnoreCase("")) {
                    Toast.makeText(this, "Enter all details.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (des.equalsIgnoreCase("") || author_org.equalsIgnoreCase("")) {
                    Toast.makeText(this, "Enter all details.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mActivityID == null) {
                    Toast.makeText(this, "Activity id not found.", Toast.LENGTH_SHORT).show();
                    return;
                }
                String insert = activities_helper.insertFile(file_type, mActivityID, title, author, des, sql_date, author_org, mFilePath, String.valueOf(mFileUri), this);
                if (insert.equalsIgnoreCase("Success")) {
                    Toast.makeText(this, "File details saved successfully.", Toast.LENGTH_SHORT).show();
                } else if (insert.equalsIgnoreCase("Exist")) {
                    Toast.makeText(this, "File details already exist.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Error. Could not save.", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "Error loading UI", Toast.LENGTH_SHORT).show();
        }
    }

    private void askForPermission() {
        //get user permission to read/write file system
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, FILE_SYSTEM);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == FILE_SYSTEM && resultCode == RESULT_OK && data != null) {
            try {
                Uri uri = data.getData();
                mFileUri = data.getData();

                String sub_dir = getResources().getString(R.string.files);
                File file = Methods.convertUriToFile(mFileUri, sub_dir, this);
                mFilePath = file.getPath();

                String filename = file.toString().substring(file.toString().lastIndexOf("/") + 1);
                Toast.makeText(this, filename, Toast.LENGTH_LONG).show();

            } catch (Exception e) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}