package com.sehalsein.alzarcapartment.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rubensousa.bottomsheetbuilder.BottomSheetBuilder;
import com.github.rubensousa.bottomsheetbuilder.BottomSheetMenuDialog;
import com.github.rubensousa.bottomsheetbuilder.adapter.BottomSheetItemClickListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef;
    private static String NODE = null;

    public ApartmentListAdapter() {
    }

    public ApartmentListAdapter(Context context, List<FlatDetail> flatDetailList, String userType) {
        this.context = context;
        this.flatDetailList = flatDetailList;
        this.userType = userType;
        NODE = context.getResources().getString(R.string.firebase_database_node_apartment_detail);
        myRef = database.getReference(NODE);
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

        if(detail.getId().equals(UserData.ownerflatDetail.getId())){
            holder.imageView.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_home_gold_24dp));
        }else{
            holder.imageView.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_home_black_24dp));
        }


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


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                switch (userType){
                    case "admin":
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
                    case "user":
                        makeToast("User");
                        break;
                    default:
                        makeToast("Invalid User");
                    
                }

                return true;
            }
        });
    }


    private void openOwnerDetailAdmin(FlatDetail data) {
        if(data.getId().equals(UserData.ownerflatDetail.getId())){
            UserData.isOwner = true;
        }else{
            UserData.isOwner = false;
        }
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
        private ImageView imageView;

        public ApartmentListViewHolder(View itemView) {
            super(itemView);
            flatNo = (TextView) itemView.findViewById(R.id.flat_number_text_view);
            ownerName = (TextView) itemView.findViewById(R.id.owner_name_text_view);
            imageView = itemView.findViewById(R.id.type_image_view);
            this.itemView = itemView;
        }
    }

}
