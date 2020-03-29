package com.example.music_store;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;


public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

//        YoYo.with(Techniques.RotateIn)
//                .duration(5000)
//                .repeat(5)
//                .playOn(findViewById(R.id.logo));

        TypeWriter tw = (TypeWriter)findViewById(R.id.logoText) ;
        tw.setText("");
        tw.setCharacterDelay(200);
        tw.animateText(" MusiMental ");



        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        },5000);



    }
}
