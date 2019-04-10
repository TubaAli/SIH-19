package com.example.sih19;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status> {

    protected LatLng mCenterLocation = new LatLng( 25.3176, 82.9739 );

    protected GoogleMap mGoogleMap;

    private HeatmapTileProvider mProvider;

    private ImageButton heatmap;
    private ImageButton testLabs;
    private ImageButton dispensary;
    private ImageButton more;

    private Button report;
    private Button predict;

    private static final String LOG_TAG = "MainActivity";

    private RecyclerView recyclerView = null;

    private List<CustomRecyclerViewItem> itemList = null;

    private CustomRecyclerViewDataAdapter customRecyclerViewDataAdapter = null;

    protected ArrayList<Geofence> mGeofenceList;
    protected GoogleApiClient mGoogleApiClient;

    String tag_json_arry = "json_array_req";
    String url="https://80fae82c.ngrok.io/db/region/";
    String url1="https://80fae82c.ngrok.io/db/hospital/";
    String url2 = "https://80fae82c.ngrok.io/db/lab/";
    String url4 = "https://sandbox-healthservice.priaid.ch/symptoms?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImFyY2hpdC5nb3lhbDI5QGdtYWlsLmNvbSIsInJvbGUiOiJVc2VyIiwiaHR0cDovL3NjaGVtYXMueG1sc29hcC5vcmcvd3MvMjAwNS8wNS9pZGVudGl0eS9jbGFpbXMvc2lkIjoiNDcyMCIsImh0dHA6Ly9zY2hlbWFzLm1pY3Jvc29mdC5jb20vd3MvMjAwOC8wNi9pZGVudGl0eS9jbGFpbXMvdmVyc2lvbiI6IjIwMCIsImh0dHA6Ly9leGFtcGxlLm9yZy9jbGFpbXMvbGltaXQiOiI5OTk5OTk5OTkiLCJodHRwOi8vZXhhbXBsZS5vcmcvY2xhaW1zL21lbWJlcnNoaXAiOiJQcmVtaXVtIiwiaHR0cDovL2V4YW1wbGUub3JnL2NsYWltcy9sYW5ndWFnZSI6ImVuLWdiIiwiaHR0cDovL3NjaGVtYXMubWljcm9zb2Z0LmNvbS93cy8yMDA4LzA2L2lkZW50aXR5L2NsYWltcy9leHBpcmF0aW9uIjoiMjA5OS0xMi0zMSIsImh0dHA6Ly9leGFtcGxlLm9yZy9jbGFpbXMvbWVtYmVyc2hpcHN0YXJ0IjoiMjAxOS0wMy0wMiIsImlzcyI6Imh0dHBzOi8vc2FuZGJveC1hdXRoc2VydmljZS5wcmlhaWQuY2giLCJhdWQiOiJodHRwczovL2hlYWx0aHNlcnZpY2UucHJpYWlkLmNoIiwiZXhwIjoxNTUxNTg3ODcwLCJuYmYiOjE1NTE1ODA2NzB9.Qnr7ofVWNJe7nKRmsyDSSEfxIN61w7fEoV0L8VOWTr8&format=json&language=en-gb";

    private ArrayList<LatLng> heatMapLocations;
    private ArrayList<LatLng> predHeatMapLocations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Empty list for storing geofences.
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                1337);
        mGeofenceList = new ArrayList<Geofence>();

        // Get the geofences used. Geofence data is hard coded in this sample.
        populateGeofenceList();

        // Kick off the request to build GoogleApiClient.
        buildGoogleApiClient();

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //initHeatMapIfNecessary();

        // get the bottom sheet view
        heatmap = (ImageButton) findViewById(R.id.heatmap);
        testLabs = (ImageButton) findViewById(R.id.Test_Labs);
        dispensary = (ImageButton) findViewById(R.id.dispensery);
        more = (ImageButton) findViewById(R.id.more);
        report = (Button)findViewById(R.id.report);
        predict = (Button)findViewById(R.id.predict);

        //initControls();

        initDengueCardsList();

        // Create the recyclerview.
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.custom_refresh_recycler_view);
        // Create the grid layout manager with 2 columns.
        GridLayoutManager layoutManager = new GridLayoutManager(this,1);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        //layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        // Set layout manager.
        recyclerView.setLayoutManager(layoutManager);

        // Create car recycler view data adapter with car item list.
        customRecyclerViewDataAdapter = new CustomRecyclerViewDataAdapter(itemList,this);
        // Set data adapter.
        recyclerView.setAdapter(customRecyclerViewDataAdapter);

        // Scroll RecyclerView a little to make later scroll take effect.
        //recyclerView.scrollToPosition(0);

        heatmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                heatMapLocations = new ArrayList<LatLng>();
