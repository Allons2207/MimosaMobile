package com.vaultits.ifad;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.JsonParser;
import com.vaultits.ifad.database.look_up_data.activity_files;
import com.vaultits.ifad.database.look_up_data.budget_activity_list_helper;
import com.vaultits.ifad.database.look_up_data.component_sub_component_helper;
import com.vaultits.ifad.database.look_up_data.project_organisation_helper;
import com.vaultits.ifad.database.look_up_data.prov_dis_helper;
import com.vaultits.ifad.database.look_up_data.activities_facilitator_helper;
import com.vaultits.ifad.logic.Methods;
import com.vaultits.ifad.retrofit.APIClient;
import com.vaultits.ifad.retrofit.ApiInterface;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LookUpDataSyncActivity extends AppCompatActivity {

    private Button mSyncProjects;
    private Button mSyncOrganisations;
    private Button mSyncComponents;
    private Button mSyncSubComponents;
    private Button mSyncActivities;
    private Button mSyncActivityStatus;
    private Button mSyncProvinces;
    private Button mSyncDistricts;
    private Button mSyncFacilitators;
    private Button mSyncBudgetList;
    private Button mSyncSchemes;
    private Button mSyncProjectDistricts;
    private Button mGenders;
    private Button mWards;
    private Button mFileTypes;

    //retrofit
    private ApiInterface apiInterface;
    private Dialog mDialog;
    private Handler handler;

    private budget_activity_list_helper budget_activity_list_helper;
    private prov_dis_helper prov_dis_helper;
    private project_organisation_helper project_organisation_helper;
    private component_sub_component_helper component_sub_component_helper;
    private activities_facilitator_helper activities_facilitator_helper;
    private activity_files activity_files;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            //set layout
            setContentView(R.layout.activity_look_up_data_sync);

            //set title
            try {
                getSupportActionBar().setTitle("Look Up Data Sync");
            } catch (Exception e) {
                Toast.makeText(this, "Error setting title.", Toast.LENGTH_SHORT).show();
            }

            apiInterface = APIClient.getApiClient().create(ApiInterface.class);
            mDialog = new Dialog(this);
            prov_dis_helper = new prov_dis_helper(this, "", null);
            project_organisation_helper = new project_organisation_helper(this, "", null);
            component_sub_component_helper = new component_sub_component_helper(this, "", null);
            activities_facilitator_helper = new activities_facilitator_helper(this, "", null);
            budget_activity_list_helper = new budget_activity_list_helper(this, "", null);
            activity_files = new activity_files(this,"",null);

            //initialize widgets
            mSyncProjects = (Button) findViewById(R.id.sync_projects);
            mSyncOrganisations = (Button) findViewById(R.id.sync_organisations);
            mSyncComponents = (Button) findViewById(R.id.sync_components);
            mSyncSubComponents = (Button) findViewById(R.id.sync_sub_components);
            mSyncActivities = (Button) findViewById(R.id.sync_activities);
            mSyncActivityStatus = (Button) findViewById(R.id.sync_activity_status);
            mSyncProvinces = (Button) findViewById(R.id.sync_provinces);
            mSyncDistricts = (Button) findViewById(R.id.sync_districts);
            mSyncFacilitators = (Button) findViewById(R.id.sync_facilitators);
            mSyncSchemes = (Button) findViewById(R.id.sync_schemes);
            mSyncProjectDistricts = (Button) findViewById(R.id.sync_project_districts);
            mSyncBudgetList = (Button) findViewById(R.id.sync_budget_list);
            mGenders = (Button) findViewById(R.id.sync_genders);
            mWards = (Button) findViewById(R.id.sync_wards);
            mFileTypes = (Button)findViewById(R.id.file_types);

            handler = new Handler();

            mFileTypes.setOnClickListener(v -> {
                getFileTypes();
            });
            mSyncProjects.setOnClickListener(v -> {
                getProjects();
            });
            mSyncOrganisations.setOnClickListener(v -> {
                getOrganisations();
            });
            mSyncComponents.setOnClickListener(v -> {
                getComponents();
            });
            mSyncSubComponents.setOnClickListener(v -> {
                getSubComponents();
                getSubComponentActivities();
            });
            mSyncActivities.setOnClickListener(v -> {
                getActivities();
            });
            mSyncActivityStatus.setOnClickListener(v -> {
                getActivityStatus();
            });
            mSyncProvinces.setOnClickListener(v -> {
                getProvinces();
            });
            mSyncDistricts.setOnClickListener(v -> {
                getDistricts();
            });
            mSyncFacilitators.setOnClickListener(v -> {
                getUsers();
            });

            mSyncSchemes.setOnClickListener(v -> {
                getSchemes();
            });
            mSyncProjectDistricts.setOnClickListener(v -> {
                getProjectDistricts();
            });
            mSyncBudgetList.setOnClickListener(v -> {
                getBudgetList();
            });

            mGenders.setOnClickListener(v -> {
                getGender();
            });
            mWards.setOnClickListener(v -> {
                getWards();
            });
        } catch (Exception e) {
            Toast.makeText(this, "Error loading UI", Toast.LENGTH_SHORT).show();
        }
    }

    private void getFileTypes() {
        try {
            Call<ResponseBody> ftypes = apiInterface.getFileTypes();
            Methods.showDialog(mDialog, "Loading file types...", true);
            ftypes.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {

                        Methods.showDialog(mDialog, "Dismiss", false);
                        if (response.isSuccessful()) {
                            String responseData = response.body().string();
                            JsonParser parser = new JsonParser();
                            String result = parser.parse(responseData).getAsString();
                            JSONArray array = new JSONArray(result);
                            ArrayList<String> a;//province ids
                            ArrayList<String> b;//province names
                            a = new ArrayList<>();
                            b = new ArrayList<>();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject = array.getJSONObject(i);
                                a.add(jsonObject.getString("id"));
                                b.add(jsonObject.getString("des"));
                            }
                            activity_files.insert_file_types(a, b);
                            Methods.showAlert("File types Sync Result", "Successfully syncronized file types.", LookUpDataSyncActivity.this);

                        } else {
                            Toast.makeText(LookUpDataSyncActivity.this, "Request unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        System.out.println(e);
                        Methods.showAlert("Error", e.toString(), LookUpDataSyncActivity.this);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Methods.showDialog(mDialog, "Dismiss", false);
                    Methods.showAlert("Failure", t.toString(), LookUpDataSyncActivity.this);
                }
            });
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    //get provinces method
    private void getProvinces() {
        try {
            Call<ResponseBody> provs = apiInterface.getProvincies();
            Methods.showDialog(mDialog, "Loading provinces...", true);
            provs.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {

                        Methods.showDialog(mDialog, "Dismiss", false);
                        if (response.isSuccessful()) {
                            String responseData = response.body().string();
                            JsonParser parser = new JsonParser();
                            String result = parser.parse(responseData).getAsString();
                            JSONArray array = new JSONArray(result);
                            ArrayList<String> a;//province ids
                            ArrayList<String> b;//province names
                            ArrayList<String> c;//country ids
                            a = new ArrayList<>();
                            b = new ArrayList<>();
                            c = new ArrayList<>();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject = array.getJSONObject(i);
                                a.add(jsonObject.getString("pid"));
                                b.add(jsonObject.getString("name"));
                                c.add(jsonObject.getString("cid"));
                            }
                            prov_dis_helper.insert_provinces(a, b, c);
                            Methods.showAlert("Provinces Sync Result", "Successfully syncronized provinces form server.", LookUpDataSyncActivity.this);

                        } else {
                            Toast.makeText(LookUpDataSyncActivity.this, "Request unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        System.out.println(e);
                        Methods.showAlert("Error", e.toString(), LookUpDataSyncActivity.this);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Methods.showDialog(mDialog, "Dismiss", false);
                    Methods.showAlert("Failure", t.toString(), LookUpDataSyncActivity.this);
                }
            });
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    //get districts method
    private void getDistricts() {
        try {
            Call<ResponseBody> dis = apiInterface.getDistricts();
            Methods.showDialog(mDialog, "Loading districts...", true);
            dis.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        Methods.showDialog(mDialog, "Dismiss", false);
                        if (response.isSuccessful()) {
                            String responseData = response.body().string();
                            JsonParser parser = new JsonParser();
                            String result = parser.parse(responseData).getAsString();
                            JSONArray array = new JSONArray(result);
                            ArrayList<String> a;//district ids
                            ArrayList<String> b;//district names
                            ArrayList<String> c;//province ids
                            a = new ArrayList<>();
                            b = new ArrayList<>();
                            c = new ArrayList<>();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject = array.getJSONObject(i);
                                a.add(jsonObject.getString("did"));
                                b.add(jsonObject.getString("name"));
                                c.add(jsonObject.getString("pid"));
                            }
                            prov_dis_helper.insert_districts(a, b, c);
                            Methods.showAlert("Districts Sync Result", "Successfully syncronized districts form server.", LookUpDataSyncActivity.this);
                        } else {
                            Toast.makeText(LookUpDataSyncActivity.this, "Request unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        System.out.println(e);
                        Methods.showAlert("Error", e.toString(), LookUpDataSyncActivity.this);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Methods.showDialog(mDialog, "Dismiss", false);
                    Methods.showAlert("Failure", t.toString(), LookUpDataSyncActivity.this);
                }
            });
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    //get projects method
    private void getProjects() {
        try {
            Call<ResponseBody> dis = apiInterface.GetProjects();
            Methods.showDialog(mDialog, "Loading projects...", true);
            dis.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        Methods.showDialog(mDialog, "Dismiss", false);
                        if (response.isSuccessful()) {
                            String responseData = response.body().string();
                            JsonParser parser = new JsonParser();
                            String result = parser.parse(responseData).getAsString();
                            JSONArray array = new JSONArray(result);
                            ArrayList<String> a;//project ids
                            ArrayList<String> b;//project names
                            ArrayList<String> c;//acronyms
                            a = new ArrayList<>();
                            b = new ArrayList<>();
                            c = new ArrayList<>();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject = array.getJSONObject(i);
                                a.add(jsonObject.getString("project_id"));
                                b.add(jsonObject.getString("name"));
                                c.add(jsonObject.getString("acronym"));
                            }
                            project_organisation_helper.insert_projects(a, b, c);
                            Methods.showAlert("Project Sync Result", "Successfully syncronized projects.", LookUpDataSyncActivity.this);
                        } else {
                            Toast.makeText(LookUpDataSyncActivity.this, "Request unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        System.out.println(e);
                        Methods.showAlert("Error", e.toString(), LookUpDataSyncActivity.this);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Methods.showDialog(mDialog, "Dismiss", false);
                    Methods.showAlert("Failure", t.toString(), LookUpDataSyncActivity.this);
                }
            });
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    //get organisations method
    private void getOrganisations() {
        try {
            Call<ResponseBody> dis = apiInterface.GetOrganisations();
            Methods.showDialog(mDialog, "Loading organisations...", true);
            dis.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        Methods.showDialog(mDialog, "Dismiss", false);
                        if (response.isSuccessful()) {
                            String responseData = response.body().string();
                            JsonParser parser = new JsonParser();
                            String result = parser.parse(responseData).getAsString();
                            JSONArray array = new JSONArray(result);
                            ArrayList<String> a;//ids
                            ArrayList<String> b;//names
                            ArrayList<String> c;//des
                            a = new ArrayList<>();
                            b = new ArrayList<>();
                            c = new ArrayList<>();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject = array.getJSONObject(i);
                                a.add(jsonObject.getString("org_id"));
                                b.add(jsonObject.getString("name"));
                                c.add(jsonObject.getString("des"));
                            }
                            project_organisation_helper.insert_organisations(a, b, c);
                            Methods.showAlert("Organisations Sync Result", "Successfully syncronized organisations.", LookUpDataSyncActivity.this);
                        } else {
                            Toast.makeText(LookUpDataSyncActivity.this, "Request unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        System.out.println(e);
                        Methods.showAlert("Error", e.toString(), LookUpDataSyncActivity.this);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Methods.showDialog(mDialog, "Dismiss", false);
                    Methods.showAlert("Failure", t.toString(), LookUpDataSyncActivity.this);
                }
            });
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    //get components method
    private void getComponents() {
        try {
            Call<ResponseBody> dis = apiInterface.GetComponents();
            Methods.showDialog(mDialog, "Loading components...", true);
            dis.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        Methods.showDialog(mDialog, "Dismiss", false);
                        if (response.isSuccessful()) {
                            String responseData = response.body().string();
                            JsonParser parser = new JsonParser();
                            String result = parser.parse(responseData).getAsString();
                            JSONArray array = new JSONArray(result);
                            ArrayList<String> a;//ids
                            ArrayList<String> b;//des
                            a = new ArrayList<>();
                            b = new ArrayList<>();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject = array.getJSONObject(i);
                                a.add(jsonObject.getString("component_id"));
                                b.add(jsonObject.getString("des"));
                            }
                            component_sub_component_helper.insert_components(a, b);
                            Methods.showAlert("Components Sync Result", "Successfully syncronized components.", LookUpDataSyncActivity.this);
                        } else {
                            Toast.makeText(LookUpDataSyncActivity.this, "Request unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        System.out.println(e);
                        Methods.showAlert("Error", e.toString(), LookUpDataSyncActivity.this);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Methods.showDialog(mDialog, "Dismiss", false);
                    Methods.showAlert("Failure", t.toString(), LookUpDataSyncActivity.this);
                }
            });
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    //get sub-components method
    private void getSubComponents() {
        try {
            Call<ResponseBody> dis = apiInterface.GetSubComponents();
            Methods.showDialog(mDialog, "Loading sub-components...", true);
            dis.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        Methods.showDialog(mDialog, "Dismiss", false);
                        if (response.isSuccessful()) {
                            String responseData = response.body().string();
                            JsonParser parser = new JsonParser();
                            String result = parser.parse(responseData).getAsString();
                            JSONArray array = new JSONArray(result);
                            ArrayList<String> a;//sub-comp-ids
                            ArrayList<String> b;//comp-ids
                            ArrayList<String> c;//des
                            a = new ArrayList<>();
                            b = new ArrayList<>();
                            c = new ArrayList<>();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject = array.getJSONObject(i);
                                a.add(jsonObject.getString("sub_component_id"));
                                b.add(jsonObject.getString("component_id"));
                                c.add(jsonObject.getString("des"));
                            }
                            component_sub_component_helper.insert_sub_comps(a, b, c);
                            Methods.showAlert("Sub-Components Sync Result", "Successfully syncronized sub-components.", LookUpDataSyncActivity.this);
                        } else {
                            Toast.makeText(LookUpDataSyncActivity.this, "Request unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        System.out.println(e);
                        Methods.showAlert("Error", e.toString(), LookUpDataSyncActivity.this);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Methods.showDialog(mDialog, "Dismiss", false);
                    Methods.showAlert("Failure", t.toString(), LookUpDataSyncActivity.this);
                }
            });
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    //get activities method
    private void getActivities() {
        try {
            Call<ResponseBody> dis = apiInterface.GetActivities();
            Methods.showDialog(mDialog, "Loading activities...", true);
            dis.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        Methods.showDialog(mDialog, "Dismiss", false);
                        if (response.isSuccessful()) {
                            String responseData = response.body().string();
                            JsonParser parser = new JsonParser();
                            String result = parser.parse(responseData).getAsString();
                            JSONArray array = new JSONArray(result);
                            ArrayList<String> a;//activity ids
                            ArrayList<String> b;//des
                            a = new ArrayList<>();
                            b = new ArrayList<>();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject = array.getJSONObject(i);
                                a.add(jsonObject.getString("activity_id"));
                                b.add(jsonObject.getString("des"));
                            }
                            activities_facilitator_helper.insert_activities(a, b);
                            Methods.showAlert("Activities Sync Result", "Successfully syncronized activities.", LookUpDataSyncActivity.this);
                        } else {
                            Toast.makeText(LookUpDataSyncActivity.this, "Request unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        System.out.println(e);
                        Methods.showAlert("Error", e.toString(), LookUpDataSyncActivity.this);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Methods.showDialog(mDialog, "Dismiss", false);
                    Methods.showAlert("Failure", t.toString(), LookUpDataSyncActivity.this);
                }
            });
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    //get activity status method
    private void getActivityStatus() {
        try {
            Call<ResponseBody> dis = apiInterface.GetActivityStatus();
            Methods.showDialog(mDialog, "Loading activity statuses...", true);
            dis.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        Methods.showDialog(mDialog, "Dismiss", false);
                        if (response.isSuccessful()) {
                            String responseData = response.body().string();
                            JsonParser parser = new JsonParser();
                            String result = parser.parse(responseData).getAsString();
                            JSONArray array = new JSONArray(result);
                            ArrayList<String> a;//activity status ids
                            ArrayList<String> b;//des
                            a = new ArrayList<>();
                            b = new ArrayList<>();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject = array.getJSONObject(i);
                                a.add(jsonObject.getString("status_id"));
                                b.add(jsonObject.getString("des"));
                            }
                            activities_facilitator_helper.insert_activity_status(a, b);
                            Methods.showAlert("Activity Status Sync Result", "Successfully syncronized activity statuses.", LookUpDataSyncActivity.this);
                        } else {
                            Toast.makeText(LookUpDataSyncActivity.this, "Request unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        System.out.println(e);
                        Methods.showAlert("Error", e.toString(), LookUpDataSyncActivity.this);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Methods.showDialog(mDialog, "Dismiss", false);
                    Methods.showAlert("Failure", t.toString(), LookUpDataSyncActivity.this);
                }
            });
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    //get users
    private void getUsers() {
        try {
            Call<ResponseBody> dis = apiInterface.GetFacilitators();
            Methods.showDialog(mDialog, "Loading facilitators...", true);
            dis.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        Methods.showDialog(mDialog, "Dismiss", false);
                        if (response.isSuccessful()) {
                            String responseData = response.body().string();
                            JsonParser parser = new JsonParser();
                            String result = parser.parse(responseData).getAsString();
                            JSONArray array = new JSONArray(result);
                            ArrayList<String> a;//user id
                            ArrayList<String> b;//name
                            ArrayList<String> c;//surname
                            a = new ArrayList<>();
                            b = new ArrayList<>();
                            c = new ArrayList<>();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject = array.getJSONObject(i);
                                a.add(jsonObject.getString("fac_id"));
                                b.add(jsonObject.getString("name"));
                                c.add(jsonObject.getString("surname"));
                            }
                            activities_facilitator_helper.insert_users(a, b, c);
                            Methods.showAlert("Facilitators Sync Result", "Successfully syncronized facilitators.", LookUpDataSyncActivity.this);
                        } else {
                            Toast.makeText(LookUpDataSyncActivity.this, "Request unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        System.out.println(e);
                        Methods.showAlert("Error", e.toString(), LookUpDataSyncActivity.this);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Methods.showDialog(mDialog, "Dismiss", false);
                    Methods.showAlert("Failure", t.toString(), LookUpDataSyncActivity.this);
                }
            });
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    //get budget list
    private void getBudgetList() {
        try {
            Call<ResponseBody> dis = apiInterface.GetBudgetList();
            Methods.showDialog(mDialog, "Loading budget list...", true);
            dis.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        Methods.showDialog(mDialog, "Dismiss", false);
                        if (response.isSuccessful()) {
                            String responseData = response.body().string();
                            JsonParser parser = new JsonParser();
                            String result = parser.parse(responseData).getAsString();
                            JSONArray array = new JSONArray(result);
                            ArrayList<String> a;//user id
                            ArrayList<String> b;//name
                            ArrayList<String> c;//surname
                            a = new ArrayList<>();
                            b = new ArrayList<>();
                            c = new ArrayList<>();
                            ArrayList<String> d = new ArrayList<>();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject = array.getJSONObject(i);
                                a.add(jsonObject.getString("id"));
                                b.add(jsonObject.getString("activity"));
                                c.add(jsonObject.getString("comp_id"));
                                d.add(jsonObject.getString("sub_comp_id"));
                            }
                            budget_activity_list_helper.insert_budget_list(a, b, c, d);
                            Methods.showAlert("Budget List Sync Result", "Successfully syncronized budget list.", LookUpDataSyncActivity.this);
                        } else {
                            Toast.makeText(LookUpDataSyncActivity.this, "Request unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        System.out.println(e);
                        Methods.showAlert("Error", e.toString(), LookUpDataSyncActivity.this);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Methods.showDialog(mDialog, "Dismiss", false);
                    Methods.showAlert("Failure", t.toString(), LookUpDataSyncActivity.this);
                }
            });
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    //get project districts
    private void getProjectDistricts() {
        try {
            Call<ResponseBody> dis = apiInterface.GetProjectDistricts();
            Methods.showDialog(mDialog, "Loading project districts..", true);
            dis.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        Methods.showDialog(mDialog, "Dismiss", false);
                        if (response.isSuccessful()) {
                            String responseData = response.body().string();
                            JsonParser parser = new JsonParser();
                            String result = parser.parse(responseData).getAsString();
                            JSONArray array = new JSONArray(result);
                            ArrayList<String> a;//user id
                            ArrayList<String> b;//name
                            a = new ArrayList<>();
                            b = new ArrayList<>();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject = array.getJSONObject(i);
                                a.add(jsonObject.getString("project_id"));
                                b.add(jsonObject.getString("district_id"));
                            }
                            project_organisation_helper.insert_project_districts(a, b);
                            Methods.showAlert("Project Districts Sync Result", "Successfully syncronized project districts.", LookUpDataSyncActivity.this);
                        } else {
                            Toast.makeText(LookUpDataSyncActivity.this, "Request unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        System.out.println(e);
                        Methods.showAlert("Error", e.toString(), LookUpDataSyncActivity.this);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Methods.showDialog(mDialog, "Dismiss", false);
                    Methods.showAlert("Failure", t.toString(), LookUpDataSyncActivity.this);
                }
            });
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    //get schemes
    private void getSchemes() {
        try {
            Call<ResponseBody> dis = apiInterface.GetSchemes();
            Methods.showDialog(mDialog, "Loading schemes...", true);
            dis.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        Methods.showDialog(mDialog, "Dismiss", false);
                        if (response.isSuccessful()) {
                            String responseData = response.body().string();
                            JsonParser parser = new JsonParser();
                            String result = parser.parse(responseData).getAsString();
                            JSONArray array = new JSONArray(result);
                            ArrayList<String> a;//user id
                            ArrayList<String> b;//name
                            a = new ArrayList<>();
                            b = new ArrayList<>();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject = array.getJSONObject(i);
                                a.add(jsonObject.getString("scheme_id"));
                                b.add(jsonObject.getString("name"));
                            }
                            project_organisation_helper.insert_irrigation_schemes(a, b);
                            Methods.showAlert("Schemes Sync Result", "Successfully syncronized schemes.", LookUpDataSyncActivity.this);
                        } else {
                            Toast.makeText(LookUpDataSyncActivity.this, "Request unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        System.out.println(e);
                        Methods.showAlert("Error", e.toString(), LookUpDataSyncActivity.this);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Methods.showDialog(mDialog, "Dismiss", false);
                    Methods.showAlert("Failure", t.toString(), LookUpDataSyncActivity.this);
                }
            });
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    //get gender
    private void getGender() {
        try {
            Call<ResponseBody> dis = apiInterface.GetGender();
            Methods.showDialog(mDialog, "Loading gender data...", true);
            dis.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        Methods.showDialog(mDialog, "Dismiss", false);
                        if (response.isSuccessful()) {
                            String responseData = response.body().string();
                            JsonParser parser = new JsonParser();
                            String result = parser.parse(responseData).getAsString();
                            JSONArray array = new JSONArray(result);
                            ArrayList<String> a;//user id
                            ArrayList<String> b;//name
                            a = new ArrayList<>();
                            b = new ArrayList<>();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject = array.getJSONObject(i);
                                a.add(jsonObject.getString("id"));
                                b.add(jsonObject.getString("gender"));
                            }
                            prov_dis_helper.insert_gender(a, b);
                            Methods.showAlert("Gender Sync Result", "Successfully syncronized gender data.", LookUpDataSyncActivity.this);
                        } else {
                            Toast.makeText(LookUpDataSyncActivity.this, "Request unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        System.out.println(e);
                        Methods.showAlert("Error", e.toString(), LookUpDataSyncActivity.this);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Methods.showDialog(mDialog, "Dismiss", false);
                    Methods.showAlert("Failure", t.toString(), LookUpDataSyncActivity.this);
                }
            });
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    //get sub component activities
    private void getSubComponentActivities() {
        try {
            Call<ResponseBody> dis = apiInterface.GetSubComponentActivities();
            Methods.showDialog(mDialog, "Loading data...", true);
            dis.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        Methods.showDialog(mDialog, "Dismiss", false);
                        if (response.isSuccessful()) {
                            String responseData = response.body().string();
                            JsonParser parser = new JsonParser();
                            String result = parser.parse(responseData).getAsString();
                            JSONArray array = new JSONArray(result);
                            ArrayList<String> a;//user id
                            ArrayList<String> b;//name
                            a = new ArrayList<>();
                            b = new ArrayList<>();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject = array.getJSONObject(i);
                                a.add(jsonObject.getString("entry_id"));
                                b.add(jsonObject.getString("activity"));
                            }
                            component_sub_component_helper.insert_sub_comp_activities(a, b);
                            Methods.showAlert("Sub Comp Acts Sync Result", "Successfully syncronized data.", LookUpDataSyncActivity.this);
                        } else {
                            Toast.makeText(LookUpDataSyncActivity.this, "Request unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        System.out.println(e);
                        Methods.showAlert("Error", e.toString(), LookUpDataSyncActivity.this);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Methods.showDialog(mDialog, "Dismiss", false);
                    Methods.showAlert("Failure", t.toString(), LookUpDataSyncActivity.this);
                }
            });
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    //get wards
    private void getWards() {
        try {
            Call<ResponseBody> dis = apiInterface.GetWards();
            Methods.showDialog(mDialog, "Loading wards data...", true);
            dis.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        Methods.showDialog(mDialog, "Dismiss", false);
                        if (response.isSuccessful()) {
                            String responseData = response.body().string();
                            JsonParser parser = new JsonParser();
                            String result = parser.parse(responseData).getAsString();
                            JSONArray array = new JSONArray(result);
                            ArrayList<String> a;//user id
                            ArrayList<String> b;//name
                            a = new ArrayList<>();
                            b = new ArrayList<>();
                            ArrayList<String> c = new ArrayList<>();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject = array.getJSONObject(i);
                                a.add(jsonObject.getString("ward_id"));
                                b.add(jsonObject.getString("name"));
                                c.add(jsonObject.getString("district_id"));
                            }
                            new Thread(()->{
                                handler.post(()->{
                                    Methods.showDialog(mDialog,"Saving wards.",true);
                                });
                                prov_dis_helper.insert_wards(a, b, c);
                                handler.post(()->{
                                    Methods.showDialog(mDialog,"Saving wards.",false);
                                });
                            }).start();
                            Methods.showAlert("Wards Sync Result", "Successfully syncronized wards data.", LookUpDataSyncActivity.this);
                        } else {
                            Toast.makeText(LookUpDataSyncActivity.this, "Request unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        System.out.println(e);
                        Methods.showAlert("Error", e.toString(), LookUpDataSyncActivity.this);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Methods.showDialog(mDialog, "Dismiss", false);
                    Methods.showAlert("Failure", t.toString(), LookUpDataSyncActivity.this);
                }
            });
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}