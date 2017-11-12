package com.sehalsein.alzarcapartment.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sehalsein.alzarcapartment.AdminActivities.AdminOwnerDetail;
import com.sehalsein.alzarcapartment.Miscellaneous.UserData;
import com.sehalsein.alzarcapartment.Model.FlatDetail;
import com.sehalsein.alzarcapartment.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sehalsein on 12/11/17.
 */

public class ApartmentListAdapter extends RecyclerView.Adapter<ApartmentListAdapter.ApartmentListViewHolder> {

    private Context context;
    private List<FlatDetail> flatDetailList = new ArrayList<>();
    private String userType;

    public ApartmentListAdapter() {
    }

    public ApartmentListAdapter(Context context, List<FlatDetail> flatDetailList, String userType) {
        this.context = context;
        this.flatDetailList = flatDetailList;
        this.userType = userType;
    }

    @Override
    public ApartmentListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_apartment_info, parent, false);
        ApartmentListViewHolder searchViewHolder = new ApartmentListViewHolder(view);
        return searchViewHolder;
    }


    @Override
    public void onBindViewHolder(ApartmentListViewHolder holder, int position) {
        final FlatDetail detail = flatDetailList.get(position);

        holder.flatNo.setText(detail.getBlockNo() + "" + detail.getDoorNo());
        holder.ownerName.setText(detail.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (userType) {
                    case "admin":
                        //makeToast("ADMIN");
                        openOwnerDetailAdmin(detail);
                        break;
                    case "user":
                        makeToast("User");
                        break;
                    default:
                        makeToast("Invalid User");
                }
            }
        });
    }


    private void openOwnerDetailAdmin(FlatDetail data) {
        UserData.flatDetail = data;
        context.startActivity(new Intent(context, AdminOwnerDetail.class));
    }


    private void makeToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return flatDetailList.size();
    }

    public ApartmentListAdapter(Context context, List<FlatDetail> flatDetailList) {
        this.context = context;
        this.flatDetailList = flatDetailList;
    }


    public class ApartmentListViewHolder extends RecyclerView.ViewHolder {

        private TextView flatNo, ownerName;
        private View itemView;


        public ApartmentListViewHolder(View itemView) {
            super(itemView);
            flatNo = (TextView) itemView.findViewById(R.id.flat_number_text_view);
            ownerName = (TextView) itemView.findViewById(R.id.owner_name_text_view);
            this.itemView = itemView;
        }
    }

}
