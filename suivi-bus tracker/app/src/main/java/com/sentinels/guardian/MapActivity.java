package com.sentinels.guardian;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class MapActivity extends AppCompatActivity implements
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleMap.OnMapClickListener
    {
//    CONSTANTS
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;


//    GLOBAL OBJECTS
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private boolean permissionDenied = false;
    private LocationListener listener;
    private GoogleMap map;
    private Location location;
    private FusedLocationProviderClient fusedLocationClient;
    public static double latii,longii;
    public static double someLati,someLongi;
    private LocationManager locationManager;
    private ActionBarDrawerToggle drawerToggle;

    private double parsedLat =  LocationActivity.latitude != null ? LocationActivity.latitude.get(LocationActivity.latitude.size()-1):0;
    private double parseLong = LocationActivity.longitude != null ? LocationActivity.longitude.get(LocationActivity.longitude.size()-1):0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
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

        @SuppressLint("MissingPermission")
        private void enableMyLocation() {
            // 1. Check if permissions are granted, if so, enable the my location layer
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                this.map.setMyLocationEnabled(true);
                return;
            }

            // 2. Otherwise, request location permissions from the user.
            PermissionUtils.requestLocationPermissions(this, LOCATION_PERMISSION_REQUEST_CODE, true);
        }

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
                menuItem -> selectDrawerItem(menuItem));
    }

    public boolean selectDrawerItem(MenuItem menuItem) {
        switch(menuItem.getItemId()) {
            case R.id.nav_first_fragment:
                Intent intent = new Intent(getApplicationContext(),MainHomeActivity.class);
                startActivity(intent);
                return true;
            case R.id.nav_second_fragment:
                Toast.makeText(getApplicationContext(),"Already in Track!",Toast.LENGTH_LONG).show();
                return true;
            case R.id.nav_third_fragment:
                Intent intent1 = new Intent(getApplicationContext(),LocationActivity.class);
                startActivity(intent1);
                return true;
            case R.id.nav_fourth_fragment:
                Intent intent2 = new Intent(getApplicationContext(),AboutActivity.class);
                startActivity(intent2);
                return true;
            default:

        }

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

        @Override
        public void onMapClick(@NonNull LatLng latLng) {
            someLati = latLng.latitude;
            someLongi = latLng.longitude;
            startService(new Intent(MapActivity.this,MyService.class));
            map.addMarker(new MarkerOptions().position(latLng));
        }

        @SuppressLint("MissingPermission")
        @Override
        public boolean onMyLocationButtonClick() {
            Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
            fusedLocationClient.getLastLocation().addOnSuccessListener(this,location->{
                if(location!=null){
                    latii = location.getLatitude();
                    longii = location.getLongitude();
                }
            });
            Log.d("Latitude", String.valueOf(latii));
            Log.d("longitude", String.valueOf(longii));
            return false;
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                               @NonNull int[] grantResults) {
            if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                return;
            }

            if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                    Manifest.permission.ACCESS_FINE_LOCATION) || PermissionUtils
                    .isPermissionGranted(permissions, grantResults,
                            Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // Enable the my location layer if the permission has been granted.
                enableMyLocation();

            } else {
                // Permission was denied. Display an error message
                // Display the missing permission error dialog when the fragments resume.
                permissionDenied = true;
            }
        }

        @Override
        protected void onResumeFragments() {
            super.onResumeFragments();
            if (permissionDenied) {
                // Permission was not granted, display error dialog.
                showMissingPermissionError();
                permissionDenied = false;
            }
        }

        /**
         * Displays a dialog with error message explaining that the location permission is missing.
         */
        private void showMissingPermissionError() {
            PermissionUtils.PermissionDeniedDialog
                    .newInstance(true).show(getSupportFragmentManager(), "dialog");
        }

        @Override
        public void onMyLocationClick(@NonNull Location location) {
            Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            this.map = googleMap;
            this.map.setOnMyLocationButtonClickListener(this);
            this.map.setOnMyLocationClickListener(this);
            this.map.setOnMapClickListener(this);
            this.map.addMarker(new MarkerOptions().position(new LatLng(parsedLat,parseLong)).title(String.valueOf(parsedLat)+String.valueOf(parseLong)));
            enableMyLocation();
        }
    }
