package com.example.suaveco;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ClothingAdapter adapter;
    private List<Clothing> clothingList;
    private DatabaseHelper dbHelper;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        dbHelper = new DatabaseHelper(this);


        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2)); // Set grid layout with 2 columns


        clothingList = new ArrayList<>();
        adapter = new ClothingAdapter(clothingList, this);
        recyclerView.setAdapter(adapter);

        //Async
        new LoadClothingItemsTask(this, dbHelper).execute();

        setSupportActionBar(findViewById(R.id.toolbar));

        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }


        FloatingActionButton fabCart = findViewById(R.id.fabCart);
        fabCart.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            startActivity(intent);
        });


        adapter.setOnItemClickListener(new ClothingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Clothing selectedClothing = clothingList.get(position);
                Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
                intent.putExtra("clothingName", selectedClothing.getName());
                intent.putExtra("clothingPrice", selectedClothing.getPrice());
                intent.putExtra("clothingImage", selectedClothing.getImageResourceId());
                intent.putExtra("clothingCategory", "tops");
                startActivity(intent);
            }
        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();


                if (id == R.id.nav_home) {
                    startActivity(new Intent(MainActivity.this, MainActivity.class));
                } else if (id == R.id.nav_tops) {
                    startActivity(new Intent(MainActivity.this, TopsActivity.class));
                } else if (id == R.id.nav_jackets) {
                    startActivity(new Intent(MainActivity.this, JacketsActivity.class));
                } else if (id == R.id.nav_bottoms) {
                    startActivity(new Intent(MainActivity.this, BottomsActivity.class));
                } else if (id == R.id.nav_shoes) {
                    startActivity(new Intent(MainActivity.this, ShoesActivity.class));
                } else if (id == R.id.nav_about) {
                    startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
                } else if (id == R.id.nav_contact_us) {
                    startActivity(new Intent(MainActivity.this, ContactUsActivity.class));
                } else if (id == R.id.nav_account_settings) {
                    startActivity(new Intent(MainActivity.this, AccountSettingsActivity.class));
                } else if (id == R.id.nav_logout) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
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

    public void updateClothingList(List<Clothing> clothingItems) {
        clothingList.clear();
        clothingList.addAll(clothingItems);
        adapter.notifyDataSetChanged();
    }


    private void loadClothingItems() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(DatabaseHelper.CLOTHING_TABLE, null, null, null, null, null, null);

            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    int nameIndex = cursor.getColumnIndex(DatabaseHelper.COL_CLOTHING_NAME);
                    int priceIndex = cursor.getColumnIndex(DatabaseHelper.COL_CLOTHING_PRICE);
                    int imageIndex = cursor.getColumnIndex(DatabaseHelper.COL_CLOTHING_IMAGE);

                    if (nameIndex >= 0 && priceIndex >= 0 && imageIndex >= 0) {
                        String name = cursor.getString(nameIndex);
                        double price = cursor.getDouble(priceIndex);
                        int image = cursor.getInt(imageIndex);
                        clothingList.add(new Clothing(name, String.format("R%.2f", price), image));
                    }
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        adapter.notifyDataSetChanged();
    }

}
