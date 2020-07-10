package com.lambton.projects.note_wethree_android.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.lambton.projects.note_wethree_android.R;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //To get the full screen

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //to hide the nav bar
        getSupportActionBar().hide();

        setContentView(R.layout.activity_splash_screen);


        //if we use object once...............
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this,CategoryListActivity.class );
                startActivity(intent);

            }
        },5000);
    }
}