package com.example.quanlycuahangtrasua.DesignPattern.Composite;

import com.example.quanlycuahangtrasua.DesignPattern.Composite.Interface.IComposite;

import java.util.ArrayList;
import java.util.List;

public class Composite implements IComposite {
    private List<IComposite> components;

    public Composite() {
        this.components = new ArrayList<>();
    }

    public void addComponent(IComposite component) {
        components.add(component);
    }

    public void removeComponent(IComposite component) {
        components.remove(component);
    }
    @Override
    public void display() {
        for (IComposite component : components) {
            component.display();
        }
    }




}
