package com.pavigeeth.alzarcapartment.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pavigeeth.alzarcapartment.Adapter.AttendanceAdapter;
import com.pavigeeth.alzarcapartment.AdminActivities.AdminMeetingActivity;
import com.pavigeeth.alzarcapartment.Miscellaneous.UserData;
import com.pavigeeth.alzarcapartment.Model.BasicUserModal;
import com.pavigeeth.alzarcapartment.Model.BasicUserModal;
import com.pavigeeth.alzarcapartment.R;

import net.bohush.geometricprogressview.GeometricProgressView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AttendanceMeeting extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef;
    private static String NODE = null;
    private List<BasicUserModal> meetingDetailList =  new ArrayList<>();
    private GeometricProgressView progressView;
    private RelativeLayout emptyView;
    private EditText searchBar;
    private String userType;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_meeting);

        id = getIntent().getStringExtra("EXTRA_ID");
        progressView = findViewById(R.id.progressView);
        emptyView = findViewById(R.id.empty_view);
        emptyView.setVisibility(View.INVISIBLE);
        showProgressView();
        userType = UserData.userType;

        NODE = getResources().getString(R.string.firebase_database_node_event_meeting_available);
        myRef = database.getReference(NODE);

        //Inititalizing Recycler View
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(AttendanceMeeting.this, LinearLayoutManager.VERTICAL, false));

        loadNotification();
    }


    private void hideProgressView(){
        progressView.setVisibility(View.INVISIBLE);
    }


    private void showProgressView(){
        progressView.setVisibility(View.VISIBLE);
    }


    private void loadNotification(){

        myRef.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                meetingDetailList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    meetingDetailList.add(snapshot.getValue(BasicUserModal.class));
                }
                if (!meetingDetailList.isEmpty()) {
                    emptyView.setVisibility(View.INVISIBLE);
                    Collections.reverse(meetingDetailList);
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView.setAdapter(new AttendanceAdapter(meetingDetailList, AttendanceMeeting.this));
                } else {
                    emptyView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.INVISIBLE);
                }
                hideProgressView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                hideProgressView();
                makeToast("Something went wrong while searching");
            }
        });
    }

    private void makeToast(String message) {
        Toast.makeText(AttendanceMeeting.this, message, Toast.LENGTH_SHORT).show();
    }

}
