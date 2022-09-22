package com.vaultits.ifad.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import com.vaultits.ifad.database.look_up_data.budget_activity_list_helper;
import com.vaultits.ifad.database.look_up_data.component_sub_component_helper;
import com.vaultits.ifad.database.look_up_data.project_organisation_helper;
import com.vaultits.ifad.database.look_up_data.prov_dis_helper;
import com.vaultits.ifad.logic.Methods;

import java.util.ArrayList;
import java.util.Calendar;

public class EditActivityActivity extends AppCompatActivity {

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

    private Intent mIntent;
    private String mAppointmentID;

    private String mProject;
    private String mOrganisation;
    private String mComponent;
    private String mSub_component;
    private String mActivity;
    private String mStatus;
    private String mFacilitator;
    private String mScheme;
    private String mDistrict;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            //set UI
            setContentView(R.layout.activity_edit_activity);

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
            mSave = (Button) findViewById(R.id.update_activity);

            //set current data
            mIntent = getIntent();
            mAppointmentID = mIntent.getStringExtra("id");
            mPlace.setText(mIntent.getStringExtra("site"));
            mTopic.setText(mIntent.getStringExtra("topic"));
            mComment.setText(mIntent.getStringExtra("comment"));
            mStart.setText(mIntent.getStringExtra("start"));
            mEnd.setText(mIntent.getStringExtra("end"));

            mProject = mIntent.getStringExtra("project");
            mOrganisation = mIntent.getStringExtra("org");
            mComponent = mIntent.getStringExtra("comp");
            mSub_component = mIntent.getStringExtra("sub_comp");
            mActivity = mIntent.getStringExtra("activity");
            mStatus = mIntent.getStringExtra("status");
            mFacilitator = mIntent.getStringExtra("facilitator");
            mScheme = mIntent.getStringExtra("scheme");
            mDistrict = mIntent.getStringExtra("dis");

