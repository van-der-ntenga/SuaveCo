package com.example.suaveco;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class CheckoutActivity extends AppCompatActivity {

    private EditText etHouseNumber, etStreetName, etTown, etProvince, etPostalCode, etCardNumber, etCvv, etExpiryDate;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        etHouseNumber = findViewById(R.id.etHouseNumber);
        etStreetName = findViewById(R.id.etStreetName);
        etTown = findViewById(R.id.etTown);
        etProvince = findViewById(R.id.etProvince);
        etPostalCode = findViewById(R.id.etPostalCode);
        etCardNumber = findViewById(R.id.etCardNumber);
        etCvv = findViewById(R.id.etCvv);
        etExpiryDate = findViewById(R.id.etExpiryDate);

        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigation_view);
        setSupportActionBar(toolbar);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        findViewById(R.id.btnConfirmCheckout).setOnClickListener(v -> validateAndCheckout());

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                startActivity(new Intent(CheckoutActivity.this, MainActivity.class));
            } else if (id == R.id.nav_tops) {
                startActivity(new Intent(CheckoutActivity.this, TopsActivity.class));
            } else if (id == R.id.nav_jackets) {
                startActivity(new Intent(CheckoutActivity.this, JacketsActivity.class));
            } else if (id == R.id.nav_bottoms) {
                startActivity(new Intent(CheckoutActivity.this, BottomsActivity.class));
            } else if (id == R.id.nav_shoes) {
                startActivity(new Intent(CheckoutActivity.this, ShoesActivity.class));
            } else if (id == R.id.nav_about) {
                startActivity(new Intent(CheckoutActivity.this, AboutUsActivity.class));
            } else if (id == R.id.nav_contact_us) {
                startActivity(new Intent(CheckoutActivity.this, ContactUsActivity.class));
            } else if (id == R.id.nav_account_settings) {
                startActivity(new Intent(CheckoutActivity.this, AccountSettingsActivity.class));
            } else if (id == R.id.nav_logout) {
                startActivity(new Intent(CheckoutActivity.this, LoginActivity.class));
                finish();
            }

            drawerLayout.closeDrawers();
            return true;
        });
    }

    private void validateAndCheckout() {
        String houseNumber = etHouseNumber.getText().toString().trim();
        String streetName = etStreetName.getText().toString().trim();
        String town = etTown.getText().toString().trim();
        String province = etProvince.getText().toString().trim();
        String postalCode = etPostalCode.getText().toString().trim();
        String cardNumber = etCardNumber.getText().toString().trim();
        String cvv = etCvv.getText().toString().trim();
        String expiryDate = etExpiryDate.getText().toString().trim();

        if (TextUtils.isEmpty(houseNumber) || TextUtils.isEmpty(streetName) || TextUtils.isEmpty(town) ||
                TextUtils.isEmpty(province) || TextUtils.isEmpty(postalCode) ||
                TextUtils.isEmpty(cardNumber) || TextUtils.isEmpty(cvv) || TextUtils.isEmpty(expiryDate)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        } else {

            DatabaseHelper dbHelper = new DatabaseHelper(this);
            dbHelper.clearCart();

            Toast.makeText(this, "Checkout Successful", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(CheckoutActivity.this, OrderSummaryActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
