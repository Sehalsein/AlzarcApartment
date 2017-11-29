package com.pavigeeth.alzarcapartment.AdminActivities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pavigeeth.alzarcapartment.Miscellaneous.APIService;
import com.pavigeeth.alzarcapartment.Miscellaneous.ApiUtils;
import com.pavigeeth.alzarcapartment.Miscellaneous.UserData;
import com.pavigeeth.alzarcapartment.Model.MeetingDetail;
import com.pavigeeth.alzarcapartment.Model.NotificationDetail;
import com.pavigeeth.alzarcapartment.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminMeetingDetailActivity extends AppCompatActivity {

    private EditText messageEditText;
    private EditText topicEditText;
    private EditText resultEditText;
    private AutoCompleteTextView titleEditText;
    private MeetingDetail meetingDetail;
    private APIService mAPIService;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef;
    private static String NODE = null;
    private String id;
    private NotificationDetail notificationDetail;
    private static String NOTIFICATION_NODE = null;
    private DatabaseReference myNotificationRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_meeting_detail);

        titleEditText = findViewById(R.id.title_edit_text);
        messageEditText = findViewById(R.id.message_edit_text);
        topicEditText = findViewById(R.id.topic_edit_text);
        resultEditText = findViewById(R.id.result_edit_text);

        id = getIntent().getStringExtra("EXTRA_ID");

        mAPIService = ApiUtils.getAPIService();
        NODE = getResources().getString(R.string.firebase_database_node_meeting_detail);
        myRef = database.getReference(NODE);


        NOTIFICATION_NODE = getResources().getString(R.string.firebase_database_node_notifications);
        myNotificationRef = database.getReference(NOTIFICATION_NODE);

        if (id != null) {
            meetingDetail = UserData.meetingDetail;
            updateUI();
        }
    }

    private void updateUI() {
        titleEditText.setText(meetingDetail.getTitle());
        messageEditText.setText(meetingDetail.getMessage());
        topicEditText.setText(meetingDetail.getTopic());

        if (meetingDetail.getResults() != null) {
            resultEditText.setText(meetingDetail.getResults());
        }
    }

    public void sendNotification(View view) {
        //showProgressDialog();
        if (validate()) {
            if (id != null) {
                updateNotification();
            } else {
                sendNotification();
            }
        } else {
            makeToast("Please fill in all the information.");
        }
    }

    private void updateNotification() {

        if (!isEmpty(resultEditText)) {
            meetingDetail.setResults(resultEditText.getText().toString());
        }

        String key = id;
        meetingDetail.setId(key);
        myRef.child(key).setValue(meetingDetail);
        this.finish();
    }


    public void showResponse(String response) {
        makeToast(response);
    }

    public void sendPost(String title, String body) {
        final String topic = "notifications";
        mAPIService.savePost(title, body, topic).enqueue(new Callback<NotificationDetail>() {
            @Override
            public void onResponse(Call<NotificationDetail> call, Response<NotificationDetail> response) {

                if (response.isSuccessful()) {
                    //hideProgressDialog();
                    showResponse(response.body().toString());
                    sendNotification();
                    //makeToast("Notification Sent to ." + topic);
                    //Toast.makeText(AddNotification.this, "Notification Sent to ." + topic, Toast.LENGTH_SHORT).show();
                    Log.i("NOTIFICATION", "post submitted to API." + response.body().toString());

                } else {
                    makeToast(response.message());
                }
            }

            @Override
            public void onFailure(Call<NotificationDetail> call, Throwable t) {
                // hideProgressDialog();
                makeToast("Unable send notification!!");
                //Toast.makeText(AddNotification.this, "Unable send notification!!.", Toast.LENGTH_SHORT).show();
                Log.e("NOTIFICATION", "Unable to submit post to API.");
            }
        });
    }

    private void sendNotification() {
        String key = myRef.child(NODE).push().getKey();
        meetingDetail.setId(key);
        myRef.child(key).setValue(meetingDetail);

        notificationDetail = new NotificationDetail();
        notificationDetail.setId(key);
        notificationDetail.setTimeStamp(getCurrentTimeStamp());
        notificationDetail.setTitle("Meeting");
        notificationDetail.setMessage(meetingDetail.getTitle()+" "+meetingDetail.getMessage());
        notificationDetail.setTopic("meeting");

        myNotificationRef.child(key).setValue(notificationDetail);


        this.finish();
    }

    private boolean validate() {
        String title = null;
        String message = null;
        String topic = null;

        if (isEmpty(titleEditText)) {
            titleEditText.setError("Enter title");
        } else {
            title = titleEditText.getText().toString();
        }

        if (isEmpty(messageEditText)) {
            messageEditText.setError("Enter message");
        } else {
            message = messageEditText.getText().toString();
        }

        if (isEmpty(topicEditText)) {
            topicEditText.setError("Enter topic");
        } else {
            topic = topicEditText.getText().toString();
        }

        if (title != null && message != null) {
            meetingDetail = new MeetingDetail(title, message, getCurrentTimeStamp());
            meetingDetail.setTopic(topic);
            return true;
        } else {
            return false;
        }
    }

    private void makeToast(String message) {
        Toast.makeText(AdminMeetingDetailActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    public static String getCurrentTimeStamp() {
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDateTime = dateFormat.format(new Date()); // Find todays date

            return currentDateTime;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }
}
