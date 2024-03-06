package com.example.quanlycuahangtrasua.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlycuahangtrasua.Interface.ItemClickListener;
import com.example.quanlycuahangtrasua.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView productName, productIngre, productPrice;
    public ImageView productImage;
    public ItemClickListener listener;
    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        productImage = (ImageView) itemView.findViewById(R.id.productImage);
        productName = (TextView) itemView.findViewById(R.id.productName);
        productIngre = (TextView) itemView.findViewById(R.id.productIngre);
        productPrice = (TextView) itemView.findViewById(R.id.productPrice);
    }

    public void setItemClickListener(ItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v, getAdapterPosition(), false);
    }
}
