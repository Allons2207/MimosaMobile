package com.vaultits.ifad.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.vaultits.ifad.CapturedDataSyncActivity;
import com.vaultits.ifad.HomeFragment;
import com.vaultits.ifad.LookUpDataSyncActivity;
import com.vaultits.ifad.R;
import com.vaultits.ifad.activities.mytrip;
import com.vaultits.ifad.activities.pendinginsp;
import com.vaultits.ifad.activities.securitycheckpoint;
import com.vaultits.ifad.activities.buschedule;
import com.vaultits.ifad.activities.ViewActivitiesActivity;
import com.vaultits.ifad.activities.ViewParticipantsActivity;

public class MainNavigationDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            //set layout
            setContentView(R.layout.activity_drawer_main);

            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            NavigationView navigationView = findViewById(R.id.nav_view);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
            navigationView.setNavigationItemSelectedListener(this);

            //set title
            try {
                //getSupportActionBar().setTitle("");
            } catch (Exception e) {
                Toast.makeText(this, "Error setting title.", Toast.LENGTH_SHORT).show();
            }

            //use the home fragment as the default
            FragmentTransaction home = getSupportFragmentManager().beginTransaction();
            home.replace(R.id.mainFrame, new HomeFragment());
            home.commit();

            navigationView.setCheckedItem(R.id.nav_home);
        } catch (Exception e) {
            Toast.makeText(this, "Error loading UI.", Toast.LENGTH_SHORT).show();
        }
    }

    //onBackPressed
    @Override
    public void onBackPressed() {
        try {
            Fragment f = this.getSupportFragmentManager().findFragmentById(R.id.mainFrame);

            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else if (f != null && f instanceof HomeFragment) {
                finishAffinity();
            } else {
                FragmentTransaction home = getSupportFragmentManager().beginTransaction();
                home.replace(R.id.mainFrame, new HomeFragment());
                home.commit();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error onBackPressed.", Toast.LENGTH_SHORT).show();
        }
    }

    //method for setting action bar title in fragments
    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_navigation_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        try {
            int selected = item.getItemId();
            if (selected == R.id.action_settings) {

            } else if (selected == R.id.action_lookup_data_sync) {
                Intent look_up_data = new Intent(this, LookUpDataSyncActivity.class);
                startActivity(look_up_data);
            } else if (selected == R.id.action_captured_data_sync) {
                Intent sync_data = new Intent(this, CapturedDataSyncActivity.class);
                startActivity(sync_data);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Options menu error.", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int selected = item.getItemId();

        try {
            if (selected == R.id.nav_add_trips) {
                Intent add_activity = new Intent(this, mytrip.class);
                startActivity(add_activity);
            } else if (selected == R.id.nav_add_participants) {
                Intent add_participant = new Intent(this, securitycheckpoint.class);
                startActivity(add_participant);
            } else if (selected == R.id.nav_home) {
                FragmentTransaction home = getSupportFragmentManager().beginTransaction();
                home.replace(R.id.mainFrame, new HomeFragment());
                home.commit();
            } else if (selected == R.id.nav_view_activities) {
                Intent view_appointments = new Intent(this, pendinginsp.class);
                startActivity(view_appointments);
            }else if (selected == R.id.nav_add_bus) {
                Intent view_appointments = new Intent(this, buschedule.class);
                startActivity(view_appointments);
            }
            else if (selected == R.id.nav_view_participants) {
                Intent view_participants = new Intent(this, ViewParticipantsActivity.class);
                startActivity(view_participants);
            }
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}