//                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
//
//                // Initialize a new JsonArrayRequest instance
//                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
//                        Request.Method.GET,
//                        url,
//                        null,
//                        new Response.Listener<JSONArray>() {
//                            @Override
//                            public void onResponse(JSONArray response) {
//                                Log.d(LOG_TAG, response.toString());
//                                for(int i=0;i<response.length();i++)
//                                {
//                                    try {
//                                        heatMapLocations.add(new LatLng(response.getJSONObject(i).getDouble("longitude"),response.getJSONObject(i).getDouble("latitude")));
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                                initDynamicHeatMapIfNecessary();
//                            }
//                        },
//                        new Response.ErrorListener(){
//                            @Override
//                            public void onErrorResponse(VolleyError error){
//                                VolleyLog.d(LOG_TAG, "Error: " + error.getMessage() + error.networkResponse);
//                            }
//                        }
//                );
//
//                // Add JsonArrayRequest to the RequestQueue
//                requestQueue.add(jsonArrayRequest);

                initHeatMapIfNecessary();
            }
        });

        testLabs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initTestLabsMapIfNecessary();
            }
        });

        dispensary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDispensaryMapIfNecessary();
            }
        });

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPredHeatMapIfNecessary();
            }
        });

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ReportDiseaseActivity.class));
            }
        });

        predict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),PredictDiseaseActivity.class));
            }
        });

        //initHeatMapIfNecessary();
        getHeatMapMarkers();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_Dengue) {
            initDengueCardsList();

            // Create the recyclerview.
            RecyclerView recyclerView = (RecyclerView)findViewById(R.id.custom_refresh_recycler_view);
            // Create the grid layout manager with 2 columns.
            GridLayoutManager layoutManager = new GridLayoutManager(this,1);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            //layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

            // Set layout manager.
            recyclerView.setLayoutManager(layoutManager);

            // Create car recycler view data adapter with car item list.
            customRecyclerViewDataAdapter = new CustomRecyclerViewDataAdapter(itemList,this);
            // Set data adapter.
            recyclerView.setAdapter(customRecyclerViewDataAdapter);
            return true;
        }
        else if (id == R.id.action_malaria) {
            initMalariaCardsList();
            // Create the recyclerview.
            RecyclerView recyclerView = (RecyclerView)findViewById(R.id.custom_refresh_recycler_view);
            // Create the grid layout manager with 2 columns.
            GridLayoutManager layoutManager = new GridLayoutManager(this,1);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            //layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

            // Set layout manager.
            recyclerView.setLayoutManager(layoutManager);

            // Create car recycler view data adapter with car item list.
            customRecyclerViewDataAdapter = new CustomRecyclerViewDataAdapter(itemList,this);
            // Set data adapter.
            recyclerView.setAdapter(customRecyclerViewDataAdapter);
            return true;
        }
        else if (id == R.id.action_tuberculosis) {
            initTubercolosisCardsList();
            // Create the recyclerview.
            RecyclerView recyclerView = (RecyclerView)findViewById(R.id.custom_refresh_recycler_view);
            // Create the grid layout manager with 2 columns.
            GridLayoutManager layoutManager = new GridLayoutManager(this,1);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            //layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

            // Set layout manager.
            recyclerView.setLayoutManager(layoutManager);

            // Create car recycler view data adapter with car item list.
            customRecyclerViewDataAdapter = new CustomRecyclerViewDataAdapter(itemList,this);
            // Set data adapter.
            recyclerView.setAdapter(customRecyclerViewDataAdapter);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        } else if (id == R.id.nav_geofence) {
            addGeofencesButtonHandler();
        } else if (id == R.id.nav_call) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:+17372105294"));
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        //initHeatMapIfNecessary();
    }

    protected void initHeatMapIfNecessary() {
        if( mGoogleMap != null ) {
            mGoogleMap.clear();
        }

        mGoogleMap = ( (MapFragment) getFragmentManager().findFragmentById( R.id.map ) ).getMap();

        initHeatMapSettings();
        initCamera();
    }

    protected void initCamera() {
        CameraPosition position = CameraPosition.builder()
                .target( mCenterLocation )
                .zoom( getInitialMapZoomLevel() )
                .build();

        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), null);
    }

    protected int getMapLayoutId() {
        return R.layout.activity_base_map;
    }

    protected float getInitialMapZoomLevel() {
        return 12.0f;
    }

    protected void initHeatMapSettings()
    {
        //ArrayList<LatLng> locations = generateLocations();
        mProvider = new HeatmapTileProvider.Builder().data( heatMapLocations ).build();
        mProvider.setRadius( HeatmapTileProvider.DEFAULT_RADIUS );
        mGoogleMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
    }

    private ArrayList<LatLng> generateLocations() {
        ArrayList<LatLng> locations = new ArrayList<LatLng>();
        double lat;
        double lng;
        Random generator = new Random();
        for( int i = 0; i < 1000; i++ ) {
            lat = generator.nextDouble() / 3;
            lng = generator.nextDouble() / 3;
            if( generator.nextBoolean() ) {
                lat = -lat;
            }
            if( generator.nextBoolean() ) {
                lng = -lng;
            }
            locations.add(new LatLng(mCenterLocation.latitude + lat, mCenterLocation.longitude + lng));
        }

        Log.d(LOG_TAG,Arrays.toString(locations.toArray()));
        return locations;
    }

    protected void initTestLabsMapIfNecessary() {
        if( mGoogleMap != null ) {
            mGoogleMap.clear();
        }

        MapFragment mapFragment = ( (MapFragment) getFragmentManager().findFragmentById( R.id.map ) );
        mapFragment.getMapAsync(this);

//        initTestLabsMapSettings();
//        initCamera();
//        initMarkers();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        //mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

        initTestLabsMapSettings();
        initCamera();
        //initMarkers();

        CustomInfoWindowAdapter adapter = new CustomInfoWindowAdapter(MainActivity.this);
        mGoogleMap.setInfoWindowAdapter(adapter);

        LatLng varanasi = new LatLng( 25.3176, 82.9739 );
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        // Initialize a new JsonArrayRequest instance
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url2,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(LOG_TAG, response.toString());
                        for(int i=0;i<response.length();i++)
                        {
                            try {
                                LatLng loc = new LatLng(response.getJSONObject(i).getJSONObject("pincode").getDouble("latitude"),response.getJSONObject(i).getJSONObject("pincode").getDouble("longitude"));
                                mGoogleMap.addMarker(new MarkerOptions().position(loc).title(response.getJSONObject(i).getString("first_name")).snippet("Free : "+response.getJSONObject(i).getString("free"))).showInfoWindow();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        Log.d(LOG_TAG, Arrays.toString(heatMapLocations.toArray()));
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        VolleyLog.d(LOG_TAG, "Error: " + error.getMessage() + error.networkResponse);
                    }
                }
        );

        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);

        // Add a marker at the Oval and move the camera
