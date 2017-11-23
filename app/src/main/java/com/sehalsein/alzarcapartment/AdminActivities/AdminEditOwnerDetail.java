package com.sehalsein.alzarcapartment.AdminActivities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sehalsein.alzarcapartment.Activities.CreateNewUser;
import com.sehalsein.alzarcapartment.Miscellaneous.UserData;
import com.sehalsein.alzarcapartment.Model.FlatDetail;
import com.sehalsein.alzarcapartment.R;

public class AdminEditOwnerDetail extends AppCompatActivity {


    private EditText nameEditText;
    //private EditText phoneEditText;
    private EditText emailEditText;
    private EditText doorNoEditText;
    private EditText blockEditText;
    private EditText occupationEditText;
    private EditText familyMemberEditText;
    private FlatDetail flatDetail;


    private static final String TAG = "CreateNewUser";
    private static final int RC_SIGN_IN = 123;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef;
    private static String NODE = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_owner_detail);

        NODE = getResources().getString(R.string.firebase_database_node_apartment_detail);
        mAuth = FirebaseAuth.getInstance();
        myRef = database.getReference(NODE);


        nameEditText = findViewById(R.id.name_edit_text);
        //phoneEditText = findViewById(R.id.phone_number_edit_text);
        emailEditText = findViewById(R.id.emailId_editText);
        doorNoEditText = findViewById(R.id.door_no_edit_text);
        blockEditText = findViewById(R.id.block_edit_text);
        occupationEditText = findViewById(R.id.occupation_edit_text);
        familyMemberEditText = findViewById(R.id.family_member_edit_text);


        flatDetail = UserData.flatDetail;

        nameEditText.setText(flatDetail.getName());
        emailEditText.setText(flatDetail.getEmailId());
        doorNoEditText.setText(flatDetail.getDoorNo());
        blockEditText.setText(flatDetail.getBlockNo());
        occupationEditText.setText(flatDetail.getOccupation());
        familyMemberEditText.setText(flatDetail.getFamilyMembers());
    }

    private void makeToast(String message) {
        Toast.makeText(AdminEditOwnerDetail.this, message, Toast.LENGTH_SHORT).show();
    }

    public void registerApartment(View view) {
        if (validate()) {
            updateDB();
        } else {
            makeToast("Please fill in all the information");
        }
    }

    private void updateDB(){
        myRef.child(flatDetail.getId()).setValue(flatDetail);
        this.finish();
    }
    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    private boolean validate() {
        boolean nameCheck;
        //boolean phoneCheck;
        boolean emailCheck;
        boolean doorNoCheck;
        boolean blockCheck;
        boolean occupationCheck;
        boolean familyMemeberCheck;

        String name = "";
        //String phone = "";
        String email = "";
        String doorNo = "";
        String block = "";
        String occupation = "";
        String familyMember = "";

        if (isEmpty(nameEditText)) {
            nameEditText.setError("Enter name");
            nameCheck = false;
        } else {
            nameCheck = true;
            name = nameEditText.getText().toString();
        }

//        if (isEmpty(phoneEditText)) {
//            phoneEditText.setError("Enter phone no");
//            phoneCheck = false;
//        } else {
//            phoneCheck = true;
//            phone = phoneEditText.getText().toString();
//        }

        if (isEmpty(emailEditText)) {
            emailEditText.setError("Enter email id");
            emailCheck = false;
        } else {
            emailCheck = true;
            email = emailEditText.getText().toString();
        }

        if (isEmpty(doorNoEditText)) {
            doorNoEditText.setError("Enter door no");
            doorNoCheck = false;
        } else {
            doorNoCheck = true;
            doorNo = doorNoEditText.getText().toString();
        }

        if (isEmpty(blockEditText)) {
            blockEditText.setError("Enter block name");
            blockCheck = false;
        } else {
            blockCheck = true;
            block = blockEditText.getText().toString();
        }

        if (isEmpty(occupationEditText)) {
            occupationEditText.setError("Enter occupation");
            occupationCheck = false;
        } else {
            occupationCheck = true;
            occupation = occupationEditText.getText().toString();
        }

        if (isEmpty(familyMemberEditText)) {
            familyMemberEditText.setError("Enter no of family members");
            familyMemeberCheck = false;
        } else {
            familyMemeberCheck = true;
            familyMember = familyMemberEditText.getText().toString();
        }

        if (nameCheck && emailCheck && doorNoCheck && blockCheck && occupationCheck && familyMemeberCheck) {
            flatDetail.setName(name);
            //flatDetail.setPhoneNumber(phone);
            flatDetail.setEmailId(email);
            flatDetail.setDoorNo(doorNo);
            flatDetail.setBlockNo(block);
            flatDetail.setOccupation(occupation);
            flatDetail.setFamilyMembers(familyMember);
            return true;
        } else {
            return false;
        }
    }
}
