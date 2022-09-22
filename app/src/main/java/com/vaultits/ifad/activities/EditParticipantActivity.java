package com.vaultits.ifad.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.vaultits.ifad.R;
import com.vaultits.ifad.database.captured_data.activities_helper;
import com.vaultits.ifad.database.look_up_data.activities_facilitator_helper;
import com.vaultits.ifad.database.look_up_data.component_sub_component_helper;
import com.vaultits.ifad.database.look_up_data.project_organisation_helper;
import com.vaultits.ifad.database.look_up_data.prov_dis_helper;
import com.vaultits.ifad.logic.Methods;

import java.util.ArrayList;

public class EditParticipantActivity extends AppCompatActivity {

    private prov_dis_helper prov_dis_helper;
    private project_organisation_helper project_organisation_helper;
    private component_sub_component_helper component_sub_component_helper;
    private activities_facilitator_helper activities_facilitator_helper;
    private activities_helper activities_helper;

    private Spinner mProjects;//project dropdown
    private Spinner mWards;//wards dropdown
    private Spinner mComponents;//components dropdown
    private Spinner mSubComponents;//sub-components dropdown
    private Spinner mActivities;//activities spinner
    private Spinner mGender;//gender spinner
    private Spinner mDistricts;//districts dropdown
    private Spinner mAppointments;//appointments dropdown

    private TextInputEditText mFirstName;
    private TextInputEditText mSurname;
    private TextInputEditText mNationalId;
    private TextInputEditText mYearOfBirth;
    private TextInputEditText mContactNo;
    private Button mSave;//save

    private Intent mIntent;
    private String mParticipantID;
    private String mProject;
    private String mDistrict;
    private String mAppointment;
    private String mWard;
    private String mSex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            //set layout
            setContentView(R.layout.activity_edit_participant);

            //database instances
            prov_dis_helper = new prov_dis_helper(this, "", null);
            project_organisation_helper = new project_organisation_helper(this, "", null);
            component_sub_component_helper = new component_sub_component_helper(this, "", null);
            activities_facilitator_helper = new activities_facilitator_helper(this, "", null);
            activities_helper = new activities_helper(this, "", null);

            //init widgets
            mProjects = (Spinner) findViewById(R.id.projects);
            mWards = (Spinner) findViewById(R.id.wards);
            mComponents = (Spinner) findViewById(R.id.components);
            mSubComponents = (Spinner) findViewById(R.id.sub_components);
            mActivities = (Spinner) findViewById(R.id.activities);
            mGender = (Spinner) findViewById(R.id.gender);
            mDistricts = (Spinner) findViewById(R.id.districts);
            mAppointments = (Spinner) findViewById(R.id.appointments);

            mFirstName = (TextInputEditText) findViewById(R.id.first_name);
            mSurname = (TextInputEditText) findViewById(R.id.surname);
            mNationalId = (TextInputEditText) findViewById(R.id.national_id);
            mYearOfBirth = (TextInputEditText) findViewById(R.id.year_of_birth);
            mContactNo = (TextInputEditText) findViewById(R.id.contact);

            //set current data
            mIntent = getIntent();
            mFirstName.setText(mIntent.getStringExtra("name"));
            mSurname.setText(mIntent.getStringExtra("surname"));
            mNationalId.setText(mIntent.getStringExtra("national_id"));
            mYearOfBirth.setText(mIntent.getStringExtra("yob"));
            mContactNo.setText(mIntent.getStringExtra("contact"));
            mParticipantID = mIntent.getStringExtra("id");

            mProject = mIntent.getStringExtra("project");
            mDistrict = mIntent.getStringExtra("district");
            mAppointment = mIntent.getStringExtra("appointment");
            mWard = mIntent.getStringExtra("ward");
            mSex = mIntent.getStringExtra("gender");

