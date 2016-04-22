package tech.randm.googlemapsapi;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
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


    private static String directionsApiURL = "https://maps.googleapis.com/maps/api/directions/xml?";

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
                        getDirections();
                        break;
                    default:
                        break;
                }
                index++;
            }
        });
    }


    // When user clicks button, calls AsyncTask.
    // Before attempting to fetch the URL, makes sure that there is a network connection.
    public void getDirections() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {

            // API Key only added for quick and dirty testing -- will remove
            String apiCall = directionsApiURL + "origin=38.9909013,-76.9342696&destination=38.9910548,-76.938171&key=" + "AIzaSyArttUogDRqOAMXHCoUqEIt_gN2yxjeFUI";
            new DownloadWebpageTask().execute(apiCall);
        }
    }

    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            System.out.println(result);
        }
    }

    private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 500;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d("DEBUG", "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readInputStream(is, len);
            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public String readInputStream(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

    private void createMarkerOptions() {
        mMarkerOptions.add(new MarkerOptions().position(LOONEYS).title("Looney's").snippet("Not bad for College Park"));
        mMarkerOptions.add(new MarkerOptions().position(BOARDANBREW).title("The Board and Brew").snippet("Great coffee. OK food. Too many nerds.").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        mMarkerOptions.add(new MarkerOptions().position(KIM).title("Jeong H. Kim Engineering Bldg").snippet("Too many bionerds. Not enough classrooms. Lots of air!").icon(BitmapDescriptorFactory.fromResource(R.drawable.rocket)));
        //mMarkerOptions.add(new MarkerOptions().position(STAMP).title("Adele H. Stamp Student Union").snippet("Not bad for College Park"));
    }

}
