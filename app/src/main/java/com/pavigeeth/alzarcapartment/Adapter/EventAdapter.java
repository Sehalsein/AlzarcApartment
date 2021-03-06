package com.pavigeeth.alzarcapartment.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.rubensousa.bottomsheetbuilder.BottomSheetBuilder;
import com.github.rubensousa.bottomsheetbuilder.BottomSheetMenuDialog;
import com.github.rubensousa.bottomsheetbuilder.adapter.BottomSheetItemClickListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pavigeeth.alzarcapartment.Activities.GalleryViewActivity;
import com.pavigeeth.alzarcapartment.Model.NotificationDetail;
import com.pavigeeth.alzarcapartment.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by sehalsein on 22/11/17.
 */

public class EventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<NotificationDetail> notificationDetailList;
    private Context context;
    private String userType;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef;
    private static String NODE = null;


    public EventAdapter() {
    }

    public EventAdapter(List<NotificationDetail> notificationDetailList, Context context,String userType) {
        this.notificationDetailList = notificationDetailList;
        this.context = context;
        this.userType = userType;
        NODE = context.getResources().getString(R.string.firebase_database_node_event_detail);
        myRef = database.getReference(NODE);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_notification, parent, false);
        return new NotificationContentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final NotificationDetail notificationDetail = notificationDetailList.get(position);
        NotificationContentViewHolder notificationContentViewHolder = (NotificationContentViewHolder) holder;
        notificationContentViewHolder.setTitle(notificationDetail.getTitle());
        try {
            notificationContentViewHolder.setTimeStamp(printDifference(simpleDateFormat.parse(notificationDetail.getTimeStamp()),simpleDateFormat.parse(getCurrentTimeStamp())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        notificationContentViewHolder.setMessage(notificationDetail.getMessage());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (userType) {
                    case "admin":
                        // makeToast("ADMIN");
                        BottomSheetMenuDialog dialog = new BottomSheetBuilder(context, R.style.AppTheme_BottomSheetDialog)
                                .expandOnStart(true)
                                .setMode(BottomSheetBuilder.MODE_LIST)
                                .setMenu(R.menu.event_menu)
                                .setItemClickListener(new BottomSheetItemClickListener() {
                                    @Override
                                    public void onBottomSheetItemClick(MenuItem item) {
                                        switch (item.getItemId()) {
                                            case R.id.delete:
                                                myRef.child(notificationDetail.getId()).setValue(null);
                                                break;
                                            case R.id.images:
                                                viewGallery(notificationDetail);
                                                break;
                                        }
                                    }
                                })
                                .createDialog();
                        dialog.show();
                        //openOwnerDetailAdmin(detail);
                        break;
                    case "user":
                        BottomSheetMenuDialog dialog1 = new BottomSheetBuilder(context, R.style.AppTheme_BottomSheetDialog)
                                .expandOnStart(true)
                                .setMode(BottomSheetBuilder.MODE_LIST)
                                .setMenu(R.menu.event_menu_user)
                                .setItemClickListener(new BottomSheetItemClickListener() {
                                    @Override
                                    public void onBottomSheetItemClick(MenuItem item) {
                                        switch (item.getItemId()) {
                                            case R.id.images:
                                                viewGallery(notificationDetail);
                                                break;
                                        }
                                    }
                                })
                                .createDialog();
                        dialog1.show();
                        // makeToast("User");
                        break;
                    default:
                        //makeToast("Invalid User");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationDetailList.size();
    }


    private void viewGallery(NotificationDetail notificationDetail){
        Intent intent = new Intent(context, GalleryViewActivity.class);
        intent.putExtra("EVENTID",notificationDetail.getId()+"");
        context.startActivity(intent);
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
