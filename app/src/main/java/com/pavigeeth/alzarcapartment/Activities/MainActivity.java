package com.pavigeeth.alzarcapartment.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.pavigeeth.alzarcapartment.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void openLogin(View view){
        startActivity(new Intent(MainActivity.this,LoginActivity.class));
    }
}