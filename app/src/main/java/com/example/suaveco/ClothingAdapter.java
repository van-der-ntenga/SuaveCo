package com.example.suaveco;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ClothingAdapter extends RecyclerView.Adapter<ClothingAdapter.ClothingViewHolder> {

    private List<Clothing> clothingList;
    private Context context;
    private OnItemClickListener listener;


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public ClothingAdapter(List<Clothing> clothingList, Context context) {
        this.clothingList = clothingList;
        this.context = context;
    }

    @NonNull
    @Override
    public ClothingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_clothing, parent, false);
        return new ClothingViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ClothingViewHolder holder, int position) {
        Clothing clothing = clothingList.get(position);
        holder.clothingName.setText(clothing.getName());
        holder.clothingPrice.setText(clothing.getPrice());
        holder.clothingImage.setImageResource(clothing.getImageResourceId());
    }

    @Override
    public int getItemCount() {
        return clothingList.size();
    }

    public static class ClothingViewHolder extends RecyclerView.ViewHolder {
        TextView clothingName, clothingPrice;
        ImageView clothingImage;

        public ClothingViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            clothingName = itemView.findViewById(R.id.clothingName);
            clothingPrice = itemView.findViewById(R.id.clothingPrice);
            clothingImage = itemView.findViewById(R.id.clothingImage);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getBindingAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }
}
