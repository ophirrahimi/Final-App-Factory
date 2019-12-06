package com.appfactory.kaldi;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;


public class DrinkerMainActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener, LocationListener {
    // Global Variables
    public boolean mLocationPermissionGranted = false;
    public static final int ERROR_DIALOG_REQUEST = 9001;
    public static final int PERMISSIONS_REQUEST_ENABLE_GPS = 9002;
    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 9003;
    private Location myLocation;
    private com.google.maps.model.LatLng destination = null;
    private com.google.maps.model.LatLng currLoc = null;
    private GoogleMap mMap;
    private Marker destMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Button manageProfile =  (Button) findViewById(R.id.manageProfile);
        manageProfile.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                Intent myIntent = new Intent(view.getContext(), ManageProfileActivity.class);
                String currentUser = getIntent().getStringExtra("currentUser");
                boolean isDrinker = getIntent().getBooleanExtra("isDrinker", true);
                myIntent.putExtra("currentUser", currentUser);
                myIntent.putExtra("isDrinker", isDrinker);
                startActivityForResult(myIntent, 0);
            }
        });
        Button historyButton =  (Button) findViewById(R.id.history);
        historyButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                Intent myIntent = new Intent(view.getContext(), OrderHistoryActivity.class);
                String currentUser = getIntent().getStringExtra("currentUser");
                boolean isDrinker = getIntent().getBooleanExtra("isDrinker", true);
                myIntent.putExtra("currentUser", currentUser);
                myIntent.putExtra("isDrinker", isDrinker);
                startActivityForResult(myIntent, 0);
            }

        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /* Calculates the directions for the marker we click on */
    private void calculateDirections(Marker marker)
    {
        if (mLocationPermissionGranted)
        {
            getCurrentLoc();
            destination = new com.google.maps.model.LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
            currLoc = new com.google.maps.model.LatLng(myLocation.getLatitude(), myLocation.getLongitude());
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/dir/" + currLoc.toString() + "/" + destination.toString()));
            startActivity(browserIntent);
        }
        else
        {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps"));
            startActivity(browserIntent);
        }

    }


    // Converts a street address to a geolocation address
    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;
        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address.size() == 0) {
                return null;
            }
            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return p1;
    }

    // Gets the current location of the user
    private void getCurrentLoc()
    {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            getLocationPermission();
        }
        else {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            myLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Log.d("Get myLocation", "" + myLocation);
            if (myLocation != null) {
                // Debug Statement
                double latitude = myLocation.getLatitude();
                double longitude = myLocation.getLongitude();
                // End of debug
                LatLng latLng = new LatLng(latitude, longitude);
                CameraUpdate loc = CameraUpdateFactory.newLatLngZoom(latLng, 15.5f);
                mMap.animateCamera(loc);
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, this);
            }
        }


    }

    // Adds marker on map using LatLng object
    void addMarker(String businessName, LatLng latlng, int tag)
    {
        Marker marker = mMap.addMarker(new MarkerOptions().position(latlng).title(businessName).snippet("Come have some delicious coffee!"));
        marker.setTag(tag);
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);

        // Add all current location features if the user grants access
        if(checkMapServices())
        {
            if(mLocationPermissionGranted)
            {
                getCurrentLoc();
            }
            else
            {
                getLocationPermission();
            }
        }
        // ---- Repeat process for all added business ---
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("users").child("merchants");
        database.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren())
                {
                    Merchant merchant = postSnapshot.getValue(Merchant.class);
                    if (merchant.stores != null)
                    {
                        for (int i = 0; i < merchant.stores.size(); i++) {
                            String strAddress = merchant.stores.get(i).getLocation();
                            String businessName = merchant.stores.get(i).getStoreName();
                            LatLng latLng = getLocationFromAddress(getApplicationContext(), strAddress);
                            if(latLng != null) {
                                addMarker(businessName, latLng, 0);
                            }
                        }
                    }
                    else
                    {
                        Log.d("Debug", "No merchants were found.");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMarkerClickListener(this);
    }


    @Override
    public boolean onMarkerClick(Marker marker)
    {
        LatLng latLng = marker.getPosition();
        return false;
    }

    @Override
    public void onInfoWindowClick(final Marker marker)
    {
        if(marker.getSnippet().equals("This is you"))
        {
            marker.hideInfoWindow();
        }
        else{

            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Please make your selection").setCancelable(true)
                    .setPositiveButton("Directions", new DialogInterface.OnClickListener()
            {
                public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id)
                {
                    destMarker = marker;
                    calculateDirections(marker);
                    dialog.dismiss();
                }
            })
            .setNegativeButton("Menu", new DialogInterface.OnClickListener()
            {
                public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id)
                {
                    Intent myIntent = new Intent(DrinkerMainActivity.this, MenuActivity.class);
                    String currentUser = getIntent().getStringExtra("currentUser");
                    boolean isDrinker = getIntent().getBooleanExtra("isDrinker", true);
                    myIntent.putExtra("currentUser", currentUser);
                    myIntent.putExtra("isDrinker", isDrinker);
                    myIntent.putExtra("businessTitle", marker.getTitle());
                    startActivityForResult(myIntent, 0);
                    dialog.cancel();
                }
            });
            final AlertDialog alert = builder.create();
            alert.show();
        }
    }

    float distanceToDest()
    {
        Location dest = new Location("");
        dest.setLatitude(destination.lat);
        dest.setLongitude(destination.lng);
        return myLocation.distanceTo(dest);
    }

    @Override
    public void onLocationChanged(Location location)
    {
        myLocation = location;
        Log.d("Update myLocation", "" + myLocation);
        if(((destination != null) && (myLocation != null)) && (mLocationPermissionGranted))
        {
            // Prevents the application from switching back from the mapping page to the menu page
            // this could happen if the user requests directions when he/she is at the store
            try
            {
                Thread.sleep(10000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            float distance = distanceToDest();
            Log.d("dist", "" + distance);
            if (distance < 50)
            {
                Intent myIntent = new Intent(DrinkerMainActivity.this, MenuActivity.class);
                String currentUser = getIntent().getStringExtra("currentUser");
                boolean isDrinker = getIntent().getBooleanExtra("isDrinker", true);
                myIntent.putExtra("currentUser", currentUser);
                myIntent.putExtra("isDrinker", isDrinker);
                myIntent.putExtra("businessTitle", destMarker.getTitle());
                startActivityForResult(myIntent, 0);
            }
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {

    }

    @Override
    public void onProviderEnabled(String provider)
    {

    }

    @Override
    public void onProviderDisabled(String provider)
    {

    }
    // Checks if all map services are enabled
    private boolean checkMapServices()
    {
        if(isServicesOK())
        {
            if(isMapsEnabled())
            {
                return true;
            }
        }
        return false;
    }

    // Prompts user to enable GPS
    private void buildAlertMessageNoGps()
    {
        final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setMessage("This application requires GPS to work properly, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        Intent enableGpsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(enableGpsIntent, PERMISSIONS_REQUEST_ENABLE_GPS);
                    }
                });
        final androidx.appcompat.app.AlertDialog alert = builder.create();
        alert.show();
    }

    // Checks if the maps functionality is enabled on the device
    public boolean isMapsEnabled()
    {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled(LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
            return false;
        }
        return true;
    }

    /*
     * Request location permission, so that we can get the location of the
     * device. The result of the permission request is handled by a callback,
     * onRequestPermissionsResult.
     */
    private void getLocationPermission()
    {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)
        {
            mLocationPermissionGranted = true;
            getCurrentLoc();
        }
        else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    // Checks if the device can use google services
    public boolean isServicesOK()
    {
        Log.d("TAG", "isServicesOK: checking google services version");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(DrinkerMainActivity.this);
        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            Log.d("TAG", "isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
            Log.d("TAG", "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(DrinkerMainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults)
    {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION:  {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    mLocationPermissionGranted = true;
                    getCurrentLoc();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("TAG", "onActivityResult: called.");
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ENABLE_GPS: {
                if(mLocationPermissionGranted)
                {
                    getCurrentLoc();
                }
                else{
                    getLocationPermission();
                }
            }
        }

    }
}
