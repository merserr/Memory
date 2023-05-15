package com.example.memory;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.memory.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayout;


public class MainActivity extends AppCompatActivity {

    public static final String BROADCAST_ACTION_CONTROL = "com.example.memory_control";
    public static final String BROADCAST_ACTION_FRAG_1 = "com.example.memory_frag_1";
    public static final String BROADCAST_ACTION_FRAG_2 = "com.example.memory.frag_2";
    public static final String BROADCAST_ACTION_FRAG_3 = "com.example.memory.frag_3";
    public static final String MESSAGEOUTPUTCONTROL = "txtARPTable1Control";
    public static final String MESSAGEOUTPUTFR1 = "txtARPTable1Fragment1";
    public static final String MESSAGEOUTPUTFR2 = "txtARPTable1Fragment2";
    public static final String MESSAGEOUTPUTFR3 = "txtARPTable1Fragment3";
    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void goSettings(View view) {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }
}