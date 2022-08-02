package com.my.Instagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.SurfaceControl;
import android.widget.Toast;

import com.google.firebase.database.Transaction;
import com.iammert.library.readablebottombar.ReadableBottomBar;
import com.my.Instagram.databinding.ActivityMainBinding;

import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    boolean press=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container,new HomeFragment());
        transaction.commit();

        binding.readableBottomBar.setOnItemSelectListener(new ReadableBottomBar.ItemSelectListener() {
            @Override
            public void onItemSelected(int i) {
                FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
              switch (i){
                  case 0:
                      transaction.replace(R.id.container,new HomeFragment());
                      break;
                  case 1:
                      transaction.replace(R.id.container,new SearchFragment());
                      break;
                  case 2:
                      transaction.replace(R.id.container,new AddFragment());
                      break;
                  case 3:
                      transaction.replace(R.id.container,new NotificationFragment());
                      break;
                  case 4:
                      transaction.replace(R.id.container,new Profile());
                      break;


              }
              transaction.commit();
            }
        });


    }
  // int c=0;
    @Override
    public void onBackPressed() {
      //  c++;
        if (press)
        {

            super.onBackPressed();
            return;
        }
        else
        {
           this.press=true;
            Toast.makeText(MainActivity.this, "Double Tab To BAck", Toast.LENGTH_SHORT).show();
           new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
               @Override
               public void run() {
                   press=false;


               }
           }, 1000);
        }


    }


}