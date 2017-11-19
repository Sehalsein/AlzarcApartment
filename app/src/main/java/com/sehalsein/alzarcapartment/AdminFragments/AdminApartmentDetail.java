package com.sehalsein.alzarcapartment.AdminFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sehalsein.alzarcapartment.Adapter.ApartmentListAdapter;
import com.sehalsein.alzarcapartment.Miscellaneous.UserData;
import com.sehalsein.alzarcapartment.Model.FlatDetail;
import com.sehalsein.alzarcapartment.R;

import net.bohush.geometricprogressview.GeometricProgressView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdminApartmentDetail extends Fragment implements TextWatcher{

    private RecyclerView recyclerView;
    private String TAG = getClass().getName();

    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRef ;
    private String NODE = null;
    private EditText searchBar;
    private GeometricProgressView progressView;
    private List<FlatDetail> flatDetailList =  new ArrayList<>();
    private List<FlatDetail> filteredflatDetailList =  new ArrayList<>();

    public AdminApartmentDetail() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_admin_apartment_detail, container, false);


        searchBar = layout.findViewById(R.id.editTextSearch);
        NODE = getResources().getString(R.string.firebase_database_node_apartment_detail);
        mRef = mDatabase.getReference(NODE);
        progressView = layout.findViewById(R.id.progressView);

        recyclerView = layout.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        showProgressView();
        loadList();

        return layout;
    }

    private void makeToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
    private void hideProgressView(){
        progressView.setVisibility(View.INVISIBLE);
    }
    private void showProgressView(){
        progressView.setVisibility(View.VISIBLE);
    }

    private void loadList(){

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                flatDetailList.clear();
                hideProgressView();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    flatDetailList.add(snapshot.getValue(FlatDetail.class));
                }

                filteredflatDetailList = flatDetailList;
                recyclerView.setAdapter(new ApartmentListAdapter(getActivity(), flatDetailList,"admin"));
                //geometricProgressView.setVisibility(View.INVISIBLE);
                searchBar.addTextChangedListener(AdminApartmentDetail.this);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                //   Toast.makeText(this, "Something went wrong while searching", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void searchInList(String searchName){

        List<FlatDetail> memeberListToReturn =  new ArrayList<>();

        filteredflatDetailList = flatDetailList;
        for(FlatDetail memeber: filteredflatDetailList){
            if(memeber.getDoorNo().toLowerCase().contains(searchName)){
                memeberListToReturn.add(memeber);
            }
        }
        recyclerView.setAdapter(new ApartmentListAdapter(getActivity(), memeberListToReturn,"admin"));
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
