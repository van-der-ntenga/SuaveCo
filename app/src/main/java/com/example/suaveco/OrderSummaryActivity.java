package com.example.suaveco;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.Random;

public class OrderSummaryActivity extends AppCompatActivity {

    private TextView tvOrderNumber;
    private DrawerLayout drawerLayout;
    private Button btnDone;
    private DatabaseHelper dbHelper;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);

        drawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvOrderNumber = findViewById(R.id.tvOrderNumber);
        btnDone = findViewById(R.id.btnDone);

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


        String orderNumber = generateOrderNumber();
        tvOrderNumber.setText("Order Number: " + orderNumber);


        dbHelper = new DatabaseHelper(this);

        btnDone.setOnClickListener(v -> {

            dbHelper.clearCart();
            Intent intent = new Intent(OrderSummaryActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });


        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                startActivity(new Intent(OrderSummaryActivity.this, MainActivity.class));
            } else if (id == R.id.nav_logout) {
                startActivity(new Intent(OrderSummaryActivity.this, LoginActivity.class));
                finish();
            }

            drawerLayout.closeDrawers();
            return true;
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private String generateOrderNumber() {
        Random random = new Random();
        int orderNumber = 100000 + random.nextInt(900000);
        return String.valueOf(orderNumber);
    }
}
