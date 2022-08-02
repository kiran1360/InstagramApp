package com.my.Instagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.my.Instagram.databinding.ActivitySignupBinding;

public class Signup extends AppCompatActivity {
ActivitySignupBinding binding;
FirebaseAuth auth;
FirebaseDatabase database;
FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        currentUser=auth.getCurrentUser();
        getSupportActionBar().hide();

        binding.signupE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email=binding.emailED.getText().toString(),password=binding.PasswordED.getText().toString();
                auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            User user=new User(binding.nameEt.getText().toString(),binding.professionED.getText().toString(),email,password);
                            String id=task.getResult().getUser().getUid();
                            database.getReference().child("Users").child(id).setValue(user);
                            Intent intent=new Intent(Signup.this,MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(Signup.this, "Successfully Register", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(Signup.this, "User Not Register", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });










        binding.goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Signup.this,Login.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (currentUser!=null)
        {
            Intent intent=new Intent(Signup.this,MainActivity.class);
            startActivity(intent);

        }
    }
}