package com.example.groupandgoeventplanner;

//import androidx.fragment.app.FragmentActivity;
import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;


public class MapViewerActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private static final String ARG_PARAM1 = "param1";
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private float zoom = 15;
    //GeoPoint geo;


    public static MapViewerActivity newInstance() {
        MapViewerActivity fragment = new MapViewerActivity();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, username);
        //fragment.setArguments(args);
        return fragment;
    }



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
        FirebaseFirestore ref = FirebaseFirestore.getInstance();


        CollectionReference docRef = ref.collection("Events");
        docRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    /*QuerySnapshot document = task.getResult();
                    if (document.exists()) {
                        //Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        GeoPoint geo = document.getGeoPoint("latLong");
                        String name = document.getString("name");
                        double lat = geo.getLatitude();
                        double lng = geo.getLongitude();
                        LatLng latLng = new LatLng(lat,lng);
                        mMap.addMarker(new MarkerOptions().position(latLng).title(name));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));
                    } else {
                        //Log.d(TAG, "No such document");
                    }*/

                    for(QueryDocumentSnapshot document : task.getResult()){
                        Map<String, Object> docMap = document.getData();
                        GeoPoint point = (GeoPoint) docMap.get("latLong");
                        String name = (String) docMap.get("name");
                        //SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyy HH:mm:ss");
                        Timestamp date = (Timestamp) docMap.get("date");
                        //String s = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(date.get);
                        Date dateD = new Date(date.getSeconds()*1000L);
                        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy ");
                        sdf.setTimeZone(TimeZone.getTimeZone("GMT-8"));
                        String s = sdf.format(dateD);
                        double lat = point.getLatitude();
                        double lng = point.getLongitude();
                        LatLng latLng = new LatLng(lat,lng);
                        mMap.addMarker(new MarkerOptions().position(latLng).title(name).snippet(s));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));
                    }


                }
                else{
                    //Log.d(TAG,"get failed with ", task.getException());
                }
            }
        });



        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        enableMyLocation();
    }

    private void enableMyLocation(){
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
            mMap.setMyLocationEnabled(true);
        }
        else{
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }
    }
}
