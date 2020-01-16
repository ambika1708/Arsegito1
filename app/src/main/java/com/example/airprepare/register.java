package com.example.airprepare;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;


public class register extends AppCompatActivity {

    EditText ename,eemail,epassword;

    DatabaseReference databasemember;

    FirebaseAuth firebaseAuth;

    FirebaseUser user;

    String userId;

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        databasemember = FirebaseDatabase.getInstance().getReference();

        eemail=findViewById(R.id.emailr);

        epassword=findViewById(R.id.passwordr);

        firebaseAuth=FirebaseAuth.getInstance();
    }



    public void Openloginpage(View view) {
        addmember();

        Intent intent = new Intent(register.this,Homescreen.class);
        startActivity(intent);

    }

    private void addmember(){

        String email=eemail.getText().toString().trim();

        String password=epassword.getText().toString().trim();

        String number=getIntent().getStringExtra("number");

        String a = number + "";


        if (!TextUtils.isEmpty(email)) {

            String id= databasemember.push().getKey();

            Insert insert = new Insert(id, a, email, password);

            databasemember.child(Objects.requireNonNull(firebaseAuth.getUid())).setValue(insert);

            Toast.makeText(this, "REGISTRATION SUCCESS", Toast.LENGTH_SHORT).show();

        }

        else {

            Toast.makeText(this, "REGISTRATION NOT SUCCESS", Toast.LENGTH_SHORT).show();

        }

    }



}