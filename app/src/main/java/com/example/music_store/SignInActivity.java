package com.example.music_store;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import io.paperdb.Paper ;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent ;
import com.example.music_store.Model.Users ;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.music_store.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignInActivity extends AppCompatActivity {

    private EditText InputEmail, InputPassword;

    private Button LoginBtn ;
    private String parentDbName = "Users";
private CheckBox chkboxrememberme ;
    private ProgressDialog loadingBar ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        chkboxrememberme = findViewById(R.id.rememberme) ;

        Paper.init(this) ;
        LoginBtn = findViewById(R.id.signinbtn) ;
        InputEmail = findViewById(R.id.emailinput) ;
        InputPassword = findViewById(R.id.passwordinput);
        loadingBar = new ProgressDialog(this) ;

        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loginUser();

            }

            private void loginUser() {
                String email = InputEmail.getText().toString() ;
                String password = InputPassword.getText().toString() ;
                if (email.equals("")) {
                    YoYo.with(Techniques.Tada)
                            .duration(200)
                            .repeat(1)
                            .playOn(findViewById(R.id.firstname));
                    Toast.makeText(getApplicationContext(), "Enter Email", Toast.LENGTH_SHORT).show();
                }else if (password.equals("")) {
                    YoYo.with(Techniques.Tada)
                            .duration(200)
                            .repeat(1)
                            .playOn(findViewById(R.id.lastname));
                    Toast.makeText(getApplicationContext(), "Enter Password", Toast.LENGTH_SHORT).show();
                }else{

                    loadingBar.setTitle("Create Account");
                    loadingBar.setMessage("Please Wait While We are Checking Credentials");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    AllowAccessToAccount(email,password) ;

                }


            }

            //Checking Users Credentials

            private void AllowAccessToAccount(final String email, final String password) {
                if(chkboxrememberme.isChecked()){

                    Paper.book().write(Prevalent.UserEmailKey, email) ;
                    Paper.book().write(Prevalent.UserPasswordKey, password) ;

                }

                final DatabaseReference RootRef ;
                RootRef = FirebaseDatabase.getInstance().getReference();

                final String encodedemail = email.replace(".", ",");
                RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(parentDbName).child(encodedemail).exists()){

                            Users usersData = dataSnapshot.child(parentDbName).child(encodedemail).getValue(Users.class) ;

                            if(usersData.getEmail().equals(email)){
                                if(usersData.getPassword().equals(password)){
                                    Toast.makeText(SignInActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show() ;
                                    loadingBar.dismiss();


                                    Intent intent =  new Intent(SignInActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(SignInActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show() ;
                                    loadingBar.dismiss();
                                }

                            }


                        }else{
                            Toast.makeText(SignInActivity.this, "Email Doesn't Exist",Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

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
