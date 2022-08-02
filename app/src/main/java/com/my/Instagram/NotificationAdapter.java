package com.my.Instagram;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.my.Instagram.databinding.NotificationSampleBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.viewHolder> {
    Context context;
    ArrayList<NotificationModel> list;

    public NotificationAdapter(Context context, ArrayList<NotificationModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.notification_sample,parent,false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
     NotificationModel notificationModel=list.get(position);
     String type=notificationModel.getType();
        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(notificationModel.getNotificationBy()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user=snapshot.getValue(User.class);
                Picasso.get()
                        .load(user.getCover())
                        .placeholder(R.drawable.photo)
                        .into(holder.binding.profileImage);

                if (type.equals("like"))
                {
                    holder.binding.notificationUSER.setText(Html.fromHtml("<b>"+user.getName()+"<b>  "+  "like your post"));
                }
                else if (type.equals("comment"))
                {
                    holder.binding.notificationUSER.setText(Html.fromHtml("<b>"+user.getName()+"<b>  "+  "comment your post"));
                }
                else
                {
                    holder.binding.notificationUSER.setText(Html.fromHtml("<b>"+user.getName()+"<b>  "+  "start following you"));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        NotificationSampleBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding=NotificationSampleBinding.bind(itemView);
        }
    }
}
