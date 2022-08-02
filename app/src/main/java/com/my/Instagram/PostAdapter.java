package com.my.Instagram;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.my.Instagram.databinding.DashboardBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.viewHolder>{
    ArrayList<PostModel> list;
    Context context;

    public PostAdapter(ArrayList<PostModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.dashboard,parent,false);


        return new viewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        PostModel postModel=list.get(position);
        Picasso.get()
                .load(postModel.getPostImage())
                .placeholder(R.drawable.photo)
                .into(holder.binding.imageView);
        holder.binding.like.setText(postModel.getPostLike()+"");
        holder.binding.comment.setText(postModel.getCommentCount()+"");
        String data=postModel.getPostDescription();
        if (data.equals(""))
        {
            holder.binding.postDEs.setVisibility(View.GONE);
        }
        else {
            holder.binding.postDEs.setText(postModel.getPostDescription());
            holder.binding.postDEs.setVisibility(View.VISIBLE);
        }

        FirebaseDatabase.getInstance().getReference().child("Users").child(postModel.getPostedBy()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                    User user=snapshot.getValue(User.class);
                    Picasso.get()
                            .load(user.getCover())
                            .placeholder(R.drawable.man)
                            .into(holder.binding.profileImage);
                    holder.binding.textView10.setText(user.getName());
                    holder.binding.textView11.setText(user.getProfession());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        FirebaseDatabase.getInstance().getReference().child("posts")
                .child(postModel.getPostId())
                .child("like")
                .child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    holder.binding.like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.heeart,0,0,0);
                }
                else
                {
                    holder.binding.like.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FirebaseDatabase.getInstance().getReference()
                                    .child("posts")
                                    .child(postModel.getPostId())
                                    .child("like")
                                    .child(FirebaseAuth.getInstance().getUid())
                                    .setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("posts")
                                            .child(postModel.getPostId())
                                            .child("postLike")
                                            .setValue(postModel.getPostLike() +1 ).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            holder.binding.like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.heeart,0,0,0);

                                            NotificationModel model=new NotificationModel();
                                            model.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                            model.setNotificationAt(new Date().getTime());
                                            model.setPostID(postModel.getPostId());
                                            model.setPostedBy(postModel.getPostedBy());
                                            model.setType("like");


                                            FirebaseDatabase.getInstance().getReference()
                                                    .child("notification")
                                                    .child(postModel.getPostedBy())
                                                    .push()
                                                    .setValue(model);
                                        }
                                    });

                                }
                            });


                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        holder.binding.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,CommentActivity.class);
                intent.putExtra("postId",postModel.getPostId());
                intent.putExtra("postedBy",postModel.getPostedBy());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

      /*  holder.binding.menu1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popupMenu=new PopupMenu(context.getApplicationContext(),holder.binding.menu1);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu,popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId())
                        {
                            case R.id.item:
                                Toast.makeText(context, "click1", Toast.LENGTH_SHORT).show();
                        }
                        return false;
                    }
                });
                return false;
            }
        });*/

        if (postModel.getPostedBy().equals(FirebaseAuth.getInstance().getUid()))
        {
            holder.binding.menu1.setVisibility(View.VISIBLE);
            holder.binding.menu1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
               /* PopupMenu popupMenu=new PopupMenu(context,holder.binding.menu1);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu,popupMenu.getMenu());*/
                    AlertDialog.Builder builder=new AlertDialog.Builder(context);
                    builder.setTitle("Delete");
                    builder.setMessage("Are you sure..");
                    builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                     /*   if (!postModel.getPostId().equals(FirebaseAuth.getInstance().getUid()))
                        {
                            holder.binding.menu1.setVisibility(View.VISIBLE);
                        }
                        else
                        {*/
                            //  holder.binding.menu1.setVisibility(View.INVISIBLE);
                            FirebaseDatabase.getInstance().getReference().child("posts")
                                    .child(postModel.getPostId()).removeValue();
                        }

                        // }
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
        else {
            holder.binding.menu1.setVisibility(View.INVISIBLE);

        }
        holder.binding.textView10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,EditActivity2.class);
                intent.putExtra("s1",postModel.getPostId());
                intent.putExtra("s3",postModel.getPostImage());
                intent.putExtra("s2",postModel.getPostedBy());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

       /* holder.binding.textView10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                Intent intent=new Intent(context,ChnageFragment.class);
                intent.putExtras(bundle);
                context.startActivity(intent,bundle);
            }
        });*/


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public  class viewHolder extends RecyclerView.ViewHolder{
        DashboardBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding=DashboardBinding.bind(itemView);
        }
    }
}
