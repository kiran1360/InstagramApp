package com.my.Instagram;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.my.Instagram.databinding.CommentSampleBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.viewHolder>{
    Context context;
    ArrayList<Comment> list;

    public CommentAdapter(Context context, ArrayList<Comment> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.comment_sample,parent,false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        Comment comment=list.get(position);



       holder.binding.timePOST.setText(comment.getCommentBody()+"");
       // String timePOST = TimeAgo.using(comment.getCommentedAt());
     //  holder.binding.timePOST.setText(comment.getCommentedAt()+"");
     FirebaseDatabase.getInstance().getReference().child("Users")
             .child(CommentActivity.postedBy)
             .addValueEventListener(new ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull DataSnapshot snapshot) {
                     if (snapshot.exists())
                     {
                         User user=snapshot.getValue(User.class);
                         Picasso.get()
                                 .load(user.getCover())
                                 .placeholder(R.drawable.photo)
                                 .into(holder.binding.profileImage);
                         holder.binding.samplePOST.setText(user.getName()+""+comment.getCommentBody());

                     }


                 }

                 @Override
                 public void onCancelled(@NonNull DatabaseError error) {

                 }
             });

            holder.binding.samplePOST.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(context);
                    builder.setTitle("Delete");
                    builder.setMessage("Are you sure ?");
                    builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (comment.getCommentedBy().equals(FirebaseAuth.getInstance().getUid())) {
                                FirebaseDatabase.getInstance().getReference().child("posts")
                                        .child(CommentActivity.postId)
                                        .child("comments")
                                        .removeValue();
                            }
                        }
                    });
                    builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }
            });
        }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        CommentSampleBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding=CommentSampleBinding.bind(itemView);
        }
    }
}
