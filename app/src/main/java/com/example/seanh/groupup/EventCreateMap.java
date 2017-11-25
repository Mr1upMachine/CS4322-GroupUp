package com.example.seanh.groupup;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class EventCreateMap extends FragmentActivity implements OnMapReadyCallback {

    android.support.v7.widget.Toolbar tb;
    public GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_create_map);
        overridePendingTransition(R.anim.slide_in, R.anim.nothing);

        tb = findViewById(R.id.toolbarEventCreateMap);
        tb.setTitle("Select a location:");
        tb.setNavigationIcon(R.drawable.ic_arrow_back_white_36dp);
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.nothing, R.anim.slide_out);
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Marker Event;

        findViewById(R.id.btnSearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText locationSearch = findViewById(R.id.editSearch);
                String location = locationSearch.getText().toString();

                if(location != null || !location.equals("")) {
                    Geocoder geocoder = new Geocoder(EventCreateMap.this, Locale.getDefault());
                    List<Address> addressList = null;
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(),address.getLongitude());
                    Marker event = mMap.addMarker(new MarkerOptions().position(latLng).draggable(true).title("Event"));
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                }
            }
        });


    }







    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }


            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                marker.getPosition();
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(EventCreateMap.this, Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocation(marker.getPosition().latitude, marker.getPosition().longitude, 1);
                    String city = addresses.get(0).getAddressLine(1);
                    LatLng eventLocation = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
                    Toast.makeText(EventCreateMap.this, city, Toast.LENGTH_SHORT).show();
                    //Event Location to Database********
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });



        Marker Current;


        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        float zoomLevel = 12f;
        double numLocX = EventCreateActivity.numLocX;
        double numLocY = EventCreateActivity.numLocY;
        LatLng event = new LatLng(numLocX - 0.00000001,numLocY - 0.0000001);
        LatLng current = new LatLng(numLocX,numLocY);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, zoomLevel));
        Current = mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.littleguy))
                .position(current)
                .title("Current Location"));
                Current.setTag(0);

    }

}
