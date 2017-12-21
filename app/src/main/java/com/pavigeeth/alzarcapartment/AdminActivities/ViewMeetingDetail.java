package com.pavigeeth.alzarcapartment.AdminActivities;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pavigeeth.alzarcapartment.Miscellaneous.UserData;
import com.pavigeeth.alzarcapartment.Model.BasicUserModal;
import com.pavigeeth.alzarcapartment.Model.MeetingDetail;
import com.pavigeeth.alzarcapartment.R;

public class ViewMeetingDetail extends AppCompatActivity {

    private String id;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef;
    private DatabaseReference mRef;
    private static String NODE = null;
    private static String MNODE = null;
    private TextView titleTextView;
    private TextView dateTextView;
    private TextView messageTextView;
    private TextView resultTextView;
    private LinearLayout meetingAvailable;
    private Button yesButton;
    private Button noButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_meeting_detail);

        id = getIntent().getStringExtra("EXTRA_ID");
        //makeToast(id);


        NODE = getResources().getString(R.string.firebase_database_node_meeting_detail);
        MNODE = getResources().getString(R.string.firebase_database_node_event_meeting_available);
        myRef = database.getReference(NODE).child(id);
        mRef = database.getReference(MNODE);

        yesButton = findViewById(R.id.yes_button);
        noButton = findViewById(R.id.no_button);
        meetingAvailable = findViewById(R.id.meeting_layout);

        titleTextView = findViewById(R.id.title_text_view);
        dateTextView = findViewById(R.id.date_text_view);
        messageTextView = findViewById(R.id.message_text_view);
        resultTextView = findViewById(R.id.result_number_text_view);

        loadMeeting();


        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectYesButton();
            }
        });


        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectNoButton();
            }
        });

    }

    private void selectYesButton(){
        yesButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        noButton.setBackgroundColor(getResources().getColor(R.color.grey_font));
        makeToast("yes");

        BasicUserModal userModal = new BasicUserModal(UserData.ownerflatDetail.getId(),UserData.ownerflatDetail.getName());
        mRef.child(id).child(UserData.ownerflatDetail.getId()).setValue(userModal);

    }


    private void selectNoButton(){
        noButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        yesButton.setBackgroundColor(getResources().getColor(R.color.grey_font));
        makeToast("No");

        mRef.child(id).child(UserData.ownerflatDetail.getId()).setValue(null);
    }

    private void loadMeeting(){

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MeetingDetail data = dataSnapshot.getValue(MeetingDetail.class);
                titleTextView.setText(data.getTitle());
                dateTextView.setText(data.getTimeStamp());
                messageTextView.setText(data.getMessage());
                resultTextView.setText(data.getResults());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void makeToast(String message) {
        Toast.makeText(ViewMeetingDetail.this, message, Toast.LENGTH_SHORT).show();
    }
}
