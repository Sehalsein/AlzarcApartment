package com.pavigeeth.alzarcapartment.AdminFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pavigeeth.alzarcapartment.Adapter.MoreAdapter;
import com.pavigeeth.alzarcapartment.Model.MoreOption;
import com.pavigeeth.alzarcapartment.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdminMoreOption extends Fragment {


    private RecyclerView recyclerView;

    public AdminMoreOption() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_admin_more_option, container, false);

        recyclerView = layout.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new MoreAdapter(createList(), getActivity()));


        return layout;
    }

    private List<MoreOption> createList() {
        List<MoreOption> result = null;
        try {

            String title[] = {
                    "Meetings",
                    "Security",
                    "Event",
                    "Pay Maintaince",
                    "Logout",
            };

            String description[] = {
                    "View previous meetings",
                    "View security detail",
                    "View event detail",
                    "",
                    "",
            };

            String code[] = {
                    "adminMeeting",
                    "adminSecurity",
                    "adminEvent",
                    "pay",
                    "Logout",
            };

            int icons[] = {
                    R.drawable.ic_people_outline_black_24dp,
                    R.drawable.ic_security_black_24dp,
                    R.drawable.ic_event_black_24dp,
                    R.drawable.ic_credit_card_black_24dp,
                    R.drawable.ic_exit_to_app_black_24dp,
            };

            result = new ArrayList<MoreOption>();
            for (int i = 0; i < title.length ; i++) {
                MoreOption moreOption = new MoreOption(title[i],description[i%description.length],icons[i%icons.length]);
                moreOption.setCode(code[i]);
                result.add(moreOption);
            }
        } catch (Exception e) {
            System.out.print(e);
        }
        return result;
    }

}
