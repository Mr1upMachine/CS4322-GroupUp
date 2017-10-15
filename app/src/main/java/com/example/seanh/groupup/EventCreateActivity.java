package com.example.seanh.groupup;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;

public class EventCreateActivity extends AppCompatActivity {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    EditText editName, editDesc, editWhen, editLocX, editLocY;

    String date_time = "", strName = "", strDesc = "", strWhen = "";
    int mYear, mMonth, mDay, mHour, mMinute;
    double numLocX = 0.0, numLocY = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_create);

        //sets action bar to user's email (for testing)
        setTitle("Create an Event:");

        editName = (EditText) findViewById(R.id.editEventCreateName);
        editDesc = (EditText) findViewById(R.id.editEventCreateDescription);
        editWhen = (EditText) findViewById(R.id.editEventCreateWhen);
        editLocX = (EditText) findViewById(R.id.editEventCreateLocX);
        editLocY = (EditText) findViewById(R.id.editEventCreateLocY);



        editWhen.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                datePicker();

            }
        });

        findViewById(R.id.buttonCreateEventSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strName = editName.getText().toString();
                strDesc = editDesc.getText().toString();
                strWhen = editWhen.getText().toString();
                numLocX = Double.parseDouble(editLocX.getText().toString());
                numLocY = Double.parseDouble(editLocY.getText().toString());

                //Verifies all data fields are filled
                if(!strName.isEmpty() && !strDesc.isEmpty() && !strWhen.isEmpty() && numLocX!=0.0 && numLocY!=0.0){
                    Database.createNewEvent(new Event(strName, strDesc, strWhen, "Dummy pic URL here", numLocX, numLocY,
                            new User(user.getUid(), user.getEmail(), user.getDisplayName(), user.getDisplayName()) ));
                    try{   Thread.sleep(250);   }catch(Exception e){} //Arbitrary waiting for design choice
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

                        editWhen.setText(date_time+" "+hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }
}
