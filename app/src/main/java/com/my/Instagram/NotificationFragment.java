package com.my.Instagram;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
import com.my.Instagram.databinding.FragmentNotificationBinding;

import java.util.ArrayList;


public class NotificationFragment extends Fragment {

FragmentNotificationBinding binding;
ArrayList<NotificationModel> list;
FirebaseAuth auth;
FirebaseDatabase database;
FirebaseStorage storage;
    public NotificationFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();
        list=new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentNotificationBinding.inflate(inflater, container, false);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        binding.notificationRecycle.setLayoutManager(layoutManager);
        NotificationAdapter adapter=new NotificationAdapter(getContext(),list);
        binding.notificationRecycle.setNestedScrollingEnabled(false);
        binding.notificationRecycle.setAdapter(adapter);

     database.getReference().child("notification")
             .child(FirebaseAuth.getInstance().getUid())
             .addValueEventListener(new ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull DataSnapshot snapshot) {
                     list.clear();
                     for (DataSnapshot dataSnapshot:snapshot.getChildren())
                     {
                         NotificationModel notificationModel=dataSnapshot.getValue(NotificationModel.class);
                         notificationModel.setNotificationID(dataSnapshot.getKey());
                         if (!dataSnapshot.getKey().equals(FirebaseAuth.getInstance().getUid()))
                         {
                             list.add(notificationModel);
                         }
                        /* if (!dataSnapshot.getKey().equals(FirebaseAuth.getInstance().getUid()))
                         {

                         }*/


                     }
                     adapter.notifyDataSetChanged();

                 }

                 @Override
                 public void onCancelled(@NonNull DatabaseError error) {

                 }
             });

       return binding.getRoot();
    }
}