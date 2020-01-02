package com.example.airprepare;



import androidx.appcompat.app.AppCompatActivity;



import android.content.Intent;

import android.os.Bundle;

import android.view.View;

import android.widget.Button;

import android.widget.EditText;
import android.widget.Toast;


public class numberauthentication extends AppCompatActivity {

    EditText numbera;

    Button numberab;



    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_numberauthentication);

        numbera=findViewById(R.id.numbera);

        numberab=findViewById(R.id.numberab);

    }



    public void OpenOTPactivity(View view) {

        String number = numbera.getText().toString();
        if (number.length() != 13) {
            Toast.makeText(this, "please enter a valid mobile number", Toast.LENGTH_SHORT).show();
        } else {

            Intent intent = new Intent(this, OTPactivity.class);

            intent.putExtra("number", number);

            startActivity(intent);

        }
    }
}