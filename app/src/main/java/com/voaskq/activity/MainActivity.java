package com.voaskq.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.voaskq.Fragment.AskFragment;
import com.voaskq.Fragment.HomeFragment;
import com.voaskq.Fragment.NewVideoFragment;
import com.voaskq.Fragment.ProfileFragment;
import com.voaskq.Fragment.VoteFragment;
import com.voaskq.R;
import com.voaskq.helper.Constant;

import static com.voaskq.helper.Constant.HOME_PAGE_SELECTED;

public class MainActivity extends AppCompatActivity {

    FragmentManager fragmentManager;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    ImageView header_add,header_search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initviews();

        if (savedInstanceState == null) {
            Fragment fragment = new HomeFragment();
            changeFragment(fragment);
        }

        clickEvents();

    }

    private void clickEvents() {

        header_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              Intent in = new Intent(MainActivity.this,AddActivity.class);
              startActivity(in);

            }
        });

        header_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(MainActivity.this,SearchActivity.class);
                startActivity(in);

            }
        });


    }

    private void initviews() {
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        pref = MainActivity.this.getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();

        header_add = findViewById(R.id.header_add);
        header_search  = findViewById(R.id.header_search);
    }


    private void changeFragment(Fragment fragment) {
        fragmentManager = MainActivity.this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
              //      System.gc();
                    Constant.CURRENT_PAGE_SELECTED =  Constant.HOME_PAGE_SELECTED;
                    Fragment fragment = new HomeFragment();
                    changeFragment(fragment);
                    return true;

                case R.id.navigation_ask:
               //     System.gc();
                    Constant.CURRENT_PAGE_SELECTED =  Constant.ASK_PAGE_SELECTED;
                    Fragment fragment4 = new AskFragment();
                    changeFragment(fragment4);
                    return true;

                case R.id.navigation_vote:
               //     System.gc();
                    Constant.CURRENT_PAGE_SELECTED =  Constant.VOTE_PAGE_SELECTED;
                    Fragment fragment2 = new VoteFragment();
                    changeFragment(fragment2);
                    return true;

                case R.id.navigation_profile:
              //      System.gc();
                    Fragment fragment1 = new ProfileFragment();
                    changeFragment(fragment1);
                    return true;

                case R.id.navigation_video:
             //       System.gc();
                    Fragment fragment3 = new NewVideoFragment();
                    changeFragment(fragment3);
                    return true;


            }
            return false;
        }
    };

    private void showLogoutDialog() {

        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.message);

        final TextView tvmsg = (TextView) dialog.findViewById(R.id.msg);
        TextView done = dialog.findViewById(R.id.btn_done);
        TextView cancel = dialog.findViewById(R.id.btn_cancel);

        done.setText("Yes");
        cancel.setText("No");

        tvmsg.setText("Do You Want to Logout?");
        tvmsg.setGravity(Gravity.CENTER);


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Logout Successfully ", Toast.LENGTH_SHORT).show();
                editor.putString("Userid",null );
                editor.commit();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();

        showLogoutDialog();
    }
}
