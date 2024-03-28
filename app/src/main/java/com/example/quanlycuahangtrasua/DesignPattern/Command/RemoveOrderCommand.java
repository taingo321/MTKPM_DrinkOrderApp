package com.example.quanlycuahangtrasua.DesignPattern.Command;

import com.example.quanlycuahangtrasua.AdminOrderActivity;
import com.example.quanlycuahangtrasua.Model.Orders;

public class RemoveOrderCommand implements Command{
    private AdminOrderActivity adminOrderActivity;
    private String uID;

    public RemoveOrderCommand(AdminOrderActivity adminOrderActivity,String uID) {
        this.adminOrderActivity = adminOrderActivity;
        this.uID = uID;
    }

    @Override
    public void execute() {
        adminOrderActivity.removeOrder(uID);
    }
}
