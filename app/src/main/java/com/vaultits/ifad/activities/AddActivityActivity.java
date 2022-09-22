package com.vaultits.ifad.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.vaultits.ifad.R;
import com.vaultits.ifad.database.captured_data.activities_helper;
import com.vaultits.ifad.database.look_up_data.activities_facilitator_helper;
import com.vaultits.ifad.database.look_up_data.budget_activity_list_helper;
import com.vaultits.ifad.database.look_up_data.component_sub_component_helper;
import com.vaultits.ifad.database.look_up_data.project_organisation_helper;
import com.vaultits.ifad.database.look_up_data.prov_dis_helper;
import com.vaultits.ifad.logic.Methods;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;

public class AddActivityActivity extends AppCompatActivity {

    private budget_activity_list_helper budget_activity_list_helper;
    private prov_dis_helper prov_dis_helper;
    private project_organisation_helper project_organisation_helper;
    private component_sub_component_helper component_sub_component_helper;
    private activities_facilitator_helper activities_facilitator_helper;
    private activities_helper activities_helper;

    private Spinner mProjects;//project dropdown
    private Spinner mOrganisations;//organisations dropdown
    private Spinner mComponents;//components dropdown
    private Spinner mSubComponents;//sub-components dropdown
    private Spinner mActivities;//activities spinner
    private Spinner mStatuses;//statuses spinner
    private Spinner mDistricts;//districts dropdown
    private TextInputEditText mPlace;//place/site
    private Spinner mFacilitators;//users spinner
    private TextInputEditText mTopic;//topic
    private TextInputEditText mComment;//comment
    private Spinner mSchemes;//schemes spinner
    private Button mStart;//start date
    private Button mEnd;//end date
    private Button mSave;//save

    Calendar calendar;//calender
    int year, month, day;
    private Dialog mDialog;//custom dialog
    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            //set UI
            setContentView(R.layout.activity_add_activity);

            //database instances
            budget_activity_list_helper = new budget_activity_list_helper(this, "", null);
            prov_dis_helper = new prov_dis_helper(this, "", null);
            project_organisation_helper = new project_organisation_helper(this, "", null);
            component_sub_component_helper = new component_sub_component_helper(this, "", null);
            activities_facilitator_helper = new activities_facilitator_helper(this, "", null);
            activities_helper = new activities_helper(this, "", null);

            //init widgets
            mProjects = (Spinner) findViewById(R.id.projects);
            mOrganisations = (Spinner) findViewById(R.id.organisations);
            mComponents = (Spinner) findViewById(R.id.components);
            mSubComponents = (Spinner) findViewById(R.id.sub_components);
            mActivities = (Spinner) findViewById(R.id.activities);
            mStatuses = (Spinner) findViewById(R.id.status);
            mDistricts = (Spinner) findViewById(R.id.districts);
            mFacilitators = (Spinner) findViewById(R.id.facilitator);
            mSchemes = (Spinner) findViewById(R.id.schemes);
            calendar = Calendar.getInstance();
            mDialog = new Dialog(this);

            mPlace = (TextInputEditText) findViewById(R.id.place);
            mTopic = (TextInputEditText) findViewById(R.id.topic);
            mComment = (TextInputEditText) findViewById(R.id.comment);
            mStart = (Button) findViewById(R.id.start_date);
            mEnd = (Button) findViewById(R.id.end_date);
            mSave = (Button) findViewById(R.id.save_activity);

            //populate projects spinner
            ArrayList<String> projects = project_organisation_helper.getProjects();
            projects.add(0,"--Select Option--");
            ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, projects);
            itemsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mProjects.setAdapter(itemsAdapter);

            //project on selected listener
            mProjects.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //populate districts spinner
                    String proj = mProjects.getSelectedItem().toString();
                    ArrayList<String> dists = prov_dis_helper.getDistrictsOfProject(project_organisation_helper.getDistrictIds(project_organisation_helper.projectID(proj)));
                    dists.add(0,"--Select Option--");
                    ArrayAdapter<String> districts = new ArrayAdapter<String>(AddActivityActivity.this, android.R.layout.simple_spinner_item,dists);
                    districts.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mDistricts.setAdapter(districts);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            //populate organisations spinner
            ArrayList<String> orgs = project_organisation_helper.getOrganisations();
            orgs.add(0,"--Select Option--");
            ArrayAdapter<String> organisationsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, orgs);
            organisationsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mOrganisations.setAdapter(organisationsAdapter);

            //populate components spinner
            ArrayList<String> comps = component_sub_component_helper.getComponents();
            comps.add(0,"--Select Option--");
            ArrayAdapter<String> components = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, comps);
            components.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mComponents.setAdapter(components);
            //on item selected listener
            mComponents.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //select sub-components of a component
                    String myComponent = mComponents.getSelectedItem().toString();
                    ArrayList<String> subc = component_sub_component_helper.getSubComponents(component_sub_component_helper.componentID(myComponent));
                    subc.add(0,"--Select Option--");
                    ArrayAdapter<String> itemsAdapter1 = new ArrayAdapter<String>(AddActivityActivity.this, android.R.layout.simple_spinner_item, subc);
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
                    ArrayList<String> act = component_sub_component_helper.getActivitiesOfSubComponent(component_sub_component_helper.subComponentId(mySubComponent));
                    act.add(0,"--Select Option--");
                    ArrayAdapter<String> acts = new ArrayAdapter<String>(AddActivityActivity.this, android.R.layout.simple_spinner_item, act);
                    acts.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mActivities.setAdapter(acts);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            //populate status spinner
            ArrayList<String> sts = activities_facilitator_helper.getActivityStatuses();
            sts.add(0,"--Select Option--");
            ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sts);
            statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mStatuses.setAdapter(statusAdapter);
            //populate districts spinner
