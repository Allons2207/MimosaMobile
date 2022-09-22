package com.vaultits.ifad;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.vaultits.ifad.logic.Methods;
import com.vaultits.ifad.navigation.MainNavigationDrawerActivity;
import com.vaultits.ifad.retrofit.APIClient;
import com.vaultits.ifad.retrofit.ApiInterface;

import org.json.JSONObject;

import java.io.File;

import cz.msebera.android.httpclient.Header;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private Button mLogin;//login button
    private TextInputEditText mUsername;//username field
    private TextInputEditText mPassword;//password field
    private Dialog mDialog;//progress dialog
    //retrofit
    private ApiInterface apiInterface;
    //login details preferences
    private SharedPreferences sharedPreferences;

    public static final String ACC_PREFERENCES = "AccountPreferences";
    public static final String UserId = "user_id";
    public static final String IsLogged = "is_logged";
    private static final int FILE_SYSTEM = 100;

    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            //check if user is logged
            boolean result = Methods.isLogged(this);
            //check login preferences if user is logged in or not
            if (result) {
                //logged in
                //navigate to main dashboard
                Intent intent = new Intent(this, MainNavigationDrawerActivity.class);
                startActivity(intent);
            } else {
                //show login screen
                //set layout
                setContentView(R.layout.activity_main);

                mLogin = (Button) findViewById(R.id.login);
                mUsername = (TextInputEditText) findViewById(R.id.username);
                mPassword = (TextInputEditText) findViewById(R.id.password);
                mDialog = new Dialog(this);
                dialog = new ProgressDialog(this);

                sharedPreferences = getSharedPreferences(MainActivity.ACC_PREFERENCES, Context.MODE_PRIVATE);
                apiInterface = APIClient.getApiClient().create(ApiInterface.class);
                mLogin.setOnClickListener(v -> {
                    try {
                        String user = mUsername.getText().toString().trim();
                        String pass = mPassword.getText().toString().trim();
                        if (user.equalsIgnoreCase("") || pass.equalsIgnoreCase("")) {
                            Methods.showAlert("Missing info", "Enter all details", this);
                            return;
                        }
                        login(user, pass);
                    } catch (Exception e) {
                        Toast.makeText(this, "Error raising event.", Toast.LENGTH_SHORT).show();
                    }
                });

                //ask for file permissions
                try {
                    //get user permission to read/write file system
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, FILE_SYSTEM);
                } catch (
                        Exception e) {
                    System.out.println(e);
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error loading UI.", Toast.LENGTH_SHORT).show();
        }
    }

    //login method
    private void login(String user, String pass) {
        try {
            RequestBody username = RequestBody.create(MultipartBody.FORM, user);
            RequestBody password = RequestBody.create(MultipartBody.FORM, pass);
            Call<ResponseBody> login = apiInterface.UserLogin(username, password);
            Methods.showDialog(mDialog, "Signing in...", true);
            login.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        Methods.showDialog(mDialog, "dismiss", false);
                        String result = response.body().string();

                        String[] tokens = Methods.removeQoutes(result).split(":");
                        String message = tokens[0];

                        if (message.equalsIgnoreCase("Invalid username or password")) {
                            Methods.showAlert("Response", "Invalid username or password.", MainActivity.this);
                        } else if (message.equalsIgnoreCase("Success")) {
                            //methods.showAlert("Response", "Sign in successful.", UserLoginAndSignUpActivity.this);
                            Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                            String user_id = tokens[1];
                            //create account prefs
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(MainActivity.UserId, user_id);
                            editor.putBoolean(MainActivity.IsLogged, true);
                            editor.apply();
                            //clear edit texts
                            mUsername.setText("");
                            mPassword.setText("");
                            Intent main = new Intent(MainActivity.this, MainNavigationDrawerActivity.class);
                            MainActivity.this.startActivity(main);
                        } else if (message.equalsIgnoreCase("Error")) {
                            Methods.showAlert("Response", "Server error.", MainActivity.this);
                        } else {
                            Methods.showAlert("Response", message, MainActivity.this);
                        }
                        

                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, "Error " + e.toString(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    try {
                        Methods.showDialog(mDialog, "dismiss", false);
                        Methods.showAlert("Request failed", t.toString(), MainActivity.this);

                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, "Error " + e.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == FILE_SYSTEM) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //check if media is mounted and if there is enough space when creating app files
                File root = Environment.getExternalStorageDirectory();
                File base_dir = new File(root.getAbsolutePath() + getResources().getString(R.string.base_dir));
                if (!base_dir.exists()) {
                    base_dir.mkdirs();
                }
            } else {
                Toast.makeText(getApplicationContext(), "You don't have permission to access file system !", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
