package com.example.qunltxe.screen.home;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qunltxe.R;
import com.example.qunltxe.screen.login.DangNhap;

public class SplashScreen extends AppCompatActivity {

    Animation ChangeColor;
    private ImageView Logo;
    private Thread Thread;
    private TextView Loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        setControl();
        startAnimation();
    }

    private void setControl() {
        Logo = findViewById(R.id.image);
        Loading = findViewById(R.id.loading);
    }

    private void startAnimation() {
        Animation fade = AnimationUtils.loadAnimation(this, R.anim.fade);
        Animation loading = AnimationUtils.loadAnimation(this, R.anim.fade_blink);

        fade.reset();
        loading.reset();

        final AnimationDrawable change_color = (AnimationDrawable) Logo.getDrawable();
        change_color.start();

        Logo.setAnimation(fade);
        Loading.setAnimation(loading);
        Thread = new Thread() {
            @Override
            public void run() {
                super.run();
                int waited = 0;
                while (waited < 5000) {
                    try {
                        sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    waited += 100;
                }
                SplashScreen.this.finish();
                Intent intent = new Intent(SplashScreen.this, DangNhap.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }
        };
        Thread.start();
    }
}
