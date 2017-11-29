package com.pavigeeth.alzarcapartment.Activities;

import android.content.Intent;
import android.support.annotation.MainThread;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.pavigeeth.alzarcapartment.AdminActivities.AdminHomeActivity;
import com.pavigeeth.alzarcapartment.Miscellaneous.UserData;
import com.pavigeeth.alzarcapartment.Model.FlatDetail;
import com.pavigeeth.alzarcapartment.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private TextView createAccount;
    private EditText phoneNumberEditText;


    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 123;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private List<FlatDetail> flatDetailList = new ArrayList<>();
    private DatabaseReference myRef;
    private static String NODE = null;
    private static String phoneNumber;
    private FlatDetail flatDetail;
   // private String adminNumber ="8722564630";
    private String adminNumber ="7019823512";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        NODE = getResources().getString(R.string.firebase_database_node_apartment_detail);
        mAuth = FirebaseAuth.getInstance();
        myRef = database.getReference(NODE);

        phoneNumberEditText = findViewById(R.id.emailId_editText);

        phoneNumberEditText.setText(adminNumber);
        createAccount = findViewById(R.id.create_account);
        FirebaseInstanceId.getInstance().getToken();
        FirebaseMessaging.getInstance().subscribeToTopic("notifications");

        getAllUser();
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCreateNewUserPage();
            }
        });


    }


    public void login(View view) {

        if (isEmpty(phoneNumberEditText)) {
            phoneNumberEditText.setError("Please enter your phone number!!");
        } else {
            phoneNumber = phoneNumberEditText.getText().toString();
            //makeToast("VALID");
            final MaterialDialog dialog = new MaterialDialog.Builder(LoginActivity.this)
                    .title("Authenticating")
                    .content("Please wait")
                    .progress(true, 0)
                    .cancelable(false)
                    .progressIndeterminateStyle(true)
                    .show();

            if(checkIfUserExist()){
                validatePhone();
                dialog.dismiss();
            }else{
                dialog.dismiss();
                new MaterialDialog.Builder(LoginActivity.this)
                        .title("Unsuccessful")
                        .content("Invalid number.")
                        .positiveText("Dismiss")
                        .show();
            }
        }
    }

    private void validatePhone() {


        if (phoneNumber.length() == 10) {
            if (phoneNumber.equals(adminNumber)) {
                UserData.userType = "admin";
            } else {
                UserData.userType = "user";
            }
            openAdminPage();
            //phoneVerification();
        } else {
            phoneNumberEditText.setError("Please enter a valid phone number!!");
        }


    }

    public void phoneVerification() {
        Bundle params = new Bundle();
        params.putString(AuthUI.EXTRA_DEFAULT_COUNTRY_CODE, "in");
        params.putString(AuthUI.EXTRA_DEFAULT_NATIONAL_NUMBER, phoneNumber);
        AuthUI.IdpConfig phoneConfigWithDefaultNumber =
                new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER)
                        .setParams(params)
                        .build();
//        startActivityForResult(
//                AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(Arrays.asList(
//                        new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build()
//                )).build(),
//                RC_SIGN_IN
//        );
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(Arrays.asList(
                        phoneConfigWithDefaultNumber
                )).build(),
                RC_SIGN_IN
        );
    }

    @MainThread
    private void showSnackbar(@StringRes int errorMessageRes) {
        Snackbar.make(phoneNumberEditText, errorMessageRes, Snackbar.LENGTH_LONG).show();
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
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
                openAdminPage();
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

    private boolean checkIfUserExist() {
        for(int i = 0;i<flatDetailList.size();i++){

            if(flatDetailList.get(i).getPhoneNumber().equals("+91"+phoneNumber)){
                flatDetail = flatDetailList.get(i);
                return true;
            }
        }
        return false;
    }

    private void getAllUser() {

        final MaterialDialog dialog = new MaterialDialog.Builder(LoginActivity.this)
                .title("Loading")
                .content("Please wait")
                .progress(true, 0)
                .cancelable(false)
                .progressIndeterminateStyle(true)
                .show();


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                flatDetailList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    FlatDetail flat = snapshot.getValue(FlatDetail.class);
                    flatDetailList.add(flat);
                }
                //hideProgressView();
                dialog.dismiss();
                //geometricProgressView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // hideProgressView();
                dialog.dismiss();
                makeToast("Something went wrong while searching");
            }
        });
    }

    private void makeToast(String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void openAdminPage() {
        UserData.ownerflatDetail = flatDetail;
        startActivity(new Intent(LoginActivity.this, AdminHomeActivity.class));
    }

    private void openCreateNewUserPage() {
        startActivity(new Intent(LoginActivity.this, CreateNewUser.class));
    }
}
