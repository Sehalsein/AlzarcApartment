package com.sehalsein.alzarcapartment.AdminFragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sehalsein.alzarcapartment.Adapter.NotificationAdapter;
import com.sehalsein.alzarcapartment.AdminActivities.AdminNewNotificationActivity;
import com.sehalsein.alzarcapartment.Model.NotificationDetail;
import com.sehalsein.alzarcapartment.R;

import net.bohush.geometricprogressview.GeometricProgressView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdminNotification extends Fragment {


    private RecyclerView recyclerView;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef;
    private static String NODE = null;
    private List<NotificationDetail> notificationDetailList =  new ArrayList<>();
    private GeometricProgressView progressView;
    private RelativeLayout emptyView;


    public AdminNotification() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_admin_notification, container, false);

        progressView = layout.findViewById(R.id.progressView);
        emptyView = layout.findViewById(R.id.empty_view);
        emptyView.setVisibility(View.INVISIBLE);
        showProgressView();

        NODE = getResources().getString(R.string.firebase_database_node_notifications);
        myRef = database.getReference(NODE);

        //Inititalizing Recycler View
        recyclerView = layout.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        loadNotification();

        FloatingActionButton fab = layout.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AdminNewNotificationActivity.class));
            }
        });


        return layout;
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
                    recyclerView.setAdapter(new NotificationAdapter(notificationDetailList, getActivity()));

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
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

}