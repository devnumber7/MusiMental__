package com.example.music_store;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import io.paperdb.Paper ;

import android.view.View;
import android.widget.Button ;
public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button logoutbtn= findViewById(R.id.logout) ;
        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Paper.book().destroy() ;
                Intent intent = new Intent(HomeActivity.this, MainActivity.class) ;
                startActivity(intent);
            }
        });
    }
}