            mSave = (Button) findViewById(R.id.update_participant);
            //populate projects spinner
            ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, project_organisation_helper.getProjects());
            itemsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mProjects.setAdapter(itemsAdapter);
            //select the current saved project
            ArrayList<String> projects = project_organisation_helper.getProjects();
            mProjects.setSelection(projects.indexOf(project_organisation_helper.getProjectName(mProject)));

            //populate components spinner
            ArrayAdapter<String> components = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, component_sub_component_helper.getComponents());
            components.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mComponents.setAdapter(components);
            //on item selected listener
            mComponents.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //select sub-components of a component
                    String myComponent = mComponents.getSelectedItem().toString();
                    ArrayAdapter<String> itemsAdapter1 = new ArrayAdapter<String>(EditParticipantActivity.this, android.R.layout.simple_spinner_item, component_sub_component_helper.getSubComponents(component_sub_component_helper.componentID(myComponent)));
                    itemsAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mSubComponents.setAdapter(itemsAdapter1);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            //sub components on item click listener
            mSubComponents.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //select sub-components of a component
                    String mySubComponent = mSubComponents.getSelectedItem().toString();
                    ArrayAdapter<String> acts = new ArrayAdapter<String>(EditParticipantActivity.this, android.R.layout.simple_spinner_item, component_sub_component_helper.getActivitiesOfSubComponent(component_sub_component_helper.subComponentId(mySubComponent)));
                    acts.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mActivities.setAdapter(acts);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            //populate districts spinner
            ArrayAdapter<String> districts = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, prov_dis_helper.getDistrictsOfProject(project_organisation_helper.getDistrictIds(project_organisation_helper.projectID(mProjects.getSelectedItem().toString()))));
            districts.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mDistricts.setAdapter(districts);
            //select the current saved district
            ArrayList<String> dis = prov_dis_helper.getDistrictsOfProject(project_organisation_helper.getDistrictIds(project_organisation_helper.projectID(mProjects.getSelectedItem().toString())));
            mDistricts.setSelection(dis.indexOf(prov_dis_helper.getDistrictName(mDistrict)));

            mDistricts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String district = mDistricts.getSelectedItem().toString();
                    ArrayAdapter<String> acts = new ArrayAdapter<String>(EditParticipantActivity.this, android.R.layout.simple_spinner_item, prov_dis_helper.getWardsOfDistrict(prov_dis_helper.disID(district)));
                    acts.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mWards.setAdapter(acts);
                    //select the current saved ward
                    ArrayList<String> ward = prov_dis_helper.getWardsOfDistrict(prov_dis_helper.disID(district));
                    mWards.setSelection(ward.indexOf(prov_dis_helper.getWardName(mWard)));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            //populate gender
            ArrayAdapter<String> gender = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, prov_dis_helper.getGenders());
            gender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mGender.setAdapter(gender);
            if (mSex.equalsIgnoreCase("Male")) {
                mGender.setSelection(0);
            } else if (mSex.equalsIgnoreCase("Female")) {
                mGender.setSelection(1);
            } else {
                mGender.setSelection(2);
            }

            //populate gender
            ArrayAdapter<String> appoints = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, activities_helper.getAppointments());
            appoints.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mAppointments.setAdapter(appoints);
            //select the current saved appointment
            ArrayList<String> apps = activities_helper.getAppointments();
            mAppointments.setSelection(apps.indexOf(activities_helper.getAppointment(mAppointment)));

            //save
            mSave.setOnClickListener(v -> {
                try {

                    String fname = mFirstName.getText().toString().trim();
                    String surname = mSurname.getText().toString().trim();
                    String natid = mNationalId.getText().toString().trim();
                    String sex = mGender.getSelectedItem().toString();
                    String contact = mContactNo.getText().toString().trim();
                    String yob = mYearOfBirth.getText().toString().trim();
                    if (fname.equalsIgnoreCase("") || surname.equalsIgnoreCase("")) {
                        Methods.showAlert("Missing info", "Enter all details.", this);
                        return;
                    }
                    if (natid.equalsIgnoreCase("") || sex.equalsIgnoreCase("")) {
                        Methods.showAlert("Missing info", "Enter all details.", this);
                        return;
                    }

                    if (contact.equalsIgnoreCase("") || yob.equalsIgnoreCase("")) {
                        Methods.showAlert("Missing info", "Enter all details.", this);
                        return;
                    }

                    String ward_id = prov_dis_helper.getWardId(mWards.getSelectedItem().toString(), prov_dis_helper.disID(mDistricts.getSelectedItem().toString()));
                    String district_id = prov_dis_helper.disID(mDistricts.getSelectedItem().toString());
                    //String activity_id = activities_facilitator_helper.getActivityId(mActivities.getSelectedItem().toString());
                    //String sub_comp_id = component_sub_component_helper.subComponentId(mSubComponents.getSelectedItem().toString());
                    //String comp_id = component_sub_component_helper.componentID(mComponents.getSelectedItem().toString());
                    String project_id = project_organisation_helper.projectID(mProjects.getSelectedItem().toString());
                    String appointment_id = activities_helper.getAppointmentID(mAppointments.getSelectedItem().toString());
                    String activity_id = "0";
                    String sub_comp_id = "0";
                    String comp_id = "0";

                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setCancelable(true);
                    builder.setTitle("Continue");
                    builder.setMessage("Are you sure you want to update? Please check the information before committing.");
                    builder.setPositiveButton("Yes",
                            (dialog, which) -> {
                                boolean update = activities_helper.updateParticipant(project_id, comp_id, sub_comp_id, activity_id, district_id, ward_id, fname, surname, natid, sex, yob, contact, appointment_id, this, mParticipantID);
                                if (update) {
                                    Methods.Alert("Success", "Participant Successfully Edited.", this);
                                } else {
                                    Methods.Alert("Failed", "Failed to Edit Participant.", this);
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
                    Toast.makeText(this, "Error raising save event.", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "Error loading UI.", Toast.LENGTH_SHORT).show();
        }
    }
}