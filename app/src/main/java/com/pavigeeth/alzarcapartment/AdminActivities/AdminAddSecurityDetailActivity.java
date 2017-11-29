package com.pavigeeth.alzarcapartment.AdminActivities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pavigeeth.alzarcapartment.Miscellaneous.APIService;
import com.pavigeeth.alzarcapartment.Model.NotificationDetail;
import com.pavigeeth.alzarcapartment.Model.SecurityDetail;
import com.pavigeeth.alzarcapartment.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AdminAddSecurityDetailActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText phoneNumberEditText;
    private EditText addressEditText;
    private EditText timingEditText;
    private SecurityDetail securityDetail;
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
        setContentView(R.layout.activity_admin_add_security_detail);

        nameEditText = findViewById(R.id.name_edit_text);
        timingEditText = findViewById(R.id.time_edit_text);
        addressEditText = findViewById(R.id.address_edit_text);
        phoneNumberEditText = findViewById(R.id.phone_number_edit_text);

        NODE = getResources().getString(R.string.firebase_database_node_security_detail);
        myRef = database.getReference(NODE);


        NOTIFICATION_NODE = getResources().getString(R.string.firebase_database_node_notifications);
        myNotificationRef = database.getReference(NOTIFICATION_NODE);
    }


    public void sendNotification(View view) {
        //showProgressDialog();
        if (validate()) {
            sendNotification();
        } else {
            makeToast("Please fill in all the information.");
        }
    }

    private void sendNotification() {
        String key = myRef.child(NODE).push().getKey();
        securityDetail.setId(key);
        myRef.child(key).setValue(securityDetail);
        notificationDetail = new NotificationDetail();
        notificationDetail.setId(key);
        notificationDetail.setTimeStamp(getCurrentTimeStamp());
        notificationDetail.setTitle("Security");
        notificationDetail.setMessage(securityDetail.getName()+" "+securityDetail.getTiming());
        notificationDetail.setTopic("security");

        myNotificationRef.child(key).setValue(notificationDetail);
        this.finish();
    }

    private boolean validate() {
        String name = null;
        String address = null;
        String timing = null;
        String phoneNumber = null;

        if (isEmpty(nameEditText)) {
            nameEditText.setError("Enter name");
        } else {
            name = nameEditText.getText().toString();
        }

        if (isEmpty(addressEditText)) {
            addressEditText.setError("Enter address");
        } else {
            address = addressEditText.getText().toString();
        }

        if (isEmpty(timingEditText)) {
            timingEditText.setError("Enter timing");
        } else {
            timing = timingEditText.getText().toString();
        }

        if (isEmpty(phoneNumberEditText)) {
            phoneNumberEditText.setError("Enter phone number");
        } else {
            phoneNumber = phoneNumberEditText.getText().toString();
        }

        if (name != null && address != null && timing != null && phoneNumber != null) {
            securityDetail = new SecurityDetail();
            securityDetail.setName(name);
            securityDetail.setAddress(address);
            securityDetail.setTiming(timing);
            securityDetail.setPhoneNo(phoneNumber);
            return true;
        } else {
            return false;
        }
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

    private void makeToast(String message) {
        Toast.makeText(AdminAddSecurityDetailActivity.this, message, Toast.LENGTH_SHORT).show();
    }


    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }
}
