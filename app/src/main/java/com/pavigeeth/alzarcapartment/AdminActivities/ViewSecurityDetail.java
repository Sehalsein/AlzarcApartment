package com.pavigeeth.alzarcapartment.AdminActivities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pavigeeth.alzarcapartment.Model.MeetingDetail;
import com.pavigeeth.alzarcapartment.Model.SecurityDetail;
import com.pavigeeth.alzarcapartment.R;

public class ViewSecurityDetail extends AppCompatActivity {

    private String id;
    private TextView nameTextView;
    private TextView phoneTextView;
    private TextView timingTextView;
    private TextView addressTextView;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef;
    private static String NODE = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_security_detail);

        id = getIntent().getStringExtra("EXTRA_ID");
        //makeToast(id);

        nameTextView = findViewById(R.id.name_text_view);
        phoneTextView = findViewById(R.id.phone_number_text_view);
        timingTextView = findViewById(R.id.timing_number_text_view);
        addressTextView = findViewById(R.id.address_text_view);
        id = getIntent().getStringExtra("EXTRA_ID");
        //makeToast(id);


        NODE = getResources().getString(R.string.firebase_database_node_security_detail);
        myRef = database.getReference(NODE).child(id);


        loadMeeting();

    }

    private void loadMeeting(){

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                SecurityDetail data = dataSnapshot.getValue(SecurityDetail.class);
                nameTextView.setText(data.getName());
                phoneTextView.setText(data.getPhoneNo());
                timingTextView.setText(data.getTiming());
                addressTextView.setText(data.getAddress());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private void makeToast(String message) {
        Toast.makeText(ViewSecurityDetail.this, message, Toast.LENGTH_SHORT).show();
    }
}
