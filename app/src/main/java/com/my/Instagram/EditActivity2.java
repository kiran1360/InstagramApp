package com.my.Instagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.my.Instagram.databinding.ActivityEdit2Binding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class EditActivity2 extends AppCompatActivity {
    ActivityEdit2Binding binding;
  FirebaseDatabase database;
  FirebaseAuth auth;
  ArrayList<PostModel> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityEdit2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
        list=new ArrayList<>();

        String puID=getIntent().getStringExtra("s2");
        String PostID=getIntent().getStringExtra("s1");
        String PostImage=getIntent().getStringExtra("s3");




        database.getReference().child("Users")
                .child(puID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists())
                        {
                            User user=snapshot.getValue(User.class);
                            Picasso.get()
                                    .load(user.getCover())
                                    .placeholder(R.drawable.photo)
                                    .into(binding.profileImage);
                            binding.userName.setText(user.getName());

                            binding.userId.setText(user.getProfession());
                            binding.followerNum.setText(user.getFollowerCount()+"");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        EditAdapter editAdapter=new EditAdapter(list,this);
        GridLayoutManager layoutManager=new GridLayoutManager(this,3);
        binding.viewpager.setAdapter(editAdapter);
        binding.viewpager.setLayoutManager(layoutManager);


        database.getReference().child("posts")
                .child(PostID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot dataSnapshot:snapshot.getChildren())
                        {
                            PostModel postModel=snapshot.getValue(PostModel.class);

                            list.clear();
                            list.add(postModel);
                        }
                        editAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });






               database.getReference()
                        .child("Users")
                        .child(puID)
                        .child("followers")
                        .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                       if (snapshot.exists()){
                           binding.textView4.setBackgroundDrawable(ContextCompat.getDrawable(EditActivity2.this,R.drawable.followactiv));
                        binding.textView4.setText("Following");
                        binding.textView4.setTextColor(EditActivity2.this.getResources().getColor(R.color.purple_200));
                   binding.textView4.setEnabled(false);
                       }
                       else
                       {
                           binding.textView4.setBackgroundDrawable(ContextCompat.getDrawable(EditActivity2.this,R.drawable.followactiv));
                           binding.textView4.setText("Follow");
                           binding.textView4.setTextColor(EditActivity2.this.getResources().getColor(R.color.purple_200));
                           binding.textView4.setEnabled(false);
                       }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }





}