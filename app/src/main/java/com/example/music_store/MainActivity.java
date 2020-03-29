package com.example.music_store;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.music_store.Model.Users;
import com.example.music_store.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog loadingBar ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Paper.init(this);

        loadingBar = new ProgressDialog(this) ;


        Button signupbtn = findViewById(R.id.signup);
        TextView signinbtn = findViewById(R.id.signin);
//
//        TypeWriter tw = (TypeWriter)findViewById(R.id.musimental) ;
//        tw.setText("");
//        tw.setCharacterDelay(200);
//        tw.animateText("MusiMental");
//

        signinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignInActivity.class) ;
                startActivity(intent) ;

            }
        });




               signupbtn.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {

                       Intent intent = new Intent(MainActivity.this, SignUpActivity.class) ;
                       startActivity(intent) ;

               }


    });


               String UserPasswordKey = Paper.book().read(Prevalent.UserPasswordKey) ;
        String UserEmailKey = Paper.book().read(Prevalent.UserEmailKey) ;

        if(UserEmailKey!= "" && UserPasswordKey !=""){
            if(!TextUtils.isEmpty(UserEmailKey) && !TextUtils.isEmpty(UserPasswordKey)){
                AllowAccess(UserEmailKey, UserPasswordKey) ;

                loadingBar.setTitle("Already Logged In");
                loadingBar.setMessage("Please Wait For a while");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
            }
        }


    }

    private void AllowAccess(final String userEmailKey, final String userPasswordKey) {

        final DatabaseReference RootRef ;
        RootRef = FirebaseDatabase.getInstance().getReference();

        final String encodedemail = userEmailKey.replace(".", ",");
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("Users").child(encodedemail).exists()){

                    Users usersData = dataSnapshot.child("Users").child(encodedemail).getValue(Users.class) ;

                    if(usersData.getEmail().equals(userEmailKey)){
                        if(usersData.getPassword().equals(userPasswordKey)){
                            Toast.makeText(MainActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show() ;
                            loadingBar.dismiss();


                            Intent intent =  new Intent(MainActivity.this, HomeActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(MainActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show() ;
                            loadingBar.dismiss();
                        }

                    }


                }else{
                    Toast.makeText(MainActivity.this, "Email Doesn't Exist",Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}



