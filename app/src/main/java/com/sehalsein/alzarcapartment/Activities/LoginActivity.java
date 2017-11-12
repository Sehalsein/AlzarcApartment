package com.sehalsein.alzarcapartment.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.sehalsein.alzarcapartment.AdminActivities.AdminHomeActivity;
import com.sehalsein.alzarcapartment.R;

public class LoginActivity extends AppCompatActivity {

    private TextView createAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        createAccount = findViewById(R.id.create_account);
        FirebaseInstanceId.getInstance().getToken();
        FirebaseMessaging.getInstance().subscribeToTopic("notifications");

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCreateNewUserPage();
            }
        });
    }


    public void login(View view){
        openAdminPage();
    }


    private void openAdminPage(){
        startActivity(new Intent(LoginActivity.this, AdminHomeActivity.class));
    }

    private void openCreateNewUserPage(){
        startActivity(new Intent(LoginActivity.this,CreateNewUser.class));
    }
}
