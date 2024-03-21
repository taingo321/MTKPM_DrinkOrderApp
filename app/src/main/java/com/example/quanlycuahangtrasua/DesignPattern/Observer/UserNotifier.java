package com.example.quanlycuahangtrasua.DesignPattern.Observer;

public class UserNotifier implements IOrderObserver{
    @Override
    public void update(String status) {
        System.out.println("Trạng thái của đơn hàng đã thay đổi thành: " + status);
    }
}