            //populate projects spinner
            ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, project_organisation_helper.getProjects());
            itemsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mProjects.setAdapter(itemsAdapter);
            //select the current saved project
            ArrayList<String> projects = project_organisation_helper.getProjects();
            mProjects.setSelection(projects.indexOf(project_organisation_helper.getProjectName(mProject)));

            //populate organisations spinner
            ArrayAdapter<String> organisationsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, project_organisation_helper.getOrganisations());
            organisationsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mOrganisations.setAdapter(organisationsAdapter);
            //set saved org
            ArrayList<String> orgs = project_organisation_helper.getOrganisations();
            mOrganisations.setSelection(orgs.indexOf(project_organisation_helper.getOrganisationName(mOrganisation)));

            //populate components spinner
            ArrayAdapter<String> components = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, component_sub_component_helper.getComponents());
            components.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mComponents.setAdapter(components);
            //set saved comps
            ArrayList<String> comps = component_sub_component_helper.getComponents();
            mComponents.setSelection(comps.indexOf(component_sub_component_helper.getComponent(mComponent)));

            //on item selected listener
            mComponents.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //select sub-components of a component
                    String myComponent = mComponents.getSelectedItem().toString();
                    ArrayAdapter<String> itemsAdapter1 = new ArrayAdapter<String>(EditActivityActivity.this, android.R.layout.simple_spinner_item, component_sub_component_helper.getSubComponents(component_sub_component_helper.componentID(myComponent)));
                    itemsAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mSubComponents.setAdapter(itemsAdapter1);
                    //set saved sub-comps
                    ArrayList<String> comps = component_sub_component_helper.getSubComponents(component_sub_component_helper.componentID(myComponent));
                    mSubComponents.setSelection(comps.indexOf(component_sub_component_helper.getSubComp(mSub_component)));
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
                    ArrayAdapter<String> acts = new ArrayAdapter<String>(EditActivityActivity.this, android.R.layout.simple_spinner_item, component_sub_component_helper.getActivitiesOfSubComponent(component_sub_component_helper.subComponentId(mySubComponent)));
                    acts.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mActivities.setAdapter(acts);
                    //set saved activities
                    ArrayList<String> actss = component_sub_component_helper.getActivitiesOfSubComponent(component_sub_component_helper.subComponentId(mySubComponent));
                    mActivities.setSelection(actss.indexOf(activities_facilitator_helper.getActivityDescription(mActivity)));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            //populate status spinner
            ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, activities_facilitator_helper.getActivityStatuses());
            statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mStatuses.setAdapter(statusAdapter);
            //set saved status
            ArrayList<String> statuses = activities_facilitator_helper.getActivityStatuses();
            mStatuses.setSelection(statuses.indexOf(activities_facilitator_helper.getActivityStatus(mStatus)));

            //populate districts spinner
            ArrayAdapter<String> districts = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, prov_dis_helper.getDistrictsOfProject(project_organisation_helper.getDistrictIds(project_organisation_helper.projectID(mProjects.getSelectedItem().toString()))));
            districts.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mDistricts.setAdapter(districts);
            //set saved district
            ArrayList<String> dis = prov_dis_helper.getDistrictsOfProject(project_organisation_helper.getDistrictIds(project_organisation_helper.projectID(mProjects.getSelectedItem().toString())));
            mDistricts.setSelection(dis.indexOf(prov_dis_helper.getDistrictName(mDistrict)));

            //populate users spinner
            ArrayAdapter<String> users = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, activities_facilitator_helper.getUsers());
            users.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mFacilitators.setAdapter(users);
            //set saved facilitator
            ArrayList<String> fac = activities_facilitator_helper.getUsers();
            mFacilitators.setSelection(fac.indexOf(activities_facilitator_helper.getFullName(mFacilitator)));

            //populate schemes spinner
            ArrayAdapter<String> schemes = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, project_organisation_helper.getSchemes());
            schemes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSchemes.setAdapter(schemes);
            //set saved district
            ArrayList<String> sch = project_organisation_helper.getSchemes();
            mSchemes.setSelection(sch.indexOf(project_organisation_helper.getSchemeName(mScheme)));

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

                    String scheme_id = project_organisation_helper.getSchemeId(mSchemes.getSelectedItem().toString());
                    String facilitator_id = activities_facilitator_helper.getUserId(mFacilitators.getSelectedItem().toString());
                    String district_id = prov_dis_helper.disID(mDistricts.getSelectedItem().toString());
                    String status_id = activities_facilitator_helper.getActivityStatusId(mStatuses.getSelectedItem().toString());
                    String activity_id = activities_facilitator_helper.getActivityId(mActivities.getSelectedItem().toString());
                    String sub_comp_id = component_sub_component_helper.subComponentId(mSubComponents.getSelectedItem().toString());
                    String comp_id = component_sub_component_helper.componentID(mComponents.getSelectedItem().toString());
                    String organisation_id = project_organisation_helper.organisationID(mOrganisations.getSelectedItem().toString());
                    String project_id = project_organisation_helper.projectID(mProjects.getSelectedItem().toString());

                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setCancelable(true);
                    builder.setTitle("Continue");
                    builder.setMessage("Are you sure you want to update? Please check the information before committing.");
                    String finalThe_comment = the_comment;
                    String finalUser_id = user_id;
                    builder.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    boolean update = activities_helper.updateActivity(project_id, organisation_id, comp_id, sub_comp_id, activity_id, status_id, district_id, site, facilitator_id, topic, finalThe_comment, start, end, date_captured, finalUser_id, scheme_id, mAppointmentID);
                                    if (update) {
                                        Methods.Alert("Success", "Activity Successfully Edited.", EditActivityActivity.this);
                                    } else {
                                        Methods.Alert("Failed", "Failed to Edit Activity.", EditActivityActivity.this);
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
                    Toast.makeText(this, "Error raising save event.", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "Error loading UI.", Toast.LENGTH_SHORT).show();
        }
    }
}