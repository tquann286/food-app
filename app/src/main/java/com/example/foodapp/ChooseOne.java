package com.example.foodapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.view.View;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.Button;

public class ChooseOne extends AppCompatActivity {

    Button Chef,Customer,DeliveryPerson;
    Intent intent;
    String type;
    ConstraintLayout bgImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_one);

        AnimationDrawable animationDrawable = new AnimationDrawable();
        animationDrawable.addFrame(getResources().getDrawable(R.drawable.bg2),3000);
        animationDrawable.addFrame(getResources().getDrawable(R.drawable.img2),3000);
        animationDrawable.addFrame(getResources().getDrawable(R.drawable.img3),3000);
        animationDrawable.addFrame(getResources().getDrawable(R.drawable.img4),3000);
        animationDrawable.addFrame(getResources().getDrawable(R.drawable.img5),3000);
        animationDrawable.addFrame(getResources().getDrawable(R.drawable.img6),3000);
        animationDrawable.addFrame(getResources().getDrawable(R.drawable.img7),3000);
        animationDrawable.addFrame(getResources().getDrawable(R.drawable.img8),3000);
        animationDrawable.addFrame(getResources().getDrawable(R.drawable.bg3),3000);
        animationDrawable.addFrame(getResources().getDrawable(R.drawable.img9),3000);
        animationDrawable.addFrame(getResources().getDrawable(R.drawable.img10),3000);
        animationDrawable.addFrame(getResources().getDrawable(R.drawable.img11),3000);
        animationDrawable.addFrame(getResources().getDrawable(R.drawable.img11),3000);

        animationDrawable.setOneShot(false);
        animationDrawable.setEnterFadeDuration(850);
        animationDrawable.setExitFadeDuration(1600);

        bgImage = findViewById(R.id.back3);
        bgImage.setBackgroundDrawable(animationDrawable);
        animationDrawable.start();

        intent = getIntent();
        type = intent.getStringExtra("Home").toString().trim();

        Chef = (Button)findViewById(R.id.chef);
        DeliveryPerson = (Button)findViewById(R.id.delivery);
        Customer = (Button)findViewById(R.id.customer);

        Chef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type.equals("Email")){
                    Intent loginEmail  = new Intent(ChooseOne.this,ChefLogin.class);
                    startActivity(loginEmail);
                    finish();
                }
                if(type.equals("Phone")){
                    Intent loginPhone  = new Intent(ChooseOne.this,ChefLoginPhone.class);
                    startActivity(loginPhone);
                    finish();
                }
                if(type.equals("SignUp")){
                    Intent Register  = new Intent(ChooseOne.this,ChefRegistration.class);
                    startActivity(Register);
                }
            }
        });

        Customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(type.equals("Email")){
                    Intent loginEmailCustomer  = new Intent(ChooseOne.this,Login.class);
                    startActivity(loginEmailCustomer);
                    finish();
                }
                if(type.equals("Phone")){
                    Intent loginPhoneCustomer  = new Intent(ChooseOne.this,LoginPhone.class);
                    startActivity(loginPhoneCustomer);
                    finish();
                }
                if(type.equals("SignUp")){
                    Intent RegisterCustomer  = new Intent(ChooseOne.this,Registration.class);
                    startActivity(RegisterCustomer);
                }

            }
        });

        DeliveryPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(type.equals("Email")){
                    Intent loginEmail = new Intent(ChooseOne.this,DeliveryLogin.class);
                    startActivity(loginEmail);
                    finish();
                }
                if(type.equals("Phone")){
                    Intent loginPhone  = new Intent(ChooseOne.this,DeliveryLoginPhone.class);
                    startActivity(loginPhone);
                    finish();
                }
                if(type.equals("SignUp")){
                    Intent RegisterDelivery  = new Intent(ChooseOne.this,DeliveryRegistration.class);
                    startActivity(RegisterDelivery);
                }

            }
        });

    }
}