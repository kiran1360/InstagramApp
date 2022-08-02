package com.my.Instagram;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.my.Instagram.databinding.FragmentAddBinding;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.sql.Time;
import java.util.Date;

public class AddFragment extends Fragment {
    FragmentAddBinding binding;
    Uri uri;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    ActivityResultLauncher<String> launcher;
    ProgressDialog dialog;


    public AddFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      auth=FirebaseAuth.getInstance();
      database=FirebaseDatabase.getInstance();
      storage=FirebaseStorage.getInstance();
      dialog=new ProgressDialog(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentAddBinding.inflate(inflater, container, false);




       /* launcher=registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri uri) {
                binding.imageView2.setImageURI(uri);
                binding.imageView2.setVisibility(View.VISIBLE);

                binding.postBTN.setBackgroundDrawable(ContextCompat.getDrawable(getContext(),R.drawable.bg));
                binding.postBTN.setTextColor(getContext().getResources().getColor(R.color.white));
                binding.postBTN.setEnabled(true);



            }
        });*/



       dialog.setProgress(ProgressDialog.STYLE_SPINNER);
       dialog.setTitle("Post Uploading..");
       dialog.setMessage("Please Wait..");
       dialog.setCancelable(false);
       dialog.setCanceledOnTouchOutside(false);
        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    User user=snapshot.getValue(User.class);
                    Picasso.get()
                            .load(user.getCover())
                            .placeholder(R.drawable.man)
                            .into(binding.profileImage);
                    binding.textView3.setText(user.getName());
                    binding.textView5.setText(user.getProfession());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });













        binding.discription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String description=binding.discription.getText().toString();
                if (!description.isEmpty())
                {
                    binding.postBTN.setBackgroundDrawable(ContextCompat.getDrawable(getContext(),R.drawable.bg));
                    binding.postBTN.setTextColor(getContext().getResources().getColor(R.color.white));
                    binding.postBTN.setEnabled(true);
                }
                else
                {
                    binding.postBTN.setBackgroundDrawable(ContextCompat.getDrawable(getContext(),R.drawable.sp));
                    binding.postBTN.setTextColor(getContext().getResources().getColor(R.color.purple_200));
                    binding.postBTN.setEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.postUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // launcher.launch("image/*");
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,10);


            }
        });
        binding.postBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                final StorageReference reference=storage.getReference().child("posts").child(FirebaseAuth.getInstance().getUid())
                        .child(new Date().getTime()+"");
                reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {


                                PostModel postModel=new PostModel();

                                postModel.setPostImage(uri.toString());
                                postModel.setPostedBy(FirebaseAuth.getInstance().getUid());
                                postModel.setPostDescription(binding.discription.getText().toString());
                                postModel.setPostedAt(new Date().getTime());

                                database.getReference().child("posts")
                                        .push()
                                        .setValue(postModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        dialog.dismiss();
                                        Toast.makeText(getContext(), "Posted successfully", Toast.LENGTH_SHORT).show();

                                    }
                                });

                            }
                        });
                    }
                });

            }
        });
        return  binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data.getData() !=null)
        {
        uri=data.getData();
        binding.imageView2.setImageURI(uri);
        binding.imageView2.setVisibility(View.VISIBLE);


        }
    }
}