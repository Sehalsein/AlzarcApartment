package com.sehalsein.alzarcapartment.AdminActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sehalsein.alzarcapartment.Adapter.EventAdapter;
import com.sehalsein.alzarcapartment.Miscellaneous.UserData;
import com.sehalsein.alzarcapartment.Model.NotificationDetail;
import com.sehalsein.alzarcapartment.R;

import net.bohush.geometricprogressview.GeometricProgressView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdminEventActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef;
    private static String NODE = null;
    private List<NotificationDetail> notificationDetailList =  new ArrayList<>();
    private GeometricProgressView progressView;
    private RelativeLayout emptyView;
    private String userType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressView = findViewById(R.id.progressView);
        emptyView = findViewById(R.id.empty_view);
        emptyView.setVisibility(View.INVISIBLE);


        NODE = getResources().getString(R.string.firebase_database_node_event_detail);
        myRef = database.getReference(NODE);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(AdminEventActivity.this, LinearLayoutManager.VERTICAL, false));

        loadNotification();

        FloatingActionButton fab = findViewById(R.id.fab);
        userType = UserData.userType;
        if(UserData.userType.equals("user")){
            fab.setVisibility(View.GONE);
        }else{
            fab.setVisibility(View.VISIBLE);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminEventActivity.this, AdminAddNewEvent.class));

            }
        });
    }

    private void hideProgressView(){
        progressView.setVisibility(View.INVISIBLE);
    }
    private void showProgressView(){
        progressView.setVisibility(View.VISIBLE);
    }

    private void loadNotification(){

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                notificationDetailList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    notificationDetailList.add(snapshot.getValue(NotificationDetail.class));
                }
                if (!notificationDetailList.isEmpty()) {
                    emptyView.setVisibility(View.INVISIBLE);
                    Collections.reverse(notificationDetailList);
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView.setAdapter(new EventAdapter(notificationDetailList, AdminEventActivity.this,userType));

                } else {
                    emptyView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.INVISIBLE);
                }
                hideProgressView();
                //geometricProgressView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                hideProgressView();
                makeToast("Something went wrong while searching");
            }
        });
    }

    private void makeToast(String message) {
        Toast.makeText(AdminEventActivity.this, message, Toast.LENGTH_SHORT).show();
    }

}
