package com.sentinels.guardian;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class LocationActivity extends AppCompatActivity {

    private DrawerLayout mDrawer;
    private Toolbar toolbar;

    private ActionBarDrawerToggle drawerToggle;

    // creating a variable for
    // our Firebase Database.
    FirebaseDatabase firebaseDatabase;
    public static ArrayList<Double> latitude ;
    public static ArrayList<Double> longitude;

    // creating a variable for our
    // Database Reference for Firebase.
    DatabaseReference databaseReference;
    private TextView Latitude,Longitude;

    private ArrayList<Double> getCoord(Map<String,String> vals){
        ArrayList<Double> temp  =  new ArrayList<>();
        for(Map.Entry<String, String> entry:vals.entrySet()){
            String s = entry.getValue();
            temp.add(Double.valueOf(s));
        }
        return temp;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        Latitude = findViewById(R.id.latitude);
        Longitude = findViewById(R.id.longitude);

        // below line is used to get the instance
        // of our Firebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();

        // below line is used to get
        // reference for our database.
        DatabaseReference  loc = firebaseDatabase.getInstance().getReference("location");
        DatabaseReference lat = loc.child("latitude");
        DatabaseReference lon = loc.child("longitude");

        lat.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                latitude = getCoord((Map<String,String>)dataSnapshot.getValue());
                Log.d("Latitude",latitude.toString());

                String lati =  String.valueOf(latitude.get(latitude.size()-1));
                Latitude.setText(lati);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // calling on cancelled method when we receive
                // any error or we are not able to get the data.
                Toast.makeText(LocationActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });

        lon.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                longitude = getCoord((Map<String,String>)dataSnapshot.getValue());
                Log.d("Longitude",longitude.toString());
                String longi =  String.valueOf(longitude.get(longitude.size()-1));

                Longitude.setText(longi);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // calling on cancelled method when we receive
                // any error or we are not able to get the data.
                Toast.makeText(LocationActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
        //databaseReference = firebaseDatabase.getReference("location");
        // calling method
        // for getting data.
        //getdata();

        Toolbar mtoolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mtoolbar);
        // This will display an Up icon (<-), we will replace it with hamburger later
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();
        // Setup toggle to display hamburger icon with nice animation
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerToggle.syncState();

        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.addDrawerListener(drawerToggle);
        NavigationView nvDrawer = (NavigationView) findViewById(R.id.nvView);
        // Setup drawer view
        setupDrawerContent(nvDrawer);
    }

//    private void getdata() {
//
//        // calling add value event listener method
//        // for getting the values from database.
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                // this method is call to get the realtime
//                // updates in the data.
//                // this method is called when the data is
//                // changed in our Firebase console.
//                // below line is for getting the data from
//                // snapshot of our database.
////                String lat = snapshot.getValue(String.class);
////                String lon = snapshot.getValue(String.class);
//                for (DataSnapshot ds : snapshot.getChildren()){
//                    String lat = ds.child("latitude").child("-N4236x-IG6_gpJQ7_Lc").getValue(String.class);
//                    String lon = ds.child("longitude").child("-N4238UEllM4amf6N_mP").getValue(String.class);
//                    Latitude.setText(lat);
//                    Longitude.setText(lon);
//                }
//                // after getting the value we are setting
//                // our value to our text view in below line.
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // calling on cancelled method when we receive
//                // any error or we are not able to get the data.
//                Toast.makeText(LocationActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    return selectDrawerItem(menuItem);
                });
    }

    public boolean selectDrawerItem(MenuItem menuItem) {
        switch(menuItem.getItemId()) {
            case R.id.nav_first_fragment:
                Intent intent = new Intent(getApplicationContext(),MainHomeActivity.class);
                startActivity(intent);
                return true;
            case R.id.nav_second_fragment:
                Intent intent1 = new Intent(getApplicationContext(),MapActivity.class);
                startActivity(intent1);
                return true;
            case R.id.nav_third_fragment:
                Toast.makeText(getApplicationContext(),"Already in Location!",Toast.LENGTH_LONG).show();
                return true;
            case R.id.nav_fourth_fragment:
                Intent intent2 = new Intent(getApplicationContext(),AboutActivity.class);
                startActivity(intent2);
                return true;
            default:
        }

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();
        return true;
    }

    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        else{
            switch (item.getItemId()) {
                case R.id.help:
                    Toast.makeText(getApplicationContext(),"HELP CLICKED!!",Toast.LENGTH_LONG).show();
                    return true;
                case R.id.log:
                    Intent intent2 = new Intent(getApplicationContext(),SignInActivity.class);
                    startActivity(intent2);
                    Toast.makeText(getApplicationContext(),"Logged out successfully",Toast.LENGTH_LONG).show();
                    return true;
                case android.R.id.home:
                    mDrawer.openDrawer(GravityCompat.START);
                    return true;
                default:
                    return super.onContextItemSelected(item);
            }
        }
    }

}
