package com.example.quanlycuahangtrasua.DesignPattern.Composite.Interface;

import androidx.recyclerview.widget.RecyclerView;

public interface IComposite {
    void display(RecyclerView.ViewHolder viewHolder, String uid);
}
