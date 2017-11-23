package com.example.seanh.groupup;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.FileDescriptor;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

//TODO Micah stuff here
public class EventCreateActivity extends AppCompatActivity {
    private final String LOGTAG = "EventCreateActivity";
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private EditText editName, editDesc, editWhere;
    TextView textStartTime, textEndTime, textStartDate, textEndDate;
    private ImageView eventPicture;
    private String date_time = "", strName = "", strDesc = "", strStartDate = "", strEndDate = "", strStartTime = "", strEndTime = "", strWhere = "", strType = "", strSpinnerInit = "";
    private Bitmap bitMapEventImage;
    private int mYear, mMonth, mDay, mHour, mMinute;
    public static double numLocX = 0.0;
    public static double numLocY = 0.0;
    private LocationManager mLocationManager;
    private String provider;
    private Spinner editSpinner;
    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            numLocX = location.getLatitude();
            numLocY = location.getLongitude();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    };
    private static int RESULT_LOAD_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_create);

        setupKeyboardHide(findViewById(R.id.layoutEventCreate)); //This auto-hides the keyboard
        setTitle("Create an Event:");

        //continuation of value setup from above
        editName = findViewById(R.id.editEventCreateName);
        editDesc = findViewById(R.id.editEventCreateDescription);
        textStartTime = findViewById(R.id.textEventCreateStartTime);
        textEndTime = findViewById(R.id.textEventCreateEndTime);
        textStartDate = findViewById(R.id.textEventCreateStartDate);
        textEndDate = findViewById(R.id.textEventCreateEndDate);
        eventPicture = null;
        editSpinner = findViewById(R.id.editEventCreateTypeSpinner);
        strSpinnerInit = "Event Type";

        //not sure how this'll fit in with GoogleMaps, or if it'll just get replaced with eric's code

//        editLocX = (EditText) findViewById(R.id.editEventCreateLocX);
//        editLocY = (EditText) findViewById(R.id.editEventCreateLocY);

        //updates location first time TODO Doesn't work on API 26 Why?
        setupLocation();

        //Button for Google Maps extension, currently just sets address to current Latitude and Longitude
        findViewById(R.id.btnMap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editWhere.setText("" + numLocX + ", ");
                editWhere.setText("" + numLocY);
                Toast.makeText(getApplicationContext(), "Right now I just populate the current Lat/Lon!", Toast.LENGTH_LONG).show();
            }
        });


        textStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerStart();
            }
        });

        textEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerEnd();
            }
        });

        textStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerStart();
            }
        });

        textEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerEnd();
            }
        });

        //Go to EventCreateMap Activity

        findViewById(R.id.btnMap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EventCreateActivity.this, EventCreateMap.class));
            }
        });

        //Code for Submit button
        findViewById(R.id.fabCreateEventSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strName = editName.getText().toString();
                strDesc = editDesc.getText().toString();
                strStartDate = textStartDate.getText().toString();
                strEndDate = textEndDate.getText().toString();
                strStartTime = textStartTime.getText().toString();
                strEndTime = textEndTime.getText().toString();
                strWhere = editWhere.getText().toString();
                strType = editSpinner.getSelectedItem().toString();


//                numLocX = Double.parseDouble(editLocX.getText().toString());
//                numLocY = Double.parseDouble(editLocY.getText().toString());

                //Verifies all data fields are filled
                if (
                        !strName.isEmpty() &&
                                !strDesc.isEmpty() &&
                                !strStartDate.isEmpty() &&
                                !strEndDate.isEmpty() &&
                                !strStartTime.isEmpty() &&
                                !strEndTime.isEmpty() &&
                                !strWhere.isEmpty() &&
                                user != null &&
                                !Objects.equals(strType, strSpinnerInit)
                        ) {

                    Database.createNewEvent(new Event(
                            strName, strDesc,
                            strStartTime, strEndTime,
                            strStartDate, strEndDate,
                            strWhere, strType,
                            bitMapEventImage,
                            user.getUid()));

                    Toast.makeText(getApplicationContext(), "Event created successfully",
                            Toast.LENGTH_LONG).show();

                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Please fill all fields",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.buttonCreateEventPicture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View w) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);

            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            eventPicture = findViewById(R.id.imageViewCreateEventPicture);

            Bitmap bmp = null;
            try {
                bmp = getBitmapFromUri(selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            bitMapEventImage = bmp;
            eventPicture.setImageBitmap(bmp);

            //TODO sets plus button to 20% opacity, change value to like 1% later
            ImageButton ib = findViewById(R.id.buttonCreateEventPicture);
            ib.setAlpha(0.2f);
        }

    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();

        return image;
    }


    private void datePickerStart() {

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        textStartDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void datePickerEnd() {

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        textEndDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void timePickerStart() {
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
                        textStartTime.setText(hourOfDay + ":" + df.format(minute));
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    private void timePickerEnd() {
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
                        textEndTime.setText(hourOfDay + ":" + df.format(minute));
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }


    public void setupLocation() {

        TextView address;
        Geocoder geocoder;
        List<Address> addresses;


        //security check if permission is not already granted
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            findViewById(R.id.btnMap).setVisibility(View.GONE);
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

        //TODO Location**************************
        String FullAddress = GeoFull(numLocX, numLocY);
        address = findViewById(R.id.editAddress);
        address.setText(FullAddress);

    }


    //TODO Coordinates to the entire Address
    public String GeoFull(double xCord, double yCord) {

        Geocoder geocoder;
        List<Address> addresses;


        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(xCord, yCord, 1);

            String add = addresses.get(0).getAddressLine(0);
            String area = addresses.get(0).getLocality();
            String city = addresses.get(0).getAdminArea();
            String postalCode = addresses.get(0).getPostalCode();

            String FullAddress = add + ", " + area + ", " + city + ", " + postalCode;


            return FullAddress;

        } catch (Exception e) {
            Log.d(LOGTAG, "Get Location Failed");
        }
        return null;
    }

    //TODO Coordinates to Address
    public String GeoAdd(double xCord, double yCord) {

        Geocoder geocoder;
        List<Address> addresses;


        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(xCord, yCord, 1);

            String add = addresses.get(0).getAddressLine(0);


            String FullAddress = add;


            return FullAddress;

        } catch (Exception e) {
            Log.d(LOGTAG, "Get Location Failed");
        }
        return null;
    }

    //TODO Coordinates to City, State
    public String GeoState(double xCord, double yCord) {

        Geocoder geocoder;
        List<Address> addresses;


        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(xCord, yCord, 1);

            String area = addresses.get(0).getLocality();
            String city = addresses.get(0).getAdminArea();

            String FullAddress = area + ", " + city;


            return FullAddress;

        } catch (Exception e) {
            Log.d(LOGTAG, "Get Location Failed");
        }
        return null;
    }

    //TODO Coordinates to Zipcode
    public String GeoZip(double xCord, double yCord) {

        Geocoder geocoder;
        List<Address> addresses;


        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(xCord, yCord, 1);

            String postalCode = addresses.get(0).getPostalCode();

            String FullAddress = postalCode;


            return FullAddress;

        } catch (Exception e) {
            Log.d(LOGTAG, "Get Location Failed");
        }
        return null;
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
