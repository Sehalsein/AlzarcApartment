package com.sehalsein.alzarcapartment.AdminFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sehalsein.alzarcapartment.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdminMoreOption extends Fragment {


    public AdminMoreOption() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_admin_more_option, container, false);


        return layout;
    }

}
