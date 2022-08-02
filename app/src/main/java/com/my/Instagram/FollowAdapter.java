package com.my.Instagram;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.my.Instagram.databinding.SearchSampleBinding;

import java.util.ArrayList;

public class FollowAdapter extends RecyclerView.Adapter<FollowAdapter.viewHolder> {
    Context context;
    ArrayList<Follow> list;

    public FollowAdapter(Context context, ArrayList<Follow> list) {
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
Follow follow=list.get(position);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public  class viewHolder extends RecyclerView.ViewHolder{
        SearchSampleBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding=SearchSampleBinding.bind(itemView);
        }
    }
}
