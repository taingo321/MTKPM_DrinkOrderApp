package com.example.quanlycuahangtrasua.DesignPattern.Command;

import java.util.ArrayList;
import java.util.List;

public class CommandInvoker {
    private Command command;
    public void setCommand(Command command) {
        this.command = command;
    }
    public void executeCommand(){
        command.execute();
    }
}
