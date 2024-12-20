package com.example.suaveco;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

public class RegisterActivity extends AppCompatActivity {
    private EditText etName, etSurname, etEmail, etPassword, etPhone;
    private Button btnRegister;
    private CheckBox cbTerms;
    private ImageView ivPasswordToggle;
    private DatabaseHelper dbHelper;

    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new DatabaseHelper(this);

        etName = findViewById(R.id.etName);
        etSurname = findViewById(R.id.etSurname);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etPhone = findViewById(R.id.etPhone);
        btnRegister = findViewById(R.id.btnRegister);
        cbTerms = findViewById(R.id.cbTerms);
        ivPasswordToggle = findViewById(R.id.ivPasswordToggle);

        etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

        btnRegister.setOnClickListener(v -> registerUser());

        ivPasswordToggle.setOnClickListener(v -> togglePasswordVisibility());
    }

    private void registerUser() {
        String name = etName.getText().toString().trim();
        String surname = etSurname.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        boolean termsAccepted = cbTerms.isChecked();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(surname) || TextUtils.isEmpty(email) ||
                TextUtils.isEmpty(password) || TextUtils.isEmpty(phone) || !termsAccepted) {
            Toast.makeText(this, "Please fill all fields and accept terms.", Toast.LENGTH_SHORT).show();
        } else {
            // Hash the password
            String hashedPassword = encryptPassword(password);
            dbHelper.addUser(name, surname, email, phone);
            dbHelper.addUserProfile(email, hashedPassword);
            Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private String encryptPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            // Hide password
            etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            ivPasswordToggle.setImageResource(R.drawable.ic_eye); // Eye icon
        } else {
            // Show password
            etPassword.setTransformationMethod(null);
            ivPasswordToggle.setImageResource(R.drawable.ic_eye_off);
        }
        isPasswordVisible = !isPasswordVisible;

        etPassword.setSelection(etPassword.getText().length());
    }
}
