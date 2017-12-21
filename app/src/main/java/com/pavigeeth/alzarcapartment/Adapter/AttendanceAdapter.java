package com.pavigeeth.alzarcapartment.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pavigeeth.alzarcapartment.Model.BasicUserModal;
import com.pavigeeth.alzarcapartment.Model.MeetingDetail;
import com.pavigeeth.alzarcapartment.R;

import java.util.List;

/**
 * Created by sehalsein on 21/12/17.
 */

public class AttendanceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<BasicUserModal> meetingDetailList;
    private Context context;

    public AttendanceAdapter(List<BasicUserModal> meetingDetailList, Context context) {
        this.meetingDetailList = meetingDetailList;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_notification, parent, false);
        return new AttendanceAdapter.NotificationContentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final BasicUserModal notificationDetail = meetingDetailList.get(position);
        AttendanceAdapter.NotificationContentViewHolder viewHolder = (AttendanceAdapter.NotificationContentViewHolder) holder;
        viewHolder.setTitle(notificationDetail.getName());
    }

    @Override
    public int getItemCount() {
        return meetingDetailList.size();
    }

    public class NotificationContentViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView message;
        private TextView timeStamp;

        public NotificationContentViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            message = (TextView) itemView.findViewById(R.id.message);
            timeStamp = (TextView) itemView.findViewById(R.id.timeStamp);
            message.setVisibility(View.GONE);
            timeStamp.setVisibility(View.GONE);
        }

        public void setTitle(String title) {
            this.title.setText(title);
        }

        public void setMessage(String message) {
            this.message.setText(message);
        }

        public void setTimeStamp(String timeStamp) {
            this.timeStamp.setText(timeStamp);
        }
    }
}
