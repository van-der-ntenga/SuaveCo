package com.example.suaveco;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;

public class ProductDetailActivity extends AppCompatActivity {

    private TextView productName, productPrice, tvQuantity;
    private Spinner sizeSpinner;
    private Button btnAddToCart, btnMinus, btnPlus;
    private ImageView productImage;
    private String name, category;
    private double price;
    private int imageResId;
    private int quantity = 1;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);


        name = getIntent().getStringExtra("clothingName");
        price = Double.parseDouble(getIntent().getStringExtra("clothingPrice").replace("R", ""));
        imageResId = getIntent().getIntExtra("clothingImage", 0);
        category = getIntent().getStringExtra("clothingCategory");

        productName = findViewById(R.id.productName);
        productPrice = findViewById(R.id.productPrice);
        tvQuantity = findViewById(R.id.tvQuantity);
        sizeSpinner = findViewById(R.id.sizeSpinner);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        btnMinus = findViewById(R.id.btnMinus);
        btnPlus = findViewById(R.id.btnPlus);
        productImage = findViewById(R.id.productImage);


        productName.setText(name);
        productPrice.setText(String.format("R%.2f", price));
        tvQuantity.setText(String.valueOf(quantity));
        productImage.setImageResource(imageResId);

        setSupportActionBar(findViewById(R.id.toolbar));

        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        loadSizeOptions(category);


        btnMinus.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                tvQuantity.setText(String.valueOf(quantity));
            }
        });

        btnPlus.setOnClickListener(v -> {
            quantity++;
            tvQuantity.setText(String.valueOf(quantity));
        });


        btnAddToCart.setOnClickListener(v -> {
            String selectedSize = sizeSpinner.getSelectedItem().toString();
            DatabaseHelper dbHelper = new DatabaseHelper(ProductDetailActivity.this);
            dbHelper.addToCart(name, price, selectedSize, quantity, imageResId);
            Toast.makeText(ProductDetailActivity.this, "Item added to cart", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(ProductDetailActivity.this, MainActivity.class);
            startActivity(intent);

            finish();
        });


        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                startActivity(new Intent(ProductDetailActivity.this, MainActivity.class));
            } else if (id == R.id.nav_tops) {
                startActivity(new Intent(ProductDetailActivity.this, TopsActivity.class));
            } else if (id == R.id.nav_jackets) {
                startActivity(new Intent(ProductDetailActivity.this, JacketsActivity.class));
            } else if (id == R.id.nav_bottoms) {
                startActivity(new Intent(ProductDetailActivity.this, BottomsActivity.class));
            } else if (id == R.id.nav_shoes) {
                startActivity(new Intent(ProductDetailActivity.this, ShoesActivity.class));
            } else if (id == R.id.nav_about) {
                startActivity(new Intent(ProductDetailActivity.this, AboutUsActivity.class));
            } else if (id == R.id.nav_contact_us) {
                startActivity(new Intent(ProductDetailActivity.this, ContactUsActivity.class));
            } else if (id == R.id.nav_account_settings) {
                startActivity(new Intent(ProductDetailActivity.this, AccountSettingsActivity.class));
            } else if (id == R.id.nav_logout) {
                startActivity(new Intent(ProductDetailActivity.this, LoginActivity.class));
                finish();
            }

            drawerLayout.closeDrawers();
            return true;
        });
    }

    private void loadSizeOptions(String category) {
        ArrayAdapter<CharSequence> sizeAdapter;
        switch (category.toLowerCase()) {
            case "shoes":
                sizeAdapter = ArrayAdapter.createFromResource(this, R.array.sizes_shoes, android.R.layout.simple_spinner_item);
                break;
            case "tops":
                sizeAdapter = ArrayAdapter.createFromResource(this, R.array.sizes_tops, android.R.layout.simple_spinner_item);
                break;
            case "bottoms":
                sizeAdapter = ArrayAdapter.createFromResource(this, R.array.sizes_bottoms, android.R.layout.simple_spinner_item);
                break;
            default:
                sizeAdapter = ArrayAdapter.createFromResource(this, R.array.sizes_tops, android.R.layout.simple_spinner_item); // Default to tops
                break;
        }
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sizeSpinner.setAdapter(sizeAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
