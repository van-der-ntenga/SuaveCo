package com.example.suaveco;

import android.os.AsyncTask;
import android.widget.Toast;
import java.util.List;

class LoadClothingItemsTask extends AsyncTask<Void, Void, List<Clothing>> {

    private MainActivity activity; //
    private DatabaseHelper dbHelper;


    public LoadClothingItemsTask(MainActivity activity, DatabaseHelper dbHelper) {
        this.activity = activity;
        this.dbHelper = dbHelper;
    }

    @Override
    protected List<Clothing> doInBackground(Void... voids) {
        return dbHelper.getClothingItems();
    }


    @Override
    protected void onPostExecute(List<Clothing> clothingList) {
        if (activity == null || activity.isFinishing()) {
            return;
        }

        if (clothingList != null && !clothingList.isEmpty()) {
            activity.updateClothingList(clothingList);
        } else {
            Toast.makeText(activity, "No items found", Toast.LENGTH_SHORT).show();
        }
    }
}
