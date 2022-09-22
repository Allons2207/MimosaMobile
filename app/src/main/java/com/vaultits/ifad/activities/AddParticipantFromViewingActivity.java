package com.vaultits.ifad.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
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
import java.util.Calendar;

public class AddParticipantFromViewingActivity extends AppCompatActivity {

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

    private String mActivityID;

    Calendar calendar;//calender
    int year, month, day;
    DatePickerDialog datePickerDialog;
    private Button mDateCreated;
    private TextInputEditText mCreatedBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            //set layout
            setContentView(R.layout.activity_add_participant_from_viewing);

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
            mDateCreated = (Button)findViewById(R.id.created_date);
            mCreatedBy = (TextInputEditText)findViewById(R.id.created_by);

            mFirstName = (TextInputEditText) findViewById(R.id.first_name);
            mSurname = (TextInputEditText) findViewById(R.id.surname);
            mNationalId = (TextInputEditText) findViewById(R.id.national_id);
            mYearOfBirth = (TextInputEditText) findViewById(R.id.year_of_birth);
            mContactNo = (TextInputEditText) findViewById(R.id.contact);
            calendar = Calendar.getInstance();

            Intent intent = getIntent();
            mActivityID = intent.getStringExtra("id");

            mSave = (Button) findViewById(R.id.save_participant);
            //populate projects spinner
            ArrayList<String> projects = project_organisation_helper.getProjects();
            projects.add(0,"--Select Option--");
            ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, projects);
            itemsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mProjects.setAdapter(itemsAdapter);

            mProjects.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //populate districts spinner
                    String proj = mProjects.getSelectedItem().toString();
                    ArrayList<String> dists = prov_dis_helper.getDistrictsOfProject(project_organisation_helper.getDistrictIds(project_organisation_helper.projectID(proj)));
                    dists.add(0,"--Select Option--");
                    ArrayAdapter<String> districts = new ArrayAdapter<String>(AddParticipantFromViewingActivity.this, android.R.layout.simple_spinner_item,dists);
                    districts.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mDistricts.setAdapter(districts);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            //populate components spinner
            ArrayList<String> componentsList = component_sub_component_helper.getComponents();
            componentsList.add(0,"");
            ArrayAdapter<String> components = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, componentsList);
            components.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mComponents.setAdapter(components);
            //on item selected listener
            mComponents.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //select sub-components of a component
                    String myComponent = mComponents.getSelectedItem().toString();
                    ArrayList<String> sub_comps = component_sub_component_helper.getSubComponents(component_sub_component_helper.componentID(myComponent));
                    sub_comps.add(0,"");
                    ArrayAdapter<String> itemsAdapter1 = new ArrayAdapter<String>(AddParticipantFromViewingActivity.this, android.R.layout.simple_spinner_item, sub_comps);
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
                    ArrayAdapter<String> acts = new ArrayAdapter<String>(AddParticipantFromViewingActivity.this, android.R.layout.simple_spinner_item, component_sub_component_helper.getActivitiesOfSubComponent(component_sub_component_helper.subComponentId(mySubComponent)));
                    acts.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mActivities.setAdapter(acts);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

//            ArrayList<String> dists = prov_dis_helper.getDistrictsOfProject(project_organisation_helper.getDistrictIds(project_organisation_helper.projectID(mProjects.getSelectedItem().toString())));
//            dists.add(0,"");
//            ArrayAdapter<String> districts = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,dists);
//            districts.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            mDistricts.setAdapter(districts);

            //districts on click listener
            mDistricts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String district = mDistricts.getSelectedItem().toString();
                    ArrayList<String> wrds = prov_dis_helper.getWardsOfDistrict(prov_dis_helper.disID(district));
                    wrds.add(0,"--Select Option--");
                    ArrayAdapter<String> acts = new ArrayAdapter<String>(AddParticipantFromViewingActivity.this, android.R.layout.simple_spinner_item, wrds);
                    acts.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mWards.setAdapter(acts);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            //populate gender
            ArrayList<String> gens = prov_dis_helper.getGenders();
            gens.add(0,"--Select Option--");
            ArrayAdapter<String> gender = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, gens);
            gender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mGender.setAdapter(gender);

            //populate appointments
            ArrayAdapter<String> appoints = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, activities_helper.getAppointments());
            appoints.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mAppointments.setAdapter(appoints);

            //set get date created button click event
            mDateCreated.setOnClickListener(v -> {
                if (v == mDateCreated) {
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
                        mDateCreated.setText(datenow);
                    }, year, month, day);
                    datePickerDialog.show();
                }
            });
            //remove focus on edit texts when the activity loads
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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

                    if(mProjects.getSelectedItem().toString().equalsIgnoreCase("--Select Option--")){
                        Toast.makeText(this, "Select all options", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if(mDistricts.getSelectedItem().toString().equalsIgnoreCase("--Select Option--")){
                        Toast.makeText(this, "Select all options", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if(mWards.getSelectedItem().toString().equalsIgnoreCase("--Select Option--")){
                        Toast.makeText(this, "Select all options", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if(mGender.getSelectedItem().toString().equalsIgnoreCase("--Select Option--")){
                        Toast.makeText(this, "Select all options", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String ward_id = prov_dis_helper.getWardId(mWards.getSelectedItem().toString(),prov_dis_helper.disID(mDistricts.getSelectedItem().toString()));
                    String district_id = prov_dis_helper.disID(mDistricts.getSelectedItem().toString());
                    //String activity_id = activities_facilitator_helper.getActivityId(mActivities.getSelectedItem().toString());
                    //String sub_comp_id = component_sub_component_helper.subComponentId(mSubComponents.getSelectedItem().toString());
                    //String comp_id = component_sub_component_helper.componentID(mComponents.getSelectedItem().toString());
                    String project_id = project_organisation_helper.projectID(mProjects.getSelectedItem().toString());
                    String appointment_id = activities_helper.getAppointmentID(mAppointments.getSelectedItem().toString());

                    String activity_id = "0";
                    String sub_comp_id = "0";
                    String comp_id = "0";
                    
                    String insert = activities_helper.insertParticipant(project_id, comp_id, sub_comp_id, activity_id, district_id, ward_id, fname, surname, natid, sex, yob, contact,mActivityID,this);
                    if (insert.equalsIgnoreCase("Success")) {
                        Methods.Alert("Success", "Participant Successfully Saved", this);
                    } else if (insert.equalsIgnoreCase("Exist")) {
                        Methods.Alert("Exist", "Participant Already Exist", this);
                    } else {
                        Methods.Alert("Failed", "Failed to Save Participant.", this);
                    }
                } catch (Exception e) {
                    Toast.makeText(this, "Error raising save event.", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "Error loading UI.", Toast.LENGTH_SHORT).show();
        }
    }
}