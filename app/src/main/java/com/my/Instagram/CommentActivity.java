package com.my.Instagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.my.Instagram.databinding.ActivityCommentBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class CommentActivity extends AppCompatActivity {
    ActivityCommentBinding binding;
   public static String postId;
    public static String postedBy;
    Intent intent;
    FirebaseAuth auth;
    FirebaseDatabase database;
    ArrayList<Comment> list=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCommentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        intent=getIntent();

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
      // postId=getIntent().getStringExtra("postId");
        postId=intent.getStringExtra("postId");
        //postedBy=getIntent().getStringExtra("postedBy");
        postedBy=intent.getStringExtra("postedBy");


        database.getReference().child("posts")
                .child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                PostModel postModel=snapshot.getValue(PostModel.class);
                Picasso.get()
                        .load(postModel.getPostImage())
                        .placeholder(R.drawable.photo)
                        .into(binding.commentActivity);
                binding.likeComment.setText(postModel.getPostLike()+"");
                binding.textView13.setText(postModel.getPostDescription());
                binding.cmComment.setText(postModel.getCommentCount()+"");


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        database.getReference().child("Users")
             .child(postedBy).addListenerForSingleValueEvent(new ValueEventListener() {
         @Override
         public void onDataChange(@NonNull DataSnapshot snapshot) {
          User user=snapshot.getValue(User.class);
          Picasso.get()
                  .load(user.getCover())
                  .placeholder(R.drawable.photo)
                  .into(binding.profileImage);
          binding.commentProfill.setText(user.getName());
         }

         @Override
         public void onCancelled(@NonNull DatabaseError error) {

         }
     });

binding.postComment.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Comment comment=new Comment();
        comment.setCommentBody(binding.textComment.getText().toString());
        comment.setCommentedAt(new Date().getTime());
        comment.setCommentedBy(FirebaseAuth.getInstance().getUid());

        database.getReference().child("posts")
                .child(postId)
                .child("comments")
                .push()
                .setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                database.getReference()
                        .child("posts")
                        .child(postId)
                        .child("commentCount").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int commentCount=0;
                        if (snapshot.exists())
                        {
                            commentCount=snapshot.getValue(Integer.class);
                        }
                        database.getReference().child("posts")
                                .child(postId)
                                .child("commentCount")
                                .setValue(commentCount + 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                binding.textComment.setText("");
                                Toast.makeText(CommentActivity.this, "commented", Toast.LENGTH_SHORT).show();


                                NotificationModel model=new NotificationModel();
                                model.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                model.setNotificationAt(new Date().getTime());
                                model.setPostID(postId);
                                model.setPostedBy(postedBy);
                                model.setType("comment");

                                FirebaseDatabase.getInstance().getReference()
                                        .child("notification")
                                        .child(postedBy)
                                        .push()
                                        .setValue(model);

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }
});

        CommentAdapter adapter=new CommentAdapter(this,list);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        binding.recyleComment.setAdapter(adapter);
        binding.recyleComment.setLayoutManager(layoutManager);

        database.getReference()
                .child("posts")
                .child(postId)
                .child("comments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    Comment comment=snapshot.getValue(Comment.class);
                    list.add(comment);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}