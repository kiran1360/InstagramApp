package com.my.Instagram;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.my.Instagram.databinding.SearchSampleBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.viewHolder>{
    Context context;
    ArrayList<User> list;
    boolean isAdd=false;

    public SearchAdapter(Context context, ArrayList<User> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.search_sample,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

      User user=list.get(position);
        Picasso.get()
                .load(user.getCover())
                .placeholder(R.drawable.man)
                .into(holder.binding.profileImage);
        holder.binding.searchUser.setText(user.getName());


         FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        final String userId=firebaseUser.getUid();


        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(user.getUserID())
                .child("followers")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists())
                        {
                            int followerCount=(int)snapshot.child(user.getUserID()).getChildrenCount();
                            holder.binding.searchBTN.setBackgroundDrawable(ContextCompat.getDrawable(context,R.drawable.bg));
                            holder.binding.searchBTN.setText("Following");
                            holder.binding.searchBTN.setTextColor(context.getResources().getColor(R.color.black));

                        }
                        else
                        {
                            int followerCount=(int)snapshot.child(user.getUserID()).getChildrenCount();
                            holder.binding.searchBTN.setBackgroundDrawable(ContextCompat.getDrawable(context,R.drawable.sp));
                            holder.binding.searchBTN.setText("Follow");
                            holder.binding.searchBTN.setTextColor(context.getResources().getColor(R.color.white));

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
      holder.binding.searchBTN.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

              isAdd=true;

              FirebaseDatabase.getInstance().getReference()
                      .child("Users")
                      .child(user.getUserID())
                      .child("followers").addValueEventListener(new ValueEventListener() {
                  @Override
                  public void onDataChange(@NonNull DataSnapshot snapshot) {
                      if (isAdd==true)
                      {
                          if (snapshot.hasChild(userId))
                          {
                              FirebaseDatabase.getInstance().getReference()
                                      .child("Users")
                                      .child(user.getUserID())
                                      .child("followers").removeValue();
                              isAdd=false;
                          }
                          else {
                              Follow follow=new Follow();
                              follow.setFollowedBy(FirebaseAuth.getInstance().getUid());
                              follow.setFollowedAt(new Date().getTime());

                              FirebaseDatabase.getInstance().getReference()
                                      .child("Users")
                                      .child(user.getUserID())
                                      .child("followers").child(userId).setValue(follow);
                              isAdd=false;
                          }
                      }

                  }

                  @Override
                  public void onCancelled(@NonNull DatabaseError error) {

                  }
              });

          }
      });


     /*   FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUserID()).child("followers").child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    holder.binding.searchBTN.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.bg));
                    holder.binding.searchBTN.setText("Following");
                    holder.binding.searchBTN.setTextColor(context.getResources().getColor(R.color.black));
                //    holder.binding.searchBTN.setEnabled(false);
                }
                else
                {
                    holder.binding.searchBTN.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.sp));
                    holder.binding.searchBTN.setText("Follow");
                    holder.binding.searchBTN.setTextColor(context.getResources().getColor(R.color.black));

                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        holder.binding.searchBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference()
                        .child("Users")
                        .child(user.getUserID())
                        .child("followers").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(userId))
                        {
                            Follow follow=new Follow();
                            follow.setFollowedBy(FirebaseAuth.getInstance().getUid());
                            follow.setFollowedAt(new Date().getTime());

                            FirebaseDatabase.getInstance().getReference()
                                    .child("Users")
                                    .child(user.getUserID())
                                    .child("followers")
                                    .child(FirebaseAuth.getInstance().getUid())
                                    .setValue(follow).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("Users")
                                            .child(user.getUserID())
                                            .child("followerCount")
                                            .setValue(user.getFollowerCount() + 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                               holder.binding.searchBTN.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.bg));
                                holder.binding.searchBTN.setText("Following");
                                holder.binding.searchBTN.setTextColor(context.getResources().getColor(R.color.black));
                                holder.binding.searchBTN.setEnabled(false);

                                            Toast.makeText(context, "You Followed", Toast.LENGTH_SHORT).show();

                                            NotificationModel model=new NotificationModel();
                                            model.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                            model.setNotificationAt(new Date().getTime());
                                            model.setType("follow");

                                            FirebaseDatabase.getInstance().getReference()
                                                    .child("notification")
                                                    .child(user.getUserID())
                                                    .push()
                                                    .setValue(model);

                                        }
                                    });


                                }

                            });
                        }
                        else {
                            Follow follow=new Follow();
                            follow.setFollowedBy(FirebaseAuth.getInstance().getUid());
                            follow.setFollowedAt(new Date().getTime());

                            FirebaseDatabase.getInstance().getReference()
                                    .child("Users")
                                    .child(user.getUserID())
                                    .child("followers")
                                    .child(FirebaseAuth.getInstance().getUid())
                                    .setValue(follow).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("Users")
                                            .child(user.getUserID())
                                            .child("followerCount")
                                            .setValue(user.getFollowerCount() - 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                               holder.binding.searchBTN.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.bg));
                                holder.binding.searchBTN.setText("Following");
                                holder.binding.searchBTN.setTextColor(context.getResources().getColor(R.color.white));
                              //  holder.binding.searchBTN.setEnabled(false);

                                            Toast.makeText(context, "You Followed", Toast.LENGTH_SHORT).show();

                                            NotificationModel model=new NotificationModel();
                                            model.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                            model.setNotificationAt(new Date().getTime());
                                            model.setType("follow");

                                            FirebaseDatabase.getInstance().getReference()
                                                    .child("notification")
                                                    .child(user.getUserID())
                                                    .push()
                                                    .setValue(model);

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



            }
        });*/



/*

        holder.binding.searchBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Follow follow=new Follow();
                follow.setFollowedBy(FirebaseAuth.getInstance().getUid());
                follow.setFollowedAt(new Date().getTime());

                FirebaseDatabase.getInstance().getReference()
                        .child("Users")
                        .child(user.getUserID())
                        .child("followers")
                        .child(FirebaseAuth.getInstance().getUid())
                        .setValue(follow).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        FirebaseDatabase.getInstance().getReference()
                                .child("Users")
                                .child(user.getUserID())
                                .child("followerCount")
                                .setValue(user.getFollowerCount() + 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                holder.binding.searchBTN.setBackgroundDrawable(ContextCompat.getDrawable(context,R.drawable.followactiv));
                                holder.binding.searchBTN.setText("Following");
                                holder.binding.searchBTN.setTextColor(context.getResources().getColor(R.color.purple_200));
                                holder.binding.searchBTN.setEnabled(false);

                                Toast.makeText(context, "You Followed", Toast.LENGTH_SHORT).show();


                               NotificationModel model=new NotificationModel();
                               model.setNotificationBy(FirebaseAuth.getInstance().getUid());
                               model.setNotificationAt(new Date().getTime());
                               model.setType("follow");

                               FirebaseDatabase.getInstance().getReference()
                                       .child("notification")
                                       .child(user.getUserID())
                                       .push()
                                       .setValue(model);


                            }
                        });


                    }
                });

            }
        });
        holder.binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,ChnageFragment.class);
                context.startActivity(intent);
            }
        });
*/

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        SearchSampleBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding=SearchSampleBinding.bind(itemView);
        }
    }
}
