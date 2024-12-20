package com.example.suaveco;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnItemRemoveListener {

    private RecyclerView recyclerCart;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItemList;
    private Button btnCheckout;
    private TextView tvCartSummary;
    private DatabaseHelper dbHelper;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerCart = findViewById(R.id.recyclerCart);
        btnCheckout = findViewById(R.id.btnCheckout);
        tvCartSummary = findViewById(R.id.tvCartSummary);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        dbHelper = new DatabaseHelper(this);

        // Set up RecyclerView
        recyclerCart.setLayoutManager(new LinearLayoutManager(this));
        cartItemList = new ArrayList<>();
        loadCartItems();

        setSupportActionBar(findViewById(R.id.toolbar));

        cartAdapter = new CartAdapter(cartItemList, this, this);
        recyclerCart.setAdapter(cartAdapter);

        updateCartSummary();

        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_home) {
                    startActivity(new Intent(CartActivity.this, MainActivity.class));
                } else if (id == R.id.nav_tops) {
                    startActivity(new Intent(CartActivity.this, TopsActivity.class));
                } else if (id == R.id.nav_jackets) {
                    startActivity(new Intent(CartActivity.this, JacketsActivity.class));
                } else if (id == R.id.nav_bottoms) {
                    startActivity(new Intent(CartActivity.this, BottomsActivity.class));
                } else if (id == R.id.nav_shoes) {
                    startActivity(new Intent(CartActivity.this, ShoesActivity.class));
                } else if (id == R.id.nav_about) {
                    startActivity(new Intent(CartActivity.this, AboutUsActivity.class));
                } else if (id == R.id.nav_contact_us) {
                    startActivity(new Intent(CartActivity.this, ContactUsActivity.class));
                } else if (id == R.id.nav_account_settings) {
                    startActivity(new Intent(CartActivity.this, AccountSettingsActivity.class));
                } else if (id == R.id.nav_logout) {
                    startActivity(new Intent(CartActivity.this, LoginActivity.class));
                    finish();
                }

                drawerLayout.closeDrawers();
                return true;
            }
        });

        // Checkout button
        btnCheckout.setOnClickListener(v -> {
            Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
            startActivity(intent);
        });
    }

    private void loadCartItems() {
        Cursor cursor = dbHelper.getCartItems();

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int productNameIndex = cursor.getColumnIndex(DatabaseHelper.COL_PRODUCT_NAME);
                int priceIndex = cursor.getColumnIndex(DatabaseHelper.COL_PRICE);
                int sizeIndex = cursor.getColumnIndex(DatabaseHelper.COL_SIZE);
                int quantityIndex = cursor.getColumnIndex(DatabaseHelper.COL_QUANTITY);
                int imageResourceIndex = cursor.getColumnIndex("image_resource"); // Image resource column


                if (productNameIndex >= 0 && priceIndex >= 0 && sizeIndex >= 0 && quantityIndex >= 0 && imageResourceIndex >= 0) {
                    String productName = cursor.getString(productNameIndex);
                    double price = cursor.getDouble(priceIndex);
                    String size = cursor.getString(sizeIndex);
                    int quantity = cursor.getInt(quantityIndex);
                    int imageResourceId = cursor.getInt(imageResourceIndex); // Retrieve actual image resource


                    cartItemList.add(new CartItem(productName, price, size, quantity, imageResourceId));
                }
            }
            cursor.close();
        }
    }


    private void updateCartSummary() {
        double totalAmount = 0;
        int totalQuantity = 0;

        for (CartItem item : cartItemList) {
            totalAmount += item.getPrice() * item.getQuantity();
            totalQuantity += item.getQuantity();
        }

        tvCartSummary.setText(String.format("Total: R%.2f, Items: %d", totalAmount, totalQuantity));
    }


    @Override
    public void onItemRemove(CartItem cartItem) {
        cartItemList.remove(cartItem);
        cartAdapter.notifyDataSetChanged();
        updateCartSummary();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
