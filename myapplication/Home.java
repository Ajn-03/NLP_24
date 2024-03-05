package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Home extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    GridView gridView; TextView textView;ImageView imgView; SearchView searchView;
    private ViewPager viewPager;
    private SliderPageAdapter sliderPagerAdapter; private SimpleAdapter adapter;
    private String[] cities = {"Delhi", "Jodhpur", "Aurangabad", "Mumbai", "Jaipur", "Udaipur"};
    private int[] img={R.drawable.delhi,R.drawable.jodhpur,R.drawable.aurangabad,R.drawable.mumbai,R.drawable.jaipur,R.drawable.udaipur};
    private Handler handler; private Runnable runnable; private int delay = 3000; // Delay in milliseconds between page changes

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //Navigation Drawer
        drawerLayout = findViewById(R.id.my_drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.img_3);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_fav) {
                    // Handle "My Account" item click
                    Toast.makeText(Home.this, "My Account clicked", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (id == R.id.nav_settings) {
                    // Handle "Settings" item click
                    Toast.makeText(Home.this, "Settings clicked", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (id == R.id.nav_scan) {
                    // Handle "Logout" item click
                    Intent scan = new Intent(Home.this, Scan.class);
                    startActivity(scan);
                    return true;
                }

                return false;
            }
        });

        //Slider
        viewPager = findViewById(R.id.viewPager);
        sliderPagerAdapter = new SliderPageAdapter(this, img, cities);
        viewPager.setAdapter(sliderPagerAdapter);
        setViewPagerHeight(viewPager);
        // Initialize handler and runnable
        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                int currentItem = viewPager.getCurrentItem();
                int nextItem = currentItem + 1;
                if (nextItem >= img.length) {
                    nextItem = 0;
                }
                viewPager.setCurrentItem(nextItem, true);
                handler.postDelayed(this, delay);
            } };

        //Search View
        searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });

        //Grid View
        gridView = findViewById(R.id.gridView);
        textView = findViewById(R.id.textView);
        imgView = findViewById(R.id.imageView);
        // Set up HashMap
        List<HashMap<String,String>> li =new ArrayList<>();
        for(int i=0;i<cities.length;i++)
        {
            HashMap<String,String> hm=new HashMap<>();
            hm.put("name",cities[i]);
            hm.put("pict",img[i]+"");
            li.add(hm);
        }
        String[] from ={"name","pict"};
        int[] to ={R.id.textView,R.id.imageView};
        adapter = new SimpleAdapter(this,li,R.layout.cardvieww,from,to);
        // Set adapter to GridView
        gridView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Start the auto-scrolling when the activity is resumed
        handler.postDelayed(runnable, delay);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop the auto-scrolling when the activity is paused
        handler.removeCallbacks(runnable);
    }

    private void setViewPagerHeight(ViewPager viewPager) {
        // Get screen height
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenHeight = displayMetrics.heightPixels;

        // Calculate 1/4th of the screen height
        int viewPagerHeight = screenHeight * 3 / 8;

        // Set layout parameters
        ViewGroup.LayoutParams params = viewPager.getLayoutParams();
        params.height = viewPagerHeight;
        viewPager.setLayoutParams(params);
    }
}