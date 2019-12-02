package com.example.groupandgoeventplanner;

//import androidx.fragment.app.FragmentActivity;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;


public class MapViewerActivityPublic extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private static final String ARG_PARAM1 = "param1";
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private float zoom = 15;
    Intent intent;
    //GeoPoint geo;


    public static MapViewerActivityPublic newInstance() {
        MapViewerActivityPublic fragment = new MapViewerActivityPublic();
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
        intent = getIntent();
        String eventName = intent.getExtras().getString("eventName");

        DocumentReference docRef = ref.collection("Events").document(eventName);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        //intent = getIntent();
                        GeoPoint geo = document.getGeoPoint("latLong");
                        String name = document.getString("name");
                        double lat = geo.getLatitude();//intent.getExtras().getDouble("lat");
                        double lng = geo.getLongitude();//intent.getExtras().getDouble("long");
                        LatLng latLng = new LatLng(lat,lng);
                        mMap.addMarker(new MarkerOptions().position(latLng).title(name));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));
                    } else {
                        //Log.d(TAG, "No such document");
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
