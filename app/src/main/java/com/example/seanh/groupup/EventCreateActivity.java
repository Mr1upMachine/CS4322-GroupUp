package com.example.seanh.groupup;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DecimalFormat;
import java.util.Calendar;

//TODO Micah stuff here
public class EventCreateActivity extends AppCompatActivity {
    private final String LOGTAG = "EventCreateActivity";
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private EditText editName, editDesc, editWhen, editLocX, editLocY;
    private String date_time = "", strName = "", strDesc = "", strWhen = "";
    private int mYear, mMonth, mDay, mHour, mMinute;
    private double numLocX = 0.0, numLocY = 0.0;
    private LocationManager mLocationManager;
    private String provider;
    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            numLocX = location.getLatitude();
            numLocY = location.getLongitude();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {  }
        @Override
        public void onProviderEnabled(String provider) {  }
        @Override
        public void onProviderDisabled(String provider) {  }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_create);
        setupKeyboardHide(findViewById(R.id.layoutEventCreate)); //This auto-hides the keyboard
        setTitle("Create an Event:");

        //continuation of value setup from above
        editName = (EditText) findViewById(R.id.editEventCreateName);
        editDesc = (EditText) findViewById(R.id.editEventCreateDescription);
        editWhen = (EditText) findViewById(R.id.editEventCreateWhen);
        editLocX = (EditText) findViewById(R.id.editEventCreateLocX);
        editLocY = (EditText) findViewById(R.id.editEventCreateLocY);

        //updates location first time TODO Doesn't work on API 26 Why?
        setupLocation();

        //Sets the EditText to the current location
        findViewById(R.id.buttonCreateEventGetloc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editLocX.setText(""+numLocX);
                editLocY.setText(""+numLocY);
            }
        });


        editWhen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker();
            }
        });

        //Code for Submit button
        findViewById(R.id.buttonCreateEventSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strName = editName.getText().toString();
                strDesc = editDesc.getText().toString();
                strWhen = editWhen.getText().toString();
                numLocX = Double.parseDouble(editLocX.getText().toString());
                numLocY = Double.parseDouble(editLocY.getText().toString());

                //Verifies all data fields are filled
                if(!strName.isEmpty() && !strDesc.isEmpty() && !strWhen.isEmpty() && numLocX!=0.0 && numLocY!=0.0 && user!=null){
                    Database.createNewEvent(new Event(strName, strDesc, strWhen, "Dummy pic URL here", numLocX, numLocY, user.getUid()));
                    Toast.makeText(getApplicationContext(), "Event created successfully",Toast.LENGTH_LONG).show();
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void datePicker(){

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        date_time = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        //*************Call Time Picker Here ********************
                        timePicker();
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
    private void timePicker(){
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        mHour = hourOfDay;
                        mMinute = minute;

                        DecimalFormat df = new DecimalFormat("00"); //makes minute have 2 digits
                        editWhen.setText(date_time+" "+hourOfDay + ":" + df.format(minute));
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }


    public void setupLocation(){

        //security check if permission is not already granted
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            findViewById(R.id.buttonCreateEventGetloc).setVisibility(View.GONE);
            return;
        }

        //TODO properly prevent app from crashing if location permission is not granted
        //sets up the location for the first time

            //sets up location manager
            mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            //updates location anytime the gps updates
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,
                    0.5f, mLocationListener);

        try {
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            criteria.setAltitudeRequired(false);
            criteria.setBearingRequired(false);
            criteria.setCostAllowed(true);
            criteria.setPowerRequirement(Criteria.POWER_LOW);
            provider = mLocationManager.getBestProvider(criteria, true);
            if (provider != null) {
                Location l = mLocationManager.getLastKnownLocation(provider);
                numLocX = l.getLatitude();
                numLocY = l.getLongitude();
            }
        } catch (Exception e) {
            Log.d(LOGTAG, "Get Location Failed");
        }
    }






    //This auto-hides the keyboard
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }
    public void setupKeyboardHide(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(EventCreateActivity.this);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupKeyboardHide(innerView);
            }
        }
    }
}
