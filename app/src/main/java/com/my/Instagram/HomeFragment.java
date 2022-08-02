package com.my.Instagram;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.my.Instagram.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.Queue;

public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;
    ArrayList<PostModel> list;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;


    public HomeFragment() {
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
        binding=FragmentHomeBinding.inflate(inflater, container, false);
        binding.recyclePPP.showShimmerAdapter();

        PostAdapter adapter=new PostAdapter(list,getContext());
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        binding.recyclePPP.setLayoutManager(layoutManager);
        binding.recyclePPP.setNestedScrollingEnabled(false);

      Query query= FirebaseDatabase.getInstance().getReference("posts")
              .orderByChild("name")
              .startAt("A")
              .endAt("A\uf8ff");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



      /*
        ValueEventListener valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }*/


       database.getReference().child("posts")
               .addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               list.clear();
               for (DataSnapshot dataSnapshot:snapshot.getChildren())
               {
                   PostModel postModel=dataSnapshot.getValue(PostModel.class);
                 postModel.setPostId(dataSnapshot.getKey());
                   list.add(postModel);
               }
               binding.recyclePPP.setAdapter(adapter);
               binding.recyclePPP.hideShimmerAdapter();
               adapter.notifyDataSetChanged();

           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });





        return  binding.getRoot();
    }
}