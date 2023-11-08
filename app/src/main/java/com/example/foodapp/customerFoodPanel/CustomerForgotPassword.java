package com.example.foodapp.customerFoodPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.foodapp.R;
import com.example.foodapp.ReusableCodeForAll;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class CustomerForgotPassword extends AppCompatActivity {

    TextInputLayout emaillid;
    Button Reset;
    FirebaseAuth Fauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_forgot_password);

        emaillid=(TextInputLayout)findViewById(R.id.email);
        Reset=(Button)findViewById(R.id.reset);

        Fauth=FirebaseAuth.getInstance();
        Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fauth.sendPasswordResetEmail(emaillid.getEditText().getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(CustomerForgotPassword.this);
                            builder.setMessage("Password has been sent to your Email");
                            builder.setCancelable(false);
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.dismiss();
                                    Intent bb=new Intent(CustomerForgotPassword.this, CustomerPassword.class);
                                    startActivity(bb);

                                }
                            });
                            AlertDialog alert = builder.create();
                            alert.show();
                        } else {

                            ReusableCodeForAll.ShowAlert(CustomerForgotPassword.this,"Error",task.getException().getMessage());
                        }
                    }
                });
            }
        });
    }
}