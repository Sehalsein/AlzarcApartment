package com.sehalsein.alzarcapartment.AdminActivities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.sehalsein.alzarcapartment.Miscellaneous.UserData;
import com.sehalsein.alzarcapartment.Model.FlatDetail;
import com.sehalsein.alzarcapartment.R;

public class AdminOwnerDetail extends AppCompatActivity {


    private TextView flatnumberTextView;
    private TextView nameTextView;
    private TextView occupationTextView;
    private TextView emailIdTextView;
    private TextView phoneNumberTextView;
    private TextView familyMemebersTextView;
    private TextView statusTextView;

    private FlatDetail flatDetail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_owner_detail);


        flatDetail = UserData.flatDetail;

        flatnumberTextView = findViewById(R.id.apartment_number_text_view);
        nameTextView = findViewById(R.id.name_text_view);
        occupationTextView = findViewById(R.id.occupation_text_view);
        emailIdTextView = findViewById(R.id.email_id_text_view);
        phoneNumberTextView = findViewById(R.id.phone_number_text_view);
        familyMemebersTextView = findViewById(R.id.family_members_text_view);
        statusTextView = findViewById(R.id.status_text_view);

        flatnumberTextView.setText(flatDetail.getBlockNo()+""+flatDetail.getDoorNo());
        nameTextView.setText(flatDetail.getName());
        occupationTextView.setText(flatDetail.getOccupation());
        emailIdTextView.setText(flatDetail.getEmailId());
        phoneNumberTextView.setText(flatDetail.getPhoneNumber());
        familyMemebersTextView.setText(flatDetail.getFamilyMembers());

    }

    private void makeToast(String message) {
        Toast.makeText(AdminOwnerDetail.this, message, Toast.LENGTH_SHORT).show();
    }
}
