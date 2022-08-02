package com.my.Instagram;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.my.Instagram.databinding.LikeProfileSampleBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class LikeAdapter extends RecyclerView.Adapter<LikeAdapter.viewHolder> {
    ArrayList<PostModel> list;
    Context context;

    public LikeAdapter(ArrayList<PostModel> list, Context context) {
        this.list = list;
        this.context = context;
    }


    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.like_profile_sample,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        PostModel postModel=list.get(position);
        FirebaseDatabase.getInstance().getReference().child("posts")
                .child(postModel.getPostId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists())
                        {
                            PostModel postModel1=snapshot.getValue(PostModel.class);
                            Picasso.get()
                                    .load(postModel.getPostImage())
                                    .placeholder(R.drawable.photo)
                       // for blur photo           // .resize(20,20)
                                 // .rotate(20,20,20)

                                    .into(holder.binding.imageView);
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
        LikeProfileSampleBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding=LikeProfileSampleBinding.bind(itemView);

        }
    }
}
