package com.epam.callcenter.entity;

/**
 * This entity class describes operator
 */
public class Operator {
    private String name;
    private boolean isAvailable;

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public Operator(String name) {
        this.name = name;
        isAvailable = true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
