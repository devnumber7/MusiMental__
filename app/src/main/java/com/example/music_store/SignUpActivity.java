package com.example.music_store;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;
import android.widget.Button ;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {
    private ProgressDialog loadingBar ;


    public static String EncodeString(String string) {
        return string.replace(".", ",");

    }
    public static String DecodeString(String string) {
        return string.replace(",", ".");

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_acticity);

        Button signup = findViewById(R.id.signupbtn);
        final EditText firstname = findViewById(R.id.firstnameinput);
        final EditText lastname = findViewById(R.id.lastnameinput);
        final EditText email = findViewById(R.id.emailinput);
        final EditText password = findViewById(R.id.passwordinput);




        loadingBar = new ProgressDialog(this) ;

       // final EditText confirmpassword = findViewById(R.id.confirmpasswordinput);
       final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-zA-Z]+";

     /*   final String pass, conpass ;
        pass = password.getText().toString() ;
        conpass = password.getText().toString() ;
*/

        TextView signin = findViewById(R.id.signIn) ;


        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class) ;
                startActivity(intent) ;

            }
        });



//Email UPPER CASE BUG Confirm Password BUG

        signup.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {

                String inputfirstname = firstname.getText().toString() ;
                String inputlastname = lastname.getText().toString() ;
                String inputemail = email.getText().toString() ;
                String inputpassword = password.getText().toString() ;

                if (firstname.getText().toString().equals("")) {
                    YoYo.with(Techniques.Tada)
                            .duration(200)
                            .repeat(1)
                            .playOn(findViewById(R.id.firstname));
                    Toast.makeText(getApplicationContext(), "Enter First Name", Toast.LENGTH_SHORT).show();
                }else if (lastname.getText().toString().equals("")) {
                    YoYo.with(Techniques.Tada)
                            .duration(200)
                            .repeat(1)
                            .playOn(findViewById(R.id.lastname));
                    Toast.makeText(getApplicationContext(), "Enter Last Name", Toast.LENGTH_SHORT).show();
                } else if (email.getText().toString().isEmpty() ) {
                    YoYo.with(Techniques.Tada)
                            .duration(200)
                            .repeat(1)
                            .playOn(findViewById(R.id.email));
                    Toast.makeText(getApplicationContext(), "Enter Email", Toast.LENGTH_SHORT).show();
                }
                else if (!email.getText().toString().matches(emailPattern)) {
                    YoYo.with(Techniques.Tada)
                            .duration(200)
                            .repeat(1)
                            .playOn(findViewById(R.id.email));
                    Toast.makeText(getApplicationContext(), "Enter Valid Email", Toast.LENGTH_SHORT).show();
                }else if (password.getText().toString().isEmpty() || password.getText().toString().trim().length() < 8){
                    YoYo.with(Techniques.Tada)
                            .duration(200)
                            .repeat(1)
                            .playOn(findViewById(R.id.password));
                    Toast.makeText(getApplicationContext(), "Enter Password", Toast.LENGTH_SHORT).show();
                 /*else if ( conpass.isEmpty() ){
                    YoYo.with(Techniques.Tada)
                            .duration(200)
                            .repeat(1)
                            .playOn(findViewById(R.id.confirmpassword));
                    Toast.makeText(getApplicationContext(), "Please Confirm Your Password", Toast.LENGTH_SHORT).show();
                } else if (!conpass.equals(pass)) {

                    YoYo.with(Techniques.Tada)
                        .duration(500)
                        .repeat(1)
                        .playOn(findViewById(R.id.confirmpassword));

                }*/ }else {


                    loadingBar.setTitle("Create Account");
                    loadingBar.setMessage("Please Wait While We are Checking Credentials");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    ValidateEmailAddress(inputfirstname, inputlastname, inputemail, inputpassword);

                }
            }
            private void ValidateEmailAddress(final String inputfirstname, final String inputlastname, final String inputemail, final String inputpassword) {


                final DatabaseReference RootRef ;
                RootRef = FirebaseDatabase.getInstance().getReference() ;

               final String encodedemail = inputemail.replace(".", ",");
                Toast.makeText(SignUpActivity.this, encodedemail, Toast.LENGTH_SHORT).show();


                RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                        if(!(dataSnapshot.child("Users").child(encodedemail).exists())){



                            HashMap<String, Object> uuserdataMap = new HashMap<>() ;
                            uuserdataMap.put("firstname", inputfirstname) ;
                            uuserdataMap.put("lastname", inputlastname) ;
                            uuserdataMap.put("email", inputemail) ;
                            uuserdataMap.put("password", inputpassword) ;


                            RootRef.child("Users").child(encodedemail).updateChildren(uuserdataMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                loadingBar.dismiss();
                                                Toast.makeText(SignUpActivity.this, "Account Made Succesfully, Please Login In", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class) ;
                                                startActivity(intent);



                                            }else{
                                                loadingBar.dismiss();
                                                Toast.makeText(SignUpActivity.this, "Please Don't Use Jio Internet You Cheapster", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });



                        }else{
                            Toast.makeText(SignUpActivity.this, "This Email Already Exixts, Please Use Antoher Email", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                            Intent intent = new Intent(SignUpActivity.this, MainActivity.class) ;
                            startActivity(intent);

                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {



                    }



                });

            }


        });
    }

}








