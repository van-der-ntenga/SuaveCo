package com.example.suaveco;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;

public class AccountSettingsActivity extends AppCompatActivity {

    private EditText etName, etSurname, etPhone, etEmail, etOldPassword, etNewPassword;
    private ImageView ivOldPasswordToggle, ivNewPasswordToggle;
    private Button btnSave;
    private DatabaseHelper dbHelper;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private Toolbar toolbar;

    private String loggedInUserEmail;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        // Initialize views
        etName = findViewById(R.id.etName);
        etSurname = findViewById(R.id.etSurname);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        etOldPassword = findViewById(R.id.etOldPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        btnSave = findViewById(R.id.btnSave);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ivOldPasswordToggle = findViewById(R.id.ivOldPasswordToggle);
        ivNewPasswordToggle = findViewById(R.id.ivNewPasswordToggle);

        etOldPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        etNewPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

        dbHelper = new DatabaseHelper(this);


        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        loggedInUserEmail = sharedPreferences.getString("email", null);  // null if no email saved

        if (loggedInUserEmail != null) {

            loadUserData(loggedInUserEmail);
        } else {
            Toast.makeText(this, "Error: No user is logged in", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(AccountSettingsActivity.this, LoginActivity.class));
            finish();
        }

        // Save button action
        btnSave.setOnClickListener(v -> {

            String name = etName.getText().toString().trim();
            String surname = etSurname.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String oldPassword = etOldPassword.getText().toString().trim();  // Get old password
            String newPassword = etNewPassword.getText().toString().trim();  // Get new password

            if (validateInput(name, surname, phone, email)) {
                // Update user info in the database (excluding password)
                updateUser(name, surname, phone, email);

                // If both old and new passwords are provided, attempt password change
                if (!oldPassword.isEmpty() && !newPassword.isEmpty()) {
                    if (dbHelper.checkUser(email, dbHelper.encryptPassword(oldPassword))) {

                        updatePassword(email, newPassword);
                    } else {
                        Toast.makeText(this, "Old password is incorrect", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            }
        });


        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();


            if (id == R.id.nav_home) {
                startActivity(new Intent(AccountSettingsActivity.this, MainActivity.class));
            } else if (id == R.id.nav_tops) {
                startActivity(new Intent(AccountSettingsActivity.this, TopsActivity.class));
            } else if (id == R.id.nav_jackets) {
                startActivity(new Intent(AccountSettingsActivity.this, JacketsActivity.class));
            } else if (id == R.id.nav_bottoms) {
                startActivity(new Intent(AccountSettingsActivity.this, BottomsActivity.class));
            } else if (id == R.id.nav_shoes) {
                startActivity(new Intent(AccountSettingsActivity.this, ShoesActivity.class));
            } else if (id == R.id.nav_about) {
                startActivity(new Intent(AccountSettingsActivity.this, AboutUsActivity.class));
            } else if (id == R.id.nav_contact_us) {
                startActivity(new Intent(AccountSettingsActivity.this, ContactUsActivity.class));
            } else if (id == R.id.nav_account_settings) {
                startActivity(new Intent(AccountSettingsActivity.this, AccountSettingsActivity.class));
            } else if (id == R.id.nav_logout) {

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                startActivity(new Intent(AccountSettingsActivity.this, LoginActivity.class));
                finish();
            }
            drawerLayout.closeDrawers();
            return true;
        });
    }


    private void loadUserData(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + DatabaseHelper.USER_TABLE + " WHERE " + DatabaseHelper.COL_EMAIL + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});

        if (cursor.moveToFirst()) {
            etName.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_NAME)));
            etSurname.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_SURNAME)));
            etPhone.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PHONE)));
            etEmail.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_EMAIL)));
        }
        cursor.close();
    }

    private boolean validateInput(String name, String surname, String phone, String email) {
        return !name.isEmpty() && !surname.isEmpty() && !phone.isEmpty() && !email.isEmpty();
    }


    private void updateUser(String name, String surname, String phone, String email) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_NAME, name);
        values.put(DatabaseHelper.COL_SURNAME, surname);
        values.put(DatabaseHelper.COL_PHONE, phone);
        values.put(DatabaseHelper.COL_EMAIL, email);


        int rowsAffected = db.update(DatabaseHelper.USER_TABLE, values, DatabaseHelper.COL_EMAIL + " = ?", new String[]{email});

        if (rowsAffected > 0) {
            Toast.makeText(this, "Account details updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error updating account details", Toast.LENGTH_SHORT).show();
        }
    }


    private void updatePassword(String email, String newPassword) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_PASSWORD, dbHelper.encryptPassword(newPassword));


        int rowsAffected = db.update(DatabaseHelper.USER_TABLE, values, DatabaseHelper.COL_EMAIL + " = ?", new String[]{email});

        if (rowsAffected > 0) {
            Toast.makeText(this, "Password updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error updating password", Toast.LENGTH_SHORT).show();
        }
    }
    private void toggleOldPasswordVisibility() {
        if (isPasswordVisible) {
            // Hide password
            etOldPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            ivOldPasswordToggle.setImageResource(R.drawable.ic_eye); // Eye icon
        } else {
            // Show password
            etOldPassword.setTransformationMethod(null); // Visible password
            ivOldPasswordToggle.setImageResource(R.drawable.ic_eye_off); // Eye-off icon
        }
        isPasswordVisible = !isPasswordVisible;


        etOldPassword.setSelection(etOldPassword.getText().length());
    }
    private void toggleNewPasswordVisibility() {
        if (isPasswordVisible) {
            // Hide password
            etNewPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            ivNewPasswordToggle.setImageResource(R.drawable.ic_eye); // Eye icon
        } else {
            // Show password
            etNewPassword.setTransformationMethod(null); // Visible password
            ivNewPasswordToggle.setImageResource(R.drawable.ic_eye_off); // Eye-off icon
        }
        isPasswordVisible = !isPasswordVisible;

        etNewPassword.setSelection(etNewPassword.getText().length());
    }
}
