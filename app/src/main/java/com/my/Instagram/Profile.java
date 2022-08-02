package com.my.Instagram;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.my.Instagram.databinding.FragmentProfileBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Profile extends Fragment {
//ViewPager viewPager;
//TabLayout tabLayout;
FragmentProfileBinding binding;
ActivityResultLauncher<String> launcher;
FirebaseAuth auth;
FirebaseStorage storage;
FirebaseDatabase database;
ArrayList<PostModel> list;
PostModel postModel;


    public Profile() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       auth=FirebaseAuth.getInstance();
       storage=FirebaseStorage.getInstance();
       database=FirebaseDatabase.getInstance();
       list=new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentProfileBinding.inflate(inflater, container, false);
       // viewPager=view.findViewById(R.id.viewpager);
      /*  binding.viewpager.setAdapter(new ViewPagerAdapter(getFragmentManager()));

      //  tabLayout=view.findViewById(R.id.tabLayout);
        binding.tabLayout.setupWithViewPager(binding.viewpager);*/
        LikeAdapter likeAdapter=new LikeAdapter(list,getContext());

    GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),3);
        binding.viewpager.setAdapter(likeAdapter);
        binding.viewpager.setHasFixedSize(true);
        binding.viewpager.setLayoutManager(gridLayoutManager);



        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        final String userId=firebaseUser.getUid();

        database.getReference().child("posts")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot:snapshot.getChildren())
                        {
                            PostModel postModel=dataSnapshot.getValue(PostModel.class);
                            postModel.setPostId(dataSnapshot.getKey());
                            if (postModel.getPostedBy().equals(FirebaseAuth.getInstance().getUid()))
                            {
                                list.add(postModel);
                            }

                        }
                        likeAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });





       database.getReference().child("Users").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               if (snapshot.exists()) {
                   User user = snapshot.getValue(User.class);
                   Picasso.get()
                           .load(user.getCover())
                           .placeholder(R.drawable.man)
                           .into(binding.profileImage);
                   binding.userName.setText(user.getName());
                   binding.userId.setText(user.getProfession());
                   binding.followerNum.setText(user.getFollowerCount()+"");
               }

           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });

        launcher=registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri uri) {

                binding.profileImage.setImageURI(uri);
            //    Log.i("TAG", "onActivityResult: "+uri);
                final StorageReference reference=storage.getReference().child("coverphoto").child(FirebaseAuth.getInstance().getUid());
               reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                   @Override
                   public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                       reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                           @Override
                           public void onSuccess(Uri uri) {

                               database.getReference().child("Users").child(auth.getUid()).child("cover").setValue(uri.toString());

                           }
                       });

                   }
               });

            }
        });



        binding.setImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launcher.launch("image/*");
            }
        });










        return binding.getRoot();
    }
}