package com.sehalsein.alzarcapartment.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sehalsein.alzarcapartment.AdminActivities.AdminMeetingDetailActivity;
import com.sehalsein.alzarcapartment.Miscellaneous.UserData;
import com.sehalsein.alzarcapartment.Model.MeetingDetail;
import com.sehalsein.alzarcapartment.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by sehalsein on 12/11/17.
 */

public class MeetingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<MeetingDetail> meetingDetailList;
    private Context context;
    private String userType;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public MeetingAdapter() {
    }

    public MeetingAdapter(List<MeetingDetail> meetingDetailList, Context context) {
        this.meetingDetailList = meetingDetailList;
        this.context = context;
    }

    public MeetingAdapter(List<MeetingDetail> meetingDetailList, Context context, String userType) {
        this.meetingDetailList = meetingDetailList;
        this.context = context;
        this.userType = userType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_notification, parent, false);
        return new MeetingAdapter.NotificationContentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final MeetingDetail notificationDetail = meetingDetailList.get(position);
        MeetingAdapter.NotificationContentViewHolder notificationContentViewHolder = (MeetingAdapter.NotificationContentViewHolder) holder;
        notificationContentViewHolder.setTitle(notificationDetail.getTitle());
        try {
            notificationContentViewHolder.setTimeStamp(printDifference(simpleDateFormat.parse(notificationDetail.getTimeStamp()),simpleDateFormat.parse(getCurrentTimeStamp())));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (userType){
                    case "admin":
                        openMeetingDetail(notificationDetail);
                        break;
                    case "user":
                        break;
                    default:
                        makeToast("Invalid User");
                }
            }
        });
        notificationContentViewHolder.setMessage(notificationDetail.getMessage());
    }


    private void openMeetingDetail(MeetingDetail data){
        UserData.meetingDetail = data;
        Intent intent = new Intent(context, AdminMeetingDetailActivity.class);
        intent.putExtra("EXTRA_ID", data.getId());
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return meetingDetailList.size();
    }

    private static String getCurrentTimeStamp(){
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDateTime = dateFormat.format(new Date()); // Find todays date

            return currentDateTime;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    private void makeToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    private String printDifference(Date startDate, Date endDate) {

        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : " + endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        if (elapsedDays > 0) {
            return elapsedDays + " days ago";
        } else if (elapsedHours > 0) {
            return elapsedHours + "h ago";
        } else if (elapsedMinutes > 0) {
            return elapsedMinutes + "m ago";
        } else {
            return elapsedSeconds + "sec ago";
        }

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
