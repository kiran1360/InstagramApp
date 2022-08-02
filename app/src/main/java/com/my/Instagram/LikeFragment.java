package com.my.Instagram;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.my.Instagram.databinding.FragmentLikeBinding;

import java.util.ArrayList;


public class LikeFragment extends Fragment {

   FragmentLikeBinding binding;
   FirebaseDatabase database;
   FirebaseAuth auth;
   FirebaseStorage storage;
   ArrayList<PostModel> list;


    public LikeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
        storage=FirebaseStorage.getInstance();
        list=new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       binding=FragmentLikeBinding.inflate(inflater, container, false);
       LikeAdapter likeAdapter=new LikeAdapter(list,getContext());
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),3);
        binding.likeRecyle.setAdapter(likeAdapter);
        binding.likeRecyle.setLayoutManager(gridLayoutManager);
        database.getReference().child("posts")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot:snapshot.getChildren())
                        {
                            PostModel postModel=dataSnapshot.getValue(PostModel.class);
                            postModel.setPostId(dataSnapshot.getKey());
                            list.add(postModel);
                        }
                        likeAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


       return  binding.getRoot();
    }
}