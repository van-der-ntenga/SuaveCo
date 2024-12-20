package com.example.suaveco;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;



public class ContactUsActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        setSupportActionBar(findViewById(R.id.toolbar));

        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_home) {
                    startActivity(new Intent(ContactUsActivity.this, MainActivity.class));
                } else if (id == R.id.nav_tops) {
                    startActivity(new Intent(ContactUsActivity.this, TopsActivity.class));
                } else if (id == R.id.nav_jackets) {
                    startActivity(new Intent(ContactUsActivity.this, JacketsActivity.class));
                } else if (id == R.id.nav_bottoms) {
                    startActivity(new Intent(ContactUsActivity.this, BottomsActivity.class));
                } else if (id == R.id.nav_shoes) {
                    startActivity(new Intent(ContactUsActivity.this, ShoesActivity.class));
                } else if (id == R.id.nav_about) {
                    startActivity(new Intent(ContactUsActivity.this, AboutUsActivity.class));
                } else if (id == R.id.nav_contact_us) {
                    startActivity(new Intent(ContactUsActivity.this, ContactUsActivity.class));
                } else if (id == R.id.nav_account_settings) {
                    startActivity(new Intent(ContactUsActivity.this, AccountSettingsActivity.class));
                } else if (id == R.id.nav_logout) {
                    startActivity(new Intent(ContactUsActivity.this, LoginActivity.class));
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
