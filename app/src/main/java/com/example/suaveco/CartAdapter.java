package com.example.suaveco;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItem> cartItemList;
    private Context context;
    private OnItemRemoveListener removeListener;

    public CartAdapter(List<CartItem> cartItemList, Context context, OnItemRemoveListener removeListener) {
        this.cartItemList = cartItemList;
        this.context = context;
        this.removeListener = removeListener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = cartItemList.get(position);
        holder.tvProductName.setText(cartItem.getProductName());
        holder.tvProductPrice.setText(String.format("R%.2f", cartItem.getPrice())); // ZAR Currency
        holder.tvProductSize.setText("Size: " + cartItem.getSize());
        holder.tvProductQuantity.setText("Qty: " + cartItem.getQuantity());

        holder.cartProductImage.setImageResource(cartItem.getImageResourceId());

        holder.btnRemoveItem.setOnClickListener(v -> {
            if (removeListener != null) {
                removeListener.onItemRemove(cartItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {

        TextView tvProductName, tvProductPrice, tvProductSize, tvProductQuantity;
        ImageView cartProductImage;
        Button btnRemoveItem;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvProductSize = itemView.findViewById(R.id.tvProductSize);
            tvProductQuantity = itemView.findViewById(R.id.tvProductQuantity);
            cartProductImage = itemView.findViewById(R.id.cartProductImage);
            btnRemoveItem = itemView.findViewById(R.id.btnRemoveItem);
        }
    }
    public interface OnItemRemoveListener {
        void onItemRemove(CartItem cartItem);
    }
}
