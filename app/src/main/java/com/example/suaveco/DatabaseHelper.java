package com.example.suaveco;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database name and version
    private static final String DATABASE_NAME = "boutique.db";
    private static final int DATABASE_VERSION = 2;

    // Tables
    public static final String USER_TABLE = "users";
    public static final String CART_TABLE = "cart";
    public static final String CLOTHING_TABLE = "clothing";  // Clothing table

    // User table columns
    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_SURNAME = "surname";
    public static final String COL_EMAIL = "email";
    public static final String COL_PASSWORD = "password";
    public static final String COL_PHONE = "phone";

    // Cart table columns
    public static final String COL_CART_ID = "cart_id";
    public static final String COL_PRODUCT_NAME = "product_name";
    public static final String COL_PRICE = "price";
    public static final String COL_SIZE = "size";
    public static final String COL_QUANTITY = "quantity";
    public static final String COL_IMAGE_RESOURCE = "image_resource";

    // Clothing table columns
    public static final String COL_CLOTHING_NAME = "name";
    public static final String COL_CLOTHING_PRICE = "price";
    public static final String COL_CLOTHING_CATEGORY = "category";
    public static final String COL_CLOTHING_IMAGE = "image";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create users table
        String CREATE_USER_TABLE = "CREATE TABLE " + USER_TABLE + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT, " +
                COL_SURNAME + " TEXT, " +
                COL_EMAIL + " TEXT UNIQUE, " +
                COL_PASSWORD + " TEXT, " +
                COL_PHONE + " TEXT)";
        db.execSQL(CREATE_USER_TABLE);

        // Create clothing table with category
        String CREATE_CLOTHING_TABLE = "CREATE TABLE " + CLOTHING_TABLE + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_CLOTHING_NAME + " TEXT, " +
                COL_CLOTHING_PRICE + " REAL, " +
                COL_CLOTHING_CATEGORY + " TEXT, " +
                COL_CLOTHING_IMAGE + " INTEGER)";
        db.execSQL(CREATE_CLOTHING_TABLE);

        // Create cart table
        String CREATE_CART_TABLE = "CREATE TABLE " + CART_TABLE + " (" +
                COL_CART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_PRODUCT_NAME + " TEXT, " +
                COL_PRICE + " REAL, " +
                COL_SIZE + " TEXT, " +
                COL_QUANTITY + " INTEGER, " +
                COL_IMAGE_RESOURCE + " INTEGER)";
        db.execSQL(CREATE_CART_TABLE);


        insertInitialClothingItems(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Example: upgrading from version 1 to version 2 to add image_resource column to cart
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + CART_TABLE + " ADD COLUMN " + COL_IMAGE_RESOURCE + " INTEGER DEFAULT 0");
        }


    }


    private void insertInitialClothingItems(SQLiteDatabase db) {

        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + CLOTHING_TABLE, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();

        if (count == 0) {
            // Tops
            insertClothingItem(db, "Black Basic T-Shirt", 199.99, "tops", R.drawable.tshirt_image);
            insertClothingItem(db, "Linen Long-Sleeve Shirt", 599.99, "tops", R.drawable.shirt_image);

            // Jackets
            insertClothingItem(db, "Mombasa Explorer Leather Jacket", 999.99, "jackets", R.drawable.leather_jacket_image);
            insertClothingItem(db, "Off-White Denim Jacket", 749.99, "jackets", R.drawable.denim_jacket_image);
            insertClothingItem(db, "Packable Puffer Jacket", 449.99, "jackets", R.drawable.jacket_image);

            // Bottoms
            insertClothingItem(db, "Straight Leg Jeans", 949.99, "bottoms", R.drawable.jeans_image);
            insertClothingItem(db, "Black Shorts", 249.99, "bottoms", R.drawable.shorts_image);
            insertClothingItem(db, "Woven Cargo Trousers", 1299.99, "bottoms", R.drawable.track_image);

            // Shoes
            insertClothingItem(db, "Nike Air Force I", 499.99, "shoes", R.drawable.sneakers_image);
            insertClothingItem(db, "Burgundy Tassel Slip On Shoe", 699.99, "shoes", R.drawable.formal_shoes_image);
        }
    }


    private void insertClothingItem(SQLiteDatabase db, String name, double price, String category, int imageResourceId) {
        ContentValues values = new ContentValues();
        values.put(COL_CLOTHING_NAME, name);
        values.put(COL_CLOTHING_PRICE, price);
        values.put(COL_CLOTHING_CATEGORY, category);
        values.put(COL_CLOTHING_IMAGE, imageResourceId);
        db.insert(CLOTHING_TABLE, null, values);
    }


    public List<Clothing> getClothingItems() {
        List<Clothing> clothingList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(CLOTHING_TABLE, null, null, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_CLOTHING_NAME));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_CLOTHING_PRICE));
                String category = cursor.getString(cursor.getColumnIndexOrThrow(COL_CLOTHING_CATEGORY));
                int image = cursor.getInt(cursor.getColumnIndexOrThrow(COL_CLOTHING_IMAGE));
                clothingList.add(new Clothing(name, String.format("R%.2f", price), image));
            }
            cursor.close();
        }
        return clothingList;
    }


    public List<Clothing> getClothingItemsByCategory(String category) {
        List<Clothing> clothingList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(CLOTHING_TABLE, null, COL_CLOTHING_CATEGORY + "=?", new String[]{category}, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_CLOTHING_NAME));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_CLOTHING_PRICE));
                int image = cursor.getInt(cursor.getColumnIndexOrThrow(COL_CLOTHING_IMAGE));
                clothingList.add(new Clothing(name, String.format("R%.2f", price), image));
            }
            cursor.close();
        }
        return clothingList;
    }


    public void addToCart(String productName, double price, String size, int quantity, int imageResourceId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PRODUCT_NAME, productName);
        values.put(COL_PRICE, price);
        values.put(COL_SIZE, size);
        values.put(COL_QUANTITY, quantity);
        values.put(COL_IMAGE_RESOURCE, imageResourceId);
        db.insert(CART_TABLE, null, values);
        db.close();
    }

    public Cursor getCartItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(CART_TABLE, new String[]{COL_PRODUCT_NAME, COL_PRICE, COL_SIZE, COL_QUANTITY, COL_IMAGE_RESOURCE}, null, null, null, null, null);
    }


    public void clearCart() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CART_TABLE, null, null);  // Delete all records from the cart table
        db.close();
    }

    public void addUser(String name, String surname, String email, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_SURNAME, surname);
        values.put(COL_EMAIL, email);
        values.put(COL_PHONE, phone);
        db.insert(USER_TABLE, null, values);
    }


    public void addUserProfile(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PASSWORD, password);
        db.update(USER_TABLE, values, COL_EMAIL + "=?", new String[]{email});
    }


    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + USER_TABLE + " WHERE " + COL_EMAIL + "=? AND " + COL_PASSWORD + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{email, password});

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    public String encryptPassword(String password) {
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
}
