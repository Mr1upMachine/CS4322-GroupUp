package com.example.seanh.groupup;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pes.androidmaterialcolorpickerdialog.ColorPicker;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class EventEditActivity extends AppCompatActivity {
    private final String LOGTAG = "EventEditActivity";
    private Event event;
    private User user;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private EditText editName, editDesc, editAddress;
    private TextView textStartTime, textEndTime, textDate, textEventEditAttendance, textEventEditCapacity;
    private ImageView eventPicture;
    private String strAddress = "", strAddressStreet = "", strAddressAreaState = "", strAddressZip = "";
    private Calendar startDateTime, endDateTime;
    public static double numLocX = 0.0, numLocY = 0.0;
    private ColorPicker cp;
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
    private static final int RESULT_LOAD_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);
        overridePendingTransition(R.anim.slide_in, R.anim.nothing);
        setTitle("Edit an Event:");

        //clears cache
        final SharedPreferences preferences = getSharedPreferences("MapAddress", 0);
        preferences.edit().remove("address").remove("locX").remove("locY").apply();
        finish();

        //continuation of value setup from above
        editName = findViewById(R.id.editEventEditName);
        editDesc = findViewById(R.id.editEventEditDescription);
        textStartTime = findViewById(R.id.textEventEditStartTime);
        textEndTime = findViewById(R.id.textEventEditEndTime);
        textDate = findViewById(R.id.textEventEditDate);
        editAddress = findViewById(R.id.editEventEditAddress);
        textEventEditAttendance = findViewById(R.id.textEventEditAttendance);
        textEventEditCapacity = findViewById(R.id.textEventEditCapacity);
        eventPicture = null;

        final android.support.v7.widget.Toolbar tb = findViewById(R.id.toolbarEventEdit);
        setSupportActionBar(tb);
        tb.setNavigationIcon(R.drawable.ic_arrow_back_white_36dp);
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //clears cache
                SharedPreferences preferences = getSharedPreferences("MapAddress", 0);
                preferences.edit().remove("address").remove("locX").remove("locY").apply();
                finish();
                overridePendingTransition(R.anim.nothing, R.anim.slide_out);
            }
        });

        //Gets Event object from Main Activity
        final Bundle b = getIntent().getExtras();
        event = b.getParcelable("myEvent");
        user = b.getParcelable("myUser");

        editName.setText( event.getName() );
        editDesc.setText( event.getDescription() );
        textStartTime.setText( event.genStartTimeSimple() );
        textEndTime.setText( event.genEndTimeSimple() );
        textDate.setText( event.genStartDateSimple() );
        strAddress = event.getAddress();
        strAddressStreet = event.getAddressStreet();
        strAddressAreaState = event.getAddressCityState();
        strAddressZip = event.getAddressZip();
        editAddress.setText( strAddress );
        textEventEditAttendance.setText( ""+event.getAttendance() );
        textEventEditCapacity.setText( ""+event.getCapacity() );
        startDateTime = Calendar.getInstance();
        startDateTime.setTimeInMillis(event.getStartDateTime());
        endDateTime = Calendar.getInstance();
        endDateTime.setTimeInMillis(event.getEndDateTime());
        numLocX = event.getLocX();
        numLocY = event.getLocY();
        cp = new ColorPicker(this, event.genColorR(), event.genColorG(), event.genColorB());
        tb.setBackgroundColor( event.getColor() );

        //adds picture
        findViewById(R.id.buttonEditEventPicture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View w) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        textDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker(textDate);
            }
        });
        textStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeStPicker(textStartTime);
            }
        });
        textEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeEdPicker(textEndTime);
            }
        });
        textEventEditAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberPickerDialog(textEventEditAttendance);
            }
        });
        textEventEditCapacity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberPickerDialog(textEventEditCapacity);
            }
        });

        //Go to EventEditMap Activity
        findViewById(R.id.btnEventEditMap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EventEditActivity.this, EventCreateMap.class));
            }
        });

        final Button buttonEventEditColor = findViewById(R.id.buttonEventEditColor);
        buttonEventEditColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* Show color picker dialog */
                cp.show();

                /* On Click listener for the dialog, when the user select the color */
                cp.findViewById(R.id.okColorButton).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tb.setBackgroundColor(Color.rgb(cp.getRed(), cp.getGreen(), cp.getBlue()));
                        cp.dismiss();
                    }
                });
            }
        });

        //Code for Submit button
        findViewById(R.id.fabEditEventSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String strName = editName.getText().toString();
                final String strDesc = editDesc.getText().toString();
                final String strDate = textDate.getText().toString();
                final String strStartTime = textStartTime.getText().toString();
                final String strEndTime = textEndTime.getText().toString();
                final String strAttendance = textEventEditAttendance.getText().toString();
                final String strCapacity = textEventEditCapacity.getText().toString();


                //checks to see if user image is given, if so then upload to database


                //Verifies all data fields are filled
                if (!strName.isEmpty() &&
                        !strDesc.isEmpty() &&
                        !strDate.isEmpty() &&
                        !strStartTime.isEmpty() &&
                        !strEndTime.isEmpty() &&
                        !strAddress.isEmpty() &&
                        cp.getColor() != Color.rgb(127,127,127) &&
                        eventPicture != null) {

                    event.setName(strName);
                    event.setDescription(strDesc);
                    event.setStartDateTime(startDateTime.getTimeInMillis());
                    event.setEndDateTime(endDateTime.getTimeInMillis());
                    event.setAddress(strAddress);
                    event.setAddressStreet(strAddressStreet);
                    event.setAddressCityState(strAddressAreaState);
                    event.setAddressZip(strAddressZip);
                    event.setAttendance(Integer.parseInt(strAttendance));
                    event.setCapacity(Integer.parseInt(strCapacity));
                    event.setColor(cp.getColor());
                    event.setLocX(numLocX);
                    event.setLocY(numLocY);

                    //uploads event data
                    editEvent(event);


                    //Edit bitmap of image, compress to PNG,
                    //store byte array of image in imageData containing raw pixels
                    eventPicture.setDrawingCacheEnabled(true);
                    eventPicture.buildDrawingCache();
                    Bitmap bitmap = eventPicture.getDrawingCache();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    eventPicture.setDrawingCacheEnabled(false);
                    byte[] imageData = baos.toByteArray();

                    //path to where image will be saved in Firebase dir
                    String path = "eventImages/" + event.getId() + ".png";

                    //eventRef is the unique reference pointing to the image in Firebase,
                    //use to reference image when calling for eventView
                    StorageReference eventRef = storage.getReference(path);


                    //line physically uploading image to firebase
                    UploadTask uploadtask = eventRef.putBytes(imageData);

                    //TODO implement UploadTask to give progress bar so user knows when upload completes


                    Toast.makeText(getApplicationContext(), "Event edited successfully",
                            Toast.LENGTH_SHORT).show();

                    //clears cache
                    final SharedPreferences preferences = getSharedPreferences("MapAddress", 0);
                    preferences.edit().remove("address").remove("locX").remove("locY").apply();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Please fill all fields, select picture, & select color",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        final SharedPreferences prefs = getSharedPreferences("MapAddress", MODE_PRIVATE);
        final String restoredText = prefs.getString("address", null);
        if (restoredText != null) {
            editAddress.setText(prefs.getString("address", "No name defined"));//"No name defined" is the default value.
            numLocX = Double.parseDouble( prefs.getString("locX", "No name defined") );//"No name defined" is the default value.
            numLocY = Double.parseDouble( prefs.getString("locY", "No name defined") );//"No name defined" is the default value.
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            eventPicture = findViewById(R.id.imageViewEditEventPicture);

            Bitmap bmp = null;
            try {
                bmp = getBitmapFromUri(selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            eventPicture.setImageBitmap(bmp);

            //sets plus button to 20% opacity
            final ImageButton ib = findViewById(R.id.buttonEditEventPicture);
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


    private void datePicker(final TextView tv) {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        final int mYear = c.get(Calendar.YEAR);
        final int mMonth = c.get(Calendar.MONTH);
        final int mDay = c.get(Calendar.DAY_OF_MONTH);

        final DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        tv.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        startDateTime.set(year, monthOfYear, dayOfMonth);
                        endDateTime.set(year, monthOfYear, dayOfMonth);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }
    private void timeStPicker(final TextView tv) {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        final int mStHour = c.get(Calendar.HOUR_OF_DAY);
        final int mStMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        final TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        final DecimalFormat df = new DecimalFormat("00"); //makes minute have 2 digits
                        tv.setText(hourOfDay + ":" + df.format(minute));
                        startDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        startDateTime.set(Calendar.MINUTE, minute);
                    }
                }, mStHour, mStMinute, false);
        timePickerDialog.show();
    }
    private void timeEdPicker(final TextView tv) {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        final int mEdHour = c.get(Calendar.HOUR_OF_DAY);
        final int mEdMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        final TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        final DecimalFormat df = new DecimalFormat("00"); //makes minute have 2 digits
                        tv.setText(hourOfDay + ":" + df.format(minute));
                        endDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        endDateTime.set(Calendar.MINUTE, minute);
                    }
                }, mEdHour, mEdMinute, false);

        timePickerDialog.show();
    }
    private void numberPickerDialog(final TextView tv) {
        final Dialog d = new Dialog(this);
        d.setTitle("NumberPicker");
        d.setContentView(R.layout.dialog_number_picker);
        Button b1 = d.findViewById(R.id.buttonDialogNPSet);
        Button b2 = d.findViewById(R.id.buttonDialogNPCancel);
        final NumberPicker np = d.findViewById(R.id.numberPicker1);
        if(tv.getId() == R.id.textEventEditAttendance){ //prevents data from overflowing
            np.setMaxValue(Integer.parseInt(textEventEditCapacity.getText().toString()) - 1); // max value capacity-1
            np.setMinValue(0);   // min value 0
        }
        else{
            np.setMaxValue(99); // max value 99
            np.setMinValue(Integer.parseInt(textEventEditAttendance.getText().toString()) + 1);   // min value attendance+1
        }

        np.setWrapSelectorWheel(false);
        //np.setOnValueChangedListener(this);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.setText(String.valueOf(np.getValue())); //set the value to textview
                d.dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss(); // dismiss the dialog
            }
        });
        d.show();
    }


    public String generateGeoFull(double xCord, double yCord) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(xCord, yCord, 1);

            String add = addresses.get(0).getAddressLine(0);
            String area = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String postalCode = addresses.get(0).getPostalCode();

            return add + ", " + area + " " + state + " " + postalCode;

        } catch (Exception e) {
            Log.d(LOGTAG, "Get Location Failed");
        }
        return null;
    }
    public String generateGeoAdd(double xCord, double yCord) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(xCord, yCord, 1);
            return addresses.get(0).getAddressLine(0);

        } catch (Exception e) {
            Log.d(LOGTAG, "Get Location Failed");
        }
        return null;
    }
    public String generateGeoAreaState(double xCord, double yCord) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(xCord, yCord, 1);

            String area = addresses.get(0).getLocality();
            String city = addresses.get(0).getAdminArea();

            return area + ", " + city;

        } catch (Exception e) {
            Log.d(LOGTAG, "Get Location Failed");
        }
        return null;
    }
    public String generateGeoZip(double xCord, double yCord) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(xCord, yCord, 1);
            return addresses.get(0).getPostalCode();
        } catch (Exception e) {
            Log.d(LOGTAG, "Get Location Failed");
        }
        return null;
    }


    private final DatabaseReference dataRoot = FirebaseDatabase.getInstance().getReference();
    private final DatabaseReference dataEvents = dataRoot.child("events");
    public String editEvent(Event e) {
        final DatabaseReference dr = dataEvents.child( e.getId() ); //generates unique id for event
        dr.setValue(e); //uploads data to database
        return e.getId();
    }
}
