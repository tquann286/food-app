package com.example.foodapp.customerFoodPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class CustomerPhoneSendOTP extends AppCompatActivity {

    String phonenumber, number;
    EditText entercode;
    String verificationId;
    FirebaseAuth FAuth;
    Button verify, Resend;
    TextView txt;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_phone_send_otp);

        phonenumber = getIntent().getStringExtra("phonenumber").trim();
        number = getIntent().getStringExtra("number").trim();
        sendVerificationCode(phonenumber);

        entercode = (EditText) findViewById(R.id.phoneno);
        txt = (TextView) findViewById(R.id.text);
        Resend = (Button) findViewById(R.id.Resendotp);
        FAuth = FirebaseAuth.getInstance();
        Resend.setVisibility(View.INVISIBLE);
        txt.setVisibility(View.INVISIBLE);
        verify = (Button) findViewById(R.id.Verify);

        databaseReference = FirebaseDatabase.getInstance().getReference("Customer").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Customer customer = dataSnapshot.getValue(Customer.class);

                verify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Resend.setVisibility(View.INVISIBLE);
                        String code = entercode.getText().toString().trim();
                        if (code.isEmpty() && code.length() < 6) {
                            entercode.setError("Vui lòng nhập OTP");
                            entercode.requestFocus();
                            return;
                        }
                        verifyCode(code);
                    }
                });
                new CountDownTimer(60000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        txt.setVisibility(View.VISIBLE);
                        txt.setText("Gửi lại mã trong "+millisUntilFinished/1000+" giây");
                    }

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
                        reSendOTP(phonenumber);

                        new CountDownTimer(60000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                txt.setVisibility(View.VISIBLE);
                                txt.setText("Gửi lại mã trong "+millisUntilFinished/1000+" giây");
                            }

                            @Override
                            public void onFinish() {
                                Resend.setVisibility(View.VISIBLE);
                                txt.setVisibility(View.INVISIBLE);

                            }
                        }.start();

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void reSendOTP(String phonenumber) {
        sendVerificationCode(phonenumber);
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
                entercode.setText(code);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(CustomerPhoneSendOTP.this , e.getMessage(),Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(@NonNull String verification,@NonNull PhoneAuthProvider.ForceResendingToken token){
            super.onCodeSent(verification,token);
            verificationId = verification;
        }
    };

    private void verifyCode(String code) {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

        user.updatePhoneNumber(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    String  userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    FirebaseDatabase.getInstance().getReference("Customer").child(userid).child("MobileNo").setValue(number).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(CustomerPhoneSendOTP.this, "Đổi số điện thoại thành công", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(CustomerPhoneSendOTP.this, "Lỗi 3: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(CustomerPhoneSendOTP.this, "Lỗi 2: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}