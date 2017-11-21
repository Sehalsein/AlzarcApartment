package com.sehalsein.alzarcapartment.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.sehalsein.alzarcapartment.Activities.LoginActivity;
import com.sehalsein.alzarcapartment.AdminActivities.AdminMeetingActivity;
import com.sehalsein.alzarcapartment.AdminActivities.AdminSecurityActivity;
import com.sehalsein.alzarcapartment.Model.MoreOption;
import com.sehalsein.alzarcapartment.R;

import java.util.List;

/**
 * Created by sehalsein on 12/11/17.
 */

public class MoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MoreOption> moreOptionList;
    private Context context;

    public MoreAdapter() {
    }

    public MoreAdapter(List<MoreOption> moreOptionList, Context context) {
        this.moreOptionList = moreOptionList;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_more_option, parent, false);
        return new MoreOptionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final MoreOption item = moreOptionList.get(position);
        MoreOptionViewHolder moreOptionViewHolder = (MoreOptionViewHolder) holder;

        moreOptionViewHolder.setRow(item);
        moreOptionViewHolder.row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (item.getCode()) {
                    case "adminMeeting":
                        openMeetingAdmin();
                        break;
                    case "adminSecurity":
                        openSecurityAdmin();
                        break;
                    case "Logout":
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(context.getApplicationContext(), LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        break;
                    default:
                        makeToast("Coming Soon");
                }
            }
        });
    }

    private void openSecurityAdmin() {
        context.startActivity(new Intent(context, AdminSecurityActivity.class));
    }

    private void openMeetingAdmin() {
        context.startActivity(new Intent(context, AdminMeetingActivity.class));
    }

    @Override
    public int getItemCount() {
        return moreOptionList.size();
    }

    private void makeToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }


    public class MoreOptionViewHolder extends RecyclerView.ViewHolder {

        private TextView moreOptionTitle;
        private TextView moreOptionDescription;
        private ImageView moreOptionIcon;
        private View row;

        public MoreOptionViewHolder(View itemView) {
            super(itemView);
            moreOptionTitle = itemView.findViewById(R.id.more_option_title);
            moreOptionDescription = itemView.findViewById(R.id.more_option_description);
            moreOptionIcon = itemView.findViewById(R.id.more_option_icon_image_view);
            this.row = itemView;
        }

        public void setRow(MoreOption data) {
            this.moreOptionTitle.setText(data.getTitle());
            if (data.getDescription().equals("")) {
                this.moreOptionDescription.setVisibility(View.GONE);
            } else {
                this.moreOptionDescription.setVisibility(View.VISIBLE);
                this.moreOptionDescription.setText(data.getDescription());
            }
            this.moreOptionIcon.setImageResource(data.getImage());

        }
    }
}
