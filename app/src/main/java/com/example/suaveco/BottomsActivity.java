package com.example.suaveco;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class BottomsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ClothingAdapter adapter;
    private List<Clothing> topsList;
    private DatabaseHelper dbHelper;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2)); // Grid layout with 2 columns

        dbHelper = new DatabaseHelper(this);

        drawerLayout = findViewById(R.id.drawer_layout);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }


        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        topsList = dbHelper.getClothingItemsByCategory("bottoms");


        adapter = new ClothingAdapter(topsList, this);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fabCart = findViewById(R.id.fabCart);
        fabCart.setOnClickListener(v -> {
            Intent intent = new Intent(BottomsActivity.this, CartActivity.class);
            startActivity(intent);
        });


        adapter.setOnItemClickListener(new ClothingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                Clothing selectedClothing = topsList.get(position);


                Intent intent = new Intent(BottomsActivity.this, ProductDetailActivity.class);
                intent.putExtra("clothingName", selectedClothing.getName());
                intent.putExtra("clothingPrice", selectedClothing.getPrice());
                intent.putExtra("clothingImage", selectedClothing.getImageResourceId());
                intent.putExtra("clothingCategory", "bottoms");
                startActivity(intent);
            }
        });

        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();


                if (id == R.id.nav_home) {
                    startActivity(new Intent(BottomsActivity.this, MainActivity.class));
                } else if (id == R.id.nav_tops) {
                    startActivity(new Intent(BottomsActivity.this, TopsActivity.class));
                } else if (id == R.id.nav_jackets) {
                    startActivity(new Intent(BottomsActivity.this, JacketsActivity.class));
                } else if (id == R.id.nav_bottoms) {
                    startActivity(new Intent(BottomsActivity.this, BottomsActivity.class));
                } else if (id == R.id.nav_shoes) {
                    startActivity(new Intent(BottomsActivity.this, ShoesActivity.class));
                } else if (id == R.id.nav_about) {
                    startActivity(new Intent(BottomsActivity.this, AboutUsActivity.class));
                } else if (id == R.id.nav_contact_us) {
                    startActivity(new Intent(BottomsActivity.this, ContactUsActivity.class));
                } else if (id == R.id.nav_account_settings) {
                    startActivity(new Intent(BottomsActivity.this, AccountSettingsActivity.class));
                } else if (id == R.id.nav_logout) {
                    startActivity(new Intent(BottomsActivity.this, LoginActivity.class));
                    finish(); // End current activity
                }

                drawerLayout.closeDrawers();
                return true;
            }
        });
    }
}
