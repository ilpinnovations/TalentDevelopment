package com.tcs.maverick.talentdevelopment.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.tcs.maverick.talentdevelopment.fragments.AboutAppFragment;
import com.tcs.maverick.talentdevelopment.fragments.MenuFragment;
import com.tcs.maverick.talentdevelopment.R;
import com.tcs.maverick.talentdevelopment.fragments.UserDetailsFragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import de.hdodenhof.circleimageview.CircleImageView;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private boolean flag = true;
    private Toolbar toolbar;
    private String emp_name, emp_email;
    private String fname;
    private int position = 0;
    private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.background_light));
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);

        SharedPreferences sharedPreferences = getSharedPreferences("Talent Development", MODE_PRIVATE);
        emp_name = sharedPreferences.getString("emp_name", "");
        emp_email = sharedPreferences.getString("emp_email", "");
        fname = sharedPreferences.getString("emp_image", "");


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_menu);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;


        View header = navigationView.getHeaderView(0);
        TextView textView = (TextView) header.findViewById(R.id.emp_name);
        textView.setText(emp_name);
        TextView textView1 = (TextView) header.findViewById(R.id.emp_email);
        textView1.setText(emp_email);
        CircleImageView imageView = (CircleImageView) header.findViewById(R.id.emp_img);
        loadImageFromStorage(fname, imageView);


        MenuFragment menuFragment = new MenuFragment();
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, menuFragment);
        fragmentTransaction.commit();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            if (flag) {
                new AlertDialog.Builder(HomeActivity.this)
                        .setTitle("Talent Development")
                        .setMessage("Are you sure you want to close this application?")
                                //.setIcon(getResources().getDrawable(android.R.drawable.ic_dialog_alert))
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                manageNavigation();
                                dialog.dismiss();
                            }
                        }).show();
            }
            else {
                flag=true;
                navigationView.setCheckedItem(R.id.nav_menu);
                toolbar.setTitle("Home");
                MenuFragment menuFragment = new MenuFragment();
                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, menuFragment);
                fragmentTransaction.commit();
            }
        }

    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_menu) {
            flag=true;
            position = 0;
            toolbar.setTitle("Home");
            MenuFragment menuFragment = new MenuFragment();
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, menuFragment);
            fragmentTransaction.commit();


        } else if (id == R.id.nav_user) {
            flag=false;
            position = 1;
            toolbar.setTitle("User Details");
            UserDetailsFragment userDetailsFragment = new UserDetailsFragment();
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, userDetailsFragment).commit();

        } else if (id == R.id.nav_information) {
            flag=false;
            position = 2;
            toolbar.setTitle("Information");
            AboutAppFragment aboutAppFragment = new AboutAppFragment();
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, aboutAppFragment).commit();

        } else if (id == R.id.nav_logout) {

            new AlertDialog.Builder(HomeActivity.this)
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to logout?")
                    .setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            manageNavigation();
                        }
                    })
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SharedPreferences sharedPreferences = HomeActivity.this.getSharedPreferences("Talent Development", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();

                                    editor.putString("emp_id", "");
                                    editor.putString("emp_name", "");
                                    editor.putString("emp_email", "");
                                    editor.putString("emp_image", "");
                                    editor.commit();

                                    Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();

                                }
                            })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            manageNavigation();
                            dialog.dismiss();

                        }
                    }).show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void manageNavigation() {
        switch (position) {
            case 0:
                navigationView.setCheckedItem(R.id.nav_menu);
                break;
            case 1:
                navigationView.setCheckedItem(R.id.nav_user);
                break;

            case 2:
                navigationView.setCheckedItem(R.id.nav_information);
                break;


        }
    }

    private void loadImageFromStorage(String path, CircleImageView imageView) {

        try {
            File f = new File(path, "profile.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            imageView.setImageBitmap(b);
        } catch (FileNotFoundException e) {
            imageView.setImageResource(R.drawable.user_image);
        }

    }
}
