package com.pavigeeth.alzarcapartment.AdminActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pavigeeth.alzarcapartment.Adapter.SecurityAdapter;
import com.pavigeeth.alzarcapartment.Miscellaneous.UserData;
import com.pavigeeth.alzarcapartment.Model.SecurityDetail;
import com.pavigeeth.alzarcapartment.R;

import net.bohush.geometricprogressview.GeometricProgressView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdminSecurityActivity extends AppCompatActivity implements TextWatcher {

    private RecyclerView recyclerView;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef;
    private static String NODE = null;
    private List<SecurityDetail> meetingDetailList =  new ArrayList<>();
    private List<SecurityDetail> filteredmeetingDetailList =  new ArrayList<>();
    private GeometricProgressView progressView;
    private RelativeLayout emptyView;
    private EditText searchBar;
    private String userType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_security);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        searchBar = findViewById(R.id.editTextSearch);
        progressView = findViewById(R.id.progressView);
        emptyView = findViewById(R.id.empty_view);
        emptyView.setVisibility(View.INVISIBLE);
        showProgressView();
        userType = UserData.userType;

        NODE = getResources().getString(R.string.firebase_database_node_security_detail);
        myRef = database.getReference(NODE);

        //Inititalizing Recycler View
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(AdminSecurityActivity.this, LinearLayoutManager.VERTICAL, false));

        loadNotification();

        FloatingActionButton fab = findViewById(R.id.fab);

        if(UserData.userType.equals("user")){
            fab.setVisibility(View.GONE);
        }else{
            fab.setVisibility(View.VISIBLE);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminSecurityActivity.this, AdminAddSecurityDetailActivity.class));
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
                meetingDetailList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    meetingDetailList.add(snapshot.getValue(SecurityDetail.class));
                }
                if (!meetingDetailList.isEmpty()) {
                    emptyView.setVisibility(View.INVISIBLE);
                    Collections.reverse(meetingDetailList);
                    filteredmeetingDetailList = meetingDetailList;
                    recyclerView.setAdapter(new SecurityAdapter(meetingDetailList, AdminSecurityActivity.this,userType));
                    searchBar.addTextChangedListener(AdminSecurityActivity.this);
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

    private void searchInList(String searchName){

        List<SecurityDetail> memeberListToReturn =  new ArrayList<>();

        filteredmeetingDetailList = meetingDetailList;
        for(SecurityDetail memeber: filteredmeetingDetailList){
            if(memeber.getName().toLowerCase().contains(searchName)){
                memeberListToReturn.add(memeber);
            }
        }
        recyclerView.setAdapter(new SecurityAdapter(memeberListToReturn, AdminSecurityActivity.this,userType));
    }

    private void makeToast(String message) {
        Toast.makeText(AdminSecurityActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        searchInList(charSequence.toString().trim().toLowerCase());
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