//        double lat = 25.3176;
//        double lng = 82.9739;
//        LatLng varanasi = new LatLng( 25.3176, 82.9739 );
//        LatLng varanasi1 = new LatLng( 25.3276, 82.9739 );
//        LatLng varanasi2 = new LatLng( 25.3176, 82.9839 );
//        LatLng varanasi3 = new LatLng( 25.3276, 82.9679 );
//        mGoogleMap.addMarker(new MarkerOptions().position(varanasi).title("Cases here : 29").snippet("Free : Yes")).showInfoWindow();
//        mGoogleMap.addMarker(new MarkerOptions().position(varanasi1).title("Cases here : 50").snippet("Free : No")).showInfoWindow();
//        mGoogleMap.addMarker(new MarkerOptions().position(varanasi2).title("Cases here : 3").snippet("Free : No")).showInfoWindow();
//        mGoogleMap.addMarker(new MarkerOptions().position(varanasi3).title("Cases here : 102").snippet("Free : No")).showInfoWindow();
////        for( int i = 0; i < 10; i++ ) {
////            mGoogleMap.addMarker(new MarkerOptions().position(new LatLng( lat, lng )).title("Oval Pub"));
////            lat+=2.5;
////            lng+=2.5;
////        }
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(varanasi));

    /*
    // Add a marker at Diceys and move the camera
    LatLng diceys = new LatLng(53.3358088,-6.2636688);
    mMap.addMarker(new MarkerOptions().position(diceys).title("Diceys Nightclub"));
    mMap.moveCamera(CameraUpdateFactory.newLatLng(diceys));
    */

    mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
        @Override
        public void onInfoWindowClick(Marker marker) {
            Log.d("Archit","Hello");
            Toast.makeText(MainActivity.this, "Info window clicked",
                    Toast.LENGTH_SHORT).show();

            Geocoder geocoder;
            List<Address> addresses = null;
            geocoder = new Geocoder(MainActivity.this, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(marker.getPosition().latitude, marker.getPosition().longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            } catch (IOException e) {
                e.printStackTrace();
            }

            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();

            String geoUri = "http://maps.google.com/maps?q=loc:" + marker.getPosition().latitude + "," + marker.getPosition().longitude + " (" + address + ")";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
            startActivity(intent);
        }
    });

    }

    protected void initDispensaryMapIfNecessary() {
        if( mGoogleMap != null ) {
            mGoogleMap.clear();
        }

        mGoogleMap = ( (MapFragment) getFragmentManager().findFragmentById( R.id.map ) ).getMap();

        initTestLabsMapSettings();
        initCamera();
        CustomInfoWindowAdapter adapter = new CustomInfoWindowAdapter(MainActivity.this);
        mGoogleMap.setInfoWindowAdapter(adapter);

        LatLng varanasi = new LatLng( 25.3176, 82.9739 );
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        // Initialize a new JsonArrayRequest instance
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url1,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(LOG_TAG, response.toString());
                        for(int i=0;i<response.length();i++)
                        {
                            try {
                                LatLng loc = new LatLng(response.getJSONObject(i).getJSONObject("pincode").getDouble("latitude"),response.getJSONObject(i).getJSONObject("pincode").getDouble("longitude"));
                                mGoogleMap.addMarker(new MarkerOptions().position(loc).title(response.getJSONObject(i).getString("first_name")).snippet("No. of Free Beds : "+response.getJSONObject(i).getString("dengue_free"))).showInfoWindow();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        Log.d(LOG_TAG, Arrays.toString(heatMapLocations.toArray()));
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        VolleyLog.d(LOG_TAG, "Error: " + error.getMessage() + error.networkResponse);
                    }
                }
        );

        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);

        // Add a marker at the Oval and move the camera
