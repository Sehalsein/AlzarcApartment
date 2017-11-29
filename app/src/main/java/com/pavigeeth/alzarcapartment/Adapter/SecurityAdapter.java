package com.pavigeeth.alzarcapartment.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rubensousa.bottomsheetbuilder.BottomSheetBuilder;
import com.github.rubensousa.bottomsheetbuilder.BottomSheetMenuDialog;
import com.github.rubensousa.bottomsheetbuilder.adapter.BottomSheetItemClickListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pavigeeth.alzarcapartment.Model.SecurityDetail;
import com.pavigeeth.alzarcapartment.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sehalsein on 21/11/17.
 */

public class SecurityAdapter extends RecyclerView.Adapter<SecurityAdapter.ApartmentListViewHolder> {

    private List<SecurityDetail> flatDetailList = new ArrayList<>();
    private Context context;
    private String userType;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef;
    private static String NODE = null;


    public SecurityAdapter() {
    }

    public SecurityAdapter(List<SecurityDetail> flatDetailList, Context context, String userType) {
        this.flatDetailList = flatDetailList;
        this.context = context;
        this.userType = userType;
        NODE = context.getResources().getString(R.string.firebase_database_node_security_detail);
        myRef = database.getReference(NODE);
    }

    @Override
    public ApartmentListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_security_detail, parent, false);
        ApartmentListViewHolder searchViewHolder = new ApartmentListViewHolder(view);
        return searchViewHolder;
    }

    @Override
    public void onBindViewHolder(ApartmentListViewHolder holder, int position) {
        final SecurityDetail detail = flatDetailList.get(position);

        holder.flatNo.setText(detail.getName());
        holder.ownerName.setText(detail.getTiming());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (userType) {
                    case "admin":
                        // makeToast("ADMIN");
                        BottomSheetMenuDialog dialog = new BottomSheetBuilder(context, R.style.AppTheme_BottomSheetDialog)
                                .expandOnStart(true)
                                .setMode(BottomSheetBuilder.MODE_LIST)
                                .setMenu(R.menu.bottom_menu)
                                .setItemClickListener(new BottomSheetItemClickListener() {
                                    @Override
                                    public void onBottomSheetItemClick(MenuItem item) {
                                        switch (item.getItemId()) {
                                            case R.id.delete:
                                                myRef.child(detail.getId()).setValue(null);
                                                break;
                                        }
                                    }
                                })
                                .createDialog();
                        dialog.show();
                        //openOwnerDetailAdmin(detail);
                        break;
                    case "user":
                        // makeToast("User");
                        break;
                    default:
                        //makeToast("Invalid User");
                }
            }
        });
    }

    private void makeToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return flatDetailList.size();
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
