package com.example.seanh.groupup;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class EventViewActivity extends AppCompatActivity implements OnMapReadyCallback {
    private Toolbar tb;
    private TextView textEventViewOwner, textEventViewAttendance;
    private ImageView imageEventImage;
    private Event event;
    private User user, owner;
    //private boolean isOwner = false;
    GoogleMap mMap;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_view);
        overridePendingTransition(R.anim.slide_in, R.anim.nothing);

        //Gets Event object from Main Activity
        final Bundle b = getIntent().getExtras();
        event = b.getParcelable("myEvent");
        user = b.getParcelable("myUser");
        fetchOwner(event.getOwnerId());

        tb = findViewById(R.id.toolbarEventView);
        setSupportActionBar(tb);
        getSupportActionBar().setTitle( event.getName() );
        tb.setNavigationIcon(R.drawable.ic_arrow_back_white_36dp);
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.nothing, R.anim.slide_out);
            }
        });
        //tb.inflateMenu(R.menu.menu_event_view);
        
        textEventViewOwner =  findViewById(R.id.textEventViewOwner);
        final TextView textEventViewStartTime = findViewById(R.id.textEventViewStartTime);
        final TextView textEventViewEndTime = findViewById(R.id.textEventViewEndTime);
        final TextView textEventViewDate = findViewById(R.id.textEventViewDate);
        final TextView textEventViewDescription = findViewById(R.id.textEventViewDescription);
        final TextView textEventViewAddress = findViewById(R.id.textEventViewAddress);
        textEventViewAttendance = findViewById(R.id.textEventViewAttendance);
        final TextView textEventViewCapacity = findViewById(R.id.textEventViewCapacity);
        imageEventImage = findViewById(R.id.app_bar_image);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.MapViewActivity);
                mapFragment.getMapAsync(this);

        textEventViewStartTime.setText( event.genStartTimeSimple() );
        textEventViewEndTime.setText( event.genEndTimeSimple() );
        textEventViewDate.setText( event.genStartDateSimple() );
        textEventViewDescription.setText( event.getDescription() );
        textEventViewAddress.setText( event.genAddressPretty() );
        textEventViewAttendance.setText( ""+event.getAttendance() );
        textEventViewCapacity.setText( ""+event.getCapacity() );


        final String eventID = event.getId();
        final FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReferenceFromUrl("gs://groupup-f9e17.appspot.com/eventImages").child(eventID + ".png");



        //getting file as byteArray
        final long ONE_MEGABYTE = (1024 * 1024);
        storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imageEventImage.setImageBitmap(bitmap);
            }
        });


        findViewById(R.id.buttonViewEventShare).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, event.toStringShare());
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Share event details:"));
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        float zoomLevel = 12f;
        double numLocX = 33.93775966448825;
        double numLocY = -84.52007937612456;
        LatLng eventLocation = new LatLng(event.getLocX(),event.getLocY());
        LatLng current = new LatLng(numLocX,numLocY);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(eventLocation, zoomLevel));
        mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.littleguy))
                .position(current)
                .title("Current Location"));

        mMap.addMarker(new MarkerOptions()
                    .position(eventLocation)
                    .title(event.getName()));
    }

    /*
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(isOwner) {
            getMenuInflater().inflate(R.menu.menu_event_view, menu);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();


            //passes through the information to the next activity about this event
            //Intent i = new Intent(getApplicationContext(), EventEditActivity.class);
            //startActivity(i);

        if(id == R.id.menu_event_view_delete) {
            //TODO delete here
        }

        return super.onOptionsItemSelected(item);
    }
    */

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.nothing, R.anim.slide_out);
    }

    public void setUpJoinButton() {
        final Button buttonViewEventJoin = findViewById(R.id.buttonViewEventJoin);
        if (user != null) {
            if (!user.getId().equals(event.getOwnerId())) {
                if (user.getSubscribedEventIds().contains(event.getId())) {
                    buttonViewEventJoin.setText("Leave");
                }
                buttonViewEventJoin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!user.getSubscribedEventIds().contains(event.getId())) {
                            event.addSubscribedToEvent(user.getId());
                            user.addSubscribedEvent(event.getId());

                            if (event.getAttendance() >= event.getCapacity()) {
                                Toast.makeText(EventViewActivity.this, "Event is full!", Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                                event.setAttendance(event.getAttendance() + 1);
                                textEventViewAttendance.setText("" + event.getAttendance());
                                buttonViewEventJoin.setText("Leave");
                                Toast.makeText(EventViewActivity.this, "Event joined successfully", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            event.removeSubscribedToEvent(user.getId());
                            user.removeSubscribedEvent(event.getId());

                            event.setAttendance(event.getAttendance() - 1);
                            textEventViewAttendance.setText("" + event.getAttendance());

                            buttonViewEventJoin.setText("Join");
                            Toast.makeText(EventViewActivity.this, "Event left successfully", Toast.LENGTH_SHORT).show();
                        }
                        //relegates it to background task
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                updateEvent(event);
                                updateUser(user);
                            }
                        };
                        new Thread(runnable).start();
                    }
                });
            } else {
                buttonViewEventJoin.setText("Owner");
                buttonViewEventJoin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(EventViewActivity.this, "I'd hope you're already going", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }


    private void scheduleNotification(int delay) {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle(event.getName());
        builder.setContentText("This event is starting soon!"); //TODO Finish
        builder.setSmallIcon(R.drawable.ic_location_on_white_18dp);
        Notification notification = builder.build();

        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }


    //relevant database calls
    private final DatabaseReference dataRoot = FirebaseDatabase.getInstance().getReference();
    public void fetchOwner(String id){
        // Read from the database
        dataRoot.child("users").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                owner = dataSnapshot.getValue(User.class);
                if (owner != null) {
                    textEventViewOwner.setText("Host: " + owner.getfName() + " " + owner.getlName() );
                }
                /*if(owner.getId().equals(user.getId())) {
                    isOwner = true;
                    invalidateOptionsMenu();
                }*/
                setUpJoinButton();
            }

            @Override
            public void onCancelled(DatabaseError error) {  }
        });
    }
    public void updateEvent(Event newEvent){
        dataRoot.child("events").child(newEvent.getId()).setValue(newEvent); //uploads data to database
    }
    public void updateUser(User u){
        dataRoot.child("users").child(u.getId()).setValue(u);
    }
}
