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

public class TopsActivity extends AppCompatActivity {

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

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        dbHelper = new DatabaseHelper(this);
        topsList = dbHelper.getClothingItemsByCategory("tops");

        adapter = new ClothingAdapter(topsList, this);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fabCart = findViewById(R.id.fabCart);
        fabCart.setOnClickListener(v -> {
            Intent intent = new Intent(TopsActivity.this, CartActivity.class);
            startActivity(intent);
        });

        adapter.setOnItemClickListener(new ClothingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Get the selected item and start ProductDetailActivity
                Clothing selectedClothing = topsList.get(position);
                Intent intent = new Intent(TopsActivity.this, ProductDetailActivity.class);
                intent.putExtra("clothingName", selectedClothing.getName());
                intent.putExtra("clothingPrice", selectedClothing.getPrice());
                intent.putExtra("clothingImage", selectedClothing.getImageResourceId());
                intent.putExtra("clothingCategory", "tops");
                startActivity(intent);
            }
        });

        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                // Handle navigation item clicks
                if (id == R.id.nav_home) {
                    startActivity(new Intent(TopsActivity.this, MainActivity.class));
                } else if (id == R.id.nav_tops) {
                    startActivity(new Intent(TopsActivity.this, TopsActivity.class));
                } else if (id == R.id.nav_jackets) {
                    startActivity(new Intent(TopsActivity.this, JacketsActivity.class));
                } else if (id == R.id.nav_bottoms) {
                    startActivity(new Intent(TopsActivity.this, BottomsActivity.class));
                } else if (id == R.id.nav_shoes) {
                    startActivity(new Intent(TopsActivity.this, ShoesActivity.class));
                } else if (id == R.id.nav_about) {
                    startActivity(new Intent(TopsActivity.this, AboutUsActivity.class));
                } else if (id == R.id.nav_contact_us) {
                    startActivity(new Intent(TopsActivity.this, ContactUsActivity.class));
                } else if (id == R.id.nav_account_settings) {
                    startActivity(new Intent(TopsActivity.this, AccountSettingsActivity.class));
                } else if (id == R.id.nav_logout) {
                    startActivity(new Intent(TopsActivity.this, LoginActivity.class));
                    finish();
                }

                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
