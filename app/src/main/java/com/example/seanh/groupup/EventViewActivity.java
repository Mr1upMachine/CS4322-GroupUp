package com.example.seanh.groupup;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class EventViewActivity extends AppCompatActivity {
    private Toolbar tb;
    private TextView textEventViewOwner;
    private ImageView imageEventImage;
    private Event event;
    private User user, owner;
    private boolean isOwner = false;
    
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
        tb.inflateMenu(R.menu.menu_event_view);
        
        textEventViewOwner =  findViewById(R.id.textEventViewOwner);
        final TextView textEventViewStartTime = findViewById(R.id.textEventViewStartTime);
        final TextView textEventViewEndTime = findViewById(R.id.textEventViewEndTime);
        final TextView textEventViewDate = findViewById(R.id.textEventViewDate);
        final TextView textEventViewDescription = findViewById(R.id.textEventViewDescription);
        final TextView textEventViewAddress = findViewById(R.id.textEventViewAddress);
        final TextView textEventViewAttendance = findViewById(R.id.textEventViewAttendance);
        final TextView textEventViewCapacity = findViewById(R.id.textEventViewCapacity);
        imageEventImage = findViewById(R.id.app_bar_image);

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
            Intent i = new Intent(EventViewActivity.this, EventEditActivity.class);
            Bundle b = new Bundle();
            b.putParcelable("myEvent", event);
            b.putParcelable("myUser", user);
            i.putExtras(b);
            startActivity(i);

        if(id == R.id.menu_event_view_delete) {
            //TODO delete here
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.nothing, R.anim.slide_out);
    }

    public void setUpJoinButton(){
        final Button buttonViewEventJoin = findViewById(R.id.buttonViewEventJoin);
        if( !user.getId().equals(event.getOwnerId()) ) {
            if (user.getSubscribedEventIds().contains(event.getId())) {
                buttonViewEventJoin.setText("Leave");
            }
            buttonViewEventJoin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!user.getSubscribedEventIds().contains(event.getId())) {
                        event.addSubscribedToEvent(user.getId());
                        user.addSubscribedEvent(event.getId());
                        buttonViewEventJoin.setText("Leave");
                        Toast.makeText(EventViewActivity.this, "Event joined successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        event.removeSubscribedToEvent(user.getId());
                        user.removeSubscribedEvent(event.getId());
                        buttonViewEventJoin.setText("Join");
                        Toast.makeText(EventViewActivity.this, "Event left successfully", Toast.LENGTH_SHORT).show();
                    }
                    Database.updateEvent(event);
                    Database.updateUser(user);
                }
            });
        }
        else{
            buttonViewEventJoin.setText("Owner");
            buttonViewEventJoin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(EventViewActivity.this, "I'd hope you're already going", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    //relevant database calls
    private final DatabaseReference dataRoot = FirebaseDatabase.getInstance().getReference();
    public void fetchOwner(String id){
        // Read from the database
        dataRoot.child("users").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                owner = dataSnapshot.getValue(User.class);
                textEventViewOwner.setText("Host: " + owner.getfName() + " " + owner.getlName() );
                if(owner.getId().equals(user.getId())) {
                    isOwner = true;
                    invalidateOptionsMenu();
                }
                setUpJoinButton();
            }

            @Override
            public void onCancelled(DatabaseError error) {  }
        });
    }
}
