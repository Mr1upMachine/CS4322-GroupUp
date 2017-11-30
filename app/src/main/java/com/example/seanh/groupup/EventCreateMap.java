package com.example.seanh.groupup;

import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

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
    LatLng eventLocation;
    EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_create_map);
        overridePendingTransition(R.anim.slide_in, R.anim.nothing);

        et = findViewById(R.id.editEventCreateMapSearch);

        double numLocX = 33.93775966448825;
        double numLocY = -84.52007937612456;
        eventLocation = new LatLng(numLocX, numLocY);

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
        tb.inflateMenu(R.menu.menu_event_create_map);
        tb.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.menu_event_create_map_submit){
                    SharedPreferences.Editor editor = getSharedPreferences("MapAddress", MODE_PRIVATE).edit();
                    EditText et = findViewById(R.id.editEventCreateMapSearch);
                    editor.putString("address", et.getText().toString());
                    editor.putString("locX", ""+eventLocation.latitude);
                    editor.putString("locY", ""+eventLocation.longitude);
                    editor.apply();
                    finish();
                    overridePendingTransition(R.anim.nothing, R.anim.slide_out);
                }
                return true;
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
                EditText locationSearch = findViewById(R.id.editEventCreateMapSearch);
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

                    locationSearch.setText(address.getAddressLine(0));

                    eventLocation = new LatLng(address.getLatitude(),address.getLongitude());
                    et.setText(address.getAddressLine(0));
                    Marker event = mMap.addMarker(new MarkerOptions().position(eventLocation).draggable(true).title("Event"));
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(eventLocation));
                }
            }
        });


    }







    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear();

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
                EditText locationSearch = findViewById(R.id.editEventCreateMapSearch);
                try {
                    addresses = geocoder.getFromLocation(marker.getPosition().latitude, marker.getPosition().longitude, 1);
                    String city = addresses.get(0).getAddressLine(1);
                    eventLocation = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
                    locationSearch.setText(addresses.get(0).getAddressLine(0));
                    //Toast.makeText(EventCreateMap.this, city, Toast.LENGTH_SHORT).show();
                    //Event Location to Database********
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });



        Marker Current;


        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        float zoomLevel = 12f;
        double numLocX = 33.93775966448825;
        double numLocY = -84.52007937612456;
        //LatLng event = new LatLng(numLocX - 0.00000001,numLocY - 0.0000001);
        LatLng current = new LatLng(numLocX,numLocY);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, zoomLevel));
        Current = mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.littleguy))
                .position(current)
                .title("Current Location"));
                Current.setTag(0);

    }

}
