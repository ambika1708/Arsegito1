package com.example.airprepare;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;



public class OTPactivity extends AppCompatActivity {

    private String verificationId;

    private FirebaseAuth mAuth;

    EditText et;
    public  String phnumber;



    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_otpactivity);

        phnumber = getIntent().getStringExtra("number");

        sendVerificationCode(phnumber);

        mAuth = FirebaseAuth.getInstance();

        et = findViewById(R.id.editText);

    }

    public void Verification(View view) {



        verify(et.getText().toString());

    }

    public void verify(String code){

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId,code);

        signInWithCredential(credential);

    }



    private void signInWithCredential(PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override

            public void onComplete(@NonNull Task<AuthResult> task) {

                if(!task.isSuccessful()){

                    Toast.makeText(OTPactivity.this, "authentication successful", Toast.LENGTH_SHORT).show();

                }

                else{

                    String phnumber = getIntent().getStringExtra("number");

                    Intent i =new Intent(OTPactivity.this,register.class);

                    i.putExtra("number",phnumber);

                    startActivity(i);

                }

            }

        });

    }



    private void sendVerificationCode(String phnumber){

        PhoneAuthProvider.getInstance().verifyPhoneNumber(

                phnumber,

                60,

                TimeUnit.SECONDS,

                TaskExecutors.MAIN_THREAD,

                mCallBack



        );



    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks

            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override

        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {

            super.onCodeSent(s, forceResendingToken);

            verificationId = s;

        }



        @Override

        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            String cd = phoneAuthCredential.getSmsCode();

            if(cd != null){

                verify(cd);

            }



        }



        @Override

        public void onVerificationFailed(FirebaseException e) {
            Intent p = new Intent(OTPactivity.this,numberauthentication.class);
            startActivity(p);
            Toast.makeText(OTPactivity.this, "please enable internet connection", Toast.LENGTH_SHORT).show();



        }

    };

}