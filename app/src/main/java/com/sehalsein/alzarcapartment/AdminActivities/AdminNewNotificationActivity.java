package com.sehalsein.alzarcapartment.AdminActivities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sehalsein.alzarcapartment.Activities.CreateNewUser;
import com.sehalsein.alzarcapartment.Miscellaneous.APIService;
import com.sehalsein.alzarcapartment.Miscellaneous.ApiUtils;
import com.sehalsein.alzarcapartment.Model.NotificationDetail;
import com.sehalsein.alzarcapartment.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminNewNotificationActivity extends AppCompatActivity {

    private EditText messageEditText;
    private AutoCompleteTextView titleEditText;
    private NotificationDetail notificationDetail;
    private APIService mAPIService;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef;
    private static String NODE = null;


    private ArrayAdapter<String> getTitleAdapter(Context context) {
        String[] addresses = {"Sports day","Annual day","Flag hosting","Festival"};
        return new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, addresses);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_notification);

        titleEditText = findViewById(R.id.title_edit_text);
        titleEditText.setAdapter(getTitleAdapter(this));
        messageEditText = findViewById(R.id.message_edit_text);


        mAPIService = ApiUtils.getAPIService();
        NODE = getResources().getString(R.string.firebase_database_node_notifications);
        myRef = database.getReference(NODE);
    }


    public void sendNotification(View view){
        //showProgressDialog();
        if (validate()) {
            sendNotification();
            //sendPost(notificationDetail.getTitle(), notificationDetail.getMessage());
        } else {
            makeToast("Please fill in all the information.");
        }
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

                }else{
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

    private void sendNotification(){
        String key = myRef.child(NODE).push().getKey();
        notificationDetail.setId(key);
        myRef.child(key).setValue(notificationDetail);
        this.finish();
    }
    private boolean validate() {
        String title = null;
        String message = null;

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

        if (title != null && message != null) {
            notificationDetail = new NotificationDetail(title, message, getCurrentTimeStamp());
            return true;
        } else {
            return false;
        }
    }

    private void makeToast(String message) {
        Toast.makeText(AdminNewNotificationActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    public static String getCurrentTimeStamp(){
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