//        double lat = 25.3176;
//        double lng = 82.9739;
//        LatLng varanasi = new LatLng( 25.3176, 82.9739 );
//        LatLng varanasi1 = new LatLng( 25.3276, 82.9739 );
//        LatLng varanasi2 = new LatLng( 25.3176, 82.9839 );
//        LatLng varanasi3 = new LatLng( 25.3276, 82.9679 );
//        mGoogleMap.addMarker(new MarkerOptions().position(varanasi).title("Cases here : 29").snippet("Free : Yes")).showInfoWindow();
//        mGoogleMap.addMarker(new MarkerOptions().position(varanasi1).title("Cases here : 50").snippet("Free : No")).showInfoWindow();
//        mGoogleMap.addMarker(new MarkerOptions().position(varanasi2).title("Cases here : 3").snippet("Free : No")).showInfoWindow();
//        mGoogleMap.addMarker(new MarkerOptions().position(varanasi3).title("Cases here : 102").snippet("Free : No")).showInfoWindow();
////        for( int i = 0; i < 10; i++ ) {
////            mGoogleMap.addMarker(new MarkerOptions().position(new LatLng( lat, lng )).title("Oval Pub"));
////            lat+=2.5;
////            lng+=2.5;
////        }
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(varanasi));

    /*
    // Add a marker at Diceys and move the camera
    LatLng diceys = new LatLng(53.3358088,-6.2636688);
    mMap.addMarker(new MarkerOptions().position(diceys).title("Diceys Nightclub"));
    mMap.moveCamera(CameraUpdateFactory.newLatLng(diceys));
    */

        mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Log.d("Archit","Hello");
                Toast.makeText(MainActivity.this, "Info window clicked",
                        Toast.LENGTH_SHORT).show();

                Geocoder geocoder;
                List<Address> addresses = null;
                geocoder = new Geocoder(MainActivity.this, Locale.getDefault());

                try {
                    addresses = geocoder.getFromLocation(marker.getPosition().latitude, marker.getPosition().longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();

                String geoUri = "http://maps.google.com/maps?q=loc:" + marker.getPosition().latitude + "," + marker.getPosition().longitude + " (" + address + ")";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                startActivity(intent);
            }
        });
    }

    protected void initMoreMapIfNecessary() {
        if( mGoogleMap != null ) {
            mGoogleMap.clear();
        }

        mGoogleMap = ( (MapFragment) getFragmentManager().findFragmentById( R.id.map ) ).getMap();

        initTestLabsMapSettings();
        initCamera();
        initMarkers();
    }

    protected void initTestLabsMapSettings() {
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
    }

    private void initMarkers() {
        //ClusterManager<ClusterMarkerLocation> clusterManager = new ClusterManager<ClusterMarkerLocation>( this, mGoogleMap );
        //mGoogleMap.setOnCameraChangeListener(clusterManager);

        double lat;
        double lng;
        Random generator = new Random();
        for( int i = 0; i < 1000; i++ ) {
            lat = generator.nextDouble() / 3;
            lng = generator.nextDouble() / 3;
            if( generator.nextBoolean() ) {
                lat = -lat;
            }
            if( generator.nextBoolean() ) {
                lng = -lng;
            }
            mGoogleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(lat, lng))
                    .title("Hello world").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            //clusterManager.addItem( new ClusterMarkerLocation( new LatLng( mCenterLocation.latitude + lat, mCenterLocation.longitude + lng ) ) );
        }
    }

    /*private void initControls()
    {
        if(recyclerView == null)
        {
            recyclerView = (RecyclerView)findViewById(R.id.custom_refresh_recycler_view);

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

                    int firstCompleteVisibleItemPosition = -1;
                    int lastCompleteVisibleItemPosition = -1;
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();

                    if(layoutManager instanceof GridLayoutManager)
                    {
                        GridLayoutManager gridLayoutManager = (GridLayoutManager)layoutManager;
                        firstCompleteVisibleItemPosition = gridLayoutManager.findFirstCompletelyVisibleItemPosition();
                        lastCompleteVisibleItemPosition = gridLayoutManager.findLastCompletelyVisibleItemPosition();
                    }else if(layoutManager instanceof  LinearLayoutManager)
                    {
                        LinearLayoutManager linearLayoutManager = (LinearLayoutManager)layoutManager;
                        firstCompleteVisibleItemPosition = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                        lastCompleteVisibleItemPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                    }

                    String message = "";

                    // Means scroll at beginning ( top to bottom or left to right).
                    if(firstCompleteVisibleItemPosition == 0)
                    {
                        // dy < 0 means scroll to bottom, dx < 0 means scroll to right at beginning.
                        if(dy < 0 || dx < 0)
                        {
                            // Means scroll to bottom.
                            if(dy < 0)
                            {
                                loadData(true);
                            }

                            // Means scroll to right.
                            if(dx < 0 )
                            {
                                loadData(true);
                            }
                        }
                    }
                    // Means scroll at ending ( bottom to top or right to left )
                    else if(lastCompleteVisibleItemPosition == (totalItemCount - 1))
                    {
                        // dy > 0 means scroll to up, dx > 0 means scroll to left at ending.
                        if(dy > 0 || dx > 0)
                        {
                            // Scroll to top
                            if(dy > 0)
                            {
                                loadData(false);
                            }

                            // Scroll to left
                            if(dx > 0 )
                            {
                                loadData(false);
                            }
                        }
                    }

                    if(message.length() > 0) {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        if(uiHandler == null)
        {
            uiHandler = new Handler()
            {
                @Override
                public void handleMessage(Message msg) {
                    // If the message want to refresh list view.
                    if(msg.what == MESSAGE_UPDATE_RECYCLER_VIEW)
                    {
                        // Refresh list view after add item data.
                        customRecyclerViewDataAdapter.notifyDataSetChanged();
                    }

                    Bundle bundle = msg.getData();
                    int newItemIndex = bundle.getInt(MESSAGE_KEY_NEW_ITEM_INDEX);
                    recyclerView.scrollToPosition(newItemIndex - 1);
                }
            };
        }

        initDengueCardsList();
    }


    private void loadData(final boolean insertDataAtBeginning)
    {
        Thread workerThread = new Thread()
        {
            @Override
            public void run() {
                try {

                    Thread.sleep(3000);

                    int currItemListSize = itemList.size();

                    int newItemIndex = 0;

                    // Only add one RecyclerView item.
                    for (int i = currItemListSize; i < currItemListSize + 1; i++){

                        CustomRecyclerViewItem newViewItem = new CustomRecyclerViewItem();
                        newViewItem.setText("Card " + (i + 1));

                        if (insertDataAtBeginning) {
                            itemList.add(i - currItemListSize, newViewItem);
                            newItemIndex = 0;
                        }else
                        {
                            itemList.add(newViewItem);
                            newItemIndex = itemList.size() - 1;
                        }

                        Message message = new Message();
                        message.what = MESSAGE_UPDATE_RECYCLER_VIEW;
                        Bundle bundle = new Bundle();
                        bundle.putInt(MESSAGE_KEY_NEW_ITEM_INDEX, newItemIndex);
                        message.setData(bundle);
                        uiHandler.sendMessage(message);
                    }
                }catch(InterruptedException ex)
                {
                    Log.e(LOG_TAG, ex.getMessage(), ex);
                }
            }
        };

        workerThread.start();
    }*/

    private void initDengueCardsList()
    {
        if(itemList != null)
        {
            itemList = null;
        }
        itemList = new ArrayList<CustomRecyclerViewItem>();
        CustomRecyclerViewItem item1 = new CustomRecyclerViewItem();
        CustomRecyclerViewItem item2 = new CustomRecyclerViewItem();
        CustomRecyclerViewItem item3 = new CustomRecyclerViewItem();
        CustomRecyclerViewItem item4 = new CustomRecyclerViewItem();
        CustomRecyclerViewItem item5 = new CustomRecyclerViewItem();
        item1.setText("One more dies of dengue in Varanasi");
        item1.setBackgroundId(R.drawable.dengue1);
        item1.setNewsLink("https://timesofindia.indiatimes.com/city/varanasi/one-more-dies-of-dengue-in-varanasi-toll-goes-up-to-seven/articleshow/66201496.cms");
        item2.setText("Dengue patients may not have fever: AIIMS");
        item2.setBackgroundId(R.drawable.dengue5);
        item2.setNewsLink("https://timesofindia.indiatimes.com/city/delhi/some-patients-of-dengue-may-not-have-fever-warn-doctors/articleshow/66323937.cms");
        item3.setText("Dengue virus grips Varanasi but officials disagree over figures");
        item3.setBackgroundId(R.drawable.dengue4);
        item3.setNewsLink("https://www.newslaundry.com/2018/10/17/dengue-virus-grips-varanasi-but-officials-disagree-over-figures");
        item4.setText("Quick dengue deaths due to three-day NS1 test wait: Experts");
        item4.setBackgroundId(R.drawable.dengue3);
        item4.setNewsLink("https://timesofindia.indiatimes.com/city/kolkata/quick-dengue-deaths-due-to-three-day-ns1-test-wait-experts/articleshow/66011780.cms");
        item5.setText("Dengue count reaches 671 in Bihar");
        item5.setBackgroundId(R.drawable.dengue2);
        item5.setNewsLink("https://timesofindia.indiatimes.com/city/patna/dengue-count-reaches-671-in-bihar/articleshow/66336082.cms");
        itemList.add(item1);
        itemList.add(item2);
        itemList.add(item3);
        itemList.add(item4);
        itemList.add(item5);
    }

    private void initMalariaCardsList()
    {
        if(itemList != null)
        {
            itemList = null;
        }
        itemList = new ArrayList<CustomRecyclerViewItem>();
        CustomRecyclerViewItem item1 = new CustomRecyclerViewItem();
        CustomRecyclerViewItem item2 = new CustomRecyclerViewItem();
        CustomRecyclerViewItem item3 = new CustomRecyclerViewItem();
        CustomRecyclerViewItem item4 = new CustomRecyclerViewItem();
        item1.setText("Malaria cases outpace dengue this season");
        item1.setBackgroundId(R.drawable.malaria2);
        item1.setNewsLink("https://timesofindia.indiatimes.com/city/delhi/malaria-cases-outpace-dengue-this-season/articleshow/65110372.cms");
        item2.setText("9 succumb to dengue, malaria in 21 days in division");
        item2.setBackgroundId(R.drawable.malaria3);
        item2.setNewsLink("https://timesofindia.indiatimes.com/city/nagpur/9-succumb-to-dengue-malaria-in-21-days-in-division/articleshow/65848182.cms");
        item3.setText("Fever Hospital records 15 malaria cases in 3 days");
        item3.setBackgroundId(R.drawable.malaria4);
        item3.setNewsLink("https://timesofindia.indiatimes.com/city/hyderabad/monsoon-mania-fever-hospital-records-15-malaria-cases-in-3-days/articleshow/65126783.cms");
        item4.setText("115 malaria cases from January to March");
        item4.setBackgroundId(R.drawable.malaria1);
        item4.setNewsLink("https://timesofindia.indiatimes.com/city/patna/115-malaria-cases-from-january-to-march/articleshow/63915511.cms");
        itemList.add(item1);
        itemList.add(item2);
        itemList.add(item3);
        itemList.add(item4);
    }

    private void initTubercolosisCardsList()
    {
        if(itemList != null)
        {
            itemList = null;
        }
        itemList = new ArrayList<CustomRecyclerViewItem>();
        CustomRecyclerViewItem item1 = new CustomRecyclerViewItem();
        CustomRecyclerViewItem item2 = new CustomRecyclerViewItem();
        CustomRecyclerViewItem item3 = new CustomRecyclerViewItem();
        CustomRecyclerViewItem item4 = new CustomRecyclerViewItem();
        item1.setText("New drugs to prevent tuberculosis in the offing");
        item1.setBackgroundId(R.drawable.tuberculosis1);
        item1.setNewsLink("https://timesofindia.indiatimes.com/home/science/new-drugs-to-prevent-tuberculosis-in-the-offing/articleshow/66135152.cms");
        item2.setText("Tuberculosis patients left to die found new life in town");
        item2.setBackgroundId(R.drawable.tuberculosis2);
        item2.setNewsLink("https://timesofindia.indiatimes.com/city/shimla/how-tuberculosis-patients-left-to-die-found-new-life-in-this-town/articleshow/67317000.cms");
        item3.setText("In one month, 2,000 screened at mobile tuberculosis units");
        item3.setBackgroundId(R.drawable.tuberculosis4);
        item3.setNewsLink("https://timesofindia.indiatimes.com/city/chennai/in-1-mth-2k-screened-at-mobile-tb-units/articleshow/67705504.cms");
        item4.setText("Maharashtra joins forces with private players to eliminate Tuberculosis");
        item4.setBackgroundId(R.drawable.tuberculosis3);
        item4.setNewsLink("https://timesofindia.indiatimes.com/city/pune/maha-joins-forces-with-pvt-players-to-eliminate-tb/articleshow/66778350.cms");
        itemList.add(item1);
        itemList.add(item2);
        itemList.add(item3);
        itemList.add(item4);
    }

    public void onResult(Status status) {
        if (status.isSuccess()) {
            Toast.makeText(
                    this,
                    "Geofences Added",
                    Toast.LENGTH_SHORT
            ).show();
        } else {
            // Get the status code for the error and log it using a user-friendly message.
            String errorMessage = GeofenceErrorMessages.getErrorString(this,
                    status.getStatusCode());
        }
    }

    private PendingIntent getGeofencePendingIntent() {
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling addgeoFences()
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void addGeofencesButtonHandler() {
        if (!mGoogleApiClient.isConnected()) {
            Toast.makeText(this, getString(R.string.not_connected), Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            LocationServices.GeofencingApi.addGeofences(
                    mGoogleApiClient,
                    // The GeofenceRequest object.
                    getGeofencingRequest(),
                    // A pending intent that that is reused when calling removeGeofences(). This
                    // pending intent is used to generate an intent when a matched geofence
                    // transition is observed.
                    getGeofencePendingIntent()
            ).setResultCallback(this); // Result processed in onResult().
        } catch (SecurityException securityException) {
            // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
        }
    }

    public void populateGeofenceList() {
        for (Map.Entry<String, LatLng> entry : Constants.BAY_AREA_LANDMARKS.entrySet()) {

            mGeofenceList.add(new Geofence.Builder()
                    // Set the request ID of the geofence. This is a string to identify this
                    // geofence.
                    .setRequestId(entry.getKey())

                    // Set the circular region of this geofence.
                    .setCircularRegion(
                            entry.getValue().latitude,
                            entry.getValue().longitude,
                            Constants.GEOFENCE_RADIUS_IN_METERS
                    )

                    // Set the expiration duration of the geofence. This geofence gets automatically
                    // removed after this period of time.
                    .setExpirationDuration(Constants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)

                    // Set the transition types of interest. Alerts are only generated for these
                    // transition. We track entry and exit transitions in this sample.
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                            Geofence.GEOFENCE_TRANSITION_EXIT)

                    // Create the geofence.
                    .build());
        }
    }

    /**
     * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mGoogleApiClient.isConnecting() || !mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnecting() || mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Do something with result.getErrorCode());
    }

    @Override
    public void onConnectionSuspended(int cause) {
        mGoogleApiClient.connect();
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }

    private void test_sendNotification(String notificationDetails) {
        // Create an explicit content Intent that starts the main Activity.
        Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);

        // Construct a task stack.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        // Add the main Activity to the task stack as the parent.
        stackBuilder.addParentStack(MainActivity.class);

        // Push the content Intent onto the stack.
        stackBuilder.addNextIntent(notificationIntent);

        // Get a PendingIntent containing the entire back stack.
        PendingIntent notificationPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Get a notification builder that's compatible with platform versions >= 4
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        // Define the notification settings.
        builder.setSmallIcon(R.mipmap.ic_launcher)
                // In a real app, you may want to use a library like Volley
                // to decode the Bitmap.
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        R.mipmap.ic_launcher))
                .setColor(Color.RED)
                .setContentTitle(notificationDetails)
                .setContentText(getString(R.string.geofence_transition_notification_text))
                .setContentIntent(notificationPendingIntent);

        // Dismiss notification once the user touches it.
        builder.setAutoCancel(true);

        // Get an instance of the Notification manager
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Issue the notification
        mNotificationManager.notify(0, builder.build());
    }

    private void getHeatMapMarkers()
    {
        heatMapLocations = new ArrayList<LatLng>();
        predHeatMapLocations = new ArrayList<LatLng>();
//        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET,url,null,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        Log.d(LOG_TAG, response.toString());
//                        for(int i=0;i<response.length();i++)
//                        {
//                            try {
//                                heatMapLocations.add(new LatLng(response.getJSONObject(i).getDouble("longitude"),response.getJSONObject(i).getDouble("latitude")));
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                        initHeatMapIfNecessary();
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d(LOG_TAG, "Error: " + error.getMessage() + error.networkResponse);
//            }
//        }) {
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("Content-Type", "application/json");
//                return headers;
//            }
//        };
//
//// Adding request to request queue
//        AppController.getInstance().addToRequestQueue(req, tag_json_arry);

        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        // Initialize a new JsonArrayRequest instance
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(LOG_TAG, response.toString());
                        for(int i=0;i<response.length();i++)
                        {
                            try {
                                heatMapLocations.add(new LatLng(response.getJSONObject(i).getDouble("latitude"),response.getJSONObject(i).getDouble("longitude")));
                                predHeatMapLocations.add(new LatLng(response.getJSONObject(i).getDouble("latitude")+0.05,response.getJSONObject(i).getDouble("longitude")+0.03));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        Log.d(LOG_TAG, Arrays.toString(heatMapLocations.toArray()));
                        initHeatMapIfNecessary();
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        VolleyLog.d(LOG_TAG, "Error: " + error.getMessage() + error.networkResponse);
                    }
                }
        );

        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
    }

    private void initPredHeatMapIfNecessary()
    {
        if( mGoogleMap != null ) {
            mGoogleMap.clear();
        }

        mGoogleMap = ( (MapFragment) getFragmentManager().findFragmentById( R.id.map ) ).getMap();

        initPredHeatMapSettings();
        initCamera();
    }

    protected void initPredHeatMapSettings()
    {
        mProvider = new HeatmapTileProvider.Builder().data( predHeatMapLocations ).build();
        mProvider.setRadius( HeatmapTileProvider.DEFAULT_RADIUS );
        mGoogleMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
    }
}
