package com.my.Instagram;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;
import com.my.Instagram.databinding.EditSampeBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class EditAdapter extends RecyclerView.Adapter<EditAdapter.viewHolder> {
    ArrayList<PostModel> list;
    Context context;

    public EditAdapter(ArrayList<PostModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.edit_sampe,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        PostModel postModel=list.get(position);

        Picasso.get()
                .load(postModel.getPostImage())
                .placeholder(R.drawable.photo)
                .into(holder.binding.imageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public  class viewHolder extends RecyclerView.ViewHolder{
        EditSampeBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding=EditSampeBinding.bind(itemView);
        }
    }
}
