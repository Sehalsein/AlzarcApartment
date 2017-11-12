package com.sehalsein.alzarcapartment.Activities;

import android.content.Intent;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sehalsein.alzarcapartment.AdminActivities.AdminHomeActivity;
import com.sehalsein.alzarcapartment.Model.FlatDetail;
import com.sehalsein.alzarcapartment.R;

import java.util.Arrays;

public class CreateNewUser extends AppCompatActivity {


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
        setContentView(R.layout.activity_create_new_user);


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


    }


    private void makeToast(String message) {
        Toast.makeText(CreateNewUser.this, message, Toast.LENGTH_SHORT).show();
    }

    private void saveUserInfo() {

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            flatDetail.setPhoneNumber(user.getPhoneNumber());
            flatDetail.setId(user.getUid());
        } else {
            makeToast("No User found");
        }

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(flatDetail.getName())
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            myRef.child(user.getUid()).setValue(flatDetail);
                            Log.d(TAG, "User profile updated.");
                        }
                    }
                });


    }

    public void phoneVerification() {
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(Arrays.asList(
                        new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build()
                )).build(),
                RC_SIGN_IN
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            // Successfully signed in
            if (resultCode == ResultCodes.OK) {
                this.makeToast("Successfully signed in");
                saveUserInfo();
                makeToast("OPEN");
                //startActivity(new Intent(RegisterActivity.this,TabBarActivity.class));
                this.finish();
                return;
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    showSnackbar(R.string.sign_in_cancelled);
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showSnackbar(R.string.no_internet_connection);
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    showSnackbar(R.string.unknown_error);
                    return;
                }
            }
            showSnackbar(R.string.unknown_sign_in_response);
        }
    }

    @MainThread
    private void showSnackbar(@StringRes int errorMessageRes) {
        Snackbar.make(familyMemberEditText, errorMessageRes, Snackbar.LENGTH_LONG).show();
    }

    public void registerApartment(View view) {
        if (validate()) {
            if (mAuth.getCurrentUser() == null) {
                phoneVerification();
            }
        } else {
            makeToast("Please fill in all the information");
        }
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

        if (nameCheck  && emailCheck && doorNoCheck && blockCheck && occupationCheck && familyMemeberCheck) {
            flatDetail = new FlatDetail();
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
