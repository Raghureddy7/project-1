package com.loginregfirestore;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

public class DashboardScreen extends AppCompatActivity {
    TextView tvtextwelcome;

    private ActionBarDrawerToggle t;
    private NavigationView nv;
    private DrawerLayout layoutwidgetdrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        getSupportActionBar().setTitle("Home");
        navigationView();

        tvtextwelcome = (TextView) findViewById(R.id.tvtextwelcome);
        tvtextwelcome.setText(getIntent().getStringExtra("message"));
        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View view) {
                Fragment fragment = null;
                if (view == findViewById(R.id.selfDetails)) {
                    fragment = new ProfileScreen();
                }
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.fragmentLayout, fragment);
                transaction.commit();
            }
        };

        Button selfDetails = (Button) findViewById(R.id.selfDetails);
        selfDetails.setOnClickListener(listener);


    }

    private void navigationView() {
        layoutwidgetdrawer = (DrawerLayout) findViewById(R.id.layoutwidgetdrawer);
        t = new ActionBarDrawerToggle(this, layoutwidgetdrawer, R.string.Open, R.string.Close);
        layoutwidgetdrawer.addDrawerListener(t);
        t.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        nv = (NavigationView) findViewById(R.id.nv);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.dashboard:
                        Intent intent = new Intent(getApplicationContext(), DashboardScreen.class);
                        intent.putExtra("message",getIntent().getStringExtra("message"));
                        startActivity(intent);
                        break;
               /*     case R.id.myprofile:
                        Intent view_jobs = new Intent(getApplicationContext(), MyProfileActivity.class);
                        startActivity(view_jobs);
                        break;*/

                    case R.id.logout:
                        Intent logout = new Intent(getApplicationContext(), MainScreen.class);
                        startActivity(logout);
                        finish();
                        break;

                    default:
                        return true;
                }
                layoutwidgetdrawer.closeDrawer(GravityCompat.START);
                return true;

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (layoutwidgetdrawer.isDrawerOpen(GravityCompat.START)) {
            layoutwidgetdrawer.closeDrawer(GravityCompat.START);
        } else {

            diolougebox();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (layoutwidgetdrawer.isDrawerOpen(GravityCompat.START)) {
            layoutwidgetdrawer.closeDrawer(GravityCompat.START);
        } else {
            layoutwidgetdrawer.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    private void diolougebox() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(DashboardScreen.this);
        builder1.setMessage("Do you want to close the Application.");  //message we want to show the end user
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel(); //cancle the alert dialog box
                        finish();//finish the process
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}