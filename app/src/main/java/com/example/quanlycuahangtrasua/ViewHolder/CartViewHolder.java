package com.example.quanlycuahangtrasua.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlycuahangtrasua.Interface.ItemClickListener;
import com.example.quanlycuahangtrasua.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView cartOrderDayOrName, cartOrderTimeOrQuality, orderTotalPrice, status;
    private ItemClickListener itemClickListener;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        cartOrderDayOrName = (TextView)itemView.findViewById(R.id.order_day_or_name);
        orderTotalPrice = (TextView)itemView.findViewById(R.id.order_total);
        cartOrderTimeOrQuality = (TextView)itemView.findViewById(R.id.order_time_or_quality);
        status = (TextView)itemView.findViewById(R.id.status);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}

