package com.lambton.projects.note_wethree_android.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.lambton.projects.note_wethree_android.R;

public class SplashScreenActivity extends AppCompatActivity {
    //Variables
    Animation logoAnim, textAnim;
    ImageView image;
    TextView title;
    private static int SPLASH_SCREEN = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // to get full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        //animations forthe splash screen
        logoAnim = AnimationUtils.loadAnimation(this,R.anim.logo_animation);
        textAnim = AnimationUtils.loadAnimation(this,R.anim.text_animation);

        //hooks
        image = findViewById(R.id.imageView);
        title = findViewById(R.id.titleTV);

        image.setAnimation(logoAnim);
        title.setAnimation(textAnim);

        //delay method
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this,CategoryListActivity.class);
                startActivity(intent);
                finish();

            }
        }, SPLASH_SCREEN);


    }
}