package com.example.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class ChefVerifyPhone extends AppCompatActivity {
    String verificationId;
    FirebaseAuth FAuth;
    Button verify , Resend ;
    TextView txt;
    EditText entercode;
    String phoneno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_verify_phone);

        phoneno = getIntent().getStringExtra("phonenumber").trim();
        Log.w("a",phoneno.toString());
        entercode = (EditText) findViewById(R.id.code);
        txt = (TextView) findViewById(R.id.text);
        Resend = (Button)findViewById(R.id.Resendotp);
        verify = (Button) findViewById(R.id.Verify);
        FAuth = FirebaseAuth.getInstance();

        FAuth.setLanguageCode("vi");

        Resend.setVisibility(View.INVISIBLE);
        txt.setVisibility(View.INVISIBLE);

        sendVerificationCode(phoneno);

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = entercode.getText().toString().trim();
                Resend.setVisibility(View.INVISIBLE);

                if (code.isEmpty() && code.length()<6){
                    entercode.setError("Vui lòng nhập.");
                    entercode.requestFocus();
                    return;
                }
                verifyCode(code);
            }
        });

        new CountDownTimer(60000,1000){

            @Override
            public void onTick(long millisUntilFinished) {
                txt.setVisibility(View.VISIBLE);
                txt.setText("Gửi lại mã trong "+millisUntilFinished/1000+" giây");
            }

            /**
             * Callback fired when the time is up.
             */
            @Override
            public void onFinish() {
                Resend.setVisibility(View.VISIBLE);
                txt.setVisibility(View.INVISIBLE);
            }
        }.start();

        Resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Resend.setVisibility(View.INVISIBLE);
                ResendOtp(phoneno);

                new CountDownTimer(60000,1000){
                    @Override
                    public void onTick(long millisUntilFinished) {
                        txt.setVisibility(View.VISIBLE);
                        txt.setText("Gửi lại mã trong "+millisUntilFinished/1000+" giây");
                    }

                    /**
                     * Callback fired when the time is up.
                     */
                    @Override
                    public void onFinish() {
                        Resend.setVisibility(View.VISIBLE);
                        txt.setVisibility(View.INVISIBLE);
                    }
                }.start();
            }
        });



    }

    private void sendVerificationCode(String number) {

        PhoneAuthOptions options = PhoneAuthOptions.newBuilder()
                .setPhoneNumber(number)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(callBack)
                .build();

        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callBack=new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            String code = phoneAuthCredential.getSmsCode();
            if(code != null){
                entercode.setText(code);  // Auto Verification
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(ChefVerifyPhone.this , e.getMessage(),Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s , PhoneAuthProvider.ForceResendingToken forceResendingToken){
            super.onCodeSent(s,forceResendingToken);
            verificationId = s;
        }
    };

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId , code);
        linkCredential(credential);
    }

    private void linkCredential(PhoneAuthCredential credential) {
        FAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(ChefVerifyPhone.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(ChefVerifyPhone.this , MainMenu.class);
                            startActivity(intent);
                            finish();
                        }else{
                            ReusableCodeForAll.ShowAlert(ChefVerifyPhone.this,"Có lỗi xảy ra, vui lòng thử lại",task.getException().getMessage());
                        }
                    }
                });

    }

    private void ResendOtp(String phonenum) {
        sendVerificationCode(phonenum);
    }
}