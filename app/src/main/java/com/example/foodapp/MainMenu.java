package com.example.foodapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.os.Bundle;

public class MainMenu extends AppCompatActivity {

    Button signInEmail,signInPhone,signUp;
    ImageView bgImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        signInEmail=(Button)findViewById(R.id.SignwithEmail);
        signInPhone=(Button)findViewById(R.id.SignwithPhone);
        signUp=(Button)findViewById(R.id.Signup);

        signInEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signEmail = new Intent(MainMenu.this,ChooseOne.class);
                signEmail.putExtra("Home","Email");
                startActivity(signEmail);
                finish();
            }
        });

        signInPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signPhone = new Intent(MainMenu.this,ChooseOne.class);
                signPhone.putExtra("Home","Phone");
                startActivity(signPhone);
                finish();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUp = new Intent(MainMenu.this,ChooseOne.class);
                signUp.putExtra("Home","SignUp");
                startActivity(signUp);
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
    }
}