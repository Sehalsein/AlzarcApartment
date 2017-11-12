package com.sehalsein.alzarcapartment.AdminActivities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sehalsein.alzarcapartment.Miscellaneous.APIService;
import com.sehalsein.alzarcapartment.Miscellaneous.ApiUtils;
import com.sehalsein.alzarcapartment.Miscellaneous.UserData;
import com.sehalsein.alzarcapartment.Model.MeetingDetail;
import com.sehalsein.alzarcapartment.Model.NotificationDetail;
import com.sehalsein.alzarcapartment.R;

import net.bohush.geometricprogressview.GeometricProgressView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminMeetingDetailActivity extends AppCompatActivity {

    private EditText messageEditText;
    private EditText topicEditText;
    private EditText resultEditText;
    private AutoCompleteTextView titleEditText;
    private MeetingDetail notificationDetail;
    private APIService mAPIService;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef;
    private static String NODE = null;
    private String id;

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

        if (id != null) {
            notificationDetail = UserData.meetingDetail;
            updateUI();
        }
    }

    private void updateUI() {
        titleEditText.setText(notificationDetail.getTitle());
        messageEditText.setText(notificationDetail.getMessage());
        topicEditText.setText(notificationDetail.getTopic());
        
        if (notificationDetail.getResults() != null) {
            resultEditText.setText(notificationDetail.getResults());
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
            notificationDetail.setResults(resultEditText.getText().toString());
        }

        String key = id;
        myRef.child(key).setValue(notificationDetail);
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
        notificationDetail.setId(key);
        myRef.child(key).setValue(notificationDetail);
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
            notificationDetail = new MeetingDetail(title, message, getCurrentTimeStamp());
            notificationDetail.setTopic(topic);
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