//            ArrayAdapter<String> districts = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, prov_dis_helper.getDistrictsOfProject(project_organisation_helper.getDistrictIds(project_organisation_helper.projectID(mProjects.getSelectedItem().toString()))));
//            districts.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            mDistricts.setAdapter(districts);
            //populate users spinner
            ArrayList<String> facs = activities_facilitator_helper.getUsers();
            facs.add(0,"--Select Option--");
            ArrayAdapter<String> users = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, facs);
            users.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mFacilitators.setAdapter(users);
            //populate users spinner
            ArrayList<String> irri = project_organisation_helper.getSchemes();
            irri.add(0,"--Select Option--");
            ArrayAdapter<String> schemes = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, irri);
            schemes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSchemes.setAdapter(schemes);

            //set get start date button click event
            mStart.setOnClickListener(v -> {
                if (v == mStart) {
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
                        mStart.setText(datenow);
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
            //save
            mSave.setOnClickListener(v -> {
                try {
                    String user_id = "";
                    if (Methods.getUserId(this) == "0") {
                        Methods.showAlert("Error", "Could not get user id.", this);
                        return;
                    }
                    user_id = String.valueOf(Methods.getUserId(this));
                    String date_captured = Methods.getDateForSqlServer();
                    String site = mPlace.getText().toString().trim();
                    String topic = mTopic.getText().toString().trim();
                    String the_comment = "";
                    String comment = mComment.getText().toString().trim();
                    String start = mStart.getText().toString().trim();
                    String end = mEnd.getText().toString().trim();
                    if (site.equalsIgnoreCase("") || topic.equalsIgnoreCase("")) {
                        Methods.showAlert("Missing info", "Enter site or topic.", this);
                        return;
                    }
                    if (start.equalsIgnoreCase("Start date") || end.equalsIgnoreCase("End date")) {
                        Methods.showAlert("Missing info", "Enter start or end date.", this);
                        return;
                    }
                    if (comment.equalsIgnoreCase("")) {
                        the_comment = "N/A";
                    } else {
                        the_comment = mComment.getText().toString().trim();
                    }

                    if(mProjects.getSelectedItem().toString().equalsIgnoreCase("--Select Option--")){
                        Toast.makeText(this, "Select all options", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if(mOrganisations.getSelectedItem().toString().equalsIgnoreCase("--Select Option--")){
                        Toast.makeText(this, "Select all options", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if(mComponents.getSelectedItem().toString().equalsIgnoreCase("--Select Option--")){
                        Toast.makeText(this, "Select all options", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(mSubComponents.getSelectedItem().toString().equalsIgnoreCase("--Select Option--")){
                        Toast.makeText(this, "Select all options", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if(mActivities.getSelectedItem().toString().equalsIgnoreCase("--Select Option--")){
                        Toast.makeText(this, "Select all options", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(mStatuses.getSelectedItem().toString().equalsIgnoreCase("--Select Option--")){
                        Toast.makeText(this, "Select all options", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(mDistricts.getSelectedItem().toString().equalsIgnoreCase("--Select Option--")){
                        Toast.makeText(this, "Select all options", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(mFacilitators.getSelectedItem().toString().equalsIgnoreCase("--Select Option--")){
                        Toast.makeText(this, "Select all options", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(mSchemes.getSelectedItem().toString().equalsIgnoreCase("--Select Option--")){
                        Toast.makeText(this, "Select all options", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String scheme_id = project_organisation_helper.getSchemeId(mSchemes.getSelectedItem().toString());
                    String facilitator_id = activities_facilitator_helper.getUserId(mFacilitators.getSelectedItem().toString());
                    String district_id = prov_dis_helper.disID(mDistricts.getSelectedItem().toString());
                    String status_id = activities_facilitator_helper.getActivityStatusId(mStatuses.getSelectedItem().toString());
                    String activity_id = activities_facilitator_helper.getActivityId(mActivities.getSelectedItem().toString());
                    String sub_comp_id = component_sub_component_helper.subComponentId(mSubComponents.getSelectedItem().toString());
                    String comp_id = component_sub_component_helper.componentID(mComponents.getSelectedItem().toString());
                    String organisation_id = project_organisation_helper.organisationID(mOrganisations.getSelectedItem().toString());
                    String project_id = project_organisation_helper.projectID(mProjects.getSelectedItem().toString());

                    String insert = activities_helper.insertActivity(project_id, organisation_id, comp_id, sub_comp_id, activity_id, status_id, district_id, site, facilitator_id, topic, the_comment, start, end, date_captured, user_id, scheme_id);
                    if (insert.equalsIgnoreCase("Success")) {
                        Methods.Alert("Success", "Activity Successfully Saved", this);
                    } else if (insert.equalsIgnoreCase("Exist")) {
                        Methods.Alert("Exist", "Activity Already Exist", this);
                    } else {
                        Methods.Alert("Failed", "Failed to Save Activity.", this);
                    }
                } catch (Exception e) {
                    Toast.makeText(this, "Error raising save event.", Toast.LENGTH_SHORT).show();
                }
            });

            //remove focus on edit texts when the activity loads
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        } catch (Exception e) {
            Toast.makeText(this, "Error loading UI.", Toast.LENGTH_SHORT).show();
        }
    }
}