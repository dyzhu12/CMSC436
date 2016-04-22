package tech.randm.googlemapsapi;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private static final int REQUEST_FINE_LOCATION = 0;

    // Predefined locations
    private static final LatLng LOONEYS = new LatLng(38.9909013,-76.9342696);
    private static final LatLng BOARDANBREW = new LatLng(38.9915366,-76.9337273);
    private static final LatLng KIM = new LatLng(38.9910548,-76.938171);
    private static final LatLng CSIC_LOC = new LatLng(38.9899745,-76.9370508);
    //private static final LatLng STAMP = new LatLng(38.9880831,-76.9447758);

    // new camera position
    private static final CameraPosition CSIC =
            new CameraPosition.Builder().target(CSIC_LOC)
                                        .zoom(15.5f)
                                        .bearing(0.0f)
                                        .tilt(0.0f)
                                        .build();


    // using ArrayList to
    ArrayList <MarkerOptions> mMarkerOptions = new ArrayList <MarkerOptions> ();
    private int index = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_FINE_LOCATION);
        }

        // Disables map toolbar from appearing when marker is clicked
        UiSettings mUiSettings = mMap.getUiSettings();
        mUiSettings.setMapToolbarEnabled(false);

        setUpFABListener(mMap);

        // creating marker options
        createMarkerOptions();
        // move camera to CSIC building
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(CSIC));
    }


    /**
     * Every time user is asked for permissions, this function gets called
     * Every permission gets assigned integer
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {mMap.setMyLocationEnabled(true);}
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void setUpFABListener(GoogleMap map) {
        FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.addMarker);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                switch (index) {
                    case 0:
                        // adding marker to the map
                        mMap.addMarker(mMarkerOptions.get(index));
                        break;
                    case 1:
                        mMap.addMarker(mMarkerOptions.get(index));
                        break;
                    case 2:
                        mMap.addMarker(mMarkerOptions.get(index));
                        break;
                    case 3:
                        // using intent to launch navigation at Google Maps
                        Uri gmmIntentUri = Uri.parse("google.navigation:q=38.9915366,-76.9337273");
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);
                    default:
                        break;
                }
                index++;
            }
        });
    }

    private void createMarkerOptions() {
        mMarkerOptions.add(new MarkerOptions().position(LOONEYS).title("Looney's").snippet("Not bad for College Park"));
        mMarkerOptions.add(new MarkerOptions().position(BOARDANBREW).title("The Board and Brew").snippet("Great coffee. OK food. Too many nerds.").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        mMarkerOptions.add(new MarkerOptions().position(KIM).title("Jeong H. Kim Engineering Bldg").snippet("Too many bionerds. Not enough classrooms. Lots of air!").icon(BitmapDescriptorFactory.fromResource(R.drawable.rocket)));
        //mMarkerOptions.add(new MarkerOptions().position(STAMP).title("Adele H. Stamp Student Union").snippet("Not bad for College Park"));
    }

}
